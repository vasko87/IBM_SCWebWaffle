/**
 * 
 */
package com.ibm.salesconnect.model.standard.Contact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardViewPage;
import com.ibm.salesconnect.model.partials.EditContactSubpanel;
import com.ibm.salesconnect.objects.Contact;

/**
 * @author Administrator
 *
 */
public class ViewContactPage extends StandardViewPage {
	Logger log = LoggerFactory.getLogger(ViewContactPage.class);
	
	/**
	 * @param exec
	 */
	public ViewContactPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "View Contact page has not loaded within 60 seconds");	
	}

	/* (non-Javadoc)
	 * @see com.ibm.salesconnect.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		scrollToBottomOfPage();
		return waitForPageToLoad(pageLoaded);
	}
	
	
	//Selectors
	public static String pageLoaded = "//*";
	public static String searchFrame = "//iframe[@id='bwc-frame']";
	
	public static String getSearchFormType = "//form[@id='search_form']//input[@name='searchFormTab']";
	public static String clearSearchForm = "//form[@id='search_form']//input[@id='search_form_clear']";
	public static String changeToBasic = "//a[contains(text(),'Basic Search')]";
	public static String changeToAdvanced = "//a[contains(text(),'Advanced search')]";
		
	public static String getSearchField =  "//input[@id='search_name_basic' or @id='search_name_advanced']";
	public static String getAdvancedSearchField =  "//input[@id='basic_search_link" + "' or @id='advanced_search_link" + "']";
	public static String SearchEmailField = "//input[@id='email_basic']";
	public static String SearchContactTagField = "//input[@id='Contacts_tags_search']";
	public static String getCountrySelection = "//select[@id='billing_address_country_basic' or @id='billing_address_country_advanced']";
	public static String getCountrySelection2 = "//input[@id='primary_address_country_basic-input' or @id='primary_address_country_advanced-input']";
	public static String selectAllcountries = "//li[@role='option' and @data-text='All countries']";
	public static String allCountries_Dropdown = "//div[@aria-hidden='false']//li[@data-text='All countries']";
	public static String countrySelectButton = "//button[@id='primary_address_country_basic-image' or @id='primary_address_country_advanced-image']";
	public static String allCountriesDropdownFromButton = "//button[@id='primary_address_country_basic-image']/../div[contains(@class,'yui3-aclist yui3-widget-positioned yui3-widget-stacked')]/div/ul/li[@data-text='All countries']";
	public static String countryRegionTextBox = "//*[@id='primary_address_country_basic-input' or @id='primary_address_country_advanced-input']";
	private String selectCountryDropDown(String country) {return "//li[@data-text='"+ country+"']";}
	public String getSelection(String dropdown, String choice){return dropdown + "/..//li";};
	
	public static String getMyItems =  "//input[contains(@id,'current_user_only_')]";//Check on Check box My Contacts
	public static String getMyFavorites = "//input[@id='favorites_only_basic' or @id='favorites_only_advanced']";
	public static String searchButton = "//*[@id='search_form_submit']";
	public static String globalSearchMenuNo = "//input[@value='No']";
	public static String globalSearchMenuClose = "//a[@id='dcmenu_close_link')]";
	public static String globalSearchDontAskAgainCheck = "//input[@id='privacyCountriesGSDontShow')]";
	
	private String getContactSelection(String contactName) {return "//form[@id='MassUpdate']//a[contains(text(),'" + contactName + "')]";}
	
	private String getEditContactSubpanel(String contactName) {return "//form[@id='MassUpdate']//a[contains(text(),'" + contactName + "')]/../..//span[@class='sprite_edit_inline_png']";}
	private String privacyPopUp = "//div[@id='privacyCountriesPopupBody']";
	private String closePrivacyPopUp = "//a[@id='dcmenu_close_link']";
	private String noButtonPrivacyPopup = "//input[@title='No' and @class='button']"; 

	
	//Methods
	/**
	 * Gets the current search form status 
	 * returns basic/advanced
	 */
