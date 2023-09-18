/* Copyright (c) 2017 LibJ
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.libj.logging;

import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * Logger that defers output of log events until flushed.
 * <p>
 * The {@link DeferredLogger} addresses a common use-case:
 * <p>
 * Consider an application that is complex, and has considerable trace and debug log statements throughout its code. This
 * application also has a long running test phase, where the same code is tested numerous times for different input. If the test
 * phase is successful, the detailed trace and debug log statements flood the console buffers. If the test phase fails, the detailed
 * trace and debug log statements are principle in helping the developer diagnose the problem. In order to satisfy both the success
 * and failure cases, there exist a couple of approaches:
 * <ol>
 * <li>By default, set the log level to {@code INFO}. This would ensure the success case does not flood the logs. If there is a
 * failure, however, the developer would have to set the log level to {@code TRACE}, and restart the long running test phase.
 * Additionally, the log statements from this test phase would be outputted regardless of whether the specific operation led to the
 * error or not. The developer would therefore have to filter through the entire log output to find the events corresponding to the
 * error.</li>
 * <li>Alternatively, consider a solution that uses the {@link DeferredLogger}. The {@link DeferredLogger} is configured with two
 * parameters:
 * <ol>
 * <li>The default log level: Specified by the current level set for the logger (e.g. {@code INFO}).</li>
 * <li>The deferred log level: Specifies the lower level of events to defer for later output (e.g. {@code TRACE}).</li>
 * </ol>
 * With this configuration, the {@link DeferredLogger} allows events with a level of {@code INFO} or above to be outputted to the
 * console, while events with a level from {@code TRACE} (inclusive) to {@code INFO} (exclusive) will be deferred. The developer may
 * use this pattern to thereafter include a call to {@link DeferredLogger#flush()} in a catch block that may be invoked by an
 * exception (which will happen in the case of the error). When {@link DeferredLogger#flush()} is called, the deferred log
 * statements from {@code TRACE} (inclusive) to {@code INFO} (exclusive) are outputted. To help reduce the number of irrelevant log
 * events outputted during the test phase, the developer may include a call to {@link DeferredLogger#clear()} at the end of each
 * test method (meaning the test was successful). When {@link DeferredLogger#clear()} is called, the buffer of deferred log
 * statements is cleared.</li>
 * </ol>
 * With the {@link DeferredLogger}, trace log statements will be outputted only if an exception triggers
 * {@link DeferredLogger#flush()}. This logging pattern can be used to produce output of trace log statements that are directly
 * related to the error in question.
 * <p>
 * <b>The {@link DeferredLogger} is only applicable to the <a href="https://logback.qos.ch/">Logback</a> implementation of
 * {@link org.slf4j.Logger} instances.</b>
 */
public final class DeferredLogger {
  private static class FlushFilter extends Filter<ILoggingEvent> {
    private Level level;

    /**
     * Sets the {@link Level} of this {@link FlushFilter}.
     *
     * @param level The {@link Level}.
     */
    public void setLevel(final Level level) {
      this.level = level;
    }

    @Override
    public FilterReply decide(final ILoggingEvent event) {
      return level == null ? FilterReply.NEUTRAL : event.getLevel().isGreaterOrEqual(level) ? FilterReply.ACCEPT : FilterReply.DENY;
    }
  }

  private final class AppenderBuffer {
    private final FlushFilter flushFilter = new FlushFilter();
    private final Deque<ILoggingEvent> events;
    private final Appender<ILoggingEvent> appender;

    /**
     * Creates a new {@link AppenderBuffer} with the specified {@link Appender} to which deferred events will be flushed.
     *
     * @throws NullPointerException If {@code appender} is null.
     */
    private AppenderBuffer() {
      this.appender = getAppender(logger);
      this.appender.addFilter(flushFilter);

      this.events = listSupplier.get();

      appender.addFilter(new Filter<ILoggingEvent>() {
        @Override
        public FilterReply decide(final ILoggingEvent event) {
          if (!matchesLogger(event, logger, appender))
            return FilterReply.NEUTRAL;

          final Level level = event.getLevel();
          final boolean loggable = level.levelInt >= logger.getEffectiveLevel().levelInt;
          if (level.levelInt < deferredLevel.levelInt)
            return loggable ? FilterReply.ACCEPT : FilterReply.DENY;

          if (loggable)
            addEvent(event);

          return FilterReply.DENY;
        }
      });
    }

    /**
     * Adds a {@link ILoggingEvent} that will be deferred for later output.
     *
     * @param event The {@link ILoggingEvent} that will be deferred for later output.
     */
    private void addEvent(final ILoggingEvent event) {
      events.add(event);
      for (int i = maxEvents, i$ = events.size(); i < i$; ++i) // [N]
        events.removeFirst();
    }

