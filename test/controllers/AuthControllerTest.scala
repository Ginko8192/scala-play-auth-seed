package controllers

import authentication.models.LoginRequest
import org.apache.pekko.stream.Materializer
import org.scalatestplus.play.*
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.Status.{OK, UNAUTHORIZED}
import play.api.libs.json.{Json, Reads}
import play.api.test.*
import play.api.test.Helpers.{POST, call, contentAsString, defaultAwaitTimeout, status}

import play.api.test.Helpers.writeableOf_AnyContentAsEmpty


class AuthControllerTest extends PlaySpec with GuiceOneAppPerSuite with Injecting {

  // Required implicits
  given Materializer = app.materializer

  "AuthController POST" should {
    "return JWT token for valid credentials" in {
      val controller = app.injector.instanceOf[AuthController]
      val jsonBody = Json.obj("email" -> "gmail@gmail.com", "password" -> "password")

      val request = FakeRequest(POST, "/login")
        .withHeaders("Content-Type" -> "application/json")
        .withBody(jsonBody)

      val result = call(controller.processLogin(), request)

      println("Status: " + status(result))
      println("Response body: " + contentAsString(result))

      println(result)

      status(result) mustBe OK
      contentAsString(result) must not be empty
    }

    "Calling reserved endpoint with JWT returns OK" in {
      val controller = app.injector.instanceOf[AuthController]

      val jsonBody = Json.obj("email" -> "gmail@gmail.com", "password" -> "password")

      val request = FakeRequest(POST, "/login")
        .withHeaders("Content-Type" -> "application/json")
        .withBody(jsonBody)

      val result = call(controller.processLogin(), request)

      val JWT = contentAsString(result)

      val bearer: String =  "Bearer " + JWT

      val authRequest = FakeRequest(POST, "/")
        .withHeaders(
          "Content-Type" -> "application/json",
          "Authorization" -> bearer
        )

      val resultAuth = call(controller.index(), authRequest)

      status(resultAuth) mustBe OK
    }

    "Calling reserved endpoint with JWT returns UNAUTHORIZED" in {
      val controller = app.injector.instanceOf[AuthController]

      val request = FakeRequest(POST, "/")
        .withHeaders("Content-Type" -> "application/json")

      val result = call(controller.index(), request)

      status(result) mustBe UNAUTHORIZED
    }
  }
}


