package com.itude.mobile.mcds.actions;

import javax.inject.Named;

import com.itude.mobile.template.actions.GenericAction;
import com.itude.mobile.template.util.Utilities;
import com.itude.mobile.mcds.util.MCDSUtilities;
import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.model.MBElement;

@Named("GetHomePageContent")
public class GetHomePageContent extends GenericAction 
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
	    MBDocument realtonesRequestDoc = loadDocument("MBGenericRequest");
	    Utilities.setRequestParameter("10", "per_page", realtonesRequestDoc);
	    Utilities.setRequestParameter("true", "hash", realtonesRequestDoc);
	    Utilities.setRequestParameter("false", "nsfw", realtonesRequestDoc);
	    Utilities.setRequestParameter("nl", "locale", realtonesRequestDoc);
	    Utilities.setRequestParameter("false", "premium", realtonesRequestDoc);
	    MBDocument realtonesResponseDoc = loadDocument("realtones", realtonesRequestDoc);
	    MCDSUtilities.setPositions(realtonesResponseDoc, "realtone", 0);
	    
	    MBDocument javagamesRequestDoc = loadDocument("MBGenericRequest");
	    Utilities.setRequestParameter("6", "per_page", javagamesRequestDoc);
	    Utilities.setRequestParameter("true", "hash", javagamesRequestDoc);
	    Utilities.setRequestParameter("false", "nsfw", javagamesRequestDoc);
	    Utilities.setRequestParameter("nl", "locale", javagamesRequestDoc);
	    Utilities.setRequestParameter("false", "premium", javagamesRequestDoc);
	    //Utilities.setRequestParameter("Nokia6300", "handset", javagamesRequestDoc);
	    MBDocument javagamesResponseDoc = loadDocument("javagames", javagamesRequestDoc);
	    MCDSUtilities.setPositions(javagamesResponseDoc, "javagame", 0);
	    MCDSUtilities.resizeImages(javagamesResponseDoc, "javagame", 40, 40);

	    MBDocument truetonesRequestDoc = loadDocument("MBGenericRequest");
	    Utilities.setRequestParameter("10", "per_page", truetonesRequestDoc);
	    Utilities.setRequestParameter("true", "hash", truetonesRequestDoc);
	    Utilities.setRequestParameter("false", "nsfw", truetonesRequestDoc);
	    Utilities.setRequestParameter("nl", "locale", truetonesRequestDoc);
	    Utilities.setRequestParameter("false", "premium", truetonesRequestDoc);
	    MBDocument truetonesResponseDoc = loadDocument("truetones", truetonesRequestDoc);
	    MCDSUtilities.setPositions(truetonesResponseDoc, "truetone", 0);

	    MBDocument homePageContentDoc = loadDocument("HomePageContentDocument");
	    
	    MBElement realtones = homePageContentDoc.createElementWithName("realtones");
	    for (MBElement realtone : realtonesResponseDoc.getElementsWithName("realtone"))
	    	realtones.addElement(realtone);
	    
	    MBElement javagames = homePageContentDoc.createElementWithName("javagames");
	    for (MBElement javagame : javagamesResponseDoc.getElementsWithName("javagame"))
	    	javagames.addElement(javagame);

	    MBElement truetones = homePageContentDoc.createElementWithName("truetones");
	    for (MBElement truetone : truetonesResponseDoc.getElementsWithName("truetone"))
	    	truetones.addElement(truetone);
	    
	    MBOutcome outcome = new MBOutcome("OUTCOME-page_home", homePageContentDoc);

	    return outcome;
	}

}