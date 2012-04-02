package com.itude.mobile.mobbl2.client.core.services.datamanager;

import com.itude.mobile.mobbl2.client.core.model.MBDocument;

public interface MBDataHandler
{
  public MBDocument loadDocument(String documentName);
  public MBDocument loadDocument(String documentName, MBDocument args);
  public MBDocument loadDocument(String documentName, MBDocument args, String uniqueAddition);
  public void storeDocument(MBDocument document);

}
