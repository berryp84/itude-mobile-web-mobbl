package com.itude.mobile.web.jsf.renderers;

import javax.faces.context.FacesContext;

import com.itude.mobile.web.util.PageHelper;

public class ButtonRenderer extends com.sun.faces.renderkit.html_basic.ButtonRenderer
{

  @Override
  public String convertClientId(FacesContext context, String clientId)
  {
    return PageHelper.convertClientId(context, clientId);
  }

}