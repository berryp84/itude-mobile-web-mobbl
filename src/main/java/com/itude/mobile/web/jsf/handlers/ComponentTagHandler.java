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
package com.itude.mobile.web.jsf.handlers;

import java.io.IOException;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

import com.itude.mobile.mobbl2.client.core.view.MBComponent;

public class ComponentTagHandler extends TagHandler
{

  private final TagAttribute _value;

  public ComponentTagHandler(TagConfig config)
  {
    super(config);
    _value = getRequiredAttribute("definition");
  }

  @Override
  public void apply(FaceletContext ctx, UIComponent parent) throws IOException
  {

    MBComponent blaat = (MBComponent) _value.getObject(ctx, MBComponent.class);

    ValueExpression ve = ctx.getExpressionFactory().createValueExpression(blaat, MBComponent.class);
    ctx.getVariableMapper().setVariable("comp", ve);

    this.nextHandler.apply(ctx, parent);

  }

}
