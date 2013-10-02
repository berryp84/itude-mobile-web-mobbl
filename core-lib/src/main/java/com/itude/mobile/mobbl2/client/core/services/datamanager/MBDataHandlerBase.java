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
package com.itude.mobile.mobbl2.client.core.services.datamanager;

import org.apache.log4j.Logger;

import com.itude.mobile.mobbl2.client.core.model.MBDocument;

public class MBDataHandlerBase implements MBDataHandler
{
  private static final Logger LOGGER = Logger.getLogger(MBDataHandlerBase.class);

  @Override
  public MBDocument loadDocument(String documentName)
  {
    LOGGER.warn("MBDataHandlerBase: No loadDocument implementation for " + documentName);
    return null;
  }

  @Override
  public MBDocument loadDocument(String documentName, MBDocument args)
  {
    LOGGER.warn("MBDataHandlerBase: No loadDocument implementation for " + documentName);
    return null;
  }

  @Override
  public void storeDocument(MBDocument document)
  {
    LOGGER.warn("MBDataHandlerBase: No storeDocument implementation for " + document.getDefinition().getName());
  }

}
