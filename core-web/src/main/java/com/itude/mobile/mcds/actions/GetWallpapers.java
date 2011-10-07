package com.itude.mobile.mcds.actions;

import javax.inject.Named;

import com.itude.mobile.mcds.util.MCDSUtilities;
import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.services.MBDataManagerService;
import com.itude.mobile.web.actions.GenericAction;
import com.itude.mobile.web.util.Utilities;

@Named("GetWallpapers")
public class GetWallpapers extends GenericAction 
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
	    MBDocument sessionDoc = loadDocument("SessionState");
	    String sIndex = sessionDoc.getValueForPath("/Session[0]/@page");
	    int index = Integer.parseInt(sIndex);
	    
	    if(outcomeName.contains("_next")) index++;
	    else if(outcomeName.contains("_previous")) index--;
	    else index=1;
	    sessionDoc.setValue(""+index, "/Session[0]/@page");
	    MBDataManagerService.getInstance().storeDocument(sessionDoc);
	    
	    MBDocument requestDoc = loadDocument("MBGenericRequest");
	    Utilities.setRequestParameter("9", "per_page", requestDoc);
	    Utilities.setRequestParameter(""+index, "page", requestDoc);
	    Utilities.setRequestParameter("true", "hash", requestDoc);
	    Utilities.setRequestParameter("false", "nsfw", requestDoc);
	    Utilities.setRequestParameter("nl", "locale", requestDoc);
	    Utilities.setRequestParameter("false", "premium", requestDoc);

	    MBDocument responseDoc = loadDocument("wallpapers", requestDoc);
	    MCDSUtilities.setPositions(responseDoc, "wallpaper", 0+((index-1)*9));
	    MCDSUtilities.resizeImages(responseDoc, "wallpaper", 80, 80);
	    
	    MBOutcome outcome = new MBOutcome("OUTCOME-page_wallpapers", responseDoc);

	    return outcome;
	}

}