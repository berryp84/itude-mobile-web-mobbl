package com.itude.mobile.web.servlets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
 
   /**
    * A servlet filter that simply adds all header specified in its config to replies the filter is mapped to. An
    * example would be to set the cache control max age:
    * <p/>
    * <filter> <filter-name>CacheControlFilter</filter-name> <filter-class>filter.ReplyHeaderFilter</filter-class>
    * <init-param> <param-name>Cache-Control</param-name> <param-value>max-age=3600</param-value> </init-param>
    * </filter>
    * <p/>
    * <filter-mapping> <filter-name>CacheControlFilter</filter-name> <url-pattern>/images/*</url-pattern>
    * </filter-mapping> <filter-mapping> <filter-name>CacheControlFilter</filter-name> <url-pattern>*.js</url-pattern>
    * </filter-mapping>
    *
    * @author Scott.Stark@jboss.org
    * @version $Revison:$
    */
public class ReplyHeaderFilter implements Filter
{
   private String[][] replyHeaders = {{}};
   
   private static final String DEFAULT_DATE_FORMAT = "E, d MMM yyyy HH:mm:ss 'GMT'";
   private static ThreadLocal<SimpleDateFormat> GMTDateFormatter = new ThreadLocal<SimpleDateFormat>()
   {
     @Override
     protected SimpleDateFormat initialValue()
     {
       return new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.ENGLISH);
     }
   };
 
   public void init(FilterConfig config)
   {
      @SuppressWarnings("unchecked")
      Enumeration<String> names = (Enumeration<String>)config.getInitParameterNames();
      ArrayList<String[]> tmp = new ArrayList<String[]>();
      while (names.hasMoreElements())
      {
         String name = names.nextElement();
         String value = config.getInitParameter(name);
         String[] pair = {name, value};
         tmp.add(pair);
      }
      replyHeaders = new String[tmp.size()][2];
      tmp.toArray(replyHeaders);
   }
 
   public void doFilter(ServletRequest request, ServletResponse response,
                        FilterChain chain)
      throws IOException, ServletException
   {
      // Apply the headers
      HttpServletResponse httpResponse = (HttpServletResponse)response;
      for (int n = 0; n < replyHeaders.length; n++)
      {
         String name = replyHeaders[n][0];
         String value = replyHeaders[n][1];
         httpResponse.setHeader(name, value);
      }
 
      // Set the Expires header to in 3 days. I have no idea why the original author dit that
//      long relExpiresInMillis = System.currentTimeMillis() + (1000 * 259200);
//      httpResponse.setHeader("Expires", getGMTTimeString(relExpiresInMillis));
      chain.doFilter(request, response);
   }
 
   public static String getGMTTimeString(long milliSeconds)
   {
      return GMTDateFormatter.get().format(new Date(milliSeconds));
   }
   
   public void destroy()
   {
   }
}