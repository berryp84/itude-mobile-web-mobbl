package com.itude.mobile.web.jsf;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

import com.itude.commons.exceptions.ItudeRuntimeException;
import com.itude.mobile.mobbl2.client.core.util.MobblEnvironment;

@RequestScoped
@Named
public class CapacityBean implements Serializable
{
  private static final Logger LOGGER           = Logger.getLogger(CapacityBean.class);

  private static final long   serialVersionUID = 1L;

  private int                 _width;
  private int                 _height;
  private int                 _imageWidth;
  private boolean             _android;
  private boolean             _webkit;
  private boolean             _blackBerry;
  private boolean             _iphone;
  private String              _brandName;
  private String              _modelName;

  @PostConstruct
  protected void init()
  {
    FacesContext ci = FacesContext.getCurrentInstance();
    ExternalContext ec = ci.getExternalContext();
    ServletContext sc = (ServletContext) ec.getContext();

    WURFLHolder wurflHolder = (WURFLHolder) sc.getAttribute(WURFLHolder.class.getName());

    WURFLManager wurfl = wurflHolder.getWURFLManager();

    HttpServletRequest hsr = (HttpServletRequest) ec.getRequest();
    String userAgent = hsr.getHeader("user-agent");
    Device device = wurfl.getDeviceForRequest(hsr);

    _brandName = device.getCapability("brand_name");
    _modelName = device.getCapability("model_name");
    //    
    LOGGER.debug("user agent: " + userAgent);
    LOGGER.debug("Device: " + _brandName + " " + _modelName + " (" + device.getId() + ")");

    // Set values in the request for statistics' sake
    hsr.setAttribute("device-brand", _brandName);
    hsr.setAttribute("device-model", _modelName);
    hsr.setAttribute("projectID", MobblEnvironment.getArtifactId());

    _imageWidth = Integer.parseInt(device.getCapability("max_image_width"));
    _width = Integer.parseInt(device.getCapability("resolution_width"));
    _height = Integer.parseInt(device.getCapability("resolution_height"));

    _webkit = userAgent.toLowerCase().contains("webkit");
    _android = userAgent.toLowerCase().contains("android");
    _blackBerry = device.getId().contains("blackberry");

    // this returns true also with iPod touch. This is intentional.
    _iphone = userAgent.toLowerCase().contains("iphone");
  }

  public String getHandsetName()
  {
    String encoding = "ISO-8859-1";

    try
    {
      // Dirty fix for Blackberry games
      return URLEncoder.encode(_brandName.replace("RIM", "") + " " + _modelName, encoding);
    }
    catch (UnsupportedEncodingException e)
    {
      throw new ItudeRuntimeException(e);
    }
  }

  public String format(String toFormat)
  {
    return toFormat.replace("DEVICE[brand_name]", _brandName).replace("DEVICE[model_name]", _modelName);
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

  public boolean isIphone()
  {
    return _iphone;
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
