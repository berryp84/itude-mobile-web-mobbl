package com.itude.mobile.mobbl2.client.core.configuration.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itude.mobile.mobbl2.client.core.configuration.MBDefinition;
import com.itude.mobile.mobbl2.client.core.util.StringUtilities;

public class MBConfigurationDefinition extends MBDefinition
{

  private static final Logger                     _log                              = Logger.getLogger(MBConfigurationDefinition.class);

  public static final String                      DOC_SYSTEM_EMPTY                  = "MBEmpty";
  public static final String                      DOC_SYSTEM_LANGUAGE               = "MBBundle";
  public static final String                      DOC_SYSTEM_EXCEPTION              = "MBException";
  public static final String                      PATH_SYSTEM_EXCEPTION_NAME        = "/Exception[0]/@name";
  public static final String                      PATH_SYSTEM_EXCEPTION_DESCRIPTION = "/Exception[0]/@description";
  public static final String                      PATH_SYSTEM_EXCEPTION_ORIGIN      = "/Exception[0]/@origin";
  public static final String                      PATH_SYSTEM_EXCEPTION_OUTCOME     = "/Exception[0]/@outcome";
  public static final String                      PATH_SYSTEM_EXCEPTION_PATH        = "/Exception[0]/@path";

  public static final String                      DOC_SYSTEM_PROPERTIES             = "MBApplicationProperties";

  private final Map<String, MBDomainDefinition>   _domainTypes;
  private final Map<String, MBDocumentDefinition> _documentTypes;
  private final Map<String, MBActionDefinition>   _actionTypes;
  private final List<MBOutcomeDefinition>         _outcomeTypes;
  private final Map<String, MBPageDefinition>     _pageTypes;
  private final Map<String, MBDialogDefinition>   _dialogs;
  private MBDialogDefinition                      _firstDialog;

  public MBConfigurationDefinition()
  {
    _domainTypes = new HashMap<String, MBDomainDefinition>();
    _documentTypes = new HashMap<String, MBDocumentDefinition>();
    _actionTypes = new HashMap<String, MBActionDefinition>();
    _outcomeTypes = new ArrayList<MBOutcomeDefinition>();
    _dialogs = new HashMap<String, MBDialogDefinition>();
    _pageTypes = new HashMap<String, MBPageDefinition>();
  }

  public void addAll(MBConfigurationDefinition otherConfig)
  {
    for (MBDocumentDefinition documentDef : otherConfig.getDocuments().values())
    {
      addDocument(documentDef);
    }
    for (MBDomainDefinition domainDef : otherConfig.getDomains().values())
    {
      addDomain(domainDef);
    }
    for (MBActionDefinition actionDef : otherConfig.getActions().values())
    {
      addAction(actionDef);
    }
    for (MBOutcomeDefinition outcomeDef : otherConfig.getOutcomes())
    {
      addOutcome(outcomeDef);
    }
    for (MBDialogDefinition dialogDef : otherConfig.getDialogs().values())
    {
      addDialog(dialogDef);
    }
    for (MBPageDefinition pageDef : otherConfig.getPages().values())
    {
      addPage(pageDef);
    }
  }

  @Override
  public String asXmlWithLevel(int level)
  {
    String result = StringUtilities.getIndentStringWithLevel(level) + "<Configuration>\n";
    result += StringUtilities.getIndentStringWithLevel(level + 2) + "<Model>\n";
    result += StringUtilities.getIndentStringWithLevel(level + 4) + "<Domains>\n";
    for (MBDomainDefinition domain : _domainTypes.values())
    {
      result += domain.asXmlWithLevel(level + 6);
    }
    result += StringUtilities.getIndentStringWithLevel(level + 4) + "</Domains>\n";
    result += StringUtilities.getIndentStringWithLevel(level + 4) + "<Documents>\n";
    for (MBDocumentDefinition document : _documentTypes.values())
    {
      result += document.asXmlWithLevel(level + 6);
    }
    result += StringUtilities.getIndentStringWithLevel(level + 4) + "</Documents>\n";
    result += StringUtilities.getIndentStringWithLevel(level + 2) + "</Model>\n";

    result += StringUtilities.getIndentStringWithLevel(level + 2) + "<Controller>\n";
    result += StringUtilities.getIndentStringWithLevel(level + 4) + "<Actions>\n";
    for (MBActionDefinition acion : _actionTypes.values())
    {
      result += acion.asXmlWithLevel(level + 6);
    }
    result += StringUtilities.getIndentStringWithLevel(level + 4) + "</Actions>\n";
    result += StringUtilities.getIndentStringWithLevel(level + 4) + "<Wiring>\n";
    for (MBOutcomeDefinition outcome : _outcomeTypes)
    {
      result += outcome.asXmlWithLevel(level + 6);
    }
    result += StringUtilities.getIndentStringWithLevel(level + 4) + "</Wiring>\n";
    result += StringUtilities.getIndentStringWithLevel(level + 2) + "</Controller>\n";

    result += StringUtilities.getIndentStringWithLevel(level + 2) + "<View>\n";
    result += StringUtilities.getIndentStringWithLevel(level + 4) + "<Dialogs>\n";
    for (MBDialogDefinition dialog : _dialogs.values())
    {
      result += dialog.asXmlWithLevel(level + 6);
    }
    result += StringUtilities.getIndentStringWithLevel(level + 4) + "</Dialogs>\n";
    for (MBPageDefinition page : _pageTypes.values())
    {
      result += page.asXmlWithLevel(level + 4);
    }
    result += StringUtilities.getIndentStringWithLevel(level + 2) + "</View>\n";
    result += StringUtilities.getIndentStringWithLevel(level) + "</Configuration>\n";

    return result;
  }

