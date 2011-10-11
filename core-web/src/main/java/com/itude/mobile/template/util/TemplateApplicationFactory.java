package com.itude.mobile.template.util;

import com.itude.commons.exceptions.ItudeRuntimeException;
import com.itude.mobile.mobbl2.client.core.controller.MBApplicationFactory;
import com.itude.mobile.mobbl2.client.core.services.MBResultListener;

public class TemplateApplicationFactory extends MBApplicationFactory
{

  @Override
  public MBResultListener createResultListener(String listenerClassName)
  {
    // You can add ResultListeners here in case you need any
    throw new ItudeRuntimeException("Unknown ResultListener "+ listenerClassName);
  }

}
