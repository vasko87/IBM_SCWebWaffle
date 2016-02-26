package com.ibm.appium.model.partials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.Objects.LineItem;
import com.ibm.appium.model.MobilePageFrame;

public class L40_MachineTypeSelectPage extends MobilePageFrame {
	Logger log = LoggerFactory.getLogger(L40_MachineTypeSelectPage.class);

	// XPath Selectors
	public static String pageLoaded = "//div[@class='items']//article";
	public static String searchField = "input.search-query";
	public static String searchIcon = ".searchBtn";
	public static String firstProduct = "//div[@class='items']/article[1]";
	
	public L40_MachineTypeSelectPage() {
		Assert.assertTrue(isPageLoaded(), "L40 Machine Type Select page has not loaded");
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
	 * Get specific product record from Product list view.
	 * 
	 * @param sClientName
	 *            - Client name
	 */
	public String getResult(String Product) {
		return "//span[contains(text(),'" + Product + "')]";
	}

	/**
	 * Search for specific product using search input box located on client list
	 * view.
	 */
	public void searchForProduct(LineItem lineItem) {
		clickByJS(searchIcon);
		typeByCSS(searchField, lineItem.getsL40_MachineType());
		waitForElementVisible(getResult(lineItem.getsL40_MachineType()), 30);
	}

	/**
	 * Select first record after searching for RLI and navigate back to main
	 * RLI create form.
	 */
	public void selectFirstProduct() {
		click(firstProduct);
		return;
	}

	/**
	 * Select record after searching for product and navigate back to main
	 * RLI create form.
	 */
	public void selectProduct(LineItem lineItem) {
		click(getResult(lineItem.getsL40_MachineType()));
	}
}
