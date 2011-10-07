package com.itude.mobile.web.servlets;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class StorageCleaner implements Job
{

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException
  {
    FileStorage storage = (FileStorage) context.getMergedJobDataMap().get("storage");
    storage.clean();
  }

}
