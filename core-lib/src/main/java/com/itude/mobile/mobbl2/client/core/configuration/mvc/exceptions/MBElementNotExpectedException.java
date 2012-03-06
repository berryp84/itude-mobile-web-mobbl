package com.itude.mobile.mobbl2.client.core.configuration.mvc.exceptions;

import com.itude.mobile.mobbl2.client.core.MBException;

public class MBElementNotExpectedException extends MBException
{

  /**
   * 
   */
  private static final long serialVersionUID = -8755637266607855612L;

  public MBElementNotExpectedException(String msg)
  {
    super(msg);
  }

  public MBElementNotExpectedException(String msg, Throwable throwable)
  {
    super(msg, throwable);
  }

}
