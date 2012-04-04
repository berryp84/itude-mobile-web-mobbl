package com.itude.mobile.mobbl2.client.core.services.datamanager.handlers;

import org.apache.log4j.Logger;

import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBDocumentDefinition;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.model.MBDocumentFactory;
import com.itude.mobile.mobbl2.client.core.services.MBMetadataService;
import com.itude.mobile.mobbl2.client.core.services.datamanager.MBDataHandlerBase;
import com.itude.mobile.mobbl2.client.core.util.DataUtil;
import com.itude.mobile.mobbl2.client.core.util.exceptions.MBDataParsingException;

public class MBMemoryDataHandler extends MBDataHandlerBase
{
  private static final Logger           _log = Logger.getLogger(MBMemoryDataHandler.class);

//  private final Map<String, MBDocument> _dictionary;

  public MBMemoryDataHandler()
  {
    super();
//    _dictionary = new Hashtable<String, MBDocument>();
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
        _log.debug("Unable to find file " + fileName + " in assets");
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