/*	public String getSearchtype(){
		String value = getObjectAttribute(getSearchFormType, "value");
		return value.substring(0,value.length()-7);
	}
*/	
	/**
	 * Ensures that the search page is set to basic
	 * 
	 */
	public void setSearchTypeToBasic(){
		if(isPresent(changeToBasic)){
			click(changeToBasic);
		}
	}
	
	/**
	 * Ensures that the search page is set to advanced
	 * 
	 */
	public void setSearchTypeToAdvanced(){
		if(isPresent(changeToAdvanced)){
			click(changeToAdvanced);
		}
	}
	/**
	 * Clears the contact search form
	 */
	public void clearSearchForm(){
		click(clearSearchForm);
		if(isChecked(getMyItems)){
			click(getMyItems);
		}
	}
	
	/**
	 * Enters the given search term into the serach text field
	 * @param searchTerm
	 */
	public void enterSearchTerm(String searchTerm){
		type(getSearchField,searchTerm);
	}
	
	/**
	 * Clears the search field
	 */
	public void clearSearchTerm(){
		clearField(getSearchField);
	}
	
	/**
	 * Clear the email field
	 */
	public void clearAnyEmail(){
		clearField(SearchEmailField);
	}
	
	/**
	 * Enters the given email into the Any Email field
	 * @param email
	 */
	public void enterAnyEmail(String email){
		type(SearchEmailField,email);
	}
	
	/**
	 * Selects the given country in the dropdown
	 * @param country
	 */
	public void selectCountry(String country){
		//type(getCountrySelection2,country);
		click(countrySelectButton);
		//sleep(1);
		click(selectCountryDropDown(country));
		//sleep(1);
		//clickWithScrolling(selectCountryDropDown(country));
	}
	
	/**
	 * Clicks the search button
	 */
	public void clickSearchButton(){
		click(searchButton);
	}
	
	/**
	 * Checks if Advanced Search Active
	 * @return true if presnt false if not
	 */
	public boolean isAdvancedSearch(){
		if (isVisible(changeToBasic)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Checks if Basic Search Active
	 * @return true if presnt false if not
	 */
	public boolean isBasicSearch(){
		if (isVisible(changeToAdvanced)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Checks if privacy pop up is present
	 * @return true if presnt false if not
	 */
	public boolean isPrivacyPopUpPresent(){
		if (isPresent(privacyPopUp)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Closes the Privacy Pop Up
	 */
	public void closePrivacyPopUp(){
		click(closePrivacyPopUp);
	}
	
	/**
	 * Closes the Privacy Pop Up
	 */
	public void noPrivacyPopUp(){
		//isPresent(privacyPopUp);
		if(isPresent(noButtonPrivacyPopup)){
			click(noButtonPrivacyPopup);
		}
	}

	/**
	 * Searches for a contact based on the Name
	 * @param contact Name
	 */
	public void searchForContactName(String contactName){
		click(clearSearchForm);
		waitForSubpanelToLoad(pageLoaded);
		log.debug("Click country select birdie");
		click(countrySelectButton);
		click(selectCountryDropDown("All countries"));
		
		type(getSearchField,contactName);
		click(searchButton);
		
		//close global search prompt if present
		if(isPresent(globalSearchMenuNo)) {
			click(globalSearchMenuNo);
		}
	}
	
	/**
	 * Searches for a contact based on the Name
	 * @param contact Name
	 */
	public void searchForFavoriteContactName(String contactName){
		click(clearSearchForm);
		waitForSubpanelToLoad(pageLoaded);
		log.debug("Click country select birdie");
		click(countrySelectButton);
		click(selectCountryDropDown("All countries"));
		if(!isChecked(getMyFavorites)){
			click(getMyFavorites);
		}

		//type(getSearchField,contactName);
		click(searchButton);
		
		//close global search prompt if present
		if(isPresent(globalSearchMenuNo)) {
			click(globalSearchMenuNo);
		}
	}
	
	/**
	 * Searches for a contact based on the parameters
	 * @param contact
	 */
	public void searchForContact(Contact contact){
		//scrollElementToMiddleOfBrowser(clearSearchForm);
		click(clearSearchForm);
		waitForSubpanelToLoad(pageLoaded);
		log.debug("Click country select birdie");
		click(countrySelectButton);
		click(selectCountryDropDown("All countries"));
		
		if(contact.sSearchName){
			type(getSearchField,contact.sFirstName + " " + contact.sLastName);
		}
		else if(contact.sSearchAnyEmail){
			type(SearchEmailField,contact.sEmail0);
		}
		else if(contact.sSearchContactTags){
			type(SearchContactTagField,contact.sTags);
		}
		else type(getSearchField,contact.sFirstName + " " + contact.sLastName);
		
		if(contact.bMyItems){
			if(!isChecked(getMyItems)){
				click(getMyItems);
			}
		}
		else {
			if(isChecked(getMyItems)){
				click(getMyItems);
			}
		}
		if(contact.bMyFavorites){ //contact is a favorite
			if(!isChecked(getMyFavorites)){
				click(getMyFavorites);
			}
		}
		else { //contact is not a favorite
			if(isChecked(getMyFavorites)){
				click(getMyFavorites);
			}
		}
		
		click(searchButton);
		
		//close global search prompt if present
		if(isPresent(globalSearchMenuNo)) {
			click(globalSearchMenuNo);
		}
		
//		switchToFrame("//iframe[@id='bwc-frame']");
	}

	/**
	 * Click on the correct search result
	 * @param contact
	 * @return new contact detail page object
	 */
	public ContactDetailPage selectResult(Contact contact) {
		sleep(10);
		click(clearSearchForm);
		if (isPresent(getContactSelection(contact.sFirstName)) && (isPresent(getContactSelection(contact.sLastName)))){
			click(getContactSelection(contact.sFirstName));
		}
		else{
			Assert.assertTrue(false, "Cannot find contact name in results: " + contact.sFirstName + " " + contact.sLastName);
		}
		//click(getContactSelection(contact.sFirstName + " " + contact.sLastName));
		return new ContactDetailPage(exec);
	}

	/**
	* Click on the correct search text result
	 * @param contact
	 * @return new contact detail page object
	 */
	public ContactDetailPage selectTextResult(String sTextResult) {
		click(clearSearchForm);
		click(getContactSelection(sTextResult));
		return new ContactDetailPage(exec);
	}
	
	/**
	 * Check that correct result is present
	 * @param contact
	 * @return true if present false if not
	 */
	public boolean checkResult(Contact contact) {
//		click(clearSearchForm);
		isPageLoaded();
		if(!contact.sPreferredName.equals("")){
			return isPresent(getContactSelection(contact.sFirstName + " (" + contact.sPreferredName + ") " + contact.sLastName));
		}
		else return isPresent(getContactSelection(contact.sFirstName));
	}
	
	/**
	 * Check that correct text result is present
	 * @param contact
	 * @return true if present false if not
	 */
	public boolean checkTextResult(String sTextResult) {
//		click(clearSearchForm);
		return isPresent(getContactSelection(sTextResult));
	}
	
	public EditContactSubpanel selectEditContactSubpanel(Contact contact){
		scrollElementToMiddleOfBrowser(getEditContactSubpanel(contact.sFirstName));
		click(getEditContactSubpanel(contact.sFirstName));
		
		return new EditContactSubpanel(exec);
		
	}
}
