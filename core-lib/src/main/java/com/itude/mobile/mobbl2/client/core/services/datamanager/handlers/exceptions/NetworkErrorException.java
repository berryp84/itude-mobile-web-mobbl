package com.itude.mobile.mobbl2.client.core.services.datamanager.handlers.exceptions;

import com.itude.mobile.mobbl2.client.core.MBException;

public class NetworkErrorException extends MBException
{
  private static final long serialVersionUID = 3091172841776637109L;

  public NetworkErrorException(String msg)
  {
    super(msg);
  }

}
