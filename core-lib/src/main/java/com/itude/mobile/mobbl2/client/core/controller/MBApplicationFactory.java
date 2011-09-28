package com.itude.mobile.mobbl2.client.core.controller;

import com.itude.mobile.mobbl2.client.core.services.MBResultListener;

public class MBApplicationFactory
{

  private static MBApplicationFactory _instance = null;

  public static MBApplicationFactory getInstance()
  {
    if (_instance == null)
    {
      _instance = new MBApplicationFactory();
    }
    return _instance;
  }

  public static void setInstance(MBApplicationFactory factory)
  {
    _instance = factory;
  }

  public MBResultListener createResultListener(String listenerClassName)
  {
    // TODO: should be done more elegantly
    // TODO: make abstract?
//    if ("BinckSoapFaultListener".equals(listenerClassName)) return new BinckSoapFaultListener();
//    else if ("TimedOutResultListener".equals(listenerClassName)) return new TimedOutResultListener();
//    else 
    return null;

  }

}
