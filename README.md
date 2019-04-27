# OpenJAX Extensions Logging

> Java API Extension for Logging

[![Build Status](https://travis-ci.org/openjax/ext-logging.png)](https://travis-ci.org/openjax/ext-logging)
[![Coverage Status](https://coveralls.io/repos/github/openjax/ext-logging/badge.svg)](https://coveralls.io/github/openjax/ext-logging)
[![Javadocs](https://www.javadoc.io/badge/org.openjax.ext/logging.svg)](https://www.javadoc.io/doc/org.openjax.ext/logging)
[![Released Version](https://img.shields.io/maven-central/v/org.openjax.ext/logging.svg)](https://mvnrepository.com/artifact/org.openjax.ext/logging)

## Introduction

The OpenJAX Extensions Logging library is a collection of supplementary patterns for the [Simple Logging Facade for Java (SLF4J)][slf4j] and the [Logback Project][logback].

## Classes

### [ColorConverter](src/main/java/org/openjax/ext/logging/ColorConverter.java)

A `CompositeConverter` implementation that colorizes log messages.

### [DeferredLogger](src/main/java/org/openjax/ext/loggong/DeferredLogger.java)
Logger that defers output of log events until flushed. The `DeferredLogger` addresses a common use-case: Consider an application that is complex, and has considerable trace and debug log statements throughout its code. This application also has a long running test phase, where the same code is tested numerous times for different input. If the test phase is successful, the detailed trace and debug log statements flood the console buffers. If the test phase fails, the detailed trace and debug log statements are principle in helping the developer diagnose the problem. In order to satisfy both the success and failure cases, there exist a couple of approaches:
1. By default, set the log level to `INFO`. This would ensure the success case does not flood the logs. If there is a failure, however, the developer would have to set the log level to `TRACE`, and restart the long running test phase. Additionally, the log statements from this test phase would be outputted regardless of whether the specific operation led to the error or not. The developer would therefore have to filter through the entire log output to find the events corresponding to the error.
2. Alternatively, consider a solution that uses the `DeferredLogger`. The `DeferredLogger` is configured with two parameters:
    1. The default log level: Specified by the current level set for the logger (e.g. `INFO`).
    2. The deferred log level: Specifies the lower level of events to defer for later output (e.g. `TRACE`).

With this configuration, the `DeferredLogger` allows events with a level of `INFO` or above to be outputted to the console, while events with a level from `TRACE` (inclusive) to `INFO` (exclusive) will be deferred. The developer may use this pattern to thereafter include a call to `DeferredLogger#flush()` in a catch block that may be invoked by an exception (which will happen in the case of the error). When `DeferredLogger#flush()` is called, the deferred log statements from `TRACE` (inclusive) to `INFO` (exclusive) are outputted. To help reduce the number of irrelevant log events outputted during the test phase, the developer may include a call to `DeferredLogger#clear()` at the end of each test method (meaning the test was successful). When `DeferredLogger#clear()` is called, the buffer of deferred log statements is cleared.

With the `DeferredLogger`, trace log statements will be outputted only if an exception triggers `DeferredLogger#flush()`. This logging pattern can be used to produce output of trace log statements that are directly related to the error in question. The `DeferredLogger` is only applicable to the [Logback][logback] implementation of `org.slf4j.Logger` instances.

#### Usage

The following example illustrates how to use the `DeferredLogger`.

Consider a `logback.xml` that has the root level set to `WARN`:

```xml
<configuration
  ...
  <root level="WARN">
  ...
</configuration>
```

With this configuration, consider the following use-case:

```java
Logger logger = DeferredLogger.defer(LoggerFactory.getLogger(MyClass.class), Level.DEBUG);
logger.trace("trace"); // Not logged
logger.debug("debug"); // Deferred
logger.info("info");   // Deferred
logger.warn("warn");   // Logged
logger.error("error"); // Logged

// Flushes the deferred logs (debug and info)
DeferredLogger.flush(Level.TRACE);
```

### [DelegateLogger](src/main/java/org/openjax/ext/logging/DelegateLogger.java)

A `DelegateLogger` contains some other `Logger`, possibly transforming the method parameters along the way or providing additional functionality. The class `DelegateLogger` itself simply overrides all methods of `Logger` with versions that delegate all calls to the source `Logger`. Subclasses of `DelegateLogger` may further override some of these methods and may also provide additional methods and fields.

### [LoggerPrintWriter](src/main/java/org/openjax/ext/logging/LoggerPrintWriter.java)

A `PrintWriter` that delegates its methods to a target `Logger`. The `print()`, `println()`, `write()`, and `append()` methods will result in a new log event only if the log message ends with a `'\n'` character. If a message does not end with a `'\n'` character, it will be buffered until a `'\n'` character is encountered as the last character of a later call.

### [LoggerUtil](src/main/java/org/openjax/ext/logging/LoggerUtil.java)

Utility functions for operations pertaining to `Logger`.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

### License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

[logback]: https://logback.qos.ch/
[slf4j]: https://www.slf4j.org/