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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.event.Level;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * Utility functions for operations pertaining to {@link Logger}.
 */
public final class LoggerUtil {
  /**
   * An array of {@link ch.qos.logback.classic.Level} values corresponding to the ordinal of the {@link org.slf4j.event.Level}
   * equivalents.
   */
  static final ch.qos.logback.classic.Level[] logbackLevel = {ch.qos.logback.classic.Level.ERROR, ch.qos.logback.classic.Level.WARN, ch.qos.logback.classic.Level.INFO, ch.qos.logback.classic.Level.DEBUG, ch.qos.logback.classic.Level.TRACE};

  /**
   * Programmatically sets the {@link Level} of the specified {@link Logger}.
   *
   * @implSpec This method is only applicable to the <a href="https://logback.qos.ch/">LogBack</a> implementation of {@link Logger}
   *           instances.
   * @param logger The {@link Logger}.
   * @param level The {@link Level}.
   * @throws ClassCastException If {@code logger} is not an instance of {@link ch.qos.logback.classic.Logger}.
   * @throws NullPointerException If {@code logger} is null, or if {@code level} is null and {@code logger} is the root logger.
   */
  public static void setLevel(final Logger logger, final Level level) {
    ((ch.qos.logback.classic.Logger)logger).setLevel(logbackLevel[level.ordinal()]);
  }

  /**
   * Find and load a {@code "logback.xml"} file that IS NOT located in the root location matching that of the provided {@code cls}.
   *
   * @param cls The {@link Class} of which to find and load a {@code "logback.xml"} file NOT belonging to the same root location.
   * @return {@code true} if a {@code "logback.xml"} file was found, otherwise {@code false}.
   * @throws NullPointerException If {@code cls} is null.
   */
  public static boolean loadConfigExcludeLocation(final Class<?> cls) {
    return loadConfigViaLocation(cls, true);
  }

  /**
   * Find and load a {@code "logback.xml"} file that IS located in the root location matching that of the provided {@code cls}.
   *
   * @param cls The {@link Class} of which to find and load a {@code "logback.xml"} file belonging to the same root location.
   * @return {@code true} if a {@code "logback.xml"} file was found, otherwise {@code false}.
   * @throws NullPointerException If {@code cls} is null.
   */
  public static boolean loadConfigIncludeLocation(final Class<?> cls) {
    return loadConfigViaLocation(cls, false);
  }

  private static boolean loadConfigViaLocation(final Class<?> cls, final boolean exclude) {
    final String classResource = cls.getName().replace('.', '/').concat(".class");
    final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    final URL classResourceUrl = classLoader.getResource(classResource);
    if (classResourceUrl != null) {
      try {
        final String classResourcePath = classResourceUrl.toString();
        final Enumeration<URL> e = classLoader.getResources("logback.xml");
        while (e.hasMoreElements()) {
          final URL url = e.nextElement();
          if (url.toString().regionMatches(0, classResourcePath, 0, classResourcePath.length() - classResource.length()) != exclude) {
            final LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();
            loggerContext.reset();
            final JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(loggerContext);
            try (final InputStream in = url.openStream()) {
              configurator.doConfigure(in);
              return true;
            }
          }
        }
      }
      catch (final IOException | JoranException e) {
        throw new RuntimeException(e);
      }
    }

    return false;
  }

  /**
   * Check if a message of the specified {@code level} would be logged by {@code logger}.
   *
   * @param logger The {@link Logger} to check.
   * @param level The logging {@link Level}.
   * @return {@code true} if the specified logging level is currently being logged by {@code logger}.
   */
  public static boolean isLoggable(final Logger logger, final Level level) {
    return logger != null && level != null && (level == Level.INFO && logger.isInfoEnabled() || level == Level.DEBUG && logger.isDebugEnabled() || level == Level.TRACE && logger.isTraceEnabled() || level == Level.WARN && logger.isWarnEnabled() || level == Level.ERROR && logger.isErrorEnabled());
  }

