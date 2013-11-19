/*
 * (C) Copyright Itude Mobile B.V., The Netherlands
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itude.mobile.web.servlets;

import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

import com.itude.mobile.web.exceptions.ItudeRuntimeException;
import com.itude.mobile.web.util.Base64;

public class FileStorage
{
  private final Map<String, StoredFile> _files;

  private static final FileStorage      _instance = new FileStorage();

  private static AtomicLong             _count    = new AtomicLong(0);

  private boolean                       _cleaning = false;

  protected FileStorage()
  {
    _files = new Hashtable<String, StoredFile>();
    startCleanJob();
  }

  private void startCleanJob()
  {
    try
    {
      SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
      Scheduler sched = schedFact.getScheduler();
      sched.start();

      JobDetail detail = new JobDetail("cleanJob", null, StorageCleaner.class);
      detail.getJobDataMap().put("storage", this);

      Trigger trigger = TriggerUtils.makeMinutelyTrigger();
      trigger.setStartTime(new Date());
      trigger.setName("cleanTrigger");

      sched.scheduleJob(detail, trigger);
    }
    catch (SchedulerException e)
    {
      throw new ItudeRuntimeException(e);
    }
  }

  protected static FileStorage getInstance()
  {
    return _instance;
  }

  private String doStoreFile(byte[] data, String mimeType, long ttl)
  {
    StoredFile file = new StoredFile(data, mimeType, ttl);
    String key = Base64.encodeLong(_count.incrementAndGet());
    _files.put(key, file);
    return key;
  }

  public static String storeFile(byte[] data, String mimeType)
  {
    return getInstance().doStoreFile(data, mimeType, 0);
  }

  public static String storeFile(byte[] data, String mimeType, long ttl)
  {
    return getInstance().doStoreFile(data, mimeType, ttl);
  }

  private StoredFile doRetrieveFile(String key)
  {
    return _files.get(key);
  }

  public static StoredFile retrieveFile(String key)
  {
    return getInstance().doRetrieveFile(key);
  }

  public void clean()
  {
    if (!_cleaning)
    {
      synchronized (this)
      {
        if (!_cleaning)
        {
          _cleaning = true;

          try
          {
            long now = System.currentTimeMillis();

            for (Iterator<Map.Entry<String, StoredFile>> it = _files.entrySet().iterator(); it.hasNext();)
            {
              StoredFile sf = it.next().getValue();
              if (sf.getTTL() != 0 && sf.getTTL() < now) it.remove();
            }

          }
          finally
          {
            _cleaning = false;
          }
        }
      }
    }
  }
}
