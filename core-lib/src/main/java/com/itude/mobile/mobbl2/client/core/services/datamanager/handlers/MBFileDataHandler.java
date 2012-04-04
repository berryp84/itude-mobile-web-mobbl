package com.itude.mobile.mobbl2.client.core.services.datamanager.handlers;

import org.apache.log4j.Logger;

import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBDocumentDefinition;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.model.MBDocumentFactory;
import com.itude.mobile.mobbl2.client.core.services.MBMetadataService;
import com.itude.mobile.mobbl2.client.core.services.datamanager.MBDataHandlerBase;
import com.itude.mobile.mobbl2.client.core.util.DataUtil;
import com.itude.mobile.mobbl2.client.core.util.FileUtil;

public class MBFileDataHandler extends MBDataHandlerBase
{
  private static final Logger _log = Logger.getLogger(MBFileDataHandler.class);

  @Override
  public MBDocument loadDocument(String documentName)
  {

    _log.debug("MBFileDataHandler.loadDocument: " + documentName);
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

      _log.debug("Writing document " + document.getName() + " to " + fileName);

      try
      {
        // TODO: parameterize character encoding.
        FileUtil.getInstance().writeToFile(xml.getBytes(), fileName, "UTF-8");
      }
      catch (Exception e)
      {
        _log.warn("MBFileDataHandler.storeDocument: Error writing document " + document.getName() + " to " + fileName, e);
      }
    }
  }

  private String determineFileName(String documentName)
  {
    return "documents/" + documentName + ".xml";

  }
}
