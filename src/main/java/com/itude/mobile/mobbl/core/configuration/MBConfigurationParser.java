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
package com.itude.mobile.mobbl.core.configuration;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.itude.mobile.mobbl.core.configuration.exceptions.MBUnknownElementException;
import com.itude.mobile.mobbl.core.configuration.mvc.MBActionDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBAttributeDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBDialogDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBDocumentDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBDomainDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBDomainValidatorDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBElementDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBOutcomeDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBPageDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBVariableDefinition;

public class MBConfigurationParser extends DefaultHandler
{
  private static final Logger LOGGER = Logger.getLogger(MBConfigurationParser.class);

  private Stack<MBDefinition> _stack;
  private StringBuilder        _characters;
  private String              _documentName;

  public String getDocumentName()
  {
    return _documentName;
  }

  public void setDocumentName(String documentName)
  {
    _documentName = documentName;
  }

  public MBDefinition parseData(byte[] data, String documentName)
  {
    _stack = new Stack<MBDefinition>();
    _characters = new StringBuilder();

    try
    {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      factory.setNamespaceAware(true);
      SAXParser parser = factory.newSAXParser();

      parser.parse(new ByteArrayInputStream(data), this);

      return _stack.peek();
    }
    catch (Exception e)
    {
      LOGGER.error("Unable to parse document " + documentName, e);
    }

    return null;
  }

  public void parser(Object parser, String string)
  {
  }

  public boolean processElement(String elementName, Map<String, String> attributeDict)
  {
    return false;
  }

  public void didProcessElement(String elementName)
  {
  }

  public boolean isConcreteElement(String element)
  {
    return false;
  }

  public boolean isIgnoredElement(String element)
  {
    return false;
  }

  public boolean checkAttributesForElement(String elementName, Map<String, String> attributes, List<String> valids)
  {
    boolean result = true;

    Iterator<String> keys = attributes.keySet().iterator();
    String nextKey = "";
    while (keys.hasNext())
    {
      nextKey = keys.next();
      if (!valids.contains(nextKey))
      {
        LOGGER.warn("****WARNING Invalid attribute " + nextKey + " for element " + elementName + " in document " + _documentName);
        result = false;
      }
    }

    return result;
  }

  public void notifyProcessed(MBDefinition definition)
  {
    getStack().peek().addChildElement(definition);
    getStack().push(definition);
  }

  public void notifyProcessed(MBVariableDefinition definition)
  {
    getStack().peek().addChildElement(definition);
    getStack().push(definition);
  }

  public void notifyProcessed(MBDocumentDefinition definition)
  {
    getStack().peek().addChildElement(definition);
    getStack().push(definition);
  }

  public void notifyProcessed(MBActionDefinition definition)
  {
    getStack().peek().addChildElement(definition);
    getStack().push(definition);
  }

  public void notifyProcessed(MBDomainValidatorDefinition definition)
  {
    getStack().peek().addChildElement(definition);
    getStack().push(definition);
  }

  public void notifyProcessed(MBElementDefinition definition)
  {
    getStack().peek().addChildElement(definition);
    getStack().push(definition);
  }

  public void notifyProcessed(MBAttributeDefinition definition)
  {
    getStack().peek().addChildElement(definition);
    getStack().push(definition);
  }

  public void notifyProcessed(MBDomainDefinition definition)
  {
    getStack().peek().addChildElement(definition);
    getStack().push(definition);
  }

  public void notifyProcessed(MBOutcomeDefinition definition)
  {
    getStack().peek().addChildElement(definition);
    getStack().push(definition);
  }

  public void notifyProcessed(MBPageDefinition definition)
  {
    getStack().peek().addChildElement(definition);
    getStack().push(definition);
  }

  public void notifyProcessed(MBDialogDefinition definition)
  {
    getStack().peek().addChildElement(definition);
    getStack().push(definition);
  }

  public Stack<MBDefinition> getStack()
  {
    if (_stack == null)
    {
      _stack = new Stack<MBDefinition>();
    }
    return _stack;
  }

  public String getCharacters()
  {
    return _characters.toString();
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException
  {
    _characters.append(ch, start, length);
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
  {
    _characters = new StringBuilder();

    if (isConcreteElement(localName))
    {
      HashMap<String, String> attributeValues = new HashMap<String, String>();
      for (int i = 0; i < attributes.getLength(); i++)
      {
        attributeValues.put(attributes.getLocalName(i), attributes.getValue(i));
      }

      if (!processElement(localName, attributeValues))
      {
        String message = "Document " + _documentName + ": Element " + localName + " not defined";
        throw new MBUnknownElementException(message);
      }

      _stack.peek().validateDefinition();
    }

  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException
  {

    if (!isIgnoredElement(localName))
    {
      if (!isConcreteElement(localName))
      {
        String message = "Document " + _documentName + ": Element " + localName + " not defined";
        throw new MBUnknownElementException(message);
      }

      didProcessElement(localName);
    }

  }

}
