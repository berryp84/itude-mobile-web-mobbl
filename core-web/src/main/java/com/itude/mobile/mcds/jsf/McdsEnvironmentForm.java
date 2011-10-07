package com.itude.mobile.mcds.jsf;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.itude.commons.environment.ItudeEnvironment;
import com.itude.mobile.template.jsf.EnvironmentForm;

@Named(value = "env")
@ApplicationScoped
public class McdsEnvironmentForm extends EnvironmentForm
{
  public boolean isItudeTest()
  {
    String environmentName = ItudeEnvironment.getEnvironment().getName();
    return environmentName.equals("dev");
  }

  @Override
  protected String getArtifactId()
  {
    return "mobbl-core-web";
  }
}
