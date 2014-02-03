/*
 * (C) Copyright Itude Mobile B.V., The Netherlands
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itude.mobile.mobbl.core.controller.util;

import java.util.ArrayList;

import com.itude.mobile.mobbl.core.configuration.mvc.MBAttributeDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBElementDefinition;
import com.itude.mobile.mobbl.core.controller.MBAction;
import com.itude.mobile.mobbl.core.controller.MBOutcome;
import com.itude.mobile.mobbl.core.model.MBDocument;
import com.itude.mobile.mobbl.core.model.MBElement;
import com.itude.mobile.mobbl.core.services.MBDataManagerService;
import com.itude.mobile.mobbl.core.services.datamanager.handlers.exceptions.MBServerErrorException;

/**
 * Class that can do a form submission
 *
 */
public class MBFormSubmission implements MBAction
{
  final static String                            C_GENERIC_REQUEST = "MBGenericRequest";

  protected static final org.apache.log4j.Logger LOGGER            = org.apache.log4j.Logger.getLogger(MBFormSubmission.class);

  public MBOutcome execute(MBDocument document, String path)
  {

    validateDocument(document, path);

    MBOutcome outcome = null;
    String outcomeName;

    // get request name from document
    MBElement rootElement = (MBElement) ((ArrayList<?>) document.getElements().values().toArray()[0]).get(0);
    //TODO: check arrays are not empty else exceptions will be raised
    String requestName = rootElement.getName();

    // get outcome names from document
    String outcomeOK = rootElement.getValueForAttribute("outcomeOK");

    // set up generic request
    MBDocument request = MBDataManagerService.getInstance().loadDocument(C_GENERIC_REQUEST);
    request.setValue(requestName, "Request[0]/@name");

    // copy the attributes to the generic request
    MBElementDefinition elementDefinition = rootElement.getDefinition();
    ArrayList<MBAttributeDefinition> attributesArray = (ArrayList<MBAttributeDefinition>) elementDefinition.getAttributes();

    for (MBAttributeDefinition attributeDefinition : attributesArray)
    {

      // skip outcomeOK and
      String attributeName = attributeDefinition.getName();
      if (!"outcomeOK".equals(attributeName) && !"outcomeERROR".equals(attributeName))
      {
        String value = rootElement.getValueForAttribute(attributeName);
        setRequestParameter(value, attributeName, request);
      }
    }
    // Disabled logging as it displays the password of the user when he/she is changing his/her password
    //LOGGER.debug("REQUEST = " + request);

    // retrieve generic response
    MBDocument response = MBDataManagerService.getInstance().loadDocument("MBGenericResponse", request);

    LOGGER.debug("RESPONSE = " + response);

    String body = response.getValueForPath("Response[0]/@body");
    String error = response.getValueForPath("Response[0]/@error");

    // if error, throw error with errormessage
    if (error != null && error.trim().length() > 0)
    {
      LOGGER.error("Error returned by server: " + error);
      // use body rather than error since server-side puts error code in error and error message in body
      throw new MBServerErrorException(body);
    }

    // if success, add OK action to document and navigate to confirmation page
    else if (outcomeOK == null)
    {
      response.setValue(outcomeOK, "Response[0]/@outcomeName");

      outcomeName = "OUTCOME-MBFormSubmissionOK";
    }
    else
    {
      outcomeName = outcomeOK;
    }

    return new MBOutcome(outcomeName, response);
  }

  public void validateDocument(MBDocument document, String path)
  {
    // subclasses should implement this method to perform validation
  }

  void setRequestParameter(String value, String key, MBDocument doc)
  {
    MBElement request = doc.getValueForPath("Request[0]");
    MBElement parameter = request.createElementWithName("Parameter");
    parameter.setAttributeValue(key, "key");
    parameter.setAttributeValue(value, "value");
  }

  @Override
  public MBOutcome execute(MBDocument document, String path, String outcomeName)
  {
    return this.execute(document, path);
  }

}
