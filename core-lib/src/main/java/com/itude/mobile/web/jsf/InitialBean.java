package com.itude.mobile.web.jsf;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.itude.mobile.web.controllers.ApplicationController;

@Named
@SessionScoped
public class InitialBean implements Serializable
{
  private static final long     serialVersionUID        = 1L;

  @Inject
  private ApplicationController _applicationController;

  private boolean               _initialized            = false;
  private static boolean        _applicationInitialized = false;

  public boolean isInitialized()
  {
    // Needs to be done once
    if (!_applicationInitialized)
    {
      _applicationController.initialize();
      _applicationInitialized = true;
    }

    // Needs to be done each session (which is why this bean is SessionScoped)
    if (!_initialized)
    {
      _applicationController.setInitialView();
      _initialized = true;
    }
    return _initialized;
  }
}
