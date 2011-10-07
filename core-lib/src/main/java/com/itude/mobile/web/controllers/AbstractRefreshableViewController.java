package com.itude.mobile.web.controllers;

import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.services.datamanager.handlers.MBDocumentOperationDelegate;

public abstract class AbstractRefreshableViewController extends MBViewController implements MBDocumentOperationDelegate
{
  private static final long serialVersionUID = 1L;

  private boolean           _busy            = false;

  public void refresh()
  {
    // TODO: not thread safe, but not really needed at the moment
    if (!_busy) try
    {
      _busy = true;
      MBDocument doc = getPage().getDocument().clone();
      doc.reload();
      processResult(doc);
    }
    finally
    {
      _busy = false;
    }
  }

  @Override
  public void processResult(MBDocument document)
  {
    if (document != null) handleUpdate(document);
  }

  protected abstract void handleUpdate(MBDocument document);

}
