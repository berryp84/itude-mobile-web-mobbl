package com.itude.mobile.template.controllers;

import com.itude.mobile.mcds.services.MCDSLocalizationService;
import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.mobbl2.client.core.services.MBLocalizationService;

public class MCDSController extends ApplicationController
{
  @Override
  public void initialize()
  {
    MBLocalizationService.getInstance().setInstance(new MCDSLocalizationService());
  }
  
  @Override
  public void setInitialView()
  {
    getView().clear();
    MBOutcome outcome = new MBOutcome("OUTCOME-page_home", getDataManagerService().loadDocument("MBEmptyDoc"));
    handleOutcome(outcome);
  }
}
