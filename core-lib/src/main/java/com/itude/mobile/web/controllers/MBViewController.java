package com.itude.mobile.web.controllers;

import java.io.Serializable;

import javax.inject.Inject;

import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBPageDefinition;
import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.mobbl2.client.core.controller.exceptions.MBInvalidOutcomeException;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.model.MBElement;
import com.itude.mobile.mobbl2.client.core.services.MBDataManagerService;
import com.itude.mobile.mobbl2.client.core.services.MBMetadataService;
import com.itude.mobile.mobbl2.client.core.view.MBPage;
import com.itude.mobile.mobbl2.client.core.view.MBValueChangeListenerProtocol;

public abstract class MBViewController implements Serializable, MBValueChangeListenerProtocol
{
  private static final long     serialVersionUID = 1L;

  private MBPage                _page;
  private MBDocument            _document;

  @Inject
  private ApplicationController _applicationController;

  @Inject
  private MBDataManagerService  _dataManagerService;

  @Inject
  private MBMetadataService     _metadataService;

  protected MBDataManagerService getDataManagerService()
  {
    return _dataManagerService;
  }

  protected MBDocument loadDocument(String name)
  {
    return getDataManagerService().loadDocument(name);
  }

  protected MBDocument loadDocument(String name, MBDocument arguments)
  {
    return getDataManagerService().loadDocument(name, arguments);
  }

  protected MBMetadataService getMetadataService()
  {
    return _metadataService;
  }

  public MBPage getPage()
  {
    return _page;
  }

  public void setPage(MBPage page)
  {
    _page = page;
  }

  public MBDocument getDocument()
  {
    if (getPage() != null) return getPage().getDocument();
    else return _document;
  }

  public void setDocument(MBDocument document)
  {
    if (getPage() != null) getPage().setDocument(document);
    else _document = document;
  }

  public void onPageCreated()
  {
  }

  public void rebuild()
  {
    _page.rebuild();
  }

  public void constructPage(MBOutcome outcome)
  {
    MBPageDefinition pageDefinition = _metadataService.getDefinitionForPageName(outcome.getAction());

    MBDocument document = getDocument();
    // TODO: dirty hack.. :$
    if (document != null && document.getName().equals("MBEmptyDoc") && !pageDefinition.getDocumentName().equals("MBEmptyDoc")) document = null;

    if (document != null && !document.getDocumentName().equals(pageDefinition.getDocumentName())) throw new MBInvalidOutcomeException(
        "While maintaining document between pages, document \'" + document.getDocumentName() + "\' and \'"
            + pageDefinition.getDocumentName() + "\' are not the same.");

    if (document == null) document = _dataManagerService.loadDocument(pageDefinition.getDocumentName());
    setDocument(document);
    MBPage page = new MBPage(pageDefinition, document, outcome.getPath());
    setPage(page);
  }

  public void handleOutcome(String name, String path)
  {
    MBOutcome outcome = new MBOutcome(name, getDocument());
    outcome.setOriginName(getPage().getName());
    outcome.setPath(path);

    _applicationController.handleOutcome(outcome);
  }

  @Override
  public boolean valueWillChange(String value, String originalValue, String path)
  {
    return true;
  }

  @Override
  public void valueChanged(String value, String originalValue, String path)
  {
  }

  protected void setRequestParameter(String value, String key, MBDocument doc)
  {
    MBElement request = (MBElement) doc.getValueForPath("Request[0]");
    MBElement parameter = request.createElementWithName("Parameter");
    parameter.setAttributeValue(key, "key");
    parameter.setAttributeValue(value, "value");
  }
}
