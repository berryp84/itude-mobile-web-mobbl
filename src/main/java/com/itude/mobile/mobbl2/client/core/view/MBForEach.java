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
package com.itude.mobile.mobbl2.client.core.view;

import java.util.ArrayList;
import java.util.List;

import com.itude.mobile.mobbl2.client.core.configuration.MBDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBForEachDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBVariableDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.exceptions.MBInvalidPathException;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.util.StringUtilities;

public class MBForEach extends MBComponentContainer
{
  private ArrayList<MBRow> _rows; // arrayofMBRows
  private String           _value;

  public MBForEach(MBForEachDefinition definition, MBDocument document, MBComponentContainer parent)
  {
    super(definition, document, parent);

    setValue(definition.getValue());

    _rows = new ArrayList<MBRow>();

    MBForEachDefinition def = (MBForEachDefinition) getDefinition();
    if (!def.isPreConditionValid(document, parent.getAbsoluteDataPath()))
    {
      // Our precondition is not true; so we must not exist:
      setMarkedForDestruction(true);
    }
    else
    {
      String fullPath = _value;
      if (!fullPath.startsWith("/") && fullPath.indexOf(':') == -1)
      {
        fullPath = parent.getAbsoluteDataPath() + "/" + _value;
      }

      Object pathResult = document.getValueForPath(fullPath);
      if (pathResult != null)
      {
        // TODO: this is Binck only. Should be removed from this project
        int rowAmount = "/EXT-SchermenHomeResult[0]/Topstijgersdalers[0]/Stijgers[0]/TopStijgersDalersTypeStijgersStijger"
            .equals(definition.getValue()) ? 3 : ((List<?>) pathResult).size();
        rowAmount = "/EXT-SchermenHomeResult[0]/Topstijgersdalers[0]/Dalers[0]/TopStijgersDalersTypeDalersDaler".equals(definition
            .getValue()) ? 3 : rowAmount;

        if (!(pathResult instanceof List<?>)) throw new MBInvalidPathException(_value);
        for (int i = 0; i < rowAmount; i++)
        {

          MBRow row = new MBRow(getDefinition(), getDocument(), this);
          addRow(row);

          for (MBDefinition childDef : (ArrayList<MBDefinition>) def.getChildren())
          {
            if (childDef.isPreConditionValid(document, row.getAbsoluteDataPath()))
            {
              row.addChild(MBComponentFactory.getComponentFromDefinition(childDef, document, row));
            }

          }
        }
        if (definition.getSuppressRowComponent())
        {
          // Prune the rows and ourselves
          for (MBRow row : _rows)
          {
            for (MBComponent child : row.getChildren())
            {
              child.translatePath();
              getParent().addChild(child);
            }
          }
          _rows.clear();
          // Now mark ourself for destruction so we will not be added to the child array of our parent.
          setMarkedForDestruction(true);
        }
      }
    }

  }

  public ArrayList<MBRow> getRows()
  {
    return _rows;
  }

  public void setRows(ArrayList<MBRow> rows)
  {
    _rows = rows;
  }

  public String getValue()
  {
    return _value;
  }

  public void setValue(String value)
  {
    _value = value;
  }

  public void addRow(MBRow row)
  {
    row.setParent(this);
    row.setIndex(_rows.size());
    _rows.add(row);
  }

  //This method is overridden because we (may) have to the children of the rows too
  @SuppressWarnings("unchecked")
  @Override
  public <T extends MBComponent> List<T> getDescendantsOfKind(Class<T> clazz)
  {

    List<T> result = super.getDescendantsOfKind(clazz);
    for (MBRow child : _rows)
    {

      if (clazz.isInstance(child)) result.add((T) child);
      result.addAll(child.getDescendantsOfKind(clazz));
    }
    return result;
  }

  //This method is overridden because we (may) have to the children of the rows too
  @SuppressWarnings("unchecked")
  @Override
  public <T extends MBComponent> List<T> getChildrenOfKind(Class<T> clazz)
  {
    List<T> result = super.getChildrenOfKind(clazz);
    for (MBComponent child : _rows)
    {
      if (clazz.isInstance(child)) result.add((T) child);
    }
    return result;
  }

  @Override
  public String asXmlWithLevel(int level)
  {
    String result = StringUtilities.getIndentStringWithLevel(level) + "<MBForEach " + this.attributeAsXml("value", _value) + ">\n";

    MBForEachDefinition def = (MBForEachDefinition) getDefinition();
    for (MBVariableDefinition var : def.getVariables().values())
      result = result + var.asXmlWithLevel(level + 2);
    for (MBRow child : _rows)
      result = result + child.asXmlWithLevel(level + 2);

    result = result + childrenAsXmlWithLevel(level + 2);
    result = result + StringUtilities.getIndentStringWithLevel(level) + "</MBForEach>\n";

    return result;
  }

  @Override
  public String toString()
  {
    return asXmlWithLevel(0);
  }

}