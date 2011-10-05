package com.itude.mobile.mcds.actions;

import javax.inject.Named;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.Logger;

import com.itude.mobile.mobbl.server.http.HttpDelegate;
import com.itude.mobile.mobbl.server.http.HttpResponse;
import com.itude.mobile.mobbl.server.util.XpathUtil;
import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.template.actions.GenericAction;

@Named("RequestContentAction")
public class RequestContentAction extends GenericAction 
{
	private static final long serialVersionUID = 1L;
	
	private static final Logger _log                = Logger.getLogger(RequestContentAction.class);
	
	@Override
	public MBOutcome execute(MBDocument document, String path) 
	{	
		return this.execute(document, path, null);
	}

	@Override
    public MBOutcome execute(MBDocument document, String path, String outcomeName) 
    {
        String msisdn = document.getValueForPath("./UserData[0]/@msisdn");
        String scenario_id = document.getValueForPath("./UserData[0]/@scenarioId");
        String shortcode = document.getValueForPath("./UserData[0]/@shortcode");
        String keyword = document.getValueForPath("./ContentData[0]/@keyword");
        String third_party_id = document.getValueForPath("./ContentData[0]/@thirdPartyId");
        String token = document.getValueForPath("./UserData[0]/@token");
        String referrer = document.getValueForPath("./UserData[0]/@referrer");
        String click_id = document.getValueForPath("./UserData[0]/@clickId");
        String content_partner_name = document.getValueForPath("./UserData[0]/@contentPartnerName");
        String source = document.getValueForPath("./UserData[0]/@source");
        String ip = document.getValueForPath("./UserData[0]/@msisdn");

        NameValuePair[] formContent = {
            new NameValuePair("msisdn", msisdn),
            new NameValuePair("scenario_id", scenario_id),
            new NameValuePair("shortcode", shortcode),
            new NameValuePair("keyword", keyword),
            new NameValuePair("third_party_id", third_party_id),
            new NameValuePair("token", token),
            new NameValuePair("keyword", keyword),
            new NameValuePair("referrer", referrer),
            new NameValuePair("click_id", click_id),
            new NameValuePair("content_partner_name", content_partner_name),
            new NameValuePair("source", source),
            new NameValuePair("ip", ip)
            };

        // connect with whatsringing
        String url = "http://dl.whatsringing.mobi/downloads/request_content";
        HttpDelegate delegate = HttpDelegate.getInstance();
        HttpResponse delegateResponse = delegate.connectPost(url, formContent);
        
        // evaluate response
        byte[] result = delegateResponse.getResponseBody();
        _log.debug(new String(result));
        
        String errorMessage = XpathUtil.getValueForKey(result, "/error/message");
        boolean containsError = errorMessage != null;
        _log.debug("error: " + containsError);
        
        if (containsError)
        {
          // TODO: if errorMessage doesn't equal "Not enough credits to download new content", display the contents of errorMessage
          return new MBOutcome("limit_reached", loadDocument("MBEmptyDoc"));
        }
        else
        {
          String link = XpathUtil.getValueForKey(result, "/success/url");
          MBDocument responseDoc = loadDocument("OrderResponseDocument");
          responseDoc.setValue(link, "DownloadLink[0]/@text()");
          return new MBOutcome("OUTCOME-page_member_download_content", responseDoc);
        }
    }
}