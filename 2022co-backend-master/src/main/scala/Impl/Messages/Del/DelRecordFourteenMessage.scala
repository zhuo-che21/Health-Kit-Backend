package Impl.Messages.Del

import Classtype.Place
import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{PlaceAssoUserTable, PlaceTable}
import Utils.{DBUtils, IOUtils}
import org.joda.time.DateTime

import scala.util.Try

/**
 * 删除地点14天前记录,成功返回删除成功
 *
 * @param place
 */
case class DelRecordFourteenMessage(place: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val placeName = IOUtils.toObject[Place](Some(place)).get.get
    val ID = DBUtils.exec(PlaceTable.transPlaNametoID(placeName).getOrElse(throw Exceptions.UnrecognizedPlace()))
    if (DBUtils.exec(PlaceTable.checkPlaceWithID(ID).get)) {
      DBUtils.exec(PlaceAssoUserTable.delRecordForteen(ID).get)
      TSMSPReply(STATUS_OK, Globals.GlobalVariables.SuccessDel)
    }
    else throw Exceptions.PlaceNameNotExistsException()
  }
}
