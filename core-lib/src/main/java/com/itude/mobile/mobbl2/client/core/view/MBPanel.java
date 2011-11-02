package com.itude.mobile.mobbl2.client.core.view;

import com.itude.mobile.mobbl2.client.core.configuration.MBDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBPanelDefinition;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.services.MBLocalizationService;
import com.itude.mobile.mobbl2.client.core.util.StringUtilities;

public class MBPanel extends MBComponentContainer
{
  private String  _type;
  private String  _title;
  private int     _width;
  private int     _height;
  private boolean _hidden;

  public MBPanel(MBPanelDefinition definition, MBDocument document, MBComponentContainer parent)
  {
    this(definition, document, parent, true);
  }

  public MBPanel(MBPanelDefinition definition, MBDocument document, MBComponentContainer parent, boolean buildViewStructure)
  {
    super(definition, document, parent);
    setType(definition.getType());
    setWidth(definition.getWidth());
    setHeight(definition.getHeight());

    if (buildViewStructure) buildViewStructure();
  }

  protected void buildViewStructure()
  {
    MBPanelDefinition definition = (MBPanelDefinition) getDefinition();
    for (MBDefinition def : definition.getChildren())
    {
      String dataPath = null;
      if (getParent() != null) dataPath = getParent().getAbsoluteDataPath();
      else
        dataPath = getAbsoluteDataPath();

      if (def.isPreConditionValid(getDocument(), dataPath))
      {
        addChild(MBComponentFactory.getComponentFromDefinition(def, getDocument(), this));
      }
    }

    setTitle(substituteExpressions(definition.getTitle()));

  }

  public String getType()
  {
    return _type;
  }

  public void setType(String type)
  {
    _type = type;
  }

  public String getTitle()
  {
    String result = _title;

    if (_title != null)
    {
      result = _title;
    }
    else
    {
      MBPanelDefinition definition = (MBPanelDefinition) getDefinition();
      if (definition.getTitle() != null)
      {
        result = definition.getTitle();
      }
      else if (definition.getTitlePath() != null)
      {
        String path = definition.getTitlePath();
        if (!path.startsWith("/"))
        {
          path = getAbsoluteDataPath() + "/" + path;
        }

        // Do not localize data coming from documents; which would become very confusing
        return (String) getDocument().getValueForPath(path);
      }
    }

    return MBLocalizationService.getInstance().getTextForKey(result);
  }

  public String getOutcomeName()
  {
    return ((MBPanelDefinition) getDefinition()).getOutcomeName();
  }

  @Override
  public String asXmlWithLevel(int level)
  {
    String result = StringUtilities.getIndentStringWithLevel(level);
    result += "<MBPanel " + attributeAsXml("type", _type) + " " + attributeAsXml("title", _title) + " " + attributeAsXml("width", _width)
              + " " + attributeAsXml("height", _height) + " " + attributeAsXml("outcomeName", getOutcomeName()) + ">\n";

    result += childrenAsXmlWithLevel(level + 2);
    result += StringUtilities.getIndentStringWithLevel(level) + "</MBPanel>\n";

    return result;
  }

  @Override
  public String toString()
  {
    return asXmlWithLevel(0);
  }

  public void setTitle(String title)
  {
    _title = title;
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
  
  public boolean isHidden()
  {
    return _hidden;
  }
  
  public void setHidden(boolean hidden)
  {
    _hidden = hidden;
  }

  public void rebuild()
  {
    getChildren().clear();
    MBPanelDefinition panelDef = (MBPanelDefinition) getDefinition();
    String path;
    if (getParent() != null) path = getParent().getAbsoluteDataPath();
    else path = getAbsoluteDataPath();
    for (MBDefinition def : panelDef.getChildren())
    {
      if (def.isPreConditionValid(getDocument(), path))
      {
        addChild(MBComponentFactory.getComponentFromDefinition(def, getDocument(), this));
      }
    }
  }

  @Override
  public int getLeftInset()
  {
    if (getType().equals("LIST") || getType().equals("MATRIX"))
    {
      return 10;
    }
    else
    {
      return super.getLeftInset();
    }
  }

  @Override
  public int getBottomInset()
  {
    if (getType().equals("LIST") || getType().equals("MATRIX"))
    {
      return 10;
    }
    else
    {
      return super.getBottomInset();
    }
  }

  @Override
  public int getRightInset()
  {
    if (getType().equals("LIST") || getType().equals("MATRIX"))
    {
      return 10;
    }
    else
    {
      return super.getRightInset();
    }
  }

  @Override
  public int getTopInset()
  {
    if (getType().equals("LIST") || getType().equals("MATRIX"))
    {
      return 0;
    }
    else
    {
      return super.getTopInset();
    }
  }
}
