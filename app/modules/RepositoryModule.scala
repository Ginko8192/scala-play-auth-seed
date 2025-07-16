package modules

import com.google.inject.{AbstractModule, Provides}
import inventory.{TestRepository, UserRepository}
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

  @Provides
  @Singleton
  def provideTestRepository(config: Configuration)(implicit mat: Materializer, ec: ExecutionContext): TestRepository = {
    val db = Database.forConfig("slick.dbs.default.db")
    new TestRepository(db)
  }
}
