package Impl.Messages.Del

import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{RequestTable, UserTokenTable}
import Utils.DBUtils
import org.joda.time.DateTime

import scala.util.Try

/**
 * 删除关联请求，成功返回Token
 * @param userToken
 * @param reqedName
 * @param reqedPhone
 */
case class DelRequestMessage(userToken : String, reqedName: String, reqedPhone: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val phone = UserTokenTable.checkPhone(userToken).get
    if(!DBUtils.exec(RequestTable.checkRequestExist(phone, reqedName, reqedPhone).get)) {
      DBUtils.exec(RequestTable.delRequest(phone, reqedName, reqedPhone).get)
      TSMSPReply(STATUS_OK, UserTokenTable.checkToken(phone).get)
    }
    else throw Exceptions.RequestNotExists()
  }
}