package com.itude.mobile.web.actions;

import javax.inject.Inject;

import com.itude.mobile.mobbl2.client.core.controller.MBAction;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.services.MBDataManagerService;
import com.itude.mobile.mobbl2.client.core.services.MBMetadataService;

public abstract class GenericAction implements MBAction
{
  @Inject
  private MBDataManagerService _dataManagerService;

  @Inject
  private MBMetadataService    _metadataService;

  protected MBDataManagerService getDataManagerService()
  {
    return _dataManagerService;
  }

  protected MBDocument loadDocument(String name)
  {
    return getDataManagerService().loadDocument(name);
  }

  protected MBDocument loadDocument(String name, MBDocument arguments)
  {
    return getDataManagerService().loadDocument(name, arguments);
  }

  protected MBMetadataService getMetadataService()
  {
    return _metadataService;
  }

}
