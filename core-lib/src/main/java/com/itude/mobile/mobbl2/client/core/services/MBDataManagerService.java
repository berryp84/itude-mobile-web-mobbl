package com.itude.mobile.mobbl2.client.core.services;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

import javax.enterprise.inject.Alternative;

import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBDocumentDefinition;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.services.datamanager.MBDataHandler;
import com.itude.mobile.mobbl2.client.core.services.datamanager.handlers.MBDocumentOperation;
import com.itude.mobile.mobbl2.client.core.services.datamanager.handlers.MBDocumentOperationDelegate;
import com.itude.mobile.mobbl2.client.core.services.datamanager.handlers.MBMemoryDataHandler;
import com.itude.mobile.mobbl2.client.core.services.datamanager.handlers.MBMobbl1ServerDataHandler;
import com.itude.mobile.mobbl2.client.core.services.datamanager.handlers.MBRESTServiceDataHandler;
import com.itude.mobile.mobbl2.client.core.services.datamanager.handlers.MBResourceDataHandler;
import com.itude.mobile.mobbl2.client.core.services.datamanager.handlers.MBSystemDataHandler;
import com.itude.mobile.mobbl2.client.core.services.exceptions.MBNoDataManagerException;
import com.itude.mobile.template.jsf.EnvironmentForm;

@Alternative
public class MBDataManagerService implements Serializable
{
  private static final long                serialVersionUID      = 1L;

  public static final String               DATA_HANDLER_MEMORY   = "MBMemoryDataHandler";
  public static final String               DATA_HANDLER_FILE     = "MBFileDataHandler";
  public static final String               DATA_HANDLER_SYSTEM   = "MBSystemDataHandler";
  public static final String               DATA_HANDLER_WS_REST  = "MBRESTServiceDataHandler";
  public static final String               DATA_HANDLER_WS_MOBBL = "MBMobbl1ServerDataHandler";
  public static final String               DATA_HANDLER_RESOURCE = "MBResourceDataHandler";

  private static MBDataManagerService      _instance             = null;

  private final Map<String, MBDataHandler> _registeredHandlers;

  private MBDataManagerService()
  {
    _registeredHandlers = new Hashtable<String, MBDataHandler>();

    // TODO: these might need to be persisted somewhere
    registerDataHandler(new MBMemoryDataHandler(), DATA_HANDLER_FILE);
    registerDataHandler(new MBSystemDataHandler("applicationproperties_" + EnvironmentForm.getStaticEnvironment() + ".xml"), DATA_HANDLER_SYSTEM);
    registerDataHandler(new MBMemoryDataHandler(), DATA_HANDLER_MEMORY);
    registerDataHandler(new MBRESTServiceDataHandler(), DATA_HANDLER_WS_REST);
    registerDataHandler(new MBMobbl1ServerDataHandler(), DATA_HANDLER_WS_MOBBL);
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

  private MBDocument createDocument(String documentName)
  {
    MBDocumentDefinition def = MBMetadataService.getInstance().getDefinitionForDocumentName(documentName);
    return new MBDocument(def);
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
