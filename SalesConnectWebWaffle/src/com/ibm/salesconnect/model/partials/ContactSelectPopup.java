/**
 * 
 */
package com.ibm.salesconnect.model.partials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

/**
 * @author Administrator
 *
 */
public class ContactSelectPopup extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(ContactSelectPopup.class);
	/**
	 * @param exec
	 */
	public ContactSelectPopup(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Client Select Popup has not loaded within 60 seconds");	
	}

	/* (non-Javadoc)
	 * @see com.ibm.salesconnect.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForElement(pageLoaded);
	}
	
	//Selectors
	public static String pageLoaded = "//input[@id='search_form_submit']";
	
	public String getSearchField(String searchType){ return  "//input[@id='search_name_" + searchType + "']";}
	public static String countrySelectButton = "//button[@id='primary_address_country_basic-image' or @id='primary_address_country_advanced-image']";
	private String selectCountryDropDown(String country) {return "//li[@data-text='"+ country+"']";}
	public static String searchButton = "//input[@id='search_form_submit']";
	public String getContactSelection(String clientName){ return "//table[contains(@class,'list')]//a[contains(text(),'" + clientName + "')]";}

	
	//Methods
	/**
	 * Searches for a contact
	 * @param contact name
	 */
	public void searchForContact(String searchTerm){
		String searchType="advanced";

		type(getSearchField(searchType),searchTerm);

		//select all countries
		click(countrySelectButton);
		click(selectCountryDropDown("All countries"));
		
		
		click(searchButton);
	}
	
	/**
	 * Selects the specified contact from the results list
	 * @param Contact Name
	 */
	public void selectResult(String contactName) {
		click(getContactSelection(contactName));
		switchToMainWindow();
	}

}
