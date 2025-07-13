package inventory

import play.api.Logger
import slick.jdbc.GetResult

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.PostgresProfile.api._
import user.model.UserEntity

class UserRepository @Inject()(db: Database)(implicit ec: ExecutionContext) extends AbstractRepository {
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
}

