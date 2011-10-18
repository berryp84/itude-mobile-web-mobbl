package com.itude.mobile.web.util;

import java.util.Collection;

import com.itude.commons.jsf.util.ELUtil;
import com.itude.mobile.mobbl2.client.core.services.MBLocalizationService;
import com.itude.mobile.mobbl2.client.core.view.MBComponent;
import com.itude.mobile.mobbl2.client.core.view.MBComponentContainer;
import com.itude.mobile.mobbl2.client.core.view.MBField;
import com.itude.mobile.mobbl2.client.core.view.MBForEach;
import com.itude.mobile.mobbl2.client.core.view.MBPage;
import com.itude.mobile.mobbl2.client.core.view.MBPanel;
import com.itude.mobile.mobbl2.client.core.view.MBRow;
import com.itude.mobile.web.controllers.ApplicationController;

public class PageHelper
{
  
  public static String componentType(MBComponent component)
  {
    if (component instanceof MBPage) return "page";
    else if (component instanceof MBField) return "field";
    else if (component instanceof MBForEach) return "forEach";
    else if (component instanceof MBRow) return "row";
    else if (component instanceof MBPanel) return "panel";
    else return "unknown";
  }

  public static String handleOutcome(String outcome)
  {
    ApplicationController controller = ELUtil.getValue("controller", ApplicationController.class);
    controller.handleOutcome(outcome);
    return ".";
  }

  public static String value(MBField field)
  {
    if (field.getLabel() != null) return field.getLabel();
    else if (field.getValue() != null || field.getValueIfNil() != null) return field.getFormattedValue();

    else return "";
  }
  
  public static String getTranslatedPathValue(MBField field)
  {
    String path = field.getAbsoluteDataPath();
    String originalValue = (String) field.getDocument().getValueForPath(path);
    return MBLocalizationService.getInstance().getTextForKey(originalValue);
  }

  public static Integer size(Collection<?> component)
  {
    return component.size();
  }

  public static Integer compareWithMarker(MBComponentContainer row, MBField column)
  {
    
    MBField marker = null;
    MBField primary = null;
    
    if (!"DIFFABLE_PRIMARY".equals(column.getStyle()) && !"DIFFABLE_SECONDARY".equals(column.getStyle())) return 0;

    if (column.getValue() == null)
      return 0;
    
    for (MBComponent child : row.getChildren())
    {
      if (child instanceof MBField)
      {
        MBField field = (MBField) child;
        if ("DIFFABLE_MARKER".equals(field.getStyle())) marker = field;
        if ("DIFFABLE_PRIMARY".equals(field.getStyle())) primary = field;
      }
    }

    if (marker == null || marker.getValue() == null) return 0;
    if (primary == null || primary.getValue() == null) return 0;

    try
    {
      double markerValue = Double.parseDouble(marker.getValue());
      double columnValueD = Double.parseDouble(primary.getValue());

      if (markerValue > columnValueD) return -1;
      else return 1;
    }
    catch (NumberFormatException e)
    {
      return 0;
    }
  }

  public static String getStyleClassFor(MBField field)
  {
    int delta = compareWithMarker(field.getParent(), field);

    if (delta == 1 || (field.getStyle() != null && field.getValue() != null && "DIFFABLE_SECONDARY".equals(field.getStyle()) && Double.parseDouble(field.getValue()) > 0)) return "POSITIVE";
    if (delta == -1 || (field.getStyle() != null && field.getValue() != null && "DIFFABLE_SECONDARY".equals(field.getStyle()) && Double.parseDouble(field.getValue()) < 0)) return "NEGATIVE";

    if (!"DIFFABLE_PRIMARY".equals(field.getStyle()) && !"DIFFABLE_SECONDARY".equals(field.getStyle()))
      return "";
    
     return "NEUTRAL";
  }
  
  public static Integer nonHiddenChildren(MBComponentContainer row)
  {
    int nonHidden = -2;
    for (MBComponent child :  row.getChildren())
    {
      if (child instanceof MBField)
      {
        MBField field = (MBField)child;
        if (!field.isHidden())
          nonHidden++;
      }
    }
    return nonHidden;
  }
  
  public static Integer nonHiddenChildren2(MBComponentContainer row)
  {
    int nonHidden = 0;
    for (MBComponent child : row.getChildren())
    {
      if (child instanceof MBField)
      {
        MBField field = (MBField) child;
        if (!field.isHidden()) nonHidden++;
      }
    }
    return nonHidden;
  }
  
  public static String doHTML(String htmlString)
  {
    String newHtmlString = htmlString.replaceAll("<br />", "\n");
    newHtmlString = newHtmlString.replaceAll("\\<.*?\\>", "");
    newHtmlString = newHtmlString.replaceAll("\n", "<br />");
    return newHtmlString;
  }
  
  public static String unescapeHTML(String htmlString)
  {
    String newHtmlString = htmlString.replaceAll("&lt;", "<");
    newHtmlString = newHtmlString.replaceAll("&gt;", ">");
    return newHtmlString;
  }
  
  public static String textForKey(String key)
  {
    return MBLocalizationService.getInstance().getTextForKey(key);
  }
}
