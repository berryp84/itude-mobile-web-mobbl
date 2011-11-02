package com.itude.mobile.web.annotations;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Named;

public class NamedQualifier extends AnnotationLiteral<Named> implements Named
{
  private static final long serialVersionUID = 1L;

  private String            _value;

  public NamedQualifier(String value)
  {
    _value = value;
  }

  @Override
  public String value()
  {
    return _value;
  }
}
