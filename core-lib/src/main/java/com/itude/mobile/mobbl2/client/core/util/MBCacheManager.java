package com.itude.mobile.mobbl2.client.core.util;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.itude.commons.jsf.util.ELUtil;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;

@SessionScoped
@Named("CacheManager")
public class MBCacheManager implements Serializable
{
  private static final long       serialVersionUID = 1L;

  private static final Logger     _log             = Logger.getLogger(MBCacheManager.class);

  private Map<String, MBDocument> _documents;
  private Map<String, Long>       _ttls;

  @PostConstruct
  protected void init()
  {
    _ttls = new Hashtable<String, Long>();
    _documents = new Hashtable<String, MBDocument>();
  }

  protected static MBCacheManager getInstance()
  {
    return ELUtil.getValue("CacheManager", MBCacheManager.class);
  }

  protected synchronized void expire(String key)
  {
    _ttls.remove(key);
    _documents.remove(key);
  }

  protected MBDocument doGetDocumentForKey(String key)
  {
    _log.debug("Retreiving cache for " + key);
    Long ttl = _ttls.get(key);
    if (ttl != null && System.currentTimeMillis() > ttl) expire(key);
    return _documents.get(key);
  }

  protected synchronized void doSetDocument(MBDocument document, String key, int ttl)
  {
    _log.debug("Setting cache for " + key);
    if (ttl != 0) _ttls.put(key, System.currentTimeMillis() + ttl);
    _documents.put(key, document);
  }

  public static MBDocument documentForKey(String key)
  {
    return getInstance().doGetDocumentForKey(key);
  }

  public static void setDocument(MBDocument document, String key, int ttl)
  {
    getInstance().doSetDocument(document, key, ttl);
  }

}
