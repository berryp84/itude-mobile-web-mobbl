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
package com.itude.mobile.mobbl2.client.core.configuration.mvc;

import com.itude.mobile.mobbl2.client.core.configuration.MBDefinition;
import com.itude.mobile.mobbl2.client.core.util.StringUtilities;

public class MBOutcomeDefinition extends MBDefinition
{
  private String  _origin;
  private String  _action;
  private String  _dialog;
  private String  _displayMode;
  private String  _preCondition;
  private boolean _persist;
  private boolean _transferDocument;
  private boolean _noBackgroundProcessing;

  @Override
  public String asXmlWithLevel(int level)
  {
    String persistBool;
    if (_persist)
    {
      persistBool = "TRUE";
    }
    else
    {
      persistBool = "FALSE";
    }
    String transferDocumentBool;
    if (_transferDocument)
    {
      transferDocumentBool = "TRUE";
    }
    else
    {
      transferDocumentBool = "FALSE";
    }

    String noBackgroundProcessingBool;
    if (_noBackgroundProcessing)
    {
      noBackgroundProcessingBool = "TRUE";
    }
    else
    {
      noBackgroundProcessingBool = "FALSE";
    }

    String result = StringUtilities.getIndentStringWithLevel(level) + "<Outcome origin='" + _origin + "' name='" + getName() + "' action='"
                    + _action + "' transferDocument='" + transferDocumentBool + "' persist='" + persistBool + "' noBackgroundProcessing='"
                    + noBackgroundProcessingBool + "'" + getAttributeAsXml("dialog", _dialog)
                    + getAttributeAsXml("preCondition", _preCondition) + getAttributeAsXml("displayMode", _displayMode) + "/>\n";
    return result;
  }

  public String getOrigin()
  {
    return _origin;
  }

  public void setOrigin(String origin)
  {
    _origin = origin;
  }

  public String getAction()
  {
    return _action;
  }

  public void setAction(String action)
  {
    _action = action;
  }

  public String getDialog()
  {
    return _dialog;
  }

  public void setDialog(String dialog)
  {
    _dialog = dialog;
  }

  public String getDisplayMode()
  {
    return _displayMode;
  }

  public void setDisplayMode(String displayMode)
  {
    _displayMode = displayMode;
  }

  public String getPreCondition()
  {
    return _preCondition;
  }

  public void setPreCondition(String preCondition)
  {
    _preCondition = preCondition;
  }

  public boolean getPersist()
  {
    return _persist;
  }

  public void setPersist(boolean persist)
  {
    _persist = persist;
  }

  public boolean getTransferDocument()
  {
    return _transferDocument;
  }

  public void setTransferDocument(boolean transferDocument)
  {
    _transferDocument = transferDocument;
  }

  public boolean getNoBackgroundProcessing()
  {
    return _noBackgroundProcessing;
  }

  public void setNoBackgroundProcessing(boolean noBackgroundProcessing)
  {
    _noBackgroundProcessing = noBackgroundProcessing;
  }

}
