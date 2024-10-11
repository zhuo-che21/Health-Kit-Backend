package Impl.Messages.Get

import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{UserTokenTable, VaccTable}
import Utils.{DBUtils, IOUtils}
import org.joda.time.DateTime

import scala.util.Try

/**
 * 查询第一次疫苗信息
 *
 * @param userToken
 */
case class GetFirstVaccMessage(userToken: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val phone = UserTokenTable.checkPhone(userToken).get
    TSMSPReply(STATUS_OK, IOUtils.serialize(DBUtils.exec(VaccTable.getFirstVacc(phone).get)).get)
  }
}
