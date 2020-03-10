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

package io.opentracing.contrib.specialagent.test.servlet.tomcat;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.opentracing.util.GlobalTracer;

public class AsyncServlet extends HttpServlet {
  private static final long serialVersionUID = 6184640156851545023L;

  @Override
  public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
    if (GlobalTracer.get().activeSpan() == null)
      throw new AssertionError("ERROR: no active span");

    final AsyncContext asyncContext = request.startAsync(request, response);
    new Thread() {
      @Override
      public void run() {
        try {
          if (GlobalTracer.get().activeSpan() == null)
            throw new AssertionError("ERROR: no active span");

          final ServletResponse response = asyncContext.getResponse();
          response.setContentType("text/plain");
          final PrintWriter out = response.getWriter();
          out.println("Async Servlet active span: " + GlobalTracer.get().activeSpan());
          out.flush();
          asyncContext.complete();
        }
        catch (final Exception e) {
          throw new RuntimeException(e);
        }
      }
    }.start();
  }
}