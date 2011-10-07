package com.itude.mobile.mcds.util;

import com.itude.commons.exceptions.ItudeRuntimeException;
import com.itude.mobile.mobbl2.client.core.controller.MBApplicationFactory;
import com.itude.mobile.mobbl2.client.core.services.MBResultListener;

public class MCDSApplicationFactory extends MBApplicationFactory
{

  @Override
  public MBResultListener createResultListener(String listenerClassName)
  {
    // With MCDS, we don't use resultListeners
    throw new ItudeRuntimeException("Unknown ResultListener "+ listenerClassName);
  }

}
