package com.itude.mobile.mobbl2.client.core.view;

public interface MBValueChangeListenerProtocol
{

  boolean valueWillChange(String value, String originalValue, String path);

  void valueChanged(String value, String originalValue, String path);

}
