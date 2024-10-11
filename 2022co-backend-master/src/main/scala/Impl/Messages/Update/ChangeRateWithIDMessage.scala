package Impl.Messages.Update

import Classtype.Rate
import Impl.Messages.TSMSPMessage
import Impl.{STATUS_OK, TSMSPReply}
import Tables.PlaceTable
import Utils.{DBUtils, IOUtils}
import org.joda.time.DateTime

import scala.util.Try

/**
 * 通过地点ID更改Rate
 *
 * @param place
 * @param rate
 */
case class ChangeRateWithIDMessage(ID: String, rate: String) extends TSMSPMessage {
  override def reaction(now: DateTime): Try[TSMSPReply] = Try {
    val newRate = IOUtils.toObject[Rate](Some(rate)).get.get
    if (DBUtils.exec(PlaceTable.checkPlaceWithID(ID).get)) {
      DBUtils.exec(PlaceTable.changeRateWithID(ID, newRate).get)
      TSMSPReply(STATUS_OK, Globals.GlobalVariables.updateSuccess)
    }
    else throw Exceptions.PlaceNameNotExistsException()
  }
}
