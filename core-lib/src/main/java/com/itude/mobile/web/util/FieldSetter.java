package com.itude.mobile.web.util;

import com.itude.mobile.mobbl2.client.core.model.MBElementContainer;

public class FieldSetter
{
  private MBElementContainer _container;
  private String _path;
  
  public FieldSetter(MBElementContainer container, String path)
  {
    _container = container;
    _path = path;
  }
  
  public void setValue(String value)
  {
    // No notifyValueChanged will be called, but AFIK, this isn't used anyway
    _container.setValue(value, _path);
  }
  
  public String getValue()
  {
    return _container.getValueForPath(_path);
  }
}
