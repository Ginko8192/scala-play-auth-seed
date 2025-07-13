package authentication

import pdi.jwt.{JwtAlgorithm, JwtJson}
import play.api.libs.json.Json

object JWTHelper:
  private val secretKey = "your-secret-key"
  private val algorithm = JwtAlgorithm.HS256

  def createToken(userId: String): String = {
    val claim = Json.obj("user_id" -> userId)
    JwtJson.encode(claim, secretKey, algorithm)
  }

  def validateToken(token: String): Option[String] = {
    JwtJson.decodeJson(token, secretKey, Seq(algorithm)).toOption.flatMap { claim =>
      (claim \ "user_id").asOpt[String]
    }
  }

