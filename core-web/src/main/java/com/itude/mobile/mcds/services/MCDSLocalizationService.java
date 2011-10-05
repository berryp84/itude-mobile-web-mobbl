package com.itude.mobile.mcds.services;

import java.util.Map;

import org.apache.log4j.Logger;

import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.model.MBElement;
import com.itude.mobile.mobbl2.client.core.services.MBDataManagerService;
import com.itude.mobile.mobbl2.client.core.services.MBLocalizationService;

public class MCDSLocalizationService extends MBLocalizationService
{
  private static final Logger                       _log              = Logger.getLogger(MCDSLocalizationService.class);
  private MBDocument                                _frontendTextsDoc = null;

  @Override
  public String getTextForKey(String key)
  {
    if (key == null)
    {
      return null;
    }

    if (_frontendTextsDoc == null)
    {
      _frontendTextsDoc = MBDataManagerService.getInstance().loadDocument("ApplicationTextResponse");
    }

    String text = null;
    for (MBElement element : _frontendTextsDoc.getElementsWithName("Text"))
    {
      if (element.getValueForAttribute("key").equalsIgnoreCase(key))
      {
        return element.getValueForAttribute("text()");
      }
    }

    Map<String, String> dict = super._currentLanguageMap;
    text = dict.get(key);
    if (text == null)
    {
      _log.warn("Warning: no translation defined for key '" + key + "' using languageCode=" + getCurrentLanguage());
      // add the missing translation to prevent future warnings
      dict.put(key, key);
      text = key;
    }

    return text;
  }
}
