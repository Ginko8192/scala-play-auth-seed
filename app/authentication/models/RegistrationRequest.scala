package authentication.models

import play.api.libs.json.{Format, Json}

case class RegistrationRequest (                       
                            email: String,
                            username: String,
                            password: String,
                            firstName: String,
                            lastName: String
                          )

object RegistrationRequest:
  given Format[RegistrationRequest] = Json.format[RegistrationRequest]