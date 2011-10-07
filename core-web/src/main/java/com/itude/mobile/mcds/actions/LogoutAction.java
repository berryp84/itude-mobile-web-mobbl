package com.itude.mobile.mcds.actions;

import javax.inject.Inject;
import javax.inject.Named;

import com.itude.mobile.template.actions.GenericAction;
import com.itude.mobile.mcds.jsf.SessionBean;
import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;

@Named("LogoutAction")
public class LogoutAction extends GenericAction 
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
		MBOutcome outcome =  new MBOutcome("OUTCOME-page_home", null);
		_session.logOff();
	    return outcome;
	}

}