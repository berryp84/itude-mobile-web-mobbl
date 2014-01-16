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
import com.itude.mobile.mobbl.core.util.exceptions.MBDataParsingException;

public class MBMemoryDataHandler extends MBDataHandlerBase
{
  private static final Logger LOGGER = Logger.getLogger(MBMemoryDataHandler.class);

  public MBMemoryDataHandler()
  {
    super();
  }

  @Override
  public MBDocument loadDocument(String documentName)
  {
    // TODO: disabled dictonary for now
    //    MBDocument doc = _dictionary.get(documentName);
    //    if (doc == null)
    //    {
    // Not yet in the store; handle default construction of the document using a file as template
    String fileName = "documents/" + documentName + ".xml";
    byte[] data = null;
    try
    {
      data = DataUtil.getInstance().readResource(fileName);
    }
    catch (MBDataParsingException e)
    {
      LOGGER.debug("Unable to find file " + fileName + " in assets");
    }
    MBDocumentDefinition docDef = MBMetadataService.getInstance().getDefinitionForDocumentName(documentName);
    return MBDocumentFactory.getInstance().getDocumentWithData(data, MBDocumentFactory.PARSER_XML, docDef);
    //    }
    //    return doc;
  }

  @Override
  public void storeDocument(MBDocument document)
  {
    //    _dictionary.put(document.getName(), document);
  }

}
