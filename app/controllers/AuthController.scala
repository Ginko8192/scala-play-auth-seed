package controllers

import authentication.AuthService
import authentication.models.{LoginRequest, RegistrationRequest}
import authentication.models.LoginRequest.given
import controllers.actions.AuthenticatedAction
import inventory.UserRepository
import play.api.*
import play.api.libs.json.Json
import play.api.mvc.*

import javax.inject.*
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class AuthController @Inject()(
                                val controllerComponents: ControllerComponents,
                                userRepository: UserRepository,
                                authenticatedAction: AuthenticatedAction,
                                authService: AuthService
                              )(implicit ec: ExecutionContext) extends BaseController {

  def index(): Action[AnyContent] = authenticatedAction { implicit request: Request[AnyContent] =>
    val userVector = Await.result(
      userRepository.getById(1) , 10.seconds
    )

    Ok(Json.toJson(userVector)).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*") //TODO: for testing, probably isn't a great idea to keep it in production
  }

  def processLogin(): Action[LoginRequest] = Action.async(parse.json[LoginRequest]) { implicit request:Request[LoginRequest] =>
    val loginRequest = request.body
    println("loginRequest:" + loginRequest)
    authService.checkCredentialsAndReturnJWT(loginRequest).map {
      case Some(jwt) => Ok(jwt)
      case None => Unauthorized
    } 
  }

  def processRegistration(): Action[RegistrationRequest] = Action.async(parse.json[RegistrationRequest]) { implicit request: Request[RegistrationRequest] =>
    val registrationRequest = request.body

    authService.checkCredentialsAndReturnJWT(LoginRequest(registrationRequest.email, registrationRequest.password)).map {
      case Some(jwt) => Ok(jwt)
      case None => Unauthorized
    }
  }
}