package Impl.Messages.Get

import Classtype.Place
import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.PlaceTable
import Utils.{DBUtils, IOUtils}
import org.joda.time.DateTime

import scala.util.Try

/**
 * 查询地点的ID
 *
 * @param place
 */
case class QueryPlaceIDMessage(place: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val placeName = IOUtils.toObject[Place](Some(place)).get.get
    if (DBUtils.exec(PlaceTable.checkPlaceWithName(placeName).get)) {
      TSMSPReply(STATUS_OK, DBUtils.exec(PlaceTable.transPlaNametoID(placeName).get))
    }
    else throw Exceptions.PlaceNameNotExistsException()
  }
}
