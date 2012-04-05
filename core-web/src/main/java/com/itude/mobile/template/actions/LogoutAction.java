package com.itude.mobile.template.actions;

import javax.inject.Inject;
import javax.inject.Named;

import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.template.jsf.SessionBean;
import com.itude.mobile.web.actions.GenericAction;

@Named("LogoutAction")
public class LogoutAction extends GenericAction
{
  @Inject
  private SessionBean _session;

  @Override
  public MBOutcome execute(MBDocument document, String path, String outcomeName)
  {
    MBOutcome outcome = new MBOutcome("OUTCOME-page_home", null);
    _session.logOff();
    return outcome;
  }

}