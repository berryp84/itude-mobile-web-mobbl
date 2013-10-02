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
package com.itude.mobile.mobbl.server.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.Cookie;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.log4j.Logger;

import com.itude.mobile.mobbl.server.mobileweb.Constants;
import com.itude.mobile.mobbl.server.util.HeaderUtil;

public class HttpDelegate
{
  // http://hc.apache.org/httpclient-3.x/threading.html#MultiThreadedHttpConnectionManager
  private static final int    TOTAL_CONNECTIONS    = 50;
  private static final int    CONNECTIONS_PER_HOST = 20;
  private static final int    CONNECTION_TIMEOUT   = 30 * 1000;

  private HttpClient          httpClient           = null;

  private static HttpDelegate _instance            = null;
  private static final Logger logger               = Logger.getLogger(HttpDelegate.class.getName());

  public static HttpDelegate getInstance()
  {
    if (_instance == null) _instance = new HttpDelegate();
    return _instance;
  }

  private HttpDelegate()
  {
    logger.debug("HttpDelegate.HttpDelegate()");

    MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
    HttpConnectionManagerParams connectionParameters = connectionManager.getParams();
    connectionParameters.setDefaultMaxConnectionsPerHost(CONNECTIONS_PER_HOST);
    connectionParameters.setMaxTotalConnections(TOTAL_CONNECTIONS);
    connectionParameters.setConnectionTimeout(CONNECTION_TIMEOUT);

    httpClient = new HttpClient(connectionManager);
  }

  public HttpResponse connectGet(String url)
  {
    return connectGet(url, new Header[0]);
  }

  public HttpResponse connectGet(String url, TreeMap<String, String[]> requestHeaders)
  {
    Header[] headers = transformToHeader(requestHeaders);
    return connectGet(url, headers);
  }

  public HttpResponse connectGet(String url, Header[] requestHeaders)
  {
    return connectGet(url, null, null, requestHeaders, false);
  }

  public HttpResponse connectGet(String url, String userAgent, ArrayList<Cookie> reqCookies, Header[] requestHeaders,
                                 boolean followRedirects)
  {
    logger.debug("HttpDelegate.connectGet(): " + url);

    GetMethod get = new GetMethod(url);
    try
    {
      prepareConnection(get, userAgent, reqCookies, requestHeaders, !url.contains("acceptxml=false"), followRedirects);
      if (logger.isDebugEnabled()) HeaderUtil.printRequestHeaders(logger, get);

      return executeHttpMethod(get);
    }
    catch (HttpException e)
    {
      logger.error(e.getMessage());
      logger.trace(e.getMessage(), e);
    }
    catch (IOException e)
    {
      logger.error(e.getMessage());
      logger.trace(e.getMessage(), e);
    }
    finally
    {
      get.releaseConnection();
    }

    return null;
  }

  public HttpResponse connectPost(String url, NameValuePair[] postData)
  {
    return connectPost(url, postData, new Header[0]);
  }

  public HttpResponse connectPost(String url, NameValuePair[] postData, TreeMap<String, String[]> requestHeaders)
  {
    Header[] headers = transformToHeader(requestHeaders);
    return connectPost(url, postData, headers);
  }

  public HttpResponse connectPost(String url, NameValuePair[] postData, Header[] requestHeaders)
  {
    return connectPost(url, null, null, postData, requestHeaders, false);
  }

  public HttpResponse connectPost(String url, String userAgent, ArrayList<Cookie> reqCookies, NameValuePair[] postData,
                                  Header[] requestHeaders, boolean followRedirects)
  {
    logger.debug("HttpDelegate.connectPost(): " + url);

    PostMethod post = new PostMethod(url);
    try
    {
      prepareConnection(post, userAgent, reqCookies, requestHeaders, !url.contains("acceptxml=false"), followRedirects);
      if (postData != null) post.setRequestBody(postData);

      return executeHttpMethod(post);
    }
    catch (HttpException e)
    {
      logger.error(e.getMessage());
      logger.trace(e.getMessage(), e);
    }
    catch (IOException e)
    {
      logger.error(e.getMessage());
      logger.trace(e.getMessage(), e);
    }
    finally
    {
      post.releaseConnection();
    }

    return null;
  }

  private HttpResponse executeHttpMethod(HttpMethod method) throws HttpException, IOException
  {
    HttpResponse response = new HttpResponse();
    String responseHeader = null;

    response.setResponseCode(httpClient.executeMethod(method));
    try
    {
      responseHeader = method.getResponseHeader(Constants.CONTENT_TYPE).getValue();
    }
    catch (NullPointerException e)
    {
      logger.debug("ResponseHeader was null");
    }

    if (responseHeader != null) response.setContentType(responseHeader);
    //    response.setResponseBody(method.getResponseBody());
    response.setResponseHeaders(method.getResponseHeaders());
    response.setResponseStatusLine(response.getResponseStatusLine());
    Header contentLength = method.getResponseHeader(Constants.CONTENT_LENGTH);
    if (contentLength != null) response.setContentLength(contentLength.getValue());

    if (response.getContentLength() > -1)
    {
      InputStream stream = null;
      byte[] body = new byte[response.getContentLength()];
      try
      {
        stream = method.getResponseBodyAsStream();

        int offset = 0;
        int i = -1;
        while ((i = stream.read(body, offset, 1024)) != -1)
          offset += i;
        response.setResponseBody(body);
      }
      catch (Exception e)
      {
        logger.error("Could not read entire response body", e);
      }
      finally
      {
        if (stream != null) stream.close();
      }
    }
    else
    {
      logger.debug("Content-length unknown");

      InputStream stream = null;
      ByteArrayOutputStream body = new ByteArrayOutputStream();
      try
      {
        stream = method.getResponseBodyAsStream();

        int i = -1;
        while ((i = stream.read()) != -1)
          body.write(i);

        response.setResponseBody(body.toByteArray());
        response.setContentLength(body.size());
      }
      catch (Exception e)
      {
        logger.error("Could not read entire response body", e);
      }
      finally
      {
        body.close();
        if (stream != null) stream.close();
      }
    }

    return response;
  }

  private void prepareConnection(HttpMethod method, String userAgent, ArrayList<Cookie> cookies, Header[] requestHeaders,
                                 boolean acceptXml, boolean followRedirects)
  {
    logger.debug("HttpDelegate.prepareConnection()");

    if (logger.isDebugEnabled()) HeaderUtil.printRequestHeaders(logger, method);

    if (acceptXml) method.addRequestHeader("Accept", "application/xml, */*");

    if (cookies != null)
    {
      for (Cookie cookie : cookies)
        method.addRequestHeader("Cookie", cookie.getName() + "=" + cookie.getValue());
    }

    if (userAgent != null) method.addRequestHeader(Constants.USER_AGENT, userAgent);

    method.setFollowRedirects(followRedirects);

    if (requestHeaders != null) for (Header h : requestHeaders)
      method.addRequestHeader(h);

  }

  private synchronized Header[] transformToHeader(TreeMap<String, String[]> headers)
  {
    if (headers == null) return null;

    Header[] result = new Header[headers.size()];
    Iterator<Entry<String, String[]>> iterator = headers.entrySet().iterator();

    int i = 0;
    while (iterator.hasNext())
    {
      Entry<String, String[]> entry = iterator.next();
      for (String s : entry.getValue())
        result[i] = new Header(entry.getKey(), s);
      i++;
    }

    return result;
  }
}
