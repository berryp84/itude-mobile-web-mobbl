/*
 * (C) Copyright ItudeMobile.
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
package com.itude.mobile.mobbl2.client.core.configuration.mvc.exceptions;

import com.itude.mobile.mobbl2.client.core.MBException;

public class MBInvalidPageTypeException extends MBException
{

  /**
   * 
   */
  private static final long serialVersionUID = -7306721136666175447L;

  public MBInvalidPageTypeException(String msg)
  {
    super(msg);
  }

  public MBInvalidPageTypeException(String msg, Throwable throwable)
  {
    super(msg, throwable);
  }

}
