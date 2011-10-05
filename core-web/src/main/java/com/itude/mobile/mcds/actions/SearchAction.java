package com.itude.mobile.mcds.actions;

import javax.inject.Named;

import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.services.MBLocalizationService;
import com.itude.mobile.template.actions.GenericAction;
import com.itude.mobile.template.util.MCDSUtilities;
import com.itude.mobile.template.util.Utilities;

@Named("SearchAction")
public class SearchAction extends GenericAction 
{
	private static final long serialVersionUID = 1L;

	@Override
	public MBOutcome execute(MBDocument document, String path) 
	{	
		return this.execute(document, path, null);
	}

	@Override
	public MBOutcome execute(MBDocument document, String path, String outcomeName) 
	{
		String query = document.getValueForPath("/Query[0]/@text()");
	    
	    MBDocument searchResultsRequestDoc = loadDocument("MBGenericRequest");
	    Utilities.setRequestParameter("true", "hash", searchResultsRequestDoc);
	    Utilities.setRequestParameter("false", "nsfw", searchResultsRequestDoc);
	    Utilities.setRequestParameter("nl", "locale", searchResultsRequestDoc);
	    Utilities.setRequestParameter("false", "premium", searchResultsRequestDoc);
	    Utilities.setRequestParameter(query, "query", searchResultsRequestDoc);
	    MBDocument searchResultsDoc = loadDocument("results", searchResultsRequestDoc);
	    MCDSUtilities.setPositionsForSearchResults(searchResultsDoc);
	    searchResultsDoc.createElementWithName("Query");
	    searchResultsDoc.setValue(""+MBLocalizationService.getInstance().getTextForKey("LABEL_SEARCHRESULTS")+" '"+query+"'", "/Query[0]/@text()");
	    MBOutcome outcome = new MBOutcome("OUTCOME-page_search", searchResultsDoc);

	    return outcome;
	}

}