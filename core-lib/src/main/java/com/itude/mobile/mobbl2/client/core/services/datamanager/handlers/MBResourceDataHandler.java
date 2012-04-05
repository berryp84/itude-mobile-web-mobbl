package com.itude.mobile.mobbl2.client.core.services.datamanager.handlers;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBDocumentDefinition;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.model.MBDocumentFactory;
import com.itude.mobile.mobbl2.client.core.services.MBMetadataService;
import com.itude.mobile.mobbl2.client.core.services.datamanager.MBDataHandlerBase;
import com.itude.mobile.mobbl2.client.core.util.DataUtil;

public class MBResourceDataHandler extends MBDataHandlerBase implements Serializable
{
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(MBResourceDataHandler.class);

  @Override
  public MBDocument loadDocument(String documentName)
  {
    LOGGER.debug("MBResourceDataHandler.loadDocument: " + documentName);
    String fileName = determineFileName(documentName);
    MBDocumentDefinition docDef = MBMetadataService.getInstance().getDefinitionForDocumentName(documentName);
    byte[] data = DataUtil.getInstance().readResource(fileName);

    if (data == null) return null;
    else return MBDocumentFactory.getInstance().getDocumentWithData(data, MBDocumentFactory.PARSER_XML, docDef);

  }

  private String determineFileName(String documentName)
  {
    return "documents/" + documentName + ".xml";

  }

}
