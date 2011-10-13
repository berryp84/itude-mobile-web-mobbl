package com.itude.mobile.web.jsf;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.itude.commons.util.VersionUtil;
import com.itude.mobile.mobbl2.client.core.util.MobblEnvironment;

/**
 * This form should be extended. When you extend this, please use the following:
 * @Named(value = "env")
 * @ApplicationScoped
 */
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
      _projectVersion = VersionUtil.getVersion(getGroupId(), getArtifactId());
    }
    return _projectVersion;
  }
  
  protected String getGroupId()
  {
    return MobblEnvironment.getGroupId();
  }
  
  protected String getArtifactId()
  {
    return MobblEnvironment.getArtifactId();
  }
}
