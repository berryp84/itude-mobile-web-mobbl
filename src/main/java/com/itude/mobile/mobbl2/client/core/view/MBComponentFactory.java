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
package com.itude.mobile.mobbl2.client.core.view;

import com.itude.mobile.mobbl2.client.core.configuration.MBDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBFieldDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBForEachDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBPanelDefinition;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.view.exceptions.MBInvalidComponentTypeException;

public class MBComponentFactory
{
  // This is an internal utility class; not meant to be extended or modified by applications
  public static MBComponent getComponentFromDefinition(MBDefinition definition, MBDocument document, MBComponentContainer parent)
  {

    MBComponent result = null;

    if (definition instanceof MBPanelDefinition)
    {
      result = new MBPanel((MBPanelDefinition) definition, document, parent);
    }
    else if (definition instanceof MBForEachDefinition)
    {
      result = new MBForEach((MBForEachDefinition) definition, document, parent);
    }
    else if (definition instanceof MBFieldDefinition)
    {
      result = new MBField(definition, document, parent);
    }
    else
    {
      String message = "Unsupported child type: " + definition.getClass().getSimpleName() + " in page or panel";
      throw new MBInvalidComponentTypeException(message);
    }

    return result;
  }

}
