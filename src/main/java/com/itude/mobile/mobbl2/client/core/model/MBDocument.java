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
package com.itude.mobile.mobbl2.client.core.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBDocumentDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBElementDefinition;
import com.itude.mobile.mobbl2.client.core.model.exceptions.MBCannotAssignException;
import com.itude.mobile.mobbl2.client.core.services.MBDataManagerService;
import com.itude.mobile.mobbl2.client.core.services.datamanager.handlers.MBDocumentOperationDelegate;
import com.itude.mobile.mobbl2.client.core.util.StringUtilities;

public class MBDocument extends MBElementContainer implements Cloneable
{
  private MBDocumentDefinition         _definition;
  private Map<String, MBDocument>      _sharedContext;
  private final Map<String, MBElement> _pathCache;
  private MBDocument                   _argumentsUsed;

  public MBDocument()
  {
    super();
    _sharedContext = new HashMap<String, MBDocument>();
    _pathCache = new HashMap<String, MBElement>();
  }

  public MBDocument(MBDocumentDefinition documentDefinition)
  {
    _definition = documentDefinition;
    _sharedContext = new HashMap<String, MBDocument>();
    _pathCache = new HashMap<String, MBElement>();
  }

  @Override
  public Map<String, MBDocument> getSharedContext()
  {
    return _sharedContext;
  }

  @Override
  public void setSharedContext(Map<String, MBDocument> sharedContext)
  {
    _sharedContext = sharedContext;
  }

  public MBDocument getArgumentsUsed()
  {
    return _argumentsUsed;
  }

  public void setArgumentsUsed(MBDocument argumentsUsed)
  {
    _argumentsUsed = argumentsUsed;
  }

  public void assignToDocument(MBDocument target)
  {
    if (!target.getDefinition().getName().equals(_definition.getName()))
    {
      String message = "Cannot assign document since document types differ: " + target.getDefinition().getName() + " != "
                       + _definition.getName();
      throw new MBCannotAssignException(message);
    }

    target.getElements().clear();
    target._pathCache.clear();
    copyChildrenInto(target);
  }

  @Override
  public String getUniqueId()
  {
    String uid = "";

    // Specification: the uniqueId of a document starts with <docname>:
    // This is required for the cache manager to determine the document type
    uid += _definition.getName() + ":";
    uid += super.getUniqueId();

    return uid;
  }

  public void loadFreshCopyForDelegate(MBDocumentOperationDelegate delegate, Object resultSelector, Object errorSelector)
  {
    MBDataManagerService.getInstance().loadDocument(_definition.getName(), _argumentsUsed, delegate);
  }

  // Be careful with reload since it might change the number of elements; making any existing path (indexes) invalid
  // It is safer to use loadFreshCopyForDelegate:resultSelector:errorSelector: and process the result in the callbacks
  public void reload()
  {

    MBDocument fresh;

    if (_argumentsUsed == null)
    {
      fresh = MBDataManagerService.getInstance().loadDocument(_definition.getName());
    }
    else
    {
      fresh = MBDataManagerService.getInstance().loadDocument(_definition.getName(), _argumentsUsed);
    }

    setElements(fresh.getElements());
    _pathCache.clear();
  }

  public void clearPathCache()
  {
    _pathCache.clear();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getValueForPath(String path)
  {
    if (path != null)
    {
      int posAt = path.indexOf('@');
      if (posAt < 0 || path.indexOf('@', posAt + 1) >= 0) return (T) getValueForPath(path, null);

      String componentBeforeAt = path.substring(0, posAt);
      //      assert path.substring(posAt + 1).length() > 0;
      MBElement element = _pathCache.get(componentBeforeAt);

      if (element == null)
      {
        element = (MBElement) super.getValueForPath(componentBeforeAt);
        _pathCache.put(componentBeforeAt, element);
      }

      // TODO Check if this is a proper workaround for a bug which caused a nullpointer exception to occur
      if (element != null)
      {
        return (T) element.getValueForAttribute(path.substring(posAt + 1));
      }
      else
      {
        return null;
      }

    }
    else
    {
      return null;
    }
  }

  @Override
  public String toString()
  {
    return this.asXmlWithLevel(0);
  }

  public void clearAllCaches()
  {
    getSharedContext().clear();
    clearPathCache();
  }

  public String asXmlWithLevel(int level)
  {
    String result = StringUtilities.getIndentStringWithLevel(level) + "<"
                    + ((_definition.getRootElement() == null) ? _definition.getName() : _definition.getRootElement());
    if (getElements().size() == 0)
    {
      result += "/>\n";
    }
    else
    {
      result += ">\n";
      for (MBElementDefinition elemDef : _definition.getChildren())
      {
        List<MBElement> lst = getElements().get(elemDef.getName());
        for (MBElement elem : lst)
        {
          result += elem.asXmlWithLevel(level + 2);
        }
      }
      result += StringUtilities.getIndentStringWithLevel(level) + "</" + _definition.getName() + ">\n";
    }

    return result;
  }

  @Override
  public MBDocumentDefinition getDefinition()
  {
    return _definition;
  }

  @Override
  public String getDocumentName()
  {
    return _definition.getName();
  }

  @Override
  public MBDocument getDocument()
  {
    return this;
  }

  @Override
  public MBDocument clone()
  {
    MBDocument newDoc = new MBDocument(_definition);
    copyChildrenInto(newDoc);
    if (_argumentsUsed != null) newDoc.setArgumentsUsed(_argumentsUsed.clone());

    return newDoc;
  }
}
