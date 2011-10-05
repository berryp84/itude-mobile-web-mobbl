package com.itude.mobile.mcds.actions;

import javax.inject.Named;

import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.template.actions.GenericAction;
import com.itude.mobile.template.util.Utilities;

@Named("DownloadContentAction")
public class DownloadContentAction extends GenericAction
{
  private static final long serialVersionUID = 1L;

  @Override
  public MBOutcome execute(MBDocument document, String path)
  {
    return this.execute(document, path, null);
  }

  @Override
  public MBOutcome execute(MBDocument document, String path, String outcomeName)
  {
    String code = getSession().getParam(); // is dit wel de juiste code?!
    MBDocument requestDoc = loadDocument("MBGenericRequest");
    Utilities.setRequestParameter("Z4_jlbFv2iwHCzP4aGwS", "token", requestDoc);
    Utilities.setRequestParameter(code, "code", requestDoc);

    MBDocument resultDoc = loadDocument("downloadContent", requestDoc);

    return new MBOutcome("download_content", resultDoc);
  }

}