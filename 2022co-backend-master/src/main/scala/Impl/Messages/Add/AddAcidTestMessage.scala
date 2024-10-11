package Impl.Messages.Add

import Classtype.ResultOfAcid
import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{AcidTestTable, UserTokenTable}
import Utils.{DBUtils, IOUtils}
import org.joda.time.DateTime

import scala.util.Try

/**
 * 添加核酸记录，需要Token，结果和机构。成功返回上传成功
 *
 * @param userToken
 * @param result
 * @param agency
 */
case class AddAcidTestMessage(userToken: String, result: String, agency: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val newResult = IOUtils.toObject[ResultOfAcid](Some(result)).get.get
    val phone = UserTokenTable.checkPhone(userToken).get
    DBUtils.exec(AcidTestTable.addRecord(phone, newResult, agency).get)
    TSMSPReply(STATUS_OK, Globals.GlobalVariables.updateSuccess)
  }
}
