/* Copyright (c) 2018 LibJ
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;

/**
 * A {@link CompositeConverter} implementation that colorizes log messages.
 */
public class ColorConverter extends CompositeConverter<ILoggingEvent> {
  private static final Map<String,AnsiColor> colors = new HashMap<>();

  /**
   * Enum of standard ANSI color codes.
   */
  private enum AnsiColor {
    BLACK("0"), RED("1"), GREEN("2"), YELLOW("3"), BLUE("4"), MAGENTA("5"), CYAN("6"), WHITE("7"), DEFAULT("9");

    private final String code;

    AnsiColor(final String code) {
      this.code = code;
      colors.put(name().toLowerCase(), this);
    }

    @Override
    public String toString() {
      return code;
    }
  }

  private static final Map<Integer,AnsiColor> levels = new HashMap<>();

  static {
    levels.put(Level.ERROR_INTEGER, AnsiColor.RED);
    levels.put(Level.WARN_INTEGER, AnsiColor.YELLOW);
  }

  private static final String ENCODE_START = "\033[";
  private static final String ENCODE_END = "m";
  private static final String RESET = "0;3" + AnsiColor.DEFAULT;

  private static Boolean enabled;

  /**
   * Returns {@code true} if the {@link ColorConverter} is enabled; otherwise
   * {@code false}.
   *
   * @return {@code true} if the {@link ColorConverter} is enabled; otherwise
   *         {@code false}.
   */
  private static boolean isEnabled() {
    if (enabled != null)
      return enabled;

    try {
      return enabled = System.getProperty("os.name").toLowerCase().indexOf("win") < 0;
    }
    catch (final Throwable t) {
      return false;
    }
  }

  /**
   * Sets the enabled state of the {@link ColorConverter} to the value of
   * {@code enabled}.
   *
   * @param enabled The value to which the enabled state of the
   *          {@link ColorConverter} should be set.
   */
  public static void setEnabled(final boolean enabled) {
    ColorConverter.enabled = enabled;
  }

  /**
   * Transforms the specified {@link ILoggingEvent} by amending its message with
   * color options specified in the {@code <pattern>} element under
   * {@code <layout>} in {@code logback.xml}.
   * <p>
   * An example {@code <pattern>} that specifies color codes and options is:
   *
   * <pre>
   * {@code <pattern>[%color(%4level){bold,blue}] %color(%msg%n){magenta}</pattern>}
   * </pre>
   *
   * @throws NullPointerException If the specified {@link ILoggingEvent} is
   *           null.
   */
  @Override
  protected String transform(final ILoggingEvent event, final String in) {
    if (!isEnabled())
      return in;

    String group = "3";
    String strength = "0";
    AnsiColor color = null;
    final List<String> options = getOptionList();
    if (options != null) {
      for (final String option : getOptionList()) {
        if ("bold".equals(option)) {
          group = "3";
          strength = "1";
        }
        else if ("faint".equals(option)) {
          group = "3";
          strength = "2";
        }
        else if ("italic".equals(option)) {
          group = "3";
          strength = "3";
        }
        else if ("underline".equals(option)) {
          group = "3";
          strength = "4";
        }
        else if ("intense".equals(option)) {
          group = "9";
          strength = "0";
        }
        else {
          color = colors.get(option);
        }
      }
    }

    if (color == null && event.getLevel() != null)
      color = levels.get(event.getLevel().toInteger());

    if (color == null)
      color = AnsiColor.DEFAULT;

    final StringBuilder builder = new StringBuilder();
    builder.append(ENCODE_START).append(strength);
    if (color != null)
      builder.append(';').append(group).append(color);

    return builder.append(ENCODE_END).append(in).append(ENCODE_START).append(RESET).append(ENCODE_END).toString();
  }
}