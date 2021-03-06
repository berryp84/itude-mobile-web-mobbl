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
package com.itude.mobile.mobbl.core.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.itude.mobile.mobbl.core.configuration.MBDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBAttributeDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBDomainDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBFieldDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.exceptions.MBInvalidPathException;
import com.itude.mobile.mobbl.core.model.MBDocument;
import com.itude.mobile.mobbl.core.services.MBLocalizationService;
import com.itude.mobile.mobbl.core.services.MBMetadataService;
import com.itude.mobile.mobbl.core.util.StringUtilities;
import com.itude.mobile.web.util.PageHelper;

/**
 * Smallest user interface element definition in MOBBL framework. Typically an input field, label or button
 */
public class MBField extends MBComponent
{
  private static final Logger   LOGGER           = Logger.getLogger(MBField.class);

  private static final Pattern  NUMBERPATTERN    = Pattern.compile("\\[[0-9]+\\]");

  private Object                _responder;
  private MBAttributeDefinition _attributeDefinition;
  private boolean               _domainDetermined;
  private MBDomainDefinition    _domainDefinition;
  private String                _label;
  private String[]              _labelAttrs;
  private String                _dataType;
  private int                   _minimumDecimals = -1;
  private int                   _maximumDecimals = -1;
  private String                _formatMask;
  private String                _alignment;
  private String                _valueIfNil;
  private boolean               _hidden;
  private boolean               _required;
  private int                   _width;
  private int                   _height;
  private String                _custom1;
  private String                _custom2;
  private String                _custom3;
  private String                _customTranslated;

  public MBField(MBDefinition definition, MBDocument document, MBComponentContainer parent)
  {
    super(definition, document, parent);

    _responder = null;
    _attributeDefinition = null;
    _domainDetermined = false;

    MBFieldDefinition fieldDef = (MBFieldDefinition) getDefinition();

    if (fieldDef.getWidth() != null)
    {
      String evaluatedWidth = substituteExpressions(fieldDef.getWidth());
      if (evaluatedWidth != null && !evaluatedWidth.isEmpty())
      {
        setWidth(Integer.parseInt(evaluatedWidth));
      }
    }
    if (fieldDef.getHeight() != null)
    {
      String evaluatedHeight = substituteExpressions(fieldDef.getHeight());
      if (evaluatedHeight != null && !evaluatedHeight.isEmpty())
      {
        setHeight(Integer.parseInt(evaluatedHeight));
      }
    }

    if (fieldDef.getMinimumDecimals() != null)
    {
      String evaluatedDecimals = substituteExpressions(fieldDef.getMinimumDecimals());
      if (evaluatedDecimals != null && !evaluatedDecimals.isEmpty())
      {
        setMinimumDecimals(Integer.parseInt(evaluatedDecimals));
      }

    }

    if (fieldDef.getMaximumDecimals() != null)
    {
      String evaluatedDecimals = substituteExpressions(fieldDef.getMaximumDecimals());
      if (evaluatedDecimals != null && !evaluatedDecimals.isEmpty())
      {
        setMaximumDecimals(Integer.parseInt(evaluatedDecimals));
      }

    }

    setStyle(substituteExpressions(fieldDef.getStyle()));
    setDataType(substituteExpressions(fieldDef.getDataType()));
    setLabel(substituteExpressions(fieldDef.getLabel()));
    String labelAttrs = fieldDef.getLabelAttrs();
    setLabelAttrs((labelAttrs != null) ? labelAttrs.split(",") : null);
    setFormatMask(substituteExpressions(fieldDef.getFormatMask()));
    setAlignment(substituteExpressions(fieldDef.getAlignment()));
    setValueIfNil(substituteExpressions(fieldDef.getValueIfNil()));
    setHidden(Boolean.parseBoolean(substituteExpressions(fieldDef.getHidden())));
    setRequired(Boolean.parseBoolean(substituteExpressions(fieldDef.getRequired())));
    setCustom1(substituteExpressions(fieldDef.getCustom1()));
    setCustom2(substituteExpressions(fieldDef.getCustom2()));
    setCustom3(substituteExpressions(fieldDef.getCustom3()));
    setCustomTranslated(substituteExpressions(fieldDef.getCustomTranslated()));

  }

  public Object getResponder()
  {
    return _responder;
  }