    /**
     * Clears the buffer of deferred events.
     */
    private void clear() {
      lock.lock();
      events.clear();
      lock.unlock();
    }

    /**
     * Flushes the buffer of deferred events. This method will invoke the default {@link Appender#doAppend(Object)} method for each
     * event that satisfies the specified {@code level}.
     *
     * @param level The lowest {@link Level} condition for events to be flushed. If an event has a level lower than {@code level},
     *          it will not be flushed.
     */
    private void flush(final Level level) {
      lock.lock();
      flushFilter.setLevel(level);
      for (int i = 0, i$ = events.size(); i < i$; ++i) { // [RA]
        final ILoggingEvent event = events.pollFirst();
        if (event != null && event.getLevel().isGreaterOrEqual(level))
          appender.doAppend(event);
      }

      flushFilter.setLevel(null);
      lock.unlock();
    }
  }

  private static final LinkedHashMap<org.slf4j.Logger,DeferredLogger> deferrers = new LinkedHashMap<>();

  /**
   * Returns the first {@link Appender} of the specified {@link Logger}, or if one does not exist, the first {@link Appender} of the
   * ROOT logger.
   *
   * @param logger The {@link Logger}.
   * @return The first {@link Appender} of the specified {@link Logger}, or if one does not exist, the first {@link Appender} of the
   *         ROOT logger.
   * @throws IllegalStateException If the specified {@link Logger} and the ROOT logger do not have an appender.
   * @throws NullPointerException If {@code logger} is null.
   */
  private static Appender<ILoggingEvent> getAppender(final Logger logger) {
    if (logger.iteratorForAppenders().hasNext())
      return logger.iteratorForAppenders().next();

    final Logger rootLogger = (Logger)LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
    if (!rootLogger.iteratorForAppenders().hasNext())
      throw new IllegalStateException("ROOT logger does not have an appender");

    return rootLogger.iteratorForAppenders().next();
  }

  /**
   * Configures the specified {@link org.slf4j.Logger} to defer log events with a level between:
   * <ol>
   * <li>The level for {@code logger} that is configured in {@code logback.xml}.</li>
   * <li>The level specified by {@code deferredLevel}.</li>
   * </ol>
   *
   * @param logger The logger to configure to defer log events with a level between (1) and (2) above.
   * @param deferredLevel The lowest {@link org.slf4j.event.Level} that will be deferred for later output.
   * @return The specified {@link Logger}.
   * @throws ClassCastException If {@code logger} is not an instance of {@link ch.qos.logback.classic.Logger}.
   * @throws IllegalStateException If the specified {@link Logger} and the root logger do not have an appender.
   * @throws NullPointerException If the specified {@link org.slf4j.event.Level} or {@code logger} is null.
   */
  public static org.slf4j.Logger defer(final org.slf4j.Logger logger, final org.slf4j.event.Level deferredLevel, final int maxEvents, final Supplier<Deque> listSupplier) {
    return defer((Logger)logger, LoggerUtil.logbackLevel[deferredLevel.ordinal()], maxEvents, listSupplier);
  }

  /**
   * Configures the specified {@link org.slf4j.Logger} to defer log events with a level between:
   * <ol>
   * <li>The level for {@code logger} that is configured in {@code logback.xml}.</li>
   * <li>The level specified by {@code deferredLevel}.</li>
   * </ol>
   *
   * @param logger The logger to configure to defer log events with a level between (1) and (2) above.
   * @param deferredLevel The lowest {@link org.slf4j.event.Level} that will be deferred for later output.
   * @return The specified {@link Logger}.
   * @throws ClassCastException If {@code logger} is not an instance of {@link ch.qos.logback.classic.Logger}.
   * @throws IllegalStateException If the specified {@link Logger} and the root logger do not have an appender.
   * @throws NullPointerException If the specified {@link org.slf4j.event.Level} or {@code logger} is null.
   */
  public static org.slf4j.Logger defer(final org.slf4j.Logger logger, final org.slf4j.event.Level deferredLevel, final Supplier<Deque> listSupplier) {
    return defer((Logger)logger, LoggerUtil.logbackLevel[deferredLevel.ordinal()], Integer.MAX_VALUE, listSupplier);
  }

