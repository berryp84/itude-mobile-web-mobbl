package com.itude.mobile.mobbl2.client.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itude.commons.exceptions.ItudeRuntimeException;

public class MBException extends ItudeRuntimeException
{
  private static final long    serialVersionUID = 1271249723743935918L;

  private static final Pattern NAME_PATTERN     = Pattern.compile(".*\\.MB(.+)Exception");

  private final String         _name;

  private String extractName()
  {
    Matcher matcher = NAME_PATTERN.matcher(this.getClass().getName());
    matcher.find();
    return matcher.group(1);
  }

  public MBException(String msg)
  {
    super(msg);
    _name = extractName();
  }

  public MBException(String msg, Throwable throwable)
  {
    super(msg, throwable);
    _name = extractName();
  }

  public String getName()
  {
    return _name;
  }

}
