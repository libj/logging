/* Copyright (c) 2018 OpenJAX
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

package org.openjax.classic.logging;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * A {@code DelegateLogger} contains some other {@link Logger}, possibly
 * transforming the method parameters along the way or providing additional
 * functionality. The class {@code DelegateLogger} itself simply overrides all
 * methods of {@link Logger} with versions that delegate all calls to the source
 * {@link Logger}. Subclasses of {@code DelegateLogger} may further override
 * some of these methods and may also provide additional methods and fields.
 */
public abstract class DelegateLogger implements Logger {
  /**
   * The target {@link Logger}.
   */
  protected volatile Logger target;

  /**
   * Creates a new {@code DelegateLogger} with the specified {@code target}.
   *
   * @param target The target {@link Logger} object.
   * @throws NullPointerException If {@code target} is null.
   */
  public DelegateLogger(final Logger target) {
    this.target = Objects.requireNonNull(target);
  }

  /**
   * Creates a new {@code DelegateLogger} with a null target.
   */
  protected DelegateLogger() {
  }

  @Override
  public String getName() {
    return target.getName();
  }

  @Override
  public boolean isTraceEnabled() {
    return target.isTraceEnabled();
  }

  @Override
  public void trace(final String msg) {
    target.trace(msg);
  }

  @Override
  public void trace(final String format, final Object arg) {
    target.trace(format, arg);
  }

  @Override
  public void trace(final String format, final Object arg1, final Object arg2) {
    target.trace(format, arg1, arg2);
  }

  @Override
  public void trace(final String format, final Object ... arguments) {
    target.trace(format, arguments);
  }

  @Override
  public void trace(final String msg, final Throwable t) {
    target.trace(msg, t);
  }

  @Override
  public boolean isTraceEnabled(final Marker marker) {
    return target.isTraceEnabled(marker);
  }

  @Override
  public void trace(final Marker marker, final String msg) {
    target.trace(marker, msg);
  }

  @Override
  public void trace(final Marker marker, final String format, final Object arg) {
    target.trace(marker, format, arg);
  }

  @Override
  public void trace(final Marker marker, final String format, final Object arg1, final Object arg2) {
    target.trace(marker, format, arg1, arg2);
  }

  @Override
  public void trace(final Marker marker, final String format, final Object ... argArray) {
    target.trace(marker, format, argArray);
  }

  @Override
  public void trace(final Marker marker, final String msg, final Throwable t) {
    target.trace(marker, msg, t);
  }

  @Override
  public boolean isDebugEnabled() {
    return isDebugEnabled();
  }

  @Override
  public void debug(final String msg) {
    target.debug(msg);
  }

  @Override
  public void debug(final String format, final Object arg) {
    target.debug(format, arg);
  }

  @Override
  public void debug(final String format, final Object arg1, final Object arg2) {
    target.debug(format, arg1, arg2);
  }

  @Override
  public void debug(final String format, final Object ... arguments) {
    target.debug(format, arguments);
  }

  @Override
  public void debug(final String msg, final Throwable t) {
    target.debug(msg, t);
  }

  @Override
  public boolean isDebugEnabled(final Marker marker) {
    return target.isDebugEnabled(marker);
  }

  @Override
  public void debug(final Marker marker, final String msg) {
    target.debug(marker, msg);
  }

  @Override
  public void debug(final Marker marker, final String format, final Object arg) {
    target.debug(marker, format, arg);
  }

  @Override
  public void debug(final Marker marker, final String format, final Object arg1, final Object arg2) {
    target.debug(marker, format, arg1, arg2);
  }

  @Override
  public void debug(final Marker marker, final String format, final Object ... arguments) {
    target.debug(marker, format, arguments);
  }

  @Override
  public void debug(final Marker marker, final String msg, final Throwable t) {
    target.debug(marker, msg, t);
  }

  @Override
  public boolean isInfoEnabled() {
    return target.isInfoEnabled();
  }

