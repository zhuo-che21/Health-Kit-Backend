package Impl.Messages.Get

import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{UserTable, UserTokenTable}
import Utils.{DBUtils, IOUtils}
import org.joda.time.DateTime

import scala.util.Try

/**
 * 返回最新用户信息
 *
 * @param userToken
 */
case class QueryUserInfoMessage(userToken: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val phone = UserTokenTable.checkPhone(userToken).get
    UserTable.checkColor(phone)
    TSMSPReply(STATUS_OK, IOUtils.serialize(DBUtils.exec(UserTable.returnInfo(phone).get)).get)
  }
}
