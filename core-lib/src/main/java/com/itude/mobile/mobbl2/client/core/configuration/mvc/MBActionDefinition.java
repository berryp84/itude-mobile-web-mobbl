package com.itude.mobile.mobbl2.client.core.configuration.mvc;

import com.itude.mobile.mobbl2.client.core.configuration.MBDefinition;
import com.itude.mobile.mobbl2.client.core.util.StringUtilities;

public class MBActionDefinition extends MBDefinition
{
  private String _className;

  public String asXmlWithLevel(int level)
  {
    String result = StringUtilities.getIndentStringWithLevel(level)+"<Action name='"+getName()+"' className='"+getClassName()+"'/>\n";

    return result;
  }

  public String getClassName()
  {
    return _className;
  }

  public void setClassName(String className)
  {
    _className = className;
  }

}
