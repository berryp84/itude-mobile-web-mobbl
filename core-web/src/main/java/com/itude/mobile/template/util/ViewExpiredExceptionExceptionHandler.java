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
package com.itude.mobile.template.util;

import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import com.itude.commons.jsf.util.ELUtil;
import com.itude.mobile.template.jsf.SessionBean;

public class ViewExpiredExceptionExceptionHandler extends ExceptionHandlerWrapper
{

  private final ExceptionHandler _wrapped;

  public ViewExpiredExceptionExceptionHandler(ExceptionHandler wrapped)
  {
    _wrapped = wrapped;
  }

  @Override
  public ExceptionHandler getWrapped()
  {
    return _wrapped;
  }

  @Override
  public void handle() throws FacesException
  {
    for (Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator(); i.hasNext();)
    {
      ExceptionQueuedEvent event = i.next();
      ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
      Throwable t = context.getException();
      if (t instanceof ViewExpiredException)
      {
        FacesContext fc = FacesContext.getCurrentInstance();
        NavigationHandler nav = fc.getApplication().getNavigationHandler();

        Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();

        try
        {
          requestMap.put("sessionExpired", true);

          SessionBean session = ELUtil.getValue("sessionBean", SessionBean.class);
          session.logOff();

          nav.handleNavigation(fc, null, "default");

          fc.renderResponse();

        }
        finally
        {
          i.remove();
        }
      }
    }

    getWrapped().handle();
  }

}
