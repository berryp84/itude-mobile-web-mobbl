package com.itude.mobile.mobbl2.client.core.controller;

import com.itude.mobile.mobbl2.client.core.services.MBResultListener;

public abstract class MBApplicationFactory
{
  public abstract MBResultListener createResultListener(String listenerClassName);

}
