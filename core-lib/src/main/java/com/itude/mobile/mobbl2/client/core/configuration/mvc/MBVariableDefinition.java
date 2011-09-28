package com.itude.mobile.mobbl2.client.core.configuration.mvc;

import com.itude.mobile.mobbl2.client.core.configuration.MBDefinition;
import com.itude.mobile.mobbl2.client.core.util.StringUtilities;

public class MBVariableDefinition extends MBDefinition
{
  private String _expression;

  public String asXmlWithLevel(int level)
  {
    String result = StringUtilities.getIndentStringWithLevel(level)+"<Variable name='"+getName()+"' expression='"+_expression+"'/>\n";
    return result;
  }

  public String getExpression()
  {
    return _expression;
  }

  public void setExpression(String expression)
  {
    _expression = expression;
  }

}