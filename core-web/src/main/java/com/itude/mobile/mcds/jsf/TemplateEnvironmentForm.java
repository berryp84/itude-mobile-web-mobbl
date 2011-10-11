package com.itude.mobile.mcds.jsf;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.itude.mobile.web.jsf.EnvironmentForm;

@Named(value = "env")
@ApplicationScoped
public class TemplateEnvironmentForm extends EnvironmentForm
{
  @Override
  protected String getArtifactId()
  {
    return "mobbl-core-web";
  }
}