  /**
   * Configures the specified {@link org.slf4j.Logger} to defer log events with a level between:
   * <ol>
   * <li>The level for {@code logger} that is configured in {@code logback.xml}.</li>
   * <li>The level specified by {@code deferredLevel}.</li>
   * </ol>
   *
   * @param logger The logger to configure to defer log events with a level between (1) and (2) above.
   * @param deferredLevel The lowest {@link org.slf4j.event.Level} that will be deferred for later output.
   * @return The specified {@link Logger}.
   * @throws ClassCastException If {@code logger} is not an instance of {@link ch.qos.logback.classic.Logger}.
   * @throws IllegalStateException If the specified {@link Logger} and the root logger do not have an appender.
   * @throws NullPointerException If the specified {@link org.slf4j.event.Level} or {@code logger} is null.
   */
  public static org.slf4j.Logger defer(final org.slf4j.Logger logger, final org.slf4j.event.Level deferredLevel) {
    return defer((Logger)logger, LoggerUtil.logbackLevel[deferredLevel.ordinal()], Integer.MAX_VALUE, LinkedList::new);
  }

  /**
   * Configures the specified {@link Logger} to defer log events with a level between:
   * <ol>
   * <li>The level for {@code logger} that is configured in {@code logback.xml}.</li>
   * <li>The level specified by {@code deferredLevel}.</li>
   * </ol>
   *
   * @param logger The logger to configure to defer log events with a level between (1) and (2) above.
   * @param deferredLevel The lowest {@link Level} that will be deferred for later output.
   * @return The specified {@link Logger}.
   * @throws IllegalStateException If the specified {@link Logger} and the root logger do not have an appender.
   * @throws NullPointerException If {@code logger} or {@code deferredLevel} is null.
   */
  private static org.slf4j.Logger defer(final Logger logger, final Level deferredLevel, final int maxEvents, final Supplier<Deque> listSupplier) {
    synchronized (deferrers) {
      DeferredLogger deferredLogger = deferrers.get(logger);
      if (deferredLogger == null)
        deferrers.put(logger, deferredLogger = new DeferredLogger(logger, maxEvents, listSupplier));

      deferredLogger.setDeferredLevel(deferredLevel);
    }

    return logger;
  }

  /**
   * Tests whether the specified {@link ILoggingEvent} matches the specified {@link Logger} and {@link Appender}.
   * <p>
   * The algorithm to determine whether {@code event} matches the {@code logger} and {@code appender} is as follows:
   * <ol>
   * <li>If {@code logger.getName()} is equal to "ROOT"
   * <ol>
   * <li>{@code true} if {@code event.getLoggerName()} is equal to "ROOT".</li>
   * <li>{@code true} if the {@link Appender} is <i>not</i> owned by the {@link Logger} of {@code event} (e.g. If the {@link Logger}
   * of {@code event} does not have its own {@link Appender}, then the "ROOT" appender applies).</li>
   * <li>{@code false} otherwise.</li>
   * </ol>
   * </li>
   * <li>If {@code logger.getName().length()} is equal to {@code event.getLoggerName().length()}
   * <ol>
   * <li>{@code true} if {@code logger.getName()} is equal to {@code event.getLoggerName()}.</li>
   * <li>{@code false} otherwise.</li>
   * </ol>
   * </li>
   * <li>If {@code logger.getName().length()} is less than {@code event.getLoggerName().length()}
   * <ol>
   * <li>{@code true} if {@code event.getLoggerName()} starts with {@code logger.getName() + "."}.</li>
   * <li>{@code false} otherwise.</li>
   * </ol>
   * </li>
   * </ol>
   *
   * @param event The {@link ILoggingEvent}.
   * @param logger The {@link Logger}.
   * @param appender The {@link Appender}.
   * @return {@code true} if the specified {@link ILoggingEvent} matches the specified {@link Logger} and {@link Appender};
   *         otherwise {@code false}.
   */
  private boolean matchesLogger(final ILoggingEvent event, final Logger logger, final Appender<ILoggingEvent> appender) {
    final String eventLoggerName = event.getLoggerName();
    if (isRootLogger)
      return org.slf4j.Logger.ROOT_LOGGER_NAME.equals(eventLoggerName) || !((Logger)LoggerFactory.getLogger(event.getLoggerName())).isAttached(appender);

    final char ch;
    return eventLoggerName.startsWith(loggerName) && (eventLoggerName.length() == loggerNameLength || (ch = eventLoggerName.charAt(loggerNameLength)) == '.' || ch == '$');
  }

  /**
   * Clears the buffers of deferred events for all deferred loggers.
   */
  public static void clear() {
    if (deferrers.size() > 0)
      for (final DeferredLogger dererrer : deferrers.values()) // [C]
        dererrer.buffer.clear();
  }

  /**
   * Clears the buffers of deferred events for the specified {@link org.slf4j.Logger}.
   *
   * @param logger The deferred {@link org.slf4j.Logger}.
   * @throws IllegalArgumentException If the specified {@link org.slf4j.Logger} is not a {@link DeferredLogger}.
   */
  public static void clear(final org.slf4j.Logger logger) {
    final DeferredLogger deferredLogger = deferrers.get(logger);
    if (deferredLogger == null)
      throw new IllegalArgumentException("The specified logger is not a " + DeferredLogger.class.getSimpleName());

    deferredLogger.buffer.clear();
  }

