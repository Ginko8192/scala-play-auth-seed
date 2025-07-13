package inventory

import com.google.inject.Provides
import org.apache.pekko.stream.Materializer
import play.api.Configuration
import slick.jdbc.PostgresProfile.api._

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class RepositoryFactory @Inject()(config: Configuration)(implicit mat: Materializer, ec: ExecutionContext) {
  private val db = Database.forConfig("slick.dbs.default.db")

  private lazy val userRepository = new UserRepository(db)

  @Provides
  def provideUserRepository: UserRepository = userRepository
}

