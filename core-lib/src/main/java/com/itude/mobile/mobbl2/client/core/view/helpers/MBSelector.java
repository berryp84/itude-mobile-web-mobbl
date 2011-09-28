package com.itude.mobile.mobbl2.client.core.view.helpers;

public interface MBSelector<T, S>
{
  public boolean meets(T object, S value);
}
