package bao.ho.services

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class PoolServiceImplSpec extends AnyFlatSpec {
  "Function calculatePercentile" should "return correct values with list List(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0)" in {
    val values = List(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0)
    PoolServiceImpl.calculatePercentile(values, 0) shouldBe (0.0)
    PoolServiceImpl.calculatePercentile(values, 20) shouldBe (2.0)
    PoolServiceImpl.calculatePercentile(values, 80) shouldBe (8.0)
    PoolServiceImpl.calculatePercentile(values, 100) shouldBe (10.0)
  }

  it should "return correct values with list List(0.0, 1.0, 2.0, 3.0)" in {
    val values: List[Double] = List(0.0, 1.0, 2.0, 3.0)
    PoolServiceImpl.calculatePercentile(values, 51) shouldBe (2.0)
    PoolServiceImpl.calculatePercentile(values, 49) shouldBe (1.0)
  }

  it should "return correct values with list List(42.0)" in {
    val values: List[Double] = List(42.0)
    PoolServiceImpl.calculatePercentile(values, 0) shouldBe (42.0)
    PoolServiceImpl.calculatePercentile(values, 100) shouldBe (42.0)
  }
}
