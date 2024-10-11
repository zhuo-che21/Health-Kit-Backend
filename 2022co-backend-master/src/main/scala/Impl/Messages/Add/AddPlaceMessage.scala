package Impl.Messages.Add

import Classtype.{Place, Rate}
import Exceptions.PlaceNameAlreadyExistsException
import Globals.GlobalVariables.placeAddSuccess
import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.PlaceTable
import Utils.{DBUtils, IOUtils}
import org.joda.time.DateTime

import scala.util.Try

/**
 * 管理员传输序列化的place,rate，返回是否添加成功
 *
 * @param place 序列化的地点
 * @param rate  序列化的风险等级
 */
case class AddPlaceMessage(place: String, rate: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val placeName = IOUtils.toObject[Place](Some(place)).get
    val newRate = IOUtils.toObject[Rate](Some(rate)).get
    if (DBUtils.exec(PlaceTable.checkPlaceWithName(placeName.get).get)) throw PlaceNameAlreadyExistsException()
    else {
      DBUtils.exec(PlaceTable.addPlace(placeName.get, newRate.get).get)
      TSMSPReply(STATUS_OK, DBUtils.exec(PlaceTable.transPlaNametoID(placeName.get).get))
    }
  }
}
