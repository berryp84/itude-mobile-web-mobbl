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
package com.itude.mobile.mobbl.core.configuration;

import java.util.List;

import com.itude.mobile.mobbl.core.configuration.mvc.MBActionDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBAttributeDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBDialogDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBDocumentDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBDomainDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBDomainValidatorDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBElementDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBOutcomeDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBPageDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBVariableDefinition;
import com.itude.mobile.mobbl.core.configuration.webservices.MBEndPointDefinition;
import com.itude.mobile.mobbl.core.model.MBDocument;
import com.itude.mobile.mobbl.core.services.MBResultListenerDefinition;

/**
* Common superclass of configuration definitions.
*
* A MOBBL application is for a large part defined in XML configuration files. On startup,
* the framework parses these configuration files and creates MBDefinition objects for the
* configuration.
*/
public class MBDefinition
{
  private String _name;

  /** 
   * Get the value of the `name` property of the XML element in the configuration.
   *  
   * @return the definition's name
   */
  public String getName()
  {
    return _name;
  }

  /** 
   * Set the value of the `name` property of the XML element in the configuration.
   *  
   * @name the definition's name
   */
  public void setName(String name)
  {
    _name = name;
  }

  /**
   * Exporting to XML
   * 
   * @param name name 
   * @param attrValue attribute value
   * @return exported XML
   */
  public String getAttributeAsXml(String name, Object attrValue)
  {
    if (attrValue == null)
    {
      return "";
    }

    return " " + name + "='" + (String) attrValue + "'";
  }

  /**
   * Structured String representation
   * 
   * @param level level
   * @return structured String representation
   */
  public String asXmlWithLevel(int level)
  {
    return "";
  }

  /** Checks the validity of the configuration.
   *
   * This method is called after parsing a configuration item and should be implemented by subclasses
   * to check the validity of the configuration. The method is expected to throw an NSException when
   * the definition is invalid.
   *
   * The default implementation is empty.
   */
  public void validateDefinition()
  {
  }

  /** Checks the validity of any preconditions that are defined with this definition.
   *
   * Definitions that use this should implement MBConditionalDefinition. 
   * 
   * @return the default implementation always returns true.
   */
  public boolean isPreConditionValid(MBDocument document, String currentPath)
  {
    return true;
  }

  /**
   * Add {@link MBDomainValidatorDefinition} as child element
   * 
   * @param child {@link MBDomainValidatorDefinition}
   */
  public void addChildElement(MBDomainValidatorDefinition child)
  {
  }

  /**
   * Add {@link MBElementDefinition} as child element
   * 
   * @param child {@link MBElementDefinition}
   */
  public void addChildElement(MBElementDefinition child)
  {
  }

  /**
   * Add {@link MBAttributeDefinition} as child element
   * 
   * @param child {@link MBAttributeDefinition}
   */
  public void addChildElement(MBAttributeDefinition child)
  {
  }

  /**
   * Add {@link MBDefinition} as child element
   * 
   * @param child {@link MBDefinition}
   */
  public void addChildElement(MBDefinition definition)
  {
  }

  /**
   * Add {@link MBVariableDefinition} as child element
   * 
   * @param child {@link MBVariableDefinition}
   */
  public void addChildElement(MBVariableDefinition definition)
  {
  }

  /**
   * Add {@link MBDomainDefinition} as child element
   * 
   * @param child {@link MBDomainDefinition}
   */
  public void addChildElement(MBDomainDefinition child)
  {
  }

  /**
   * Add {@link MBDocumentDefinition} as child element
   * 
   * @param child {@link MBDocumentDefinition}
   */
  public void addChildElement(MBDocumentDefinition child)
  {
  }

  /**
   * Add {@link MBActionDefinition} as child element
   * 
   * @param child {@link MBActionDefinition}
   */
  public void addChildElement(MBActionDefinition child)
  {
  }

  /**
   * Add {@link MBOutcomeDefinition} as child element
   * 
   * @param child {@link MBOutcomeDefinition}
   */
  public void addChildElement(MBOutcomeDefinition child)
  {
  }

  /**
   * Add {@link MBPageDefinition} as child element
   * 
   * @param child {@link MBPageDefinition}
   */
  public void addChildElement(MBPageDefinition child)
  {
  }

  /**
   * Add {@link MBDialogDefinition} as child element
   * 
   * @param child {@link MBDialogDefinition}
   */
  public void addChildElement(MBDialogDefinition child)
  {
  }

  /**
   * Add {@link MBEndPointDefinition} as child element
   * 
   * @param child {@link MBEndPointDefinition}
   */
  public void addEndPoint(MBEndPointDefinition definition)
  {
  }

  /**
   * Add {@link MBResultListenerDefinition} as child element
   * 
   * @param child {@link MBResultListenerDefinition}
   */
  public void addResultListener(MBResultListenerDefinition lsnr)
  {
  }

  /**
   * Check if Child with elementName is a valid child
   * @param elementName element name
   * @return the default implementation always returns false.
   */
  public boolean isValidChild(String elementName)
  {
    return false;
  }

  /**
   * Get a {@link List} of {@link MBElementDefinition}s
   * @return {@link List} of {@link MBElementDefinition}s
   */
  public List<MBElementDefinition> getChildElements()
  {
    return null;
  }

  @Override
  public String toString()
  {
    return asXmlWithLevel(0);
  }
}
