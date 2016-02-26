package com.ibm.appium.model.partials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.Objects.LineItem;
import com.ibm.appium.model.MobilePageFrame;

public class L15_SubBrandSelectPage extends MobilePageFrame {
	Logger log = LoggerFactory.getLogger(L15_SubBrandSelectPage.class);

	// XPath Selectors
	public static String pageLoaded = "//div[@class='items']//article";
	public static String searchField = "input.search-query";
	public static String searchIcon = ".searchBtn";
	public static String firstProduct = "//div[@class='items']/article[1]";
	
	public L15_SubBrandSelectPage() {
		Assert.assertTrue(isPageLoaded(), "L15 Sub-Brand Select page has not loaded");
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
	 * @param ProductName
	 *            - Product name
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
		typeByCSS(searchField, lineItem.getsL15_SubBrand());
		waitForElementVisible(getResult(lineItem.getsL15_SubBrand()), 30);
	}

	/**
	 * Select first record after searching for Product and navigate back to main
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
		click(getResult(lineItem.getsL15_SubBrand()));
	}

}
