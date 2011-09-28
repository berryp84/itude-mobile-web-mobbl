package com.itude.mobile.mobbl2.client.core.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itude.mobile.mobbl2.client.core.configuration.MBDefinition;

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
