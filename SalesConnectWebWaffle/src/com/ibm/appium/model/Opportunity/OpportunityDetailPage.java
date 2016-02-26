package com.ibm.appium.model.Opportunity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.Objects.Opportunity;
import com.ibm.appium.model.MobilePageFrame;
import com.ibm.appium.model.RLI.CreateRLIPage;
import com.ibm.appium.model.RLI.RLIListPage;

public class OpportunityDetailPage extends MobilePageFrame {

	// XPath Selectors
	public static String pageLoaded = "//a[@class='title append-root']//span[@class='value']";
	public static String editButton = "//a[@class='icon icon-pencil fast-click-highlighted append-root']";
	public static String opptyDescription = "//a[@class='title append-root']//span[@class='value']";
	public static String opptyTag = "//div[@class='card']/*[13]//span[@class='value']";
	public static String opptyId = "//div[@class='card']/*[2]//span[@class='value']";
	public static String createRelatedLineItem = "//span[@class='label-module-sm label-ibm_RevenueLineItems']/../../a[@class='create-related']";
	public static String opptyRelatedLineItems = "//span[@class='label-module-sm label-ibm_RevenueLineItems']";
	
	Logger log = LoggerFactory.getLogger(OpportunityDetailPage.class);

	public OpportunityDetailPage() {
		Assert.assertTrue(isPageLoaded(),
				"Opportunity Detail Page has not loaded");
	}

	/**
	 * Check if new page is loaded by finding element on that page before
	 * starting any actions.
	 */
	@Override
	public boolean isPageLoaded() {
		isTutorial();
		return waitForPageToLoad(pageLoaded);
	}

	/**
	 * Get Opportunity description located on opportunity detail page.
	 */
	public String getOpportunityDescription() {
		return getText(opptyDescription);
	}

	/**
	 * Get the newly generated Opportunity ID located on opportunity detail
	 * page.
	 */
	public String getOpportunityId() {
		return getText(opptyId);
	}

	/**
	 * Get Opportunity tag located on opportunity detail page.
	 */
	public String getOpportunityTag() {
		return getText(opptyTag); // TODO fix this
	}

	/**
	 * Open edit opportunity from opportunity detail page.
	 * 
	 * @return CreateContactPage
	 */
	public CreateOpportunityPage editOppty() {
		click(editButton);
		return new CreateOpportunityPage();
	}
	
	/**
	 * Update the opportunity value with the newly generated details.
	 * 
	 * @return Opportunity
	 */
	public Opportunity updateOpportunityDetails(Opportunity oppty) {
		oppty.setsId(getOpportunityId());
		return oppty;
	}
	
	/**
	 * Open create new related Revenue Line Item page
	 * 
	 * @return CreateRLIPage
	 */
	public CreateRLIPage createRelatedLineItem() {
		click(createRelatedLineItem);
		return new CreateRLIPage();
	}
	
	/**
	 * Open related Revenue Line Item list page
	 * 
	 * @return RelatedRLIListPage
	 */
	public RLIListPage openRelatedLineItemListPage() {
		click(opptyRelatedLineItems);
		return new RLIListPage();
	}
}
