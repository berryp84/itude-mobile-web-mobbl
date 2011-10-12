package com.itude.mobile.web.controllers;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.itude.commons.exceptions.ItudeRuntimeException;
import com.itude.mobile.mobbl2.client.core.MBException;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBOutcomeDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBPageDefinition.MBPageType;
import com.itude.mobile.mobbl2.client.core.controller.MBAction;
import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.mobbl2.client.core.controller.exceptions.MBNoOutcomesDefinedException;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.model.MBElement;
import com.itude.mobile.mobbl2.client.core.services.MBDataManagerService;
import com.itude.mobile.mobbl2.client.core.services.MBLocalizationService;
import com.itude.mobile.mobbl2.client.core.services.MBMetadataService;
import com.itude.mobile.mobbl2.client.core.view.MBPage;
import com.itude.mobile.web.annotations.NamedQualifier;
import com.itude.mobile.web.annotations.PageQualifier;
import com.itude.mobile.web.jsf.AlertBean;

@RequestScoped
@Named
public abstract class ApplicationController
{
  private Logger                     _log = Logger.getLogger(ApplicationController.class);

  @Inject
  private CurrentView                _view;

  @Inject
  private AlertBean                  _alert;

  @Inject
  private MBMetadataService          _metadataService;

  @Inject
  private MBDataManagerService       _dataManagerService;

  @Inject
  @Any
  private Instance<MBAction>         _actions;

  @Inject
  @Any
  private Instance<MBViewController> _viewControllers;
  
  public abstract void initialize();
  
  public abstract void setInitialView();

  public abstract void initializeTab();

  public void handleOutcome(String outcomeName)
  {
    MBOutcome outcome = new MBOutcome(outcomeName, _view.getView().getDocument());
    outcome.setOriginName(_view.getView().getPage().getName());
    handleOutcome(outcome);
  }

  public void handleOutcome(MBOutcome outcome)
  {
    try
    {
      doHandleOutcome(outcome);
    }
    catch (Exception e)
    {
      handleException(outcome, e);
    }
  }

  public void doHandleOutcome(MBOutcome initialOutcome)
  {

    List<MBOutcomeDefinition> definitions = _metadataService.getOutcomeDefinitionsForOrigin(initialOutcome.getOriginName(),
                                                                                            initialOutcome.getOutcomeName());
    if (definitions.isEmpty()) throw new MBNoOutcomesDefinedException("No outcome found for view " + initialOutcome.getOriginName()
                                                                      + " with outcome " + initialOutcome.getOutcomeName());

    // TODO: are outcomes unambiguous?
    // find outcome with matching precondigion
    MBOutcome outcome = null;
    for (MBOutcomeDefinition def : definitions)
    {

      MBOutcome outcomeToProcess = new MBOutcome(def);

      outcomeToProcess.setPath(initialOutcome.getPath());
      outcomeToProcess.setDocument(initialOutcome.getDocument());
      outcomeToProcess.setDialogName(initialOutcome.getDialogName());
      outcomeToProcess.setNoBackgroundProcessing(initialOutcome.getNoBackgroundProcessing() || def.getNoBackgroundProcessing());
      // TODO: is this correct? Could this cause any problems?
      if(initialOutcome.getTransferDocument())
        outcomeToProcess.setTransferDocument(true);

      if (outcomeToProcess.isPreConditionValid()) outcome = outcomeToProcess;

    }

    if ("POPUP".equals(outcome.getDisplayMode())) _view.pushView(null);
    else if ("POP".equals(outcome.getDisplayMode())) _view.popView();
    else if ("MODAL".equals(outcome.getDisplayMode())) _view.pushView(null);
    else if ("ENDMODAL".equals(outcome.getDisplayMode())) _view.popView();

    // find out if the outcome's action corresponds to an action object
    Instance<MBAction> refined = _actions.select(new NamedQualifier(outcome.getAction()));
    if (!refined.isUnsatisfied())
    {
      MBOutcome next = refined.get().execute(outcome.getDocument(), outcome.getPath(), outcome.getOutcomeName());
      if (next != null)
      {
        next.setOriginName(outcome.getAction());
        handleOutcome(next);
      }
    }
    else if (!"none".equals(outcome.getAction()))
    {
      _log.info("Going to page " + outcome.getAction());
      MBViewController controller = getControllerForPage(outcome.getAction());

      if (outcome.getTransferDocument()) controller.setDocument(outcome.getDocument());
      else controller.setDocument(null);

      controller.constructPage(outcome);

      controller.onPageCreated();

      if (MBPageType.MBPageTypesErrorPage.equals(controller.getPage().getPageType())) showAlertView(controller.getPage());
      else _view.replaceView(controller);
    }
  }

