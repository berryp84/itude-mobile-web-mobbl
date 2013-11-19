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
