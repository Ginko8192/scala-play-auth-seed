package controllers.actions

import play.api.mvc.{Request, WrappedRequest}

case class AuthenticatedRequest[A](userId: String, request: Request[A]) extends WrappedRequest[A](request)
