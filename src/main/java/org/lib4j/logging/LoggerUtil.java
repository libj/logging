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

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;

public final class LoggerUtil {
  /**
   * Check if a message of the given level would actually be logged
   * by this logger.
   *
   * @param   level   a message logging level
   * @return  true if the given message level is currently being logged.
   */
  public static boolean isLoggable(final Logger logger, final Level level) {
    return logger != null && level != null && (
      level == Level.INFO && logger.isInfoEnabled() ||
      level == Level.DEBUG && logger.isDebugEnabled() ||
      level == Level.TRACE && logger.isTraceEnabled() ||
      level == Level.WARN && logger.isWarnEnabled() ||
      level == Level.ERROR && logger.isErrorEnabled());
  }

  /**
   * Check if a message of the given level would actually be logged
   * by this logger.
   *
   * @param   level   a message logging level
   * @param   marker The marker specific to this log statement
   * @return  true if the given message level is currently being logged.
   */
  public static boolean isLoggable(final Logger logger, final Level level, final Marker marker) {
    return logger != null && level != null && (
      level == Level.INFO && logger.isInfoEnabled(marker) ||
      level == Level.DEBUG && logger.isDebugEnabled(marker) ||
      level == Level.TRACE && logger.isTraceEnabled(marker) ||
      level == Level.WARN && logger.isWarnEnabled(marker) ||
      level == Level.ERROR && logger.isErrorEnabled(marker));
  }

  /**
   * Log a message using <code>logger</code> at <code>level</code>
   *
   * @param logger the logger
   * @param level the logging level
   * @param msg the message string to be logged
   */
  public static void log(final Logger logger, final Level level, final String msg) {
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
      throw new UnsupportedOperationException("Unexpected level: " + level);
  }

  /**
   * Log a message using the <code>logger</code> at <code>level</code> according to the specified format
   * and argument.
   * <p/>
   * <p>This form avoids superfluous object creation when the logger
   * is disabled for the <code>level</code> level. </p>
   *
   * @param logger the logger
   * @param level the logging level
   * @param format the format string
   * @param arg    the argument
   */
  public static void log(final Logger logger, final Level level, final String format, final Object arg) {
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
      throw new UnsupportedOperationException("Unexpected level: " + level);
  }

  /**
   * Log a message using the <code>logger</code> at <code>level</code> according to the specified format
   * and arguments.
   * <p/>
   * <p>This form avoids superfluous object creation when the logger
   * is disabled for the <code>level</code> level. </p>
   *
   * @param logger the logger
   * @param level the logging level
   * @param format the format string
   * @param arg1   the first argument
   * @param arg2   the second argument
   */
  public static void log(final Logger logger, final Level level, final String format, final Object arg1, final Object arg2) {
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
      throw new UnsupportedOperationException("Unexpected level: " + level);
  }

  /**
   * Log a message using the <code>logger</code> at <code>level</code> according to the specified format
   * and arguments.
   * <p/>
   * <p>This form avoids superfluous string concatenation when the logger
   * is disabled for the <code>level</code> level. However, final this variant incurs the hidden
   * (final and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
   * even if this logger is disabled for <code>level</code>. The variants taking
   * {@link #log(final Logger logger, final Level level, String, Object) one} and {@link #log(final Logger logger, final Level level, String, Object, Object) two}
   * arguments exist solely in order to avoid this hidden cost.</p>
   *
   * @param logger the logger
   * @param level the logging level
   * @param format    the format string
   * @param arguments a list of 3 or more arguments
   */
  public static void log(final Logger logger, final Level level, final String format, final Object... arguments) {
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
      throw new UnsupportedOperationException("Unexpected level: " + level);
  }

  /**
   * Log an exception (throwable) using the <code>logger</code> at <code>level</code> with an
   * accompanying message.
   *
   * @param msg the message accompanying the exception
   * @param t   the exception (throwable) to log
   */
  public static void log(final Logger logger, final Level level, final String msg, final Throwable t) {
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
      throw new UnsupportedOperationException("Unexpected level: " + level);
  }

  /**
   * Log a message using <code>logger</code> at <code>level</code> with the specific Marker.
   *
   * @param logger the logger
   * @param level the logging level
   * @param marker The marker specific to this log statement
   * @param msg    the message string to be logged
   */
  public static void log(final Logger logger, final Level level, final Marker marker, final String msg) {
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
      throw new UnsupportedOperationException("Unexpected level: " + level);
  }

  /**
   * This method is similar to {@link #log(final Logger logger, final Level level, String, Object)} method except that the
   * marker data is also taken into consideration.
   *
   * @param logger the logger
   * @param level the logging level
   * @param marker the marker data specific to this log statement
   * @param format the format string
   * @param arg    the argument
   */
  public static void log(final Logger logger, final Level level, final Marker marker, final String format, final Object arg) {
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
      throw new UnsupportedOperationException("Unexpected level: " + level);
  }

  /**
   * This method is similar to {@link #log(final Logger logger, final Level level, String, Object, Object)}
   * method except that the marker data is also taken into
   * consideration.
   *
   * @param logger the logger
   * @param level the logging level
   * @param marker the marker data specific to this log statement
   * @param format the format string
   * @param arg1   the first argument
   * @param arg2   the second argument
   */
  public static void log(final Logger logger, final Level level, final Marker marker, final String format, final Object arg1, final Object arg2) {
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
      throw new UnsupportedOperationException("Unexpected level: " + level);
  }

  /**
   * This method is similar to {@link #log(final Logger logger, final Level level, String, Object...)}
   * method except that the marker data is also taken into
   * consideration.
   *
   * @param logger the logger
   * @param level the logging level
   * @param marker    the marker data specific to this log statement
   * @param format    the format string
   * @param arguments a list of 3 or more arguments
   */
  public static void log(final Logger logger, final Level level, final Marker marker, final String format, final Object... arguments) {
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
      throw new UnsupportedOperationException("Unexpected level: " + level);
  }

  /**
   * This method is similar to {@link #log(final Logger logger, final Level level, String, Throwable)} method
   * except that the marker data is also taken into consideration.
   *
   * @param logger the logger
   * @param level the logging level
   * @param marker the marker data for this log statement
   * @param msg    the message accompanying the exception
   * @param t      the exception (throwable) to log
   */
  public static void log(final Logger logger, final Level level, final Marker marker, final String msg, final Throwable t) {
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
      throw new UnsupportedOperationException("Unexpected level: " + level);
  }

  private LoggerUtil() {
  }
}