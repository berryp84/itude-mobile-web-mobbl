/*
 * (C) Copyright Itude Mobile B.V., The Netherlands
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

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.StatusLine;

public class HttpResponse
{
  private int        _responseCode       = -1;
  private byte[]     _responseBody       = null;
  private String     _contentType        = null;
  private Header[]   _responseHeaders    = null;
  private StatusLine _responseStatusLine = null;
  private int        _contentLength      = -1;

  public void setResponseCode(int responseCode)
  {
    _responseCode = responseCode;
  }

  public int getResponseCode()
  {
    return _responseCode;
  }

  public void setResponseBody(byte[] responseBody)
  {
    _responseBody = responseBody;
  }

  public byte[] getResponseBody()
  {
    return _responseBody;
  }

  public void setContentType(String contentType)
  {
    _contentType = contentType;
  }

  public String getContentType()
  {
    return _contentType;
  }

  public void setResponseHeaders(Header[] responseHeaders)
  {
    _responseHeaders = responseHeaders;
  }

  public Header[] getResponseHeaders()
  {
    return _responseHeaders;
  }

  public void setResponseStatusLine(StatusLine responseStatusLine)
  {
    _responseStatusLine = responseStatusLine;
  }

  public StatusLine getResponseStatusLine()
  {
    return _responseStatusLine;
  }

  public void setContentLength(int contentLength)
  {
    _contentLength = contentLength;
  }

  public void setContentLength(String contentLength)
  {
    _contentLength = Integer.parseInt(contentLength);
  }

  public int getContentLength()
  {
    return _contentLength;
  }
}
