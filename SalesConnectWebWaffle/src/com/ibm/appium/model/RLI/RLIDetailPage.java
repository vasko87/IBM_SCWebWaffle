package com.ibm.appium.model.RLI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.model.MobilePageFrame;

public class RLIDetailPage extends MobilePageFrame {

	// XPath Selectors
	public static String pageLoaded = "//span[contains(text(),'Offering Type (L-10)')]";
	public static String editButton = "//a[@class='icon icon-pencil fast-click-highlighted append-root']";

	public String getAmount(String amount) { return "//span[contains(text(),'"+ amount +"')]";	}
	
	public static String RLIOfferingTypeL10 = "//span[contains(text(),'Offering Type (L-10)')]/../span[@class='field-value value']";
	public static String RLISubBrandL15 = "//span[contains(text(),'Sub-Brand (L-15)')]/../span[@class='field-value value']";
	public static String RLIBrandCodeL20 = "//span[contains(text(),'Brand Code (L-20)')]/../span[@class='field-value value']";
	public static String RLIProductInformationL30 = "//span[contains(text(),'Product Information (L-30)')]/../span[@class='field-value value']";
	
	public static String RLIRoadmapStatus= "//span[contains(text(),'Roadmap')]/../span[@class='value']";
	public static String RLIProbability = "//span[contains(text(),'Probability')]/../span[@class='value']";
	
	Logger log = LoggerFactory.getLogger(RLIDetailPage.class);

	public RLIDetailPage() {
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
	 * Get RLI Offering Type L-10 located on RLI detail page.
	 */
	public String getRLIL10() {
		return getText(RLIOfferingTypeL10);
	}

	/**
	 * Get RLI Sub-Brand L-15 located on RLI detail page.
	 */
	public String getRLIL15() {
		return getText(RLISubBrandL15);
	}
	
	/**
	 * Get RLI Brand Code L-20 located on RLI detail page.
	 */
	public String getRLIL20() {
		return getText(RLIBrandCodeL20);
	}
	
	/**
	 * Get RLI Product Information L-30 located on RLI detail page.
	 */
	public String getRLIL30() {
		return getText(RLIProductInformationL30);
	}
	
	/**
	 * Get RLI Amount located on RLI detail page.
	 */
	public String getRLIAmount(String amount) {
		return getText(getAmount(amount));
	}
	
	/**
	 * Get RLI Roadmap Status located on RLI detail page.
	 */
	public String getRLIRoadmapStatus() {
		return getText(RLIRoadmapStatus);
	}
	
	/**
	 * Get RLI Probability located on RLI detail page.
	 */
	public String getRLIProbability() {
		return getText(RLIProbability);
	}
	
	/**
	 * Open edit opportunity from opportunity detail page.
	 * 
	 * @return CreateContactPage
	 */
	public CreateRLIPage editRLI() {
		click(editButton);
		return new CreateRLIPage();
	}
	
}
