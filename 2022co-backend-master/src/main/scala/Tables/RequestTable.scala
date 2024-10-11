package Tables

import Classtype.ResultOfRequest
import Exceptions.{TokenNotExistsException, UserNotExistsException}
import Globals.GlobalVariables
import Impl.JacksonSerializable
import Utils.{DBUtils, StringUtils}
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, Tag}

import scala.util.Try
import Tables.PlaceAssoUserTable
import Tables.UserTraceTable
import Utils.CustomColumnTypes.jacksonSerializableType
import org.joda.time.DateTime

/**
 * 请求加为亲属
 * @param requestingName 请求者 名
 * @param requestingPhone
 * @param requestedName 被请求者 名
 * @param requestedPhone
 * @param time 申请时间
 * @param result 请求结果（同意 拒绝 未回复）
 */
case class RequestRow(
                     requestingName: String,
                     requestingPhone: String,
                     requestedName: String,
                     requestedPhone: String,
                     result: ResultOfRequest,
                     time: Long,
                     ) extends JacksonSerializable

class RequestTable(tag: Tag) extends Table[RequestRow](tag, GlobalVariables.mainSchema, "relative_request"){
  def requstingName = column[String]("requsting_name")
  def requestingPhone = column[String]("requesting_phone")
  def requestedName = column[String]("requested_name")
  def requestedPhone = column[String]("requested_phone")
  def result = column[ResultOfRequest]("result")
  def time = column[Long]("time")
  def * = (requstingName ,requestingPhone, requestedName, requestedPhone, result, time).mapTo[RequestRow]
}

object RequestTable {
  val requestTable = TableQuery[RequestTable]

  def addRequest(reqingPhone: String, reqedName: String, reqedPhone: String): Try[Int] =
    Try{
      val reqingName = DBUtils.exec(UserTable.userTable.filter(_.phone === reqingPhone).map(_.name).result.head)
      DBUtils.exec(requestTable += RequestRow(reqingName, reqingPhone, reqedName, reqedPhone, ResultOfRequest.getType("未回复"), time = DateTime.now().getMillis))
    }

  /**
   * 申请人撤销申请
   * @param reqingName
   * @param reqingPhone
   * @param reqedName
   * @param reqedPhone
   * @return
   */
  def delRequest(reqingPhone: String, reqedName: String, reqedPhone: String): Try[DBIO[Int]] =
    Try(requestTable.filter(r => r.requestingPhone === reqingPhone
    && r.requestedName === reqedName && r.requestedPhone === reqedPhone).delete)

  /**
   * 返回用户发出的申请请求
   * @param reqingPhone
   * @return
   */
  def showRequesting(reqingPhone: String): Try[List[RequestRow]] = Try{
    DBUtils.exec(requestTable.filter(_.requestingPhone === reqingPhone).sortBy(_.time.desc).result).toList
  }

  /**
   * 返回用户接收到的请求
   * @param reqedPhone
   * @return
   */
  def showRequested(reqedPhone: String): Try[List[RequestRow]] = Try {
    DBUtils.exec(requestTable.filter(_.requestedPhone === reqedPhone).sortBy(_.time.desc).result).toList
  }

  /**
   * 检查是否有重复申请
   * @param reqingName
   * @param reqingPhone
   * @param reqedName
   * @param reqedPhone
   * @return 若有重复返回true
   */
  def checkRequestExist( reqingPhone: String, reqedName: String, reqedPhone: String): Try[DBIO[Boolean]] =
    Try(requestTable.filter(r => r.requestingPhone === reqingPhone
      && r.requestedName === reqedName && r.requestedPhone === reqedPhone).exists.result)
  def acceptRequest(reqingPhone: String,  reqedPhone: String): Try[DBIO[Int]] =
    Try(requestTable.filter(r => r.requestingPhone === reqingPhone
      && r.requestedPhone === reqedPhone).map(_.result).update(ResultOfRequest.getType("同意")))

  def rejectRequest(reqingPhone: String, reqedPhone: String): Try[DBIO[Int]] =
    Try(requestTable.filter(r => r.requestingPhone === reqingPhone
      && r.requestedPhone === reqedPhone).map(_.result).update(ResultOfRequest.getType("拒绝")))

}