/*
 * (C) Copyright ItudeMobile.
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
package com.itude.mobile.web.jsf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import com.itude.mobile.mobbl2.client.core.view.MBPage;
import com.itude.mobile.web.controllers.AbstractRefreshableViewController;
import com.itude.mobile.web.controllers.CurrentView;
import com.itude.mobile.web.controllers.MBViewController;

@RequestScoped
@Named
public class PageBean implements Serializable
{
  private static final long serialVersionUID = 1L;

  @Inject
  private CurrentView       _view;

  private MBViewController  _viewController;

  @PostConstruct
  protected void init()
  {
    setViewController(_view.getView());
  }

  public void setViewController(MBViewController controller)
  {
    _viewController = controller;
  }

  public void handleOutcome(ActionEvent ev)
  {
    String outcomeName = (String) ev.getComponent().getAttributes().get("outcome");
    String path = (String) ev.getComponent().getAttributes().get("path");
    if (_viewController != null) _viewController.handleOutcome(outcomeName, path);
  }

  public MBPage getPage()
  {
    return _viewController != null ? _viewController.getPage() : null;
  }

  public MBViewController getViewController()
  {
    return _viewController;
  }

  public boolean isRefreshable()
  {
    return _viewController instanceof AbstractRefreshableViewController;
  }

  public String refresh()
  {
    if (isRefreshable())
    {
      AbstractRefreshableViewController rfvc = (AbstractRefreshableViewController) _viewController;
      rfvc.refresh();
    }
    return "default";
  }

}
