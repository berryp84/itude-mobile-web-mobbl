package com.itude.mobile.web.jsf;

import java.io.Serializable;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.itude.mobile.mobbl2.client.core.util.MBCacheManager;

@RequestScoped
@Named
public class FlushCacheBean implements Serializable
{
  private static final Logger _log             = Logger.getLogger(FlushCacheBean.class);

  private static final long   serialVersionUID = 1L;

  public String getFlushMessage()
  {
    MBCacheManager.flushGlobalCache();
    String message = "Cache flushed at "+ new Date();
    _log.info(message);
    return message;
  }

}
