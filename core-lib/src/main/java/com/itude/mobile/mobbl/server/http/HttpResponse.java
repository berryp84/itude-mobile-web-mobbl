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
