package Exceptions

case class TokenNotExistsException() extends Exception {
  override def getMessage: String = "错误！用户不存在或登录信息已过期！"
}

case class UserNotExistsException() extends Exception {
  override def getMessage: String = "错误！用户不存在！"
}

case class WrongPasswordException() extends Exception {
  override def getMessage: String = "错误！密码错误或用户不存在"
}

case class UserAlreadyExistsException() extends Exception {
  override def getMessage: String = "错误！用户已经存在了"
}

case class PlaceNameNotExistsException() extends Exception {
  override def getMessage: String = "错误！地点不存在"
}

case class PlaceNameAlreadyExistsException() extends Exception {
  override def getMessage: String = "错误！地点已存在"
}

case class SamePlace() extends Exception {
  override def getMessage: String = "警告！与旧地点重复"
}

case class SamePassword() extends Exception {
  override def getMessage: String = "警告！与旧密码重复"
}

case class EmptyTrace() extends Exception {
  override def getMessage: String = "错误！不存在轨迹记录"
}

case class UnrecognizedPlace() extends Exception {
  override def getMessage: String = "错误！无法识别地点信息"
}

case class WrongManagerOrPassword() extends Exception {
  override def getMessage: String = "错误！密码错误或者用户不存在或无管理员权限"
}

case class RelativeAlreadyExists() extends Exception {
  override def getMessage: String = "错误！关联关系已存在"
}

case class RelativeNotExists() extends Exception {
  override def getMessage: String = "错误！关联关系不存在"
}

case class RelativeEmpty() extends Exception {
  override def getMessage: String = "错误！用户不存在关联关系"
}

case class AcidTestEmpty() extends Exception {
  override def getMessage: String = "错误！核酸记录为空"
}

case class RequestAlreadyExists() extends Exception {
  override def getMessage: String = "错误！请求已存在"
}

case class RequestNotExists() extends Exception {
  override def getMessage: String = "错误！请求不存在"
}