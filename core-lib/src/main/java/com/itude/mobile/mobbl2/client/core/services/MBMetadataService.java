package com.itude.mobile.mobbl2.client.core.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Alternative;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBActionDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBConfigurationDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBDialogDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBDocumentDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBDomainDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBMvcConfigurationParser;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBOutcomeDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBPageDefinition;
import com.itude.mobile.mobbl2.client.core.services.exceptions.MBActionNotDefinedException;
import com.itude.mobile.mobbl2.client.core.services.exceptions.MBDialogNotDefinedException;
import com.itude.mobile.mobbl2.client.core.services.exceptions.MBDocumentNotDefinedException;
import com.itude.mobile.mobbl2.client.core.services.exceptions.MBDomainNotDefinedException;
import com.itude.mobile.mobbl2.client.core.services.exceptions.MBPageNotDefinedException;

@Alternative
public class MBMetadataService implements Serializable
{
  private static final long               serialVersionUID = 1L;

  private static final Logger             LOGGER             = Logger.getLogger(MBMetadataService.class);

  private final MBConfigurationDefinition _cfg;

  private static MBMetadataService        _instance;
  private static final String             CONFIG_NAME      = "config";

  private Map<String, Object> getRequestMap()
  {
    return FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
  }

  private MBMetadataService()
  {
    MBMvcConfigurationParser parser = new MBMvcConfigurationParser();
    _cfg = (MBConfigurationDefinition) parser.parseData(MBResourceService.getInstance().getResourceByID(CONFIG_NAME), CONFIG_NAME);
  }

  public static MBMetadataService getInstance()
  {
    if (_instance == null)
    {
      _instance = new MBMetadataService();
    }

    return _instance;
  }

  public MBDomainDefinition getDefinitionForDomainName(String domainName)
  {
    return getDefinitionForDomainName(domainName, true);
  }

  public MBDomainDefinition getDefinitionForDomainName(String domainName, boolean doThrow)
  {
    // Pjotter: fun! ^_^ The domain definitions are modified in some controllers, and we don't really
    // want to do this over the entire application. While this can be done more nicely, it would
    // cause major differences between the android and the JSF applications.
    // Therefore, the definitions are transparently stored in the current request scope when
    // requested.

    String key = "DOMAINDEFINITION_" + domainName;
    MBDomainDefinition domDef;
    domDef = (MBDomainDefinition) getRequestMap().get(key);
    if (domDef == null)
    {
      domDef = _cfg.getDefinitionForDomainName(domainName);
      if (domDef != null) getRequestMap().put(key, domDef);
    }
    if (domDef == null && doThrow)
    {
      String message = "Domain with name " + domainName + " not defined";
      throw new MBDomainNotDefinedException(message);
    }

    return domDef;
  }

  public MBPageDefinition getDefinitionForPageName(String pageName)
  {
    return getDefinitionForPageName(pageName, true);
  }

  public MBPageDefinition getDefinitionForPageName(String pageName, boolean doThrow)
  {
    MBPageDefinition pageDef = _cfg.getDefinitionForPageName(pageName);
    if (pageDef == null && doThrow)
    {
      String message = "Page with name " + pageName + " not defined";
      throw new MBPageNotDefinedException(message);
    }

    return pageDef;
  }

  public MBActionDefinition getDefinitionForActionName(String actionName)
  {
    return getDefinitionForActionName(actionName, true);
  }

  public MBActionDefinition getDefinitionForActionName(String actionName, boolean doThrow)
  {
    MBActionDefinition actionDef = _cfg.getDefinitionForActionName(actionName);
    if (actionDef == null && doThrow)
    {
      String message = "Action with name " + actionName + " not defined";
      throw new MBActionNotDefinedException(message);
    }

    return actionDef;
  }

  public MBDocumentDefinition getDefinitionForDocumentName(String documentName)
  {
    return getDefinitionForDocumentName(documentName, true);
  }

  public MBDocumentDefinition getDefinitionForDocumentName(String documentName, boolean doThrow)
  {
    MBDocumentDefinition docDef = _cfg.getDefinitionForDocumentName(documentName);
    if (docDef == null && doThrow)
    {
      String message = "Document with name " + documentName + " not defined";
      throw new MBDocumentNotDefinedException(message);
    }

    return docDef;
  }

  public MBDialogDefinition getDefinitionForDialogName(String dialogName)
  {
    return getDefinitionForDialogName(dialogName, true);
  }

  public MBDialogDefinition getDefinitionForDialogName(String dialogName, boolean doThrow)
  {
    MBDialogDefinition dialogDef = _cfg.getDefinitionForDialogName(dialogName);
    if (dialogDef == null && doThrow)
    {
      String message = "Dialog with name " + dialogName + " not defined";
      throw new MBDialogNotDefinedException(message);
    }

    return dialogDef;
  }

  public MBDialogDefinition getFirstDialogDefinition()
  {
    return _cfg.getFirstDialogDefinition();
  }

  public List<MBDialogDefinition> getDialogs()
  {
    return new ArrayList<MBDialogDefinition>(_cfg.getDialogs().values());
  }

  //For now do not raise an exception if an outcome is not defined
  public List<MBOutcomeDefinition> getOutcomeDefinitionsForOrigin(String originName)
  {
    ArrayList<MBOutcomeDefinition> list = (ArrayList<MBOutcomeDefinition>) _cfg.getOutcomeDefinitionsForOrigin(originName);
    if (list == null || list.size() <= 0)
    {
      LOGGER.warn("WARNING No outcomes defined for origin " + originName + " ");
    }

    return list;
  }

  public List<MBOutcomeDefinition> getOutcomeDefinitionsForOrigin(String originName, String outcomeName)
  {
    return getOutcomeDefinitionsForOrigin(originName, outcomeName, true);
  }

  public List<MBOutcomeDefinition> getOutcomeDefinitionsForOrigin(String originName, String outcomeName, boolean doThrow)
  {
    ArrayList<MBOutcomeDefinition> outcomeDefs = (ArrayList<MBOutcomeDefinition>) _cfg.getOutcomeDefinitionsForOrigin(originName,
                                                                                                                      outcomeName);
    if (outcomeDefs.size() == 0 && doThrow)
    {
      String message = "Outcome with originName=" + originName + " outcomeName=" + outcomeName + " not defined";
      throw new MBActionNotDefinedException(message);
    }
    return outcomeDefs;
  }

  public MBConfigurationDefinition getConfiguration()
  {
    return _cfg;
  }

}
