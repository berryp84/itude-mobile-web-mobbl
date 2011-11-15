package com.itude.mobile.mobbl2.client.core.util;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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

  private static MBCacheManager _globalInstance;

  private Map<String, MBDocument> _documents;
  private Map<String, Long>       _ttls;

  @PostConstruct
  protected void init()
  {
    _ttls = new Hashtable<String, Long>();
    _documents = new Hashtable<String, MBDocument>();
  }

  // Returns the SessionScoped MBCacheManager
  protected static MBCacheManager getInstance()
  {
    return ELUtil.getValue("CacheManager", MBCacheManager.class);
  }
  
  // Returns the global (equal to all users) MBCacheManager
  protected static MBCacheManager getGlobalInstance()
  {
    if(_globalInstance == null)
    {
      MBCacheManager globalInstance = new MBCacheManager();
      globalInstance.init();
      _globalInstance = globalInstance;
    }
    return _globalInstance;
  }

  public synchronized void checkExpirationDates()
  {
    Iterator<Entry<String,Long>> it = _ttls.entrySet().iterator();
    while (it.hasNext())
    {
        Map.Entry<String,Long> pair = it.next();
        Long ttl = pair.getValue();
        if(System.currentTimeMillis() > ttl)
        {
          it.remove();
          _ttls.remove(pair.getKey());
          _documents.remove(pair.getKey());
        }
    }
  }

  protected MBDocument doGetDocumentForKey(String key)
  {
    // Check all ttls in order to avoid items staying forever in the cache if they aren't retrieved again
    checkExpirationDates();
    
    MBDocument toReturn = _documents.get(key);
    if(toReturn != null)
    {
      _log.trace(key + " retreived from cache");
    }
    return toReturn;
  }

  protected synchronized void doSetDocument(MBDocument document, String key, int ttl)
  {
    _log.debug("Setting cache for " + key);
    if (ttl != 0) _ttls.put(key, System.currentTimeMillis() + ttl);
    _documents.put(key, document);
  }

  public static MBDocument documentForKey(String key, boolean global)
  {
    if(global)
    {
      return getGlobalInstance().doGetDocumentForKey(key);
    }
    return getInstance().doGetDocumentForKey(key);
  }

  public static void setDocument(MBDocument document, String key, int ttl, boolean global)
  {
    if(global)
    {
      getGlobalInstance().doSetDocument(document, key, ttl);
    }
    else
    {
      getInstance().doSetDocument(document, key, ttl);
    }
  }
  
  public static void flushGlobalCache()
  {
    // Just removing the global cache will do the trick
    _globalInstance = null;
  }

}
