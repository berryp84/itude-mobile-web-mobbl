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
package com.itude.mobile.mobbl2.client.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBAttributeDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBElementDefinition;
import com.itude.mobile.mobbl2.client.core.model.exceptions.MBCannotAssignException;
import com.itude.mobile.mobbl2.client.core.model.exceptions.MBInvalidAttributeNameException;
import com.itude.mobile.mobbl2.client.core.util.StringUtilities;

public class MBElement extends MBElementContainer

{
  private static final Logger       LOGGER         = Logger.getLogger(MBElement.class);
  private static final String       TEXT_ATTRIBUTE = "text()";

  private final Map<String, String> _values;                                           // Dictionaryofstrings
  private MBElementDefinition       _definition;

  public MBElement(MBElementDefinition definition)
  {
    super();
    _definition = definition;
    _values = new HashMap<String, String>();
  }

  @Override
  public MBElement clone()
  {
    MBElement newElement = new MBElement(_definition);
    newElement._values.putAll(_values);
    copyChildrenInto(newElement);

    return newElement;
  }

  @Override
  public void setValue(String value, String path)
  {
    if (path.startsWith("@"))
    {
      setValue(value, path.substring(1));
    }
    else
    {
      if (!path.startsWith("./@"))
      {
        LOGGER.debug("Path \"" + path + "\" didn't start wih @ or ./@. Perhaps setAttributeValue should have been called instead.");
      }
      super.setValue(value, path);
    }
  }

  public void setAttributeValue(String value, String attributeName)
  {
    setAttributeValue(value, attributeName, true);
  }

  public void setAttributeValue(String value, String attributeName, boolean throwIfInvalid)
  {
    if (throwIfInvalid)
    {
      validateAttribute(attributeName);
      _values.put(attributeName, value);
    }
    else
    {
      if (isValidAttribute(attributeName))
      {
        _values.put(attributeName, value);
      }
    }

  }

  public String getValueForAttribute(String attributeName)
  {
    validateAttribute(attributeName);
    return _values.get(attributeName);
  }

  public String getValueForKey(String key)
  {
    return getValueForAttribute(key);
  }

  public String asXmlWithLevel(int level)
  {
    boolean hasBodyText = (isValidAttribute(TEXT_ATTRIBUTE) && getBodyText() != null && getBodyText().length() > 0);
    String result = StringUtilities.getIndentStringWithLevel(level) + "<" + _definition.getName();
    for (MBAttributeDefinition def : _definition.getAttributes())
    {
      String attrName = def.getName();
      String attrValue = _values.get(attrName);
      if (!attrName.equals(TEXT_ATTRIBUTE))
      {
        result += attributeAsXml(attrName, attrValue);
      }
    }

    if (_definition.getChildren().size() == 0 && !hasBodyText)
    {
      result += "/>\n";
    }
    else
    {
      result += ">";
      if (hasBodyText)
      {
        result += getBodyText();
      }
      else
      {
        result += "\n";
      }

      for (MBElementDefinition elemDef : _definition.getChildren())
      {
        List<MBElement> lst = getElements().get(elemDef.getName());
        if (lst != null)
        {
          for (MBElement elem : lst)
          {
            result += elem.asXmlWithLevel(level + 2);
          }
        }
      }

      int closingLevel = 0;
      if (!hasBodyText)
      {
        closingLevel = level;
      }
      result += StringUtilities.getIndentStringWithLevel(closingLevel) + "</" + _definition.getName() + ">\n";
    }

    return result;
  }

  @Override
  public MBElementDefinition getDefinition()
  {
    return _definition;
  }

  public void setDefinition(MBElementDefinition definition)
  {
    _definition = definition;
  }

  public boolean isValidAttribute(String attributeName)
  {
    return (getDefinition()).isValidAttribute(attributeName);
  }

