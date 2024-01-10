/* Copyright (c) 2024 LibJ
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

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.event.Level;

public class PrintStreamLoggerTest {
  @Test
  public void testException() {
    try {
      new PrintStreamLogger(null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      new PrintStreamLogger(Level.DEBUG, null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      new PrintStreamLogger(Level.DEBUG, null, System.out, System.out, System.out, System.out);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      new PrintStreamLogger(Level.DEBUG, System.out, null, System.out, System.out, System.out);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      new PrintStreamLogger(Level.DEBUG, System.out, System.out, null, System.out, System.out);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      new PrintStreamLogger(Level.DEBUG, System.out, System.out, System.out, null, System.out);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      new PrintStreamLogger(Level.DEBUG, System.out, System.out, System.out, System.out, null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }
  }
}