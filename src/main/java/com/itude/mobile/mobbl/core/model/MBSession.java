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

/**
 * Class responsible for the session
 *
 */
public class MBSession implements MBSessionInterface
{

  private static MBSessionInterface _instance;

  private MBSession()
  {

  }

  /**
   * Returns the current session instance
   * 
   * @return {@link MBSessionInterface}
   */
  public static MBSessionInterface getInstance()
  {
    if (_instance == null)
    {
      _instance = new MBSession();
    }

    return _instance;
  }

  /**
   * Sets the given sessions as the instance
   * 
   * @param session {@link MBSessionInterface}
   */
  public static void setInstance(MBSessionInterface session)
  {
    _instance = session;
  }

  /**
   * Should return the session document that stores the current session state
   * 
   * IMPORTANT: THIS METHOD IS NOT IMPLEMENTED! It needs to be overridden in a superclass
   * 
   * @return a {@link MBDocument} that keeps track of the current session state (e.g. A MBDocument that stores the current session state)
   */
  @Override
  public MBDocument getDocument()
  {
    return null;
  }

  /**
   * Should logOff the current session (e.g. clear the current session state from the session document)
   * 
   * IMPORTANT: THIS METHOD IS NOT IMPLEMENTED! It needs to be overridden in a superclass
   */
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
