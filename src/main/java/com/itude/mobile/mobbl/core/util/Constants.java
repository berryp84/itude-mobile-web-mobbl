/*
 * (C) Copyright Itude Mobile B.V., The Netherlands
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itude.mobile.mobbl.core.util;

public interface Constants
{

  public static String       APPLICATION_NAME         = "MOBBL";

  //  public static String       C_SPLASHSCREEN           = "whitelabel-splashscreen";
  public static String       C_ENCODING               = "UTF-8";
  //  public static String       C_ARROW                  = "arrow";

  //container types
  public static final String C_SECTION                = "SECTION";
  public static final String C_ROW                    = "ROW";
  public static final String C_MATRIXHEADER           = "MATRIX-HEADER";
  public static final String C_MATRIXROW              = "MATRIX-ROW";
  // cell types for use in identifiers
  public static final String C_REGULARCELL            = "REGULARCELL";
  public static final String C_SUBTITLECELL           = "SUBTITLECELL";
  public static final String C_DROPDOWNLISTCELL       = "DROPDOWNLISTCELL";
  public static final String C_WEBVIEWCELL            = "WEBVIEWCELL";
  // field types
  public static final String C_FIELD_LABEL            = "LABEL";
  public static final String C_FIELD_SUBLABEL         = "SUBLABEL";
  public static final String C_FIELD_BUTTON           = "BUTTON";
  public static final String C_FIELD_TEXT             = "TEXT";
  public static final String C_FIELD_IMAGE            = "IMAGE";
  public static final String C_FIELD_ICON             = "ICON";
  public static final String C_FIELD_INPUT            = "INPUTFIELD";
  public static final String C_FIELD_USERNAME         = "USERNAMEFIELD";
  public static final String C_FIELD_PASSWORD         = "PASSWORDFIELD";
  public static final String C_FIELD_DROPDOWNLIST     = "DROPDOWNLIST";
  public static final String C_FIELD_CHECKBOX         = "CHECKBOX";
  public static final String C_FIELD_RADIOGROUPFIELD  = "RADIOGROUP";
  public static final String C_FIELD_MATRIXTITLE      = "MATRIX-TITLE";
  public static final String C_FIELD_MATRIXCELL       = "MATRIX-CELL";

  // field datatypes
  public static final String C_FIELD_DATATYPE_INT     = "int";
  public static final String C_FIELD_DATATYPE_double  = "double";
  public static final String C_FIELD_DATATYPE_float   = "float";

  // button styles
  public static final String C_FIELD_STYLE_NAVIGATION = "NAVIGATION";
  public static final String C_FIELD_STYLE_NETWORK    = "NETWORK";
  public static final String C_FIELD_STYLE_POPUP      = "POPUP";

  // Alignment
  public static final String C_ALIGNMENT_LEFT         = "LEFT";
  public static final String C_ALIGNMENT_CENTER       = "CENTER";
  public static final String C_ALIGNMENT_RIGHT        = "RIGHT";

  // Locale related constants
  public static final String C_LOCALE_CODE_DUTCH      = "nl_NL";

  public static final String C_TRUE                   = "true";
  public static final String C_FALSE                  = "false";
}
