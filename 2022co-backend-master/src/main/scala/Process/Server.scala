package Process

import Tables.{UserTable, UserTokenTable, VaccTable}
import Utils.DBUtils
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import com.typesafe.scalalogging.Logger


object Server {
  val logger = Logger("MainServer")
  def main(args: Array[String]): Unit = try {
    //DBUtils.dropDatabases()
    DBUtils.initDatabase()
    if (!DBUtils.exec(UserTable.checkUserExistsWithPhone(Globals.GlobalVariables.metaManager).get)){
      DBUtils.exec(UserTable.addManager("Admin", Globals.GlobalVariables.metaPassword, "0", Globals.GlobalVariables.metaManager)
        .get.andThen(UserTokenTable.addRow(Globals.GlobalVariables.metaManager).get).andThen(VaccTable.addVacc(Globals.GlobalVariables.metaManager).get))
    }
        implicit val system : ActorSystem[Nothing] = ActorSystem[Nothing](Behaviors.empty[Nothing], "template-system")
    TSMSPPortalHttpServer.startHttpServer(new Routes().routes, system)

  } catch {
    case exception: Exception =>
      logger.error(exception.getMessage)
  }
}
