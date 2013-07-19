package com.itude.mobile.mobbl2.client.core.util;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

import com.itude.mobile.mobbl2.client.core.services.MBLocalizationService;
import com.itude.mobile.mobbl2.client.core.util.exceptions.MBDateParsingException;
import com.itude.mobile.mobbl2.client.core.util.exceptions.MBInvalidRelativePathException;

public final class StringUtilities
{
  private static final Logger LOG                 = Logger.getLogger(StringUtilities.class);

  private static Locale       defaultFormattingLocale;

  private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

  private StringUtilities()
  {
  }

  private static void setupFormatter(DecimalFormat formatter, int minumumDecimals, int maximumDecimals)
  {
    formatter.setDecimalFormatSymbols(new DecimalFormatSymbols(getDefaultFormattingLocale()));
    formatter.setMinimumIntegerDigits(1);

    if (minumumDecimals > -1)
    {
      formatter.setMinimumFractionDigits(minumumDecimals);
    }
    if (maximumDecimals > -1)
    {
      formatter.setMaximumFractionDigits(maximumDecimals);
    }
    formatter.setGroupingUsed(true);
    formatter.setGroupingSize(3);
  }

  private static ThreadLocal<SimpleDateFormat> tLDefaultDateFormatter = new ThreadLocal<SimpleDateFormat>()
                                                                      {
                                                                        @Override
                                                                        protected SimpleDateFormat initialValue()
                                                                        {
                                                                          return new SimpleDateFormat(DEFAULT_DATE_FORMAT);
                                                                        }
                                                                      };

  public static String stripCharacters(String inputString, String stripCharacters)
  {
    char[] charArray = stripCharacters.toCharArray();

    for (char c : charArray)
    {
      inputString = inputString.replace(String.valueOf(c), "");
    }

    return inputString;
  }

  public static String stripCharacter(String inputString, char stripCharacter)
  {
    return inputString.replaceAll(Pattern.quote(Character.toString(stripCharacter)), "");
  }

  /**
   * Returns a List of path-parts with some light processing.
   * for example the path
   * /a/b/c/////.///d/../e
   * is returned as a list containing
   * a,b,c,e
   * multiple adjacent /-es are ignored
   * a . is removed
   * a .. is interpreted as: pop the previous path part (d in the example above)  
   * 
   * @param path
   * @return
   */
  public static List<String> splitPath(String toSplit)
  {
    // performance tuned implementation of splitPath
    // measurements show this impl takes just 25% compared to the old
    // implementation
    List<String> components = new ArrayList<String>();
    int previousPosition = 0;
    int slashPosition;
    while ((slashPosition = toSplit.indexOf('/', previousPosition)) >= 0)
    {
      String component = toSplit.substring(previousPosition, slashPosition);
      previousPosition = slashPosition + 1;

      processPathComponent(component, components, toSplit);
    }

    if (previousPosition < toSplit.length())
    {
      // this happens when the path is something like /a/b/c
      // (no trailing forward slash).
      String component = toSplit.substring(previousPosition);
      processPathComponent(component, components, toSplit);
    }

    return components;
  }

  private static void processPathComponent(String component, List<String> componentsInPath, String completePath)
  {
    if (component.length() == 0 || (component.length() == 1 && component.equals(".")))
    {
      // nothing, ignore this component
    }
    else if (component.length() == 2 && component.equals(".."))
    {
      // pop the previous path component
      if (componentsInPath.size() == 0)
      {
        throw new MBInvalidRelativePathException(completePath);
      }
      componentsInPath.remove(componentsInPath.size() - 1);
    }
    else
    {
      componentsInPath.add(component);
    }
  }

  public static String normalizedPath(String path)
  {
    // try to prevent work in the normal case (the path is already normalized)
    // especially the splitPath method-call is expensive.
    if (path.indexOf('.') < 0 && path.indexOf("//") < 0)
    {
      // remove trailing / if present
      if (path.endsWith("/")) return path.substring(0, path.length() - 1);
      else return path;
    }
    boolean isRelative = !path.startsWith("/");

    StringBuilder result = new StringBuilder();
    for (String component : splitPath(path))
    {
      result.append('/').append(component);
    }

    if (isRelative && result.length() > 0 && result.charAt(0) == '/')
    {
      return result.substring(1);
    }
    else
    {
      return result.toString();
    }
  }

  /**
   * 
   * @param level
   * @return String of length level spaces
   * 
   */
  public static String getIndentStringWithLevel(int level)
  {
    StringBuilder rt = new StringBuilder(level);
    return appendIndentString(rt, level).toString();
  }

