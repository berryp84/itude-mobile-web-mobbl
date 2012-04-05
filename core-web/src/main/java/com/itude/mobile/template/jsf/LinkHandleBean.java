package com.itude.mobile.template.jsf;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.itude.commons.util.ComparisonUtil;
import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.services.MBDataManagerService;
import com.itude.mobile.mobbl2.client.core.services.MBLocalizationService;
import com.itude.mobile.web.annotations.HttpParam;
import com.itude.mobile.web.controllers.ApplicationController;
import com.itude.mobile.web.controllers.CurrentView;
import com.itude.mobile.web.jsf.AbstractLinkHandleBean;

@RequestScoped
@Named
public class LinkHandleBean extends AbstractLinkHandleBean
{
  @Inject
  @HttpParam("ref")
  private String                _ref;

  @Inject
  @HttpParam("ymid")
  private String                _ymid;

  @Inject
  @HttpParam("param")
  private String                _param;

  @Inject
  @HttpParam("click_id")
  private String                _click_id;

  @Inject
  @HttpParam("nakko_id")
  private String                _nakko_id;

  @Inject
  @HttpParam("content_type")
  private String                _content_type;

  @Inject
  @HttpParam("third_party_id")
  private String                _third_party_id;

  @Inject
  @HttpParam("outcome")
  private String                _outcome;

  @Inject
  @HttpParam("path")
  private String                _path;

  @Inject
  @HttpParam("tab")
  private String                _tab;

  @Inject
  @HttpParam("title")
  private String                _title;

  @Inject
  @HttpParam("message")
  private String                _message;

  @Inject
  private CurrentView           _view;

  @Inject
  private ApplicationController _controller;

  @Inject
  private SessionBean           _sessionBean;

  @Override
  protected void doInit(FacesContext fc, boolean newSession)
  {
    NavigationHandler nav = fc.getApplication().getNavigationHandler();

    // Store referrer and ymid
    MBDocument sessionDoc = _sessionBean.getDocument();
    if (_ref != null) sessionDoc.setValue(_ref, "Session[0]/@referrer");
    if (_ymid != null) sessionDoc.setValue(_ymid, "Session[0]/@clickId");
    if (_click_id != null) sessionDoc.setValue(_click_id, "Session[0]/@clickId");
    if (_nakko_id != null) sessionDoc.setValue(_nakko_id, "Session[0]/@clickId");

    // Store properties needed for PAGE-page_error
    if (_title != null) sessionDoc.setValue(MBLocalizationService.getInstance().getTextForKey(_title), "Session[0]/@title");
    if (_message != null) sessionDoc.setValue(MBLocalizationService.getInstance().getTextForKey(_message), "Session[0]/@message");

    // Store title and third_party_id to direct access the order page
    if (_content_type != null) sessionDoc.setValue(_content_type, "Session[0]/@content_type");
    if (_third_party_id != null) sessionDoc.setValue(_third_party_id, "Session[0]/@third_party_id");

    MBDataManagerService.getInstance().storeDocument(sessionDoc);

    if (_view.getView() != null && _outcome != null)
    {
      if (_param != null)
      {
        _sessionBean.set_param(_param);
      }
      _view.getView().handleOutcome(_outcome, _path);
      nav.handleNavigation(fc, null, "default");
    }

    else if (_tab != null)
    {
      boolean reInit = false;
      if (ComparisonUtil.safeEquals(_tab, _view.getCurrentDialog()) || "HOME".equals(_tab)) reInit = true;
      _view.setCurrentDialog(_tab);
      if (_view.getView() == null || reInit) _controller.initializeTab();
      nav.handleNavigation(fc, null, "default");
    }
    else if (_outcome != null)
    {
      MBOutcome outcome = new MBOutcome(_outcome, null);
      _controller.handleOutcome(outcome);
      nav.handleNavigation(fc, null, "default");
    }
  }
}
