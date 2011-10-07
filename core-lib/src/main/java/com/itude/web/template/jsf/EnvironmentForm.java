package com.itude.web.template.jsf;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.itude.commons.util.VersionUtil;

/**
 * This form should be extended. When you extend this, please use the following:
 * @Named(value = "env")
 * @ApplicationScoped
 */
public abstract class EnvironmentForm
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
      _projectVersion = VersionUtil.getVersion(getGroupId(), getArtifactId());
    }
    return _projectVersion;
  }
  
  // You want to override this function when this isn't correct
  protected String getGroupId()
  {
    return "com.itude.mobile.web.mobbl";
  }
  
  protected abstract String getArtifactId();
}
