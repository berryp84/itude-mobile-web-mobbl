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
package com.itude.mobile.template.actions;

import javax.inject.Inject;
import javax.inject.Named;

import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.template.jsf.SessionBean;
import com.itude.mobile.web.actions.GenericAction;

@Named("LoginAction")
public class LoginAction extends GenericAction
{
  @Inject
  private SessionBean _session;

  @Override
  public MBOutcome execute(MBDocument document, String path, String outcomeName)
  {
    String username = document.getValueForPath("/LoginUser[0]/@username");
    String password = document.getValueForPath("/LoginUser[0]/@password");

    // TODO: you want to really login here like this:
    //		MBDocument loginRequestDoc = loadDocument("MBGenericRequest");
    //	    Utilities.setRequestParameter(username, "msisdn", loginRequestDoc);
    //	    Utilities.setRequestParameter(password, "password", loginRequestDoc);
    //	    Utilities.setRequestParameter("Z4_jlbFv2iwHCzP4aGwS", "token", loginRequestDoc);
    //	    // TODO: we probably want to make a setting of the scenario_id
    //	    Utilities.setRequestParameter("WhatsringingDirectBillingScenario", "scenario_id", loginRequestDoc);
    //	    MBDocument loginResultsDoc = loadDocument("client", loginRequestDoc);

    MBOutcome outcome;
    if (!username.isEmpty() && !password.isEmpty())
    {
      outcome = new MBOutcome("OUTCOME-page_login_succeeded", document);
      MBDocument sessionDocument = _session.getDocument();
      sessionDocument.setValue("true", "Session[0]/@loggedIn");
    }
    else
    {
      outcome = new MBOutcome("OUTCOME-page_login_failed", loadDocument("MBEmptyDoc"));
    }
    return outcome;
  }

}