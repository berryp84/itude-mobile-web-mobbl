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
package com.itude.mobile.web.controllers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;

import com.itude.mobile.mobbl2.client.core.util.MBProperties;
import com.itude.mobile.web.jsf.PageBean;

@Named
@SessionScoped
public class CurrentView implements Serializable
{
  private static final long             serialVersionUID = 1L;

  // A map with the dialogs to enable multiple tabs. 
  // When this is ignored, this will remain a hashmap with one entry: "HOME", the current dialog
  private Map<String, MBViewController> _dialogs;

  // I think this is needed for pop-ups?
  private Stack<MBViewController>       _viewStack;

  private String                        _currentDialog;

  // Required when navigating with paths
  private static boolean                _keephistory;

  // Needed when _keephistory == true
  private Map<Long, String>             _tabHistory;
  private Map<Long, MBViewController>   _controllerHistory;

  private long                          _docId;

  @Inject
  private Instance<PageBean>            _pageBean;

  private MBViewController              _fakeController;

  @PostConstruct
  protected void init()
  {
    String keepHistory = MBProperties.getInstance().getValueForProperty("keepHistory");
    _keephistory = "false".equals(keepHistory) ? false : true;
    _viewStack = new Stack<MBViewController>();
    _dialogs = new HashMap<String, MBViewController>();
    _currentDialog = "HOME";
    _docId = 0;
    _tabHistory = new HashMap<Long, String>();
    _controllerHistory = new HashMap<Long, MBViewController>();
    _fakeController = null;
  }

  public MBViewController getView()
  {
    if (_fakeController != null) return _fakeController;
    else if (!_viewStack.isEmpty()) return _viewStack.peek();
    else return _dialogs.get(_currentDialog);
  }

  public void replaceView(MBViewController controller)
  {
    _fakeController = null;

    if (isTopLevel())
    {
      _dialogs.put(_currentDialog, controller);
    }
    else
    {
      _viewStack.pop();
      _viewStack.push(controller);
    }
    _pageBean.get().setViewController(getView());
    _docId++;
  }

  public void pushView(MBViewController controller)
  {
    if (_fakeController == null)
    {
      replaceView(_fakeController);
      _fakeController = null;
    }

    _viewStack.push(controller);
  }

  public void popView()
  {
    if (_fakeController != null) _fakeController = null;
    else _viewStack.pop();
  }

  public boolean isTopLevel()
  {
    return _viewStack.isEmpty();
  }

  public void clear()
  {
    _viewStack.clear();
    _dialogs.clear();
    _fakeController = null;
    _docId = 0;
    _controllerHistory.clear();
    _tabHistory.clear();
    setCurrentDialog("HOME");
  }

  public String getCurrentDialog()
  {
    return _currentDialog;
  }

  public void setCurrentDialog(String currentDialog)
  {
    _fakeController = null;
    _currentDialog = currentDialog;
    _pageBean.get().setViewController(getView());
    _docId++;
  }

  public long getDocId()
  {
    return _docId;
  }

  public void setDocId(long docId)
  {
    if (_keephistory)
    {
      if (_docId != docId)
      {
        // install a fake view, since someone pressed the back button
        if (_controllerHistory.containsKey(docId) && _tabHistory.containsKey(docId))
        {
          _fakeController = _controllerHistory.get(docId);
          _currentDialog = _tabHistory.get(docId);
          _pageBean.get().setViewController(getView());
        }
      }
      else
      {
        _controllerHistory.put(docId, getView());
        _tabHistory.put(docId, getCurrentDialog());
      }
    }
    _docId = docId;
  }

}
