package com.itude.mobile.mobbl2.client.core.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itude.commons.util.CollectionUtil;
import com.itude.mobile.mobbl2.client.core.configuration.MBDefinition;
import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.view.helpers.MBSelector;

public class MBComponent
{
  private MBDefinition         _definition;
  private MBComponentContainer _parent;
  private String               _style;
  private Map<String, Object>  _viewData;
  private boolean              _markedForDestruction;
  private int                  _leftInset;
  private int                  _rightInset;
  private int                  _topInset;
  private int                  _bottomInset;

  public MBComponent(MBDefinition definition, MBDocument document, MBComponentContainer parent)
  {
    _definition = definition;
    _parent = parent;

    // Not all definitions have a style attribute; if they do set it
    if (MBStylableDefinition.class.isAssignableFrom(definition.getClass()))
    {
      setStyle(((MBStylableDefinition) definition).getStyle());
    }

    _viewData = null;
  }

  public MBDefinition getDefinition()
  {
    return _definition;
  }

  public void setDefinition(MBDefinition definition)
  {
    _definition = definition;
  }

  public MBComponentContainer getParent()
  {
    return _parent;
  }

  public void setParent(MBComponentContainer parent)
  {
    _parent = parent;
  }

  public String getStyle()
  {
    return _style;
  }

  public void setStyle(String style)
  {
    _style = style;
  }

  public boolean isMarkedForDestruction()
  {
    return _markedForDestruction;
  }

  public void setMarkedForDestruction(boolean markedForDestruction)
  {
    _markedForDestruction = markedForDestruction;
  }

  public int getLeftInset()
  {
    return _leftInset;
  }

  public void setLeftInset(int leftInset)
  {
    _leftInset = leftInset;
  }

  public int getRightInset()
  {
    return _rightInset;
  }

  public void setRightInset(int rightInset)
  {
    _rightInset = rightInset;
  }

  public int getTopInset()
  {
    return _topInset;
  }

  public void setTopInset(int topInset)
  {
    _topInset = topInset;
  }

  public int getBottomInset()
  {
    return _bottomInset;
  }

  public void setBottomInset(int bottomInset)
  {
    _bottomInset = bottomInset;
  }

  public void handleOutcome(MBOutcome outcome)
  {
    _parent.handleOutcome(outcome);
  }

  public void handleOutcome(String outcomeName, String path)
  {
    _parent.handleOutcome(outcomeName, path);
  }

  public String substituteExpressions(String expression)
  {
    if (expression == null)
    {
      return null;
    }

    if (!expression.contains("{"))
    {
      return expression;
    }

    String subPart = "";
    String singleExpression;

    String result = "";

    int position = 0;
    int subPartPosition = -1;

    boolean evalToNil = false;
    while ((position = expression.indexOf("${")) > -1)
    {
      result += expression.substring(0, position);
      expression = expression.substring(position + 2);

      if ((subPartPosition = expression.indexOf("}")) != -1)
      {
        subPart = expression.substring(subPartPosition + 1);
        singleExpression = expression.substring(0, subPartPosition);

        if (expression.length() < (subPartPosition + 1)) expression = "";
        else expression = expression.substring(subPartPosition + 1);
        String value = evaluateExpression(singleExpression);
        if (value != null)
        {
          result += value;
        }
        else
        {
          evalToNil = true;
        }

      }

    }

    result += subPart;

    if (result.length() == 0 && evalToNil)
    {
      result = null;
    }

    return result;
  }

  public String getComponentDataPath()
  {
    return "";
  }

  public String getAbsoluteDataPath()
  {
    String componentPath = getComponentDataPath();

    // If the path is not set (a field without a path specified for instance) return nil; since it then also does not have an absolute path:
    if (componentPath == null)
    {
      return null;
    }

    // Absolute path set for the component? (possibly using a doc:path expression) Than do not prefix with the parent path and return it:
    if (componentPath.startsWith("/") || componentPath.contains(":"))
    {
      return componentPath;
    }

    String pathToMe = null;
    if (_parent != null) pathToMe = _parent.getAbsoluteDataPath();
    if (pathToMe != null && !pathToMe.endsWith("/"))
    {
      pathToMe = pathToMe + "/";
    }
    if (pathToMe == null)
    {
      pathToMe = "";
    }

    if (componentPath != null)
    {
      pathToMe = pathToMe + componentPath;
    }

    return pathToMe;
  }

  public MBDocument getDocument()
  {
    MBPage page = getPage();
    if (page != null) return getPage().getDocument();
    else return null;
  }

