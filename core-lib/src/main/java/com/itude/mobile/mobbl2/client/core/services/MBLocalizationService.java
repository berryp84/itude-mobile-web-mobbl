package com.itude.mobile.mobbl2.client.core.services;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itude.mobile.mobbl2.client.core.util.MBProperties;

public class MBLocalizationService
{
  private static final Logger _log             = Logger.getLogger(MBLocalizationService.class);
  
  private final Map<String, Map<String, String>> _languages;      //DictionaryofDictionaries(languagecode->(key->value))
  private String                                 _currentLanguage;
  protected Map<String, String>                  _currentLanguageMap;
  private String                                 _localeCode;

  private static MBLocalizationService           _instance;

  protected MBLocalizationService()
  {
    _languages = new Hashtable<String, Map<String, String>>();
    setCurrentLanguage(getLocale().getLanguage());
  }

  public void setInstance(MBLocalizationService instance)
  {
    if (instance != null)
          _instance = instance;
  }

  public static MBLocalizationService getInstance()
  {
    // so called double-check
    if (_instance == null)
    {
      synchronized (MBLocalizationService.class)
      {
        if (_instance == null)
          _instance = new MBLocalizationService();
      }
    }

    return _instance;
  }

  public Map<String, String> getBundleForCode(String languageCode)
  {
    Map<String, String> result = new HashMap<String, String>();
    for (Map<String, String> bundle : MBResourceService.getInstance().getBundlesForLanguageCode(languageCode))
    {
      result.putAll(bundle);
    }

    return result;
  }

  private Map<String, String> getLanguageForCode(String languageCode)
  {
    Map<String, String> result = null;
    result = _languages.get(languageCode);
    if (result == null)
    {
      result = getBundleForCode(languageCode);
      _languages.put(languageCode, result);
    }

    return result;
  }

  public String getCurrentLanguage()
  {
    return _currentLanguage;
  }

  public synchronized void setCurrentLanguage(String currentLanguage)
  {
    _currentLanguage = currentLanguage;
    _currentLanguageMap = getLanguageForCode(_currentLanguage);
  }

  public String getTextForKey(String key)
  {
    if (key == null)
    {
      return null;
    }

    Map<String, String> dict = _currentLanguageMap;
    String text = dict.get(key);
    if (text == null)
    {
      _log.warn("Warning: no translation defined for key '" + key + "' using languageCode=" + getCurrentLanguage());
      // add the missing translation to prevent future warnings
      dict.put(key, key);
      text = key;
    }

    return text;
  }

  public String getLocaleCode()
  {
    if (_localeCode == null)
    {
      _localeCode = MBProperties.getInstance().getValueForProperty("localeCode");
      if (_localeCode == null)
      {
        _localeCode = Locale.getDefault().toString();
      }
    }
    return _localeCode;
  }

  public Locale getLocale()
  {
    String localeCode = getLocaleCode();

    if (localeCode != null && localeCode.length() > 0)
    {
      String[] parts = localeCode.split("_");

      if (parts.length == 1)
      {
        return new Locale(parts[0]);
      }
      else if (parts.length == 2)
      {
        return new Locale(parts[0], parts[1]);
      }

    }

    return null;
  }

}