  /**
   * Flushes the buffer of deferred events for all deferred loggers. This method will invoke the default
   * {@link Appender#doAppend(Object)} method for each event that satisfies the specified {@code level}.
   *
   * @param level The lowest {@link Level} condition for events to be flushed. If an event has a level lower than {@code level}, it
   *          will not be flushed.
   */
  public static void flush(final org.slf4j.event.Level level) {
    if (deferrers.size() > 0)
      for (final DeferredLogger dererrer : deferrers.values()) // [C]
        dererrer.buffer.flush(LoggerUtil.logbackLevel[level.ordinal()]);
  }

  /**
   * Flushes the buffer of deferred events for all deferred loggers. This method will invoke the default
   * {@link Appender#doAppend(Object)} method for each event with level at or above the {@code deferredLevel} (specified in
   * {@link DeferredLogger#defer(org.slf4j.Logger,org.slf4j.event.Level)}), and below the default level set in {@code logback.xml}.
   */
  public static void flush() {
    if (deferrers.size() > 0)
      for (final DeferredLogger dererrer : deferrers.values()) // [C]
        dererrer.buffer.flush(dererrer.logger.getLevel());
  }

  /**
   * Flushes the buffer of deferred events for all deferred loggers. This method will invoke the default
   * {@link Appender#doAppend(Object)} method for each event that satisfies the specified {@code level}.
   *
   * @param logger The deferred {@link org.slf4j.Logger}.
   * @param level The lowest {@link Level} condition for events to be flushed. If an event has a level lower than {@code level}, it
   *          will not be flushed.
   * @throws IllegalArgumentException If the specified {@link org.slf4j.Logger} is not a {@link DeferredLogger}.
   */
  public static void flush(final org.slf4j.Logger logger, final org.slf4j.event.Level level) {
    final DeferredLogger deferredLogger = deferrers.get(logger);
    if (deferredLogger == null)
      throw new IllegalArgumentException("The specified logger is not a " + DeferredLogger.class.getSimpleName());

    deferredLogger.buffer.flush(LoggerUtil.logbackLevel[level.ordinal()]);
  }

  /**
   * Flushes the buffer of deferred events for all deferred loggers. This method will invoke the default
   * {@link Appender#doAppend(Object)} method for each event with level at or above the {@code deferredLevel} (specified in
   * {@link DeferredLogger#defer(org.slf4j.Logger,org.slf4j.event.Level)}), and below the default level set in {@code logback.xml}.
   *
   * @param logger The deferred {@link org.slf4j.Logger}.
   * @throws IllegalArgumentException If the specified {@link org.slf4j.Logger} is not a {@link DeferredLogger}.
   */
  public static void flush(final org.slf4j.Logger logger) {
    final DeferredLogger deferredLogger = deferrers.get(logger);
    if (deferredLogger == null)
      throw new IllegalArgumentException("The specified logger is not a " + DeferredLogger.class.getSimpleName());

    deferredLogger.buffer.flush(deferredLogger.logger.getLevel());
  }

  private final ReentrantLock lock = new ReentrantLock();
  private final Logger logger;
  private final String loggerName;
  private final boolean isRootLogger;
  private final int loggerNameLength;
  private final AppenderBuffer buffer;
  private final int maxEvents;
  private final Supplier<Deque> listSupplier;
  private Level deferredLevel;

  /**
   * Creates a new {@link DeferredLogger} with the specified parameters.
   *
   * @param logger The {@link Logger}.
   * @param maxEvents The maximum number of events to buffer.
   * @param listSupplier The {@link Supplier} to create the instance of the {@link Deque} buffer.
   * @throws NullPointerException If {@code logger} or {@code listSupplier} is null.
   */
  private DeferredLogger(final Logger logger, final int maxEvents, final Supplier<Deque> listSupplier) {
    this.logger = logger;
    this.loggerName = logger.getName();
    this.loggerNameLength = loggerName.length();
    this.isRootLogger = org.slf4j.Logger.ROOT_LOGGER_NAME.equals(loggerName);

    if ((this.maxEvents = maxEvents) <= 0)
      throw new IllegalArgumentException("maxEvents (" + maxEvents + ") must be positive");

    this.listSupplier = Objects.requireNonNull(listSupplier, "listSupplier is null");
    this.buffer = new AppenderBuffer();
  }

  /**
   * Set the {@code deferredLevel} value.
   *
   * @param deferredLevel The deferred {@link Level} value.
   */
  private void setDeferredLevel(final Level deferredLevel) {
    this.deferredLevel = deferredLevel;
  }
}