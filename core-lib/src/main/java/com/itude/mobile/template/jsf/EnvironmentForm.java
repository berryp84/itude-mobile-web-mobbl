package com.itude.mobile.template.jsf;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import com.itude.commons.environment.ItudeEnvironment;
import com.itude.commons.util.VersionUtil;
import com.itude.mobile.mobbl2.client.core.services.MBLocalizationService;

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
  
  
  
  public boolean isItudeTest()
  {
    String environmentName = ItudeEnvironment.getEnvironment().getName();
    return environmentName.equals("dev");
  }
  
  public String getCallText()
  {
    return MBLocalizationService.getInstance().getTextForKey("CallNowMessage");
  }
  
  public String getCallPhoneNumber()
  {
    return MBLocalizationService.getInstance().getTextForKey("CallNowPhoneNumber");
  }
}
