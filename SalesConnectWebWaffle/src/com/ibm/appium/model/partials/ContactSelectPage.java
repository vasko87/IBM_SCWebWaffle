package com.ibm.appium.model.partials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.Objects.Opportunity;
import com.ibm.appium.model.MobilePageFrame;

public class ContactSelectPage extends MobilePageFrame {
	Logger log = LoggerFactory.getLogger(ContactSelectPage.class);

	// XPath Selectors
	public static String pageLoaded = "//h1[contains(text(),'Select Contact')]";
	public static String searchIcon = "//div[@class='layout__list layout__Contacts layout__def']//a[@class='searchBtn fast-click-highlighted']";
	public static String searchIconCSS = ".searchBtn";
	public static String searchField = "//div[@class='box__search-query']//input[@class='search-query']";
	public static String firstContact = "//div[@id='listing-Contacts']//div[@class='items']/article[1]";
	
	public static String filterButton = "//div[@class='layout__list layout__Contacts layout__def']//span[@class='filter__item filter__item-main']//a";
	
	public static String filterMyContactsInMyCountry = "//a[@data-filter-id='my_contacts_in_my_country']";
	public static String filterMyContacts = "//a[@data-filter-id='assigned_to_me']";
	public static String filterFavorites = "//a[@data-filter-id='favorites']";
	
	public ContactSelectPage() {
		Assert.assertTrue(isPageLoaded(), "Contact Select page has not loaded");
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
	 * Get specific contact record from Contact list view.
	 * 
	 * @param sContactName
	 *            - Contact name
	 */
	public String getResult(String sContactName) {
		return "//div[contains(text(),'" + sContactName + "')]";
	}

	/**
	 * Search for specific contact using search input box located on contact
	 * list view.
	 */
	public void searchForContact(Opportunity oppty) {
		type(searchField, oppty.getsContact());
		waitForElementVisible(getResult(oppty.getsContact()), 30);
		return;
	}

	/**
	 * Select first record after searching for Client and navigate back to main
	 * Opportunity create form.
	 */
	public void selectFirstContact(Opportunity oppty) {
		click(firstContact);
		return;
	}
	
	public void filterItems(String filterOption){
		click(filterButton);
		click(filterOption);
	}
}
