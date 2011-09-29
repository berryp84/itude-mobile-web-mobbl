package com.itude.mobile.template.annotations;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;

class HttpParams
{
  @Produces
  @HttpParam("")
  String getParamValue(ServletRequest request, InjectionPoint ip)
  {
    return request.getParameter(ip.getAnnotated().getAnnotation(HttpParam.class).value());
  }

  @Produces
  @HttpParam("")
  long getParsedLongParamValue(ServletRequest request, InjectionPoint ip)
  {
    String value = request.getParameter(ip.getAnnotated().getAnnotation(HttpParam.class).value());
    if (value == null || value.isEmpty()) return 0;
    try
    {
      return Long.parseLong(value);
    }
    catch (NumberFormatException e)
    {
      return 0;
    }
  }

  @Produces
  @HttpParam("")
  boolean getParsedBooleanParamValue(ServletRequest request, InjectionPoint ip)
  {
    String value = request.getParameter(ip.getAnnotated().getAnnotation(HttpParam.class).value());
    if (value == null) return false;
    else return (!"false".equalsIgnoreCase(value));
  }

  @Produces
  ServletRequest getCurrentRequest()
  {
    return (ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
  }
}
