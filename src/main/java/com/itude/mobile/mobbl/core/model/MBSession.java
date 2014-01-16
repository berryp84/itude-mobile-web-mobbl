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
package com.itude.mobile.mobbl.core.model;

public class MBSession implements MBSessionInterface
{

  private static MBSessionInterface _instance;

  private MBSession()
  {

  }

  public static MBSessionInterface getInstance()
  {
    if (_instance == null)
    {
      _instance = new MBSession();
    }

    return _instance;
  }

  public static void setInstance(MBSessionInterface session)
  {
    _instance = session;
  }

  //
  //Override the following methods in an instance specific for your app; and register it app startup with setSharedInstance
  //
  @Override
  public MBDocument getDocument()
  {
    return null;
  }

  @Override
  public void logOff()
  {
  }

  @Override
  public boolean isLoggedOn()
  {
    return false;
  }

}
