/*
 * (C) Copyright ItudeMobile.
 * 
 * Licimport javax.enterprise.util.AnnotationLiteral;
import javax.inject.Named;
 use this file except in compliance with the License.
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
package com.itude.mobile.web.annotations;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Named;

public class NamedQualifier extends AnnotationLiteral<Named> implements Named
{
  private static final long serialVersionUID = 1L;

  private final String      _value;

  public NamedQualifier(String value)
  {
    _value = value;
  }

  @Override
  public String value()
  {
    return _value;
  }
}
