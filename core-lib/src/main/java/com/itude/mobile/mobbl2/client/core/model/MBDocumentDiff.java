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
package com.itude.mobile.mobbl2.client.core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.itude.mobile.mobbl2.client.core.util.StringUtilities;

public class MBDocumentDiff
{
  private final HashSet<String>     _modified;
  private final Map<String, String> _aValues;
  private final Map<String, String> _bValues;

  public MBDocumentDiff(MBDocument a, MBDocument b)
  {
    _modified = new HashSet<String>();
    _aValues = new HashMap<String, String>();
    _bValues = new HashMap<String, String>();

    diffA(a, b);
  }

  private String normalize(String path)
  {
    if (!path.endsWith("/"))
    {
      path = "/" + path;
    }

    return StringUtilities.normalizedPath(path);
  }

  private void diffA(MBDocument a, MBDocument b)
  {
    HashSet<String> set = new HashSet<String>();
    a.addAllPathsTo(set, "");
    b.addAllPathsTo(set, "");

    for (String changedPath : set)
    {
      String path = normalize(changedPath);
      String valueA = (String) a.getValueForPath(path);
      String valueB = (String) b.getValueForPath(path);

      if ((valueA != null && valueB == null) || (valueA == null && valueB != null))
      {
        _modified.add(path);
      }
      else if (valueA != null && valueB != null && !valueA.equals(valueB))
      {
        _modified.add(path);
        _aValues.put(path, valueA);
        _bValues.put(path, valueB);
      }
    }

  }

  public boolean isChanged()
  {
    return _modified.size() != 0;
  }

  public boolean isChanged(String path)
  {
    return _modified.contains(normalize(path));
  }

  public Set<String> getPaths()
  {
    return _modified;
  }

  public String valueOfAForPath(String path)
  {
    return _aValues.get(normalize(path));
  }

  public String valueOfBForPath(String path)
  {
    return _bValues.get(normalize(path));
  }

  @Override
  public String toString()
  {
    return _modified.toString();
  }

}
