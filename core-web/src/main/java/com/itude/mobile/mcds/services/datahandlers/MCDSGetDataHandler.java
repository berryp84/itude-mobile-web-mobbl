package com.itude.mobile.mcds.services.datahandlers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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

public class MCDSGetDataHandler extends MBRESTServiceDataHandler
{
  private static final Logger _log = Logger.getLogger(MCDSGetDataHandler.class);

  @Override
  public MBDocument loadDocument(String documentName, MBDocument args)
  {
    MBEndPointDefinition endPoint = getEndPointForDocument(documentName);
    String URL = endPoint.getEndPointUri();

    if (args != null)
    {
      // TODO: why is this if statement here?
      if (documentName.equalsIgnoreCase("realtones") || documentName.equalsIgnoreCase("polytones")
          || documentName.equalsIgnoreCase("truetones") || documentName.equalsIgnoreCase("javagames")
          || documentName.equalsIgnoreCase("wallpapers") || documentName.equalsIgnoreCase("minimovies")
          || documentName.equalsIgnoreCase("results") || documentName.equalsIgnoreCase("downloadContent")
          || documentName.equalsIgnoreCase("realtone") || documentName.equalsIgnoreCase("polytone")
          || documentName.equalsIgnoreCase("truetone") || documentName.equalsIgnoreCase("javagame")
          || documentName.equalsIgnoreCase("wallpaper") || documentName.equalsIgnoreCase("minimovie"))
      {
        String params = "";
        for (MBElement element : ((MBElementContainer) args.getValueForPath("/Request[0]")).getElementsWithName("Parameter"))
        {
          if (params.length() > 0)
          {
            params += "&" + element.getValueForAttribute("key") + "=" + element.getValueForAttribute("value");
          }
          else
          {
            params += element.getValueForAttribute("key") + "=" + element.getValueForAttribute("value");
          }
        }

        if (params.length() > 0)
        {
          URL += "?" + params;
        }
      }
    }

    _log.debug("MCDSDataHandler:loadDocument " + documentName + " from " + URL);

    String dataString = null;
    MBDocument responseDoc = null;
    try
    {
      HttpClient client = new DefaultHttpClient();
      HttpGet get = new HttpGet(URL);
      get.addHeader("Accept", "application/xml");
      get.addHeader("Content-Type", "application/xml");
      HttpResponse httpResponse = client.execute(get);

      HttpEntity entity = httpResponse.getEntity();
      if (entity != null)
      {
        InputStream inStream = entity.getContent();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int i = inStream.read(buffer);;
        while (i > -1)
        {
          bos.write(buffer, 0, i);
          i = inStream.read(buffer);
        }
        inStream.close();
        if (File.separatorChar == '\\')
        {
          // The default doesn't work with windows, but for some weird reason, UTF-8 doesn't work either
          // Big5 will result in something readable (although special characters won't be the correct ones)
          dataString = new String(bos.toByteArray(), "Big5");
        }
        else
        {
          _log.info("Decoding using the following charset: " + Charset.defaultCharset().name());
          dataString = new String(bos.toByteArray());
        }
      }
      boolean serverErrorHandled = false;

      for (MBResultListenerDefinition lsnr : endPoint.getResultListeners())
      {
        if (lsnr.matches(dataString))
        {
          MBResultListener rl = MBApplicationFactory.getInstance().createResultListener(lsnr.getName());
          rl.handleResult(dataString, args, lsnr);
          serverErrorHandled = true;
        }
      }

      /*       if (delegate.err != nil) {
                 String errorMessage = null;
                 //[[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
                 Log.w("MOBBL","An error ("+errorMessage+") occured while accessing endpoint "+endPoint.getEndPointUri());
                 throw new NetworkErrorException(MBLocalizationService.getInstance().textForKey((errorMessage);
             }
             //[[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
      */
      if (dataString != null)
      {
        byte[] data = dataString.getBytes();
        responseDoc = MBDocumentFactory.getInstance().getDocumentWithData(data,
                                                                          MBDocumentFactory.PARSER_XML,
                                                                          MBMetadataService.getInstance()
                                                                              .getDefinitionForDocumentName(documentName));
      }
      // if the response document is empty and unhandled by endpoint listeners let the user know there is a problem
      if (!serverErrorHandled && responseDoc == null)
      {
        String msg = MBLocalizationService.getInstance().getTextForKey("The server returned an error. Please try again later");
        //                if(delegate.err != nil) {
        //                    msg = [NSString stringWithFormat:@"%@ %@: %@", msg, delegate.err.domain, delegate.err.code];
        //                }
        throw new MBServerErrorException(msg);
      }
    }
    // TODO: clean up exception handling
    catch (Exception e)
    {
      // Trace in stead of debug because it can contain a password
      _log.info("Received:\n" + dataString);
      if (e instanceof RuntimeException) throw (RuntimeException) e;
      else throw new ItudeRuntimeException(e);
    }
    return responseDoc;
  }
}
