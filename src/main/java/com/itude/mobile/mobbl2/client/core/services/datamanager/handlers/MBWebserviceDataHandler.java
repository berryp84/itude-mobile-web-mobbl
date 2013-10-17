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
package com.itude.mobile.mobbl2.client.core.services.datamanager.handlers;

import com.itude.mobile.mobbl2.client.core.configuration.webservices.MBEndPointDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.webservices.MBWebservicesConfiguration;
import com.itude.mobile.mobbl2.client.core.configuration.webservices.MBWebservicesConfigurationParser;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.services.MBResourceService;
import com.itude.mobile.mobbl2.client.core.services.datamanager.MBDataHandlerBase;
import com.itude.mobile.mobbl2.client.core.util.MBCacheManager;
import com.itude.mobile.web.exceptions.ItudeRuntimeException;

public abstract class MBWebserviceDataHandler extends MBDataHandlerBase
{
  private final MBWebservicesConfiguration _webServiceConfiguration;
  private static final String              ENDPOINTS_NAME = "endpoints";

  public MBWebserviceDataHandler()
  {
    MBWebservicesConfigurationParser parser = new MBWebservicesConfigurationParser();
    byte[] data = MBResourceService.getInstance().getResourceByID(ENDPOINTS_NAME);
    _webServiceConfiguration = (MBWebservicesConfiguration) parser.parseData(data, ENDPOINTS_NAME);
  }

  @Override
  public MBDocument loadDocument(String documentName)
  {
    return loadDocument(documentName, null);
  }

  @Override
  public MBDocument loadDocument(String documentName, MBDocument doc)
  {
    MBEndPointDefinition endPoint = getEndPointForDocument(documentName);
    if (endPoint == null)
    {
      throw new ItudeRuntimeException("No endpoint for document " + documentName + " found");
    }
    boolean cacheable = endPoint.isCacheable();
    boolean globalCacheable = endPoint.isGlobalCacheable();
    String documentIdentifier = null;

    if (cacheable)
    {
      MBDocument result;

      documentIdentifier = doc == null ? documentName : documentName + doc.getUniqueId();
      result = MBCacheManager.documentForKey(documentIdentifier, globalCacheable);

      if (result != null)
      {
        return result;
      }
    }

    MBDocument result = doLoadDocument(documentName, doc);

    if (cacheable)
    {
      if (doc == null)
      {
        MBCacheManager.setDocument(result, documentIdentifier, endPoint.getTtl(), globalCacheable);
      }
      else
      {
        MBCacheManager.setDocument(result, documentIdentifier, endPoint.getTtl(), globalCacheable);
      }
    }

    return result;
  }

  protected abstract MBDocument doLoadDocument(String documentName, MBDocument doc);

  @Override
  public void storeDocument(MBDocument document)
  {
  }

  public MBEndPointDefinition getEndPointForDocument(String name)
  {
    return _webServiceConfiguration.getEndPointForDocumentName(name);
  }

}
