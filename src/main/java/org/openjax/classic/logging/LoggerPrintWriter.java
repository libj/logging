/* Copyright (c) 2016 OpenJAX
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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.event.Level;

/**
 * A {@link PrintWriter} that delegates its methods to a target {@code Logger}.
 * The {@code print()}, {@code println()}, {@code write()}, and {@code append()}
 * methods will result in a new log event only if the log message ends with a
 * {@code '\n'} character. If a message does not end with a {@code '\n'}
 * character, it will be buffered until a {@code '\n'} character is encountered
 * as the last character of a later call.
 */
public class LoggerPrintWriter extends PrintWriter {
  private final StringBuffer buffer = new StringBuffer();
  private final Logger logger;
  private final Level level;

  /**
   * Creates a new {@code LoggerPrintWriter} with the specified {@link Logger}
   * and {@link Level}.
   *
   * @param logger The {@link Logger} instance to which methods of this
   *          {@code LoggerPrintWriter} will be delegated.
   * @param level The {@link Level} that will be used for log statements from
   *          this {@code LoggerPrintWriter}.
   */
  public LoggerPrintWriter(final Logger logger, final Level level) {
    super(new Writer() {
      @Override
      public void close() throws IOException {
      }

      @Override
      public void flush() throws IOException {
      }

      @Override
      public void write(final char[] cbuf, final int off, final int len) throws IOException {
      }
    });

    this.logger = Objects.requireNonNull(logger);
    this.level = Objects.requireNonNull(level);
  }

  @Override
  public void flush() {
    if (buffer.length() == 0 || buffer.charAt(buffer.length() - 1) != '\n')
      return;

    LoggerUtil.log(logger, level, buffer.toString());
    buffer.setLength(0);
  }

  @Override
  public void close() {
    if (buffer.length() > 0 && buffer.charAt(buffer.length() - 1) != '\n')
      buffer.append('\n');

    flush();
  }

  @Override
  public void write(final int c) {
    buffer.append(c);
    flush();
  }

  @Override
  public void write(final char[] buf, final int off, final int len) {
    buffer.append(buf, off, len);
    flush();
  }

  @Override
  public void write(final char[] buf) {
    buffer.append(buf);
    flush();
  }

  @Override
  public void write(final String s, final int off, final int len) {
    buffer.append(s.substring(off, off + len));
    flush();
  }

  @Override
  public void write(final String s) {
    buffer.append(s);
    flush();
  }

  @Override
  public void print(final boolean b) {
    buffer.append(b);
  }

  @Override
  public void print(final char c) {
    buffer.append(c);
    flush();
  }

  @Override
  public void print(final int i) {
    buffer.append(i);
    flush();
  }

  @Override
  public void print(final long l) {
    buffer.append(l);
    flush();
  }

  @Override
  public void print(final float f) {
    buffer.append(f);
    flush();
  }

  @Override
  public void print(final double d) {
    buffer.append(d);
    flush();
  }

  @Override
  public void print(final char[] s) {
    buffer.append(s);
    flush();
  }

  @Override
  public void print(final String s) {
    buffer.append(s);
    flush();
  }

  @Override
  public void print(final Object obj) {
    buffer.append(obj);
    flush();
  }

  @Override
  public void println() {
    buffer.append('\n');
    flush();
  }

  @Override
  public void println(final boolean x) {
    buffer.append(x).append('\n');
    flush();
  }

  @Override
  public void println(final char x) {
    buffer.append(x).append('\n');
    flush();
  }

  @Override
  public void println(final int x) {
    buffer.append(x).append('\n');
    flush();
  }

  @Override
  public void println(final long x) {
    buffer.append(x).append('\n');
    flush();
  }

  @Override
  public void println(final float x) {
    buffer.append(x).append('\n');
    flush();
  }

  @Override
  public void println(final double x) {
    buffer.append(x).append('\n');
    flush();
  }

  @Override
  public void println(final char[] x) {
    buffer.append(x).append('\n');
    flush();
  }

  @Override
  public void println(final String x) {
    buffer.append(x).append('\n');
    flush();
  }

  @Override
  public void println(final Object x) {
    buffer.append(x).append('\n');
    flush();
  }

  @Override
  public PrintWriter printf(final String format, final Object ... args) {
    return format(format, args);
  }

  @Override
  public PrintWriter printf(final Locale l, final String format, final Object ... args) {
    return format(l, format, args);
  }

  @Override
  public PrintWriter format(final String format, final Object ... args) {
    buffer.append(String.format(format, args));
    flush();
    return this;
  }

  @Override
  public PrintWriter format(final Locale l, final String format, final Object ... args) {
    buffer.append(String.format(l, format, args));
    flush();
    return this;
  }

  @Override
  public PrintWriter append(final CharSequence csq) {
    buffer.append(csq);
    flush();
    return this;
  }

  @Override
  public PrintWriter append(final CharSequence csq, final int start, final int end) {
    buffer.append(csq, start, end);
    flush();
    return this;
  }

  @Override
  public PrintWriter append(final char c) {
    buffer.append(c);
    flush();
    return this;
  }
}