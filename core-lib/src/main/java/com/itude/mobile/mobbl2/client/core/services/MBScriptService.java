package com.itude.mobile.mobbl2.client.core.services;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;

import com.itude.mobile.mobbl2.client.core.services.exceptions.MBScriptErrorException;

public class MBScriptService
{
  private static MBScriptService _instance = null;
  
  private static final int MAX_ENTRIES = 1000;

  private MBScriptService()
  {

  }

  public static MBScriptService getInstance()
  {
    if (_instance == null)
    {
      // 2 threads may enter this if
      synchronized (MBScriptService.class)
      {
        // but one of them temporarily blocks on the sync block
        // the other one will create the new instance, so we need
        // to check again if the instance is null.
        if (_instance == null)
        {
          _instance = new MBScriptService();
        }
      }
    }

    return _instance;
  }
  
//map needs synchronization because it is not read-only in the evaluate function
  private static Map<String, String> KNOWN_EXPRESSIONS = Collections.synchronizedMap(new LinkedHashMap<String,String>(MAX_ENTRIES, .75F, true) {  
    private static final long serialVersionUID = 1L;

    protected boolean removeEldestEntry(Map.Entry<String,String> eldest)
    {
      return size() > MAX_ENTRIES;
    };
  });
  static {
    KNOWN_EXPRESSIONS.put("false", "false");
    KNOWN_EXPRESSIONS.put("!false", "true");
    KNOWN_EXPRESSIONS.put("true", "true");
    KNOWN_EXPRESSIONS.put("!true", "false");
    KNOWN_EXPRESSIONS.put("('EUR'=='EUR')", "true");
    KNOWN_EXPRESSIONS.put("!('EUR'=='EUR')", "false");
    KNOWN_EXPRESSIONS.put("('USD'=='EUR')", "false");
    KNOWN_EXPRESSIONS.put("!('USD'=='EUR')", "true");
    KNOWN_EXPRESSIONS.put("'EUR'=='EUR'", "true");
    KNOWN_EXPRESSIONS.put("1!=10", "true");
    KNOWN_EXPRESSIONS.put("10!=10", "false");
    KNOWN_EXPRESSIONS.put("1==2", "false");
    KNOWN_EXPRESSIONS.put("1!=2", "true");
    KNOWN_EXPRESSIONS.put("10==2", "false");
    KNOWN_EXPRESSIONS.put("10!=2", "true");
  }

  public String evaluate(String expression)
  {
    String result = KNOWN_EXPRESSIONS.get(expression);
    if (result != null)
      return result;
    String ERROR_MARKER = "SCRIPT_ERROR: ";
    String stub = "function x(){ try { return " + expression + "; } catch(e) { return '" + ERROR_MARKER + "'+e; } } x(); ";
    result = "";

    Context jsContext = ContextFactory.getGlobal().enterContext();
    jsContext.setOptimizationLevel(-1);

    try
    {
      Object jsResult = jsContext.evaluateString(jsContext.initStandardObjects(), stub, "evaluate:", 1, null);

      if (jsResult instanceof Boolean)
      {
        result = Boolean.toString((Boolean) jsResult);
      }

      if (result.startsWith(ERROR_MARKER))
      {
        String message = "Error evaluating expression <" + expression + ">: " + result.substring(ERROR_MARKER.length());
        throw new MBScriptErrorException(message);
      }
      else
      {
        // most expressions are extremely common, like "1==2" or "1!=2"
        // prevent instantiation of an expensive JS interpreter for future cases
        KNOWN_EXPRESSIONS.put(expression, result);
      }
    }
    finally
    {
      Context.exit();
    }

    return result;
  }

}