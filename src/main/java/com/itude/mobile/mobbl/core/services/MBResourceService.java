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
package com.itude.mobile.mobbl.core.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itude.mobile.mobbl.core.configuration.mvc.MBConfigurationDefinition;
import com.itude.mobile.mobbl.core.configuration.resources.MBResourceConfiguration;
import com.itude.mobile.mobbl.core.configuration.resources.MBResourceConfigurationParser;
import com.itude.mobile.mobbl.core.configuration.resources.MBResourceDefinition;
import com.itude.mobile.mobbl.core.model.MBDocument;
import com.itude.mobile.mobbl.core.model.MBDocumentFactory;
import com.itude.mobile.mobbl.core.model.MBElement;
import com.itude.mobile.mobbl.core.services.exceptions.MBBundleNotFoundException;
import com.itude.mobile.mobbl.core.services.exceptions.MBResourceNotDefinedException;
import com.itude.mobile.mobbl.core.util.DataUtil;
import com.itude.mobile.mobbl.core.util.MBBundleDefinition;
import com.itude.mobile.mobbl.core.util.MobblEnvironment;

/** 
 * Service for accessing resources over the network or on the file system.
 * retrieves images or files and caches them.
 */
public class MBResourceService
{
  private static final Logger      LOGGER                    = Logger.getLogger(MBResourceService.class);

  public static final String       RESOURCE_CONFIG_FILE_NAME = MobblEnvironment.getResourcesFile();

  private MBResourceConfiguration  _config;

  private static MBResourceService _instance;

  public static MBResourceService getInstance()
  {
    if (_instance == null)
    {
      _instance = new MBResourceService();
      MBResourceConfigurationParser parser = new MBResourceConfigurationParser();

      byte[] data = DataUtil.getInstance().readResource(RESOURCE_CONFIG_FILE_NAME);
      _instance.setConfig((MBResourceConfiguration) parser.parseData(data, RESOURCE_CONFIG_FILE_NAME));

    }

    return _instance;
  }

  public MBResourceConfiguration getConfig()
  {
    return _config;
  }

  public void setConfig(MBResourceConfiguration config)
  {
    _config = config;
  }

  public byte[] getResourceByID(String resourceId)
  {
    MBResourceDefinition def = getConfig().getResourceWithID(resourceId);
    if (def == null) throw new MBResourceNotDefinedException("Resource for ID=" + resourceId + " could not be found");

    return getResourceByURL(def.getUrl(), def.getCacheable(), def.getTtl());
  }

  public byte[] getResourceByURL(String urlString)
  {
    return getResourceByURL(urlString, false, 0);
  }

  public byte[] getResourceByURL(String urlString, boolean cacheable, int ttl)
  {
    if (urlString.startsWith("resource:"))
    {
      String fileName = urlString.substring(9);
      byte[] data;

      try
      {
        data = DataUtil.getInstance().readResource(fileName);

        if (data == null)
        {
          LOGGER.warn("Warning: could not load file=" + fileName + " based on URL=" + urlString);
        }

        return data;
      }
      catch (Exception e)
      {
        LOGGER.warn("Warning: could not load file=" + fileName + " based on URL=" + urlString);
      }
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, String>> getBundlesForLanguageCode(String languageCode)
  {
    List<Map<String, String>> result = new ArrayList<Map<String, String>>();

    List<MBBundleDefinition> bundleDefs = _config.getBundlesForLanguageCode(languageCode);
    if (bundleDefs == null)
    {
      String message = "No bundles defined for language with code " + languageCode;
      throw new MBBundleNotFoundException(message);
    }

    for (MBBundleDefinition def : bundleDefs)
    {
      byte[] data = getResourceByURL(def.getUrl());

      if (data == null)
      {
        String message = "Bundle with url " + def.getUrl() + " could not be loaded";
        throw new MBBundleNotFoundException(message);
      }

      MBDocument bundleDoc = MBDocumentFactory.getInstance()
          .getDocumentWithData(data, MBDocumentFactory.PARSER_XML,
                               MBMetadataService.getInstance().getDefinitionForDocumentName(MBConfigurationDefinition.DOC_SYSTEM_LANGUAGE));
      Map<String, String> dict = new HashMap<String, String>();
      for (MBElement text : (List<MBElement>) bundleDoc.getValueForPath("/Text"))
      {
        dict.put(text.getValueForAttribute("key"), text.getValueForAttribute("value"));
      }

      result.add(dict);

    }

    return result;
  }
}
