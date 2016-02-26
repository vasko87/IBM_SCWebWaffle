package com.ibm.appium.model.partials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.model.MobilePageFrame;
import com.ibm.appium.model.Call.CreateCallPage;
import com.ibm.appium.model.Opportunity.CreateOpportunityPage;

public class ChooseLinkTypePage extends MobilePageFrame {
	Logger log = LoggerFactory.getLogger(ChooseLinkTypePage.class);

	// XPath Selectors
	public static String pageLoaded = "//article";
	public static String linkCalls = "//article[@module='Calls']";
	public static String linkOpportunities = "//article[@module='Opportunities']";
	public static String linkMeetings = "//article[@module='Meetings']";

	
	public ChooseLinkTypePage() {
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

	/**
	 * Link a new call to the selected item
	 * 
	 */
	public CreateCallPage linkNewCall() {
		click(linkCalls);
		return new CreateCallPage();
	}
	
	/**
	 * Link a new opportunity to the selected item
	 * 
	 */
	public CreateOpportunityPage linkNewOpportunity() {
		click(linkOpportunities);
		return new CreateOpportunityPage();
	}

	/**
	 * Link an existing call to the selected item
	 * 
	 */
	public LinkCallPage linkExistingCalls() {
		click(linkCalls);
		return new LinkCallPage();
	}

	/**
	 * Link an existing opportunity to the selected item
	 * 
	 */
	public LinkOpportunityPage linkExistingOpportunities() {
		click(linkMeetings);
		return new LinkOpportunityPage();
	}

	/**
	 * Link an existing meeting to the selected item
	 * 
	 */
	public LinkMeetingPage linkExistingMeetings() {
		click(linkMeetings);
		return new LinkMeetingPage();
	}

}
