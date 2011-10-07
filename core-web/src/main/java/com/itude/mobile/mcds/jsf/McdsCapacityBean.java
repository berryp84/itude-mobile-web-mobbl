package com.itude.mobile.mcds.jsf;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.itude.mobile.web.jsf.CapacityBean;

@RequestScoped
@Named
public class McdsCapacityBean extends CapacityBean implements Serializable
{
  private static final long serialVersionUID = 1L;

  public boolean isHasTinyScreen()
  {
    return getWidth() < 240;
  }
  
  public boolean isSimplePhone()
  {
    return getWidth() < 320;
  }
  
  public int getTopImageIndex()
  {
    if(!isSimplePhone() || isAndroid())
    {
      // normal stylesheet
      return ((getImageWidth() - 26) / 48) * 2;
    }
    else
    {
      // simple stylesheet
      return ((getImageWidth() - 4) / 48) * 2;
    }
  }
  
  public boolean isAlwaysShowImages()
  {
    return getHeight() >= 320;
  }
}
