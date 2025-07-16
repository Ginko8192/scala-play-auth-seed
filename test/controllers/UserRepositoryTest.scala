package controllers

import inventory.{TestRepository, UserRepository}
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

  val userRepository: UserRepository = app.injector.instanceOf[UserRepository]
  val testRepository: TestRepository = app.injector.instanceOf[TestRepository]

  "Repository test" should {
    "Can save user" in {
      cleanDb()

      println(saveExampleUser)
    }

    "Can get user by email" in {
      cleanDb()
      saveExampleUser

      val users = Await.result(userRepository.getByEmail("example@example.com"), 10.seconds)

      println(users.head)
    }

    def saveExampleUser: Int =
      val createUserRequest: CreateUserRequest = CreateUserRequest(
        google_id = None,
        email = "example@example.com",
        username = "username",
        hashed_password = "xxx",
        first_name = "FirstName",
        last_name = "LastName",
        bio = "example-bio"
      )

      Await.result(userRepository.save(createUserRequest), 10.seconds)

    def cleanDb() =
      testRepository.cleanTestDB
  }
}
