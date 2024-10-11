package Impl.Messages.Add

import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{RequestTable, UserTokenTable, UserTable}
import Utils.DBUtils
import org.joda.time.DateTime

import scala.util.Try

/**
 * 添加关联请求，成功返回Token
 * @param userToken
 * @param reqedName
 * @param reqedPhone
 */
case class AddRequestMessage(userToken : String, reqedName: String, reqedPhone: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val phone = UserTokenTable.checkPhone(userToken).get
    if(!DBUtils.exec(RequestTable.checkRequestExist(phone, reqedName, reqedPhone).get)) {
      if(DBUtils.exec(UserTable.checkUserExistsWithPhone(reqedPhone).get)) {
        if(DBUtils.exec(UserTable.checkPhoneForName(reqedPhone, reqedName).get)) {
          RequestTable.addRequest(phone, reqedName, reqedPhone).get
          TSMSPReply(STATUS_OK, UserTokenTable.checkToken(phone).get)
        }
        else throw Exceptions.UserNotExistsException()
      }
      else throw Exceptions.UserNotExistsException()
    }
    else throw Exceptions.RequestAlreadyExists()
  }
}
