package com.itude.mobile.web.annotations;

import javax.enterprise.util.AnnotationLiteral;

public class PageQualifier extends AnnotationLiteral<ForPage> implements ForPage
{
  private static final long serialVersionUID = 1L;

  private String            _value;

  public PageQualifier(String value)
  {
    _value = value;
  }

  @Override
  public String value()
  {
    return _value;
  }

}
