package inventory

import slick.jdbc.GetResult
import slick.jdbc.PostgresProfile.api.*

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class TestRepository @Inject()(db: Database)(implicit ec: ExecutionContext):
  def cleanTestDB: Future[Int] =
    val query = sql"""
      TRUNCATE TABLE public."users" RESTART IDENTITY CASCADE;
    """.as[Int]

    db.run(query).map(r => r.head)
