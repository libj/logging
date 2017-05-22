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

package org.safris.commons.logging;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.event.Level;

public class LoggerPrintWriter extends PrintWriter {
  private final StringBuffer buffer = new StringBuffer();
  private final Logger logger;
  private final Level level;

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

    if (logger == null)
      throw new NullPointerException("logger == null");

    if (level == null)
      throw new NullPointerException("level == null");

    this.logger = logger;
    this.level = level;
  }

  private void flushBuffer() {
    if (buffer.length() == 0)
      return;

    LoggerUtil.log(logger, level, buffer.toString());
    buffer.setLength(0);
  }

  @Override
  public void flush() {
    flushBuffer();
    super.flush();
  }

  @Override
  public void close() {
    flushBuffer();
    super.close();
  }

  @Override
  public void write(final int c) {
    buffer.append(c);
  }

  @Override
  public void write(final char[] buf, final int off, final int len) {
    buffer.append(buf, off, len);
  }

  @Override
  public void write(final char[] buf) {
    buffer.append(buf);
  }

  @Override
  public void write(final String s, final int off, final int len) {
    buffer.append(s.substring(off, off + len));
  }

  @Override
  public void write(final String s) {
    buffer.append(s);
  }

  @Override
  public void print(final boolean b) {
    buffer.append(b);
  }

  @Override
  public void print(final char c) {
    buffer.append(c);
  }

  @Override
  public void print(final int i) {
    buffer.append(i);
  }

  @Override
  public void print(final long l) {
    buffer.append(l);
  }

  @Override
  public void print(final float f) {
    buffer.append(f);
  }

  @Override
  public void print(final double d) {
    buffer.append(d);
  }

  @Override
  public void print(final char[] s) {
    buffer.append(s);
  }

  @Override
  public void print(final String s) {
    buffer.append(s);
  }

  @Override
  public void print(final Object obj) {
    buffer.append(obj);
  }

  @Override
  public void println() {
    buffer.append('\n');
    flushBuffer();
  }

  @Override
  public void println(final boolean x) {
    buffer.append(x);
    flushBuffer();
  }

  @Override
  public void println(final char x) {
    buffer.append(x);
    flushBuffer();
  }

  @Override
  public void println(final int x) {
    buffer.append(x);
    flushBuffer();
  }

  @Override
  public void println(final long x) {
    buffer.append(x);
    flushBuffer();
  }

  @Override
  public void println(final float x) {
    buffer.append(x);
    flushBuffer();
  }

  @Override
  public void println(final double x) {
    buffer.append(x);
    flushBuffer();
  }

  @Override
  public void println(final char[] x) {
    buffer.append(x);
    flushBuffer();
  }

  @Override
  public void println(final String x) {
    buffer.append(x);
    flushBuffer();
  }

  @Override
  public void println(final Object x) {
    buffer.append(x);
    flushBuffer();
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
    flushBuffer();
    return this;
  }

  @Override
  public PrintWriter format(final Locale l, final String format, final Object ... args) {
    buffer.append(String.format(l, format, args));
    flushBuffer();
    return this;
  }

  @Override
  public PrintWriter append(final CharSequence csq) {
    buffer.append(csq);
    flushBuffer();
    return this;
  }

  @Override
  public PrintWriter append(final CharSequence csq, final int start, final int end) {
    buffer.append(csq, start, end);
    flushBuffer();
    return this;
  }

  @Override
  public PrintWriter append(final char c) {
    buffer.append(c);
    flushBuffer();
    return this;
  }
}