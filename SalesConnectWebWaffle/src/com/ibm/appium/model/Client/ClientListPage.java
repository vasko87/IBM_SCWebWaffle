package com.ibm.appium.model.Client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.Objects.Client;
import com.ibm.appium.model.MobilePageFrame;

public class ClientListPage extends MobilePageFrame {

	Logger log = LoggerFactory.getLogger(ClientListPage.class);

	// XPath Selectors
	public static String pageLoaded = "//div[@id='listing-Accounts']";
	public static String searchIconCSS = ".searchBtn";
	public static String searchField = "//input[@class='search-query']";
	public static String searchClear = "i.clear-button";
	
	public static String filterButton = "//span[@class='filter__item filter__item-main']//a";
	public static String filterMyClientsInMyCountry = "//a[@data-filter-id='my_clients_in_my_country']";
	public static String filterMyClients = "//a[@data-filter-id='assigned_to_me']";
	public static String filterMyRestrictedClients = "//a[@data-filter-id='my_restricted_clients']";
	public static String filterFavorites = "//a[@data-filter-id='my_clients_in_my_country']";

	
	public ClientListPage(){
		Assert.assertTrue(isPageLoaded(), "Client List Page has not loaded");
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
	public String getResult(String sName) {
		return "//div[@id='listing-Accounts']//div[contains(text(),'" + sName
				+ "')]";
	}

	/**
	 * Search for specific contact using search input box located on contact
	 * list view.
	 */
	public void searchForClient(Client client) {
		log.info("Searching for item: " + client.getsCCMSID());
		clickByJS(searchIconCSS);
		click(searchField);
		log.info("Clearing search field if needed");
		clear();
		log.info("Typing search phrase");
		type(searchField, client.getsCCMSID());
	}

	/**
	 * Select record after searching for contact and navigate to Contact detail
	 * page.
	 * 
	 * @return ContactDetailPage
	 */
	public ClientDetailPage selectResult(Client client) {
		click(getResult(client.getsName()));
		return new ClientDetailPage();
	}
	
	public void filterItems(String filterOption){
		click(filterButton);
		click(filterOption);
	}
	
}
