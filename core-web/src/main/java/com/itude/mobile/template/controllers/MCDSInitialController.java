package com.itude.mobile.template.controllers;

import com.itude.mobile.mcds.services.MCDSLocalizationService;
import com.itude.mobile.mobbl2.client.core.services.MBLocalizationService;

public class MCDSInitialController extends InitialController {
	public void initialize() {
		MBLocalizationService.getInstance().setInstance(
				new MCDSLocalizationService());
	}
}
