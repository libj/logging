/* Copyright (c) 2006 lib4j
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

import java.text.MessageFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ConsoleFormatter extends Formatter {
  private static final String format = "{0,date} {0,time}";
  private static final String newline = System.getProperty("line.separator");

  private final MessageFormat formatter;

  public ConsoleFormatter() {
    this.formatter = new MessageFormat(format);
  }

  /**
   * Format the given LogRecord.
   * @param record the log record to be formatted.
   * @return a formatted log record
   */
  @Override
  public String format(final LogRecord record) {
    synchronized (formatter) {
      final StringBuilder builder = new StringBuilder();
      final String message = formatMessage(record);
      builder.append("[").append(record.getLevel().getLocalizedName()).append("] ");
      builder.append(message);
      builder.append(newline);
      return builder.toString();
    }
  }
}