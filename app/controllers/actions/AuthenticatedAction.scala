package controllers.actions

import authentication.JWTHelper
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject

class AuthenticatedAction @Inject()(val parser: BodyParsers.Default)(implicit val executionContext: ExecutionContext)
  extends ActionBuilder[AuthenticatedRequest, AnyContent] with ActionRefiner[Request, AuthenticatedRequest] {

  override def refine[A](request: Request[A]): Future[Either[Result, AuthenticatedRequest[A]]] = {
    request.headers.get("Authorization")
      .filter(header => header.startsWith("Bearer "))
      .map( header => header.substring(7)) // we get the token from the header
      .map( jwtToken => // we check if the token is valid, and we return an EitherRight with the AuthenticatedRequest
        JWTHelper.validateTokenAndReturnUserId(jwtToken).map(userId => Future.successful(Right(AuthenticatedRequest(userId, request))))
          .getOrElse(Future.successful(Left(Results.Unauthorized("Invalid token"))))
      ).getOrElse(Future.successful(Left(Results.Unauthorized("Missing or invalid Authorization header"))))
  }
}
