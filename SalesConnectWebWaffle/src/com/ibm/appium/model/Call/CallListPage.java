package com.ibm.appium.model.Call;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.Objects.Call;
import com.ibm.appium.model.MobilePageFrame;
import com.ibm.appium.model.Call.CallListPage;

public class CallListPage extends MobilePageFrame {
	Logger log = LoggerFactory.getLogger(CallListPage.class);

	// XPath Selectors
	public static String pageLoaded = "//div[@id='listing-Calls']";
	public static String searchField = "//input[@class='search-query']";
	public static String searchButton = "(//a[@track='click:search'])[1]";
	public static String searchClear = "//i[@class='icon clear-button icon-remove-sign']";
	
	public CallListPage() {
		Assert.assertTrue(isPageLoaded(), "Call List Page has not loaded");
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
	 * Get specific Call record from Call list view.
	 * 
	 * @param sSubject
	 *            - Call subject
	 */
	public String searchedListItem(String itemName) {
		return "//div[contains(text(), '" + itemName + "')]";
	}

	/**
	 * Search for specific call using search input box located on call list
	 * view.
	 */
	public CallDetailPage searchForCall(Call call) {
		
		String searchQuery = call.getsSubject();
		
		log.info("Searching for item" + searchQuery);
		click(searchButton);
		click(searchField);
		log.info("Clearing search field if needed");
		clear();
		log.info("Typing search phrase");
		type(searchField, searchQuery);
		log.debug("Typed in. Waiting for element: " + searchedListItem(searchQuery));
		waitForElementPresent(searchedListItem(searchQuery), 30);
		log.debug("Done waiting. Is it present?");
		
		if(isPresent(searchedListItem(searchQuery))){
			log.debug("Element found. Clicking.");
			click(searchedListItem(searchQuery));
		}
		else {
			log.debug("Element not found. Trying search again.");
			click(searchField);
			clear();
			log.info("Typing search phrase again");
			type(searchField, searchQuery);				
			log.debug("Typed in. Waiting for element.");
			waitForElementPresent(searchedListItem(searchQuery), 15);
			
			log.debug("Done waiting. Is it present?");
			if(isPresent(searchedListItem(searchQuery))){
				log.debug("Element found. Clicking.");
				click(searchedListItem(searchQuery));
			}
			else {
				log.debug("Element not found. Trying search last time.");
				click(searchField);
				clear();
				log.info("Typing search phrase again");
				type(searchField, searchQuery);				
				waitForElementPresent(searchedListItem(searchQuery), 15);
				click(searchedListItem(searchQuery));
			}
		}
		return new CallDetailPage();
	}

	/**
	 * Select result after searching for call record and navigate to Call detail
	 * page.
	 * 
	 * @return CallDetailPage
	 */
	public CallDetailPage selectResult(Call call) {
		click(searchedListItem(call.getsSubject()));
		return new CallDetailPage();
	}

}
