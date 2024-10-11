package Utils

import Globals.GlobalVariables
import Tables.{AcidTestTable, PlaceAssoUserTable, PlaceTable, RelativeTable, RequestTable, UserTable, UserTokenTable, UserTraceTable, VaccTable}
import com.typesafe.config.{Config, ConfigFactory}
import slick.dbio.DBIO
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration


object DBUtils {

  lazy val DBConfig: Config = ConfigFactory
    .parseString(s"""""")withFallback(ConfigFactory.load())

  lazy val db=Database.forConfig("tsmsp", config=DBConfig)

  def exec[T] : DBIO[T] => T = action => Await.result(db.run(action), Duration.Inf)
  def initDatabase():Unit={
    exec(
      DBIO.seq(
        sql"CREATE SCHEMA IF NOT EXISTS #${GlobalVariables.mainSchema.get}".as[Long],
        UserTable.userTable.schema.createIfNotExists,
        UserTokenTable.userTokenTable.schema.createIfNotExists,
        UserTraceTable.userTraceTable.schema.createIfNotExists,
        PlaceTable.placeTable.schema.createIfNotExists,
        PlaceAssoUserTable.placeAssoUserTable.schema.createIfNotExists,
        RelativeTable.relativeTable.schema.createIfNotExists,
        VaccTable.vaccTable.schema.createIfNotExists,
        AcidTestTable.acidTestTable.schema.createIfNotExists,
        RequestTable.requestTable.schema.createIfNotExists,
      )
    )
  }
  def dropDatabases():Unit={
    exec(
      DBIO.seq(
        UserTable.userTable.schema.dropIfExists,
        UserTokenTable.userTokenTable.schema.dropIfExists,
        UserTraceTable.userTraceTable.schema.dropIfExists,
        PlaceTable.placeTable.schema.dropIfExists,
        RelativeTable.relativeTable.schema.dropIfExists,
        VaccTable.vaccTable.schema.dropIfExists,
        AcidTestTable.acidTestTable.schema.dropIfExists,
        PlaceAssoUserTable.placeAssoUserTable.schema.dropIfExists,
        RequestTable.requestTable.schema.dropIfExists,
        sql"DROP SCHEMA IF EXISTS #${GlobalVariables.mainSchema.get}".as[Long],
      )
    )
  }
}
