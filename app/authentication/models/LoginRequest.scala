package authentication.models

import play.api.libs.json.{Format, Json, Reads}

case class LoginRequest(
                       email: String,
                       password: String
                       )
object LoginRequest:
  given Format[LoginRequest] = Json.format[LoginRequest]