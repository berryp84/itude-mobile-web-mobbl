package com.itude.mobile.mobbl.server.mobileweb;

public interface Constants
{

  public static final String baseUrl                     = "m";
  public static final String BASEURL_ATT                 = "baseUrl";
  public static final String DATASOURCE                  = "jdbc/localDB";
  public static final String ENCODING                    = "UTF-8";
  public static final String MENU_FILENAME               = "menu.xml";
  public static final String MENU_STORE                  = "infomidlet";
  public static final int    NOT_REGISTERED              = 2;
  public static final int    OK                          = 1;
  public static final int    QUOTA_REACHED               = 3;

  //ADDED
  public static final String DENIEDPAGE                  = "deniedPage";
  public static final String XHTMLLEVEL                  = "xhtmlLevel";

  //  
  // HTML tags
  public static final String TAG_A                       = "a";
  public static final String TAG_ACTION                  = "action";
  public static final String TAG_ALT                     = "alt";
  public static final String RSS_TAG_ALTERNATE           = "alternate";
  public static final String TAG_ACCESSKEY               = "accesskey";
  public static final String TAG_B                       = "b";
  public static final String TAG_BASE                    = "base";
  public static final String TAG_BODY                    = "body";
  public static final String TAG_BGCOLOR                 = "bgcolor";
  public static final String TAG_BR                      = "br";
  public static final String TAG_BREAKBEFORETABLE        = "breakBeforeTable";
  public static final String TAG_BREAKBEFOREROW          = "breakBeforeRow";
  public static final String TAG_BLOCK                   = "block";
  public static final String TAG_CALLERPROTOCOL          = "callerProtocol";
  public static final String TAG_CHECKED                 = "checked";
  public static final String TAG_CLASS                   = "class";
  public static final String TAG_COLOR                   = "color";
  public static final String TAG_COLORIZE                = "colorize";
  public static final String TAG_CONTENTTYPE             = "content-type";
  public static final String TAG_CONVERTJAVASCRIPT       = "convertJavaScript";
  public static final String TAG_COOLMENU                = "cool_menu";
  public static final String TAG_DEFAULT                 = "default";
  public static final String TAG_DISABLED                = "disabled";
  public static final String TAG_DIV                     = "div";
  public static final String TAG_EM                      = "em";
  public static final String TAG_ENABLEWML               = "enable_wml";
  public static final String TAG_ENCODING                = "encoding";
  public static final String TAG_FIRSTCELLBOLD           = "firstCellBold";
  public static final String TAG_FORM                    = "form";
  public static final String TAG_FORMAT                  = "format";
  public static final String TAG_FRAME                   = "frame";
  public static final String TAG_H1                      = "h1";
  public static final String TAG_H2                      = "h2";
  public static final String TAG_H3                      = "h3";
  public static final String TAG_H4                      = "h4";
  public static final String TAG_H5                      = "h5";
  public static final String TAG_H6                      = "h6";
  public static final String TAG_HR                      = "hr";
  public static final String TAG_HTML                    = "html";
  public static final String TAG_HEAD_LINK               = "link";
  public static final String TAG_HEAD_STYLE              = "style";
  public static final String TAG_HREF                    = "href";
  public static final String TAG_HTTP                    = "http";
  public static final String TAG_HTTPS                   = "https";
  public static final String TAG_HTTPEQUIV               = "http-equiv";
  public static final String TAG_I                       = "i";
  public static final String TAG_ID                      = "id";
  public static final String TAG_IFRAME                  = "iframe";
  public static final String TAG_IGNOREIMGSRC            = "ignoreImgSrc";
  public static final String TAG_IGNORELINKSRC           = "ignoreLinkSrc";
  public static final String TAG_IGNORENOFRAMES          = "ignoreNoFrames";
  public static final String TAG_IGNORENOSCRIPT          = "ignoreNoScript";
  public static final String TAG_IGNOREID                = "ignoreId";
  public static final String TAG_IGNORECLASS             = "ignoreClass";
  public static final String TAG_IGNORENBSP              = "ignoreNbsp";
  public static final String TAG_IMG                     = "img";
  public static final String TAG_IMAGE_DEFAULT_VALUE     = "Image";
  public static final String TAG_SRC                     = "src";
  public static final String TAG_INPUT                   = "input";
  public static final String TAG_LABEL                   = "label";
  public static final String TAG_LI                      = "li";
  public static final String TAG_MAXLENGTH               = "maxlength";
  public static final String TAG_MENU                    = "menu";
  public static final String TAG_META                    = "meta";
  public static final String TAG_MULTIPLE                = "multiple";
  public static final String TAG_MOVEIMAGETOTOPSRC       = "moveImageToTopSrc";
  public static final String TAG_MOVETOBOTTOMID          = "moveToBottomId";
  public static final String TAG_MOVETOTOPID             = "moveToTopId";
  public static final String TAG_MOVETOBOTTOMCLASS       = "moveToBottomClass";
  public static final String TAG_MOVETOTOPCLASS          = "moveToTopClass";
  public static final String TAG_NAME                    = "name";
  public static final String TAG_NOFRAMES                = "noframes";
  public static final String TAG_NOSCRIPT                = "noscript";
  public static final String TAG_REL                     = "rel";
  public static final String TAG_READONLY                = "readonly";
  public static final String TAG_REFRESH                 = "refresh";
  public static final String TAG_OL                      = "ol";
  public static final String TAG_ONCLICK                 = "onclick";
  public static final String TAG_OPTION                  = "option";
  public static final String TAG_P                       = "p";
  public static final String TAG_PARSEDIV                = "parseDiv";
  public static final String TAG_PARSELINK               = "parselink";
  public static final String TAG_PARSEIMG                = "parseImg";
  public static final String TAG_PARSEFORM               = "parseForm";
  public static final String TAG_PARSEBODYCOLORS         = "parseBodyColors";
  public static final String TAG_PARSESTYLES             = "parseStyles";
  public static final String TAG_PARSECLASS              = "parseClass";
  public static final String TAG_SCRIPT                  = "script";
  public static final String TAG_SELECT                  = "select";
  public static final String TAG_SIZE                    = "size";
  public static final String TAG_SPAN                    = "span";
  public static final String TAG_SPACEBEFORECELL         = "spaceBeforeCell";
  public static final String TAG_STRONG                  = "strong";
  public static final String TAG_STYLE                   = "style";
  public static final String TAG_STYLESHEET              = "stylesheet";
  public static final String TAG_TABLE                   = "table";
  public static final String TAG_TEL                     = "tel";
  public static final String TAG_TEXTAREA                = "textarea";
  public static final String TAG_TITLE                   = "title";
  public static final String TAG_TR                      = "tr";
  public static final String TAG_TD                      = "td";
  public static final String TAG_TEXT                    = "text";
  public static final String TAG_TYPE                    = "type";
  public static final String TAG_UL                      = "ul";
  public static final String TAG_USEATOMFEEDS            = "useAtomFeeds";
  public static final String TAG_USERSSFEEDS             = "useRssFeeds";
  public static final String TAG_UNABLETOPARSEURL        = "unableToParseUrl";
  public static final String TAG_VALUE                   = "value";
  public static final String TAG_WALL                    = "wall:";
  public static final String TAG_XHTML                   = "xhtml";

