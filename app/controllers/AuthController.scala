package controllers

import authentication.models.LoginRequest
import authentication.models.LoginRequest.given
import controllers.actions.AuthenticatedAction
import helper.JWTHelper
import inventory.RepositoryFactory
import play.api.*
import play.api.libs.json.Json
import play.api.mvc.*

import javax.inject.*
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

@Singleton
class AuthController @Inject()(val controllerComponents: ControllerComponents, repositoryFactory: RepositoryFactory, authenticatedAction: AuthenticatedAction) extends BaseController {

  def index() = authenticatedAction { implicit request: Request[AnyContent] =>
    val userRepository = repositoryFactory.provideUserRepository

    val userVector = Await.result(
      userRepository.getById(1) , 10.seconds
    )

    Ok(Json.toJson(userVector)).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*") //TODO: for testing, probably isn't a great idea to keep it in production
  }

  def processLogin(): Action[LoginRequest] = Action(parse.json[LoginRequest]) { implicit request =>
    val loginRequest = request.body
    println("loginRequest:" + loginRequest)
    if (loginRequest.email == "gmail@gmail.com" && loginRequest.password == "password") //TODO: replace with real logic
      Ok(JWTHelper.createToken("1"))
    else Unauthorized
  }
}