/* Copyright (c) 2025 LibJ
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.pattern.CompositeConverter;

/**
 * A {@link CompositeConverter} that allows for the conversion of a log pattern to be displayed in colors configurable via
 * {@code <variable>} tags.
 * <p>
 * Example:
 *
 * <pre>
 * &lt;variable scope="context" name="cc:com.example.foo" value="[38;5;21m"/>
 * &lt;variable scope="context" name="cc:com.example.bar" value="[38;5;219m"/>
 * &lt;conversionRule conversionWord="customColor" converterClass="org.libj.logging.CustomColorConverter"/>
 * &lt;appender>
 *   &lt;encoder>
 *      &lt;pattern>%date{yyyy-MM-dd HH:mm:ss.SSS} %customColor(%logger){loggerName,cc} %color(%level %msg%n)&lt;/pattern>
 *   &lt;/encoder>
 * &lt;/appender>
 * </pre>
 * <p>
 * In the provided example, two options are specified:
 * <ol>
 * <li>loggerName: The event property to match. Can be one of {level, loggerName, marker, message, threadName}.</li>
 * <li>cc: The variable prefix to be referenced when parsing defined variables.</li>
 * </ol>
 * <p>
 * The following two variables are also defined:
 * <ol>
 * <li>cc:com.example.foo: The key with "cc:" prefix to be matched resulting in the color specified as the value when matching
 * "com.example.foo" as the "loggerName".</li>
 * <li>cc:com.example.bar: The key with "cc:" prefix to be matched resulting in the color specified as the value when matching
 * "com.example.bar" as the "loggerName"</li>
 * </ol>
 */
public class CustomColorConverter extends CompositeConverter<ILoggingEvent> {
  private static final String esc = String.valueOf((char)0x1b);

  private final ArrayList<String> keys = new ArrayList<>();
  private final ArrayList<String> values = new ArrayList<>();

  private enum Property {
    LEVEL("level") {
      @Override
      String getValue(final ILoggingEvent event) {
        return event.getLevel().toString();
      }
    },
    LOGGER_NAME("loggerName") {
      @Override
      String getValue(final ILoggingEvent event) {
        return event.getLoggerName();
      }
    },
    MARKER("marker") {
      @Override
      String getValue(final ILoggingEvent event) {
        return event.getMarker().toString();
      }
    },
    MESSAGE("message") {
      @Override
      String getValue(final ILoggingEvent event) {
        return event.getMessage();
      }
    },
    THREAD_NAME("threadName") {
      @Override
      String getValue(final ILoggingEvent event) {
        return event.getThreadName();
      }
    };

    private final String name;

    private Property(final String name) {
      this.name = name;
    }

    abstract String getValue(ILoggingEvent event);

    @Override
    public String toString() {
      return name;
    }

    private static Property fromString(final String s) {
      for (final Property property : values()) // [A]
        if (property.name.equals(s))
          return property;

      return null;
    }
  }

  private Property property;

  @Override
  public void start() {
    final Context context = getContext();
    if (context == null)
      return;

    final List<String> optionList = super.getOptionList();
    if (optionList == null || optionList.size() != 2) {
      addError("Options {<EVENT_PROPERTY>,<VARIABLE_PREFIX>} are not specified for conversionRule with class " + getClass().getName());
      return;
    }

    final String propertyName = optionList.get(0);
    this.property = Property.fromString(propertyName);
    if (this.property == null) {
      addError("EVENT_PROPERTY (" + propertyName + ") does not match: " + Arrays.toString(Property.values()));
      return;
    }

    final String prefix = optionList.get(1) + ":";
    final int len = prefix.length();
    for (final Map.Entry<String,String> entry : context.getCopyOfPropertyMap().entrySet()) {
      if (entry.getKey().startsWith(prefix)) {
        final String key = entry.getKey().substring(len);
        keys.add(key);
        values.add(entry.getValue());
      }
    }

    super.start();
  }

  private String match(final ILoggingEvent event) {
    final String loggerName = event.getLoggerName();
    final int length = loggerName.length();
    for (int i = 0, i$ = keys.size(); i < i$; ++i) { // [RA]
      final String key = keys.get(i);
      final int len = key.length();
      final String value = values.get(i);
      if (len == length) {
        if (loggerName.equals(value))
          return value;
      }
      else if (len < length) {
        if (loggerName.startsWith(key) && loggerName.charAt(len) == '.')
          return value;
      }
    }

    return null;
  }

  @Override
  protected String transform(final ILoggingEvent event, final String in) {
    final String value = match(event);
    return value != null ? esc + value + in + esc + "[0m" : in;
  }
}