package Impl.Messages.Else

import Classtype.Place
import Globals.GlobalVariables.{placeNotVisited, placeVisited}
import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{PlaceTable, UserTokenTable, UserTraceTable}
import Utils.{DBUtils, IOUtils}
import org.joda.time.DateTime

import scala.util.Try

/**
 * 返回是否到访
 *
 * @param userToken
 * @param place 序列化的place
 */
case class SearchTraceMessage(userToken: String, place: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val phone = UserTokenTable.checkPhone(userToken).get
    val traceName = IOUtils.toObject[Place](Some(place)).get.get
    val traceID = DBUtils.exec(PlaceTable.transPlaNametoID(traceName).getOrElse(throw Exceptions.UnrecognizedPlace()))
    if (DBUtils.exec(UserTraceTable.searchTrace(phone, traceID).get)) {
      TSMSPReply(STATUS_OK, placeVisited)
    }
    else {
      TSMSPReply(STATUS_OK, placeNotVisited)
    }

  }
}
