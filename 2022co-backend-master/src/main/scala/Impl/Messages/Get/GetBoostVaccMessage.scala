package Impl.Messages.Get

import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{UserTokenTable, VaccTable}
import Utils.{DBUtils, IOUtils}
import org.joda.time.DateTime

import scala.util.Try

/**
 * 查询加强针疫苗信息
 *
 * @param userToken
 */
case class GetBoostVaccMessage(userToken: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val phone = UserTokenTable.checkPhone(userToken).get
    TSMSPReply(STATUS_OK, IOUtils.serialize(DBUtils.exec(VaccTable.getBoostVacc(phone).get)).get)
  }
}
