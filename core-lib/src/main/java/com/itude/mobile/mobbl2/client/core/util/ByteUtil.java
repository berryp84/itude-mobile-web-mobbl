package com.itude.mobile.mobbl2.client.core.util;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

/**
 *
 */
public final class ByteUtil
{

  private static final Logger LOG = Logger.getLogger(ByteUtil.class);

  private ByteUtil()
  {
  }

  static public byte[] encodeBytes(byte[] bytes, String encodingType)
  {
    String result = "";
    try
    {
      result = new String(bytes, encodingType);
    }
    catch (UnsupportedEncodingException e)
    {
      LOG.warn("unable is encode bytes with type " + encodingType);
    }
    return result.getBytes();
  }

  static public String encodeBytesToString(byte[] bytes, String encodingType)
  {
    String result = "";
    try
    {
      result = new String(bytes, encodingType);
    }
    catch (UnsupportedEncodingException e)
    {
      LOG.warn("unable is encode bytes with type " + encodingType);
    }
    return result;
  }

}
