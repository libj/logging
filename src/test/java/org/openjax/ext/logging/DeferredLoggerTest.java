/* Copyright (c) 2017 OpenJAX
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

package org.openjax.ext.logging;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;

public class DeferredLoggerTest extends LayoutBase<ILoggingEvent> {
  static final List<String> events = new ArrayList<>();

  @Override
  public String doLayout(final ILoggingEvent event) {
    events.add(event.getFormattedMessage());
    return "";
  }

  @Before
  public void before() {
    events.clear();
  }

  @Test
  public void test1() {
    final Logger logger = DeferredLogger.defer(LoggerFactory.getLogger(DeferredLoggerTest.class), Level.TRACE);
    logger.trace("trace");
    assertEquals(0, events.size());
    logger.debug("debug");
    assertEquals(0, events.size());
    logger.info("info");
    assertEquals(0, events.size());
    logger.warn("warn");
    assertEquals(1, events.size());
    logger.error("error");
    assertEquals(2, events.size());

    DeferredLogger.flush(Level.TRACE);
    assertEquals(5, events.size());
  }

  @Test
  public void test2() {
    final Logger logger = DeferredLogger.defer(LoggerFactory.getLogger(DeferredLoggerTest.class), Level.TRACE);
    logger.trace("trace");
    assertEquals(0, events.size());
    logger.debug("debug");
    assertEquals(0, events.size());
    logger.info("info");
    assertEquals(0, events.size());
    logger.warn("warn");
    assertEquals(1, events.size());
    logger.error("error");
    assertEquals(2, events.size());

    DeferredLogger.flush();
    assertEquals(5, events.size());
  }

  @Test
  public void test3() {
    final Logger logger = DeferredLogger.defer(LoggerFactory.getLogger(DeferredLoggerTest.class), Level.DEBUG);
    logger.trace("trace");
    assertEquals(0, events.size());
    logger.debug("debug");
    assertEquals(0, events.size());
    logger.info("info");
    assertEquals(0, events.size());
    logger.warn("warn");
    assertEquals(1, events.size());
    logger.error("error");
    assertEquals(2, events.size());

    DeferredLogger.flush(Level.TRACE);
    assertEquals(4, events.size());
  }

  @Test
  public void test4() {
    final Logger logger = DeferredLogger.defer(LoggerFactory.getLogger(DeferredLoggerTest.class), Level.DEBUG);
    logger.trace("trace");
    assertEquals(0, events.size());
    logger.debug("debug");
    assertEquals(0, events.size());
    logger.info("info");
    assertEquals(0, events.size());
    logger.warn("warn");
    assertEquals(1, events.size());
    logger.error("error");
    assertEquals(2, events.size());

    DeferredLogger.flush();
    assertEquals(4, events.size());
  }

  @Test
  public void test5() {
    final Logger logger = DeferredLogger.defer(LoggerFactory.getLogger(DeferredLoggerTest.class), Level.DEBUG);
    logger.trace("trace");
    assertEquals(0, events.size());
    logger.debug("debug");
    assertEquals(0, events.size());
    logger.info("info");
    assertEquals(0, events.size());
    logger.warn("warn");
    assertEquals(1, events.size());
    logger.error("error");
    assertEquals(2, events.size());

    DeferredLogger.flush(Level.INFO);
    assertEquals(3, events.size());
  }

  @Test
  public void test6() {
    final Logger logger = DeferredLogger.defer(LoggerFactory.getLogger(DeferredLoggerTest.class), Level.INFO);
    logger.trace("trace");
    assertEquals(0, events.size());
    logger.debug("debug");
    assertEquals(0, events.size());
    logger.info("info");
    assertEquals(0, events.size());
    logger.warn("warn");
    assertEquals(1, events.size());
    logger.error("error");
    assertEquals(2, events.size());

    DeferredLogger.flush(Level.TRACE);
    assertEquals(3, events.size());
  }

  @Test
  public void test7() {
    final Logger logger = DeferredLogger.defer(LoggerFactory.getLogger(DeferredLoggerTest.class), Level.INFO);
    logger.trace("trace");
    assertEquals(0, events.size());
    logger.debug("debug");
    assertEquals(0, events.size());
    logger.info("info");
    assertEquals(0, events.size());
    logger.warn("warn");
    assertEquals(1, events.size());
    logger.error("error");
    assertEquals(2, events.size());

    DeferredLogger.flush();
    assertEquals(3, events.size());
  }

  @Test
  public void test8() {
    final Logger logger = DeferredLogger.defer(LoggerFactory.getLogger(DeferredLoggerTest.class), Level.INFO);
    logger.trace("trace");
    assertEquals(0, events.size());
    logger.debug("debug");
    assertEquals(0, events.size());
    logger.info("info");
    assertEquals(0, events.size());
    LoggerFactory.getLogger("foo").info("foo info");
    assertEquals(1, events.size());
    DeferredLogger.clear(logger);
    logger.warn("warn");
    assertEquals(2, events.size());
    LoggerFactory.getLogger(DeferredLoggerTest.class.getName() + ".SubClass").warn("warn");
    assertEquals(3, events.size());
    DeferredLogger.defer(LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME), Level.DEBUG).debug("debug");
    assertEquals(3, events.size());
    LoggerFactory.getLogger("bar").debug("debug");
    assertEquals(3, events.size());
    logger.error("error");
    assertEquals(4, events.size());

    DeferredLogger.flush();
    assertEquals(6, events.size());
  }
}