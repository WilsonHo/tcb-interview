package bao.ho.controllers

import bao.ho.models.{ PercentileRequestBody, PoolRequestBody }
import bao.ho.services.PoolService
import bao.ho.services.PoolService.{ Appended, Inserted, PercentileResponseBody, PollResponseBody }
import cats.effect.Effect
import cats.syntax.functor._
import org.json4s.jackson.Serialization.write
import org.json4s.{ DefaultFormats, Formats }
import org.scalatra._

import scala.concurrent.ExecutionContext
import scala.language.higherKinds

class PoolController[F[_]](poolService: PoolService[F])(implicit val F: Effect[F],
                                                        ec: ExecutionContext)
    extends BaseController[F] {

  post("/pools") {
    def toResult(results: PollResponseBody): ActionResult = results.status match {
      case Inserted => Created(write(results))
      case Appended => Accepted(write(results))
    }

    parsedBody
      .extractOpt[PoolRequestBody]
      .fold(badRequest("Request body invalid"))(
        body =>
          poolService
            .addValues(body.poolId, body.poolValues)
            .map(toResult)
      )
      .toAsync

  }

  post("/pools/percentile") {
    def toResult(results: PercentileResponseBody): ActionResult =
      Ok(write(results))

    parsedBody
      .extractOpt[PercentileRequestBody]
      .fold(badRequest("Request body invalid"))(
        body =>
          poolService
            .calculatePercentile(body.poolId, body.percentile)
            .map(toResult)
      )
      .toAsync
  }

  post("/abc") {
    "Hello"
  }

  override protected implicit def jsonFormats: Formats = DefaultFormats

  override protected implicit def executor: ExecutionContext = ec
}
