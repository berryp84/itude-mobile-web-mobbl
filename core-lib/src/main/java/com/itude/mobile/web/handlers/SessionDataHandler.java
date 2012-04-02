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
  public MBDocument loadDocument(String documentName, MBDocument args, String uniqueAddition)
  {
    return loadDocument(documentName);
  }

  @Override
  public void storeDocument(MBDocument document)
  {
    getSessionMap().put(determineKey(document.getName()), document);
  }

}
