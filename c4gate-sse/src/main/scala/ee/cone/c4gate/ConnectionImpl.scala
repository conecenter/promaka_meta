package ee.cone.c4gate

import java.util.UUID

import ee.cone.c4actor.Types._
import ee.cone.c4actor._
import ee.cone.c4gate.InternetProtocol.{TcpConnection, TcpWrite}

class SSETcpStatusHandler(
  sseMessages: SSEMessages
) {
  def mapMessage(res: WorldTx, message: LEvent[TcpConnection]): WorldTx = {
    if(!changing(mClass,res,message)) res
    val toSend = message.value.map(_⇒sseMessages.header(message.srcId)).toSeq
    res.add(toSend:_*).add(message)
  }
  def changing[M](cl: Class[M], res: WorldTx, message: LEvent[M]): Boolean =
    message.value.toList == By.srcId(cl).of(res.world).getOrElse(message.srcId,Nil).toList
}






case class SSEMessages(allowOriginOption: Option[String], needWorldOffset: Long)(reducer: Reducer) extends Observer {
  private def header(connectionKey: String): TcpWrite = {
    val allowOrigin =
      allowOriginOption.map(v=>s"Access-Control-Allow-Origin: $v\n").getOrElse("")
    val headerString = s"HTTP/1.1 200 OK\nContent-Type: text/event-stream\n$allowOrigin\n"
    message(connectionKey, headerString)
  }
  def message(connectionKey: String, event: String, data: String): TcpWrite = {
    val escapedData = data.replaceAllLiterally("\n","\ndata: ")
    message(connectionKey, s"event: $event\ndata: $escapedData\n\n")
  }
  private def message(connectionKey: String, data: String): TcpWrite = {
    val bytes = okio.ByteString.encodeUtf8(data)
    val key = UUID.randomUUID.toString
    TcpWrite(key,connectionKey,bytes)
  }
  def activate(tx: ⇒ WorldTx): WorldTx = {
    val time = System.currentTimeMillis()
    val events = By.srcId(classOf[SSEConnection]).of(tx.world).values.flatten.flatMap{ conn ⇒
      conn.state match{
        case None ⇒
          LEvent.update(conn.connectionKey,SSEConnectionState(conn.connectionKey,time,0)) ::
          LEvent.update(???, header(conn.connectionKey)) ::
          LEvent.update(???, message("connect",conn.connectionKey)) ::
          Nil
        case Some(state@SSEConnectionState(_,pingTime,pongTime)) ⇒
          if(Math.max(pingTime,pongTime) + 5000 > time) Nil
          else if(pingTime < pongTime)
            LEvent.update(conn.connectionKey,state.copy(pingTime=time)) ::
            LEvent.update(???, message("ping",conn.connectionKey)) ::
            Nil
          else
            LEvent.delete(conn.connectionKey,classOf[TcpConnection]) ::
            LEvent.delete(conn.connectionKey,classOf[SSEConnectionState]) ::
            Nil
      }
    }.toSeq
    tx.add(events:_*)
  }
}



case class SSEConnectionState(connectionKey: String, pingTime: Long, pongTime: Long)
case class SSEConnection(connectionKey: String, state: Option[SSEConnectionState])

object Single {
  def apply[C](l: List[C]): C = if(l.tail.nonEmpty) throw new Exception else l.head
  def option[C](l: List[C]): Option[C] = if(l.isEmpty) None else Option(apply(l))
  def list[C](l: List[C]): List[C] = if(l.isEmpty || l.tail.isEmpty) l else throw new Exception
}

//case class FreshConnection(connectionKey: String)
class FreshConnectionJoin extends Join2(
  By.srcId(classOf[TcpConnection]),
  By.srcId(classOf[SSEConnectionState]),
  By.srcId(classOf[SSEConnection])
) {
  def join(
    tcpConnected: Values[TcpConnection],
    states: Values[SSEConnectionState]
  ) = tcpConnected.map(_.connectionKey).map(c⇒c→SSEConnection(c,Single.option(states)))
  def sort(nodes: Iterable[SSEConnection]) = Single.list(nodes.toList)
}


class SSEHttpRequestValueHandler() extends MessageHandler
(classOf[HttpRequestValue]) {
  override def mapMessage(res: MessageMapping, message: LEvent[HttpRequestValue]): MessageMapping = {
    if(message.srcId != "/connection") return res
    res.add(message.copy(value=Some(message.value.get.copy(index=9))))

    val headers = message.value.get.headers.flatMap(h ⇒
      if(h.key.startsWith("X-r-")) Seq(h.key→h.value) else Nil
    ).toMap
    val connectionKey = headers("X-r-connection")
    ???
  }
}

// /connection X-r-connection -> q-add -> q-poll -> FromAlienDictMessage
// (0/1-1) ShowToAlien -> sendToAlien

//(World,Msg) => (WorldWithChanges,Seq[Send])