package Impl.Messages.Del

import Classtype.Place
import Exceptions.PlaceNameNotExistsException
import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{PlaceTable, UserTokenTable, UserTraceTable}
import Utils.{DBUtils, IOUtils}
import org.joda.time.DateTime

import scala.util.Try

/**
 * 成功返回新的Token
 *
 * @param userToken
 * @param place
 */
case class DelTraceMessage(userToken: String, place: String) extends TSMSPMessage {

  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    //val WhetherTrace = UserTraceTable.searchTrace(userName, place).get
    //if(!WhetherTrace) TSMSPReply(STATUS_OK, "假成功！")
    val phone = UserTokenTable.checkPhone(userToken).get
    val placeName = IOUtils.toObject[Place](Some(place)).get.get
    val placeID = DBUtils.exec(PlaceTable.transPlaNametoID(placeName).getOrElse(throw Exceptions.UnrecognizedPlace()))
    if (DBUtils.exec(UserTraceTable.searchTrace(phone, placeID).get)) {
      DBUtils.exec(UserTraceTable.delTrace(phone, placeID).get)
      TSMSPReply(STATUS_OK, UserTokenTable.checkToken(phone).get)
    }
    else throw PlaceNameNotExistsException()
  }
}
