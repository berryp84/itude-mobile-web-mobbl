package com.itude.mobile.web.jsf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class AlertBean implements Serializable
{
  private static final long serialVersionUID = 1L;
  
  private boolean _shown;
  private boolean _notice;
  private String  _title;
  private String  _message;

  @PostConstruct
  protected void init()
  {
    _shown = false;
    _notice = false;
    _title = "";
    _message = "";
  }

  public boolean isShown()
  {
    return _shown;
  }

  public void setShown(boolean shown)
  {
    _shown = shown;
  }
  
  public boolean isDoHide()
  {
    _shown = false;
    return false;
  }

  public String getTitle()
  {
    return _title;
  }

  public void setTitle(String title)
  {
    _title = title;
  }

  public String getMessage()
  {
    return _message;
  }

  public void setMessage(String message)
  {
    _message = message;
  }
  
  public void setNotice(boolean notice)
  {
    _notice = notice;
  }
  
  public boolean isNotice()
  {
    boolean notice = _notice;
    _notice = false;
    return notice;
  }
}
