package com.itude.mobile.mobbl2.client.core.services.datamanager.handlers;

import com.itude.commons.exceptions.ItudeRuntimeException;
import com.itude.mobile.mobbl2.client.core.configuration.webservices.MBEndPointDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.webservices.MBWebservicesConfiguration;
import com.itude.mobile.mobbl2.client.core.configuration.webservices.MBWebservicesConfigurationParser;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.services.MBResourceService;
import com.itude.mobile.mobbl2.client.core.services.datamanager.MBDataHandlerBase;
import com.itude.mobile.mobbl2.client.core.util.MBCacheManager;

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
    if(endPoint == null)
    {
      throw new ItudeRuntimeException("No endpoint for document "+ documentName +" found");
    }
    boolean cacheable = endPoint.isCacheable();
    boolean globalCacheable = endPoint.isGlobalCacheable();
    String documentIdentifier = null;

    if (cacheable)
    {
      MBDocument result;
      if (doc == null)
      {
        documentIdentifier = documentName;
      }
      else
      {
        documentIdentifier = documentName + doc.getUniqueId();
      }
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
