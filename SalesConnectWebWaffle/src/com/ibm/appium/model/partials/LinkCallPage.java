package com.ibm.appium.model.partials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.model.MobilePageFrame;

public class LinkCallPage extends MobilePageFrame {

	Logger log = LoggerFactory.getLogger(LinkCallPage.class);

	// XPath Selectors
	public static String pageLoaded = "//article";
	
	public LinkCallPage() {
		Assert.assertTrue(isPageLoaded(), "Client Select page has not loaded");
	}

	/**
	 * Check if new page is loaded by finding element on that page before
	 * starting any actions.
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}

}
