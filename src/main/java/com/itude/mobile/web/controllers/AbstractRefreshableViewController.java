/*
 * (C) Copyright Itude Mobile B.V., The Netherlands
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itude.mobile.web.controllers;

import com.itude.mobile.mobbl.core.model.MBDocument;
import com.itude.mobile.mobbl.core.services.datamanager.handlers.MBDocumentOperationDelegate;
import com.itude.mobile.web.exceptions.ItudeRuntimeException;

public abstract class AbstractRefreshableViewController extends MBViewController implements MBDocumentOperationDelegate
{
  private static final long serialVersionUID = 1L;

  private boolean           _busy            = false;

  public void refresh()
  {
    // TODO: not thread safe, but not really needed at the moment
    if (!_busy) try
    {
      _busy = true;
      MBDocument doc = getPage().getDocument().clone();
      doc.reload();
      processResult(doc);
    }
    finally
    {
      _busy = false;
    }
  }

  @Override
  public void processResult(MBDocument document)
  {
    if (document != null) handleUpdate(document);
  }

  protected abstract void handleUpdate(MBDocument document);

  @Override
  public void processException(Exception e)
  {
    throw new ItudeRuntimeException(e);
  }

}
