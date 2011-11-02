package com.itude.mobile.mobbl2.client.core.controller;

import java.io.Serializable;

import com.itude.mobile.mobbl2.client.core.model.MBDocument;

public interface MBAction extends Serializable
{
  public MBOutcome execute(MBDocument document, String path, String outcomeName);

}
