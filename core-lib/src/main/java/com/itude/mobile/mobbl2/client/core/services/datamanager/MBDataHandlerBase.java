package com.itude.mobile.mobbl2.client.core.services.datamanager;

import org.apache.log4j.Logger;

import com.itude.mobile.mobbl2.client.core.model.MBDocument;

public class MBDataHandlerBase implements MBDataHandler
{
  private static final Logger _log = Logger.getLogger(MBDataHandlerBase.class);

  public MBDocument loadDocument(String documentName)
  {
    _log.warn("MBDataHandlerBase: No loadDocument implementation for " + documentName);
    return null;
  }

  public MBDocument loadDocument(String documentName, MBDocument args)
  {
    _log.warn("MBDataHandlerBase: No loadDocument implementation for " + documentName);
    return null;
  }

  public void storeDocument(MBDocument document)
  {
    _log.warn("MBDataHandlerBase: No storeDocument implementation for " + document.getDefinition().getName());
  }

}
