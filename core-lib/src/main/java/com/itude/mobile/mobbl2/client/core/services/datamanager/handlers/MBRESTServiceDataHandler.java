package com.itude.mobile.mobbl2.client.core.services.datamanager.handlers;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;

import com.itude.commons.exceptions.ItudeRuntimeException;
import com.itude.mobile.mobbl2.client.core.configuration.webservices.MBEndPointDefinition;
import com.itude.mobile.mobbl2.client.core.controller.MBApplicationFactory;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.model.MBDocumentFactory;
import com.itude.mobile.mobbl2.client.core.services.MBLocalizationService;
import com.itude.mobile.mobbl2.client.core.services.MBMetadataService;
import com.itude.mobile.mobbl2.client.core.services.MBResultListener;
import com.itude.mobile.mobbl2.client.core.services.MBResultListenerDefinition;
import com.itude.mobile.mobbl2.client.core.services.datamanager.handlers.exceptions.MBServerErrorException;

public class MBRESTServiceDataHandler extends MBWebserviceDataHandler implements Serializable
{
  private static final long    serialVersionUID          = 1L;
  private static final Logger  _log                      = Logger.getLogger(MBRESTServiceDataHandler.class);

  MBApplicationFactory         _applicationFactory = MBApplicationFactory.getInstance();

  // TODO: put in config file
  protected static final int   MAX_CONNECTIONS_PER_ROUTE = 20;
  protected static final int   MAX_TOTAL_CONNECTIONS     = 20;
  protected static final int   DEFAULT_TIMEOUT_SOCKET    = 30000;
  protected static final int   DEFAULT_TIMEOUTCONNECTION = 30000;
  private static final boolean ALLOW_ANY_CERTIFICATE     = true;

  private String               _documentFactoryType;

  public MBRESTServiceDataHandler()
  {
    super();
  }

  public String getDocumentFactoryType()
  {
    return _documentFactoryType;
  }

  public void setDocumentFactoryType(String documentFactoryType)
  {
    _documentFactoryType = documentFactoryType;
  }

  @Override
  public MBDocument doLoadDocument(String documentName, MBDocument args)
  {
    MBEndPointDefinition endPoint = getEndPointForDocument(documentName);
    
    if (endPoint != null)
    {
      _log.debug("MBRESTServiceDataHandler:loadDocument " + documentName + " from " + endPoint.getEndPointUri());

      String dataString = null;
      MBDocument responseDoc = null;
      String body = args.getValueForPath("/*[0]").toString();
      try
      {
        HttpPost httpPost = new HttpPost(endPoint.getEndPointUri());
        // Content-Type must be set because otherwise the MidletCommandProcessor servlet cannot read the XML
        httpPost.setHeader("Content-Type", "text/xml");
        if (body != null)
        {
          httpPost.setEntity(new StringEntity(body));
        }

        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        int timeoutConnection = DEFAULT_TIMEOUTCONNECTION;
        // Set the default socket timeout (SO_TIMEOUT) 
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = DEFAULT_TIMEOUT_SOCKET;

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", 8080, PlainSocketFactory.getSocketFactory()));
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(registry, timeoutConnection, TimeUnit.MILLISECONDS);
        cm.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        cm.setDefaultMaxPerRoute(MAX_CONNECTIONS_PER_ROUTE);

        HttpClient httpClient = new DefaultHttpClient(cm, httpParameters);
        if (endPoint.getTimeout() > 0)
        {
          timeoutSocket = endPoint.getTimeout() * 1000;
          timeoutConnection = endPoint.getTimeout() * 1000;
        }
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        if (ALLOW_ANY_CERTIFICATE) allowAnyCertificate(httpClient);

        HttpResponse httpResponse = httpClient.execute(httpPost);
        int responseCode = httpResponse.getStatusLine().getStatusCode();
        String responseMessage = httpResponse.getStatusLine().getReasonPhrase();
        if (responseCode != HttpStatus.SC_OK)
        {
          _log.error("MBRESTServiceDataHandler.loadDocument: Received HTTP responseCode=" + responseCode + ": " + responseMessage);
        }

        HttpEntity entity = httpResponse.getEntity();
        if (entity != null)
        {
          InputStream inStream = entity.getContent();
          ByteArrayOutputStream bos = new ByteArrayOutputStream();
          byte[] buffer = new byte[1024];
          int i = inStream.read(buffer);
          while (i > -1)
          {
            bos.write(buffer, 0, i);
            i = inStream.read(buffer);
          }
          inStream.close();
          dataString = new String(bos.toByteArray());
        }

        boolean serverErrorHandled = false;

        for (MBResultListenerDefinition lsnr : endPoint.getResultListeners())
        {
          if (lsnr.matches(dataString))
          {
            MBResultListener rl = _applicationFactory.createResultListener(lsnr.getName());
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
                                                                            getDocumentFactoryType(),
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
        // debug in stead of info because it can contain a password
        _log.debug("Sent xml:\n" + body);
        _log.info("Received:\n" + dataString);
        if (e instanceof RuntimeException) throw (RuntimeException) e;
        else throw new ItudeRuntimeException(e);
      }
      return responseDoc;
    }
    else
    {
      _log.warn("No endpoint defined for document name " + documentName);
      return null;
    }
  }

  @Override
  public void storeDocument(MBDocument document)
  {
  }

  private void allowAnyCertificate(HttpClient httpClient) throws KeyManagementException, NoSuchAlgorithmException
  {
    SSLContext ctx = SSLContext.getInstance("TLS");
    X509TrustManager tm = new X509TrustManager()
    {

      public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException
      {
      }

      public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException
      {
      }

      public X509Certificate[] getAcceptedIssuers()
      {
        return null;
      }
    };
    ctx.init(null, new TrustManager[]{tm}, null);
    SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    ClientConnectionManager ccm = httpClient.getConnectionManager();
    SchemeRegistry sr = ccm.getSchemeRegistry();
    sr.register(new Scheme("https", 443, ssf));
  }

}
