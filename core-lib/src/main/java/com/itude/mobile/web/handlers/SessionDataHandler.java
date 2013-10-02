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
package com.itude.mobile.web.handlers;

import java.io.Serializable;
import java.util.Map;

import javax.faces.context.FacesContext;

import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.services.datamanager.MBDataHandler;

public class SessionDataHandler implements MBDataHandler, Serializable
{
  private static final long serialVersionUID = 1L;

  private Map<String, Object> getSessionMap()
  {
    FacesContext ctx = FacesContext.getCurrentInstance();
    return ctx.getExternalContext().getSessionMap();
  }

  private String determineKey(String documentName)
  {
    return "DOCUMENT_" + documentName;
  }

  @Override
  public MBDocument loadDocument(String documentName)
  {
    return (MBDocument) getSessionMap().get(determineKey(documentName));
  }

  @Override
  public MBDocument loadDocument(String documentName, MBDocument args)
  {
    return loadDocument(documentName);
  }

  @Override
  public void storeDocument(MBDocument document)
  {
    getSessionMap().put(determineKey(document.getName()), document);
  }

}
