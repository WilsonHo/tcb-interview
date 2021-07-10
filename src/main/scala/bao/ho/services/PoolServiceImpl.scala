package bao.ho.services

import bao.ho.dbs.Db
import bao.ho.services.PoolService.{
  Appended,
  Inserted,
  PercentileInvalid,
  PercentileResponseBody,
  PollNotFound,
  PollResponseBody,
  PollValueIsEmpty
}
import cats.effect.Sync
import cats.syntax.applicative._

import scala.language.higherKinds

class PoolServiceImpl[F[_]](db: Db[F])(implicit F: Sync[F]) extends PoolService[F] {

  import cats.syntax.flatMap._
  import cats.syntax.functor._

  private def insert(id: Int, values: List[Double]): F[PollResponseBody] =
    db.save(id, values).map(_ => PollResponseBody(Inserted))

  private def update(id: Int,
                     values: List[Double],
                     oldValues: List[Double]): F[PollResponseBody] = {
    val allValues = values ++ oldValues
    db.save(id, allValues).map(_ => PollResponseBody(Appended))
  }

  private def checkPoolValues(values: List[Double]): F[Unit] =
    F.raiseError[List[Double]](PollValueIsEmpty).whenA(values.isEmpty)

  def addValues(id: Int, values: List[Double]): F[PollResponseBody] =
    for {
      _        <- checkPoolValues(values)
      valueOpt <- db.findById(id)
      result   <- valueOpt.fold(insert(id, values))(oldValues => update(id, values, oldValues))
    } yield result

  private def checkExistPool(valuesOpt: Option[List[Double]]): F[List[Double]] =
    valuesOpt.fold(F.raiseError[List[Double]](PollNotFound))(F.pure)

  def checkPercentile(percentile: Double): F[Unit] =
    F.raiseError(PercentileInvalid).whenA(percentile <= 0 || percentile > 100)

  override def calculatePercentile(poolId: Int, percentile: Double): F[PercentileResponseBody] =
    for {
      valuesOpt    <- db.findById(poolId)
      values       <- checkExistPool(valuesOpt)
      _            <- checkPercentile(percentile)
      result       = PoolServiceImpl.calculatePercentile(values, percentile)
      numOfElement = values.size
    } yield PercentileResponseBody(result, numOfElement)

}

object PoolServiceImpl {

  def calculatePercentile(values: List[Double], percentile: Double): Double = {
    val sortedValues = values.sorted
    val index        = math.round(percentile / 100.0 * (sortedValues.size - 1)).toInt
    sortedValues(index)
  }
}
