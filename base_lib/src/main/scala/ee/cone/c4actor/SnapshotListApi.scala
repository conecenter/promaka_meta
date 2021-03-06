package ee.cone.c4actor

import ee.cone.c4actor.Types.NextOffset
import ee.cone.c4proto.{Id, protocol}

trait SnapshotListProtocolAppBase
@protocol("SnapshotListProtocolApp") object SnapshotListRequestProtocol {
  @Id(0x36c9) case class S_ListSnapshotsRequest(
    @Id(0x36ca) source: String
  )

  @Id(0x36cb) case class S_ListSnapshotsResponse(
    @Id(0x36cc) source: String,
    @Id(0x36cd) snapshotsInfo: List[N_SnapshotInfoProto]
  )

  case class N_SnapshotInfoProto(
    @Id(0x36cf) subDirStr: String,
    @Id(0x36d0) offset: String,
    @Id(0x36d1) uuid: String,
    @Id(0x36d2) raw: Option[N_RawSnapshotInfoProto],
    @Id(0x36d5) creationDate: Long
  )

  case class N_RawSnapshotInfoProto(
    @Id(0x36d4) relativePath: String
  )
}

trait ConsumerBeginningOffset {
  def get(): NextOffset
}
