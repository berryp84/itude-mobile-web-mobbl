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
package com.itude.mobile.web.util;

import com.itude.mobile.mobbl.core.model.MBElementContainer;

public class FieldSetter
{
  private final MBElementContainer _container;
  private final String             _path;

  public FieldSetter(MBElementContainer container, String path)
  {
    _container = container;
    _path = path;
  }

  public void setValue(String value)
  {
    // No notifyValueChanged will be called, but AFIK, this isn't used anyway
    _container.setValue(value, _path);
  }

  public String getValue()
  {
    return _container.getValueForPath(_path);
  }
}
