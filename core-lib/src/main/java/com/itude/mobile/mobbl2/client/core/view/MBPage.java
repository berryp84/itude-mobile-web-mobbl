package com.itude.mobile.mobbl2.client.core.view;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBPageDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBPageDefinition.MBPageType;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.exceptions.MBInvalidPathException;
import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.util.StringUtilities;

public class MBPage extends MBPanel
{
  private static final Logger                                    _log = Logger.getLogger(MBPage.class);

  private String                                                 _pageName;
  private String                                                 _rootPath;
  private String                                                 _dialogName;
  private MBDocument                                             _document;
  private final Map<String, List<MBValueChangeListenerProtocol>> _valueChangedListeners;
  private final List<MBOutcomeListenerProtocol>                  _outcomeListeners;
  private MBPageDefinition.MBPageType                            _pageType;

  public MBPage(MBPageDefinition definition, MBDocument document, String rootPath, Object viewState)
  {
    super(definition, document, null, false);

    setDefinition(definition);
    setRootPath(rootPath);
    setPageName(definition.getName());
    setDocument(document);
    setPageType(definition.getPageType());
    setTitle(definition.getTitle());
    _outcomeListeners = new ArrayList<MBOutcomeListenerProtocol>();
    _valueChangedListeners = new Hashtable<String, List<MBValueChangeListenerProtocol>>();

    buildViewStructure();
  }

  public String getPageName()
  {
    return _pageName;
  }

  public void setPageName(String pageName)
  {
    _pageName = pageName;
  }

  public String getDialogName()
  {
    return _dialogName;
  }

  public void setDialogName(String dialogName)
  {
    _dialogName = dialogName;
  }

  @Override
  public MBDocument getDocument()
  {
    return _document;
  }

  public void setDocument(MBDocument document)
  {
    _document = document;
  }

  public MBPageDefinition.MBPageType getPageType()
  {
    return _pageType;
  }

  public void setPageType(MBPageType pageType)
  {
    _pageType = pageType;
  }

  // for loading interface builder files:- (id)
  // initWithDefinition:(MBPageDefinition*) definition
  // withViewController:(Object<MBViewControllerProtocol>*)
  // viewController document:(MBDocument*) document rootPath:(NSString*)
  // rootPath viewState:(MBViewState) viewState;
  // for initialising a generic page:- (id) initWithDefinition:(id)definition
  // document:(MBDocument*) document rootPath:(NSString*) rootPath
  // viewState:(MBViewState) viewState withMaxBounds:(Object) bounds;
  public void handleOutcome(String outcomeName)
  {
    handleOutcome(outcomeName, null);
  }

  @Override
  public void handleOutcome(String outcomeName, String path)
  {
    MBOutcome outcome = new MBOutcome();
    outcome.setOriginName(getPageName());
    outcome.setOutcomeName(outcomeName);
    outcome.setDocument(getDocument());
    outcome.setDialogName(getDialogName());
    outcome.setPath(path);

    for (MBOutcomeListenerProtocol lsnr : _outcomeListeners)
    {
      lsnr.outcomeProduced(outcome);
    }
  }

  @Override
  public String getComponentDataPath()
  {
    return getRootPath();
  }

  @Override
  public String getDescription()
  {
    return "Page: pageID=" + _pageName;
  }

  public String getRootPath()
  {
    return _rootPath;
  }

  public void setRootPath(String path)
  {
    boolean ignorePath = false;

    if (path == null)
    {
      path = "";
    }

    if (path.length() > 0)
    {
      MBPageDefinition pd = (MBPageDefinition) getDefinition();
      String stripped = StringUtilities.normalizedPath(StringUtilities.stripCharacters(path, "[]0123456789"));
      stripped = stripped + "/";

      String mustBe = pd.getRootPath();
      if (mustBe == null || mustBe.equals(""))
      {
        mustBe = "/";
      }

      if (!stripped.equals(mustBe))
      {
        if (mustBe.equals("/"))
        {
          _log.warn("Ignoring path " + stripped + " because the document definition used root path " + mustBe);
          ignorePath = true;
        }
        else
        {
          String message = "Invalid root path " + path + "->" + stripped + "; does not conform to defined document root path " + mustBe;
          throw new MBInvalidPathException(message);
        }
      }
    }

    if (!ignorePath && _rootPath != path)
    {
      _rootPath = path;
    }

  }

