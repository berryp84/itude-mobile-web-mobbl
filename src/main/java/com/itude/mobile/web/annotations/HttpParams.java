/*
 * (C) Copyright Itude Mobile B.V., The Netherlands
 * 
 * Licimport java.io.Serializable;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
rg/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itude.mobile.web.annotations;

import java.io.Serializable;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;

class HttpParams implements Serializable
{
  private static final long serialVersionUID = 1L;

  @Produces
  @HttpParam("")
  String getParamValue(ServletRequest request, InjectionPoint ip)
  {
    return request.getParameter(ip.getAnnotated().getAnnotation(HttpParam.class).value());
  }

  @Produces
  @HttpParam("")
  long getParsedLongParamValue(ServletRequest request, InjectionPoint ip)
  {
    String value = request.getParameter(ip.getAnnotated().getAnnotation(HttpParam.class).value());
    if (value == null || value.isEmpty()) return 0;
    try
    {
      return Long.parseLong(value);
    }
    catch (NumberFormatException e)
    {
      return 0;
    }
  }

  @Produces
  @HttpParam("")
  boolean getParsedBooleanParamValue(ServletRequest request, InjectionPoint ip)
  {
    String value = request.getParameter(ip.getAnnotated().getAnnotation(HttpParam.class).value());
    if (value == null) return false;
    else return (!"false".equalsIgnoreCase(value));
  }

  @Produces
  ServletRequest getCurrentRequest()
  {
    return (ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
  }
}
