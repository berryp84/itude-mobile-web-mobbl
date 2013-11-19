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
package com.itude.mobile.mobbl2.client.core.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.itude.mobile.mobbl2.client.core.configuration.MBDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBAttributeDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBDocumentDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBElementDefinition;
import com.itude.mobile.mobbl2.client.core.model.exceptions.MBParseErrorException;

public class MBJsonDocumentParser
{

  public static MBDocument getDocumentWithData(byte[] data, MBDocumentDefinition definition)
  {
    try
    {
      String string = new String(data, "UTF-8");
      MBDocument result = MBJsonDocumentParser.getDocumentWithString(string, definition);

      return result;
    }
    catch (Exception e)
    {
      String message = "Error parsing document " + definition.getName() + " : " + e.getMessage();
      throw new MBParseErrorException(message, e);
    }

  }

  public static MBDocument getDocumentWithString(String string, MBDocumentDefinition definition)
  {
    MBJsonDocumentParser parser = new MBJsonDocumentParser();
    MBDocument result = parser.parseJsonString(string, definition);

    return result;
  }

  public MBDocument parseJsonString(String jsonString, MBDocumentDefinition definition)
  {

    try
    {
      JSONObject jsonDoc = new JSONObject(jsonString);
      MBDocument document = new MBDocument(definition);
      // ignore the first Element, use its child as the root
      JSONObject jsonRoot = jsonDoc.optJSONObject(jsonDoc.names().getString(0));

      // kick off recursive construction, starting with root element
      parseJsonValue(jsonRoot, definition, document);

      return document;
    }
    catch (Exception e)
    {
      String message = "Error parsing document " + definition.getName() + " : " + e.getMessage();
      throw new MBParseErrorException(message, e);
    }

  }

  public void parseJsonValue(Object jsonValue, MBDefinition definition, MBElementContainer element)
  {

    if (jsonValue instanceof JSONObject)
    {
      JSONObject jsonObject = (JSONObject) jsonValue;
      List<MBElementDefinition> elements = definition.getChildElements();

      for (MBElementDefinition childDefinition : elements)
      {

        try
        {
          if (!jsonObject.has(childDefinition.getName()))
          {
            element.addElement(childDefinition.createElement());
          }
          else
          {

            Object jsonChild = jsonObject.get(childDefinition.getName());

            if (!(jsonChild instanceof JSONObject) && !(jsonChild instanceof JSONArray))
            {
              MBElement childElement = childDefinition.createElement();

              if (jsonChild instanceof String)
              {
                childElement.setBodyText((String) jsonChild);
              }
              if (jsonChild instanceof Integer)
              {
                childElement.setBodyText(Integer.toString((Integer) jsonChild));
              }
              if (jsonChild instanceof Double)
              {
                childElement.setBodyText(Double.toString((Double) jsonChild));
              }
              if (jsonChild instanceof Long)
              {
                childElement.setBodyText(Long.toString((Long) jsonChild));
              }

              element.addElement(childElement);
            }

            if (jsonChild instanceof JSONObject)
            {
              MBElement childElement = childDefinition.createElement();
              element.addElement(childElement);
              parseJsonValue((jsonChild), childDefinition, childElement);
            }

            if (jsonChild instanceof JSONArray)
            {

              for (int i = 0; i < ((JSONArray) jsonChild).length(); i++)
              {
                MBElement childElement = childDefinition.createElement();
                element.addElement(childElement);
                parseJsonValue(((JSONArray) jsonChild).get(i), childDefinition, childElement);
              }
            }
          }
        }
        catch (Exception e)
        {
          String message = "Error parsing value : " + childDefinition.getName() + " of document " + definition.getName() + " : "
                           + e.getMessage();
          throw new MBParseErrorException(message, e);
        }

      }

    }

    // get attributes
    if (definition instanceof MBElementDefinition && element instanceof MBElement)
    {

      try
      {
        for (MBAttributeDefinition attributeDefinition : ((MBElementDefinition) definition).getAttributes())
        {
          JSONObject jsonObject = (JSONObject) jsonValue;
          Object attributeValue = JSONObject.NULL;
          if (jsonObject.has(attributeDefinition.getName())) attributeValue = jsonObject.get(attributeDefinition.getName());

          if (attributeValue == JSONObject.NULL) attributeValue = attributeDefinition.getDefaultValue();
          else attributeValue = attributeValue.toString();

          ((MBElement) element).setAttributeValue((String) attributeValue, attributeDefinition.getName());

        }
      }
      catch (Exception e)
      {
        String message = "Error parsing document " + definition.getName() + " : " + e.getMessage();
        throw new MBParseErrorException(message, e);
      }

    }

  }

}
