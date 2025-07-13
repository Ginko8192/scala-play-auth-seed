package modules

import com.google.inject.{AbstractModule, Provides}
import inventory.UserRepository
import org.apache.pekko.stream.Materializer
import play.api.Configuration
import slick.jdbc.PostgresProfile.api.*

import javax.inject.Singleton
import scala.concurrent.ExecutionContext

class RepositoryModule extends AbstractModule {

  @Provides
  @Singleton
  def provideUserRepository(config: Configuration)(implicit mat: Materializer, ec: ExecutionContext): UserRepository = {
    val db = Database.forConfig("slick.dbs.default.db")
    new UserRepository(db)
  }
}
