import bao.ho.controllers.PoolController
import bao.ho.dbs.MemDb
import bao.ho.services.PoolServiceImpl
import cats.effect.{ ContextShift, Effect, IO }
import org.scalatra._

import javax.servlet.ServletContext
import scala.concurrent.{ ExecutionContext, ExecutionContextExecutor }

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    implicit def ec: ExecutionContextExecutor = ExecutionContext.global
    implicit def cs: ContextShift[IO]         = IO.contextShift(ExecutionContext.global)
    implicit def F: Effect[IO]                = IO.ioEffect
    val db                                    = new MemDb[IO]
    val poolService                           = new PoolServiceImpl[IO](db)
    context.mount(new PoolController(poolService), "/*")
  }
}
