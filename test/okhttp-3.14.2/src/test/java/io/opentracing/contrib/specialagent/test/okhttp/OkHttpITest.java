/* Copyright 2019 The OpenTracing Authors
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

package io.opentracing.contrib.specialagent.test.okhttp;

import java.io.IOException;

import io.opentracing.contrib.specialagent.TestUtil;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpITest {
  public static void main(final String[] args) throws Exception {
    TestUtil.initTerminalExceptionHandler();
    testBuilder();
    testClient();
    testAsync(new OkHttpClient());
  }

  private static void testBuilder() throws IOException {
    test(new OkHttpClient.Builder().build());
    TestUtil.checkSpan("okhttp", 1);
  }

  private static void testClient() throws IOException {
    test(new OkHttpClient());
    TestUtil.checkSpan("okhttp", 1);
  }

  private static void test(final OkHttpClient client) throws IOException {
    final Request request = new Request.Builder().url("https://www.google.com").build();
    try (final Response response = client.newCall(request).execute()) {
      System.out.println(response.code());
    }

    client.dispatcher().executorService().shutdown();
    client.connectionPool().evictAll();
  }

  private static void testAsync(final OkHttpClient client) throws Exception {
    final Request request = new Request.Builder().url("https://www.google.com").build();
    final CountDownLatch latch = new CountDownLatch(1);
    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        latch.countDown();
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        latch.countDown();
      }
    });

    latch.await(15, TimeUnit.SECONDS);

    TestUtil.checkSpan("okhttp", 1);

    client.dispatcher().executorService().shutdown();
    client.connectionPool().evictAll();
  }
}