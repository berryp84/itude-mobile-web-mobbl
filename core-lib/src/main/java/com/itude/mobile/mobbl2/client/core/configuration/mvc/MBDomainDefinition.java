package com.itude.mobile.mobbl2.client.core.configuration.mvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.itude.mobile.mobbl2.client.core.configuration.MBDefinition;
import com.itude.mobile.mobbl2.client.core.util.StringUtilities;

public class MBDomainDefinition extends MBDefinition
{
  private String                            _type;
  private BigDecimal                        _maxLength;
  private List<MBDomainValidatorDefinition> _validators;

  public MBDomainDefinition()
  {
    _validators = new ArrayList<MBDomainValidatorDefinition>();
  }

  /*@Override
  public void addChildElement(Object child)
  {
    addValidator((MBDomainValidatorDefinition) child);
  }*/

  @Override
  public void addChildElement(MBDomainValidatorDefinition child)
  {
    addValidator(child);
  }

  public void addValidator(MBDomainValidatorDefinition validator)
  {
    _validators.add(validator);
  }

  @Override
  public String asXmlWithLevel(int level)
  {
    String result = StringUtilities.getIndentStringWithLevel(level) + "<Domain name='" + getName() + "' type='" + _type + "'"
                    + getAttributeAsXml("maxLength", _maxLength) + ">\n";

    for (MBDomainValidatorDefinition vld : _validators)
    {
      result += vld.asXmlWithLevel(level + 2);
    }

    result += StringUtilities.getIndentStringWithLevel(level) + "</Domain>\n";

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

  public List<MBDomainValidatorDefinition> getDomainValidators()
  {
    return _validators;
  }

  public void setDomainValidators(List<MBDomainValidatorDefinition> domainValidators)
  {
    _validators = domainValidators;
  }

  public BigDecimal getMaxLength()
  {
    return _maxLength;
  }

  public void setMaxLength(BigDecimal maxLength)
  {
    _maxLength = maxLength;
  }

}
