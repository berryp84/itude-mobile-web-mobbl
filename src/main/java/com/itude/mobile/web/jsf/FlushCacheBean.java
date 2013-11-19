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
package com.itude.mobile.web.jsf;

import java.io.Serializable;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.itude.mobile.mobbl2.client.core.util.MBCacheManager;

@RequestScoped
@Named
public class FlushCacheBean implements Serializable
{
  private static final Logger LOGGER           = Logger.getLogger(FlushCacheBean.class);

  private static final long   serialVersionUID = 1L;

  public String getFlushMessage()
  {
    MBCacheManager.flushGlobalCache();
    String message = "Cache flushed at " + new Date();
    LOGGER.info(message);
    return message;
  }

}
