package com.itude.mobile.mcds.jsf;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.model.MBSessionInterface;
import com.itude.mobile.mobbl2.client.core.services.MBDataManagerService;
import com.itude.mobile.template.controllers.CurrentView;

@SessionScoped
@Named
public class SessionBean implements Serializable, MBSessionInterface
{
  private static final long    serialVersionUID = 1L;

  private MBDocument           _document;

  @Inject
  private MBDataManagerService _dataManagerService;

  @Inject
  private CurrentView          _view;

  private String               _param;

  public MBDocument getDocument()
  {
    if (_document == null) _document = _dataManagerService.loadDocument("SessionState");
    return _document;
  }

  public void logOn(String msisdn, String credits)
  {
    MBDocument sessionDoc = getDocument();

    sessionDoc.setValue(msisdn, "Session[0]/@msisdn");
    sessionDoc.setValue(credits, "Session[0]/@credits");
    sessionDoc.setValue("true", "Session[0]/@loggedIn");

    MBDataManagerService.getInstance().storeDocument(sessionDoc);
  }

  @Override
  public void logOff()
  {
    MBDocument sessionDoc = getDocument();

    sessionDoc.setValue(null, "Session[0]/@msisdn");
    sessionDoc.setValue(null, "Session[0]/@credits");
    sessionDoc.setValue("false", "Session[0]/@loggedIn");

    _view.clear();
  }

  @Override
  public boolean isLoggedOn()
  {
    return getDocument().getValueForPath("Session[0]/@loggedIn").equals("true");
  }

  public String getParam()
  {
    return _param;
  }

  public void set_param(String _param)
  {
    this._param = _param;
  }

  public String getReferrer()
  {
    MBDocument sessionDoc = getDocument();
    return (String) sessionDoc.getValueForPath("Session[0]/@referrer");
  }

  public String getIp()
  {
    MBDocument sessionDoc = getDocument();
    return (String) sessionDoc.getValueForPath("Session[0]/@ip");
  }

  public String getClickId()
  {
    MBDocument sessionDoc = getDocument();
    return (String) sessionDoc.getValueForPath("Session[0]/@clickId");
  }
}
