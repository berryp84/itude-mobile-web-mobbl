package com.itude.mobile.template.jsf;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.ExternalContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import com.itude.commons.jsf.util.ELUtil;
import com.itude.mobile.template.controllers.CurrentView;

public class HistoryPhaseListener implements PhaseListener
{
  private static final long serialVersionUID = 1L;

  private static Pattern    docIdPattern     = Pattern.compile("\\?docId=(\\d+)");

  @Override
  public void afterPhase(PhaseEvent event)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void beforePhase(PhaseEvent event)
  {
    ExternalContext ec = event.getFacesContext().getExternalContext();
    Map<String, String> pm = ec.getRequestParameterMap();

    String idStr = pm.get("docId");

    if (idStr == null)
    {

      Map<String, String> map = ec.getRequestHeaderMap();
      String referer = map.get("referer");
      if (referer != null)
      {
        Matcher docIdMatcher = docIdPattern.matcher(referer);
        if (docIdMatcher.find())

        idStr = docIdMatcher.group(1);

      }
    }

    if (idStr != null)
    {
      try
      {
        long id = Long.parseLong(idStr);
        CurrentView view = ELUtil.getValue("currentView", CurrentView.class);
        if (view != null) view.setDocId(id);
      }
      catch (NumberFormatException e)
      {
      }
    }

  }

  @Override
  public PhaseId getPhaseId()
  {
    return PhaseId.RESTORE_VIEW;
  }

}
