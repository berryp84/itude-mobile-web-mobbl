package com.itude.mobile.mobbl2.client.core.controller;

import java.io.Serializable;

import com.itude.commons.exceptions.ItudeRuntimeException;
import com.itude.mobile.mobbl2.client.core.services.MBResultListener;
import com.itude.mobile.mobbl2.client.core.util.MobblEnvironment;

public class MBApplicationFactory implements Serializable
{
  private static final long serialVersionUID = 1L;
  
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
      throw new ItudeRuntimeException(e);
    }
    catch (IllegalAccessException e)
    {
      throw new ItudeRuntimeException(e);
    }
    catch (ClassNotFoundException e)
    {
      throw new ItudeRuntimeException(e);
    }
    return object;
  }

}
