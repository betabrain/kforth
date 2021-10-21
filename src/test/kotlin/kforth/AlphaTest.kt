package kforth

import org.testng.Assert.assertEquals
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class AlphaTest {

  private lateinit var alpha: Alpha

  @BeforeMethod
  fun setUp() {
    this.alpha = Alpha()
  }

  @AfterMethod
  fun tearDown() {
    assert(alpha.stack().isEmpty())
    alpha.eval(".s")
    alpha.eval(".r")
    alpha.eval(".c")
    alpha.eval("dump")
  }

  @Test
  fun testPushPop() {
    alpha.push(5)
    assertEquals(alpha.pop(), 5)
  }

}