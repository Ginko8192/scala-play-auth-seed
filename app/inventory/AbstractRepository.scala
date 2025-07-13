package inventory

import scala.concurrent.Future

abstract class AbstractRepository [A <: PersistentEntity]:
  def getById(id: Long): Future[Vector[A]]
