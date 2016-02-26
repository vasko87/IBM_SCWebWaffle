/**
 * 
 */
package com.ibm.salesconnect.model.standard.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.model.partials.ConnectionsBusinessCard;

/**
 * @author adrianstrock
 * @date Apr 24, 2013
 */
public class SearchEmployeesPage extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(SearchEmployeesPage.class);
	/**
	 * @param exec
	 */
	public SearchEmployeesPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Client detail page has not loaded within 60 seconds");
	}

	/* (non-Javadoc)
	 * @see com.ibm.salesconnect.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}

	
	//Selectors
	public static String pageLoaded = "//input[@id='search_form_clear']";
	public static String searchFrame = "//iframe[@id='bwc-frame']";
	
	public static String unDropDown = "//div[@id='globalLinksModule']";
	
	public static String Employees = "css=a:contains('Employees')";
	
	/**	 * Get the Search button in the Advance Search Form	 */
	public static String getButton_SearchAdvance = "//div[@id='Employeesadvanced_searchSearchForm']/descendant::input[@id='search_form_submit']";
	
	/**	 * Get the clear button in the Advance Search Form	 */
	public static String getButton_ClearAdvance = "//div[@id='Employeesadvanced_searchSearchForm']/descendant::input[@id='search_form_clear']";
	
	public static String clearSearchForm = "//input[@id='search_form_clear']";
	public static String searchButton = "//*[@id='search_form_submit']";
	
	public static String getTextField_EmployeeName = "//input[@id='search_name_basic']";
	
	public String getLink_SearchResultEmployee(String sEmployeeName){return ("//a[contains(text(),'"+sEmployeeName+"')][@class='fn url hasHover']"); }
	
	public String getLink_EmployeeNameOverviewPage(String sEmployeeName){return ("//a[contains(text(),'"+sEmployeeName+"')]"); }
	
	public String getLinkBusinessCardLC(String sLCComponent){return ("//a[contains(text(),'"+sLCComponent+"')][@class='url']"); }
		
	/**
	 * Get the Business card link
	 * @return WebLink
	 */
	public static String getLink_BusinessCard = "//span[@id='semtagmenu']/a";
	
	/**
	 * Get the business card email address link
	 * The connections email address is cases sensitive so retrieve it without using email address
	 * @param sEmailAddress
	 * @return WebLink
	 */
	public static String getLink_BusinessCardEmailAddress = "//tr[@id='cardBody' and descendant::a[contains(@href,'mailto')]]";
	
	public static String[] aLCComponents = {"Profile", "Wikis", "Communities", "Files", "Blogs", "Bookmarks", "Forums", "Activities"};
	public String getBusinessCard(String userName) { return "//a[contains(text(),'" + userName + "')]";};
	
	//Tasks
	public void searchForEmployee(String sEmployeeName) {

		click(clearSearchForm);
		
		type(getTextField_EmployeeName, sEmployeeName);
		
		click(searchButton);
	}
	
	public void selectEmployeeFromListView(String sEmployeeName) {
		click(getLink_SearchResultEmployee(sEmployeeName));
	}

	public void verifyBusinessCard(User user){
		ConnectionsBusinessCard connectionsBusinessCard = openConnectionsBusinessCard(getBusinessCard(user.getFirstName()), exec);
		connectionsBusinessCard.verifyBusinessCardContents(user);
		switchToMainWindow();
	}
	
	public void verifyEmployeeBusinessCard(String sEmployeeName) {
		selectEmployeeFromListView(sEmployeeName);
		sEmployeeName = sEmployeeName.trim();
		mouseHover(getLink_EmployeeNameOverviewPage(sEmployeeName));
		click(getLink_BusinessCard);
		
		Assert.assertTrue(isPresent(getLink_BusinessCardEmailAddress));
		
		for(int i=0; i < aLCComponents.length; i++) {
			Assert.assertTrue(isPresent(getLinkBusinessCardLC(aLCComponents[i])));
		}
	}
}
