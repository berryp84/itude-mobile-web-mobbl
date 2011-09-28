package com.itude.mobile.mobbl2.client.core.util;


public class DeviceUtil
{
  private static DeviceUtil _instance;

  private DeviceUtil()
  {
  }

  public static DeviceUtil getInstance()
  {
    if (_instance == null)
    {
      _instance = new DeviceUtil();
    }

    return _instance;
  }

  public String getUniqueID()
  {
    /*    String androidID = System.getString(_context.getContentResolver(), System.ANDROID_ID);*/
    return "Web";
  }
}
