# TODO

* add lifecycle methods, esp. save/restore app state
* add Mockito
* update models from Java version
* review wrt DIP following
  [this post](http://lostechies.com/derickbailey/2011/09/22/dependency-injection-is-not-the-same-as-the-dependency-inversion-principle/)
  
# Learning Objectives

## Modeling

* Modeling state-dependent behavior with state machine diagrams
  (see also [here](/lucoodevcourse/stopwatch-android-scala/src/default/doc))
* Distinguishing between view states and (behavioral) model states

## Semantics

* Event-driven/asynchronous program execution
* User-triggered input events
* Internal events from background timers
* Concurrency issues: single-thread rule of accessing/updating the view in the GUI thread

## Architecture and Design

http://butunclebob.com/ArticleS.UncleBob.PrinciplesOfOod

* Key architectural issues and patterns
    * [Simple dependency injection (DI)](http://www.martinfowler.com/articles/injection.html)
    * [Model-view-adapter (MVA) architectural pattern](http://en.wikipedia.org/wiki/Model–view–adapter)
    * Mapping MVA to Android
    * [Difference between MVA and model-view-controller (MVC)](https://www.palantir.com/2009/04/model-view-adapter)
    * Distinguishing among dumb, reactive, and autonomous model components
* Key design patterns
    * Implementing event-driven behavior: [Observer pattern](http://sourcemaking.com/design_patterns/observer)
    * Implementing state-dependent behavior: [State pattern](http://sourcemaking.com/design_patterns/state)
    * Hiding complexity in the model from the adapter: [Façade pattern](http://sourcemaking.com/design_patterns/facade)
    * Representing tasks as objects: [Command pattern](http://sourcemaking.com/design_patterns/command) 
* [Relevant class-level design principles](http://butunclebob.com/ArticleS.UncleBob.PrinciplesOfOod)
    * Dependency Inversion Principle (DIP)
    * Single Responsibility Principle (SRP)
    * Interface Segregation Principle (ISP)
* Package-level architecture and [relevant principles](http://butunclebob.com/ArticleS.UncleBob.PrinciplesOfOod)
    * [Dependency graph](http://en.wikipedia.org/wiki/Dependency_graph)
      (see also [here](/lucoodevcourse/stopwatch-android-scala/src/default/doc))
    * Stable Dependencies Principle (SDP)
    * Acyclic Dependencies Principle (ADP)
* [Architectural journey](/lucoodevcourse/stopwatch-android-scala/commits)

## Testing

* Different types of testing
    * Component-level unit testing
    * System testing
    * Instrumentation testing
* Mock-based testing
* Testcase Superclass pattern
* Test coverage

# Building and Running

Please refer to [these notes](http://lucoodevcourse.bitbucket.org/notes/scalaandroiddev.html) for details.

