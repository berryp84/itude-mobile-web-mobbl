package com.itude.mobile.web.servlets;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CacheControlFilter implements Filter
{

  @Override
  public void init(FilterConfig filterConfig) throws ServletException
  {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
  {
    chain.doFilter(request, response);
   ((HttpServletResponse)response).addHeader("Cache-Control", "no-transform");

  }

  @Override
  public void destroy()
  {

  }

}
