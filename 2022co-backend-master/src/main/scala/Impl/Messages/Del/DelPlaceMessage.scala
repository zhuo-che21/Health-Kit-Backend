package Impl.Messages.Del

import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.PlaceTable
import Utils.DBUtils
import org.joda.time.DateTime

import scala.util.Try

/**
 * 通过地点ID删除地点
 *
 * @param placeID
 */
case class DelPlaceMessage(placeID: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    if (DBUtils.exec(PlaceTable.checkPlaceWithID(placeID).get)) {
      DBUtils.exec(PlaceTable.delPlaceWithID(placeID).get)
      TSMSPReply(STATUS_OK, Globals.GlobalVariables.SuccessDel)
    }
    else throw Exceptions.PlaceNameNotExistsException()
  }
}
