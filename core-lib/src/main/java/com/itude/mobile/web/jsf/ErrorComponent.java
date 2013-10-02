/*
 * (C) Copyright ItudeMobile.
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
package com.itude.mobile.web.jsf;

import java.io.IOException;
import java.io.StringWriter;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class ErrorComponent extends UIComponentBase
{

  @Override
  public String getFamily()
  {
    return "MOBBL";
  }

  @Override
  public void encodeAll(FacesContext context) throws IOException
  {
    ResponseWriter original = context.getResponseWriter();
    try
    {
      StringWriter result = new StringWriter();
      ResponseWriter clonedWriter = context.getResponseWriter().cloneWithWriter(result);
      context.setResponseWriter(clonedWriter);
      super.encodeAll(context);
      clonedWriter.flush();
      original.write(result.getBuffer().toString());
    }
    catch (Exception e)
    {
      e.printStackTrace();
      handleError(e, original);
    }
    finally
    {
      context.setResponseWriter(original);
    }
  }

  protected void handleError(Exception e, ResponseWriter writer) throws IOException
  {
    writer.writeText("Error occurred: " + e.getMessage(), null);
  }
}
