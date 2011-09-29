package com.itude.mobile.template.jsf.handlers;

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