  /**
   * Appends spaces to the supplied StringBuffer, returns the same StringBuffer.
   * 
   * @param appendToMe
   * @param level
   * @return the same StringBuffer given as a param, useful for chaining calls
   */
  private static StringBuilder appendIndentString(StringBuilder appendToMe, int level)
  {
    while (level-- > 0)
      appendToMe.append(' ');

    return appendToMe;
  }

  //returns a string formatted as a number with the original amount of decimals assuming the receiver is a float 
  //WARNING: Only use this method to present data to the screen (BINCKAPPS-32, BINCKMOBILE-35, BINCKMOBILE-113)
  public static String formatNumberWithOriginalNumberOfDecimals(String stringToFormat)
  {

    if (stringToFormat == null || stringToFormat.length() == 0)
    {
      return null;
    }

    String result = null;

    DecimalFormat formatter = new DecimalFormat("#################.####################", new DecimalFormatSymbols(
        getDefaultFormattingLocale()));

    try
    {
      result = formatter.format(Double.parseDouble(stringToFormat));
    }
    catch (Exception e)
    {
      LOG.warn("Could not format string " + stringToFormat + " as number with original number of decimals (StringUtilities)", e);

      return null;
    }

    return result;
  }

  // Formats the date depending on the current date assuming the receiver is a date string 
  // If the date is equal to the current date, the time is given back as a string
  // If the date is NOT equal to the current date, then a a date is presented back as a string
  public static String formatDateDependingOnCurrentDate(String dateString)
  {
    if (isEmpty(dateString))
    {
      return "";
    }

    String result = dateString;
    Date date = dateFromXML(dateString);

    DateFormat df;
    // We can't just compare two dates, because the time is also compared.
    // Therefore the time is removed and the two dates without time are compared
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    Calendar today = Calendar.getInstance();
    today.setTime(new Date());

    if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH)
        && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR))
    {
      df = new SimpleDateFormat("HH:mm:ss");

    }
    else
    {
      df = DateFormat.getDateInstance(DateFormat.SHORT,
                                      getDefaultFormattingLocale() != null ? getDefaultFormattingLocale() : Locale.getDefault());
    }

    // Format the date
    try
    {
      result = df.format(date);
      return result;
    }
    catch (Exception e)
    {
      throw new MBDateParsingException("Could not get format date depending on current date with input string: " + dateString, e);
    }

  }

  public static synchronized Date dateFromXML(String stringToFormat)
  {
    try
    {
      if (stringToFormat == null)
      {
        LOG.warn("stringToFormat == null!");
        return new Date();
      }

      String dateString = stringToFormat.substring(0, 19);
      if (dateString != null) return tLDefaultDateFormatter.get().parse(dateString);
      else return null;
    }
    catch (Exception e)
    {
      throw new MBDateParsingException("Could not parse date from xml value: " + stringToFormat, e);
    }

  }

  public static String dateToString(Date date)
  {
    return dateToStringDefaultFormat(date);
  }

  public static String dateToString(Date date, String format)
  {
    if (isEmpty(format)) return dateToStringDefaultFormat(date);
    SimpleDateFormat df = new SimpleDateFormat(format);

    try
    {
      return df.format(date);
    }
    catch (Exception e)
    {
      throw new MBDateParsingException("Could not convert date to string with input date: " + date, e);
    }

  }

  private static String dateToStringDefaultFormat(Date dateToFormat)
  {
    return tLDefaultDateFormatter.get().format(dateToFormat);
  }

  // returns a string formatted as a number with two decimals assuming the receiver is a float string read from XML
  // WARNING: Only use this method to present data to the screen (BINCKAPPS-32, BINCKMOBILE-35, BINCKMOBILE-113)
  public static String formatNumberWithTwoDecimals(String stringToFormat)
  {
    return formatNumberWithDecimals(stringToFormat, 2);
  }

  // returns a string formatted as a number with three decimals assuming the receiver is a float string read from XML
  // WARNING: Only use this method to present data to the screen (BINCKAPPS-32, BINCKMOBILE-35, BINCKMOBILE-113)
  public static String formatNumberWithThreeDecimals(String stringToFormat)
  {
    return formatNumberWithDecimals(stringToFormat, 3);
  }

  /***
   * 
   * @param stringToFormat
   * @param numberOfDecimals can be any number, also negative as the used DecimalFormatter accepts it and makes it 0
   * @return
   */
  public static String formatNumberWithDecimals(String stringToFormat, int numberOfDecimals)
  {
    return formatNumberWithDecimals(stringToFormat, numberOfDecimals, numberOfDecimals);
  }

  /***
   * 
   * @param stringToFormat
   * @param numberOfDecimals can be any number, also negative as the used DecimalFormatter accepts it and makes it 0
   * @return
   */
  public static String formatNumberWithDecimals(String stringToFormat, int minimalNumberOfDecimals, int maximalNumberOfDecimals)
  {
    if (stringToFormat == null || stringToFormat.length() == 0)
    {
      return null;
    }

    String result = null;

    DecimalFormat formatter = new DecimalFormat();
    setupFormatter(formatter, minimalNumberOfDecimals, maximalNumberOfDecimals);

    result = formatter.format(Double.parseDouble(stringToFormat));

    return result;
  }

  // returns a string formatted as a price with two decimals assuming the receiver is a float string read from XML
  // WARNING: Only use this method to present data to the screen (BINCKAPPS-32, BINCKMOBILE-35, BINCKMOBILE-113)
  public static String formatPriceWithTwoDecimals(String stringToFormat)
  {
    return formatNumberWithDecimals(stringToFormat, 2);
  }

  public static String formatPriceWithThreeDecimals(String stringToFormat)
  {
    return formatNumberWithDecimals(stringToFormat, 3);
  }

  // returns a string formatted as a volume with group separators (eg, 131.224.000) assuming the receiver is an int string read from XML
  // WARNING: Only use this method to present data to the screen (BINCKAPPS-32, BINCKMOBILE-35, BINCKMOBILE-113)
  public static String formatVolume(String stringToFormat)
  {
    if (stringToFormat == null || stringToFormat.length() == 0)
    {
      return null;
    }

    String result = null;

    DecimalFormat formatter = new DecimalFormat();
    formatter.setDecimalFormatSymbols(new DecimalFormatSymbols(getDefaultFormattingLocale()));

    formatter.setGroupingUsed(true);
    formatter.setGroupingSize(3);
    formatter.setMaximumFractionDigits(0);

    result = formatter.format(Double.parseDouble(stringToFormat));

    return result;
  }

  // returns a string formatted as a percentage with two decimals assuming the receiver is a float string read from XML
  // the receiver's value should be "as displayed", eg for 30%, the receiver should be 30, not 0.3
  public static String formatPercentageWithTwoDecimals(String stringToFormat)
  {
    String formatted = formatPriceWithTwoDecimals(stringToFormat);
    if (formatted == null)
    {
      return null;
    }
    return formatted + "%";
  }

  public static String formatPercentageWithTwoDecimalsWithPlusSignInFrontOfIt(String stringToFormat)
  {
    String formatted = formatPriceWithTwoDecimals(stringToFormat);
    if (formatted == null)
    {
      return null;
    }
    return "+" + formatted + "%";
  }

  public static String formatPercentageWithTwoDecimalsWithMinSignInFrontOfIt(String stringToFormat)
  {
    String formatted = formatPriceWithTwoDecimals(stringToFormat);
    if (formatted == null)
    {
      return null;
    }
    return "-" + formatted + "%";
  }

  public static String md5(String stringToHash)
  {
    MessageDigest digest = null;
    try
    {
      digest = MessageDigest.getInstance("MD5");
      digest.update(stringToHash.getBytes());
      byte[] hash = digest.digest();
      return new String(Hex.encodeHex(hash));
    }
    catch (Exception e)
    {
      LOG.warn("Could not create hash of following string: " + stringToHash);
    }

    return null;
  }

  public static void setDefaultFormattingLocale(Locale defaultFormattingLocale)
  {
    StringUtilities.defaultFormattingLocale = defaultFormattingLocale;
  }

  public static Locale getDefaultFormattingLocale()
  {
    if (defaultFormattingLocale == null)
    {
      defaultFormattingLocale = MBLocalizationService.getInstance().getLocale();
    }

    return defaultFormattingLocale;
  }

  public static String replaceAdditionalHTMLTags(String str)
  {
    str = str.replace("&apos;", "'");
    str = str.replace("&amp;", "&");
    str = str.replace("&quot;", "\"");
    // TODO: should be done in the iphone / android app as well
    str = str.replace("&apos;", "\'");
    return str;
  }

  public static String stripHTMLTags(String str)
  {
    return str.replaceAll("<[^>]*>", "");
  }

  /**
   * Capitalizes every word in str 
   */
  public static String capitalize(String str)
  {
    if (str == null || str.length() == 0) return str;

    boolean capitalizeNext = true;
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < str.length(); ++i)
    {
      char ch = str.charAt(i);
      if (capitalizeNext) result.append(Character.toUpperCase(ch));
      else result.append(ch);

      capitalizeNext = Character.isWhitespace(ch);
    }

    return result.toString();
  }

  public static boolean isEmpty(String value)
  {
    return (value == null || value.isEmpty());
  }

}