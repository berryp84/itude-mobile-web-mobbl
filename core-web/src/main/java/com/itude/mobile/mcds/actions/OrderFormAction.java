package com.itude.mobile.mcds.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.itude.mobile.mcds.jsf.SessionBean;
import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.model.MBElement;
import com.itude.mobile.mobbl2.client.core.services.MBMetadataService;
import com.itude.mobile.web.actions.GenericAction;
import com.itude.mobile.web.annotations.HttpParam;
import com.itude.mobile.web.util.Utilities;

@Named("OrderFormAction")
public class OrderFormAction extends GenericAction
{
  private static final long   serialVersionUID = 1L;

  private static final Logger _log             = Logger.getLogger(OrderFormAction.class);

  @Inject
  @HttpParam("third_party_id")
  private String              _thirdPartyId;

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
    MBElement content = null;
    if (document == null || path == null) content = locateContentByThirdPartyId(document);
    else content = document.getValueForPath(path);
    if (content == null) return null;
    MBDocument orderDoc = loadDocument("OrderContentDocument");
    MBElement contentDataElement = orderDoc.createElementWithName("ContentData");
    String artist = ((String) content.getValueForPath("artist[0]/@text()") != null)
        ? (String) content.getValueForPath("artist[0]/@text()")
        : "";
    contentDataElement.setAttributeValue(artist, "artist");
    String id = (String) content.getValueForPath("id[0]/@text()");
    contentDataElement.setAttributeValue(id, "id");
    String image = "";
    if ((String) content.getValueForPath("image[0]/@text()") != null) image = (String) content.getValueForPath("image[0]/@text()");
    else if ((String) content.getValueForPath("thumbnail[0]/@text()") != null) image = (String) content
        .getValueForPath("thumbnail[0]/@text()");
    else if ((String) content.getValueForPath("preview[0]/@text()") != null) image = (String) content.getValueForPath("preview[0]/@text()");
    contentDataElement.setAttributeValue(image, "image");
    String thirdPartyID = (String) content.getValueForPath("third-party-id[0]/@text()");
    contentDataElement.setAttributeValue(thirdPartyID, "thirdPartyId");
    String title = (String) content.getValueForPath("title[0]/@text()");
    contentDataElement.setAttributeValue(title, "title");
    String keyword = (String) content.getName();
    contentDataElement.setAttributeValue(keyword, "keyword");

    MBDocument sessionDoc = _session.getDocument();
    MBElement userDataElement = orderDoc.createElementWithName("UserData");
    String referrer = ((String) sessionDoc.getValueForPath("/Session[0]/@referrer") != null) ? (String) sessionDoc
        .getValueForPath("/Session[0]/@referrer") : "";
    userDataElement.setAttributeValue(referrer, "referrer");
    String clickId = ((String) sessionDoc.getValueForPath("/Session[0]/@clickId") != null) ? (String) sessionDoc
        .getValueForPath("/Session[0]/@clickId") : "";
    userDataElement.setAttributeValue(clickId, "clickId");
    String msisdn = ((String) sessionDoc.getValueForPath("/Session[0]/@msisdn") != null) ? (String) sessionDoc
        .getValueForPath("/Session[0]/@msisdn") : "";
    userDataElement.setAttributeValue(msisdn, "msisdn");
    String ip = ((String) sessionDoc.getValueForPath("/Session[0]/@ip") != null)
        ? (String) sessionDoc.getValueForPath("/Session[0]/@ip")
        : "";
    userDataElement.setAttributeValue(ip, "ip");
    String credits = ((String) sessionDoc.getValueForPath("/Session[0]/@credits") != null) ? (String) sessionDoc
        .getValueForPath("/Session[0]/@credits") : "";
    userDataElement.setAttributeValue(credits, "credits");
    String source = "nl.whatsringing.mobi";
    userDataElement.setAttributeValue(source, "source");
    String shortcode = "5959";
    userDataElement.setAttributeValue(shortcode, "shortcode");
    String contentPartnerName = "Target";
    userDataElement.setAttributeValue(contentPartnerName, "contentPartnerName");
    String token = "Z4_jlbFv2iwHCzP4aGwS";
    userDataElement.setAttributeValue(token, "token");
    String scenarioId = "WhatsringingDirectBillingScenario";
    userDataElement.setAttributeValue(scenarioId, "scenarioId");

    MBOutcome outcome = null;
    if (((String) sessionDoc.getValueForPath("/Session[0]/@loggedIn")).equalsIgnoreCase("true")) outcome = new MBOutcome(
        "OUTCOME-page_member_orderform", orderDoc);
    else outcome = new MBOutcome("OUTCOME-page_orderform", orderDoc);

