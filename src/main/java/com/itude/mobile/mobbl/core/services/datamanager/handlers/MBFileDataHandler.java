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
package com.itude.mobile.mobbl.core.services.datamanager.handlers;

import org.apache.log4j.Logger;

import com.itude.mobile.mobbl.core.configuration.mvc.MBDocumentDefinition;
import com.itude.mobile.mobbl.core.model.MBDocument;
import com.itude.mobile.mobbl.core.model.MBDocumentFactory;
import com.itude.mobile.mobbl.core.services.MBMetadataService;
import com.itude.mobile.mobbl.core.services.datamanager.MBDataHandlerBase;
import com.itude.mobile.mobbl.core.util.DataUtil;
import com.itude.mobile.mobbl.core.util.FileUtil;

public class MBFileDataHandler extends MBDataHandlerBase
{
  private static final Logger LOGGER = Logger.getLogger(MBFileDataHandler.class);

  @Override
  public MBDocument loadDocument(String documentName)
  {

    LOGGER.debug("MBFileDataHandler.loadDocument: " + documentName);
    String fileName = determineFileName(documentName);
    MBDocumentDefinition docDef = MBMetadataService.getInstance().getDefinitionForDocumentName(documentName);
    byte[] data = DataUtil.getInstance().readResource(fileName);

    if (data == null) return null;
    else return MBDocumentFactory.getInstance().getDocumentWithData(data, MBDocumentFactory.PARSER_XML, docDef);
  }

  @Override
  public void storeDocument(MBDocument document)
  {

    if (document != null)
    {
      String fileName = determineFileName(document.getName());
      String xml = document.asXmlWithLevel(0);

      LOGGER.debug("Writing document " + document.getName() + " to " + fileName);

      try
      {
        // TODO: parameterize character encoding.
        FileUtil.getInstance().writeToFile(xml.getBytes(), fileName, "UTF-8");
      }
      catch (Exception e)
      {
        LOGGER.warn("MBFileDataHandler.storeDocument: Error writing document " + document.getName() + " to " + fileName, e);
      }
    }
  }

  private String determineFileName(String documentName)
  {
    return "documents/" + documentName + ".xml";

  }
}
