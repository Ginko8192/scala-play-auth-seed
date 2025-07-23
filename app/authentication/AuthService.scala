package authentication

import authentication.models.{LoginRequest, RegistrationRequest}
import inventory.UserRepository
import org.mindrot.jbcrypt.BCrypt
import user.model.CreateUserRequest

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AuthService @Inject()(userRepository: UserRepository)(implicit ec: ExecutionContext):
  def checkCredentialsAndReturnJWT(loginRequest: LoginRequest): Future[Option[String]] =
    val usersFuture = userRepository.getByEmail(loginRequest.email)
    println(hashPassword(loginRequest.password))
    usersFuture.map(users =>
      val user = users.head
      if users.isEmpty then
        None
      else
        if checkPassword(loginRequest.password, user.password_hash) then
          Some(JWTHelper.createToken(user.id.toString))
        else
          None
    )

  def registerUserAndReturnJWT(registrationRequest: RegistrationRequest): Future[Option[String]] =
    def saveUserFromRegistrationRequest(registrationRequest: RegistrationRequest): Future[Boolean] =
      val userToSave = CreateUserRequest(
        email = registrationRequest.email,
        username = registrationRequest.username,
        hashed_password = registrationRequest.password,
        first_name = registrationRequest.firstName,
        last_name = registrationRequest.lastName,
        bio = ""
      )

      userRepository.save(userToSave).map(_ == 1)
    
    // first we check if we have in the db a user with the same email 
    val userWithEmailFuture = userRepository.getByEmail(registrationRequest.email)
    // or with the same username
    val userWithUsernameFuture = userRepository.getByEmail(registrationRequest.email)
    
    userWithEmailFuture.flatMap(userWithEmail =>
      if userWithEmail.nonEmpty then Future(None)
      else 
        userWithUsernameFuture.flatMap(userWithUsername =>
        if userWithUsername.nonEmpty then Future(None)
        else
          // there exist no user with this combination of email and username so we can proceed with registering
          saveUserFromRegistrationRequest(registrationRequest)
            .flatMap(userWasSaved =>
              if userWasSaved then
                userRepository.getByEmail(registrationRequest.email)
                  .map(_.headOption.map(newUser => JWTHelper.createToken(newUser.id.toString)))
              else
                Future(None)
            )
      )
    )
      


  private def hashPassword(plainPassword: String): String =
    val salt = BCrypt.gensalt()
    BCrypt.hashpw(plainPassword, salt)

  private def checkPassword(plainPassword: String, hashedPassword: String): Boolean =
    BCrypt.checkpw(plainPassword, hashedPassword)