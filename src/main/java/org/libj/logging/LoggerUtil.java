/* Copyright (c) 2016 LibJ
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

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;

/**
 * Utility functions for operations pertaining to {@link Logger}.
 */
public final class LoggerUtil {
  /**
   * A mapping from {@link Level} to its corresponding
   * {@link ch.qos.logback.classic.Level}.
   */
  static final Map<Level,ch.qos.logback.classic.Level> levelToLevel;

  static {
    final Map<Level,ch.qos.logback.classic.Level> map = new EnumMap<>(Level.class);
    map.put(Level.TRACE, ch.qos.logback.classic.Level.TRACE);
    map.put(Level.DEBUG, ch.qos.logback.classic.Level.DEBUG);
    map.put(Level.INFO, ch.qos.logback.classic.Level.INFO);
    map.put(Level.WARN, ch.qos.logback.classic.Level.WARN);
    map.put(Level.ERROR, ch.qos.logback.classic.Level.ERROR);
    levelToLevel = Collections.unmodifiableMap(map);
  }

  /**
   * Programmatically sets the {@link Level} of the specified {@link Logger}.
   * <p>
   * <b>This method is only applicable to the
   * <a href="https://logback.qos.ch/">LogBack</a> implementation of
   * {@link Logger} instances.</b>
   *
   * @param logger The {@link Logger}.
   * @param level The {@link Level}.
   * @throws ClassCastException If {@code logger} is not an instance of
   *           {@link ch.qos.logback.classic.Logger}.
   * @throws IllegalArgumentException If {@code logger} is null.
   * @throws IllegalArgumentException If {@code level} is null and {@code logger} is
   *           the root logger.
   */
  public static void setLevel(final Logger logger, final Level level) {
    if (logger == null)
      throw new IllegalArgumentException("logger == null");

    ((ch.qos.logback.classic.Logger)logger).setLevel(levelToLevel.get(level));
  }

  /**
   * Check if a message of the specified {@code level} would be logged by
   * {@code logger}.
   *
   * @param logger The {@link Logger} to check.
   * @param level The logging {@link Level}.
   * @return {@code true} if the specified logging level is currently being
   *         logged by {@code logger}.
   */
  public static boolean isLoggable(final Logger logger, final Level level) {
    return logger != null && level != null && (level == Level.INFO && logger.isInfoEnabled() || level == Level.DEBUG && logger.isDebugEnabled() || level == Level.TRACE && logger.isTraceEnabled() || level == Level.WARN && logger.isWarnEnabled() || level == Level.ERROR && logger.isErrorEnabled());
  }

  /**
   * Check if a message of the specified {@code level} and {@code marker} would
   * be logged by {@code logger}.
   *
   * @param logger The {@link Logger} to check.
   * @param level The logging {@link Level}.
   * @param marker The {@link Marker} data to take into consideration.
   * @return {@code true} if the specified logging level is currently being
   *         logged by {@code logger}.
   */
  public static boolean isLoggable(final Logger logger, final Level level, final Marker marker) {
    return logger != null && level != null && (level == Level.INFO && logger.isInfoEnabled(marker) || level == Level.DEBUG && logger.isDebugEnabled(marker) || level == Level.TRACE && logger.isTraceEnabled(marker) || level == Level.WARN && logger.isWarnEnabled(marker) || level == Level.ERROR && logger.isErrorEnabled(marker));
  }

  /**
   * Log a {@code msg} message with {@code logger} at {@code level}.
   *
   * @param logger The {@link Logger}.
   * @param level The logging {@link Level}.
   * @param msg The message string to log.
   * @throws IllegalArgumentException If {@code logger} is null.
   */
  public static void log(final Logger logger, final Level level, final String msg) {
    if (logger == null)
      throw new IllegalArgumentException("logger == null");

    if (level == Level.INFO)
      logger.info(msg);
    else if (level == Level.DEBUG)
      logger.debug(msg);
    else if (level == Level.TRACE)
      logger.trace(msg);
    else if (level == Level.WARN)
      logger.warn(msg);
    else if (level == Level.ERROR)
      logger.error(msg);
    else
      throw new UnsupportedOperationException("Unsupported level: " + level);
  }

  /**
   * Log a message with {@code logger} at {@code level} according to the
   * specified {@code format} and {@code arg}.
   * <p>
   * This form avoids superfluous object creation when the logger is disabled
   * for the {@code level}.
   *
   * @param logger The {@link Logger}.
   * @param level The logging {@link Level}.
   * @param format The format string.
   * @param arg The argument.
   * @throws IllegalArgumentException If {@code logger} is null.
   */
  public static void log(final Logger logger, final Level level, final String format, final Object arg) {
    if (logger == null)
      throw new IllegalArgumentException("logger == null");

    if (level == Level.INFO)
      logger.info(format, arg);
    else if (level == Level.DEBUG)
      logger.debug(format, arg);
    else if (level == Level.TRACE)
      logger.trace(format, arg);
    else if (level == Level.WARN)
      logger.warn(format, arg);
    else if (level == Level.ERROR)
      logger.error(format, arg);
    else
      throw new UnsupportedOperationException("Unsupported level: " + level);
  }

