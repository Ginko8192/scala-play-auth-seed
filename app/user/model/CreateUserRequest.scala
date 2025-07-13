package user.model

case class CreateUserRequest(
                              google_id: Option[String] = None,
                              is_google_account: Boolean = false,
                              email: String,
                              username: String,
                              hashed_password: String,
                              first_name: String,
                              last_name: String,
                              bio: String
                            )