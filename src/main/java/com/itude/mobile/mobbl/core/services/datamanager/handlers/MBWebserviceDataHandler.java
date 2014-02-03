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
package com.itude.mobile.mobbl.core.services.datamanager.handlers;

import com.itude.mobile.mobbl.core.configuration.webservices.MBEndPointDefinition;
import com.itude.mobile.mobbl.core.configuration.webservices.MBWebservicesConfiguration;
import com.itude.mobile.mobbl.core.configuration.webservices.MBWebservicesConfigurationParser;
import com.itude.mobile.mobbl.core.model.MBDocument;
import com.itude.mobile.mobbl.core.services.MBResourceService;
import com.itude.mobile.mobbl.core.services.datamanager.MBDataHandlerBase;
import com.itude.mobile.mobbl.core.util.MBCacheManager;
import com.itude.mobile.web.exceptions.ItudeRuntimeException;

/** 
 * Retrieves and sends MBDocument instances to and from a webservice.
 * <br/>
 * The MBWebserviceDataHandler is the top level in the DataHandlers for HTTP network communication.
 * Default behavior is to process an MBDocument, add the result to the request body and perform an HTTP operation (POST/GET).
 * 
 * The endpoints.xml file maps Document names to Webservice URL's together with caching and timeout information. 
 * The response body is parsed and validated against the Document Definition.
 * 
 * The response can be handled by a ResultListener, also defined in the endpoints.xml file. Matching is by regex, so errors can be flexibly handled.
 * 
 * Override this class to influence behavior. There are a bunch of template methods for easily changing HTTP headers, HTTP method etc.
 * 
 * Caching is configurable and automatic. The cache key is based on the document name and arguments. For REST webservices the operation name is one of the arguments.
 */
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
