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
package com.itude.mobile.mobbl.core.controller;

import com.itude.mobile.mobbl.core.configuration.mvc.MBConfigurationDefinition;
import com.itude.mobile.mobbl.core.configuration.mvc.MBOutcomeDefinition;
import com.itude.mobile.mobbl.core.controller.exceptions.MBExpressionNotBooleanException;
import com.itude.mobile.mobbl.core.model.MBDocument;
import com.itude.mobile.mobbl.core.services.MBDataManagerService;

/**
 * Defines an outcome
 */
public class MBOutcome
{
  private String     _originName;
  private String     _outcomeName;
  private String     _dialogName;
  private String     _originDialogName;
  private String     _displayMode;
  private String     _path;
  private boolean    _persist;
  private boolean    _transferDocument;
  private boolean    _noBackgroundProcessing;
  private MBDocument _document;
  private String     _preCondition;
  private String     _action;

  public String getOriginName()
  {
    return _originName;
  }

  public void setOriginName(String originName)
  {
    _originName = originName;
  }

  public String getOutcomeName()
  {
    return _outcomeName;
  }

  public void setOutcomeName(String outcomeName)
  {
    _outcomeName = outcomeName;
  }

  public String getDialogName()
  {
    return _dialogName;
  }

  public void setDialogName(String dialogName)
  {
    _dialogName = dialogName;
  }

  public String getOriginDialogName()
  {
    return _originDialogName;
  }

  public void setOriginDialogName(String originDialogName)
  {
    _originDialogName = originDialogName;
  }

  public String getPath()
  {
    return _path;
  }

  public void setPath(String path)
  {
    _path = path;
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

  public MBDocument getDocument()
  {
    return _document;
  }

  public void setDocument(MBDocument document)
  {
    _document = document;
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

  public String getAction()
  {
    return _action;
  }

  public void setAction(String action)
  {
    _action = action;
  }

  public MBOutcome(MBOutcome outcome)
  {
    _originName = outcome.getOriginName();
    _outcomeName = outcome.getOutcomeName();
    _originDialogName = outcome.getOriginDialogName();
    _dialogName = outcome.getDialogName();
    _displayMode = outcome.getDisplayMode();
    _document = outcome.getDocument();
    _path = outcome.getPath();
    _persist = outcome.getPersist();
    _transferDocument = outcome.getTransferDocument();
    _preCondition = outcome.getPreCondition();
    _noBackgroundProcessing = outcome.getNoBackgroundProcessing();
    _action = outcome.getAction();
  }

  public MBOutcome(MBOutcomeDefinition definition)
  {
    _originName = definition.getOrigin();
    _outcomeName = definition.getName();
    _dialogName = definition.getDialog();
    _displayMode = definition.getDisplayMode();
    _persist = definition.getPersist();
    _transferDocument = definition.getTransferDocument();
    _noBackgroundProcessing = definition.getNoBackgroundProcessing();
    _document = null;
    _path = null;
    _preCondition = definition.getPreCondition();
    _action = definition.getAction();
  }

  public MBOutcome(String outcomeName, MBDocument document)
  {

    _outcomeName = outcomeName;
    _document = document;
  }

  public MBOutcome(String outcomeName, MBDocument document, String dialogName)
  {
    this(outcomeName, document);
    _dialogName = dialogName;
  }

  public MBOutcome()
  {
  }

  public boolean isPreConditionValid()
  {
    boolean isValid = true;
    if (getPreCondition() != null)
    {
      MBDocument doc = getDocument();
      if (doc == null)
      {
        doc = MBDataManagerService.getInstance().loadDocument(MBConfigurationDefinition.DOC_SYSTEM_EMPTY);
      }
      String result = doc.evaluateExpression(this.getPreCondition());
      if ("1".equals(result) || "YES".equalsIgnoreCase(result) || "TRUE".equalsIgnoreCase(result))
      {
        return true;
      }
      if ("0".equals(result) || "NO".equalsIgnoreCase(result) || "FALSE".equalsIgnoreCase(result))
      {
        return false;
      }
      String msg = "Expression of outcome with origin=" + getOriginName() + " name=" + getOutcomeName() + " precondition="
                   + getPreCondition() + " is not boolean (result=" + result + ")";
      throw new MBExpressionNotBooleanException(msg);
    }

    return isValid;
  }

  @Override
  public String toString()
  {
    return "Outcome: dialog=" + getDialogName() + " originName=" + getOriginName() + " outcomeName=" + getOutcomeName() + " path="
           + getPath() + " persist=" + getPersist() + " displayMode=" + getDisplayMode() + " preCondition=" + getPreCondition()
           + " noBackgroundProsessing=" + getNoBackgroundProcessing() + " action=" + getAction();
  }

}
