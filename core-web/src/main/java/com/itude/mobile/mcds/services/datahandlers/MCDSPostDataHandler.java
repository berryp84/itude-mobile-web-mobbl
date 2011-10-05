package com.itude.mobile.mcds.services.datahandlers;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import com.itude.commons.exceptions.ItudeRuntimeException;
import com.itude.mobile.mobbl2.client.core.configuration.webservices.MBEndPointDefinition;
import com.itude.mobile.mobbl2.client.core.controller.MBApplicationFactory;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.model.MBDocumentFactory;
import com.itude.mobile.mobbl2.client.core.model.MBElement;
import com.itude.mobile.mobbl2.client.core.model.MBElementContainer;
import com.itude.mobile.mobbl2.client.core.services.MBLocalizationService;
import com.itude.mobile.mobbl2.client.core.services.MBMetadataService;
import com.itude.mobile.mobbl2.client.core.services.MBResultListener;
import com.itude.mobile.mobbl2.client.core.services.MBResultListenerDefinition;
import com.itude.mobile.mobbl2.client.core.services.datamanager.handlers.MBRESTServiceDataHandler;
import com.itude.mobile.mobbl2.client.core.services.datamanager.handlers.exceptions.MBServerErrorException;

public class MCDSPostDataHandler extends MBRESTServiceDataHandler {
	private static final Logger _log = Logger
			.getLogger(MCDSPostDataHandler.class);

	@SuppressWarnings("unused")
	@Override
	public MBDocument loadDocument(String documentName, MBDocument args) {
		MBEndPointDefinition endPoint = getEndPointForDocument(documentName);
		String URL = endPoint.getEndPointUri();

		if (endPoint != null) {
			_log.debug("MCDSDataHandler:loadDocument " + documentName
					+ " from " + URL);

			String dataString = null;
			MBDocument responseDoc = null;
			try {
				//HttpDelegate httpDelegate = HttpDelegate.getInstance();
			    //HttpResponse response = null;
			    //Header[] requestHeaders = new Header[] {new Header("Content-Type", "application/x-www-form-urlencoded")};

			    // fake a browser
			    //String userAgent = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_7; en-us) AppleWebKit/533.21.1 (KHTML, like Gecko) Version/5.0.5 Safari/533.21.1";
			    //ArrayList<Cookie> reqCookies = null;
			    //NameValuePair[] postParams = null;
			    //[name=msisdn, value=31630901586, name=password, value=3aef, name=token, value=Z4_jlbFv2iwHCzP4aGwS, name=_MWP_APPLICATION_ID, value=, name=_MWP_USER, value=3, name=scenario_id, value=Mob247DirectBillingScenario, name=2505, value=Login, name=_MWP_ID, value=2505, name=_MWP_PROJECT, value=mob24-7new]
			    //[name=msisdn, value=31630901586, name=password, value=3aef, name=token, value=jlbFv2iwHCzP4aGwS, name=scenario_id, value=Mob247DirectBillingScenario]
				HttpClient client = new HttpClient();
				PostMethod method = new PostMethod(URL);

				if (args != null) {
			    //    postParams = new NameValuePair[((MBElementContainer) args.getValueForPath("/Request[0]")).getElementsWithName("Parameter").size()];
					//int i=0;
					for(MBElement element : ((MBElementContainer) args.getValueForPath("/Request[0]")).getElementsWithName("Parameter"))
					{
						method.addParameter(element.getValueForAttribute("key"), element.getValueForAttribute("value"));
				        //postParams[i] = new NameValuePair(element.getValueForAttribute("key"), element.getValueForAttribute("value"));
				        //i++;
					}
				}
				
			    //response = httpDelegate.connectPost(URL, userAgent, reqCookies, postParams, requestHeaders, false);
			    //String htmlText = new String(response.getResponseBody(), "UTF-8");
				//System.out.println(htmlText);
				// Execute the POST method
				int statusCode = client.executeMethod(method);
				if (statusCode != -1) {
					dataString = method.getResponseBodyAsString();
					method.releaseConnection();
					//System.out.println(dataString);
				}

				boolean serverErrorHandled = false;

				for (MBResultListenerDefinition lsnr : endPoint
						.getResultListeners()) {
					if (lsnr.matches(dataString)) {
						MBResultListener rl = MBApplicationFactory
								.getInstance().createResultListener(
										lsnr.getName());
						rl.handleResult(dataString, args, lsnr);
						serverErrorHandled = true;
					}
				}

				/*
				 * if (delegate.err != nil) { String errorMessage = null;
				 * //[[UIApplication sharedApplication]
				 * setNetworkActivityIndicatorVisible:NO];
				 * Log.w("MOBBL","An error ("
				 * +errorMessage+") occured while accessing endpoint "
				 * +endPoint.getEndPointUri()); throw new
				 * NetworkErrorException(MBLocalizationService
				 * .getInstance().textForKey((errorMessage); } //[[UIApplication
				 * sharedApplication] setNetworkActivityIndicatorVisible:NO];
				 */
				if (dataString != null) {
					byte[] data = dataString.getBytes();
					responseDoc = MBDocumentFactory.getInstance()
							.getDocumentWithData(
									data,
									MBDocumentFactory.PARSER_XML,
									MBMetadataService.getInstance()
											.getDefinitionForDocumentName(
													documentName));
				}
				// if the response document is empty and unhandled by endpoint
				// listeners let the user know there is a problem
				if (!serverErrorHandled && responseDoc == null) {
					String msg = MBLocalizationService
							.getInstance()
							.getTextForKey(
									"The server returned an error. Please try again later");
					// if(delegate.err != nil) {
					// msg = [NSString stringWithFormat:@"%@ %@: %@", msg,
					// delegate.err.domain, delegate.err.code];
					// }
					throw new MBServerErrorException(msg);
				}
			}
			// TODO: clean up exception handling
			catch (Exception e) {
				// Trace in stead of debug because it can contain a password
				_log.info("Received:\n" + dataString);
				if (e instanceof RuntimeException)
					throw (RuntimeException) e;
				else
					throw new ItudeRuntimeException(e);
			}
			return responseDoc;
		} else {
			_log.warn("No endpoint defined for document name " + documentName);
			return null;
		}
	}
}
