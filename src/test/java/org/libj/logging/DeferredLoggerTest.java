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

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;

public class DeferredLoggerTest extends LayoutBase<ILoggingEvent> {
  static final ArrayList<String> events = new ArrayList<>();

  @Override
  public String doLayout(final ILoggingEvent event) {
//    System.err.println(event.getFormattedMessage());
    events.add(event.getFormattedMessage());
    return "";
  }

  @Before
  public void before() {
    events.clear();
  }

  @Test
  public void test1() {
    for (final Level defaultLevel : Level.values()) { // [A]
      LoggerUtil.setLevel(LoggerFactory.getLogger(DeferredLoggerTest.class), defaultLevel);
      for (final Level deferredLevel : Level.values()) { // [A]
        final Logger logger = DeferredLogger.defer(LoggerFactory.getLogger(DeferredLoggerTest.class), deferredLevel);
        int loggable = 0;
        int printed = 0;
        for (final Level level : Level.values()) { // [A]
          if (LoggerUtil.isLoggable(logger, level)) {
            ++loggable;
            LoggerUtil.log(logger, level, level.toString());
            if (level.ordinal() <= defaultLevel.ordinal() && level.ordinal() > deferredLevel.ordinal())
              ++printed;
          }

          assertEquals(printed, events.size());
        }

        DeferredLogger.flush(deferredLevel);
        assertEquals(loggable, events.size());
        events.clear();
      }
    }
  }

  @Test
  public void test2() {
    LoggerUtil.setLevel(LoggerFactory.getLogger(DeferredLoggerTest.class), Level.WARN);
    final Logger logger = DeferredLogger.defer(LoggerFactory.getLogger(DeferredLoggerTest.class), Level.INFO);
    logger.trace("trace");
    assertEquals(0, events.size());
    logger.debug("debug");
    assertEquals(0, events.size());
    logger.info("info");
    assertEquals(0, events.size());

    LoggerFactory.getLogger("foo").info("foo info");
    assertEquals(1, events.size());
    events.clear();

    DeferredLogger.clear(logger);
    logger.warn("warn");
    assertEquals(0, events.size());
    LoggerFactory.getLogger(DeferredLoggerTest.class.getName() + ".SubClass").error("error");
    LoggerFactory.getLogger(DeferredLoggerTest.class.getName() + ".SubClass").warn("warn");
    LoggerFactory.getLogger(DeferredLoggerTest.class.getName() + ".SubClass").info("info");
    assertEquals(0, events.size());
    DeferredLogger.flush();
    assertEquals(3, events.size());
    events.clear();

    DeferredLogger.defer(LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME), Level.DEBUG).debug("debug");
    assertEquals(0, events.size());
    LoggerFactory.getLogger("bar").debug("debug");
    assertEquals(0, events.size());

    logger.error("error");
    assertEquals(0, events.size());
    DeferredLogger.flush();
    assertEquals(1, events.size());
  }
}