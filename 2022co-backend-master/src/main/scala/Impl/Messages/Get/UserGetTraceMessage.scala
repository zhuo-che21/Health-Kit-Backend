package Impl.Messages.Get

import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{UserTokenTable, UserTraceTable}
import Utils.IOUtils
import org.joda.time.DateTime

import scala.util.Try

/**
 * 获取一段时间内的用户轨迹，序列化输出相关地点信息
 *
 * @param userToken
 * @param startTime
 * @param endTime
 */
case class UserGetTraceMessage(userToken: String, startTime: Long, endTime: Long) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val phone = UserTokenTable.checkPhone(userToken).get
    TSMSPReply(STATUS_OK, IOUtils.serialize(UserTraceTable.checkTrace(phone, startTime, endTime).get).get)
  }
}
