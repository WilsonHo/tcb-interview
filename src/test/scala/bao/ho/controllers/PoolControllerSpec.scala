package bao.ho.controllers

import bao.ho.dbs.MemDb
import bao.ho.models.{ PercentileRequestBody, PoolRequestBody }
import bao.ho.services.PoolServiceImpl
import cats.effect.IO
import org.json4s.{ DefaultFormats, Formats }
import org.json4s.jackson.Serialization.write
import org.scalatra.test.scalatest._

import scala.concurrent.ExecutionContext.Implicits.global

class PoolControllerSpec extends ScalatraFlatSpec {

  val db                            = new MemDb[IO]
  val poolService                   = new PoolServiceImpl[IO](db)
  implicit def jsonFormats: Formats = DefaultFormats
  val reqHeaders                    = List(("Content-Type", "application/json"))

  addServlet(new PoolController(poolService), "/*")

  "POST /pools" should "response 201 for inserted new values" in {
    val body = write(PoolRequestBody(1, List(1.0, 2.0)))
    post("/pools", body = body, headers = reqHeaders) {
      status shouldBe 201
    }
  }
  it should "response 202 for appended new values" in {
    val body = write(PoolRequestBody(1, List(2.0, 3.0)))
    post("/pools", body = body, headers = reqHeaders) {
      status shouldBe 202
    }
  }

  it should "response 400 when the request body values is empty" in {
    val body = write(PoolRequestBody(1, List.empty))
    post("/pools", body = body, headers = reqHeaders) {
      status shouldBe 400
    }
  }

  it should "response 400 when the incorrect request body" in {
    val body = "Any".getBytes
    post("/pools", body = body, headers = reqHeaders) {
      status shouldBe 400
    }
  }

  it should "response 400 when body is empty" in {
    post("/pools") {
      status shouldBe 400
    }
  }

  "POST /pools/percentile" should "response 200 with correct request body and pool id is existed" in {
    val body = write(PercentileRequestBody(1, 50))
    post("/pools/percentile", body = body, headers = reqHeaders) {
      status shouldBe 200
    }
  }
  it should "response 404 when the pool id is not existed" in {
    val body = write(PercentileRequestBody(2, 50))
    post("/pools/percentile", body = body, headers = reqHeaders) {
      status shouldBe 404
    }
  }

  it should "response 400 when the percentile value incorrect" in {
    val body = write(PercentileRequestBody(1, 101))
    post("/pools/percentile", body = body, headers = reqHeaders) {
      status shouldBe 400
    }
  }

  override def header = ???
}
