package controllers

import inventory.UserRepository
import org.apache.pekko.stream.Materializer
import org.scalatestplus.play.*
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.*
import user.model.CreateUserRequest

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class UserRepositoryTest extends PlaySpec with GuiceOneAppPerSuite with Injecting {

  // Required implicits
  given Materializer = app.materializer

  "Repository test" should {
    "Can get user by email" in {
      val userRepository = app.injector.instanceOf[UserRepository]

      val users = Await.result(userRepository.getByEmail("example@example.com"), 10.seconds)

      println(users.head)
    }
    "Can save user" in {
      val userRepository = app.injector.instanceOf[UserRepository]

      val createUserRequest: CreateUserRequest = CreateUserRequest(
        google_id = None,
        email = "example2@example.com",
        username = "anUsername",
        hashed_password = "xxx",
        first_name = "FirstName",
        last_name = "LastName",
        bio = "example-bio"
      )

      val users = Await.result(userRepository.saveUser(createUserRequest), 10.seconds)

      println(users)
    }
  }
}
