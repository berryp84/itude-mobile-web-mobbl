package com.itude.mobile.web.jsf.renderers;

import javax.faces.context.FacesContext;

import com.itude.commons.util.Base64;

public class MenuRenderer extends com.sun.faces.renderkit.html_basic.MenuRenderer
{

  @Override
  public String convertClientId(FacesContext context, String clientId)
  {
    int hashCode = clientId.hashCode();
    return Base64.encodeLong(hashCode);
  }

}