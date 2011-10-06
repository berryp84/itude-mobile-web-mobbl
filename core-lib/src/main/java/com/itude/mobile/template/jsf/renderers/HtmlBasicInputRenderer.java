package com.itude.mobile.template.jsf.renderers;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.itude.commons.util.Base64;
import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;

public class HtmlBasicInputRenderer extends com.sun.faces.renderkit.html_basic.TextRenderer
{
  private static final Attribute[] INPUT_ATTRIBUTES = AttributeManager.getAttributes(AttributeManager.Key.INPUTTEXT);

  @Override
  public String convertClientId(FacesContext context, String clientId)
  {
    int hashCode = clientId.hashCode();
    return Base64.encodeLong(hashCode);
  }

  @Override
  protected void getEndTextToRender(FacesContext context, UIComponent component, String currentValue) throws IOException
  {

    ResponseWriter writer = context.getResponseWriter();
    assert (writer != null);

    String styleClass = (String) component.getAttributes().get("styleClass");
    String placeHolder = (String) component.getAttributes().get("placeholder");
    String type = (String) component.getAttributes().get("type");

    writer.startElement("input", component);
    writeIdAttributeIfNecessary(context, writer, component);
    if (type != null) writer.writeAttribute("type", type, "type");
    else writer.writeAttribute("type", "text", "type");
    writer.writeAttribute("name", (component.getClientId(context)), "clientId");

    // only output the autocomplete attribute if the value
    // is 'off' since its lack of presence will be interpreted
    // as 'on' by the browser
    if ("off".equals(component.getAttributes().get("autocomplete")))
    {
      writer.writeAttribute("autocomplete", "off", "autocomplete");
    }

    // render default text specified
    if (currentValue != null)
    {
      writer.writeAttribute("value", currentValue, "value");
    }

    if (placeHolder != null)
    {
      writer.writeAttribute("placeholder", placeHolder, "placeholder");
    }

    if (null != styleClass)
    {
      writer.writeAttribute("class", styleClass, "styleClass");
    }

    // style is rendered as a passthur attribute
    RenderKitUtils.renderPassThruAttributes(context, writer, component, INPUT_ATTRIBUTES, getNonOnChangeBehaviors(component));
    RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

    RenderKitUtils.renderOnchange(context, component, false);

    writer.endElement("input");

  }

}