  public static final String INPUTTYPE_TEXT              = "text";
  public static final String INPUTTYPE_CHECK             = "check";
  public static final String INPUTTYPE_RADIO             = "radio";
  public static final String INPUTTYPE_HIDDEN            = "hidden";
  public static final String INPUTTYPE_PASSWORD          = "password";
  public static final String SUBMITTYPE_SUBMIT           = "submit";
  public static final String SUBMITTYPE_BUTTON           = "button";
  public static final String SUBMITTYPE_IMAGE            = "image";

  // image attributes
  public static final String IMG_WIDTH                   = "width";
  public static final String IMG_MAXIMAGEWIDTH           = "max_image_width";
  public static final String IMG_HEIGHT                  = "height";
  public static final String IMG_MAXIMAGEHEIGHT          = "max_image_height";
  public static final String IMG_RATIO                   = "ratio";
  public static final String IMG_TRANSPARENT             = "preserveTransparency";

  //
  // url attributes used to communicate between jsp-files.
  // these attributes are NOT passed to the source site.
  public static final String PROJECT_ATT                 = "project";
  public static final String USER_ATT                    = "user";
  public static final String FORM_METHOD_ATT             = "method";
  public static final String LOCATION_ATT                = "location";
  public static final String USER_AGENT_ATT              = "UA";
  public static final String CACHE_ATT                   = "cache";
  public static final String DEBUG_ATT                   = "debug";
  public static final String INCLUDE_MENU_ID_ATT         = "includeId";                              // MWP-specific attribute
  public static final String PREVIEW_ATT                 = "preview";
  public static final String RSS_ATT                     = "rss";
  public static final String INDEX_SUBMIT_ATT            = "_mobbl1";
  public static final String MOBBL_PREFIX                = "_mobbl";
  public static final String ESCAPE_XML_CHARS            = "escapeXmlChars";
  // URL nalopen (pageparser parsed page includer uit httprequest moet hij deze gebruiken): pageparser.jsp?=nu.nl
  public static final String URL_ATT                     = "url";                                    //ATT? replaced with XmlTags.ATT_PLATFORMREQUEST_URL 

