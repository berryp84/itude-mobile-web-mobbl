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
package com.itude.mobile.template.controllers;

import java.io.Serializable;

import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.web.controllers.ApplicationController;

public class TemplateController extends ApplicationController implements Serializable
{
  private static final long serialVersionUID = 1L;

  @Override
  public void initialize()
  {

  }

  @Override
  public void setInitialView()
  {
    getView().clear();
    MBOutcome outcome = new MBOutcome("OUTCOME-page_home", getDataManagerService().loadDocument("MBEmptyDoc"));
    handleOutcome(outcome);
  }

  @Override
  public void initializeTab()
  {
    // TODO: do something like this:
    /*
    String outcomeName = null;
    if ("SHARES".equals(getView().getCurrentDialog())) outcomeName = "OUTCOME-tab_shares";
    else if ("SEARCH".equals(getView().getCurrentDialog())) outcomeName = "OUTCOME-SearchAction";
    else if ("ACCOUNT".equals(getView().getCurrentDialog())) outcomeName = "OUTCOME-tab_my_account";
    else if ("HOME".equals(getView().getCurrentDialog())) outcomeName = "OUTCOME-tab_home";
    else if ("INFO".equals(getView().getCurrentDialog())) outcomeName = "OUTCOME-page_info";

    if (outcomeName != null)
    {
      MBOutcome outcome = new MBOutcome(outcomeName, getDataManagerService().loadDocument("MBEmptyDoc"));
      handleOutcome(outcome);
    }
    */
  }

}