  @Override
  public void info(final String msg) {
    target.info(msg);
  }

  @Override
  public void info(final String format, final Object arg) {
    target.info(format, arg);
  }

  @Override
  public void info(final String format, final Object arg1, final Object arg2) {
    target.info(format, arg1, arg2);
  }

  @Override
  public void info(final String format, final Object ... arguments) {
    target.info(format, arguments);
  }

  @Override
  public void info(final String msg, final Throwable t) {
    target.info(msg, t);
  }

  @Override
  public boolean isInfoEnabled(final Marker marker) {
    return target.isInfoEnabled(marker);
  }

  @Override
  public void info(final Marker marker, final String msg) {
    target.info(marker, msg);
  }

  @Override
  public void info(final Marker marker, final String format, final Object arg) {
    target.info(marker, format, arg);
  }

  @Override
  public void info(final Marker marker, final String format, final Object arg1, final Object arg2) {
    target.info(marker, format, arg1, arg2);
  }

  @Override
  public void info(final Marker marker, final String format, final Object ... arguments) {
    target.info(marker, format, arguments);
  }

  @Override
  public void info(final Marker marker, final String msg, final Throwable t) {
    target.info(marker, msg);
  }

  @Override
  public boolean isWarnEnabled() {
    return target.isWarnEnabled();
  }

  @Override
  public void warn(final String msg) {
    target.warn(msg);
  }

  @Override
  public void warn(final String format, final Object arg) {
    target.warn(format, arg);
  }

  @Override
  public void warn(final String format, final Object ... arguments) {
    target.warn(format, arguments);
  }

  @Override
  public void warn(final String format, final Object arg1, final Object arg2) {
    target.warn(format, arg1, arg2);
  }

  @Override
  public void warn(final String msg, final Throwable t) {
    target.warn(msg, t);
  }

  @Override
  public boolean isWarnEnabled(final Marker marker) {
    return isWarnEnabled(marker);
  }

  @Override
  public void warn(final Marker marker, final String msg) {
    target.warn(marker, msg);
  }

  @Override
  public void warn(final Marker marker, final String format, final Object arg) {
    target.warn(marker, format, arg);
  }

  @Override
  public void warn(final Marker marker, final String format, final Object arg1, final Object arg2) {
    target.warn(marker, format, arg1, arg2);
  }

  @Override
  public void warn(final Marker marker, final String format, final Object ... arguments) {
    target.warn(marker, format, arguments);
  }

  @Override
  public void warn(final Marker marker, final String msg, final Throwable t) {
    target.warn(marker, msg);
  }

  @Override
  public boolean isErrorEnabled() {
    return target.isErrorEnabled();
  }

  @Override
  public void error(final String msg) {
    target.error(msg);
  }

  @Override
  public void error(final String format, final Object arg) {
    target.error(format, arg);
  }

  @Override
  public void error(final String format, final Object arg1, final Object arg2) {
    target.error(format, arg1, arg2);
  }

  @Override
  public void error(final String format, final Object ... arguments) {
    target.error(format, arguments);
  }

  @Override
  public void error(final String msg, final Throwable t) {
    target.error(msg, t);
  }

  @Override
  public boolean isErrorEnabled(final Marker marker) {
    return target.isErrorEnabled(marker);
  }

  @Override
  public void error(final Marker marker, final String msg) {
    target.error(marker, msg);
  }

  @Override
  public void error(final Marker marker, final String format, final Object arg) {
    target.error(marker, format, arg);
  }

  @Override
  public void error(final Marker marker, final String format, final Object arg1, final Object arg2) {
    target.error(marker, format, arg1, arg2);
  }

  @Override
  public void error(final Marker marker, final String format, final Object ... arguments) {
    target.error(marker, format, arguments);
  }

  @Override
  public void error(final Marker marker, final String msg, final Throwable t) {
    target.error(marker, msg);
  }
}