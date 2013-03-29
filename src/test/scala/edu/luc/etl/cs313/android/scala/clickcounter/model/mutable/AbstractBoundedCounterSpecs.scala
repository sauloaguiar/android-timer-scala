package edu.luc.etl.cs313.android.scala.clickcounter.model.mutable

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec

/**
 * An abstract unit test for the bounded counter model.
 * This follows the XUnit Testcase Superclass pattern.
 */
trait AbstractBoundedCounterSpecs extends FunSpec with ShouldMatchers {

  def fixture(): BoundedCounter

  describe("A counter") {
    it("has at least two values") {
      val f = fixture()
      f.min should be < f.max
    }
    it("is initially empty and not full") {
      val f = fixture()
      f.isEmpty should be (true)
      f.isFull should be (false)
    }
    it("increments correctly") {
      val f = fixture()
      f.isFull should be (false)
      val v = f.get
      f.increment()
      f.get should be (v + 1)
    }
    it("refuses to increment when full") {
      val f = fixture()
      while (! f.isFull) { f.increment() }
      f.isFull should be (true)
      evaluating { f.increment() } should produce [AssertionError]
    }
    it("decrements correctly") {
      val f = fixture()
      f.increment()
      f.isEmpty should be (false)
      val v = f.get
      f.decrement()
      f.get should be (v - 1)
    }
    it("refuses to decrement when empty") {
      val f = fixture()
      while (! f.isEmpty) { f.decrement() }
      f.isEmpty should be (true)
      evaluating { f.decrement() } should produce [AssertionError]
    }
    it("resets correctly") {
      val f = fixture()
      f.increment()
      f.isEmpty should be (false)
      f.decrement()
      f.get should be (f.min)
    }
  }
}