  public MBPage getPage()
  {
    if (_parent != null) return _parent.getPage();
    else return null;
  }

  public void setViewData(Object value, String key)
  {
    if (_viewData == null)
    {
      _viewData = new HashMap<String, Object>();
    }

    _viewData.put(key, value);
  }

  public Object getViewData(String key)
  {
    return _viewData.get(key);
  }

  public String evaluateExpression(String variableName)
  {
    return getParent().evaluateExpression(variableName);
  }

  public String asXmlWithLevel(int level)
  {
    return "";
  }

  public String attributeAsXml(String name, Object attrValue)
  {
    return attrValue == null ? "" : name + "='" + attrValue + "'";
  }

  public String getDescription()
  {
    return asXmlWithLevel(0);
  }

  public void translatePath()
  {
  }

  public <T extends MBComponent> List<T> getDescendantsOfKind(Class<T> clazz)
  {
    // This method is overridden by the various subclasses; if this could be an abstract method it would be
    return Collections.emptyList();
  }

  public <T extends MBComponent, S> List<T> getDescendantsOfKind(Class<T> clazz, MBSelector<T, S> selector, S value)
  {
    return runThroughSelector(getDescendantsOfKind(clazz), selector, value);
  }

  public <T extends MBComponent> List<T> getChildrenOfKind(Class<T> clazz)
  {
    // This method is overridden by the various subclasses; if this could be an abstract method it would be
    return Collections.emptyList();
  }

  //TODO method has a selector, needs implementation
  public <T extends MBComponent, S> List<T> getChildrenOfKind(Class<T> clazz, MBSelector<T, S> selector, S value)
  {
    return runThroughSelector(getChildrenOfKind(clazz), selector, value);
  }

  // TODO filterSet Method should be implemented

  public <T extends MBComponent> T getFirstDescendantOfKind(Class<T> clazz)
  {
    return CollectionUtil.getFirst(getDescendantsOfKind(clazz));
  }

  // TODO method has a selector, needs implementation
  public <T extends MBComponent, S> T getFirstDescendantOfKind(Class<T> clazz, MBSelector<T, S> selector, S value)
  {
    return CollectionUtil.getFirst(getDescendantsOfKind(clazz, selector, value));
  }

  private <T extends MBComponent, S> List<T> runThroughSelector(List<T> list, MBSelector<T, S> selector, S value)
  {
    List<T> results = new ArrayList<T>();
    for (T entry : list)
      if (selector.meets(entry, value)) results.add(entry);

    return results;

  }

  public <T extends MBComponent> T getFirstChildOfKind(Class<T> clazz)
  {
    List<T> result = getChildrenOfKind(clazz);

    if (result.size() == 0)
    {
      return null;
    }

    return result.get(0);
  }

  // TODO Method has a selector, needs implementation
  public <T extends MBComponent, S> T getFirstChildOfKind(Class<T> clazz, MBSelector<T, S> selector, S value)
  {
    List<T> result = getChildrenOfKind(clazz, selector, value);
    if (result.isEmpty()) return null;
    else return result.get(0);
  }

  // Listener logic is handled by the page; so delegate to parent until the page is reached:
  public void registerValueChangeListener(Object listener, String path)
  {
    getParent().registerValueChangeListener(listener, path);
  }

  public void unregisterValueChangeListener(Object listener)
  {
    getParent().unregisterValueChangeListener(listener);
  }

  public void unregisterValueChangeListener(Object listener, String path)
  {
    getParent().unregisterValueChangeListener(listener, path);
  }

  public boolean notifyValueWillChange(String value, String currentValue, String path)
  {
    return getParent().notifyValueWillChange(value, currentValue, path);
  }

  public void notifyValueChanged(String value, String currentValue, String path)
  {
    getParent().notifyValueChanged(value, currentValue, path);
  }

  public String getName()
  {
    return getDefinition().getName();
  }

  public boolean isHasListeners()
  {
    return !getPage().getListenersForPath(getAbsoluteDataPath()).isEmpty();
  }
  
  public boolean isFirstSibling()
  {
    return isFirstSibling(this, getParent());
  }
  
  private boolean isFirstSibling(MBComponent zeComponent, MBComponentContainer parent)
  {
    MBComponent firstChild = parent.getChildren().get(0);
    if(firstChild.equals(zeComponent))
    {
      if(parent.getParent() == null)
        return true;
      else
        return isFirstSibling(parent, parent.getParent());
    }
    else
      return false;
  }

}
