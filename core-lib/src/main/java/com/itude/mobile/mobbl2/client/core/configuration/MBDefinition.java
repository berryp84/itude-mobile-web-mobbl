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
package com.itude.mobile.mobbl2.client.core.configuration;

import java.util.List;

import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBActionDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBAttributeDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBDialogDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBDocumentDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBDomainDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBDomainValidatorDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBElementDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBOutcomeDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBPageDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBVariableDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.webservices.MBEndPointDefinition;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.services.MBResultListenerDefinition;

public class MBDefinition
{
  private String _name;

  public String getName()
  {
    return _name;
  }

  public void setName(String name)
  {
    _name = name;
  }

  public String getAttributeAsXml(String name, Object attrValue)
  {
    if (attrValue == null)
    {
      return "";
    }

    return " " + name + "='" + (String) attrValue + "'";
  }

  public String asXmlWithLevel(int level)
  {
    return "";
  }

  public void validateDefinition()
  {
  }

  public boolean isPreConditionValid(MBDocument document, String currentPath)
  {
    return true;
  }

  public void addChildElement(MBDomainValidatorDefinition child)
  {
  }

  public void addChildElement(MBElementDefinition child)
  {
  }

  public void addChildElement(MBAttributeDefinition child)
  {
  }

  public void addChildElement(MBDefinition definition)
  {
  }

  public void addChildElement(MBVariableDefinition definition)
  {
  }

  public void addChildElement(MBDomainDefinition child)
  {
  }

  public void addChildElement(MBDocumentDefinition child)
  {
  }

  public void addChildElement(MBActionDefinition child)
  {
  }

  public void addChildElement(MBOutcomeDefinition child)
  {
  }

  public void addChildElement(MBPageDefinition child)
  {
  }

  public void addChildElement(MBDialogDefinition child)
  {
  }

  public void addEndPoint(MBEndPointDefinition definition)
  {
  }

  public void addResultListener(MBResultListenerDefinition lsnr)
  {
  }

  public boolean isValidChild(String elementName)
  {
    return false;
  }

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
