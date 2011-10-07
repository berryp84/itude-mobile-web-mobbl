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

  @Override
  public void initializeTab()
  {
    String outcomeName = null;
    if ("SHARES".equals(getView().getCurrentDialog())) outcomeName = "OUTCOME-tab_shares";
    else if ("SEARCH".equals(getView().getCurrentDialog())) outcomeName = "OUTCOME-SearchAction";
    else if ("ACCOUNT".equals(getView().getCurrentDialog())) outcomeName = "OUTCOME-tab_my_account";
    else if ("HOME".equals(getView().getCurrentDialog())) outcomeName = "OUTCOME-tab_home";
    else if ("INFO".equals(getView().getCurrentDialog())) outcomeName = "OUTCOME-page_info";

    if (outcomeName != null)
    {
      MBOutcome outcome = new MBOutcome(outcomeName, getDataManagerService().loadDocument("MBEmptyDoc"));
      handleOutcome(outcome);
    }
  }
  
  
}