  //
  // url attributes used to communicate between sessions.
  // these attributes are NOT passed to the source site.
  public static final String MOBBL_SESSION               = "_mobblSession";
  //

  //property names used in converter.properties
  public static final String PROP_CACHE_TIME             = "cacheTime";

  //public static final String MAIN_MENU_ID = "main";// replaced with XmlTags.C_MENUITEM_ID_MAIN
  public static final int    MAX_IMAGE_SIZE              = 500000;
  public final static String pageParserUrl               = "p";
  public final static String menuViewerUrl               = "m";
  public final static String resourceServerUrl           = "r";
  public final static String imageBaseUrl                = "i";
  public static final String SINGLE_SPACE                = " ";
  public static final String INCLUDE_SHOW_LABEL          = "showLabelInParent";
  public static final String MENU_HEADING_STYLES         = "Style";                                  // XML att Style
  public static final String parsedPageIncluderUrl       = "parsedPageIncluder.jsp";
  public static final String ATOM_CONTENT                = "content";
  public static final String TAG_HEAD                    = "head";
  public static final String LOGGING_UNIVERSE_ID         = "universeID";
  public static final String LOGGING_FORWARD_URL         = "forwardUrl";
  public static final String LOGGING_ACCESS_LOG_FLAG     = "_MWP_UID=";

  public static final String STYLE                       = "style";
  public static final String CACHED_MENUXML              = "_cached_menuXML";

  // TODO: Added constants since refactoring. Check if correct
  public static final String AUDIO                       = "Audio";
  public static final String SUPPRESS_IMAGE_RESCALING    = "suppress_image_rescaling";
  public static final String CAPABILITIES                = "capabilities";
  public static final String DEVICE_OS                   = "device_os";
  //public static final String CONVERT = "convert";
  public static final String FILE                        = "file";
  public static final String LOC                         = "loc";
  public static final String MENUICONS                   = "menuIcons";
  public static final String BODYTEXT                    = "bodyText";
  public static final String IMAGEHREF_ATT               = "imageHref";                              // Let op imageHref met H, NIET imageRef
  public static final String MENUSEPERATOR               = "menuSeparator";
  public static final String REFS_ATT                    = "refs";
  public static final String IDS_ATT                     = "ids";
  public static final String HASMENUS_ATT                = "hasMenus";
  public static final String PARSEDPAGEINCLIDERURL_ATT   = "parsedPageIncluderUrl";
  public static final String PARSEDPAGEINCLUDES_ATT      = "parsedPageIncludes";
  public static final String LABELSTYLES_ATT             = "labelStyles";
  public static final String LABELS_ATT                  = "labels";

  public static final String HOSTCONTEXT                 = "http://straysystems.com/mwp/";
  public static final String NAMESPACEURI                = "http://straysystems.com/xsd/strayclient";
  public static final String MAILADRES                   = "mwp@straysystems.com";

  // TODO: Constants used in htmlToWallConverter. Check if they are correct
  public static final String USERAGENTOVERRIDE_PROPERTY  = "userAgentOverride";
  public static final String USER_AGENT                  = "User-Agent";
  public static final String CONTENT_TYPE                = "Content-Type";
  public static final String CONTENT_LENGTH              = "Content-Length";
  public static final String SEARCHREPLACE               = "searchReplace";
  public static final String STACK_EL                    = "stack";
  public static final String HTMLFRAGMENTTOP_PROPERTY    = "htmlFragmentTop";
  public static final String HTMLFRAGMENTBOTTOM_PROPERTY = "htmlFragmentBottom";

  // TODO: Constants used in jsp-files
  public static final String FORM                        = "Form";
  public static final String ISFORM                      = "isForm";

  // TODO Replaced or deleted attributes
  public static final String APPLICATION_ID              = "applicationID";                          //mainly for XmlTags.ATT_SENDDATA_APPLICATIONID
  public static final String HIDE_TITLES                 = "hideTitles";                             // for html tags to empty title attribute
  public static final String TRANSLATIONKEY              = "translationKey";                         // for html tags to empty title attribute
}