  public void setResponder(Object responder)
  {
    _responder = responder;
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

  public String getLabel()
  {
    if (getLabelAttrs() == null)
    {
      return MBLocalizationService.getInstance().getTextForKey(_label);
    }
    else
    {
      return MBLocalizationService.getInstance().getText(_label, (Object[]) getLabelAttrs());
    }
  }

  public void setLabel(String label)
  {
    _label = label;
  }

  public String getLabelKey()
  {
    return _label;
  }

  public void setLabelAttrs(String[] labelAttrs)
  {
    if (labelAttrs != null && labelAttrs.length > 0)
    {
      String[] substitutes = new String[labelAttrs.length];
      for (int i = 0; i < substitutes.length; i++)
      {
        String attr = labelAttrs[i];
        String substituded = substituteExpressions(attr);
        substitutes[i] = (substituded == null) ? getValueIfNil() : substituded;
      }
      labelAttrs = substitutes;
    }
    _labelAttrs = labelAttrs;
  }

  public String[] getLabelAttrs()
  {
    return _labelAttrs;
  }

  public String getGeneratedId()
  {
    if (_label != null)
    {
      // TODO: This can be way prettier with regular expressions
      StringBuilder sb = new StringBuilder();
      for (char c : _label.toCharArray())
      {
        if (c >= 48 && c <= 57 /* numbers */|| c >= 65 && c <= 90 /*uppercase characters*/|| c >= 97 && c <= 122 /* lowercase characters */)
        {
          sb.append(c);
        }
      }
      return PageHelper.CUSTOM_BEGIN + sb;
    }
    else
    {
      // In case of no label, the absoluteDataPath should be unique (in contrast to getPath, which is unevaluated)
      return PageHelper.CUSTOM_BEGIN + getAbsoluteDataPath().hashCode();
    }
  }

  public String getDataType()
  {
    String result = _dataType;
    if (result == null)
    {
      MBDomainDefinition domain = getDomain();
      if (domain != null) result = getDomain().getType();
      if (result == null)
      {
        MBAttributeDefinition ad = getAttributeDefinition();
        if (ad != null) result = getAttributeDefinition().getType();
      }
    }
    return result;
  }

  public boolean isNumeric()
  {
    String tp = getDataType();

    return "int".equals(tp) || "float".equals(tp) || "double".equals(tp);
  }

  public void setDataType(String dataType)
  {
    _dataType = dataType;
  }

  public void setMinimumDecimals(int minimumDecimals)
  {
    _minimumDecimals = minimumDecimals;
  }

  public void setMaximumDecimals(int maximumDecimals)
  {
    _maximumDecimals = maximumDecimals;
  }

  public String getFormatMask()
  {
    return _formatMask;
  }

  public void setFormatMask(String formatMask)
  {
    _formatMask = formatMask;
  }

  public String getAlignment()
  {
    return _alignment;
  }

  public void setAlignment(String alignment)
  {
    _alignment = alignment;
  }

  public String getValueIfNil()
  {
    return _valueIfNil;
  }

  public void setValueIfNil(String valueIfNil)
  {
    _valueIfNil = valueIfNil;
  }

  public boolean isHidden()
  {
    return _hidden;
  }

  public void setHidden(boolean hidden)
  {
    _hidden = hidden;
  }

  public boolean getRequired()
  {
    return _required;
  }

  public void setRequired(boolean required)
  {
    _required = required;
  }

  public String getCustom1()
  {
    return _custom1;
  }

  public void setCustom1(String custom1)
  {
    _custom1 = custom1;
  }

  public String getCustom2()
  {
    return _custom2;
  }

  public void setCustom2(String custom2)
  {
    _custom2 = custom2;
  }

  public String getCustom3()
  {
    return _custom3;
  }

  public void setCustom3(String custom3)
  {
    _custom3 = custom3;
  }

  public void setCustomTranslated(String customTranslated)
  {
    _customTranslated = customTranslated;
  }

  public String getCustomTranslated()
  {
    return MBLocalizationService.getInstance().getTextForKey(_customTranslated);
  }

  public String getValue()
  {
    if (_cachedValueSet)
    {
      return _cachedValue;
    }
    String result = null;

    if (getDocument() != null)
    {
      Object value = getDocument().getValueForPath(getAbsoluteDataPath());
      if (value instanceof String) result = (String) value;
      else if (value != null) result = value.toString();
    }
    _cachedValue = result;
    _cachedValueSet = true;
    return result;
  }

  private String  _cachedValue    = null;
  private boolean _cachedValueSet = false;

  public void setValue(String value)
  {
    String path = getAbsoluteDataPath();
    String originalValue = (String) getDocument().getValueForPath(path);

    boolean valueChanged = (value == null && originalValue != null) || (value != null && originalValue == null)
                           || !value.equals(originalValue);

    if (valueChanged && notifyValueWillChange(value, originalValue, path))
    {
      getDocument().setValue(value, path);
      notifyValueChanged(value, originalValue, path);
    }

  }

  public String getPath()
  {
    return ((MBFieldDefinition) getDefinition()).getPath();
  }

  public String getType()
  {
    return ((MBFieldDefinition) getDefinition()).getDisplayType();
  }

  public String dataType()
  {
    return null;
  }

  public String getText()
  {
    return MBLocalizationService.getInstance().getTextForKey(((MBFieldDefinition) getDefinition()).getText());
  }

  public String getOutcomeName()
  {
    return ((MBFieldDefinition) getDefinition()).getOutcomeName();
  }

  public boolean required()
  {
    return false;
  }

  public MBDomainDefinition getDomain()
  {
    if (!_domainDetermined)
    {
      MBAttributeDefinition attrDef = getAttributeDefinition();
      if (attrDef != null)
      {
        _domainDefinition = MBMetadataService.getInstance().getDefinitionForDomainName(attrDef.getType(), false);
        _domainDetermined = true;
      }
    }
    return _domainDefinition;
  }

  public MBAttributeDefinition getAttributeDefinition()
  {
    if (_attributeDefinition == null)
    {
      String absoluteDataPath = getAbsoluteDataPath();
      if (absoluteDataPath == null)
      {
        return null;
      }

      String path = NUMBERPATTERN.matcher(getAbsoluteDataPath()).replaceAll("");
      if (path == null)
      {
        return null;
      }
      try
      {
        _attributeDefinition = getDocument().getDefinition().getAttributeWithPath(path);
      }
      catch (MBInvalidPathException e)
      {
        // Button outcomes do not map to an attribute
        LOGGER.debug("MBField.getAttributeDefinition() with path=" + path
                     + " does not map to an attribute. Probably an outcomePath for a Button.");
      }
    }

    return _attributeDefinition;
  }

  @Override
  public String getComponentDataPath()
  {
    String path = ((MBFieldDefinition) getDefinition()).getPath();
    if (path == null || "".equals(path))
    {
      return null;
    }
    return substituteExpressions(path);
  }

  // Apply a formatmask
  public String getFormattedValue()
  {
    String fieldValue = getValue();
    if ("NaN".equals(fieldValue))
    {
      fieldValue = null;
    }

    if (fieldValue == null)
    {
      fieldValue = getValueIfNil();
    }

    if (fieldValue == null)
    {
      return "";
    }

    boolean fieldValueSameAsNilValue = fieldValue.equals(getValueIfNil());

    if (getFormatMask() != null && getDataType().equals("dateTime"))
    {
      // Get a NSDate from a xml-dateFormat
      String xmlDate = fieldValue;

      // Formats the date depending on the current date. 
      if (getFormatMask().equals("dateOrTimeDependingOnCurrentDate"))
      {
        fieldValue = StringUtilities.formatDateDependingOnCurrentDate(xmlDate);
      }
      else
      {
        Date date = StringUtilities.dateFromXML(xmlDate);

        SimpleDateFormat df = new SimpleDateFormat(getFormatMask());
        fieldValue = df.format(date);
      }

    }
    else if (!fieldValueSameAsNilValue && getDataType().equals("numberWithTwoDecimals"))
    {
      fieldValue = StringUtilities.formatNumberWithTwoDecimals(fieldValue);
    }
    else if (!fieldValueSameAsNilValue && getDataType().equals("numberWithThreeDecimals"))
    {
      fieldValue = StringUtilities.formatNumberWithThreeDecimals(fieldValue);
    }
    else if (!fieldValueSameAsNilValue && getDataType().equals("priceWithTwoDecimals"))
    {
      fieldValue = StringUtilities.formatPriceWithTwoDecimals(fieldValue);
    }
    else if (!fieldValueSameAsNilValue && getDataType().equals("priceWithThreeDecimals"))
    {
      fieldValue = StringUtilities.formatPriceWithThreeDecimals(fieldValue);
    }
    else if (!fieldValueSameAsNilValue && getDataType().equals("priceWithFourDecimals"))
    {
      fieldValue = StringUtilities.formatNumberWithDecimals(fieldValue, 4);
    }
    else if (!fieldValueSameAsNilValue && getDataType().equals("priceWithDynamicDecimals")
             && (_minimumDecimals > -1 || _maximumDecimals > -1))
    {
      fieldValue = StringUtilities.formatNumberWithDecimals(fieldValue, _minimumDecimals, _maximumDecimals);
    }
    else if (!fieldValueSameAsNilValue && getDataType().equals("volume"))
    {
      fieldValue = StringUtilities.formatVolume(fieldValue);
    }
    else if (!fieldValueSameAsNilValue && getDataType().equals("percentageWithTwoDecimals"))
    {
      fieldValue = StringUtilities.formatPercentageWithTwoDecimals(fieldValue);
    }
    else if (!fieldValueSameAsNilValue && getDataType().equals("percentageWithTwoDecimalsWithPlusSignInFrontOfIt"))
    {
      fieldValue = StringUtilities.formatPercentageWithTwoDecimalsWithPlusSignInFrontOfIt(fieldValue);
    }
    else if (!fieldValueSameAsNilValue && getDataType().equals("percentageWithTwoDecimalsWithMinSignInFrontOfIt"))
    {
      fieldValue = StringUtilities.formatPercentageWithTwoDecimalsWithMinSignInFrontOfIt(fieldValue);
    }
    else if (!fieldValueSameAsNilValue && getDataType().equals("nonNegativeDouble") && Integer.parseInt(fieldValue) < 0)
    {
      fieldValue = String.valueOf(Integer.parseInt(fieldValue) * -1);
    }

    // CURRENCY Symbols
    if ("EURO".equals(getStyle()) || "EURO NEUTRAL".equals(getStyle()))
    {
      fieldValue = "\u20AC " + fieldValue;
    }

    return fieldValue;
  }

  // Returns a path that has indexed expressions evaluated (translated) i.e. something like myelement[someattr='xx'] -> myelement[12]
  // for the current document; where the 12th element is matched
  public String evaluatedDataPath()
  {
    String path = getAbsoluteDataPath();
    if (path != null && path.length() > 0 && path.contains("="))
    {
      // Now translate the index expressions like [someAttr=='x' and someOther=='y'] into [idx]
      // We can only do this if the row that matches the expression does exist!
      List<String> components = new ArrayList<String>();
      String value = (String) getDocument().getValueForPath(path, components);

      // Now glue together the components to make a full path again:
      String result = "";
      for (String part : components)
      {
        if (!part.endsWith(":"))
        {
          result += "/";
        }
        result += part;
      }
      if (value != null)
      {
        return result;
      }

    }

    return path;
  }

  @Override
  public String asXmlWithLevel(int level)
  {
    String result = "";

    try
    {
      String required = getRequired() ? "TRUE" : "FALSE";

      result = StringUtilities.getIndentStringWithLevel(level);
      result += "<MBField " + attributeAsXml("value", getValue()) + " " + attributeAsXml("path", getAbsoluteDataPath()) + " "
                + attributeAsXml("style", getStyle()) + " " + attributeAsXml("label", getLabel()) + " " + attributeAsXml("type", getType())
                + " " + attributeAsXml("dataType", getDataType()) + " " + attributeAsXml("outcomeName", getOutcomeName()) + " "
                + attributeAsXml("formatMask", getFormatMask()) + " " + attributeAsXml("alignment", getAlignment()) + " "
                + attributeAsXml("valueIfNil", getValueIfNil()) + " " + attributeAsXml("required", required) + " width='" + getWidth()
                + "' height='" + getHeight() + "'/>\n";
    }
    catch (Exception e)
    {
      result = "<MBField errorInDefinition='" + e.getClass().getSimpleName() + ", " + e.getCause() + "'/>\n";
      LOGGER.debug(e.getMessage(), e);
    }

    return result;
  }

  @Override
  public String toString()
  {
    return asXmlWithLevel(0);
  }

}
