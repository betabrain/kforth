package kforth

import org.testng.Assert.assertEquals
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class KForthAlphaTest {

  private lateinit var kForthAlpha: KForthAlpha

  @BeforeMethod
  fun setUp() {
    this.kForthAlpha = KForthAlpha()
  }

  @AfterMethod
  fun tearDown() {
    assert(kForthAlpha.stack().isEmpty())
    kForthAlpha.eval(".s")
    kForthAlpha.eval(".r")
    kForthAlpha.eval(".c")
    kForthAlpha.eval("dump")
  }

  @Test
  fun testPushPop() {
    kForthAlpha.push(5)
    assertEquals(kForthAlpha.pop(), 5)
  }

}