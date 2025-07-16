package inventory

import play.api.Logger
import slick.jdbc.GetResult
import slick.jdbc.PostgresProfile.api.*
import user.model.{CreateUserRequest, UserEntity}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class UserRepository @Inject()(db: Database)(implicit ec: ExecutionContext) extends AbstractUserRepository:
  private val logger = Logger(this.getClass)

  def getById(id: Long): Future[Vector[UserEntity]] =
    val query =
      sql"""
        SELECT
        user_id,
        google_id,
        is_google_account,
        email,
        username,
        password_hash,
        first_name,
        last_name,
        profile_picture_storage_id,
        bio,
        created_at,
        last_login,
        email_verified,
        is_admin,
        credit_balance

	      FROM public.users;
        """.as[UserEntity](using summon[GetResult[UserEntity]])

    db.run(query)

  def getByEmail(email: String): Future[Vector[UserEntity]] =
    val query =
      sql"""
        SELECT
        user_id,
        google_id,
        is_google_account,
        email,
        username,
        password_hash,
        first_name,
        last_name,
        profile_picture_storage_id,
        bio,
        created_at,
        last_login,
        email_verified,
        is_admin,
        credit_balance

        FROM public.users
        WHERE email = $email
        ;
        """.as[UserEntity](using summon[GetResult[UserEntity]])

    db.run(query)

  def save(user: CreateUserRequest): Future[Int] = 
    val query =
      sqlu"""
      INSERT INTO public."users" (
       google_id,
       is_google_account,
       email,
       username,
       password_hash,
       first_name,
       last_name,
       bio,
       last_login
      ) VALUES (
       ${user.google_id},
       ${user.is_google_account},
       ${user.email},
       ${user.username},
       ${user.hashed_password},
       ${user.first_name},
       ${user.last_name},
       ${user.bio},
       CURRENT_TIMESTAMP);
      """

    println(query.getDumpInfo)
    db.run(query)