    return outcome;
  }

  private MBElement locateContentByThirdPartyId(MBDocument document)
  {
    if (document == null || document.getDocumentName().equalsIgnoreCase("OrderContentDocument"))
    {
      return locateContentByThirdPartyId();
    }

    List<MBElement> elements = new ArrayList<MBElement>();
    Map<String, List<MBElement>> documentElements = document.getElements();

    // These if statements most likely only return true when coming from the home page
    if (documentElements.containsKey("realtones"))
    {
      elements.addAll(documentElements.get("realtones").get(0).getElementsWithName("realtone"));
    }
    if (documentElements.containsKey("realtones"))
    {
      elements.addAll(documentElements.get("truetones").get(0).getElementsWithName("truetone"));
    }
    if (documentElements.containsKey("polytones"))
    {
      elements.addAll(documentElements.get("polytones").get(0).getElementsWithName("polytone"));
    }
    if (documentElements.containsKey("javagames"))
    {
      elements.addAll(documentElements.get("javagames").get(0).getElementsWithName("javagame"));
    }
    if (documentElements.containsKey("wallpapers"))
    {
      elements.addAll(documentElements.get("wallpapers").get(0).getElementsWithName("wallpaper"));
    }
    if (documentElements.containsKey("minimovies"))
    {
      elements.addAll(documentElements.get("minimovies").get(0).getElementsWithName("minimovie"));
    }

    // This would be the case when you come from the realtones page, or the truetones page etc.
    if (elements.isEmpty())
    {
      Map<String, List<MBElement>> childElements = document.getElements();
      if (childElements.containsKey("realtone")) elements.addAll(childElements.get("realtone"));
      else if (childElements.containsKey("truetone")) elements.addAll(childElements.get("truetone"));
      else if (childElements.containsKey("polytone")) elements.addAll(childElements.get("polytone"));
      else if (childElements.containsKey("javagame")) elements.addAll(childElements.get("javagame"));
      else if (childElements.containsKey("wallpaper")) elements.addAll(childElements.get("wallpaper"));
      else if (childElements.containsKey("minimovie")) elements.addAll(childElements.get("minimovie"));
    }

    for (MBElement element : elements)
    {
      String tpid = element.getValueForPath("third-party-id[0]/@text()");
      if (tpid.equals(_thirdPartyId))
      {
        return element;
      }
    }

    _log.warn("Element with third-party-id " + _thirdPartyId + " not found in document!");
    return locateContentByThirdPartyId();
  }

  private MBElement locateContentByThirdPartyId()
  {
    // If document is null for some reason or the OrderContentDocument already exists, search for the content
    MBDocument sessionDoc = _session.getDocument();
    String thirdPartyId = ((String) sessionDoc.getValueForPath("/Session[0]/@third_party_id") != null) ? (String) sessionDoc
        .getValueForPath("/Session[0]/@third_party_id") : "";
    String contentType = ((String) sessionDoc.getValueForPath("/Session[0]/@content_type") != null) ? (String) sessionDoc
        .getValueForPath("/Session[0]/@content_type") : "";

    MBDocument lookupResultsRequestDoc = loadDocument("MBGenericRequest");
    Utilities.setRequestParameter(thirdPartyId, "third_party_id", lookupResultsRequestDoc);
    Utilities.setRequestParameter("1", "content_partner_id", lookupResultsRequestDoc); // 1 = Target, 2 = TheMobile
    Utilities.setRequestParameter(contentType, "type", lookupResultsRequestDoc); // realtone, truetone, polytone, javagame, wallpaper, minimovie
    MBDocument document = loadDocument(contentType, lookupResultsRequestDoc);

    MBDocument contentDoc = new MBDocument(MBMetadataService.getInstance().getDefinitionForDocumentName(contentType + "s"));
    MBElement element = contentDoc.createElementWithName(contentType);
    if (document.getElementsWithName("artist").size() > 0) element.addElement((MBElement) document.getValueForPath("/artist[0]"));
    if (document.getElementsWithName("title").size() > 0) element.addElement((MBElement) document.getValueForPath("/title[0]"));
    if (document.getElementsWithName("image").size() > 0) element.addElement((MBElement) document.getValueForPath("/image[0]"));
    if (document.getElementsWithName("preview").size() > 0) element.addElement((MBElement) document.getValueForPath("/preview[0]"));
    if (document.getElementsWithName("thumbnail").size() > 0) element.addElement((MBElement) document.getValueForPath("/thumbnail[0]"));
    if (document.getElementsWithName("id").size() > 0) element.addElement((MBElement) document.getValueForPath("/id[0]"));
    if (document.getElementsWithName("third-party-id").size() > 0) element.addElement((MBElement) document
        .getValueForPath("/third-party-id[0]"));

    return contentDoc.getValueForPath("/" + contentType + "[0]");
  }
}