  private void showAlertView(MBPage page)
  {
    String title = null;
    String message = null;
    if (page.getDocument().getName().equals("MBException"))
    {
      title = page.getDocument().getValueForPath("/Exception[0]/@name");
      message = page.getDocument().getValueForPath("/Exception[0]/@description");
    }
    else
    {
      title = page.getTitle();
      message = MBLocalizationService.getInstance().getTextForKey((String) page.getDocument().getValueForPath("/message[0]/@text"));
    }

    _alert.setMessage(message);
    _alert.setTitle(title);
    _alert.setShown(true);
   // _view.replaceView(_view.getView());
  }

  protected void handleException(MBOutcome outcome, Exception e)
  {
      _log.warn("________EXCEPTION RAISED______________________________________________________________");
      _log.warn(e.getClass().getSimpleName() + ": " + e.getMessage());
      for (StackTraceElement ste : e.getStackTrace())
        _log.warn(ste.toString());
      _log.warn("______________________________________________________________________________________");

    String name;
    if (e instanceof MBException) name = ((MBException) e).getName();
    else name = e.getClass().getSimpleName();

    MBDocument exceptionDocument = _dataManagerService.loadDocument("MBException");
    exceptionDocument.setValue(MBLocalizationService.getInstance().getTextForKey(name), "/Exception[0]/@name");
    exceptionDocument.setValue(MBLocalizationService.getInstance().getTextForKey(e.getMessage()), "/Exception[0]/@description");
    exceptionDocument.setValue(outcome.getOriginName(), "/Exception[0]/@origin");
    exceptionDocument.setValue(outcome.getOutcomeName(), "/Exception[0]/@outcome");
    for (StackTraceElement ste : e.getStackTrace())
    {
      String line = ste.toString();
      MBElement stackLine = exceptionDocument.createElementWithName("/Exception[0]/Stackline");
      stackLine.setValue(line, "./@line");
    }

    _dataManagerService.storeDocument(exceptionDocument);

    // See if there is an outcome defined for this particular exception
    List<MBOutcomeDefinition> outcomeDefinitions = _metadataService.getOutcomeDefinitionsForOrigin(outcome.getOriginName(), name, false);
    if (!outcomeDefinitions.isEmpty())
    {
      MBOutcome specificExceptionHandler = new MBOutcome(outcome);
      specificExceptionHandler.setOutcomeName(name);
      specificExceptionHandler.setDocument(exceptionDocument);
      handleOutcome(specificExceptionHandler);
    }
    else
    {
      outcomeDefinitions = _metadataService.getOutcomeDefinitionsForOrigin(outcome.getOriginName(), "exception", false);
      if (outcomeDefinitions.isEmpty())
      {
        _log.warn("No outcome with origin=" + outcome.getOriginName()
                  + " name=exception defined to handle errors; so re-throwing exception");
        // unrecoverable
        throw new ItudeRuntimeException(e);
      }
      if (outcome.getOutcomeName().equals("exception"))
      {
        _log.warn("Error in handling an outcome with name=exception (i.e. the error handling in the controller is probably misconfigured) Re-throwing exception");
        // unrecoverable
        throw new ItudeRuntimeException(e);
      }

      MBOutcome genericExceptionHandler = new MBOutcome("exception", exceptionDocument);
      genericExceptionHandler.setDialogName(outcome.getDialogName());
      genericExceptionHandler.setPath(outcome.getPath());
      // TODO: is this correct? Could this cause any problems?
      genericExceptionHandler.setTransferDocument(true);

      doHandleOutcome(genericExceptionHandler);
    }
  }

  private MBViewController getControllerForPage(String name)
  {
    Instance<MBViewController> refined = _viewControllers.select(new PageQualifier(name));
    if (!refined.isUnsatisfied()) return refined.get();
    else
    {
      refined = _viewControllers.select(new NamedQualifier("Default"));
      return refined.get();
    }
  }
  
  public CurrentView getView()
  {
    return _view;
  }
  
  public MBDataManagerService getDataManagerService()
  {
    return _dataManagerService;
  }
}
