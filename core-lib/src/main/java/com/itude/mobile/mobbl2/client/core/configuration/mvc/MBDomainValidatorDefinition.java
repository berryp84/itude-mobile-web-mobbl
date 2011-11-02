package com.itude.mobile.mobbl2.client.core.configuration.mvc;

import java.math.BigDecimal;

import com.itude.mobile.mobbl2.client.core.configuration.MBDefinition;
import com.itude.mobile.mobbl2.client.core.util.StringUtilities;

public class MBDomainValidatorDefinition extends MBDefinition
{
  private String     _title;
  private String     _value;
  private BigDecimal _lowerBound;
  private BigDecimal _upperBound;

  public String asXmlWithLevel(int level)
  {
    String result = StringUtilities.getIndentStringWithLevel(level) + "<DomainValidator" + getAttributeAsXml("title", _title)
                    + getAttributeAsXml("value", _value) + getAttributeAsXml("lowerBound", _lowerBound)
                    + getAttributeAsXml("upperBound", _upperBound) + "/>\n";
    return result;
  }

  public String getTitle()
  {
    return _title;
  }

  public void setTitle(String title)
  {
    _title = title;
  }

  public String getValue()
  {
    return _value;
  }

  public void setValue(String value)
  {
    _value = value;
  }

  public BigDecimal getLowerBound()
  {
    return _lowerBound;
  }

  public void setLowerBound(BigDecimal lowerBound)
  {
    _lowerBound = lowerBound;
  }

  public BigDecimal getUpperBound()
  {
    return _upperBound;
  }

  public void setUpperBound(BigDecimal upperBound)
  {
    _upperBound = upperBound;
  }

}
