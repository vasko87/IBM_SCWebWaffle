package com.ibm.appium.model.partials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.Objects.Contact;
import com.ibm.appium.Objects.Opportunity;
import com.ibm.appium.model.MobilePageFrame;

public class ClientSelectPage extends MobilePageFrame {
	Logger log = LoggerFactory.getLogger(ClientSelectPage.class);

	// XPath Selectors
	public static String pageLoaded = "//h1[contains(text(),'Select Client')]";
	public static String searchField = "//div[@class='box__search-query']//input[@class='search-query']";
	public static String firstClient = "//div[@id='listing-Accounts']//..//div[@class='items']/article[1]";
	
	public static String filterButton = "//div[@class='layout__list layout__Accounts layout__def']//span[@class='filter__item filter__item-main']//a";
	public static String filterMyClientsInMyCountry = "//a[@data-filter-id='my_clients_in_my_country']";
	public static String filterMyClients = "//a[@data-filter-id='assigned_to_me']";
	public static String filterMyRestrictedClients = "//a[@data-filter-id='my_restricted_clients']";
	public static String filterFavorites = "//a[@data-filter-id='favorite']";
	
	public ClientSelectPage() {
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
	 * Get specific client record from Client list view.
	 * 
	 * @param sClientName
	 *            - Client name
	 */
	public String getResult(String sClientName) {
		return "//div[contains(text(),'" + sClientName + "')]";
	}

	/**
	 * Search for specific client using search input box located on client list
	 * view.
	 */
	public void searchForClient(Opportunity oppty) {
		type(searchField, oppty.getsClient());
		waitForElementVisible(getResult(oppty.getsClient()), 30);
		return;
	}

	/**
	 * Select first record after searching for Client and navigate back to main
	 * Opportunity create form.
	 */
	public void selectFirstClient(Opportunity oppty) {
		click(firstClient);
		return;
	}

	/**
	 * Select first record after searching for Client and navigate back to main
	 * Contact create form.
	 */
	public void selectContactClient(Contact contact) {
		click(firstClient);
		return;
	}
	
	public void filterItems(String filterOption){
		click(filterButton);
		click(filterOption);
	}
}
