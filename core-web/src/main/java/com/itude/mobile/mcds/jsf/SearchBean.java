package com.itude.mobile.mcds.jsf;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.services.MBDataManagerService;
import com.itude.mobile.web.controllers.ApplicationController;

@Named
@RequestScoped
public class SearchBean implements Serializable
{
  private static final long serialVersionUID = 1L;

  private String                _searchquery;

  @Inject
  private MBDataManagerService  _dataManagerService;
  
  @Inject
  private ApplicationController _applicationController;
  
  public void setSearchQuery(String searchquery)
  {
    _searchquery = searchquery;
  }

  public String getSearchQuery()
  {
    return _searchquery;
  }

  public void search()
  {
    MBDocument searchDoc = _dataManagerService.loadDocument("SearchDocument");
    searchDoc.setValue(_searchquery, "Query[0]/@text()");

    MBOutcome outcome = new MBOutcome("OUTCOME-page_search", searchDoc);
    _applicationController.handleOutcome(outcome);
  }
}