  /*
   * @Override public void addChildElement(Object child) {
   * 
   * if (child instanceof MBDomainDefinition) { addDomain((MBDomainDefinition)
   * child); } if (child instanceof MBDocumentDefinition) {
   * addDocument((MBDocumentDefinition) child); } if (child instanceof
   * MBActionDefinition) { addAction((MBActionDefinition) child); } if (child
   * instanceof MBOutcomeDefinition) { addOutcome((MBOutcomeDefinition)
   * child); } if (child instanceof MBPageDefinition) {
   * addPage((MBPageDefinition) child); } if (child instanceof
   * MBDialogDefinition) { addDialog((MBDialogDefinition) child); }
   * 
   * }
   */
  @Override
  public void addChildElement(MBActionDefinition child)
  {
    addAction(child);
  }

  @Override
  public void addChildElement(MBDialogDefinition child)
  {
    addDialog(child);
  }

  @Override
  public void addChildElement(MBDocumentDefinition child)
  {
    addDocument(child);
  }

  @Override
  public void addChildElement(MBDomainDefinition child)
  {
    addDomain(child);
  }

  @Override
  public void addChildElement(MBOutcomeDefinition child)
  {
    addOutcome(child);
  }

  @Override
  public void addChildElement(MBPageDefinition child)
  {
    addPage(child);
  }

  public void addDomain(MBDomainDefinition domain)
  {
    if (_domainTypes.containsKey(domain))
    {
      _log.warn("Domain definition overridden: multiple definitions for domain with name " + domain.getName());
    }
    _domainTypes.put(domain.getName(), domain);
  }

  public void addDocument(MBDocumentDefinition document)
  {
    if (_documentTypes.containsKey(document))
    {
      _log.warn("Document definition overridden: multiple definitions for document with name " + document.getName());
    }
    _documentTypes.put(document.getName(), document);
  }

  public void addAction(MBActionDefinition action)
  {
    if (_actionTypes.containsKey(action))
    {
      _log.warn("Action definition overridden: multiple definitions for action with name " + action.getName());
    }
    _actionTypes.put(action.getName(), action);
  }

  public void addOutcome(MBOutcomeDefinition outcome)
  {
    _outcomeTypes.add(outcome);
  }

  public void addPage(MBPageDefinition page)
  {
    if (_pageTypes.containsKey(page))
    {
      _log.warn("Page definition overridden: multiple definitions for page with name " + page.getName());
    }
    _pageTypes.put(page.getName(), page);
  }

  public void addDialog(MBDialogDefinition dialog)
  {
    if (_dialogs.containsKey(dialog))
    {
      _log.warn("Dialog definition overridden: multiple definitions for action with name " + dialog.getName());
    }

    if (_firstDialog == null)
    {
      _firstDialog = dialog;
    }
    _dialogs.put(dialog.getName(), dialog);
  }

  public MBDomainDefinition getDefinitionForDomainName(String domainName)
  {
    return _domainTypes.get(domainName);
  }

  public MBPageDefinition getDefinitionForPageName(String name)
  {
    return _pageTypes.get(name);
  }

  public MBActionDefinition getDefinitionForActionName(String actionName)
  {
    return _actionTypes.get(actionName);
  }

  public MBDocumentDefinition getDefinitionForDocumentName(String documentName)
  {
    return _documentTypes.get(documentName);
  }

  public MBDialogDefinition getDefinitionForDialogName(String dialogName)
  {
    return _dialogs.get(dialogName);
  }

  public List<MBOutcomeDefinition> getOutcomeDefinitionsForOrigin(String originName)
  {
    List<MBOutcomeDefinition> result = new ArrayList<MBOutcomeDefinition>();

    for (MBOutcomeDefinition outcomeDef : _outcomeTypes)
    {
      if (outcomeDef.getOrigin().equals(originName) || outcomeDef.getOrigin().equals("*"))
      {
        result.add(outcomeDef);
      }
    }

    return result;
  }

  public List<MBOutcomeDefinition> getOutcomeDefinitionsForOrigin(String originName, String outcomeName)
  {
    List<MBOutcomeDefinition> result = new ArrayList<MBOutcomeDefinition>();

    // First look for specific matches
    for (MBOutcomeDefinition outcomeDef : _outcomeTypes)
    {
      if (outcomeDef.getOrigin().equals(originName) && outcomeDef.getName().equals(outcomeName))
      {
        result.add(outcomeDef);
      }
    }

    // If there are no specific matches; and there are wildcard matches
    // (outcomeName matches and origin='*') then add these:
    if (result.size() <= 0)
    {
      for (MBOutcomeDefinition outcomeDef : _outcomeTypes)
      {
        if (outcomeDef.getOrigin().equals("*") && outcomeDef.getName().equals(outcomeName))
        {
          result.add(outcomeDef);
        }
      }
    }

    return result;
  }

  public Map<String, MBDialogDefinition> getDialogs()
  {
    return _dialogs;
  }

  public Map<String, MBDomainDefinition> getDomains()
  {
    return _domainTypes;
  }

  public Map<String, MBActionDefinition> getActions()
  {
    return _actionTypes;
  }

  public List<MBOutcomeDefinition> getOutcomes()
  {
    return _outcomeTypes;
  }

  public Map<String, MBDocumentDefinition> getDocuments()
  {
    return _documentTypes;
  }

  public Map<String, MBPageDefinition> getPages()
  {
    return _pageTypes;
  }

  public MBDialogDefinition getFirstDialogDefinition()
  {
    return _firstDialog;
  }

}
