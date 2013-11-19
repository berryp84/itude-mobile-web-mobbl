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
package com.itude.mobile.web.jsf;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

@RequestScoped
@Named
public abstract class AbstractLinkHandleBean
{
  @PostConstruct
  protected void init()
  {
    FacesContext fc = FacesContext.getCurrentInstance();

    HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
    boolean newSession = session == null || session.isNew();

    doInit(fc, newSession);
  }

  protected abstract void doInit(FacesContext fc, boolean newSession);

  public boolean isExists()
  {
    return true;
  }
}
