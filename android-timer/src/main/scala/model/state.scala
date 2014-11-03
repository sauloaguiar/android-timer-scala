package model

/**
 * Created by sauloaguiar on 10/30/14.
 */
object state {

  trait UIHandling {
    def onStop()
    def onButtonPress()
  }

  trait timeBasedHandling {
    def threeSecWait()
    def onTimeout()
    //def onMaxValueReach()
  }

  /*
  START COUNTING
  STOP COUNTING
  RESET
  INCREMENT
  START SOUNDING
  STOP SOUNDING
  BEEP
  */
}
