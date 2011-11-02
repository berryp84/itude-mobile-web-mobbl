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
