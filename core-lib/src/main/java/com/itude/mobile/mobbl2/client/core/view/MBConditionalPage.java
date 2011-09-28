package com.itude.mobile.mobbl2.client.core.view;

import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBPageDefinition;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.view.helpers.MBBounds;

public class MBConditionalPage extends MBPage
{
  public MBConditionalPage(MBPageDefinition definition, MBDocument document, String rootPath, Object viewState, MBBounds bounds)
  {
    super(definition, document, rootPath, viewState);
  }

  private MBPageDefinition _definitionWhenFalse;
  private MBPageDefinition _definitionWhenTrue;

  public MBPageDefinition getDefinitionWhenFalse()
  {
    return _definitionWhenFalse;
  }

  public void setDefinitionWhenFalse(MBPageDefinition definitionWhenFalse)
  {
    _definitionWhenFalse = definitionWhenFalse;
  }

  public MBPageDefinition getDefinitionWhenTrue()
  {
    return _definitionWhenTrue;
  }

  public void setDefinitionWhenTrue(MBPageDefinition definitionWhenTrue)
  {
    _definitionWhenTrue = definitionWhenTrue;
  }

  public Object initWithDefinitionWhenTrue(MBPageDefinition definitionWhenTrue, MBPageDefinition definitionWhenFalse,
                                           Object viewController, MBDocument document, String rootPath, Object viewState)
  {
    return null;
  }

  public Object initWithDefinitionWhenTrue(MBPageDefinition definitionWhenTrue, MBPageDefinition definitionWhenFalse, MBDocument document,
                                           String rootPath, Object viewState, Object bounds)
  {
    return null;
  }

}
