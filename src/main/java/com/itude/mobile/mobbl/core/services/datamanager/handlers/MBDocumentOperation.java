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
import com.itude.mobile.mobbl.core.services.MBMetadataService;
import com.itude.mobile.mobbl.core.services.datamanager.MBDataHandler;

/**
 * Class containing document operations.
 */
public class MBDocumentOperation implements Runnable
{
  private static final Logger         LOGGER = Logger.getLogger(MBDocumentOperation.class);

  private MBDataHandler               _dataHandler;
  private String                      _documentName;
  private MBDocument                  _document;
  private MBDocument                  _arguments;
  private MBDocumentOperationDelegate _delegate;

  public MBDataHandler getDataHandler()
  {
    return _dataHandler;
  }

  public void setDataHandler(MBDataHandler dataHandler)
  {
    _dataHandler = dataHandler;
  }

  public String getDocumentName()
  {
    return _documentName;
  }

  public void setDocumentName(String documentName)
  {
    _documentName = documentName;
  }

  public MBDocument getArguments()
  {
    return _arguments;
  }

  public void setArguments(MBDocument arguments)
  {
    _arguments = arguments;
  }

  public MBDocument getDocument()
  {
    return _document;
  }

  public void setDocument(MBDocument document)
  {
    _document = document;
  }

  public MBDocumentOperation(MBDataHandler dataHandler, MBDocument document)
  {
    super();
    _dataHandler = dataHandler;
    _document = document;
  }

  public MBDocumentOperation(MBDataHandler dataHandler, String documentName, MBDocument arguments)
  {
    super();
    _dataHandler = dataHandler;
    _documentName = documentName;
    _arguments = arguments;
  }

  public void setDelegate(MBDocumentOperationDelegate delegate)
  {
    _delegate = delegate;
  }

  public MBDocumentOperationDelegate getDelegate()
  {
    return _delegate;
  }

  public MBDocument load()
  {
    long startTime = System.currentTimeMillis();

    MBDocument doc = null;
    if (this.getArguments() == null) doc = getDataHandler().loadDocument(getDocumentName());
    else
    {
      doc = getDataHandler().loadDocument(getDocumentName(), getArguments().clone());
    }

    if (doc == null)
    {
      MBDocumentDefinition docDef = MBMetadataService.getInstance().getDefinitionForDocumentName(getDocumentName());
      if (docDef.getAutoCreate()) doc = docDef.createDocument();
    }
    doc.setArgumentsUsed(getArguments());
    long ms = System.currentTimeMillis() - startTime;
    if (ms >= 10) LOGGER.debug("Loading of document " + getDocumentName() + " took " + ms + " ms");
    else LOGGER.trace("Loading of document " + getDocumentName() + " took " + ms + " ms");
    return doc;
  }

  public void store()
  {
    getDataHandler().storeDocument(getDocument());
  }

  @Override
  public void run()
  {
    try
    {
      if (_document == null)
      {
        MBDocument document = load();
        getDelegate().processResult(document);
      }
      else
      {
        store();
        getDelegate().processResult(null);
      }
    }
    catch (Exception e)
    {
      LOGGER.warn("Exception during Document Operation: " + e.getMessage(), e);
      getDelegate().processException(e);
    }
  }
}
