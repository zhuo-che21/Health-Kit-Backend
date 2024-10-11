package Impl.Messages.Update

import Exceptions.UnrecognizedPlace
import Globals.GlobalVariables.updateSuccess
import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.{PlaceTable, UserTokenTable, UserTraceTable}
import Utils.DBUtils
import org.joda.time.DateTime

import scala.util.Try

/**
 * 扫码获取traceID,使用此函数上传轨迹，返回新Token
 * 若traceID无法识别，返回错误信息
 *
 * @param userToken
 * @param traceID 扫码获取
 */
case class UserUpdateTraceMessage(userToken: String, traceID: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val phone = UserTokenTable.checkPhone(userToken).get
    if (DBUtils.exec(PlaceTable.checkPlaceWithID(traceID).get)) {
      val placeName = DBUtils.exec(PlaceTable.transIDtoPlaName(traceID).get)
      val rate = DBUtils.exec(PlaceTable.returnRate(traceID).get)
      UserTraceTable.addTrace(phone, traceID, placeName, rate)
      TSMSPReply(STATUS_OK, updateSuccess)
    }
    else throw UnrecognizedPlace()
  }
}
