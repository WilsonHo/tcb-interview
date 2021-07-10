package bao.ho.dbs

import cats.effect.Sync

import scala.collection.mutable
import scala.language.higherKinds

class MemDb[F[_]](implicit F: Sync[F]) extends Db[F] {
  val memDb: mutable.Map[Int, List[Double]] = mutable.Map.empty

  override def findById(id: Int): F[Option[Value]] =
    F.delay(memDb.get(id))

  override def save(id: Int, values: Value): F[Unit] =
    F.delay(memDb += (id -> values))

}
