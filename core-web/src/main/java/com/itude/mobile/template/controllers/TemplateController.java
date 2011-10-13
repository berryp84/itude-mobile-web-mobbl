package com.itude.mobile.template.controllers;

import java.io.Serializable;

import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.web.controllers.ApplicationController;

public class TemplateController extends ApplicationController implements Serializable
{
  private static final long serialVersionUID = 1L;

  @Override
  public void initialize()
  {
    
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
    // TODO: do something like this:
    /*
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
    */
  }
  
  
}
