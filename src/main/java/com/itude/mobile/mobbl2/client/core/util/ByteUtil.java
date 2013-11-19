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
package com.itude.mobile.mobbl2.client.core.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.primitives.Bytes;

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

    byte[] result = null;

    //According to the information on Microsoft's and the Unicode Consortium's websites, positions 81, 8D, 8F, 90, and 9D are unused
    if (encodingType.equals("windows-1252"))
    {
      List<Byte> stripped = new ArrayList<Byte>(bytes.length);

      for (byte b : bytes)
      {
        if (b == (byte) 0x81)
        {
          stripped.add((byte) -62);
          stripped.add((byte) -127);
        }
        else if (b == (byte) 0x8D)
        {
          stripped.add((byte) -62);
          stripped.add((byte) -115);
        }
        else if (b == (byte) 0x8F)
        {
          stripped.add((byte) -62);
          stripped.add((byte) -113);
        }
        else if (b == (byte) 0x90)
        {
          stripped.add((byte) -62);
          stripped.add((byte) -112);
        }
        else if (b == (byte) 0x9D)
        {
          stripped.add((byte) -62);
          stripped.add((byte) -99);
        }
        else
        {
          String resultString = encodeBytesToString(new byte[]{b}, encodingType);
          byte[] encodedBytes = null;
          try
          {

            encodedBytes = resultString.getBytes("UTF-8");
          }
          catch (UnsupportedEncodingException e)
          {
            LOG.warn("unable is encode bytes with type " + encodingType);
            encodedBytes = resultString.getBytes();
          }

          for (byte stringbyte : encodedBytes)
          {
            stripped.add(stringbyte);
          }
        }
      }
      result = Bytes.toArray(stripped);
    }
    else
    {
      String resultString = encodeBytesToString(bytes, encodingType);
      //
      try
      {
        result = resultString.getBytes("UTF-8");
      }
      catch (UnsupportedEncodingException e)
      {
        LOG.warn("unable is encode bytes with type " + encodingType);
        result = resultString.getBytes();
      }
    }
    return result;
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
