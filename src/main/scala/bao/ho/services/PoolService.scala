package bao.ho.services

import bao.ho.services.PoolService.{ PercentileResponseBody, PollResponseBody }

import scala.language.higherKinds
import scala.util.control.NoStackTrace

trait PoolService[F[_]] {

  /**
   * This function for adding values in the pool
   * @param id
   * @param values
   * @return
   */
  def addValues(id: Int, values: List[Double]): F[PollResponseBody]

  /**
   * This function is used for calculating the percentile
   * @param poolId
   * @param percentile
   * @return
   */
  def calculatePercentile(poolId: Int, percentile: Double): F[PercentileResponseBody]
}

object PoolService {
  val Inserted = "Inserted"
  val Appended = "Appended"

  case class PollResponseBody(status: String)
  case class PercentileResponseBody(result: Double, numOfElement: Integer)

  object PollNotFound      extends Exception with NoStackTrace
  object PollValueIsEmpty  extends Exception with NoStackTrace
  object PercentileInvalid extends Exception with NoStackTrace
}
