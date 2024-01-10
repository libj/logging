/* Copyright (c) 2020 LibJ
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

import java.io.PrintStream;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;

/**
 * A {@link Logger} that proxies its output to a {@link PrintStream}, such as {@link System#out} and {@link System#err}.
 */
public class PrintStreamLogger implements Logger {
  private static final String TRACE = "[TRACE] ";
  private static final String DEBUG = "[DEBUG] ";
  private static final String INFO = "[INFO] ";
  private static final String WARN = "[WARN] ";
  private static final String ERROR = "[ERROR] ";

  private final PrintStream trace;
  private final PrintStream debug;
  private final PrintStream info;
  private final PrintStream warn;
  private final PrintStream error;
  private final Level level;

  /**
   * Constructs a new {@link PrintStreamLogger} with the provided logging {@link Level} to be used with this logger, and the given
   * {@link PrintStream}s to apply to each individual logging {@link Level}.
   *
   * @param level The logging {@link Level}.
   * @param trace The {@link PrintStream} to be used for {@link Level#TRACE}.
   * @param debug The {@link PrintStream} to be used for {@link Level#DEBUG}.
   * @param info The {@link PrintStream} to be used for {@link Level#INFO}.
   * @param warn The {@link PrintStream} to be used for {@link Level#WARN}.
   * @param error The {@link PrintStream} to be used for {@link Level#ERROR}.
   * @throws NullPointerException If any argument is null.
   */
  public PrintStreamLogger(final Level level, final PrintStream trace, final PrintStream debug, final PrintStream info, final PrintStream warn, final PrintStream error) {
    this.level = Objects.requireNonNull(level, "level is null");
    this.trace = Objects.requireNonNull(trace, "trace is null");
    this.debug = Objects.requireNonNull(debug, "debug is null");
    this.info = Objects.requireNonNull(info, "info is null");
    this.warn = Objects.requireNonNull(warn, "warn is null");
    this.error = Objects.requireNonNull(error, "error is null");
  }

  /**
   * Constructs a new {@link PrintStreamLogger} with the provided logging {@link Level} to be used with this logger, and the given
   * {@link PrintStream} to apply to all logging {@link Level}s.
   *
   * @param level The logging {@link Level}.
   * @param ps The {@link PrintStream}.
   * @throws NullPointerException If any argument is null.
   */
  public PrintStreamLogger(final Level level, final PrintStream ps) {
    this.level = Objects.requireNonNull(level, "level is null");
    this.trace = this.debug = this.info = this.warn = this.error = Objects.requireNonNull(ps, "ps is null");
  }

  /**
   * Constructs a new {@link PrintStreamLogger} with the provided logging {@link Level} to be used with this logger, and
   * {@link System#out System.out} as the {@link PrintStream} for all logging {@link Level}s.
   *
   * @param level The logging {@link Level}.
   * @throws NullPointerException If {@code level} is null.
   */
  public PrintStreamLogger(final Level level) {
    this.level = Objects.requireNonNull(level, "level is null");
    this.trace = this.debug = this.info = this.warn = this.error = System.out;
  }

  /**
   * Constructs a new {@link PrintStreamLogger} with the {@link Level#INFO} to be used with this logger, and {@link System#out
   * System.out} as the {@link PrintStream} for all logging {@link Level}s.
   */
  public PrintStreamLogger() {
    this.level = Level.INFO;
    this.trace = this.debug = this.info = this.warn = this.error = System.out;
  }

  @Override
  public String getName() {
    return ROOT_LOGGER_NAME;
  }

  @Override
  public boolean isTraceEnabled() {
    return level.toInt() <= Level.TRACE.toInt();
  }

  @Override
  public void trace(final String msg) {
    if (isTraceEnabled())
      trace.println(TRACE + msg);
  }

  @Override
  public void trace(final String format, final Object arg) {
    if (isTraceEnabled())
      trace.println(TRACE + String.format(format, arg));
  }

  @Override
  public void trace(final String format, final Object arg1, final Object arg2) {
    if (isTraceEnabled())
      trace.println(TRACE + String.format(format, arg1, arg2));
  }

  @Override
  public void trace(final String format, final Object ... arguments) {
    if (isTraceEnabled())
      trace.println(TRACE + String.format(format, arguments));
  }

  @Override
  public void trace(final String msg, final Throwable t) {
    if (isTraceEnabled()) {
      trace.println(TRACE + msg);
      t.printStackTrace(trace);
    }
  }

  @Override
  public boolean isTraceEnabled(final Marker marker) {
    return isTraceEnabled();
  }

  @Override
  public void trace(final Marker marker, final String msg) {
    trace(msg);
  }

  @Override
  public void trace(final Marker marker, final String format, final Object arg) {
    trace(format, arg);
  }

  @Override
  public void trace(final Marker marker, final String format, final Object arg1, final Object arg2) {
    trace(format, arg1, arg2);
  }

  @Override
  public void trace(final Marker marker, final String format, final Object ... argArray) {
    trace(format, argArray);
  }

  @Override
  public void trace(final Marker marker, final String msg, final Throwable t) {
    trace(msg, t);
  }

  @Override
  public boolean isDebugEnabled() {
    return level.toInt() <= Level.DEBUG.toInt();
  }

  @Override
  public void debug(final String msg) {
    if (isDebugEnabled())
      debug.println(DEBUG + msg);
  }

  @Override
  public void debug(final String format, final Object arg) {
    if (isDebugEnabled())
      debug.println(DEBUG + String.format(format, arg));
  }

  @Override
  public void debug(final String format, final Object arg1, final Object arg2) {
    if (isDebugEnabled())
      debug.println(DEBUG + String.format(format, arg1, arg2));
  }

