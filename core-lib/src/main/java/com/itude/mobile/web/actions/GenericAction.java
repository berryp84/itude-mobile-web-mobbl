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
package com.itude.mobile.web.actions;

import javax.inject.Inject;

import com.itude.mobile.mobbl2.client.core.controller.MBAction;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.services.MBDataManagerService;
import com.itude.mobile.mobbl2.client.core.services.MBMetadataService;

public abstract class GenericAction implements MBAction
{
  @Inject
  private MBDataManagerService _dataManagerService;

  @Inject
  private MBMetadataService    _metadataService;

  protected MBDataManagerService getDataManagerService()
  {
    return _dataManagerService;
  }

  protected MBDocument loadDocument(String name)
  {
    return getDataManagerService().loadDocument(name);
  }

  protected MBDocument loadDocument(String name, MBDocument arguments)
  {
    return getDataManagerService().loadDocument(name, arguments);
  }

  protected MBMetadataService getMetadataService()
  {
    return _metadataService;
  }

}
