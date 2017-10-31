
import java.io.File
import java.nio.file.{Files, Path, Paths}
import java.util.UUID
import java.nio.charset.StandardCharsets.UTF_8

import scala.meta._

/*
import sbt._
import sbt.Keys.{unmanagedSources, _}
import sbt.plugins.JvmPlugin

object C4GeneratorPlugin extends AutoPlugin {
  override def requires = JvmPlugin
  override lazy val projectSettings = Seq(
    sourceGenerators in Compile += Def.task {
      println(("AA1",(unmanagedSources in Compile).value))
      val toDir = (sourceManaged in Compile).value.toPath
      Files.createDirectories(toDir)
      for {
        path ← (unmanagedSources in Compile).value.map(_.toPath)
        if !path.toString.contains("-macros/")
      } yield {
        val toFile = toDir.resolve(path.getFileName).toFile
        val out = Generator.genPackage(path.toFile).mkString("\n")

        IO.write(toFile, out)
        toFile
      }
    }.taskValue
  )
}
*/
/*
object Main {
  def main(args: Array[String]): Unit = {
    val files = DirInfoImpl.deepFiles(Paths.get(".."))
      .filter(_.toString.endsWith(".scala"))
      .filterNot(_.toString.contains("-macros/"))
    files.foreach{ path ⇒
      val res = Generator.genPackage(path.toFile)
      println(res)
    }
  }
}
*/

object Main {
  private def version = Array[Byte](0)
  private def getToPath(path: Path): Option[Path] = path.getFileName.toString match {
    case "scala" ⇒ Option(path.resolveSibling("java"))
    case name ⇒ Option(path.getParent).flatMap(getToPath).map(_.resolve(name))
  }
  def main(args: Array[String]): Unit = {
    val files = DirInfoImpl.deepFiles(Paths.get(args(0)))
      .filter(_.toString.endsWith(".scala"))
      .filterNot(_.toString.contains("-macros/"))

    val keep = (for {
      path ← files
      toParentPath ← getToPath(path.getParent)
      data = Files.readAllBytes(path)
      content = new String(data,UTF_8) if content contains "@c4"
      uuid = UUID.nameUUIDFromBytes(data ++ version)
      toPath = toParentPath.resolve(s"c4gen.$uuid.scala")
    } yield {
      if(Files.notExists(toPath)) {
        println(s"generating $path --> $toPath")
        Files.createDirectories(toParentPath)
        Files.write(toPath, Generator.genPackage(content).mkString("\n").getBytes(UTF_8))
      }
      toPath
    }).toSet

    for {
      path ← files if path.toString.contains("/c4gen.") && !keep(path)
    } {
      println(s"removing $path")
      Files.delete(path)
    }
  }
}


object Generator {
  type ArgPF = PartialFunction[(Type,Option[Term]),Option[(Term,Option[Stat])]]

  def typeTree: PartialFunction[Type,Term] = {
    case tp@Type.Name(nm) ⇒ q"classOf[$tp].getName"
    case t"$tp[..$innerTypes]" ⇒
      val inner = innerTypes.map(typeTree).reduce((a,b)⇒q"$a + $b")
      q"classOf[$tp].getName + '[' + $inner + ']'"
  }

  def c4key: ArgPF = {
    case (t"$tpe[..$innerTypes] @c4key",None) ⇒
      val nArgs = innerTypes.map(i ⇒ q"(${Lit.String(s"$i")},${typeTree(i)})")
      Option((q"${Term.Name(s"the ${tpe}Factory")}.forTypes(...${List(nArgs)})",None))
  }

  def prodLens: ArgPF = {
    case (_,Some(q"$o.of(...$args)")) ⇒
      val List(head :: tail) = args
      val q"_.$field" = head
      val nArgs = List(head :: q"value⇒model⇒model.copy($field=value)" :: Lit.String(s"$field") :: tail)
      Option((q"$o.ofSet(...$nArgs)",None))
  }

  def defaultArgType: ArgPF = {
    case (tpe,Some(_)) ⇒ None
    case (tpe,None) ⇒
      val nm = Term.Name(s"the $tpe")
      Option((nm,Option(q"def $nm: $tpe")))
  }


