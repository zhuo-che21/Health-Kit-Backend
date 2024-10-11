package Impl.Messages.Get

import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{RequestTable, UserTokenTable}
import Utils.IOUtils
import org.joda.time.DateTime

import scala.util.Try

/**
 * 查询收到的请求
 * @param userToken
 */
case class ShowRequestedMessage(userToken : String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val phone = UserTokenTable.checkPhone(userToken).get
    TSMSPReply(STATUS_OK, IOUtils.serialize(RequestTable.showRequested(phone).get).get)
  }
}
