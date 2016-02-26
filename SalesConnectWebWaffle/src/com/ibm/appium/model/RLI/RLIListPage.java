package com.ibm.appium.model.RLI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.model.MobilePageFrame;

public class RLIListPage extends MobilePageFrame {
	
	Logger log = LoggerFactory.getLogger(RLIListPage.class);

	// XPath Selectors
	public static String pageLoaded = "//div[@id='listing-ibm_RevenueLineItems']";
	public static String searchFieldCSS = "//input[@placeholder='Search']";
	public static String searchClear = "//i[@class='icon clear-button icon-remove-sign']";
	public static String quickCreateButton = "//a[class='createBtn']";
	public static String firstItem = "//div[@class='items']//article[1]";
	
	
	public RLIListPage() {
		Assert.assertTrue(isPageLoaded(),
				"RLI List Page has not loaded");
	}

	/**
	 * Check if new page is loaded by finding element on that page before
	 * starting any actions.
	 */
	@Override
	public boolean isPageLoaded() {
		//isTutorial();
		return waitForPageToLoad(pageLoaded);
	}

	/**
	 * Get specific Opportunity record from Opportunity list view.
	 * 
	 * @param sOpptyDescription
	 *            - Opportunity description
	 */
	public CreateRLIPage createRLI() {
		click(quickCreateButton);
		return new CreateRLIPage();
	}

	/**
	 * Open first related RLI from Related RLI list view.
	 * 
	 * @param sOpptyDescription
	 *            - Opportunity description
	 */
	public RLIDetailPage openFirstRLI() {
		click(firstItem);
		return new RLIDetailPage();
	}
}
