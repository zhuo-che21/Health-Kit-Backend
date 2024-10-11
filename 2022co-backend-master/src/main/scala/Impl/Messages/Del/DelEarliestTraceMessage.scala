package Impl.Messages.Del

import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{UserTokenTable, UserTraceTable}
import Utils.DBUtils
import org.joda.time.DateTime

import scala.util.Try

/**
 * 删除最早的轨迹，返回新Token
 *
 * @param userToken
 */
case class DelEarliestTraceMessage(userToken: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val phone = UserTokenTable.checkPhone(userToken).get
    if (DBUtils.exec(UserTraceTable.checkEmpty(phone).get)) {
      DBUtils.exec(UserTraceTable.delEarlistTrace(phone).get)
      TSMSPReply(STATUS_OK, UserTokenTable.checkToken(phone).get)
    }
    else throw Exceptions.EmptyTrace()
  }
}
