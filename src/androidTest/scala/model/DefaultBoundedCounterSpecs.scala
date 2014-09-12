package edu.luc.etl.cs313.scala.clickcounter.model

/**
 * A concrete testcase subclass for StatelessBoundedCounter.
 * Run in sbt or Eclipse as a ScalaTest.
 */
class DefaultBoundedCounterSpecs extends AbstractBoundedCounterSpecs {
  override def fixture() = new DefaultBoundedCounter(0, 10)
}
