package com.itude.mobile.mobbl2.client.core.services.datamanager.handlers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.itude.mobile.mobbl2.client.core.configuration.webservices.MBEndPointDefinition;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.model.MBDocumentFactory;
import com.itude.mobile.mobbl2.client.core.model.MBElement;
import com.itude.mobile.mobbl2.client.core.services.MBDataManagerService;
import com.itude.mobile.mobbl2.client.core.util.DeviceUtil;
import com.itude.mobile.mobbl2.client.core.util.MBCacheManager;
import com.itude.mobile.mobbl2.client.core.util.MBProperties;
import com.itude.mobile.mobbl2.client.core.util.StringUtilities;

public class MBMobbl1ServerDataHandler extends MBRESTServiceDataHandler
{

  //
  //expects an argument Document of type MBMobbl1Request
  @Override
  public MBDocument loadDocument(String documentName, MBDocument doc)
  {

    boolean cacheable = false;

    // Look for any cached result. If there; return it
    MBEndPointDefinition endPoint = getEndPointForDocument(documentName);
    cacheable = endPoint.getCacheable();

    if (cacheable)
    {
      MBDocument result = MBCacheManager.documentForKey(doc.getUniqueId());
      if (result != null) return result;
    }

    String universeID = MBProperties.getInstance().getValueForProperty("mobblUniverseID");
    String UIDPrefix = MBProperties.getInstance().getValueForProperty("UIDPrefix");
    String UID = UIDPrefix + DeviceUtil.getInstance().getUniqueID();
    String secret = MBProperties.getInstance().getValueForProperty("mobblSecret");

    // package the incoming document in a StrayClient envelope
    String applicationID = (String) doc.getValueForPath("Request[0]/@name");
    MBDocument mobblDoc = getRequestDocumentForApplicationID(applicationID);
    MBElement mobblRequest = (MBElement) mobblDoc.getValueForPath("StrayClient[0]/SendDataDetails[0]/request[0]");

    //
    @SuppressWarnings({"unchecked"})
    List<MBElement> parameters = (List<MBElement>) doc.getValueForPath("Request[0]/Parameter");

    for (MBElement parameter : parameters)
    {
      MBElement mobblParameter = mobblRequest.createElementWithName("parameter");
      String key = parameter.getValueForAttribute("key");
      String value = parameter.getValueForAttribute("value");
      mobblParameter.setAttributeValue(key, "key");
      mobblParameter.setAttributeValue(value, "value");
      // subparameters
      for (MBElement subparameter : parameter.getElementsWithName("Subparameter"))
      {
        MBElement mobblSubparameter = mobblParameter.createElementWithName("subparameter");
        String subkey = subparameter.getValueForAttribute("key");
        String subvalue = subparameter.getValueForAttribute("value");
        mobblSubparameter.setAttributeValue(subkey, "key");
        mobblSubparameter.setAttributeValue(subvalue, "value");
      }

    }

    MBElement sendData = (MBElement) mobblDoc.getValueForPath("StrayClient[0]");

    // ======== Mobbl1 generic stuff =========
    Date currentDate = new Date();

    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String dateTime = formatter.format(currentDate);
    
    String messageID = StringUtilities.md5(dateTime + UID + secret);
    //
    sendData.setAttributeValue("http://straysystems.com/xsd/strayclient", "xmlns");
    sendData.setAttributeValue("SendData", "command");
    sendData.setAttributeValue(universeID, "universeID");
    sendData.setAttributeValue(dateTime, "dateTime");
    sendData.setAttributeValue(UID, "iPhoneUID");
    sendData.setAttributeValue(messageID, "messageID");
    setDocumentFactoryType(MBDocumentFactory.PARSER_MOBBL1);
    MBDocument result = super.loadDocument(documentName, mobblDoc);

    if (cacheable)
    {
      MBCacheManager.setDocument(result, doc.getUniqueId(), endPoint.getTtl());
    }
    return result;
  }

  public MBDocument getRequestDocumentForApplicationID(String applicationID)
  {
    MBDocument requestDoc = MBDataManagerService.getInstance().loadDocument("MBMobbl1Request");
    MBElement element = (MBElement) requestDoc.getValueForPath("StrayClient[0]");
    element.setAttributeValue(applicationID, "applicationID");
    return requestDoc;
  }

  public void setRequestParameter(String value, String key, MBDocument doc)
  {
    MBElement request = (MBElement) doc.getValueForPath("Request[0]");
    MBElement parameter = request.createElementWithName("Parameter");
    parameter.setAttributeValue(key, "key");
    parameter.setAttributeValue(value, "value");
  }

}
