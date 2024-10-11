package Tables

import Globals.GlobalVariables
import Utils.DBUtils
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, Tag}
import slick.ast.ColumnOption.{PrimaryKey, Unique}
import Classtype.Vaccine
import Utils.CustomColumnTypes.jacksonSerializableType
import org.joda.time.DateTime

import scala.util.Try

/**
 *疫苗
 * @param phone
 * @param firstVacc
 * @param secondVacc
 * @param thirdVacc
 * @param boostVacc 加强针
 */
case class VaccRow(
                  phone: String,
                  firstVacc: Vaccine,
                  secondVacc: Vaccine,
                  thirdVacc: Vaccine,
                  boostVacc: Vaccine,
                  )

class VaccTable(tag: Tag) extends Table[VaccRow](tag, GlobalVariables.mainSchema, "Vaccine"){
  def phone = column[String]("phone", Unique)
  def fristVacc = column[Vaccine]("first_vaccine")
  def secondVacc = column[Vaccine]("second_vaccine")
  def thirdVacc = column[Vaccine]("third_vaccine")
  def boostVacc = column[Vaccine]("boost_vaccine")
  def * = (phone, fristVacc, secondVacc, thirdVacc, boostVacc).mapTo[VaccRow]
}

object VaccTable {
  val vaccTable = TableQuery[VaccTable]

  def addVacc(phone: String):Try[DBIO[Int]] = Try{
    val emptyVacc = Vaccine(false, DateTime.now().getMillis(), "")
    VaccTable.vaccTable += Tables.VaccRow(phone, firstVacc = emptyVacc, secondVacc = emptyVacc, thirdVacc = emptyVacc, boostVacc = emptyVacc)
  }
  def updateFirstVacc(phone: String, vacc: Vaccine): Try[DBIO[Int]]=
    Try{
      vaccTable.filter(u => u.phone === phone).map(_.fristVacc).update(vacc)
    }

  def updateSecondVacc(phone: String, vacc: Vaccine): Try[DBIO[Int]] =
    Try(vaccTable.filter(u => u.phone === phone).map(_.secondVacc).update(vacc))

  def updateThirdVacc(phone: String, vacc: Vaccine): Try[DBIO[Int]] =
    Try(vaccTable.filter(u => u.phone === phone).map(_.thirdVacc).update(vacc))

  def updateBoostVacc(phone: String, vacc: Vaccine): Try[DBIO[Int]] =
    Try(vaccTable.filter(u => u.phone === phone).map(_.boostVacc).update(vacc))

  def getFirstVacc(phone: String): Try[DBIO[Vaccine]] =
    Try(vaccTable.filter(_.phone === phone).map(_.fristVacc).result.head)

  def getSecondVacc(phone: String): Try[DBIO[Vaccine]] =
    Try(vaccTable.filter(_.phone === phone).map(_.secondVacc).result.head)

  def getThirdVacc(phone: String): Try[DBIO[Vaccine]] =
    Try(vaccTable.filter(_.phone === phone).map(_.thirdVacc).result.head)

  def getBoostVacc(phone: String): Try[DBIO[Vaccine]] =
    Try(vaccTable.filter(_.phone === phone).map(_.boostVacc).result.head)

}
