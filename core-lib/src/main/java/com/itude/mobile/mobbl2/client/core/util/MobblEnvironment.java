package com.itude.mobile.mobbl2.client.core.util;

import com.itude.commons.environment.ItudeEnvironment;

public class MobblEnvironment extends ItudeEnvironment
{
  public static String getResourcesFile()
  {
    return getProperty("mobbl.resources", "resources.xml");
  }
  
  public static String getResultListenerClassName(String resultListenerName)
  {
    return getRequiredProperty("resultlistener."+ resultListenerName);
  }

  public static String getGroupId()
  {
    return getRequiredProperty("pom.groupid");
  }

  public static String getArtifactId()
  {
    return getRequiredProperty("pom.artifactid");
  }
}
