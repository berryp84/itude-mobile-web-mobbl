package com.itude.mobile.mobbl2.client.core.view;

import java.util.ArrayList;
import java.util.List;

import com.itude.mobile.mobbl2.client.core.configuration.MBDefinition;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;

public class MBComponentContainer extends MBComponent
{

  private ArrayList<MBComponent> _children;

  public MBComponentContainer(MBDefinition definition, MBDocument document, MBComponentContainer parent)
  {
    super(definition, document, parent);
    _children = new ArrayList<MBComponent>();
  }

  public ArrayList<MBComponent> getChildren()
  {
    return _children;
  }

  public void setChildren(ArrayList<MBComponent> children)
  {
    _children = children;
  }

  public void addChild(MBComponent child)
  {
    if (child != null && !child.isMarkedForDestruction())
    {
      _children.add(child);
      child.setParent(this);
    }
  }

  @Override
  public void translatePath()
  {
    for (MBComponent child : _children)
    {
      child.translatePath();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends MBComponent> List<T> getChildrenOfKind(Class<T> clazz)
  {
    List<T> result = new ArrayList<T>();
    for (MBComponent child : _children)
    {
      if (clazz.isInstance(child))
      {
        result.add((T) child);
      }
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends MBComponent> List<T> getDescendantsOfKind(Class<T> clazz)
  {
    List<T> result = new ArrayList<T>();
    for (MBComponent child : _children)
    {

      if (clazz.isInstance(child))
      {
        result.add((T) child);
      }

      result.addAll(child.getDescendantsOfKind(clazz));
    }

    return result;
  }

  public String childrenAsXmlWithLevel(int level)
  {
    String result = "";

    for (MBComponent child : _children)
    {
      result += child.asXmlWithLevel(level);
    }

    return result;
  }

}