  public List<MBValueChangeListenerProtocol> getListenersForPath(String path)
  {
    if (!path.startsWith("/"))
    {
      path = "/" + path;
    }

    path = StringUtilities.normalizedPath(path);
    List<MBValueChangeListenerProtocol> lsnrList = _valueChangedListeners.get(path);
    if (lsnrList == null)
    {
      lsnrList = new ArrayList<MBValueChangeListenerProtocol>();
      _valueChangedListeners.put(path, lsnrList);
    }

    return lsnrList;
  }

  public void registerValueChangeListener(MBValueChangeListenerProtocol listener, String path)
  {
    // Check that the path is valid by reading the value:
    getDocument().getValueForPath(path);

    List<MBValueChangeListenerProtocol> lsnrList = getListenersForPath(path);
    lsnrList.add(listener);
  }

  public void unregisterValueChangeListener(MBValueChangeListenerProtocol listener, String path)
  {
    // Check that the path is valid by reading the value:
    getDocument().getValueForPath(path);

    List<MBValueChangeListenerProtocol> lsnrList = getListenersForPath(path);
    lsnrList.remove(listener);
  }

  public void unregisterValueChangeListener(MBValueChangeListenerProtocol listener)
  {
    // Check that the path is valid by reading the value:

    for (List<MBValueChangeListenerProtocol> list : _valueChangedListeners.values())
    {
      list.remove(listener);
    }
  }

  @Override
  public boolean notifyValueWillChange(String value, String originalValue, String path)
  {
    boolean result = true;

    List<MBValueChangeListenerProtocol> lsnrList = getListenersForPath(path);
    for (MBValueChangeListenerProtocol lsnr : lsnrList)
    {
      result &= lsnr.valueWillChange(value, originalValue, path);
    }

    return result;
  }

  @Override
  public void notifyValueChanged(String value, String originalValue, String path)
  {
    List<MBValueChangeListenerProtocol> lsnrList = getListenersForPath(path);

    for (MBValueChangeListenerProtocol lsnr : lsnrList)
    {
      lsnr.valueChanged(value, originalValue, path);
    }

  }

  @Override
  public void rebuild()
  {
    getDocument().clearAllCaches();
    super.rebuild();
  }

  public void rebuildView()
  {
    // No need to rebuild the view in case of a website, just rebuild
    rebuild();
  }


  @Override
  public MBPage getPage()
  {
    return this;
  }

  @Override
  public String asXmlWithLevel(int level)
  {
    String result = StringUtilities.getIndentStringWithLevel(level) + "<MBPage " + attributeAsXml("pageName", _pageName) + " "
                    + attributeAsXml("rootPath", _rootPath) + " " + attributeAsXml("dialogName", _dialogName) + " "
                    + attributeAsXml("document", _document.getDocumentName()) + ">\n";

    result += childrenAsXmlWithLevel(level + 2);

    result += StringUtilities.getIndentStringWithLevel(level) + "</MBPage>\n";

    return result;
  }

  @Override
  public String evaluateExpression(String variableName)
  {
    return (String) getDocument().getValueForPath(variableName);
  }

  @Override
  public String toString()
  {
    return asXmlWithLevel(0);
  }
  
  // The following function haven't been implemented:
  // getDocumentDiff, setDocumentDiff, getChildViewControllers, getView, setViewController, getViewController, getViewControllerOfType, diffDocument
  // registerOutcomeListener, unregisterOutcomeListener, handleException, unregisterAllViewControllers, registerViewController
  // They need to be implemented when needed.
}
