package com.itude.mobile.mobbl2.client.core.controller;

import com.itude.mobile.mobbl2.client.core.services.MBResultListener;
import com.itude.mobile.mobbl2.client.core.util.MobblEnvironment;

public class MBApplicationFactory
{
  private static MBApplicationFactory _mApplicationFactory;
  
  public static MBApplicationFactory getInstance()
  {
    if(_mApplicationFactory == null)
    {
      _mApplicationFactory = new MBApplicationFactory();
    }
    return _mApplicationFactory;
  }
  
  public MBResultListener createResultListener(String listenerClassName)
  {
    String className = MobblEnvironment.getResultListenerClassName(listenerClassName);
    return (MBResultListener)createObject(className);
  }

  private static Object createObject(String className)
  {
    Object object = null;
    try
    {
      Class<?> classDefinition = Class.forName(className);
      object = classDefinition.newInstance();
    }
    catch (InstantiationException e)
    {
      System.out.println(e);
    }
    catch (IllegalAccessException e)
    {
      System.out.println(e);
    }
    catch (ClassNotFoundException e)
    {
      System.out.println(e);
    }
    return object;
  }

}
