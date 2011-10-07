package com.itude.mobile.template.jsf;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.itude.mobile.template.controllers.ApplicationController;

@Named
@SessionScoped
public class InitialBean implements Serializable
{
  private static final long     serialVersionUID = 1L;

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
}
