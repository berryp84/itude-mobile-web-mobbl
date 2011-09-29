package com.itude.mobile.template.servlets;

public class StoredFile
{
  private final byte[] _data;
  private final String _mimeType;
  private long   _ttl;

  public StoredFile(byte[] data, String mimeType)
  {
    this(data, mimeType, 0);
  }

  public StoredFile(byte[] data, String mimeType, long ttl)
  {
    _data = data;
    _mimeType = mimeType;
    if (ttl == 0) _ttl = 0;
    else _ttl = System.currentTimeMillis() + ttl;
  }

  public byte[] getData()
  {
    return _data;
  }

  public String getMimeType()
  {
    return _mimeType;
  }

  public long getTTL()
  {
    return _ttl;
  }
  
  public void setTTL(long ttl)
  {
    _ttl = ttl;
  }
}
