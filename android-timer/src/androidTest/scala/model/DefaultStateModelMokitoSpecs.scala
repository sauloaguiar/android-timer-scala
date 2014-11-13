package model
import edu.luc.etl.scala.timer.model.state.{DefaultTimerWatchStateMachine}
/**
 * Created by Shilpika on 11/12/2014.
 */
class DefaultStateModelMokitoSpecs extends AbstractStateModelMockitoSpecs {
    override def fixtureSUT(dependencies: Dependencies) =
      new DefaultTimerWatchStateMachine(
        dependencies.timeModel, dependencies.clockModel, dependencies.uiUpdateListener)

}
