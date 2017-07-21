/* Copyright (c) 2017 lib4j
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

package org.lib4j.logging;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class DeferredLogger {
  private static class FlushFilter extends Filter<ILoggingEvent> {
    private Level level;

    public void setLevel(final Level level) {
      this.level = level;
    }

    @Override
    public FilterReply decide(final ILoggingEvent event) {
      return level == null ? FilterReply.NEUTRAL : level.isGreaterOrEqual(event.getLevel()) ? FilterReply.ACCEPT : FilterReply.DENY;
    }
  }

  private static class AppenderBuffer {
    private final List<ILoggingEvent> events = new LinkedList<ILoggingEvent>();
    private final Appender<ILoggingEvent> appender;
    private final FlushFilter flushFilter = new FlushFilter();

    public AppenderBuffer(final Appender<ILoggingEvent> appender) {
      this.appender = appender;
      this.appender.addFilter(flushFilter);
    }

    public void addEvent(final ILoggingEvent event) {
      events.add(event);
    }

    public void clear() {
      events.clear();
    }

    public void flush(final Level level) {
      synchronized (flushFilter) {
        flushFilter.setLevel(level);
        appender.addFilter(new Filter<ILoggingEvent>() {
          @Override
          public FilterReply decide(final ILoggingEvent event) {
            return level.isGreaterOrEqual(event.getLevel()) ? FilterReply.ACCEPT : FilterReply.DENY;
          }
        });

        final Iterator<ILoggingEvent> iterator = events.iterator();
        while (iterator.hasNext()) {
          final ILoggingEvent event = iterator.next();
          iterator.remove();
          appender.doAppend(event);
        }

        flushFilter.setLevel(null);
      }
    }
  }

  private static final Map<Appender<ILoggingEvent>,DeferredLogger> deferrers = new LinkedHashMap<Appender<ILoggingEvent>,DeferredLogger>();

  public static void defer(final Logger logger, final Appender<ILoggingEvent> appender, final Level defaultLevel) {
    if (logger == null)
      throw new NullPointerException("logger == null");

    if (appender == null)
      throw new NullPointerException("appender == null");

    if (!logger.isAttached(appender))
      throw new IllegalArgumentException("appender is not attached to logger");

    logger.setLevel(Level.TRACE);
    DeferredLogger deferredFlushFilter = deferrers.get(appender);
    if (deferredFlushFilter == null) {
      final AppenderBuffer buffer = new AppenderBuffer(appender);
      deferrers.put(appender, deferredFlushFilter = new DeferredLogger(buffer, defaultLevel));
      appender.addFilter(new Filter<ILoggingEvent>() {
        @Override
        public FilterReply decide(final ILoggingEvent event) {
          if (!event.getLevel().isGreaterOrEqual(defaultLevel)) {
            buffer.addEvent(event);
            return FilterReply.DENY;
          }

          return FilterReply.ACCEPT;
        }
      });
    }
    else if (defaultLevel != deferredFlushFilter.defaultLevel) {
      deferredFlushFilter.setDefaultLevel(defaultLevel);
    }
  }

  public static void clear() {
    for (final Map.Entry<Appender<ILoggingEvent>,DeferredLogger> entry : deferrers.entrySet())
      entry.getValue().getBuffer().clear();
  }

  public static void flush(final Level level) {
    for (final Map.Entry<Appender<ILoggingEvent>,DeferredLogger> entry : deferrers.entrySet())
      entry.getValue().getBuffer().flush(level);
  }

  private final AppenderBuffer buffer;
  private Level defaultLevel;

  private DeferredLogger(final AppenderBuffer buffer, final Level defaultLevel) {
    this.defaultLevel = defaultLevel;
    this.buffer = buffer;
  }

  public AppenderBuffer getBuffer() {
    return this.buffer;
  }

  public void setDefaultLevel(final Level defaultLevel) {
    this.defaultLevel = defaultLevel;
  }
}