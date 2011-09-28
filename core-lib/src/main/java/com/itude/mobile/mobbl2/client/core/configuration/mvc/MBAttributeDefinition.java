package com.itude.mobile.mobbl2.client.core.configuration.mvc;

import com.itude.mobile.mobbl2.client.core.configuration.MBDefinition;
import com.itude.mobile.mobbl2.client.core.services.MBMetadataService;
import com.itude.mobile.mobbl2.client.core.util.StringUtilities;

public class MBAttributeDefinition extends MBDefinition
{
  private String             _type;
  private String             _defaultValue;
  private String             _dataType;
  private boolean            _required;
  private MBDomainDefinition _domainDefinition;

  public String asXmlWithLevel(int level)
  {
    String requiredBool = "";
    if (_required)
    {
      requiredBool = "TRUE";
    }
    else
    {
      requiredBool = "FALSE";
    }

    String result = StringUtilities.getIndentStringWithLevel(level) + "<Attribute name='" + getName() + "' type='" + _type + "' required='"
                    + requiredBool + "'" + getAttributeAsXml("defaultValue", _defaultValue) + "/>\n";
    return result;
  }

  public String getType()
  {
    return _type;
  }

  public void setType(String type)
  {
    _type = type;
  }

  public String getDefaultValue()
  {
    return _defaultValue;
  }

  public void setDefaultValue(String defaultValue)
  {
    _defaultValue = defaultValue;
  }

  public boolean getRequired()
  {
    return _required;
  }

  public void setRequired(boolean required)
  {
    _required = required;
  }

  public MBDomainDefinition getDomainDefinition()
  {
    if (_domainDefinition == null)
    {
      _domainDefinition = MBMetadataService.getInstance().getDefinitionForDomainName(_type);
    }

    return _domainDefinition;
  }

  public String getDataType()
  {
    if (_dataType == null)
    {
      MBDomainDefinition domDef = MBMetadataService.getInstance().getDefinitionForDomainName(_type, false);

      String type = domDef.getType();
      if (type == null)
      {
        type = getType();
      }
      if (_dataType != type)
      {
        _dataType = null;
        _dataType = type;
      }

    }

    return _dataType;
  }

}
