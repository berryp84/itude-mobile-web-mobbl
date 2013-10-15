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
package com.itude.mobile.mobbl2.client.core.view;

import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBPageDefinition;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.view.helpers.MBBounds;

/**
 * MBConditionalPage is probably used in android, but it isn't in mobile web
 */
public class MBConditionalPage extends MBPage
{
  public MBConditionalPage(MBPageDefinition definition, MBDocument document, String rootPath, Object viewState, MBBounds bounds)
  {
    super(definition, document, rootPath);
  }

  private MBPageDefinition _definitionWhenFalse;
  private MBPageDefinition _definitionWhenTrue;

  public MBPageDefinition getDefinitionWhenFalse()
  {
    return _definitionWhenFalse;
  }

  public void setDefinitionWhenFalse(MBPageDefinition definitionWhenFalse)
  {
    _definitionWhenFalse = definitionWhenFalse;
  }

  public MBPageDefinition getDefinitionWhenTrue()
  {
    return _definitionWhenTrue;
  }

  public void setDefinitionWhenTrue(MBPageDefinition definitionWhenTrue)
  {
    _definitionWhenTrue = definitionWhenTrue;
  }

  public Object initWithDefinitionWhenTrue(MBPageDefinition definitionWhenTrue, MBPageDefinition definitionWhenFalse,
                                           Object viewController, MBDocument document, String rootPath, Object viewState)
  {
    return null;
  }

  public Object initWithDefinitionWhenTrue(MBPageDefinition definitionWhenTrue, MBPageDefinition definitionWhenFalse, MBDocument document,
                                           String rootPath, Object viewState, Object bounds)
  {
    return null;
  }

}
