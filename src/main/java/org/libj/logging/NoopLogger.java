/* Copyright (c) 2022 LibJ
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

import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * A {@link Logger} that implements all methods as No-Op.
 */
public class NoopLogger implements Logger {
  /**
   * Creates a new {@link NoopLogger}.
   */
  public NoopLogger() {
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public boolean isTraceEnabled() {
    return false;
  }

  @Override
  public void trace(final String msg) {
  }

  @Override
  public void trace(final String format, final Object arg) {
  }

  @Override
  public void trace(final String format, final Object arg1, final Object arg2) {
  }

  @Override
  public void trace(final String format, final Object ... arguments) {
  }

  @Override
  public void trace(final String msg, final Throwable t) {
  }

  @Override
  public boolean isTraceEnabled(final Marker marker) {
    return false;
  }

  @Override
  public void trace(final Marker marker, final String msg) {
  }

  @Override
  public void trace(final Marker marker, final String format, final Object arg) {
  }

  @Override
  public void trace(final Marker marker, final String format, final Object arg1, final Object arg2) {
  }

  @Override
  public void trace(final Marker marker, final String format, final Object ... argArray) {
  }

  @Override
  public void trace(final Marker marker, final String msg, final Throwable t) {
  }

  @Override
  public boolean isDebugEnabled() {
    return false;
  }

  @Override
  public void debug(final String msg) {
  }

  @Override
  public void debug(final String format, final Object arg) {
  }

  @Override
  public void debug(final String format, final Object arg1, final Object arg2) {
  }

  @Override
  public void debug(final String format, final Object ... arguments) {
  }

  @Override
  public void debug(final String msg, final Throwable t) {
  }

  @Override
  public boolean isDebugEnabled(final Marker marker) {
    return false;
  }

  @Override
  public void debug(final Marker marker, final String msg) {
  }

  @Override
  public void debug(final Marker marker, final String format, final Object arg) {
  }

  @Override
  public void debug(final Marker marker, final String format, final Object arg1, final Object arg2) {
  }

  @Override
  public void debug(final Marker marker, final String format, final Object ... arguments) {
  }

  @Override
  public void debug(final Marker marker, final String msg, final Throwable t) {
  }

  @Override
  public boolean isInfoEnabled() {
    return false;
  }

  @Override
  public void info(final String msg) {
  }

  @Override
  public void info(final String format, final Object arg) {
  }

  @Override
  public void info(final String format, final Object arg1, final Object arg2) {
  }

  @Override
  public void info(final String format, final Object ... arguments) {
  }

  @Override
  public void info(final String msg, final Throwable t) {
  }

  @Override
  public boolean isInfoEnabled(final Marker marker) {
    return false;
  }

  @Override
  public void info(final Marker marker, final String msg) {
  }

  @Override
  public void info(final Marker marker, final String format, final Object arg) {
  }

  @Override
  public void info(final Marker marker, final String format, final Object arg1, final Object arg2) {
  }

  @Override
  public void info(final Marker marker, final String format, final Object ... arguments) {
  }

  @Override
  public void info(final Marker marker, final String msg, final Throwable t) {
  }

  @Override
  public boolean isWarnEnabled() {
    return false;
  }

  @Override
  public void warn(final String msg) {
  }

  @Override
  public void warn(final String format, final Object arg) {
  }

  @Override
  public void warn(final String format, final Object ... arguments) {
  }

  @Override
  public void warn(final String format, final Object arg1, final Object arg2) {
  }

  @Override
  public void warn(final String msg, final Throwable t) {
  }

  @Override
  public boolean isWarnEnabled(final Marker marker) {
    return false;
  }

  @Override
  public void warn(final Marker marker, final String msg) {
  }

  @Override
  public void warn(final Marker marker, final String format, final Object arg) {
  }

  @Override
  public void warn(final Marker marker, final String format, final Object arg1, final Object arg2) {
  }

  @Override
  public void warn(final Marker marker, final String format, final Object ... arguments) {
  }

  @Override
  public void warn(final Marker marker, final String msg, final Throwable t) {
  }

  @Override
  public boolean isErrorEnabled() {
    return false;
  }

  @Override
  public void error(final String msg) {
  }

  @Override
  public void error(final String format, final Object arg) {
  }

  @Override
  public void error(final String format, final Object arg1, final Object arg2) {
  }

  @Override
  public void error(final String format, final Object ... arguments) {
  }

  @Override
  public void error(final String msg, final Throwable t) {
  }

  @Override
  public boolean isErrorEnabled(final Marker marker) {
    return false;
  }

  @Override
  public void error(final Marker marker, final String msg) {
  }

  @Override
  public void error(final Marker marker, final String format, final Object arg) {
  }

  @Override
  public void error(final Marker marker, final String format, final Object arg1, final Object arg2) {
  }

  @Override
  public void error(final Marker marker, final String format, final Object ... arguments) {
  }

  @Override
  public void error(final Marker marker, final String msg, final Throwable t) {
  }
}