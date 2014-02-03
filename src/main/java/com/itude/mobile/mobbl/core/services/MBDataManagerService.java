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
package com.itude.mobile.mobbl.core.services;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

import javax.enterprise.inject.Alternative;

import com.itude.mobile.mobbl.core.model.MBDocument;
import com.itude.mobile.mobbl.core.services.datamanager.MBDataHandler;
import com.itude.mobile.mobbl.core.services.datamanager.handlers.MBDocumentOperation;
import com.itude.mobile.mobbl.core.services.datamanager.handlers.MBDocumentOperationDelegate;
import com.itude.mobile.mobbl.core.services.datamanager.handlers.MBMemoryDataHandler;
import com.itude.mobile.mobbl.core.services.datamanager.handlers.MBRESTServiceDataHandler;
import com.itude.mobile.mobbl.core.services.datamanager.handlers.MBResourceDataHandler;
import com.itude.mobile.mobbl.core.services.datamanager.handlers.MBSystemDataHandler;
import com.itude.mobile.mobbl.core.services.exceptions.MBNoDataManagerException;

/** 
 * Service Class for loading and storing MBDocument instances.
 * The MBDataManagerService handles MBDocument instantiation and storage. Depending on the options defined in the configuration and endpoints files it (and the MBDataHandlers it uses) also handles:
 * <ul>
 * <li>caching</li>
 * <li>background threading and callback on completion</li>
 * <li>concurrent requests</li>
 * <li>network errors</li>
 * <li>reachability</li>
 * <li>cancellable network requests</li>
 * </ul>
 * Every MBDocument is associated with a specific MBDataHandler in the document definition (see MBDocumentDefinition). 
 * When accessing documents using for instance loadDocument,  its associated DataHandler is used to perform the actual operation. 
 * For this to work, the name of the DataHandler must be registered using registerDataHandler(withName) first.
 */
@Alternative
public class MBDataManagerService implements Serializable
{
  private static final long                serialVersionUID      = 1L;

  public static final String               DATA_HANDLER_MEMORY   = "MBMemoryDataHandler";
  public static final String               DATA_HANDLER_FILE     = "MBFileDataHandler";
  public static final String               DATA_HANDLER_SYSTEM   = "MBSystemDataHandler";
  public static final String               DATA_HANDLER_WS_REST  = "MBRESTServiceDataHandler";
  public static final String               DATA_HANDLER_RESOURCE = "MBResourceDataHandler";

  private static MBDataManagerService      _instance             = null;

  private final Map<String, MBDataHandler> _registeredHandlers;

  private MBDataManagerService()
  {
    _registeredHandlers = new Hashtable<String, MBDataHandler>();

    registerDataHandler(new MBMemoryDataHandler(), DATA_HANDLER_FILE);
    registerDataHandler(new MBSystemDataHandler(), DATA_HANDLER_SYSTEM);
    registerDataHandler(new MBMemoryDataHandler(), DATA_HANDLER_MEMORY);
    registerDataHandler(new MBRESTServiceDataHandler(), DATA_HANDLER_WS_REST);
    registerDataHandler(new MBResourceDataHandler(), DATA_HANDLER_RESOURCE);
  }

  public static MBDataManagerService getInstance()
  {
    if (_instance == null)
    {
      _instance = new MBDataManagerService();
    }

    return _instance;
  }

  private MBDocumentOperation getLoaderForDocumentName(String documentName, MBDocument arguments)
  {
    return new MBDocumentOperation(getHandlerForDocument(documentName), documentName, arguments);
  }

  private MBDataHandler getHandlerForDocument(String documentName)
  {
    String dataManagerName = MBMetadataService.getInstance().getDefinitionForDocumentName(documentName).getDataManager();

    MBDataHandler handler = _registeredHandlers.get(dataManagerName);
    if (handler == null)
    {
      String msg = "No datamanager " + dataManagerName + " found for document " + documentName;
      throw new MBNoDataManagerException(msg);
    }
    return handler;
  }

  public MBDocument loadDocument(String documentName)
  {
    return getLoaderForDocumentName(documentName, null).load();
  }

  public MBDocument loadDocument(String documentName, MBDocument args)
  {
    return getLoaderForDocumentName(documentName, args).load();
  }

  public void loadDocument(String documentName, MBDocumentOperationDelegate delegate)
  {
    MBDocumentOperation loader = getLoaderForDocumentName(documentName, null);
    loader.setDelegate(delegate);
    Thread thread = new Thread(loader);
    thread.start();
  }

  public void loadDocument(String documentName, MBDocument args, MBDocumentOperationDelegate delegate)
  {
    MBDocumentOperation loader = getLoaderForDocumentName(documentName, args);
    loader.setDelegate(delegate);
    Thread thread = new Thread(loader);
    thread.start();
  }

  public void storeDocument(MBDocument document)
  {
    getHandlerForDocument(document.getName()).storeDocument(document);

  }

  public void storeDocument(MBDocument document, MBDocumentOperationDelegate delegate, Object resultSelector, Object errorSelector)
  {
    MBDocumentOperation storer = new MBDocumentOperation(getHandlerForDocument(document.getName()), document);
    storer.setDelegate(delegate);
    Thread thread = new Thread(storer);
    thread.start();
  }

  public void registerDataHandler(MBDataHandler handler, String name)
  {
    _registeredHandlers.put(name, handler);
  }

}
