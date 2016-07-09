# RestEasy/CDI Testing w/ embedded TJWS

With TJWS (TinyJavaWebServer) JBoss' RestEasy provides a JAX/RS capable embedded server with extreme small memory footprint and low startup time. This makes it a perfect fit for integration testing.

However, RestEasy (JAX/RS) comes rarely alone, in most cases other JEE technologies are also in the game. Very popular is to use RestEasy in combination with CDI, e.g. to inject services into REST resources. Then the plain `TJWSEmbeddedJaxrsServer` embedded server class provided by RestEasy starts to fail. The root cause is, that unlike in full-blown JBoss application container, there's no CDI integration with TJWS in place.
This project demonstrates, how it's possible to overcome missing CDI integration for testing purposes. For production such an approach is **not** recommended (and TJWS isn't meant to be used in production anyway.)

## Problem

The effect of missing CDI integration with TJWS are injected collaborators being `null`, which usually causes `NullPointerException`s while tests are executed. Actually, outside of the application container a CDI container isn't even started.

The naive approach of starting a Java SE CDI container, like _Weld for Java SE_ (Artifact: `org.jboss.weld.se:weld-se-core`), isn't sufficient to get injected collaborators resolved. There's more to be happen:

1. CDI need to be told that, beside CDI beans, JAX/RS resource classes are also target of dependency injection.

2. CDI contexts must be started and stopped at proper point in time to ensure similar behavior of application compared to deployment in JBoss server.

The goal is that eventually the [sample resource class](./src/main/java/net/gunther/app/resources/GreetingsResource.java) with [injected service](./src/main/java/net/gunther/app/resources/GreeterService.java) should be testable by [test class](./src/test/java/net/gunther/app/resources/GreetingsResourceTest.java). 
In order to allow easy browsing in sample classes, I recommend to clone the repository and import the project into IDE of your choice.

## Solution

The first piece of the solution I already mentioned: Weld CDI container for Java SE can be started programmatically, please see [utility class](./src/test/java/net/gunther/testing/CdiUtil.java).

Second, dependency injection into JAX/RS resource classes, which are managed by RestEasy, need to be enabled by configuring an _injector factory_. The injector factory comes with RestEasy and allows adaption of different DI frameworks to work together with JAX/RS resources. RestEasy comes with `CdiInjectorFactory` class, which need to be tweaked a bit to do its job properly in a Java SE environment:
[Derived custom injector factory](./src/test/java/net/gunther/testing/WeldInjectorFactory.java) works well with programmatically started Weld CDI container.

In addition, the [custom servlet dispatcher](./src/test/java/net/gunther/testing/WeldAwareServletDispatcher.java) takes care of setting up CDI's request context for each JAX/RS request.

Eventually, the [embedded server class](./src/test/java/net/gunther/testing/TJWSEmbeddedJaxrsCdiServer.java) brings all pieces together.

Javadoc of classes referenced in this chapter provide more details. 

## Conclusion

Integration testing with embedded TJWS is a rather light-weight approach. Nonetheless, with CDI enabled, we do have a very powerful tool at hand. We can then, for example, encapsulate other JEE technologies by CDI `@Producer`s. For integration testing mocks or test infrastructure may be activated by `@Alternative` implementations.

### Alternatives?

Well, there are always other ways to go, and as always, it depends on project situation, developer's experience and sensible balancing of reasons, which one is the best. Just some ideas:

* [RestEasy's server-side Mock framework](https://docs.jboss.org/resteasy/docs/1.1.GA/userguide/html/RESTEasy_Server-side_Mock_Framework.html) can be a sufficient approach in some situations.

* [Arquilian](http://arquillian.org/), which is an JEE integration testing framework based on full-blown embedded JEE containers. Arquilian for integration testing is rather heavy-weight, in regard of both, test setup and duration of test execution. But in some project situations it might be a good fit (IMHO not in general :-).

* Favor unit and system testing over integration testing: TDD developers reach good test coverage solely with unit tests. The remaining testing requirements can often be satisfied by automated tests of deployed application, AKA end-to-end tests.

### The End

The sample project shows, that it doesn't require much effort to make things happen, less than 50 lines of code glue RestEasy, TJWS, and CDI in a way together, so that it becomes applicable for integration testing. Isn't that awesome?

In that sense, happy testing...
