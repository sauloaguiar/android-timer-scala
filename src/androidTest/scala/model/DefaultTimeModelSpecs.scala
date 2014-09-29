package edu.luc.etl.cs313.scala.stopwatch.model

import time.DefaultTimeModel

/**
 * A concrete testcase subclass for StatelessBoundedCounter.
 * Run in sbt or Eclipse as a ScalaTest.
 */
class DefaultTimeModelSpecs extends AbstractTimeModelSpecs {
  override def fixture() = new DefaultTimeModel
}