  @Override
  public void debug(final String format, final Object ... arguments) {
    if (isDebugEnabled())
      debug.println(DEBUG + String.format(format, arguments));
  }

  @Override
  public void debug(final String msg, final Throwable t) {
    if (isDebugEnabled()) {
      debug.println(DEBUG + msg);
      t.printStackTrace(debug);
    }
  }

  @Override
  public boolean isDebugEnabled(final Marker marker) {
    return isDebugEnabled();
  }

  @Override
  public void debug(final Marker marker, final String msg) {
    debug(msg);
  }

  @Override
  public void debug(final Marker marker, final String format, final Object arg) {
    debug(format, arg);
  }

  @Override
  public void debug(final Marker marker, final String format, final Object arg1, final Object arg2) {
    debug(format, arg1, arg2);
  }

  @Override
  public void debug(final Marker marker, final String format, final Object ... argArray) {
    debug(format, argArray);
  }

  @Override
  public void debug(final Marker marker, final String msg, final Throwable t) {
    debug(msg, t);
  }

  @Override
  public boolean isInfoEnabled() {
    return level.toInt() <= Level.INFO.toInt();
  }

  @Override
  public void info(final String msg) {
    if (isInfoEnabled())
      info.println(INFO + msg);
  }

  @Override
  public void info(final String format, final Object arg) {
    if (isInfoEnabled())
      info.println(INFO + String.format(format, arg));
  }

  @Override
  public void info(final String format, final Object arg1, final Object arg2) {
    if (isInfoEnabled())
      info.println(INFO + String.format(format, arg1, arg2));
  }

  @Override
  public void info(final String format, final Object ... arguments) {
    if (isInfoEnabled())
      info.println(INFO + String.format(format, arguments));
  }

  @Override
  public void info(final String msg, final Throwable t) {
    if (isInfoEnabled()) {
      info.println(INFO + msg);
      t.printStackTrace(info);
    }
  }

  @Override
  public boolean isInfoEnabled(final Marker marker) {
    return isInfoEnabled();
  }

  @Override
  public void info(final Marker marker, final String msg) {
    info(msg);
  }

  @Override
  public void info(final Marker marker, final String format, final Object arg) {
    info(format, arg);
  }

  @Override
  public void info(final Marker marker, final String format, final Object arg1, final Object arg2) {
    info(format, arg1, arg2);
  }

  @Override
  public void info(final Marker marker, final String format, final Object ... argArray) {
    info(format, argArray);
  }

  @Override
  public void info(final Marker marker, final String msg, final Throwable t) {
    info(msg, t);
  }

  @Override
  public boolean isWarnEnabled() {
    return level.toInt() <= Level.WARN.toInt();
  }

  @Override
  public void warn(final String msg) {
    if (isWarnEnabled())
      warn.println(WARN + msg);
  }

  @Override
  public void warn(final String format, final Object arg) {
    if (isWarnEnabled())
      warn.println(WARN + String.format(format, arg));
  }

  @Override
  public void warn(final String format, final Object arg1, final Object arg2) {
    if (isWarnEnabled())
      warn.println(WARN + String.format(format, arg1, arg2));
  }

  @Override
  public void warn(final String format, final Object ... arguments) {
    if (isWarnEnabled())
      warn.println(WARN + String.format(format, arguments));
  }

  @Override
  public void warn(final String msg, final Throwable t) {
    if (isWarnEnabled()) {
      warn.println(WARN + msg);
      t.printStackTrace(warn);
    }
  }

  @Override
  public boolean isWarnEnabled(final Marker marker) {
    return isWarnEnabled();
  }

  @Override
  public void warn(final Marker marker, final String msg) {
    warn(msg);
  }

  @Override
  public void warn(final Marker marker, final String format, final Object arg) {
    warn(format, arg);
  }

  @Override
  public void warn(final Marker marker, final String format, final Object arg1, final Object arg2) {
    warn(format, arg1, arg2);
  }

  @Override
  public void warn(final Marker marker, final String format, final Object ... argArray) {
    warn(format, argArray);
  }

  @Override
  public void warn(final Marker marker, final String msg, final Throwable t) {
    warn(msg, t);
  }

  @Override
  public boolean isErrorEnabled() {
    return level.toInt() <= Level.ERROR.toInt();
  }

  @Override
  public void error(final String msg) {
    if (isErrorEnabled())
      error.println(ERROR + msg);
  }

  @Override
  public void error(final String format, final Object arg) {
    if (isErrorEnabled())
      error.println(ERROR + String.format(format, arg));
  }

  @Override
  public void error(final String format, final Object arg1, final Object arg2) {
    if (isErrorEnabled())
      error.println(ERROR + String.format(format, arg1, arg2));
  }

  @Override
  public void error(final String format, final Object ... arguments) {
    if (isErrorEnabled())
      error.println(ERROR + String.format(format, arguments));
  }

  @Override
  public void error(final String msg, final Throwable t) {
    if (isErrorEnabled()) {
      error.println(ERROR + msg);
      t.printStackTrace(error);
    }
  }

  @Override
  public boolean isErrorEnabled(final Marker marker) {
    return isErrorEnabled();
  }

  @Override
  public void error(final Marker marker, final String msg) {
    error(msg);
  }

  @Override
  public void error(final Marker marker, final String format, final Object arg) {
    error(format, arg);
  }

  @Override
  public void error(final Marker marker, final String format, final Object arg1, final Object arg2) {
    error(format, arg1, arg2);
  }

  @Override
  public void error(final Marker marker, final String format, final Object ... argArray) {
    error(format, argArray);
  }

  @Override
  public void error(final Marker marker, final String msg, final Throwable t) {
    error(msg, t);
  }
}