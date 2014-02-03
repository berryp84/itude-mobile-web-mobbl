/*
 * (C) Copyright Itude Mobile B.V., The Netherlands
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itude.mobile.mobbl.core.controller;

import java.io.Serializable;

import com.itude.mobile.mobbl.core.services.MBResultListener;
import com.itude.mobile.mobbl.core.util.MobblEnvironment;
import com.itude.mobile.web.exceptions.ItudeRuntimeException;

/**
 * Factory class for creating custom MBViewControllers, MBResultListeners and MBActions
 *  
 * In short there are three steps to using custom code with MOBBL framework:
 * <ol>
 *  <li>Create Pages, Actions and ResultListeners in the application definition files  (config.xml and endpoints.xml).</li>
 *  <li>Create a subclass of the MBApplicationFactory which can create custom ViewControllers, MBActions and MBResultListeners,,/li>
 *  <li>set the instance to your MBApplicationFactory subclass:
 *  <code>
 *      MBApplicationFactory.setInstance(new CustomApplicationFactory());
 *  </code>
 *  </ol>
 */
public class MBApplicationFactory implements Serializable
{
  private static final long           serialVersionUID = 1L;

  private static MBApplicationFactory _mApplicationFactory;

  public static MBApplicationFactory getInstance()
  {
    if (_mApplicationFactory == null)
    {
      _mApplicationFactory = new MBApplicationFactory();
    }
    return _mApplicationFactory;
  }

  public MBResultListener createResultListener(String listenerClassName)
  {
    String className = MobblEnvironment.getResultListenerClassName(listenerClassName);
    return (MBResultListener) createObject(className);
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
