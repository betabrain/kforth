package kforth

import org.testng.Assert.assertEquals
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class KForthTest {

  private lateinit var kForth: KForth

  @BeforeMethod
  fun setUp() {
    this.kForth = KForth()
  }

  @AfterMethod
  fun tearDown() {
    assert(kForth.stack().isEmpty())
    kForth.eval(".s")
    kForth.eval(".r")
    kForth.eval(".c")
    kForth.eval("dump")
  }

  @Test
  fun testPushPop() {
    kForth.push(5)
    assertEquals(kForth.pop(), 5)
  }

}