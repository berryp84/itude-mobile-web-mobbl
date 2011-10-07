package com.itude.mobile.mcds.actions;

import javax.inject.Inject;
import javax.inject.Named;

import com.itude.mobile.mcds.jsf.SessionBean;
import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.web.actions.GenericAction;
import com.itude.mobile.web.util.Utilities;

@Named("LoginAction")
public class LoginAction extends GenericAction 
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private SessionBean _session;

	@Override
	public MBOutcome execute(MBDocument document, String path) 
	{	
		return this.execute(document, path, null);
	}

	@Override
	public MBOutcome execute(MBDocument document, String path, String outcomeName) 
	{
		MBOutcome outcome =  new MBOutcome("OUTCOME-page_login_failed", loadDocument("MBEmptyDoc"));
		String username = document.getValueForPath("/LoginUser[0]/@username");
		String password = document.getValueForPath("/LoginUser[0]/@password");

	    MBDocument loginRequestDoc = loadDocument("MBGenericRequest");
	    Utilities.setRequestParameter(username, "msisdn", loginRequestDoc);
	    Utilities.setRequestParameter(password, "password", loginRequestDoc);
	    Utilities.setRequestParameter("Z4_jlbFv2iwHCzP4aGwS", "token", loginRequestDoc);
	    // TODO: we probably want to make a setting of the scenario_id
	    Utilities.setRequestParameter("WhatsringingDirectBillingScenario", "scenario_id", loginRequestDoc);
	    MBDocument loginResultsDoc = loadDocument("client", loginRequestDoc);
	    if(loginResultsDoc.getElements().size() > 1)
	    {
			outcome = new MBOutcome("OUTCOME-page_login_succeeded", loginResultsDoc);
			_session.logOn((String)loginResultsDoc.getValueForPath("/msisdn[0]/@text()"), (String)loginResultsDoc.getValueForPath("/available_credits[0]/@text()"));
	    }
	    return outcome;
	}

}