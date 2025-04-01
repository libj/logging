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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ANSIConstants;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

public class ColorCompositeConverter extends ForegroundCompositeConverterBase<ILoggingEvent> {
  @Override
  protected String getForegroundColorCode(final ILoggingEvent event) {
    final int level = event.getLevel().toInt();
    if (level >= Level.ERROR_INT)
      return "38;5;203"; // red

    if (level >= Level.WARN_INT)
      return "38;5;214"; // orange

    if (level >= Level.INFO_INT)
      return ANSIConstants.DEFAULT_FG;

    if (level >= Level.DEBUG_INT)
      return ANSIConstants.CYAN_FG;

    return "38;5;114"; // green
  }
}