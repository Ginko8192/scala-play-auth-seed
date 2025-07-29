package controllers

import inventory.{TestRepository, UserRepository}
import org.apache.pekko.stream.Materializer
import org.scalatestplus.play.*
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.Status.{OK, UNAUTHORIZED}
import play.api.libs.json.{JsObject, Json}
import play.api.test.*
import play.api.test.Helpers.{POST, call, contentAsString, defaultAwaitTimeout, status, writeableOf_AnyContentAsEmpty}


class AuthControllerTest extends PlaySpec with GuiceOneAppPerSuite with Injecting {

  // Required implicits
  given Materializer = app.materializer

  val userRepository: UserRepository = app.injector.instanceOf[UserRepository]
  val testRepository: TestRepository = app.injector.instanceOf[TestRepository]

  val registrationRequestJson: JsObject =
    Json.obj("email" -> "example@example.com", "password" -> "password", "username" -> "username", "firstName" -> "firstName", "lastName" -> "lastName")

  "AuthController POST" should {
    "Register user" in {
      cleanDb()

      registerUser()
    }

    def registerUser(): Unit = {
      val controller = app.injector.instanceOf[AuthController]

      val request = FakeRequest(POST, "/register")
        .withHeaders("Content-Type" -> "application/json")
        .withBody(registrationRequestJson)

      val result = call(controller.processRegistration(), request)

      println("Status: " + status(result))
      println("Response body: " + contentAsString(result))

      println(result)

      status(result) mustBe OK
      contentAsString(result) must not be empty
    }

    
    
    "Return JWT token for valid credentials" in {
      cleanDb()
      registerUser()

      val controller = app.injector.instanceOf[AuthController]
      
      val jsonBody = Json.obj("email" -> "example@example.com", "password" -> "password")

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
      cleanDb()
      registerUser()

      val controller = app.injector.instanceOf[AuthController]

      val jsonBody = Json.obj("email" -> "example@example.com", "password" -> "password")

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

    "Calling reserved endpoint without correct JWT returns UNAUTHORIZED" in {
      cleanDb()

      val controller = app.injector.instanceOf[AuthController]

      val request = FakeRequest(POST, "/")
        .withHeaders("Content-Type" -> "application/json")

      val result = call(controller.index(), request)

      status(result) mustBe UNAUTHORIZED
    }

    def cleanDb() =
      testRepository.cleanTestDB
  }
}


