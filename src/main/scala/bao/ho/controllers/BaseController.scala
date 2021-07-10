package bao.ho.controllers

import bao.ho.services.PoolService.{ PercentileInvalid, PollNotFound, PollValueIsEmpty }
import cats.effect.Effect
import org.json4s.jackson.Serialization.write
import org.scalatra.{
  ActionResult,
  AsyncResult,
  BadRequest,
  FutureSupport,
  InternalServerError,
  NotFound,
  ScalatraServlet
}
import org.scalatra.json.JacksonJsonSupport

import scala.concurrent.Future
import scala.language.higherKinds
import cats.syntax.applicative._
import cats.syntax.applicativeError._

trait BaseController[F[_]] extends ScalatraServlet with JacksonJsonSupport with FutureSupport {
  implicit val F: Effect[F]
  implicit class AsyncOpts(fa: => F[ActionResult]) {

    def toAsync: AsyncResult =
      new AsyncResult {

        val is: Future[ActionResult] =
          F.toIO(fa.recoverWith(recovery)).unsafeToFuture
      }
  }

  protected def jsonMsg(msg: String): String =
    write(Map("message" -> s"$msg"))(jsonFormats)

  def badRequest(msg: String): F[ActionResult] = BadRequest(jsonMsg(msg)).pure[F]

  def notFound(msg: String): F[ActionResult] = NotFound(jsonMsg(msg)).pure[F]

  def internalServerError(msg: String): F[ActionResult] =
    InternalServerError(jsonMsg(msg)).pure[F]

  private def recovery: PartialFunction[Throwable, F[ActionResult]] = {
    case PollNotFound      => notFound("Poll not found")
    case PollValueIsEmpty  => badRequest("The pool values is empty")
    case PercentileInvalid => badRequest("The percentile value is invalid")
    case _: Exception =>
      val message = s"Oops! Something went wrong"
      InternalServerError(jsonMsg(message)).pure[F]
  }
}
