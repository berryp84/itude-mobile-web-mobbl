/*
 * (C) Copyright ItudeMobile.
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
package com.itude.mobile.web.jsf;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.itude.mobile.web.controllers.ApplicationController;

@Named
@SessionScoped
public class InitialBean implements Serializable
{
  private static final long     serialVersionUID        = 1L;

  @Inject
  private ApplicationController _applicationController;

  private boolean               _initialized            = false;
  private static boolean        _applicationInitialized = false;

  public boolean isInitialized()
  {
    // Needs to be done once
    if (!_applicationInitialized)
    {
      _applicationController.initialize();
      _applicationInitialized = true;
    }

    // Needs to be done each session (which is why this bean is SessionScoped)
    if (!_initialized)
    {
      _applicationController.setInitialView();
      _initialized = true;
    }
    return _initialized;
  }
}
