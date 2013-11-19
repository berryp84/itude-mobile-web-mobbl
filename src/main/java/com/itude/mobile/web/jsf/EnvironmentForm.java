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

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.itude.mobile.mobbl2.client.core.util.MobblEnvironment;
import com.itude.mobile.web.util.VersionUtil;

/**
 * This form should be extended. When you extend this, please use the following:
 * @Named(value = "env")
 * @ApplicationScoped
 */
public class EnvironmentForm
{
  private static String _projectVersion;

  public String getPath()
  {
    FacesContext fc = FacesContext.getCurrentInstance();
    if (fc == null) return "";

    ExternalContext ec = fc.getExternalContext();
    return ec.getRequestContextPath();
  }

  public String getProjectVersion()
  {
    if (_projectVersion == null)
    {
      _projectVersion = VersionUtil.getVersion(getGroupId(), getArtifactId());
    }
    return _projectVersion;
  }

  protected String getGroupId()
  {
    return MobblEnvironment.getGroupId();
  }

  protected String getArtifactId()
  {
    return MobblEnvironment.getArtifactId();
  }
}
