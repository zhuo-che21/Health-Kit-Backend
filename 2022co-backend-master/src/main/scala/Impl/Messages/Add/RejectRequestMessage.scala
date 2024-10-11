package Impl.Messages.Add

import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables._
import Utils._
import org.joda.time.DateTime

import scala.util.Try

case class RejectRequestMessage(userToken : String, reqingPhone: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val phone = UserTokenTable.checkPhone(userToken).get
    val reqedName = DBUtils.exec(UserTable.returnInfo(phone).get).name
    if (DBUtils.exec(RequestTable.checkRequestExist(reqingPhone, reqedName, phone).get)) {
      if (DBUtils.exec(UserTable.checkUserExistsWithPhone(reqingPhone).get)) {
          DBUtils.exec(RequestTable.rejectRequest(reqingPhone, phone).get)
          TSMSPReply(STATUS_OK, UserTokenTable.checkToken(phone).get)
      }
      else throw Exceptions.UserNotExistsException()
    }
    else throw Exceptions.RequestNotExists()
  }
}
