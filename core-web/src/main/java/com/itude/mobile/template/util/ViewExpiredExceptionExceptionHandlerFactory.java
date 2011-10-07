package com.itude.mobile.template.util;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class ViewExpiredExceptionExceptionHandlerFactory extends ExceptionHandlerFactory
{

  private final ExceptionHandlerFactory _parent;

  public ViewExpiredExceptionExceptionHandlerFactory(ExceptionHandlerFactory parent)
  {
    _parent = parent;
  }

  @Override
  public ExceptionHandler getExceptionHandler()
  {
    ExceptionHandler result = _parent.getExceptionHandler();
    result = new ViewExpiredExceptionExceptionHandler(result);

    return result;
  }

}
