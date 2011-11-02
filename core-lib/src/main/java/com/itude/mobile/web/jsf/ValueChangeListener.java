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
