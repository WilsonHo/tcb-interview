package bao.ho.dbs

import scala.language.higherKinds

trait Db[F[_]] {
  type Value = List[Double]
  def findById(id: Int): F[Option[Value]]
  def save(id: Int, values: Value): F[Unit]
}
