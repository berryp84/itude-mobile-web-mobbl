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
import com.itude.mobile.mobbl2.client.core.model.MBDocumentDiff;
import com.itude.mobile.mobbl2.client.core.util.StringUtilities;

public class MBPage extends MBPanel
{
  private static final Logger                                    _log = Logger.getLogger(MBPage.class);

  private String                                                 _pageName;
  private String                                                 _rootPath;
  private String                                                 _dialogName;
  private MBDocument                                             _document;
  //  private MBApplicationController                                _controller;
  private Object                                                 _viewController;
  private List                                                   _childViewControllers;
  private MBDocumentDiff                                         _documentDiff;
  private final Map<String, List<MBValueChangeListenerProtocol>> _valueChangedListeners;
  private final List<MBOutcomeListenerProtocol>                  _outcomeListeners;
  private MBPageDefinition.MBPageType                            _pageType;
  private Object                                                 _maxBounds;

  //  private final MBViewManager.MBViewState                        _viewState;

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

  public MBDocumentDiff getDocumentDiff()
  {
    return _documentDiff;
  }

  public void setDocumentDiff(MBDocumentDiff documentDiff)
  {
    _documentDiff = documentDiff;
  }

  public List getChildViewControllers()
  {
    return _childViewControllers;
  }

  public void setChildViewControllers(List childViewControllers)
  {
    _childViewControllers = childViewControllers;
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
    else if (!path.endsWith("/"))
    {
      path = path + "/";
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

  public Object getView()
  {
    // TODO UIViewController is an objective C class, what should be the Android implementation of it?
    return null;
  }

  // TODO UIViewController is an objective C class, what should be the Android implementation of it?
  public void setViewController(Object viewController)
  {
  }

  // TODO UIViewController is an objective C class, what should be the Android implementation of it?
  public Object getViewController()
  {
    return null;
  }

  public MBDocumentDiff diffDocument(MBDocument other)
  {

    MBDocumentDiff diff = new MBDocumentDiff(getDocument(), other);
    setDocumentDiff(diff);

    return getDocumentDiff();
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

  public void registerOutcomeListener(MBOutcomeListenerProtocol listener)
  {
    if (!_outcomeListeners.contains(listener))
    {
      _outcomeListeners.add(listener);
    }
  }

  public void unregisterOutcomeListener(MBOutcomeListenerProtocol listener)
  {
    _outcomeListeners.remove(listener);
  }

  @Override
  public void rebuild()
  {
    getDocument().clearAllCaches();
    super.rebuild();
  }

  public void rebuildView()
  {
    // Make sure we clear the cache of all related documents:
    rebuild();
    // TODO UIViewController stuff
    //    CGRect bounds = [UIScreen mainScreen].applicationFrame; 
    //    self.viewController.view = [self buildViewWithMaxBounds: bounds viewState: _viewState];
  }

  @Override
  public MBPage getPage()
  {
    return this;
  }

  // TODO hideKeyboard method doesn't need to be implemented, or does it?

  public void handleException(Exception exception)
  {
    MBOutcome outcome = new MBOutcome(getPageName(), _document);
  }

  public void unregisterAllViewControllers()
  {
    setChildViewControllers(null);
  }

  // TODO UIViewController is an objective C class, what should be the Android implementation of it?
  //  - (void) registerViewController:(UIViewController*) controller {
  //    if(self.childViewControllers == nil) self.childViewControllers = [[NSMutableArray new] autorelease];
  //    if(![self.childViewControllers containsObject: controller]) [self.childViewControllers addObject:controller];
  //}

  // TODO UIViewController is an objective C class, what should be the Android implementation of it?
  public Object getViewControllerOfType(Class clazz)
  {
    if (getChildViewControllers() != null)
    {

    }

    return null;
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
  public String evaluateExpression(String variableName) {
    return (String) getDocument().getValueForPath(variableName);
  }

  @Override
  public String toString()
  {
    return asXmlWithLevel(0);
  }

}
