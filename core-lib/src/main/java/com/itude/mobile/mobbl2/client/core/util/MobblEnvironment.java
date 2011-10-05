package com.itude.mobile.mobbl2.client.core.util;

import com.itude.commons.environment.ItudeEnvironment;
import com.itude.mobile.template.jsf.EnvironmentForm;

public class MobblEnvironment extends ItudeEnvironment
{
  public static String getResourcesFile()
  {
    return getRequiredProperty("mobbl."+EnvironmentForm.getStaticEnvironment()+".resources");
  }
}
