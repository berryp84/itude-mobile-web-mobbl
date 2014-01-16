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
package com.itude.mobile.mobbl.core.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itude.mobile.mobbl.core.configuration.MBDefinition;

public class MBResultListenerDefinition extends MBDefinition
{
  private Pattern _matchExpression;

  public void setMatchExpression(String matchExpression)
  {
    String[] parts = matchExpression.split("\\*");
    StringBuilder regExp = new StringBuilder();
    for (String part : parts)
      if (!part.isEmpty())
      {
        if (regExp.length() != 0) regExp.append(".*");
        regExp.append("\\Q").append(part).append("\\E");
      }

    _matchExpression = Pattern.compile(regExp.toString());

  }

  public boolean matches(String result)
  {
    Matcher matcher = _matchExpression.matcher(result);
    return matcher.find();
  }

}
