package edu.luc.etl.cs313.scala.stopwatch
package model

import common.listeners.StopwatchUIListener

/**
 * A thin model facade. Following the Facade pattern,
 * this isolates the complexity of the model from its clients
 * (usually the adapter).
 */
trait StopwatchModelFacade extends StopwatchUIListener { def onStart(): Unit }