/* Copyright 2020 The OpenTracing Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opentracing.contrib.specialagent.rule.apache.httpclient;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ConfigurationTest {
  @Test
  public void testExplicitSpanDecorators() {
    testDecorators(ApacheClientSpanDecorator.StandardTags.class.getName() + "," + MockSpanDecorator.class.getName(), ApacheClientSpanDecorator.StandardTags.class, MockSpanDecorator.class);
  }

  @Test
  public void testImplicitSpanDecorators() {
    testDecorators(null, ApacheClientSpanDecorator.StandardTags.class);
  }

  private static void testDecorators(final String spanDecoratorsArgs, final Class<?> ... expecteds) {
    System.clearProperty("sa.integration.apache:httpclient.spanDecorators");
    if(spanDecoratorsArgs != null)
      System.setProperty("sa.integration.apache:httpclient.spanDecorators", spanDecoratorsArgs);
    final List<ApacheClientSpanDecorator> decorators = Configuration.parseSpanDecorators();
    assertEquals(expecteds.length, decorators.size());
    final List<Class<?>> list = Arrays.asList(expecteds);
    for (final ApacheClientSpanDecorator decorator : decorators)
      assertTrue(list.contains(decorator.getClass()));
  }
}