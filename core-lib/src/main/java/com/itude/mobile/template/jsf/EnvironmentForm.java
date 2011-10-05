package com.itude.mobile.template.jsf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.itude.commons.environment.ItudeEnvironment;
import com.itude.commons.util.VersionUtil;
import com.itude.mobile.mobbl2.client.core.services.MBLocalizationService;

@Named(value = "env")
@ApplicationScoped
public class EnvironmentForm
{
  private static String              _projectVersion;
  private static String              _environment;
  private static final Logger _log                = Logger.getLogger(EnvironmentForm.class);

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
      _projectVersion = VersionUtil.getVersion("com.itude.mobile.web.mcds", "mcds");
    }
    return _projectVersion;
  }
  
  public String getEnvironment()
  {
    if(_environment == null)
    {
      String resource = "META-INF/maven/" + "com.itude.mobile" + "/" + "itude-mobile-template-site" + "/pom.properties";
      InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
      if (is == null) return "template";
      
      Properties props = new Properties();
      try
      {
        props.load(is);
      }
      catch (IOException e)
      {
        _log.error(e);
      }
      _environment = props.getProperty("environment", "template");
    }
    return _environment;
  }
  
  public static String getStaticEnvironment()
  {
    if(_environment != null)
      return _environment;
    
    else
      return new EnvironmentForm().getEnvironment();
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
