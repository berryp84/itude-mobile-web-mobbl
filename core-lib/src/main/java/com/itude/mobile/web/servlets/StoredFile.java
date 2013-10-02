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
package com.itude.mobile.web.servlets;

public class StoredFile
{
  private final byte[] _data;
  private final String _mimeType;
  private long         _ttl;

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
