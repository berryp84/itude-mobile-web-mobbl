package com.itude.mobile.mobbl2.client.core.configuration.mvc;

import java.util.ArrayList;
import java.util.List;

import com.itude.mobile.mobbl2.client.core.configuration.MBDefinition;
import com.itude.mobile.mobbl2.client.core.util.StringUtilities;
import com.itude.mobile.mobbl2.client.core.view.MBConditionalDefinition;
import com.itude.mobile.mobbl2.client.core.view.MBStylableDefinition;

public class MBPanelDefinition extends MBConditionalDefinition implements MBStylableDefinition
{
  private String             _type;
  private String             _style;
  private String             _title;
  private String             _titlePath;
  private int                _width;
  private int                _height;
  private List<MBDefinition> _children;
  private String 			 _outcomeName;

  public MBPanelDefinition()
  {
    _children = new ArrayList<MBDefinition>();
  }

  @Override
  public void addChildElement(MBDefinition definition)
  {
    addChild(definition);
  }

  @Override
  public String asXmlWithLevel(int level)
  {
    String result = StringUtilities.getIndentStringWithLevel(level) + "<Panel width='" + _width + "' height='" + _height + "' type='"
                    + _type + "'" + getAttributeAsXml("title", _title) + getAttributeAsXml("titlePath", _titlePath)
                    + getAttributeAsXml("style", _style) + getAttributeAsXml("outcome", _outcomeName) + ">\n";
    for (MBDefinition child : _children)
    {
      result += child.asXmlWithLevel(level + 2);
    }
    result += StringUtilities.getIndentStringWithLevel(level) + "</Panel>\n";

    return result;
  }

  public void addChild(MBDefinition child)
  {
    _children.add(child);
  }

  public String getType()
  {
    return _type;
  }

  public void setType(String type)
  {
    _type = type;
  }

  public String getStyle()
  {
    return _style;
  }

  public void setStyle(String style)
  {
    _style = style;
  }

  public String getTitle()
  {
    return _title;
  }

  public void setTitle(String title)
  {
    _title = title;
  }

  public String getTitlePath()
  {
    return _titlePath;
  }

  public void setTitlePath(String titlePath)
  {
    _titlePath = titlePath;
  }

  public List<MBDefinition> getChildren()
  {
    return _children;
  }

  public void setChildren(List<MBDefinition> children)
  {
    _children = children;
  }

  public int getWidth()
  {
    return _width;
  }

  public void setWidth(int width)
  {
    _width = width;
  }

  public int getHeight()
  {
    return _height;
  }

  public void setHeight(int height)
  {
    _height = height;
  }
  
  public String getOutcomeName()
  {
    return _outcomeName;
  }

  public void setOutcomeName(String outcomeName)
  {
    _outcomeName = outcomeName;
  }

}
