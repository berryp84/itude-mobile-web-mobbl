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

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBDocumentDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBElementDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.exceptions.MBElementNotExpectedException;
import com.itude.mobile.mobbl2.client.core.model.exceptions.MBInvalidDocumentException;
import com.itude.mobile.mobbl2.client.core.model.exceptions.MBParseErrorException;
import com.itude.mobile.mobbl2.client.core.util.StringUtilities;

public class MBXmlDocumentParser extends DefaultHandler
{
  private static final Logger       LOGGER        = Logger.getLogger(MBXmlDocumentParser.class);

  private static final Pattern      NUMBERPATTERN = Pattern.compile("\\[[0-9]+\\]");

  private Stack<MBElementContainer> _stack;
  private Stack<String>             _pathStack;
  private MBDocumentDefinition      _definition;
  private StringBuilder             _characters;
  private String                    _rootElementName;
  private MBElementContainer        _rootElement;
  private boolean                   _copyRootAttributes;
  private HashSet<String>           _ignoredPaths;

  public static MBDocument getDocumentWithData(byte[] data, MBDocumentDefinition definition)
  {
    MBXmlDocumentParser documentParser = new MBXmlDocumentParser();
    MBDocument result = documentParser.parse(data, definition);

    return result;
  }

  public static void parseFragment(byte[] data, MBDocument document, String rootPath, boolean copyRootAttributes)
  {
    MBXmlDocumentParser documentParser = new MBXmlDocumentParser();
    documentParser.doParseFragment(data, document, rootPath, copyRootAttributes);
  }

  public MBDocument parse(byte[] data, MBDocumentDefinition definition)
  {
    if (data == null || data.length == 0)
    {
      return null;
    }

    MBDocument document = new MBDocument(definition);
    doParseFragment(data, document, null, false);

    return document;
  }

  private void doParseFragment(byte[] data, MBDocument document, String rootPath, boolean copyRootAttributes)
  {
    if (data != null)
    {
      try
      {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser parser = factory.newSAXParser();

        _stack = new Stack<MBElementContainer>();
        _pathStack = new Stack<String>();
        _definition = document.getDefinition();
        _characters = new StringBuilder();
        _copyRootAttributes = copyRootAttributes;
        _ignoredPaths = new HashSet<String>();

        if (rootPath != null)
        {
          List<String> parts = StringUtilities.splitPath(rootPath);
          for (String part : parts)
          {
            _pathStack.add(NUMBERPATTERN.matcher(part).replaceAll(""));
          }

          _rootElementName = _pathStack.peek();
          _rootElement = (MBElementContainer) document.getValueForPath(rootPath);
        }
        else
        {
          _rootElement = document;
          if (_definition.getRootElement() != null) _rootElementName = _definition.getRootElement();
          else _rootElementName = _definition.getName();
        }

        parser.parse(new ByteArrayInputStream(data), this);
      }
      catch (Exception e)
      {
        LOGGER.error("MBXmlDocumentParser.doParseFragment", e);
      }
    }
  }

  public String getCurrentPath()
  {
    String path = "";
    for (String part : _pathStack)
    {
      path += "/" + part;
    }
    return path;
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
  {
    MBElementContainer element = null;
    boolean copyAttributes = true;

    // check that we have the correct document type
    if (_stack.size() == 0)
    {

      if (!localName.equals(_rootElementName))
      {
        String message = "Error parsing document " + _definition.getName() + ": encountered an element with name " + localName
                         + " but expected " + _rootElementName;
        throw new MBInvalidDocumentException(message);
      }

      element = _rootElement;
      copyAttributes = _copyRootAttributes;
    }
    else if (isValidPath(getCurrentPath()))
    {
      _pathStack.add(localName);
      try
      {
        MBElementDefinition elemDef = _definition.getElementWithPath(getCurrentPath());
        element = new MBElement(elemDef);

        _stack.peek().addElement((MBElement) element);
      }
      catch (MBElementNotExpectedException e)
      {
        LOGGER.warn("Skipping element with name " + localName + ". Element is not in definition " + _definition.getName());
        _ignoredPaths.add(getCurrentPath());
      }
    }
    // add name to pathStack if a child element has the same name as an element that's already on an ignored path
    else if (localName.equals(_pathStack.peek()))
    {
      _pathStack.add(localName);
      _ignoredPaths.add(getCurrentPath());
    }

    if (element != null)
    {
      // Do not process elements that are not defined; so also check for nil definition
      if (copyAttributes && element.getDefinition() != null)
      {
        for (int i = 0; i < attributes.getLength(); i++)
        {
          ((MBElement) element).setAttributeValue(attributes.getValue(i), attributes.getLocalName(i), false);
        }
      }
      _stack.add(element);
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    if (_stack.size() > 1)
    {
      if (isValidPath(getCurrentPath()))
      {
        endValidElement(uri, localName, qName);
      }
      else if (localName.equals(_pathStack.peek()))
      {
        _pathStack.pop();
      }
    }

    _characters = new StringBuilder();
  }

  private void endValidElement(String uri, String localName, String qName) throws SAXException
  {
    String string = _characters.toString().trim();
    if (string.length() > 0)
    {
      if (_stack.peek() instanceof MBElement && ((MBElement) _stack.peek()).isValidAttribute("text()"))
      {
        ((MBElement) _stack.peek()).setAttributeValue(string, "text()");
      }
      else
      {
        LOGGER.warn("MBXmlDocumentParser.endElement: Text (" + string + ") specified in body of element " + localName
                    + " is ignored because the element has no text() attribute defined");
      }
    }

    _stack.pop();
    _pathStack.pop();
  }

  private boolean isValidPath(String path)
  {
    return !_ignoredPaths.contains(path);
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException
  {
    _characters.append(ch, start, length);
  }

  @Override
  public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException
  {
    // Ignore tabs, newlines and spaces
  }

  @Override
  public void error(SAXParseException e) throws SAXException
  {

    String message = "Error parsing document " + _definition.getName() + " at line " + e.getLineNumber() + " column " + e.getColumnNumber()
                     + ": " + e.getMessage();

    throw new MBParseErrorException(message);
  }

}
