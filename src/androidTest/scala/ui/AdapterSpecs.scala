//package edu.luc.etl.cs313.scala.clickcounter
//package ui
//
//import org.mockito.Mockito._
//import org.scalatest.FunSpec
//import org.scalatest.mock.MockitoSugar
//
///**
// * A unit test of AbstractAdapter that uses mocking to satisfy the dependencies
// * (collaborators).
// */
//class AdapterSpecs extends FunSpec with MockitoSugar {
//
//  /**
//   * Trait for mock views.
//   */
//  trait View {
//    def update(): Unit
//  }
//
//  /**
//   * Factory method for test fixtures.
//   */
//  def fixture() = new {
//    // create mock instances of the collaborators
//    val min = 0
//    val max = 10
//    val view = mock[View]
//    val model = mock[BoundedCounter]
//    // stub certain methods
//    when(model.min).thenReturn(min)
//    when(model.max).thenReturn(max)
//    val order = inOrder(model, view)
//    // create subject-under-test (SUT)
//    val mdl = model
//    val adapter = new AbstractAdapter {
//      override lazy val model = mdl // injected dependency
//      override def updateView() {
//        view.update() // hard-coded dependency
//      }
//    }
//  }
//
//  describe("A clickcounter adapter") ({
//    it("handles onIncrement") ({
//      // create and import fixture
//      val f = fixture()
//      import f._
//      // exercise SUT
//      adapter.onIncrement(null)
//      // verify interaction with collaborators
//      order.verify(model).increment()
//      order.verify(view).update()
//    })
//    it("handles onDecrement") ({
//      // create and import fixture
//      val f = fixture()
//      import f._
//      // exercise SUT
//      adapter.onDecrement(null)
//      // verify interaction with collaborators
//      order.verify(model).decrement()
//      order.verify(view).update()
//    })
//    it("handles onReset") ({
//      // create and import fixture
//      val f = fixture()
//      import f._
//      // exercise SUT
//      adapter.onReset(null)
//      // verify interaction with collaborators
//      order.verify(model).reset()
//      order.verify(view).update()
//    })
//  })
//}
