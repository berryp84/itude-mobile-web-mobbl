package com.itude.mobile.mobbl2.client.core.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBConfigurationDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.resources.MBResourceConfiguration;
import com.itude.mobile.mobbl2.client.core.configuration.resources.MBResourceConfigurationParser;
import com.itude.mobile.mobbl2.client.core.configuration.resources.MBResourceDefinition;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.model.MBDocumentFactory;
import com.itude.mobile.mobbl2.client.core.model.MBElement;
import com.itude.mobile.mobbl2.client.core.services.exceptions.MBBundleNotFoundException;
import com.itude.mobile.mobbl2.client.core.services.exceptions.MBResourceNotDefinedException;
import com.itude.mobile.mobbl2.client.core.util.DataUtil;
import com.itude.mobile.mobbl2.client.core.util.MBBundleDefinition;
import com.itude.mobile.mobbl2.client.core.util.MobblEnvironment;

public class MBResourceService
{
  private static final Logger      LOGGER                      = Logger.getLogger(MBResourceService.class);

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
