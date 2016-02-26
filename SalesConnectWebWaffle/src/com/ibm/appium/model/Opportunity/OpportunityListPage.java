package com.ibm.appium.model.Opportunity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.Objects.Opportunity;
import com.ibm.appium.model.MobilePageFrame;

public class OpportunityListPage extends MobilePageFrame {
	
	Logger log = LoggerFactory.getLogger(OpportunityListPage.class);

	// XPath Selectors
	public static String pageLoaded = "//div[@id='listing-Opportunities']";
	public static String searchField = "//input[@class='search-query']";
	public static String searchButton = "(//a[@track='click:search'])[1]";

	// Opportunity Sorting
	public static String sortIcon = "//i[@id='sort-btn']";
	public static String sortByOpptyNo = "//div[@data-index='0']";
	public static String sortByDescription = "//div[@data-index='1']";
	public static String sortByClientName = "//div[@data-index='2']";
	public static String sortByTotalAmmount = "//div[@data-index='3']";
	public static String sortBySalesStage = "//div[@data-index='4']";
	public static String sortByDecisionDate = "//div[@data-index='5']";
	public static String sortByDateCreated = "//div[@data-index='6']";
	public static String sortByDateModified = "//div[@data-index='7']";
	public static String sortDecending = "//a[@id='desc']";
	public static String sortAscending = "//a[@id='asc']";
	public static String sortDone = "//a[@class='cancel fast-click-highlighted']";
	
	public static String firstOppty = "//div[@class='items']//article[1]";
	
	public OpportunityListPage() {
		Assert.assertTrue(isPageLoaded(),
				"Opportunity List Page has not loaded");
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
	public String searchedListItem(String sOpptyId) {
		return "//div[contains(text(),'" + sOpptyId + "')]";
	}

	/**
	 * Search for specific opportunity using search input box located on
	 * opportunity list view.
	 */
	public OpportunityDetailPage searchForOppty(Opportunity oppty) {
		
		String searchQuery = oppty.getsId();
		
		log.info("Searching for item" + searchQuery);
		click(searchButton);
		click(searchField);
		log.info("Clearing search field if needed");
		clear();
		log.info("Typing search phrase");
		type(searchField, "\"" + searchQuery + "\"");
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
			type(searchField, "\"" + searchQuery + "\"");				
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
				type(searchField, "\"" + searchQuery + "\"");				
				waitForElementPresent(searchedListItem(searchQuery), 15);
				click(searchedListItem(searchQuery));
			}
		}
		return new OpportunityDetailPage();
		
	}

	/**
	 * Select first record in the opportunity list and navigate to Opportunity
	 * detail page.
	 * 
	 * @return OpportunityDetailPage
	 */	
	public OpportunityDetailPage selectFirstOppty(){
		click(firstOppty);
		return new OpportunityDetailPage();
	}
	

	/**
	 * Change sorting using Sort menu located on Opportunity list view.
	 * 
	 * @param byOption
	 *            - select one of the available values such as by Description or
	 *            Sales Stage
	 * @param byOrder
	 *            - select Ascending or Descending order
	 */
	public void sortOppty(String byOption, String byOrder) {
		log.info("Sorting list Decending by Opportunity Number");
		click(sortIcon);
		click(byOption);
		click(byOrder);
		click(sortDone);
	}
}
