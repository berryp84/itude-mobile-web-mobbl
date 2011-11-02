package com.itude.mobile.mobbl2.client.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import com.itude.mobile.mobbl2.client.core.util.exceptions.MBDataParsingException;

public class FileUtil
{
  private static final Logger _log = Logger.getLogger(FileUtil.class);

  private static FileUtil     _instance;

  private FileUtil()
  {
  }

  public static FileUtil getInstance()
  {
    if (_instance == null)
    {
      _instance = new FileUtil();
    }

    return _instance;
  }

  public byte[] getByteArray(String fileName) throws MBDataParsingException
  {
    throw new UnsupportedOperationException();
  }

  public boolean writeToFile(byte[] fileContents, String fileName, String encoding)
  {
    throw new UnsupportedOperationException();
  }

  public boolean writeObjectToFile(Object object, String fileName)
  {
    ByteArrayOutputStream byteStream = null;
    ObjectOutputStream out = null;
    boolean success = false;

    try
    {
      byteStream = new ByteArrayOutputStream();
      out = new ObjectOutputStream(byteStream);
      out.writeObject(object);
      writeToFile(byteStream.toByteArray(), fileName, null);
      success = true;
    }
    catch (Exception e)
    {
      _log.warn("Failed to serialize object, or to write to file", e);
    }
    finally
    {
      try
      {
        if (out != null) out.close();
        if (byteStream != null) byteStream.close();
      }
      catch (Exception e)
      {
        _log.warn("Unable to close streams", e);
      }
    }
    return success;
  }

  public Object readObjectFromFile(String fileName)
  {
    ByteArrayInputStream byteStream = null;
    ObjectInputStream in = null;
    Object result = null;
    try
    {
      byte[] bytes = getByteArray(fileName);
      byteStream = new ByteArrayInputStream(bytes);
      in = new ObjectInputStream(byteStream);
      result = in.readObject();
    }
    catch (Exception e)
    {
      _log.warn("Failed to read from file, or to deserialize", e);
    }
    finally
    {
      try
      {
        if (in != null) in.close();
        if (byteStream != null) byteStream.close();
      }
      catch (Exception e)
      {
        _log.warn("Unable to close streams", e);
      }
    }
    return result;
  }

  /***
   * Remove file based on the file name
   * 
   * @param fileName
   * @return true if remove was successful, false otherwise
   */
  public boolean remove(String fileName)
  {
    throw new UnsupportedOperationException();
  }

}
