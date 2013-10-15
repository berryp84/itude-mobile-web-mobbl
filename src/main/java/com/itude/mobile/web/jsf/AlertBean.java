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
package com.itude.mobile.web.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class AlertBean implements Serializable
{
  private static final long serialVersionUID = 1L;

  private boolean           _shown;
  private boolean           _notice;
  private String            _title;
  private List<String>      _messages;
  private boolean           _escapeHtml;

  @PostConstruct
  protected void init()
  {
    _shown = false;
    _notice = false;
    _title = "";
    _messages = new ArrayList<String>();
    _escapeHtml = true;
  }

  public boolean isShown()
  {
    return _shown;
  }

  public void setShown(boolean shown)
  {
    _shown = shown;
  }

  public boolean isDoHide()
  {
    _shown = false;
    return false;
  }

  public String getTitle()
  {
    return _title;
  }

  public void setTitle(String title)
  {
    _title = title;
  }

  public String getMessage()
  {
    return _messages.get(0);
  }

  public List<String> getMessages()
  {
    return _messages;
  }

  public void setMessage(String message)
  {
    _messages = new ArrayList<String>();
    _messages.add(message);
  }

  public void setMessages(List<String> messages)
  {
    _messages = messages;
  }

  public void setNotice(boolean notice)
  {
    _notice = notice;
  }

  public boolean isNotice()
  {
    boolean notice = _notice;
    _notice = false;
    return notice;
  }

  public boolean isEscapeHtml()
  {
    return _escapeHtml;
  }

  public void setEscapeHtml(boolean escapeHtml)
  {
    _escapeHtml = escapeHtml;
  }
}
