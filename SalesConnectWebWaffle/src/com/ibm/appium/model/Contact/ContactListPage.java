package com.ibm.appium.model.Contact;

import com.ibm.appium.Objects.Contact;
import com.ibm.appium.model.MobilePageFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class ContactListPage extends MobilePageFrame {
	
	Logger log = LoggerFactory.getLogger(ContactListPage.class);

	// XPath Selectors
	public static String pageLoaded = "//div[@id='listing-Contacts']";
	public static String searchField = "//input[@class='search-query']";
	public static String searchButton = "(//a[@track='click:search'])[1]";
	
	// Opportunity Sorting
	public static String sortIcon = "//i[@id='sort-btn']";
	public static String sortByJobTitle = "//div[@data-index='0']";
	public static String sortByClientName = "//div[@data-index='1']";
	public static String sortByCity = "//div[@data-index='2']";
	public static String sortByLastName = "//div[@data-index='3']";
	public static String sortByDateModified = "//div[@data-index='4']";
	public static String sortDecending = "//a[@id='desc']";
	public static String sortAscending = "//a[@id='asc']";
	public static String sortDone = "//a[@class='cancel fast-click-highlighted']";

	
	public ContactListPage() {
		Assert.assertTrue(isPageLoaded(), "Contact List Page has not loaded");
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
	 * Get specific Contact record from Contact list view.
	 * 
	 * @param sName
	 *            - Contact name
	 */
	public String searchedListItem(String itemName) {
		return "//div[contains(text(), '" + itemName + "')]";
	}
	
	/**
	 * Search for specific contact using search input box located on contact
	 * list view.
	 */
	public ContactDetailPage searchForContact(Contact contact) {
		
		String searchQuery = contact.getsFirstName();
		
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
		return new ContactDetailPage();
	}

	/**
	 * Select record after searching for contact and navigate to Contact detail
	 * page.
	 * 
	 * @return ContactDetailPage
	 */
	public ContactDetailPage selectResult(Contact contact) {
		
		String contactNameListItem = ""; 
		if(contact.getsPreferredName().length() > 0)
			contactNameListItem = searchedListItem(contact.getsFirstName() + " ("
					+ contact.getsPreferredName() + ") " + contact.getsLastName());	
		else 
			contactNameListItem = searchedListItem(contact.getsFirstName() + " " + contact.getsLastName());
		
		//TODO: Swipe down to refresh every 5 seconds for 60 sec		
		
		waitForElementPresent(contactNameListItem,10);
		click(contactNameListItem);
		return new ContactDetailPage();
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
	public void sortContact(String byOption, String byOrder) {
		log.info("Sorting list Decending by Date Modified");
		click(sortIcon);
		click(byOption);
		click(byOrder);
		click(sortDone);
	}
}
