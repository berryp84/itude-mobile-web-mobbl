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
package com.itude.mobile.mobbl.core.configuration.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itude.mobile.mobbl.core.configuration.MBDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.exceptions.MBEmptyPathException;
import com.itude.mobile.mobbl.core.configuration.mvc.exceptions.MBInvalidElementNameException;
import com.itude.mobile.mobbl.core.configuration.mvc.exceptions.MBInvalidPathException;
import com.itude.mobile.mobbl.core.configuration.mvc.exceptions.MBUnknownVariableException;
import com.itude.mobile.mobbl.core.model.MBDocument;
import com.itude.mobile.mobbl.core.services.MBMetadataService;
import com.itude.mobile.mobbl.core.util.StringUtilities;

public class MBDocumentDefinition extends MBDefinition
{
  private final List<MBElementDefinition>        _elementsSorted;
  private final Map<String, MBElementDefinition> _elements;
  private String                                 _dataManager;
  private boolean                                _autoCreate;
  private String                                 _rootElement;

  public MBDocumentDefinition()
  {
    _elements = new HashMap<String, MBElementDefinition>();
    _elementsSorted = new ArrayList<MBElementDefinition>();
  }

  @Override
  public String toString()
  {
    return asXmlWithLevel(0);
  }

  @Override
  public String asXmlWithLevel(int level)
  {
    String result = StringUtilities.getIndentStringWithLevel(level) + "<Document name='" + getName() + "' dataManager='" + _dataManager
                    + "' autoCreate='" + _autoCreate + "'>\n";
    for (MBElementDefinition elem : _elementsSorted)
      result = result + elem.asXmlWithLevel(level + 2);
    result = result + StringUtilities.getIndentStringWithLevel(level) + "</Document>\n";

    return result;
  }

  @Override
  public void addChildElement(MBElementDefinition child)
  {
    if (child instanceof MBElementDefinition)
    {
      addElement(child);
    }

  }

  @Override
  public List<MBElementDefinition> getChildElements()
  {
    return _elementsSorted;
  }

  public void addElement(MBElementDefinition element)
  {
    _elementsSorted.add(element);
    _elements.put(element.getName(), element);
  }

  @Override
  public boolean isValidChild(String elementName)
  {
    return _elements.get(elementName) != null;
  }

  public List<MBElementDefinition> getChildren()
  {
    return _elementsSorted;
  }

  public MBElementDefinition getChildWithName(String elementName)
  {
    if (!isValidChild(elementName))
    {
      throw new MBInvalidElementNameException(elementName);
    }
    return _elements.get(elementName);
  }

  public MBElementDefinition getElementWithPathComponents(List<String> pathComponents)
  {
    if (pathComponents.size() > 0)
    {
      MBElementDefinition root = getChildWithName(pathComponents.get(0));
      pathComponents.remove(0);
      return root.getElementWithPathComponents(pathComponents);
    }
    throw new MBEmptyPathException("No path specified");
  }

  public MBElementDefinition getElementWithPath(String path)
  {
    List<String> pathComponents = StringUtilities.splitPath(path);

    // If there is a ':' in the name of the first component; we might need a different document than 'self'
    if (pathComponents.size() > 0)
    {
      int location = path.indexOf(':');

      if (location > -1)
      {
        String documentName = pathComponents.get(0).substring(0, location);
        String rootElementName = pathComponents.get(0).substring(location + 1);

        if (!documentName.equals(getName()))
        {
          // Different document! Dispatch the valueForPath
          MBDocumentDefinition docDef = MBMetadataService.getInstance().getDefinitionForDocumentName(documentName);
          if (rootElementName.length() > 0)
          {
            pathComponents.set(0, rootElementName);
          }
          else
          {
            pathComponents.remove(0);
          }

          return docDef.getElementWithPathComponents(pathComponents);
        }
      }
    }

    return getElementWithPathComponents(pathComponents);
  }

  public MBAttributeDefinition getAttributeWithPath(String path)
  {
    int location = path.indexOf('@');

    if (location == -1)
    {
      throw new MBInvalidPathException(path);
    }
    String elementPath = path.substring(0, location);
    String attrName = path.substring(location + 1);

    MBElementDefinition elemDef = getElementWithPath(elementPath);
    return elemDef.getAttributeWithName(attrName);
  }

  public String getChildElementNames()
  {
    StringBuilder result = new StringBuilder();
    for (MBElementDefinition ed : _elementsSorted)
    {
      if (result.length() > 0)
      {
        result.append(", ");
      }
      result.append(ed.getName());
    }

    if (result.length() == 0)
    {
      return "[none]";
    }

    return result.toString();
  }

  public MBDocument createDocument()
  {
    MBDocument doc = new MBDocument(this);

    for (MBElementDefinition elementDef : _elementsSorted)
    {
      for (int i = 0; i < elementDef.getMinOccurs(); i++)
      {
        doc.addElement(elementDef.createElement());
      }
    }

    return doc;
  }

  public String evaluateExpression(String variableName)
  {
    throw new MBUnknownVariableException("Unknown variable: " + variableName);
  }

  public String getDataManager()
  {
    return _dataManager;
  }

  public void setDataManager(String dataManager)
  {
    _dataManager = dataManager;
  }

  public boolean getAutoCreate()
  {
    return _autoCreate;
  }

  public void setAutoCreate(boolean autoCreate)
  {
    _autoCreate = autoCreate;
  }

  public void setRootElement(String rootElement)
  {
    _rootElement = rootElement;
  }

  public String getRootElement()
  {
    return _rootElement;
  }

}
