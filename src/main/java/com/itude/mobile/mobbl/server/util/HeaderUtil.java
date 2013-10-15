/*
 * (C) Copyright ItudeMobile.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itude.mobile.mobbl.server.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.log4j.Logger;

public class HeaderUtil
{
  public static void printRequestHeaders(Logger logger, HttpServletRequest request)
  {
    if (request == null) throw new IllegalArgumentException("Request can't be null");
    if (logger == null) throw new IllegalArgumentException("Logger can't be null");

    logger.debug("List of request parameters:");

    @SuppressWarnings("unchecked")
    Enumeration<String> requestHeaders = request.getHeaderNames();
    if (requestHeaders != null)
    {
      while (requestHeaders.hasMoreElements())
      {
        String header = requestHeaders.nextElement();

        @SuppressWarnings("unchecked")
        Enumeration<String> headers = request.getHeaders(header);
        if (headers != null)
        {
          while (headers.hasMoreElements())
            logger.debug(header + ":" + headers.nextElement());
        }
      }
    }
  }

  public static void printRequestHeaders(Logger logger, HttpMethod method)
  {
    if (method == null) throw new IllegalArgumentException("Method can't be null");
    if (logger == null) throw new IllegalArgumentException("Logger can't be null");

    logger.debug("List of request parameters:");

    Header[] headers = method.getRequestHeaders();
    if (headers != null)
    {
      for (Header h : headers)
        logger.debug(h.getName() + ":" + h.getValue());
    }
  }
}