  /**
   * Check if a message of the specified {@code level} and {@code marker} would be logged by {@code logger}.
   *
   * @param logger The {@link Logger} to check.
   * @param level The logging {@link Level}.
   * @param marker The {@link Marker} data to take into consideration.
   * @return {@code true} if the specified logging level is currently being logged by {@code logger}.
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
   * @throws NullPointerException If {@code logger} is null.
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
      throw new UnsupportedOperationException("Unsupported level: " + level);
  }

  /**
   * Log a message with {@code logger} at {@code level} according to the specified {@code format} and {@code arg}.
   * <p>
   * This form avoids superfluous object creation when the logger is disabled for the {@code level}.
   *
   * @param logger The {@link Logger}.
   * @param level The logging {@link Level}.
   * @param format The format string.
   * @param arg The argument.
   * @throws NullPointerException If {@code logger} is null.
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
      throw new UnsupportedOperationException("Unsupported level: " + level);
  }

  /**
   * Log a message with {@code logger} at {@code level} according to the specified {@code format} and arguments, {@code arg1} and
   * {@code arg2}.
   * <p>
   * This form avoids superfluous object creation when the logger is disabled for the {@code level}.
   *
   * @param logger The {@link Logger}.
   * @param level The logging {@link Level}.
   * @param format The format string.
   * @param arg1 The first argument.
   * @param arg2 The second argument.
   * @throws NullPointerException If {@code logger} is null.
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
      throw new UnsupportedOperationException("Unsupported level: " + level);
  }

  /**
   * Log a message with {@code logger} at {@code level} according to the specified {@code format} and {@code arguments}.
   * <p>
   * This form avoids superfluous string concatenation when the logger is disabled for the {@code level}. However, this variant incurs
   * the hidden (and relatively small) cost of creating an {@code Object[]} before invoking the method, even if {@code logger} is
   * disabled for {@code level}. The variants taking {@link #log(Logger,Level,String,Object)} and
   * {@link #log(Logger,Level,String,Object,Object)} arguments exist solely in order to avoid this hidden cost.
   *
   * @param logger The {@link Logger}.
   * @param level The logging {@link Level}.
   * @param format The format string.
   * @param arguments A list of 3 or more arguments.
   * @throws NullPointerException If {@code logger} is null.
   */
  public static void log(final Logger logger, final Level level, final String format, final Object ... arguments) {
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
   * Log an exception {@code t} (throwable) with {@code logger} at {@code level} with an accompanying {@code msg} message.
   *
   * @param logger The {@link Logger}.
   * @param level The logging {@link Level}.
   * @param msg The message accompanying the exception.
   * @param t The {@link Throwable} to log.
   * @throws NullPointerException If {@code logger} is null.
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
      throw new UnsupportedOperationException("Unsupported level: " + level);
  }

