package edu.luc.etl.cs313.scala.stopwatch
package model

import org.junit.Assert._
import org.junit.Test
import org.scalatest.junit.JUnitSuite
import common.Constants._
import time.TimeModel

/**
 * An abstract unit test for the time model abstraction.
 * This is a simple unit test of an object without dependencies.
 * This follows the XUnit Testcase Superclass pattern.
 */
trait AbstractTimeModelSpecs extends JUnitSuite {

  def fixture(): TimeModel

  /** Verifies that runtime and laptime are initially 0 or less. */
  @Test def testPreconditions(): Unit = {
    val model = fixture()
    assertEquals(0, model.getRuntime)
    assertTrue(model.getLaptime <= 0)
  }

  /** Verifies that runtime is incremented correctly. */
  @Test def testIncrementRuntimeOne(): Unit = {
    val model = fixture()
    val rt = model.getRuntime
    val lt = model.getLaptime
    model.incRuntime()
    assertEquals((rt + SEC_PER_TICK) % SEC_PER_MIN, model.getRuntime)
    assertEquals(lt, model.getLaptime)
  }

  /** Verifies that runtime turns over correctly. */
  @Test def testIncrementRuntimeMany(): Unit = {
    val model = fixture()
    val rt = model.getRuntime
    val lt = model.getLaptime
    (1 to SEC_PER_HOUR) foreach { _ => model.incRuntime() }
    assertEquals(rt, model.getRuntime)
    assertEquals(lt, model.getLaptime)
  }

  /** Verifies that laptime works correctly. */
  @Test def testLaptime(): Unit = {
    val model = fixture()
    val rt = model.getRuntime
    val lt = model.getLaptime
    (1 to 5) foreach { _ => model.incRuntime() }
    assertEquals(rt + 5, model.getRuntime)
    assertEquals(lt, model.getLaptime)
    model.setLaptime()
    assertEquals(rt + 5, model.getLaptime)
    (1 to 5) foreach { _ => model.incRuntime() }
    assertEquals(rt + 10, model.getRuntime)
    assertEquals(rt + 5, model.getLaptime)
  }
}