  private void validateAttribute(String attributeName)
  {
    if (!isValidAttribute(attributeName))
    {
      String message = "Attribute \"" + attributeName + "\" not defined for element with name \"" + getDefinition().getName()
                       + "\". Use one of \"" + getDefinition().getAttributeNames() + "\"";

      throw new MBInvalidAttributeNameException(message);
    }
  }

  public String getBodyText()
  {
    if (isValidAttribute(TEXT_ATTRIBUTE))
    {
      return getValueForAttribute(TEXT_ATTRIBUTE);
    }
    return null;
  }

  public void setBodyText(String text)
  {
    setAttributeValue(text, TEXT_ATTRIBUTE);
  }

  public void assignToElement(MBElement target)
  {
    if (!target.getDefinition().getName().equals(_definition.getName()))
    {
      String message = "Cannot assign element since types differ: " + target.getDefinition().getName() + " != " + _definition.getName()
                       + " (use assignByName:)";
      throw new MBCannotAssignException(message);
    }

    target._values.clear();
    target._values.putAll(_values);
    target.getElements().clear();
    copyChildrenInto(target);
  }

  @Override
  public String getUniqueId()
  {
    String uid = "";
    uid += getDefinition().getName();
    for (MBAttributeDefinition def : _definition.getAttributes())
    {
      String attrName = def.getName();
      if (!attrName.equals("xmlns"))
      {
        String attrValue = _values.get(attrName);
        uid += "_";
        if (attrValue != null)
        {
          uid += cookValue(attrValue);
        }
      }
    }
    uid += super.getUniqueId();

    return uid;
  }

  public void addallPathsTo(Set<String> set, String currentPath)
  {
    //    String elementName = getDefinition().getName();

    for (String attribute : _values.keySet())
    {
      set.add(currentPath + "/@" + attribute);
    }

    super.addAllPathsTo(set, currentPath);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getValueForPathComponents(List<String> pathComponents, String originalPath, boolean nillIfMissing,
                                         List<String> translatedPathComponents)
  {
    if (pathComponents.size() > 0 && pathComponents.get(0).startsWith("@"))
    {
      String attrName = pathComponents.get(0);
      if (translatedPathComponents == null)
      {
        translatedPathComponents = new ArrayList<String>();
      }
      translatedPathComponents.add(attrName);

      return (T) getValueForAttribute(attrName.substring(1));
    }
    else
    {
      return (T) super.getValueForPathComponents(pathComponents, originalPath, nillIfMissing, translatedPathComponents);
    }
  }

  public String cookValue(String uncooked)
  {
    if (uncooked == null)
    {
      return null;
    }

    String cooked = "";
    for (int i = 0; i < uncooked.length(); i++)
    {
      char c = uncooked.charAt(i);
      if (c < 32 || c == '&' || c == '\'' || c > 126)
      {
        cooked += "&#" + (int) c + ";";
      }
      else
      {
        cooked += c;
      }
    }

    return cooked;
  }

  public String attributeAsXml(String name, Object attrValue)
  {
    attrValue = cookValue((String) attrValue);

    if (attrValue == null)
    {
      return "";
    }

    return " " + name + "='" + attrValue + "'";
  }

  public void assignByName(MBElementContainer other)
  {
    other.deleteAllChildElements();

    MBElementDefinition def = getDefinition();
    for (MBAttributeDefinition attrDef : def.getAttributes())
    {
      if (((MBElementDefinition) other.getDefinition()).isValidAttribute(attrDef.getName()))
      {
        other.setValue(getValueForAttribute(attrDef.getName()), attrDef.getName());
      }
    }

    for (String elementName : getElements().keySet())
    {
      for (MBElement src : getElements().get(elementName))
      {
        MBElement newElem = other.createElementWithName(src.getDefinition().getName());
        src.assignByName(newElem);
      }
    }

  }

  @Override
  public String getName()
  {
    return getDefinition().getName();
  }

  @Override
  public String toString()
  {
    return asXmlWithLevel(0);
  }

}
