package ee.cone.c4actor.hashsearch.index.dynamic

import ee.cone.c4proto._

@protocol("DynamicIndexAssemble") object IndexNodeProtocol  {
  @Id(0x205) case class S_IndexNodesVersion(
    @Id(0x207) srcId: String,
    @Id(0x206) version: String
  )

  // A
  @GenLens
  @Id(0x0169) case class S_IndexNode(
    @Id(0x016a) indexNodeId: String,
    @Id(0x016b) modelId: Int,
    @Id(0x016c) byAdapterId: Long,
    @Id(0x0187) commonPrefix: String
  )

  // B
  @Id(0x0180) case class S_IndexNodeSettings(
    @Id(0x0181) srcId: String,
    @Id(0x0182) allAlwaysAlive: Boolean,
    @Id(0x0195) keepAliveSeconds: Option[Long]
  )

  // C
  @GenLens
  @Id(0x0170) case class S_IndexByNode(
    @Id(0x0175) leafId: String,
    @Id(0x0177) indexNodeId: String,
    @Id(0x0194) modelId: Int,
    @Id(0x0173) heapIds: List[String],
    @Id(0x0204) byStr: String
  )

  @Id(0x0201) case class S_IndexByNodeLastSeen(
    @Id(0x0202) srcId: String,
    @Id(0x0203) lastSeenAtSeconds: Long
  )

  // E
  @Id(0x0190) case class S_IndexByNodeSettings(
    @Id(0x0191) srcId: String,
    @Id(0x0192) alwaysAlive: Boolean,
    @Id(0x0193) keepAliveSeconds: Option[Long]
  )

  @Id(0x0196) case class S_TimeMeasurement(
    @Id(0x0197) srcId: String,
    @Id(0x0198) measurement: Option[Long]
  )
}