  def classComponent: PartialFunction[Tree,(Boolean,Stat)] = {
    case q"@c4component ..$mods class $cl(...$paramsList) extends ..$ext { ..$stats }" ⇒
      lazy val needsList = for {
        params ← paramsList
      } yield for {
        param"..$mods $name: ${Some(tpe)} = $expropt" ← params
        r ← c4key.orElse(prodLens).orElse(defaultArgType)((tpe,expropt))
      } yield r
      lazy val needParamsList = for { needs ← needsList }
        yield for { (param,_) ← needs } yield param
      lazy val concreteStatement = q"${Term.Name(s"$cl")}(...$needParamsList)"
      lazy val needStms = for {
        needs ← needsList
        (_,stmOpt) ← needs
        stm ← stmOpt
      } yield stm
      lazy val listName = s"the List[$abstractName]"
      lazy val listTerm = Term.Name(listName)
      lazy val listType = Type.Name(listName)
      lazy val abstractName = if(isAbstract) s"$cl" else {
        val init"${Type.Name(nm)}(...$_)" :: Nil = ext
        nm
      }
      lazy val isAbstract = mods.collectFirst{ case mod"abstract" ⇒ true }.nonEmpty
      val isCase = mods.collectFirst{ case mod"case" ⇒ true }.nonEmpty
      val isListed = mods.collectFirst{ case mod"@listed" ⇒ true }.nonEmpty
      val mixType = Type.Name(s"The $cl")
      val resStatement = (isAbstract,isCase,isListed) match {
        case (false,true,true) ⇒
          val concreteTerm = Term.Name(s"the $cl")

          val statements =
            q"private lazy val ${Pat.Var(concreteTerm)} = $concreteStatement" ::
              q"override def $listTerm: $listType = $concreteTerm :: super.$listTerm " ::
              needStms
          val init = Init(Type.Name(s"The $abstractName"), Name(""), Nil) // q"".structure
          q"trait $mixType extends $init { ..$statements }"
        case (false,true,false) ⇒
          val statements =
            q"lazy val ${Pat.Var(Term.Name(s"the $abstractName"))}: ${Type.Name(s"the $abstractName")} = $concreteStatement" ::
              needStms
          q"trait $mixType { ..$statements }"
        case (true,false,true) ⇒
          q"trait $mixType { def $listTerm: $listType = Nil }"
        case _ ⇒ throw new Exception
      }
      (true,resStatement)
  }

  def importForComponents: PartialFunction[Tree,(Boolean,Stat)] = {
    case q"import ..$s" ⇒ (false,q"import ..$s")
  }

  lazy val componentCases: PartialFunction[Tree,(Boolean,Stat)] =
    importForComponents.orElse(classComponent)

  def genStatements: List[Stat] ⇒ Option[List[Stat]] = packageStatements ⇒
    Option(packageStatements.collect(componentCases).reverse.dropWhile(!_._1).reverseMap(_._2))
      .filter(_.nonEmpty)


  def genPackage(content: String): List[Pkg] = {
    val source = dialects.Scala211(content).parse[Source]
    val Parsed.Success(source"..$sourceStatements") = source
    for {
      q"package $n { ..$packageStatements }" ← sourceStatements.toList
      statements ← genStatements(packageStatements.toList)
    } yield q"package $n { ..$statements }"
  }


}


/*
features:
  repeat package/imports
  pass from app, no pass default
  single class | listed class | listed trait
  ProdLens
  index access
todo: integrate, (Getter,assemble,protocol)
problem:
  factory:
  - using (A,B)=>C is not good -- A & B are not named;
    so we need factory interface, it'll be in far file;
    so we need factory implementation;
    and we can skip not much;
  - we can use Inj[A] in place and replace by `Inj[A]`;
    so we skip args;
    but eithter loose debug with macro;
    or copy all code and compile 2 times
    !comment by macro, and generate 2nd

* */


object FinallyClose {
  def apply[A<:AutoCloseable,R](o: A)(f: A⇒R): R = try f(o) finally o.close()
  def apply[A,R](close: A⇒Unit)(o: A)(f: A⇒R): R = try f(o) finally close(o)
}


import scala.collection.JavaConverters.iterableAsScalaIterableConverter

trait DirInfo {
  def deepFiles(path: Path): List[Path]
}

object DirInfoImpl extends DirInfo {
  def sortedList(dir: Path): List[Path] =
    FinallyClose(Files.newDirectoryStream(dir))(_.asScala.toList).sorted
  def deepFiles(path: Path): List[Path] = {
    if(!Files.exists(path)) Nil
    else if(Files.isDirectory(path)) sortedList(path).flatMap(deepFiles)
    else List(path) //Files.isRegularFile(path)
  }
}