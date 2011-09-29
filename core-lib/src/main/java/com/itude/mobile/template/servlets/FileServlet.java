package com.itude.mobile.template.servlets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itude.commons.exceptions.ItudeRuntimeException;
import com.itude.commons.util.FileUtil;

public class FileServlet extends HttpServlet
{
  private static final long serialVersionUID = 1L;

  private void addCacheControl(HttpServletResponse response, long now, long ttl)
  {
//    if (ttl == 0)
//    {
//      response.setDateHeader("Expires", new Date().getTime() + 24 * 60 * 60 * 1000);
//      response.setDateHeader("Last-Modified", 0);
//      response.setHeader("Cache-Control", "public, max-age=31536000");
//    }
//    else
//    {
//      response.setDateHeader("Expires", ttl);
//      response.setDateHeader("Last-Modified", 0);
//      response.setHeader("Cache-Control", "public, max-age=" + (ttl - now) / 1000);
//    }

  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {

    long now = System.currentTimeMillis();

    String key = request.getParameter("f");
    if (key == null)
    {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    StoredFile file = FileStorage.retrieveFile(key);
    if (file == null || (file.getTTL() != 0 && file.getTTL() < now))
    {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    if (request.getHeader("If-Modified-Since") != null)
    {
      response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      addCacheControl(response, now, file.getTTL());
      return;
    }

    response.setContentType(file.getMimeType());

    OutputStream out = response.getOutputStream();
    ByteArrayInputStream in = new ByteArrayInputStream(file.getData());

    try
    {
      response.setContentLength(file.getData().length);
      addCacheControl(response, now, file.getTTL());
      FileUtil.copy(in, out);
      out.flush();
    }
    catch (IOException e)
    {
      // unrecoverable
      throw new ItudeRuntimeException("There was an error reading " + key, e);
    }
    finally
    {
      out.close();
      in.close();
    }

  }

}
