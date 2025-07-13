package user.model

import inventory.PersistentEntity
import play.api.libs.json.{Json, Writes}
import slick.jdbc.GetResult

import java.time.Instant
import java.util.UUID

case class UserEntity (
                       id: Long,
                       google_id: Option[String],
                       is_google_account: Boolean,
                       email: String,
                       username: Option[String],
                       password_hash: String,
                       first_name: Option [String],
                       last_name: Option [String],
                       profile_picture_storage: Option[UUID],
                       bio: Option[String],
                       created_at: Instant,
                       last_login: Instant,
                       email_verified: Boolean,
                       is_admin: Boolean,
                       credit_balance: BigDecimal
                     ) extends PersistentEntity

object UserEntity {
  // JSON
  given Writes[UserEntity] = Json.writes[UserEntity]

  given GetResult[UserEntity] = GetResult(using r => {
    UserEntity(
      id = r.nextLong(),
      google_id = r.nextStringOption(),
      is_google_account = r.nextBoolean(),
      email = r.nextString(),
      username = r.nextStringOption(),
      password_hash = r.nextString(),
      first_name = r.nextStringOption(),
      last_name = r.nextStringOption(),
      profile_picture_storage = r.nextStringOption().map(UUID.fromString),
      bio = r.nextStringOption(),
      created_at = r.nextTimestamp().toInstant,
      last_login = r.nextTimestamp().toInstant,
      email_verified = r.nextBoolean(),
      is_admin = r.nextBoolean(),
      credit_balance = r.nextBigDecimal()
    )
  }
  )
}