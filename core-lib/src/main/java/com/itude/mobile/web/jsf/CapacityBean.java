package com.itude.mobile.web.jsf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.wurfl.core.Device;
import net.sourceforge.wurfl.core.WURFLHolder;
import net.sourceforge.wurfl.core.WURFLManager;

import org.apache.log4j.Logger;

@RequestScoped
@Named
public class CapacityBean implements Serializable
{
  private static final Logger _log             = Logger.getLogger(CapacityBean.class);

  private static final long   serialVersionUID = 1L;

  private int                 _width;
  private int                 _height;
  private int                 _imageWidth;
  private boolean             _android;
  private boolean             _webkit;
  private boolean             _blackBerry;
  private String _brandName;
  private String _modelName;

  @PostConstruct
  protected void init()
  {
    FacesContext ci = FacesContext.getCurrentInstance();
    ExternalContext ec = ci.getExternalContext();
    ServletContext sc = (ServletContext) ec.getContext();

    WURFLHolder wurflHolder = (WURFLHolder) sc.getAttribute("net.sourceforge.wurfl.core.WURFLHolder");

    WURFLManager wurfl = wurflHolder.getWURFLManager();

    HttpServletRequest hsr = (HttpServletRequest) ec.getRequest();
    String userAgent = hsr.getHeader("user-agent");
    Device device = wurfl.getDeviceForRequest(hsr);
    
    _brandName = device.getCapability("brand_name");
    _modelName = device.getCapability("model_name");
    
    _log.debug("user agent: " + userAgent);
    _log.debug("Device: " + _brandName + " " + _modelName + " (" + device.getId() + ")");

    hsr.setAttribute("device_brand", _brandName);
    hsr.setAttribute("device_model", _modelName);

    _imageWidth = Integer.parseInt(device.getCapability("max_image_width"));
    _width = Integer.parseInt(device.getCapability("resolution_width"));
    _height = Integer.parseInt(device.getCapability("resolution_height"));

    _webkit = userAgent.toLowerCase().contains("webkit");
    _android = userAgent.toLowerCase().contains("android");
    _blackBerry = device.getId().contains("blackberry");

  }

  public int getWidth()
  {
    return _width;
  }

  public int getHeight()
  {
    return _height;
  }
  
  public int getImageWidth()
  {
    return _imageWidth;
  }

  public boolean isWebkit()
  {
    return _webkit;
  }

  public boolean isBlackBerry()
  {
    return _blackBerry;
  }

  public boolean isAndroid()
  {
    return _android;
  }
  
  public String getBrandName()
  {
    return _brandName;
  }
  
  public String getModelName()
  {
    return _modelName;
  }

}
