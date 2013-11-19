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
package com.itude.mobile.mobbl2.client.core.configuration.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.itude.mobile.mobbl2.client.core.configuration.MBConfigurationParser;
import com.itude.mobile.mobbl2.client.core.configuration.MBDefinition;
import com.itude.mobile.mobbl2.client.core.util.MBBundleDefinition;

public class MBResourceConfigurationParser extends MBConfigurationParser
{
  private List<String> _resourceAttributes;
  private List<String> _bundleAttributes;

  @Override
  public MBDefinition parseData(byte[] data, String documentName)
  {
    if (_resourceAttributes == null)
    {
      _resourceAttributes = new ArrayList<String>();
      _resourceAttributes.add("xmlns");
      _resourceAttributes.add("id");
      _resourceAttributes.add("url");
      _resourceAttributes.add("cacheable");
      _resourceAttributes.add("ttl");
    }
    if (_bundleAttributes == null)
    {
      _bundleAttributes = new ArrayList<String>();
      _bundleAttributes.add("xmlns");
      _bundleAttributes.add("languageCode");
      _bundleAttributes.add("url");
    }

    return super.parseData(data, documentName);
  }

  public List<String> getResourceAttributes()
  {
    return _resourceAttributes;
  }

  public void setResourceAttributes(List<String> resourceAttributes)
  {
    _resourceAttributes = resourceAttributes;
  }

  public List<String> getBundleAttributes()
  {
    return _bundleAttributes;
  }

  public void setBundleAttributes(List<String> bundleAttributes)
  {
    _bundleAttributes = bundleAttributes;
  }

  @Override
  public boolean processElement(String elementName, Map<String, String> attributeDict)
  {
    if (elementName.equals("Resources"))
    {
      MBResourceConfiguration confDef = new MBResourceConfiguration();
      getStack().push(confDef);
    }
    else if (elementName.equals("Resource"))
    {
      checkAttributesForElement(elementName, attributeDict, _resourceAttributes);

      MBResourceDefinition resourceDef = new MBResourceDefinition();
      resourceDef.setResourceId(attributeDict.get("id"));
      resourceDef.setUrl(attributeDict.get("url"));
      resourceDef.setCacheable(Boolean.parseBoolean(attributeDict.get("cacheable")));
      if (attributeDict.containsKey("ttl"))
      {
        resourceDef.setTtl(Integer.parseInt(attributeDict.get("ttl")));
      }

      ((MBResourceConfiguration) getStack().peek()).addResource(resourceDef);
      getStack().add(resourceDef);
    }
    else if (elementName.equals("Bundle"))
    {
      checkAttributesForElement(elementName, attributeDict, _bundleAttributes);

      MBBundleDefinition bundleDef = new MBBundleDefinition();
      bundleDef.setUrl(attributeDict.get("url"));
      bundleDef.setLanguageCode(attributeDict.get("languageCode"));

      ((MBResourceConfiguration) getStack().peek()).addBundle(bundleDef);
      getStack().push(bundleDef);
    }
    else
    {
      return false;
    }

    return true;
  }

  @Override
  public void didProcessElement(String elementName)
  {
    if (!elementName.equals("Resources"))
    {
      getStack().pop();
    }
  }

  @Override
  public boolean isConcreteElement(String element)
  {
    return element.equals("Resource") || element.equals("Bundle") || element.equals("Resources");
  }

  @Override
  public boolean isIgnoredElement(String element)
  {
    return false;
  }

}
