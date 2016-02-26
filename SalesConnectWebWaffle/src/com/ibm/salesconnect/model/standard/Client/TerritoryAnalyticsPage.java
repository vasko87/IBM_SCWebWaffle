package com.ibm.salesconnect.model.standard.Client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

public class TerritoryAnalyticsPage extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(TerritoryAnalyticsPage.class);

	public TerritoryAnalyticsPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Territory Analytics page has not loaded within 60 seconds");	
	}

	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}
	
	//Selectors
	public static String pageLoaded = "//iframe[@id='frame_TerritoryOverview']";
	public static String territoryOverviewFrame = "//iframe[@id='frame_TerritoryOverview']";
	public static String upSellAnalyticsTab = "//*[contains(text(),'Cross sell/Up sell analytics')]";
	
	//Tasks

	
	/**
	 * Opens Up Sell Analytics Tab
	 */
	public void openUpSellAnalyticsTab(){
		scrollElementToMiddleOfBrowser(territoryOverviewFrame);
		switchToFrame(territoryOverviewFrame);
		click(upSellAnalyticsTab);
	}
	
	/**
	 * Verifies Up Sell Analytics Tab Loads correctly
	 */
	public boolean verifyUpSellAnalyticsTab(){
		if(isTextPresent("An unexpected error occurred while checking your Territory reports")){
			log.info("An unexpected error occurred while checking your Territory reports, Up Sell Analytics Tab has NOT Loaded Correctly");
			return false;
		}
		if(!isTextPresent("Warranties or Licenses expiring within")){
			log.info("Up Sell Analytics Tab Loaded Correctly");
			return true;
		}
		else{
			Assert.assertTrue(false,"Up Sell Analytics Tab Did NOT Loaded Correctly");
		}
		return false;
	}

}
