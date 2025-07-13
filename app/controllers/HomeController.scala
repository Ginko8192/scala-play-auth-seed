package controllers

import inventory.UserRepository
import play.api.*
import play.api.libs.json.Json
import play.api.mvc.*

import javax.inject.*
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents, userRepository: UserRepository) extends BaseController {
  
  def index() = Action { implicit request: Request[AnyContent] =>

    val userVector = Await.result(
      userRepository.getById(1) , 10.seconds
    )

    Ok(Json.toJson(userVector)).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*") //TODO: for testing, probably isn't a great idea to keep it in production
  }
}
