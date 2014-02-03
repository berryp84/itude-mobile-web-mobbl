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
package com.itude.mobile.mobbl.core.configuration.mvc;

import java.math.BigDecimal;

import com.itude.mobile.mobbl.core.configuration.MBDefinition;
import com.itude.mobile.mobbl.core.util.StringUtilities;

/**
 * {@link MBDefinition} Class for a domain validator
 *
 */
public class MBDomainValidatorDefinition extends MBDefinition
{
  private String     _title;
  private String     _value;
  private BigDecimal _lowerBound;
  private BigDecimal _upperBound;

  @Override
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
