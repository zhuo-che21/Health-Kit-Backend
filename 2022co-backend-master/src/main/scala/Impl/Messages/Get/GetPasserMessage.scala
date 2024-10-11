package Impl.Messages.Get

import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Utils.{DBUtils, IOUtils}
import org.joda.time.DateTime
import Classtype.Place
import Tables.{PlaceTable, UserTraceTable}

import scala.util.Try

/**
 * 返回所有经过此地点的用户信息
 * @param place
 */
case class GetPasserMessage(place : String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try{
    val placeName = IOUtils.toObject[Place](Some(place)).get.get
    if(DBUtils.exec(PlaceTable.checkPlaceWithName(placeName).get)) {
      val ID = DBUtils.exec(PlaceTable.transPlaNametoID(placeName).get)
      TSMSPReply(STATUS_OK, IOUtils.serialize(UserTraceTable.returePasser(ID).get).get)
    }
    else throw Exceptions.PlaceNameNotExistsException()
  }
}
