package com.itude.mobile.template.jsf;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.services.MBDataManagerService;
import com.itude.mobile.mobbl2.client.core.services.MBLocalizationService;
import com.itude.mobile.template.annotations.HttpParam;
import com.itude.mobile.template.controllers.ApplicationController;

@Named(value = "initialBean")
@SessionScoped
public class InitialBean implements Serializable
{
  private static final long     serialVersionUID = 1L;

  @Inject
  @HttpParam("searchquery")
  private String                _searchquery;

  @Inject
  private MBDataManagerService  _dataManagerService;

  @Inject
  private ApplicationController _applicationController;

  private boolean               _initialized     = false;

  public boolean isInitialized()
  {
    if (!_initialized)
    {
      _applicationController.initialize();
      _applicationController.setInitialView();
      _initialized = true;
    }
    return _initialized;
  }

  public static String textForKey(String key)
  {
    return MBLocalizationService.getInstance().getTextForKey(key);
  }

  public void setSearchQuery(String _searchquery)
  {
    this._searchquery = _searchquery;
  }

  public String getSearchQuery()
  {
    return _searchquery;
  }

  public void search(ActionEvent av)
  {
    MBDocument searchDoc = _dataManagerService.loadDocument("SearchDocument");
    searchDoc.setValue(_searchquery, "Query[0]/@text()");

    MBOutcome outcome = new MBOutcome("OUTCOME-page_search", searchDoc);
    _applicationController.handleOutcome(outcome);
  }

}