  /**
   * Log a message with {@code logger} at {@code level} according to the
   * specified {@code format} and arguments, {@code arg1} and {@code arg2}.
   * <p>
   * This form avoids superfluous object creation when the logger is disabled
   * for the {@code level}.
   *
   * @param logger The {@link Logger}.
   * @param level The logging {@link Level}.
   * @param format The format string.
   * @param arg1 The first argument.
   * @param arg2 The second argument.
   * @throws IllegalArgumentException If {@code logger} is null.
   */
  public static void log(final Logger logger, final Level level, final String format, final Object arg1, final Object arg2) {
    if (logger == null)
      throw new IllegalArgumentException("logger == null");

    if (level == Level.INFO)
      logger.info(format, arg1, arg2);
    else if (level == Level.DEBUG)
      logger.debug(format, arg1, arg2);
    else if (level == Level.TRACE)
      logger.trace(format, arg1, arg2);
    else if (level == Level.WARN)
      logger.warn(format, arg1, arg2);
    else if (level == Level.ERROR)
      logger.error(format, arg1, arg2);
    else
      throw new UnsupportedOperationException("Unsupported level: " + level);
  }

  /**
   * Log a message with {@code logger} at {@code level} according to the
   * specified {@code format} and {@code arguments}.
   * <p>
   * This form avoids superfluous string concatenation when the logger is
   * disabled for the {@code level}. However, this variant incurs the hidden
   * (and relatively small) cost of creating an {@code Object[]} before invoking
   * the method, even if {@code logger} is disabled for {@code level}. The
   * variants taking {@link #log(Logger,Level,String,Object)} and
   * {@link #log(Logger,Level,String,Object,Object)} arguments exist solely in
   * order to avoid this hidden cost.
   *
   * @param logger The {@link Logger}.
   * @param level The logging {@link Level}.
   * @param format The format string.
   * @param arguments A list of 3 or more arguments.
   * @throws IllegalArgumentException If {@code logger} is null.
   */
  public static void log(final Logger logger, final Level level, final String format, final Object ... arguments) {
    if (logger == null)
      throw new IllegalArgumentException("logger == null");

    if (level == Level.INFO)
      logger.info(format, arguments);
    else if (level == Level.DEBUG)
      logger.debug(format, arguments);
    else if (level == Level.TRACE)
      logger.trace(format, arguments);
    else if (level == Level.WARN)
      logger.warn(format, arguments);
    else if (level == Level.ERROR)
      logger.error(format, arguments);
    else
      throw new UnsupportedOperationException("Unsupported level: " + level);
  }

  /**
   * Log an exception {@code t} (throwable) with {@code logger} at {@code level}
   * with an accompanying {@code msg} message.
   *
   * @param logger The {@link Logger}.
   * @param level The logging {@link Level}.
   * @param msg The message accompanying the exception.
   * @param t The {@link Throwable} to log.
   * @throws IllegalArgumentException If {@code logger} is null.
   */
  public static void log(final Logger logger, final Level level, final String msg, final Throwable t) {
    if (logger == null)
      throw new IllegalArgumentException("logger == null");

    if (level == Level.INFO)
      logger.info(msg, t);
    else if (level == Level.DEBUG)
      logger.debug(msg, t);
    else if (level == Level.TRACE)
      logger.trace(msg, t);
    else if (level == Level.WARN)
      logger.warn(msg, t);
    else if (level == Level.ERROR)
      logger.error(msg, t);
    else
      throw new UnsupportedOperationException("Unsupported level: " + level);
  }

  /**
   * Log a {@code msg} message with {@code logger} at {@code level}, with the
   * specific {@code marker}.
   *
   * @param logger The {@link Logger}.
   * @param level The logging {@link Level}.
   * @param marker The marker specific to this log statement.
   * @param msg The message string to be logged.
   * @throws IllegalArgumentException If {@code logger} is null.
   */
  public static void log(final Logger logger, final Level level, final Marker marker, final String msg) {
    if (logger == null)
      throw new IllegalArgumentException("logger == null");

    if (level == Level.INFO)
      logger.info(marker, msg);
    else if (level == Level.DEBUG)
      logger.debug(marker, msg);
    else if (level == Level.TRACE)
      logger.trace(marker, msg);
    else if (level == Level.WARN)
      logger.warn(marker, msg);
    else if (level == Level.ERROR)
      logger.error(marker, msg);
    else
      throw new UnsupportedOperationException("Unsupported level: " + level);
  }

