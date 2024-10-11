package Globals

object GlobalVariables {
  val forteenDayInMinisecond = 14*24*3600*1000
  val mainSchema : Option[String] = Some("tsmsp_portal")
  val listenAddress : String = "0.0.0.0"
  val listenPortal : Int = 6070
  val updateSuccess : String = "上传成功！"
  val placeAddSuccess : String = "地点添加成功"
  val placeVisited : String = "14天内到访过此地点"
  val placeNotVisited : String = "14天内未到访过此地点"
  val SuccessDel : String = "删除成功"
  val metaManager : String = "211A"
  val metaPassword : String = "fighting!"
  val managerSetSuccess : String = "管理员设置成功"
}
 