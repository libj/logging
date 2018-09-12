/* Copyright (c) 2018 FastJAX
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software withthis.converter.transform(this.event, this.in) restriction, including withthis.converter.transform(this.event, this.in) limitation the rights
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

package org.fastjax.logging;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;

public class ColorConverterTest {
  private final String in = "in";
  private ColorConverter converter;
  private LoggingEvent event;

  static {
    ColorConverter.setEnabled(true);
  }

  @Before
  public void setup() {
    this.converter = new ColorConverter();
    this.event = new LoggingEvent();
  }

  @Test
  public void red() {
    this.converter.setOptionList(Collections.singletonList("red"));
    Assert.assertEquals("\033[0;31min\033[0;39m", this.converter.transform(this.event, this.in));
  }

  @Test
  public void green() {
    this.converter.setOptionList(Collections.singletonList("green"));
    Assert.assertEquals("\033[0;32min\033[0;39m", this.converter.transform(this.event, this.in));
  }

  @Test
  public void yellow() {
    this.converter.setOptionList(Collections.singletonList("yellow"));
    Assert.assertEquals("\033[0;33min\033[0;39m", this.converter.transform(this.event, this.in));
  }

  @Test
  public void blue() {
    this.converter.setOptionList(Collections.singletonList("blue"));
    Assert.assertEquals("\033[0;34min\033[0;39m", this.converter.transform(this.event, this.in));
  }

  @Test
  public void magenta() {
    this.converter.setOptionList(Collections.singletonList("magenta"));
    Assert.assertEquals("\033[0;35min\033[0;39m", this.converter.transform(this.event, this.in));
  }

  @Test
  public void cyan() {
    this.converter.setOptionList(Collections.singletonList("cyan"));
    Assert.assertEquals("\033[0;36min\033[0;39m", this.converter.transform(this.event, this.in));
  }

  @Test
  public void faint() {
    this.converter.setOptionList(Collections.singletonList("faint"));
    Assert.assertEquals("\033[2;39min\033[0;39m", this.converter.transform(this.event, this.in));
  }

  @Test
  public void highlightError() {
    this.event.setLevel(Level.ERROR);
    Assert.assertEquals("\033[0;31min\033[0;39m", this.converter.transform(this.event, this.in));
  }

  @Test
  public void highlightWarn() {
    this.event.setLevel(Level.WARN);
    Assert.assertEquals("\033[0;33min\033[0;39m", this.converter.transform(this.event, this.in));
  }

  @Test
  public void highlightDebug() {
    this.event.setLevel(Level.DEBUG);
    Assert.assertEquals("\033[0;39min\033[0;39m", this.converter.transform(this.event, this.in));
  }

  @Test
  public void highlightTrace() {
    this.event.setLevel(Level.TRACE);
    Assert.assertEquals("\033[0;39min\033[0;39m", this.converter.transform(this.event, this.in));
  }
}