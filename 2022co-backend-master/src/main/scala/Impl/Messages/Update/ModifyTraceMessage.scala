package Impl.Messages.Update

import Classtype.Place
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
 * @param oldTrace
 * @param newTrace
 */
case class ModifyTraceMessage(userToken: String, oldTrace: String, newTrace: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val phone = UserTokenTable.checkPhone(userToken).get
    val oldTraceName = IOUtils.toObject[Place](Some(oldTrace)).get.get
    val newTraceName = IOUtils.toObject[Place](Some(newTrace)).get.get
    val oldTraceID = DBUtils.exec(PlaceTable.transPlaNametoID(oldTraceName).getOrElse(throw Exceptions.UnrecognizedPlace()))
    val newTraceID = DBUtils.exec(PlaceTable.transPlaNametoID(newTraceName).getOrElse(throw Exceptions.UnrecognizedPlace()))
    if (DBUtils.exec(PlaceTable.checkPlaceWithID(oldTraceID).get) && DBUtils.exec(PlaceTable.checkPlaceWithID(newTraceID).get)) {
      DBUtils.exec(UserTraceTable.modifyTrace(phone, oldTraceID, newTraceID).get)
      TSMSPReply(STATUS_OK, UserTokenTable.checkToken(phone).get)
    }
    else throw Exceptions.PlaceNameNotExistsException()
  }
}
