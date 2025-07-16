package inventory

import user.model.CreateUserRequest

import scala.concurrent.Future

abstract class AbstractUserRepository [A <: PersistentEntity]:
  def getById(id: Long): Future[Vector[A]]
  def getByEmail(email: String): Future[Vector[A]]

  def save(user: CreateUserRequest): Future[Int]
