package com.ibm.appium.model.Client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.model.MobilePageFrame;
import com.ibm.appium.model.Contact.ContactDetailPage;

public class ClientDetailPage extends MobilePageFrame {

	Logger log = LoggerFactory.getLogger(ContactDetailPage.class);

	// XPath Selectors
	public static String pageLoaded = "//a[@class='title append-root']//span[@class='value']";
	public static String editButton = "//a[@class='icon icon-pencil fast-click-highlighted append-root']";

	public ClientDetailPage() {
		Assert.assertTrue(isPageLoaded(), "Client Detail Page has not loaded");
	}
	
	@Override
	public boolean isPageLoaded() {
//		isTutorial();
		return waitForPageToLoad(pageLoaded);
	}

	/**
	 * Get Client Business Name text located on Client detail page. 
	 */
	public String getBusinessName() {
		return getText(pageLoaded);
	}
}
