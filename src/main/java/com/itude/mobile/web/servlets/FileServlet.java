/*
 * (C) Copyright Itude Mobile B.V., The Netherlands
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itude.mobile.web.servlets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itude.mobile.web.exceptions.ItudeRuntimeException;
import com.itude.mobile.web.util.FileUtil;

public class FileServlet extends HttpServlet
{
  private static final long serialVersionUID = 1L;

  private void addCacheControl(HttpServletResponse response, long now, long ttl)
  {
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
