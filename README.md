# TODO

* add external links to learning outcomes
* add lifecycle methods, esp. save/restore app state
* add Mockito
* review wrt DIP following
  [this post](http://lostechies.com/derickbailey/2011/09/22/dependency-injection-is-not-the-same-as-the-dependency-inversion-principle/)
* add code coverage plugin
* add dependency/stability metrics plugin
* consider other useful code quality plugins (checkstyle etc.)
* update models from Java version
  
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

* Key architectural issues and patterns
    * Simple dependency injection (DI)
    * Model-view-adapter (MVA) architectural pattern
    * Mapping MVA to Android
    * Difference between MVA and model-view-controller (MVC)
    * Distinguishing among dumb, reactive, and autonomous model components
* Key design patterns
    * Implementing event-driven behavior using the Observer pattern
    * Implementing state-dependent behavior using the State pattern
    * Command pattern for representing tasks as objects
    * Façade pattern for hiding complexity in the model from the adapter
* Relevant class-level design principles
    * Dependency Inversion Principle (DIP)
    * Single Responsibility Principle (SRP)
    * Interface Segregation Principle (ISP)
* Package-level architecture and relevant principles
    * Dependency graph
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

