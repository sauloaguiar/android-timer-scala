package edu.luc.etl.scala.timer
package model

import common.Constants._
import edu.luc.etl.scala.timer.model.time.{DefaultTimeModel, TimeModel}
import org.junit.Assert._
import org.junit.Test
import org.scalatest.junit.JUnitSuite


/**
 * Created by sauloaguiar on 11/5/14.
 */
trait AbstractTimeModelSpecs extends JUnitSuite{

  def fixture(): TimeModel

  @Test def testPreconditions(): Unit = {
    val model = fixture()
    assertEquals(0, model.getRuntime())
  }

  @Test def testIncrementOneCorrectly(): Unit = {
    val model = fixture()
    val rt = model.getRuntime()
    model.incRuntime()
    assertEquals((rt + SEC_PER_TICK) % MAX_VALUE, model.getRuntime())
  }

  @Test def testDecrementOneCorrectly(): Unit = {
    val model = fixture()
    val rt = model.getRuntime()
    model.setRuntime(1)
    model.decRuntime()
    assertEquals(0, model.getRuntime())
  }

  @Test def testHasReachedMaxValue(): Unit = {
    val model = fixture()
    (1 to MAX_VALUE) foreach { _ => model.incRuntime() }
    assertTrue(model.hasReachedMax())
  }

  @Test def testHasTimedOut(): Unit = {
    val model = fixture()
    assertTrue(model.hasTimedOut())
  }
}

class DefaultTimeModelSpecs extends AbstractTimeModelSpecs {
  override def fixture() = new DefaultTimeModel()
}
