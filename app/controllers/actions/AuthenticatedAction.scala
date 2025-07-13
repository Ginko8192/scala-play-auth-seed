package controllers.actions

import authentication.JWTHelper
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject

class AuthenticatedAction @Inject()(val parser: BodyParsers.Default)(implicit val executionContext: ExecutionContext)
  extends ActionBuilder[AuthenticatedRequest, AnyContent] with ActionRefiner[Request, AuthenticatedRequest] {

  override def refine[A](request: Request[A]): Future[Either[Result, AuthenticatedRequest[A]]] = {
    request.headers.get("Authorization") match {
      case Some(auth) if auth.startsWith("Bearer ") =>
        val token = auth.substring(7)
        JWTHelper.validateToken(token) match {
          case Some(userId) =>
            Future.successful(Right(AuthenticatedRequest(userId, request)))
          case None =>
            Future.successful(Left(Results.Unauthorized("Invalid token")))
        }
      case _ =>
        Future.successful(Left(Results.Unauthorized("Missing or invalid Authorization header")))
    }
  }
}
