package com.itude.mobile.mcds.jsf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.itude.mobile.mcds.services.datahandlers.MCDSGetDataHandler;
import com.itude.mobile.mcds.services.datahandlers.MCDSPostDataHandler;
import com.itude.mobile.mobbl2.client.core.services.MBDataManagerService;
import com.itude.mobile.mobbl2.client.core.services.MBMetadataService;
import com.itude.mobile.web.handlers.SessionDataHandler;

@ApplicationScoped
public class MobblProvider implements Serializable
{
  private static final long serialVersionUID = 1L;

  @PostConstruct
  protected void init()
  {
	MBDataManagerService.getInstance().registerDataHandler(new MCDSGetDataHandler(), "MCDSGetDataHandler");
	MBDataManagerService.getInstance().registerDataHandler(new MCDSPostDataHandler(), "MCDSPostDataHandler");
    MBDataManagerService.getInstance().registerDataHandler(new SessionDataHandler(), "SessionDataHandler");
  }

  @Produces
  public MBMetadataService getMetaDataService()
  {
    return MBMetadataService.getInstance();
  }

  @Produces
  public MBDataManagerService getDataManagerService()
  {
    return MBDataManagerService.getInstance();
  }
}
