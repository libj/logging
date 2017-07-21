/* Copyright (c) 2016 lib4j
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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public final class Logging {
  private static final Map<Level,ch.qos.logback.classic.Level> levelToLevel = new HashMap<Level,ch.qos.logback.classic.Level>();

  static {
    levelToLevel.put(Level.TRACE, ch.qos.logback.classic.Level.TRACE);
    levelToLevel.put(Level.DEBUG, ch.qos.logback.classic.Level.DEBUG);
    levelToLevel.put(Level.INFO, ch.qos.logback.classic.Level.INFO);
    levelToLevel.put(Level.WARN, ch.qos.logback.classic.Level.WARN);
    levelToLevel.put(Level.ERROR, ch.qos.logback.classic.Level.ERROR);
  }

  public static void setLevel(final Level level) {
    ((ch.qos.logback.classic.Logger)LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME)).setLevel(levelToLevel.get(level));
  }

  public static void setLevel(final Class<?> clazz, final Level level) {
    final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(clazz);
    if (logger != null)
      logger.setLevel(levelToLevel.get(level));
  }

  private Logging() {
  }
}