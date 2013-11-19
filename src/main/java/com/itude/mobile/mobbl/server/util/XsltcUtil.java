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
package com.itude.mobile.mobbl.server.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

/**
 * XsltcUtil is a helper class to transform xml documents using xslt 2.0.
 * Stylesheets are compiled and cached
 * 
 * Usage: 
 * XsltcUtil xsltcUtil = XstlcUtil.getInstance();
 * ..
 * String xsltName = "myXsl";
 * String xsltContents = ... 
 * OutputStream out = xsltcUtil.transform(inXmlStream, xsltName, xsltContents, "UTF-8");
 * ..
 * 
 * And when the stylesheet has changed:
 * xsltcUtil.flushTemplate(xsltName);
 * 
 * or
 * 
 * xsltcUtil.flushTemplates();
 * 
 */
public class XsltcUtil
{

  static XsltcUtil            xsltcUtil;
  //String key = "javax.xml.transform.TransformerFactory";
  //String value = "org.apache.xalan.xsltc.trax.TransformerFactoryImpl";
  //Properties props = System.getProperties();
  //props.put(key, value);
  //System.setProperties(props);
  SAXTransformerFactory       stf;
  HashMap<String, Templates>  templateMap = new HashMap<String, Templates>();

  private static final Logger logger      = Logger.getLogger(XsltcUtil.class.getName());

  private XsltcUtil()
  {
    stf = (SAXTransformerFactory) TransformerFactory.newInstance();
  }

  public static XsltcUtil getInstance()
  {

    if (xsltcUtil == null)
    {
      xsltcUtil = new XsltcUtil();
    }
    return xsltcUtil;
  }

  public synchronized void flushTemplate(String xslName)
  {
    templateMap.remove(xslName);
  }

  public synchronized void flushTemplates()
  {
    templateMap.clear();
  }

  private synchronized Templates getTemplates(String name, String xsltContents)
  {
    Templates t = templateMap.get(name);
    if (t == null)
    {
      try
      {
        ByteArrayInputStream xsltStream = new ByteArrayInputStream(xsltContents.getBytes());
        t = stf.newTemplates(new StreamSource(xsltStream));
        templateMap.put(name, t);
      }
      catch (TransformerConfigurationException e)
      {
        logger.error(e.getMessage(), e);
      }
    }
    return t;
  }

  /***
   * 
   * @param inStream
   * @param xsltName
   * @param xsltContents
   * @param encoding may not be null
   * @return
   */
  public ByteArrayOutputStream transform(InputStream inStream, String xsltName, String xsltContents, String encoding)
  {
    return transform(inStream, xsltName, xsltContents, encoding, null);
  }

  public ByteArrayOutputStream transform(InputStream inStream, String xsltName, String xsltContents, String encoding,
                                         HashMap<String, String> parameters)
  {
    logger.debug("XsltcUtil.transform()");
    if (encoding == null) throw new IllegalArgumentException("XsltcUtil.transform(): encoding may not be null");

    ByteArrayOutputStream outBytes = new ByteArrayOutputStream();

    try
    {
      Transformer t = getTemplates(xsltName, xsltContents).newTransformer();
      t.setOutputProperty(OutputKeys.ENCODING, encoding);
      if (parameters != null)
      {
        Iterator<Entry<String, String>> iterator = parameters.entrySet().iterator();
        while (iterator.hasNext())
        {
          Entry<String, String> entry = iterator.next();
          t.setParameter(entry.getKey(), entry.getValue());
        }
      }
      t.transform(new StreamSource(inStream), new StreamResult(outBytes));
    }
    catch (Exception e)
    {
      logger.error(e.getMessage(), e);
    }

    return outBytes;
  }

}
