package com.ibm.salesconnect.model.standard;

import org.openqa.selenium.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

public class IndustryPage extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(IndustryPage.class);

	public IndustryPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Industry page has not loaded within 60 seconds");	
	}

	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}
	
	//Selectors
	public static String pageLoaded = "//iframe[@id='ispIndustryFrame']";
	public static String industryFrame = "//iframe[@id='ispIndustryFrame']";
	public static String industryTextBox = "//input[@id='isp_bijits_common_ui_Selector_0-select']";
	public static String goButton = "//input[@value='GO']";
	public static String clientRoleRadioButton = "//input[@id='roleRadioBtn_1838']";
	public static String selectAll = "//input[@id='Select all_button_6883']";
	public static String viewResults = "//*[@id='isp_bijits_common_ui_ActionButton_5']";
	public static String resultsLoaded = "//a[contains(text(),'Expand all')]";
	
	//Tasks

	
	/**
	 * Populate Industry Details
	 */
	public void populateIndustryDetails(String industryType){
		isPresent(industryFrame);
		scrollElementToMiddleOfBrowser(industryFrame);
		switchToFrame(industryFrame);
		isPresent(industryTextBox);
		scrollElementToMiddleOfBrowser(industryTextBox);
		click(industryTextBox);
		isPresent(industryTextBox);
		type(industryTextBox,industryType);
		sendKeys(industryTextBox, Keys.RETURN);
		isPresent(goButton);
		scrollElementToMiddleOfBrowser(goButton);
		click(goButton);
		waitForPageToLoad(clientRoleRadioButton);
		click(clientRoleRadioButton);
		isPresent(selectAll);
		scrollElementToMiddleOfBrowser(selectAll);
		isPresent(selectAll);
		click(selectAll);
		scrollToBottomOfPage();
		isPresent(viewResults);
		click(viewResults);
		if(!isTextPresent("Error encountered")){
			waitForPageToLoad(resultsLoaded);
		}
		else{
			log.info("Industry Details Did NOT Load Correctly");
		}
		
	}
	
	/**
	 * Verifies Selected Industry Details Loads correctly
	 */
	public boolean verifyIndustryDetails(){
		if(isTextPresent("Solution Description")){
			log.info("Industry Details Loaded Correctly");
			switchToMainWindow();
			return true;
		}
		else{
			Assert.assertTrue(false,"Industry Details Did NOT Load Correctly");
		}
		return false;
	}

}
	