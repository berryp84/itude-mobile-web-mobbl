package com.itude.mobile.mobbl2.client.core.view.helpers;

import com.itude.commons.util.ComparisonUtil;
import com.itude.mobile.mobbl2.client.core.view.MBComponent;

public class MBNameSelector<T extends MBComponent> implements MBSelector<T, String>
{

  @Override
  public boolean meets(T object, String value)
  {
    return ComparisonUtil.safeEquals(object.getName(),value);
  }

}
