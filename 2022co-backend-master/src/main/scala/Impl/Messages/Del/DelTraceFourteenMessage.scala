package Impl.Messages.Del

import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{UserTokenTable, UserTraceTable}
import Utils.DBUtils
import org.joda.time.DateTime

import scala.util.Try

/**
 * 删除近14天以前的轨迹，返回新Token
 *
 * @param userToken
 */
case class DelTraceFourteenMessage(userToken: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val phone = UserTokenTable.checkPhone(userToken).get
    UserTraceTable.delTraceForteen(phone).get
    TSMSPReply(STATUS_OK, UserTokenTable.checkToken(phone).get)
  }
}
