package com.itude.mobile.template.jsf;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import com.itude.commons.util.VersionUtil;

@Named(value = "env")
@ApplicationScoped
public class EnvironmentForm
{
  private static String              _projectVersion;

  public String getPath()
  {
    FacesContext fc = FacesContext.getCurrentInstance();
    if (fc == null) return "";

    ExternalContext ec = fc.getExternalContext();
    return ec.getRequestContextPath();
  }

  public String getProjectVersion()
  {
    if (_projectVersion == null)
    {
      _projectVersion = VersionUtil.getVersion("com.itude.mobile.web.mobbl", "mobbl-core");
    }
    return _projectVersion;
  }
  
  // You want to override this function when this isn't correct
  // This should be made abstract in case all projects override this
  protected String getGroupId()
  {
    return "com.itude.mobile.web.mobbl";
  }
  
  // You want to override this function when this isn't correct
  // This should be made abstract in case all projects override this
  protected String getArtifactId()
  {
    return "mobbl-core";
  }
}