  /**
   * Log a message with {@code logger} at {@code level} according to the
   * specified {@code format} and {@code arg}, with the specific {@code marker}.
   * <p>
   * This form avoids superfluous object creation when the logger is disabled
   * for the {@code level}.
   * <p>
   * This method is similar to {@link #log(Logger,Level,String,Object)} method
   * except that the marker data is also taken into consideration.
   *
   * @param logger The {@link Logger}.
   * @param level The logging {@link Level}.
   * @param marker The marker specific to this log statement.
   * @param format The format string.
   * @param arg The argument.
   * @throws IllegalArgumentException If {@code logger} is null.
   */
  public static void log(final Logger logger, final Level level, final Marker marker, final String format, final Object arg) {
    if (logger == null)
      throw new IllegalArgumentException("logger == null");

    if (level == Level.INFO)
      logger.info(marker, format, arg);
    else if (level == Level.DEBUG)
      logger.debug(marker, format, arg);
    else if (level == Level.TRACE)
      logger.trace(marker, format, arg);
    else if (level == Level.WARN)
      logger.warn(marker, format, arg);
    else if (level == Level.ERROR)
      logger.error(marker, format, arg);
    else
      throw new UnsupportedOperationException("Unsupported level: " + level);
  }

  /**
   * Log a message with {@code logger} at {@code level} according to the
   * specified {@code format} and arguments, {@code arg1} and {@code arg2}, with
   * the specific {@code marker}.
   * <p>
   * This form avoids superfluous object creation when the logger is disabled
   * for the {@code level}.
   * <p>
   * This method is similar to {@link #log(Logger,Level,String,Object,Object)}
   * method except that the marker data is also taken into consideration.
   *
   * @param logger The {@link Logger}.
   * @param level The logging {@link Level}.
   * @param marker The marker specific to this log statement.
   * @param format The format string.
   * @param arg1 The first argument.
   * @param arg2 The second argument.
   * @throws IllegalArgumentException If {@code logger} is null.
   */
  public static void log(final Logger logger, final Level level, final Marker marker, final String format, final Object arg1, final Object arg2) {
    if (logger == null)
      throw new IllegalArgumentException("logger == null");

    if (level == Level.INFO)
      logger.info(marker, format, arg1, arg2);
    else if (level == Level.DEBUG)
      logger.debug(marker, format, arg1, arg2);
    else if (level == Level.TRACE)
      logger.trace(marker, format, arg1, arg2);
    else if (level == Level.WARN)
      logger.warn(marker, format, arg1, arg2);
    else if (level == Level.ERROR)
      logger.error(marker, format, arg1, arg2);
    else
      throw new UnsupportedOperationException("Unsupported level: " + level);
  }

  /**
   * Log a message with {@code logger} at {@code level} according to the
   * specified {@code format} and {@code arguments}, with the specific
   * {@code marker}.
   * <p>
   * This form avoids superfluous string concatenation when the logger is
   * disabled for the {@code level}. However, this variant incurs the hidden
   * (and relatively small) cost of creating an {@code Object[]} before invoking
   * the method, even if {@code logger} is disabled for {@code level}. The
   * variants taking {@link #log(Logger,Level,String,Object)} and
   * {@link #log(Logger,Level,String,Object,Object)} arguments exist solely in
   * order to avoid this hidden cost.
   * <p>
   * This method is similar to {@link #log(Logger,Level,String,Object...)}
   * method except that the marker data is also taken into consideration.
   *
   * @param logger The {@link Logger}.
   * @param level The logging {@link Level}.
   * @param marker The marker specific to this log statement.
   * @param format The format string.
   * @param arguments A list of 3 or more arguments.
   * @throws IllegalArgumentException If {@code logger} is null.
   */
  public static void log(final Logger logger, final Level level, final Marker marker, final String format, final Object ... arguments) {
    if (logger == null)
      throw new IllegalArgumentException("logger == null");

    if (level == Level.INFO)
      logger.info(marker, format, arguments);
    else if (level == Level.DEBUG)
      logger.debug(marker, format, arguments);
    else if (level == Level.TRACE)
      logger.trace(marker, format, arguments);
    else if (level == Level.WARN)
      logger.warn(marker, format, arguments);
    else if (level == Level.ERROR)
      logger.error(marker, format, arguments);
    else
      throw new UnsupportedOperationException("Unsupported level: " + level);
  }

  /**
   * Log an exception {@code t} (throwable) with {@code logger} at {@code level}
   * with an accompanying {@code msg} message, with the specific {@code marker}.
   * <p>
   * This method is similar to {@link #log(Logger,Level,String,Throwable)}
   * method except that the marker data is also taken into consideration.
   *
   * @param logger The {@link Logger}.
   * @param level The logging {@link Level}.
   * @param marker The marker specific to this log statement.
   * @param msg The message accompanying the exception.
   * @param t The {@link Throwable} to log.
   * @throws IllegalArgumentException If {@code logger} is null.
   */
  public static void log(final Logger logger, final Level level, final Marker marker, final String msg, final Throwable t) {
    if (logger == null)
      throw new IllegalArgumentException("logger == null");

    if (level == Level.INFO)
      logger.info(marker, msg, t);
    else if (level == Level.DEBUG)
      logger.debug(marker, msg, t);
    else if (level == Level.TRACE)
      logger.trace(marker, msg, t);
    else if (level == Level.WARN)
      logger.warn(marker, msg, t);
    else if (level == Level.ERROR)
      logger.error(marker, msg, t);
    else
      throw new UnsupportedOperationException("Unsupported level: " + level);
  }

  private LoggerUtil() {
  }
}