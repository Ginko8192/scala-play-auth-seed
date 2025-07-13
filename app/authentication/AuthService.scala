package authentication

import authentication.models.LoginRequest
import inventory.UserRepository
import org.mindrot.jbcrypt.BCrypt

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AuthService @Inject()(userRepository: UserRepository)(implicit ec: ExecutionContext):
  def checkCredentialsAndReturnJWT(loginRequest: LoginRequest): Future[Option[String]] =
    val usersFuture = userRepository.getByEmail(loginRequest.email)
    println(hashPassword(loginRequest.password))
    usersFuture.map(users =>
      val user = users.head
      if users.isEmpty then
        println("No users") //TODO: for debugging
        None
      else
        if(checkPassword(loginRequest.password, user.password_hash)) then
          Some(JWTHelper.createToken(user.id.toString))
        else
          None
    )



  private def hashPassword(plainPassword: String): String =
    val salt = BCrypt.gensalt()
    BCrypt.hashpw(plainPassword, salt)

  private def checkPassword(plainPassword: String, hashedPassword: String): Boolean =
    BCrypt.checkpw(plainPassword, hashedPassword)