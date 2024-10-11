package Impl.Messages.Update

import Classtype.{Place, Rate}
import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.PlaceTable
import Utils.{DBUtils, IOUtils}
import org.joda.time.DateTime

import scala.util.Try

/**
 * 通过地点名更改Rate
 *
 * @param place
 * @param rate
 */
case class ChangeRateWithNameMessage(place: String, rate: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val placeName = IOUtils.toObject[Place](Some(place)).get.get
    val newRate = IOUtils.toObject[Rate](Some(rate)).get.get
    if (DBUtils.exec(PlaceTable.checkPlaceWithName(placeName).get)) {
      DBUtils.exec(PlaceTable.changeRateWithName(placeName, newRate).get)
      TSMSPReply(STATUS_OK, Globals.GlobalVariables.updateSuccess)
    }
    else throw Exceptions.PlaceNameNotExistsException()
  }
}
