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

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

public class ValueChangeListener implements javax.faces.event.ValueChangeListener
{
  private static final String PROCESSING_KEY = "PROCESSING_ValueChangeListener";

  @Override
  public void processValueChange(ValueChangeEvent event)
  {
    FacesContext ci = FacesContext.getCurrentInstance();
    Map<String, Object> requestMap = ci.getExternalContext().getRequestMap();

    Boolean processing = (Boolean) requestMap.get(PROCESSING_KEY);
    if (processing == null || !processing)
    {
      requestMap.put(PROCESSING_KEY, true);
      event.getComponent().processUpdates(ci);
      ci.getApplication().getNavigationHandler().handleNavigation(ci, null, "default");
    }
  }

}