  /**
   * Log a {@code msg} message with {@code logger} at {@code level}, with the specific {@code marker}.
   *
   * @param logger The {@link Logger}.
   * @param level The logging {@link Level}.
   * @param marker The marker specific to this log statement.
   * @param msg The message string to be logged.
   * @throws NullPointerException If {@code logger} is null.
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
      throw new UnsupportedOperationException("Unsupported level: " + level);
  }

  /**
   * Log a message with {@code logger} at {@code level} according to the specified {@code format} and {@code arg}, with the specific
   * {@code marker}.
   * <p>
   * This form avoids superfluous object creation when the logger is disabled for the {@code level}.
   * <p>
   * This method is similar to {@link #log(Logger,Level,String,Object)} method except that the marker data is also taken into
   * consideration.
   *
   * @param logger The {@link Logger}.
   * @param level The logging {@link Level}.
   * @param marker The marker specific to this log statement.
   * @param format The format string.
   * @param arg The argument.
   * @throws NullPointerException If {@code logger} is null.
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
      throw new UnsupportedOperationException("Unsupported level: " + level);
  }

  /**
   * Log a message with {@code logger} at {@code level} according to the specified {@code format} and arguments, {@code arg1} and
   * {@code arg2}, with the specific {@code marker}.
   * <p>
   * This form avoids superfluous object creation when the logger is disabled for the {@code level}.
   * <p>
   * This method is similar to {@link #log(Logger,Level,String,Object,Object)} method except that the marker data is also taken into
   * consideration.
   *
   * @param logger The {@link Logger}.
   * @param level The logging {@link Level}.
   * @param marker The marker specific to this log statement.
   * @param format The format string.
   * @param arg1 The first argument.
   * @param arg2 The second argument.
   * @throws NullPointerException If {@code logger} is null.
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
      throw new UnsupportedOperationException("Unsupported level: " + level);
  }

  /**
   * Log a message with {@code logger} at {@code level} according to the specified {@code format} and {@code arguments}, with the
   * specific {@code marker}.
   * <p>
   * This form avoids superfluous string concatenation when the logger is disabled for the {@code level}. However, this variant incurs
   * the hidden (and relatively small) cost of creating an {@code Object[]} before invoking the method, even if {@code logger} is
   * disabled for {@code level}. The variants taking {@link #log(Logger,Level,String,Object)} and
   * {@link #log(Logger,Level,String,Object,Object)} arguments exist solely in order to avoid this hidden cost.
   * <p>
   * This method is similar to {@link #log(Logger,Level,String,Object...)} method except that the marker data is also taken into
   * consideration.
   *
   * @param logger The {@link Logger}.
   * @param level The logging {@link Level}.
   * @param marker The marker specific to this log statement.
   * @param format The format string.
   * @param arguments A list of 3 or more arguments.
   * @throws NullPointerException If {@code logger} is null.
   */
  public static void log(final Logger logger, final Level level, final Marker marker, final String format, final Object ... arguments) {
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
   * Log an exception {@code t} (throwable) with {@code logger} at {@code level} with an accompanying {@code msg} message, with the
   * specific {@code marker}.
   * <p>
   * This method is similar to {@link #log(Logger,Level,String,Throwable)} method except that the marker data is also taken into
   * consideration.
   *
   * @param logger The {@link Logger}.
   * @param level The logging {@link Level}.
   * @param marker The marker specific to this log statement.
   * @param msg The message accompanying the exception.
   * @param t The {@link Throwable} to log.
   * @throws NullPointerException If {@code logger} is null.
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
      throw new UnsupportedOperationException("Unsupported level: " + level);
  }

  private static int convert(final StringBuilder builder, final CharSequence str, int a, final Object[] args) {
    for (int i = 0, i$ = str.length(); i < i$;) { // [N]
      final char ch = str.charAt(i++);
      if (ch == '%') {
        if (i < str.length() && str.charAt(i) == '?') {
          ++i;
          final Object arg = args[a++];
          if (arg != null) {
            final Class<?> cls = arg.getClass();
            builder.append(cls.isAnonymousClass() ? cls.getName() : cls.getSimpleName()).append("@%h");
          }
          else {
            builder.append("%s");
          }

          continue;
        }

        ++a;
      }

      builder.append(ch);
    }

    return a;
  }

  /**
   * Log a debug message representing a method signature to the specified {@link Logger logger} with the provided {@link Level level}
   * of the form
   *
   * <pre>
   * $method(String.format($format, $args...))
   * </pre>
   *
   * @param logger The {@link Logger}.
   * @param level The logging {@link Level}.
   * @param method The method name.
   * @param format The {@link String#format(String,Object...) format}.
   * @param args The arguments to be appended via {@link String#valueOf(Object)}.
   * @throws NullPointerException If {@code method} or {@code args} is null.
   */
  public static void logm(final Logger logger, final Level level, String method, final String format, final Object ... args) {
    if (!isLoggable(logger, level))
      return;

    for (int i = 0, i$ = args.length; i < i$; ++i) { // [A]
      final Object arg = args[i];
      if (arg == null)
        continue;

      if (arg.getClass().isArray())
        args[i] = Arrays.toString((Object[])arg);
      else if (arg instanceof Class)
        args[i] = ((Class<?>)arg).getSimpleName();
    }

    final StringBuilder builder = new StringBuilder();
    final int a = convert(builder, method, 0, args);
    builder.append('(');
    if (format != null) {
      convert(builder, format, a, args);
      builder.append(')');
    }
    else if (args.length > 0 && a < args.length) {
      for (int i = a, i$ = args.length; i < i$; ++i) // [A]
        builder.append("%s,");

      builder.setCharAt(builder.length() - 1, ')');
    }
    else {
      builder.append(')');
    }

    final String msg = args.length > 0 ? String.format(builder.toString(), args) : builder.toString();
    log(logger, level, (Marker)null, msg, (Throwable)null);
  }

  /**
   * Log a debug message representing a method signature to the specified {@link Logger logger} with the provided {@link Level level}
   * of the form
   *
   * <pre>
   * $method($args...)
   * </pre>
   *
   * @param logger The {@link Logger}.
   * @param level The logging {@link Level}.
   * @param method The method name.
   * @param args The arguments to be appended via {@link String#valueOf(Object)}.
   * @throws NullPointerException If {@code method} or {@code args} is null.
   */
  public static void logm(final Logger logger, final Level level, final String method, final Object ... args) {
    logm(logger, level, method, null, args);
  }

  private LoggerUtil() {
  }
}