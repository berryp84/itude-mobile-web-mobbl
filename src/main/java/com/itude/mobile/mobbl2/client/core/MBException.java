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
package com.itude.mobile.mobbl2.client.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itude.mobile.web.exceptions.ItudeRuntimeException;

public class MBException extends ItudeRuntimeException
{
  private static final long    serialVersionUID = 1271249723743935918L;

  private static final Pattern NAME_PATTERN     = Pattern.compile(".*\\.MB(.+)Exception");

  private final String         _name;

  private String extractName()
  {
    Matcher matcher = NAME_PATTERN.matcher(this.getClass().getName());
    matcher.find();
    return matcher.group(1);
  }

  public MBException(String msg)
  {
    super(msg);
    _name = extractName();
  }

  public MBException(String msg, Throwable throwable)
  {
    super(msg, throwable);
    _name = extractName();
  }

  public String getName()
  {
    return _name;
  }

}
