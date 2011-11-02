package com.itude.mobile.mobbl2.client.core.view;

import com.itude.mobile.mobbl2.client.core.configuration.MBDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBForEachDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBVariableDefinition;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.util.StringUtilities;

public class MBRow extends MBComponentContainer
{
  private int _index;

  public MBRow(MBDefinition definition, MBDocument document, MBComponentContainer parent)
  {
    super(definition, document, parent);
  }

  public int getIndex()
  {
    return _index;
  }

  public void setIndex(int index)
  {
    _index = index;
  }

  @Override
  public String getComponentDataPath()
  {
    MBForEachDefinition def = (MBForEachDefinition) getDefinition();
    String path = def.getValue() + "[" + _index + "]";
    return substituteExpressions(path);
  }

  @Override
  public String evaluateExpression(String variableName)
  {
    MBForEachDefinition eachDef = (MBForEachDefinition) getParent().getDefinition();
    MBVariableDefinition varDef = eachDef.variable(variableName);
    if (varDef == null) return getParent().evaluateExpression(variableName);

    if ("currentPath()".equals(varDef.getExpression()) || "currentpath()".equals(varDef.getExpression())) return getAbsoluteDataPath();
    if ("rootPath()".equals(varDef.getExpression()) || "rootpath()".equals(varDef.getExpression())) 
      return getPage().getRootPath();

    String value;
    if (varDef.getExpression().startsWith("/") || varDef.getExpression().indexOf(":") > -1)
    {
      value = varDef.getExpression();
    }
    else
    {
      String componentPath = substituteExpressions(getComponentDataPath());
      value = componentPath + "/" + varDef.getExpression();
    }
    return (String) getDocument().getValueForPath(value);
  }

  @Override
  public String asXmlWithLevel(int level)
  {
    String result = StringUtilities.getIndentStringWithLevel(level) + "<MBRow index=" + _index + ">\n";
    result = result + childrenAsXmlWithLevel(level + 2);
    result = result + StringUtilities.getIndentStringWithLevel(level) + "</MBRow>\n";
    return result;
  }

  @Override
  public String toString()
  {
    return asXmlWithLevel(0);
  }

}
