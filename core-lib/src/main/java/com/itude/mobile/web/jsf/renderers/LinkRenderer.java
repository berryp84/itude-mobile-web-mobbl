package com.itude.mobile.web.jsf.renderers;

import javax.faces.context.FacesContext;

import com.itude.mobile.web.util.PageHelper;

public class LinkRenderer extends com.sun.faces.renderkit.html_basic.OutputLinkRenderer
{

  @Override
  public String convertClientId(FacesContext context, String clientId)
  {
    return PageHelper.convertClientId(context, clientId);
  }

}