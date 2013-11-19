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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.log4j.Logger;

import com.itude.mobile.mobbl2.client.core.util.exceptions.MBDataParsingException;

public final class DataUtil
{
  private static final Logger LOGGER = Logger.getLogger(DataUtil.class);

  private static DataUtil     _instance;

  private DataUtil()
  {
  }

  public static DataUtil getInstance()
  {
    if (_instance == null)
    {
      _instance = new DataUtil();
    }

    return _instance;
  }

  public byte[] readResource(String fileName)
  {
    boolean foundData = false;
    byte[] data = null;

    InputStream stream = com.itude.mobile.web.util.FileUtil.getResourceAsStream(fileName);
    if (stream != null)
    {
      try
      {
        data = com.itude.mobile.web.util.FileUtil.readBytes(stream);
        foundData = true;
      }
      catch (IOException e)
      {
      }
    }

    if (!foundData)
    {
      String message = "DataUtil.readResource: unable to read file or asset data from file with name " + fileName;

      // This exception used to be thrown. There are situations where it's expected that no data will be returned.
      // E.g. a Session document file will only be there when you have been logged into a session.
      throw new MBDataParsingException(message);
      //LOGGER.info(message);
    }

    return data;
  }

  public byte[] compress(byte[] uncompressed)
  {
    byte[] result = null;

    Deflater deflater = new Deflater();
    deflater.setInput(uncompressed);
    deflater.finish();

    // Create an expandable byte array to hold the compressed data. 
    // You cannot use an array that's the same size as the original because 
    // there is no guarantee that the compressed data will be smaller than 
    // the uncompressed data. 

    ByteArrayOutputStream bos = new ByteArrayOutputStream(uncompressed.length);

    // Compress the data 
    byte[] buf = new byte[1024];
    while (!deflater.finished())
    {
      int count = deflater.deflate(buf);
      bos.write(buf, 0, count);
    }

    try
    {
      bos.close();
    }
    catch (IOException e)
    {
      LOGGER.warn("Unable to close stream");
    }

    // Get the compressed data 

    result = bos.toByteArray();

    return result;
  }

  public byte[] decompress(byte[] compressed)
  {
    Inflater decompressor = new Inflater();
    decompressor.setInput(compressed);

    // Create an expandable byte array to hold the decompressed data 
    ByteArrayOutputStream bos = new ByteArrayOutputStream(compressed.length);

    // Decompress the data 
    byte[] buf = new byte[1024];
    while (!decompressor.finished())
    {
      try
      {
        int count = decompressor.inflate(buf);
        bos.write(buf, 0, count);
      }
      catch (DataFormatException e)
      {
        return null;
      }
    }
    try
    {
      bos.close();
    }
    catch (IOException e)
    {
      LOGGER.warn("Unable to close stream");
    }

    // Get the decompressed data 
    return bos.toByteArray();
  }

}
