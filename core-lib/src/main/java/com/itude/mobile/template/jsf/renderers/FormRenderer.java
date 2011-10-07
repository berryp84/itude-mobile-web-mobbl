package com.itude.mobile.template.jsf.renderers;

import javax.faces.context.FacesContext;

import com.itude.commons.util.Base64;

public class FormRenderer extends com.sun.faces.renderkit.html_basic.FormRenderer
{

  @Override
  public String convertClientId(FacesContext context, String clientId)
  {
    int hashCode = clientId.hashCode();
    return Base64.encodeLong(hashCode);
  }

}