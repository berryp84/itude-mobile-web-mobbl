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

import com.itude.mobile.web.jsf.PageBean;

@Named
@SessionScoped
public class CurrentView implements Serializable
{
  private static final long             serialVersionUID = 1L;

  private Map<String, MBViewController> _dialogs;

  private Stack<MBViewController>       _viewStack;

  private String                        _currentDialog;

  private Map<Long, String>             _tabHistory;
  private Map<Long, MBViewController>   _controllerHistory;

  private long                          _docId;

  @Inject
  private Instance<PageBean>            _pageBean;

  private MBViewController              _fakeController;

  @PostConstruct
  protected void init()
  {
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

    if (isTopLevel()) _dialogs.put(_currentDialog, controller);
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

    if (_docId != docId)
    {
      // install a fake view, since someone pressed the back button
      if (_controllerHistory.containsKey(docId) && _tabHistory.containsKey(docId))
      {
        _fakeController = _controllerHistory.get(docId);
        _currentDialog = _tabHistory.get(docId);
        _pageBean.get().setViewController(getView());
      }
    } else {
      _controllerHistory.put (docId, getView ());
      _tabHistory.put (docId, getCurrentDialog());
    }
    _docId = docId;
  }

}
