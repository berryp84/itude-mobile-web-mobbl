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
package com.itude.mobile.mobbl.core.view;

import com.itude.mobile.mobbl.core.configuration.MBDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBForEachDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBVariableDefinition;
import com.itude.mobile.mobbl.core.model.MBDocument;
import com.itude.mobile.mobbl.core.util.StringUtilities;

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
    if ("rootPath()".equals(varDef.getExpression()) || "rootpath()".equals(varDef.getExpression())) return getPage().getRootPath();

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
