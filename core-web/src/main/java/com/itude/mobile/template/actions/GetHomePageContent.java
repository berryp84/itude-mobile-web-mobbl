package com.itude.mobile.template.actions;

import javax.inject.Named;

import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.model.MBElement;
import com.itude.mobile.web.actions.GenericAction;

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
    // TODO: you'll want to do something like this (or delete this action)
    /*
    MBDocument realtonesRequestDoc = loadDocument("MBGenericRequest");
    Utilities.setRequestParameter("10", "per_page", realtonesRequestDoc);
    Utilities.setRequestParameter("nl", "locale", realtonesRequestDoc);
    MBDocument realtonesResponseDoc = loadDocument("realtones", realtonesRequestDoc);
    */

    

    MBDocument homePageContentDoc = loadDocument("HomePageContentDocument");

    MBElement testgroup = homePageContentDoc.createElementWithName("testgroup");
      MBElement testitem = testgroup.createElementWithName("testitem");
        MBElement testelement = testitem.createElementWithName("testelement");
          testelement.setBodyText("test");

    MBOutcome outcome = new MBOutcome("OUTCOME-page_home", homePageContentDoc);

    return outcome;
  }
}