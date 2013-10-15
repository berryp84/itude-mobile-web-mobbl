/*
 * (C) Copyright ItudeMobile.
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
package com.itude.mobile.mobbl.server.util;

import java.io.ByteArrayInputStream;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathConstants;

import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XpathUtil
{
  private static final Logger logger = Logger.getLogger(XpathUtil.class.getName());

  public static String getValueForKey(byte[] document, String xPath)
  {
    Object result = doEvaluate(document, xPath, XPathConstants.STRING);
    if (result instanceof String && ((String) result).length() > 0) return (String) result;

    return null;
  }

  public static NodeList getNodeList(byte[] document, String xPath)
  {
    Object result = doEvaluate(document, xPath, XPathConstants.NODESET);
    if (result instanceof NodeList && ((NodeList) result).getLength() > 0) return (NodeList) result;

    return null;
  }

  public static String getValueForKey(Object item, String xPath)
  {
    String result = doEvaluate(item, xPath);
    if (result != null && result.length() > 0) return result;

    return null;
  }

  private static Object doEvaluate(byte[] document, String xPath, QName returnType)
  {
    // 1. Instantiate an XPathFactory.
    javax.xml.xpath.XPathFactory factory = javax.xml.xpath.XPathFactory.newInstance();

    // 2. Use the XPathFactory to create a new XPath object
    javax.xml.xpath.XPath xpath = factory.newXPath();

    // 3. Compile an XPath string into an XPathExpression
    javax.xml.xpath.XPathExpression expression;
    try
    {
      InputSource inSource = new InputSource(new ByteArrayInputStream(document));
      expression = xpath.compile(xPath);
      return expression.evaluate(inSource, returnType);
    }
    catch (Exception e)
    {
      logger.error(e);
    }

    return null;
  }

  private static String doEvaluate(Object item, String xPath)
  {
    // 1. Instantiate an XPathFactory.
    javax.xml.xpath.XPathFactory factory = javax.xml.xpath.XPathFactory.newInstance();

    // 2. Use the XPathFactory to create a new XPath object
    javax.xml.xpath.XPath xpath = factory.newXPath();

    // 3. Compile an XPath string into an XPathExpression
    javax.xml.xpath.XPathExpression expression;
    try
    {
      expression = xpath.compile(xPath);
      return expression.evaluate(item);
    }
    catch (Exception e)
    {
      logger.error(e);
    }

    return null;
  }
}
