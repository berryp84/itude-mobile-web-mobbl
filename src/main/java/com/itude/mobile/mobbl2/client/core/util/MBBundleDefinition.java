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

import com.itude.mobile.mobbl2.client.core.configuration.MBDefinition;

public class MBBundleDefinition extends MBDefinition
{
  private String _languageCode;
  private String _url;

  public String getLanguageCode()
  {
    return _languageCode;
  }

  public void setLanguageCode(String languageCode)
  {
    _languageCode = languageCode;
  }

  public String getUrl()
  {
    return _url;
  }

  public void setUrl(String url)
  {
    _url = url;
  }

}
