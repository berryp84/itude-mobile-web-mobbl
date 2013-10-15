/*
 * (C) Copyright ItudeMobile.
 * 
 * Licimport javax.enterprise.util.AnnotationLiteral;
 "License");
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
package com.itude.mobile.web.annotations;

import javax.enterprise.util.AnnotationLiteral;

public class PageQualifier extends AnnotationLiteral<ForPage> implements ForPage
{
  private static final long serialVersionUID = 1L;

  private final String      _value;

  public PageQualifier(String value)
  {
    _value = value;
  }

  @Override
  public String value()
  {
    return _value;
  }

}
