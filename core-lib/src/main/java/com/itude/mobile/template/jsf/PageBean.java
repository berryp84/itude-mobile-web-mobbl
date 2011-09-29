package com.itude.mobile.template.jsf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import com.itude.mobile.mobbl2.client.core.view.MBPage;
import com.itude.mobile.template.controllers.AbstractRefreshableViewController;
import com.itude.mobile.template.controllers.CurrentView;
import com.itude.mobile.template.controllers.MBViewController;

@RequestScoped
@Named
public class PageBean implements Serializable
{
  private static final long serialVersionUID = 1L;

  //private Map<String, Object>    _inputs;

  @Inject
  private CurrentView       _view;

  private MBViewController  _viewController;

  @PostConstruct
  protected void init()
  {
    setViewController(_view.getView());
  }

  public void setViewController(MBViewController controller)
  {
    _viewController = controller;
    //    _inputs = new PageMap(_viewController != null ? _viewController.getDocument() : null);
  }

  public void handleOutcome(ActionEvent ev)
  {
    String outcomeName = (String) ev.getComponent().getAttributes().get("outcome");
    String path = (String) ev.getComponent().getAttributes().get("path");
    if (_viewController != null) _viewController.handleOutcome(outcomeName, path);
  }

  /*
  public Map<String, Object> getInputs()
  {
    return _inputs;
  }

  public void setInputs(Map<String, Object> inputs)
  {
    _inputs = inputs;
  }*/

  public MBPage getPage()
  {
    return _viewController != null ? _viewController.getPage() : null;
  }

  public MBViewController getViewController()
  {
    return _viewController;
  }
  
  public boolean isRefreshable()
  {
    return _viewController instanceof AbstractRefreshableViewController;
  }
  
  public String refresh()
  {
    if(isRefreshable())
    {
      AbstractRefreshableViewController rfvc = (AbstractRefreshableViewController)_viewController;
      rfvc.refresh();
    }
    return "default";
  }

}

