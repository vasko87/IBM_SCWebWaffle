/**
 * 
 */
package com.ibm.salesconnect.model.standard.Client;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Element;
import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.objects.Client;

/**
 * @author timlehane
 * @date Apr 23, 2013
 */
public class ViewClientPage extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(ViewClientPage.class);
	/**
	 * @param exec
	 */
	public ViewClientPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "View Client page has not loaded within 60 seconds");	
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
	public static String pageLoaded = "//form[@id='search_form']"; // "//*[@id='search_form_submit']";
	public static String searchFrame = "//iframe[@id='bwc-frame']";
	
	public static String changeToBasic = "//a[@id='basic_search_link']";
	public static String changeToAdvanced = "//a[@id='advanced_search_link']";
	
	public static String getSearchFormType = "//form[@id='search_form']//input[@name='searchFormTab']";

	public static String basicSearchLink = "//a[@id='basic_search_link']";
	public String getSearchField(String searchType){ return  "//input[@id='name_" + searchType + "']";}
	public String getSearchIn(String searchType){ return  "//select[@id='field_name_" + searchType + "']";}
	public String selectSearchInDropDown(String searchIn) {return "//option[@label='"+ searchIn+"']";}
	
	public String getGlobalId(){ return "//*[@id='global_client_ccms_id_advanced']";}
	
	public String getSearchFor(String searchType){ return  "//select[@id='billing_address_country_" + searchType + "']";}
	public String getSearchFor(){ return  "//select[@id='billing_address_country_basic' or 'billing_address_country_advance']";}
	public String countryMenuButton(String searchType){return "//select[@id='billing_address_country_" + searchType + "']";}
	private String selectCountryDropDown(String country) {return "//option[@label='"+ country+"']";}
	
	public String getSearchShowing(String searchType){ return  "//select[@name='accounts_showing_" + searchType + "']";}
	public String getSearchShowing(){ return  "//select[contains(@name='accounts_showing_')";}
	private String getSelectShowingOption(String clientType) {return "//option[@value='"+ clientType +"']";}
	public String getShowingMenuButton(String searchType){ return "//select[@name='accounts_showing_" + searchType + "']";}
	
	public String getMyClients(String searchType){ return  "//input[@id='current_user_only_" + searchType + "']";}
	public String getFavoriteClients(String searchType){return "//input[@id='favorites_only_" + searchType + "']";}
	
	public static String searchButtonBasic = "//div[@id='Accountsbasic_searchSearchForm']/descendant::input[@id='search_form_submit']";
	public static String searchButtonAdvanced = "//div[@id='Accountsadvanced_searchSearchForm']/descendant::input[@id='search_form_submit']";
	
	public static String myClientsCheckBox = "//input[@id='current_user_only_advanced']";
	public static String myFavoritesClientsCheckBox = "//input[@id='favorites_only_advanced']";
	public static String myRestrictedClientsCheckBox = "//input[@id='filter_restricted_clients_advanced']";
	
	public static String clearSearch ="//input[@id='search_form_clear']";
	
	public String getClientSelection(String clientID, String clientName){ return "//span[text() = '" 
		+ clientID + "']/../..//a[contains(text(),'" + clientName + "')]";}
	public String getClientSelection(String clientName){ return "//a[contains(text(),'" + clientName + "')]";}
	public String getClientNameLinkRowSelection(int clientListRowIndex) {int nonClientRows = 2; return "//tr[@class='pagination']/../tr[" + Integer.toString(nonClientRows+clientListRowIndex) + "]/descendant::span[contains(@id,'CLIENT_ICON_')]/../a";}
	public String getClientIdRowSelection(int clientListRowIndex) {int nonClientRows = 2; return "//tr[@class='pagination']/../tr[" + Integer.toString(nonClientRows+clientListRowIndex) + "]/descendant::span[contains(@class,'LEVEL_DC_ID_PLAIN')]";}
	public static String noDataRow = "//em";
	
	public static String getAllFavoriteButtons = "//div[@class='star']//div[@class='off']";
	public static String getAllUnfavoriteButtons = "//div[@class='star']//div[@class='on']";
	public static String getAllClientNames = "//td[@scope='row']//a";
	
	// Reserved text options for client Type
	public static String allClients = "all_accounts";
	public static String clientsOnly = "clients_only";
	public static String sitesOnly = "sites_only";
	
	//Methods
	/**
	 * Checks if Advanced Search Active
	 * @return true if present false if not
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
	 * @return true if present false if not
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
	 * Switch to Basic Search 
	 */
	public void switchToBasicSearch(){
		if (isVisible(changeToBasic)) {
			click(changeToBasic);
		}
	}
	
	/**
	 * Switch to Advance Search 
	 */
	public void switchToAdvancedSearch(){
		if (isVisible(changeToAdvanced)) {
			click(changeToAdvanced);
		}
	}
	
	/**
	 * tick/untick check-box 
	 */
	public void tickCheckBox(String selector, boolean enable){
		if (enable) { // tick check-box
			if (!isChecked(selector)) {
				click(selector);
			}
		} else { // untick check-box
			if (isChecked(selector)) {
				click(selector);
			} 
		}
	}

	/**
	 * Clears the Client search form
	 */
	public void clearSearchForm(String searchType){
		click(clearSearch);
		if(isChecked(getMyClients(searchType))){
			click(getMyClients(searchType));
		}
	}
	
	
	/**
	 * Gets the current search form status 
	 * returns basic/advanced
	 */
	public String getSearchtype(){
		String value = getObjectAttribute(getSearchFormType, "value");
		return value.substring(0,value.length()-7);
	}
	
	/**
	 * Searches for a client based on the parameters
	 * @param client
	 */
	public void searchForClient(Client client){	
		String searchType=getSearchtype();
		scrollElementToMiddleOfBrowser(clearSearch);
		click(clearSearch);
		String searchTerm="";
		if(client.sSearchIn.equals(GC.showingInClientName)){
			searchTerm=client.sClientName;
		}
		else if(client.sSearchIn.equals(GC.showingInClientID)){
			searchTerm=client.sClientID;
		}
		else if(client.sSearchIn.equals(GC.showingInSiteID)){
			searchTerm=client.sSiteID;
		}
		type(getSearchField(searchType),searchTerm);
		select(getSearchIn(searchType),client.sSearchIn);
		select(getSearchFor(searchType),client.sSearchFor);
		select(getSearchShowing(searchType),client.sSearchShowing);
		
		if(client.myClients){
			if(!isChecked(getMyClients(searchType))){
				click(getMyClients(searchType));
			}
		}
		else {
			if(isChecked(getMyClients(searchType))){
				click(getMyClients(searchType));
			}
		}
		
		if(client.bMyFavorites){
			if(!isChecked(getFavoriteClients(searchType))){
				click(getFavoriteClients(searchType));
			}
		}
		else{
			if(isChecked(getFavoriteClients(searchType))){
				click(getFavoriteClients(searchType));
			}
		}
		
		if (isBasicSearch()) {
		    scrollElementToMiddleOfBrowser(searchButtonBasic);
		    click(searchButtonBasic);
		} else {
		    scrollElementToMiddleOfBrowser(searchButtonAdvanced);
		    click(searchButtonAdvanced);
		}
	}

	/**
	 * Checks if any results are found
	 */
	public boolean resultsFound(){
		// check if 1st row of results is empty with no data		
		if (isPresent(this.noDataRow)) {
			return false;
		}
		return true;
	}
	
	public void searchForClient(String clientName, boolean myClient, boolean myFavorite){
		String searchType=getSearchtype();
		scrollElementToMiddleOfBrowser(clearSearch);
		click(clearSearch);
		type(getSearchField(searchType),clientName);
		select(getSearchIn(searchType),GC.showingInClientID);
		select(getSearchFor(searchType),GC.searchForAll);
		select(getSearchShowing(searchType),GC.showingForAll);
		
		if(myClient){
			if(!isChecked(getMyClients(searchType))){
				click(getMyClients(searchType));
			}
		}
		else {
			if(isChecked(getMyClients(searchType))){
				click(getMyClients(searchType));
			}
		}
		
		if(myFavorite){
			if(!isChecked(getFavoriteClients(searchType))){
				click(getFavoriteClients(searchType));
			}
		}
		else{
			if(isChecked(getFavoriteClients(searchType))){
				click(getFavoriteClients(searchType));
			}
		}
		
		if (isBasicSearch()) {
		    scrollElementToMiddleOfBrowser(searchButtonBasic);
		    click(searchButtonBasic);
		} else {
		    scrollElementToMiddleOfBrowser(searchButtonAdvanced);
		    click(searchButtonAdvanced);
		}		
	}
	
	/**
	 * selects and opens client in row X of Client List View page results
	 * @param client
	 */
	public void selectFromClientListResults(int rowIndex){
		//int nonClientRows = 2;
		// 3 default rows before 1st row with a client
		//tr[@class='pagination']/../../tr[default + rowIndox]
		//tr[@class='pagination']/../tr[4]/descendant::span[contains(@id,'CLIENT_ICON_')]/../a
		
		log.info("Client list view, 1st row XPath = " + getClientNameLinkRowSelection(rowIndex));
		click(getClientNameLinkRowSelection(rowIndex));
	}
	
	/**
	 * Selects the client 'in filter in the dropdown
	 * @param clientInFilter
	 * @param searchType
	 */
	public void selectSearchIn(String searchIn, String searchType){
		//type(getSearchFor(searchType), country);
		click(getSearchIn(searchType));
		//sleep(1);
		
//		if (isPresent(selectSearchInDropDown(searchIn))) {
//			click(selectSearchInDropDown(searchIn));
//		}
		sleep(1);
		clickWithScrolling(selectSearchInDropDown(searchIn));
	}
	
	/**
	 * Selects the given country in the dropdown
	 * @param country
	 * @param searchType
	 */
	public void selectCountry(String country, String searchType){
		//type(getSearchFor(searchType), country);
		click(countryMenuButton(searchType));
		//sleep(1);
		click(selectCountryDropDown(country));
		//sleep(1);
		//clickWithScrolling(selectCountryDropDown(country));
	}
	
	/**
	 * Adds text into Global ID field.
	 * 
	 * @param globalId - string containing Gobal ID (aka GU Id)
	 */
	public void addGlobalId(String globalId){
		if (isAdvancedSearch()) {
			type(getGlobalId(), globalId);
		}
	}
	
	/**
	 * Clicks the search button
	 */
	public void clickSearchButton(){
		if (isAdvancedSearch()) {
			click(searchButtonAdvanced);
		} else {
			click(searchButtonBasic);
		}
		//isPageLoaded();
	}

	/**
	 * Selects the given client type in the dropdown
	 * @param client type
	 */
	public void selectClientType(String clientType, String searchType){
		//type(getSearchFor(),clientType);
		click(getShowingMenuButton(searchType));
		//sleep(1);
		//click(getSelectShowingOption(clientType));
		sleep(1);
		clickWithScrolling(getSelectShowingOption(clientType));
	}
	
	/**
	 * Generates a Client obj from a client displayed in the Client List View results
	 * @param rowOfClient (1 to etc) is the row number of the client we want to open
	 * @return Client object containing the client name & client ID
	 */
	public Client createClientObjFromClientListView(int rowOfClient) {
		Client client = new Client();
		List<String> clientList = null;
		if (this.resultsFound()) {
			clientList = this.getAllVisibleClients();
		}
		//ClientDetailPage clientDetailPage = null;
		if (clientList.size() == 0) {
			log.info("0 client results found");
			Assert.assertTrue(false, "0 client results found in Client list view");
		} else if (rowOfClient > 0) { // client found
			client.sClientName = clientList.get(rowOfClient-1); // 
			client.sClientID = this.getObjectText(this.getClientIdRowSelection(rowOfClient)); 
			//log.info("Navigate to client in 1st row");
			//viewClientPage.selectFromClientListResults(1); 
			//clientDetailPage = this.selectResult(client); 
		}
		return client;
	}
	
	/**
	 * Selects & opens specific client displayed the Client List View results
	 * @param rowOfClient (1 to X) is the row number of the client we want to open
	 * @return ClientDetailPage object of the page we want to open is returned
	 */
	public ClientDetailPage selectResultFromClientListView(int rowOfClient) {
		Client client = new Client();
		List<String> clientList = null;
		if (this.resultsFound()) {
			clientList = this.getAllVisibleClients();
		}
		ClientDetailPage clientDetailPage = null;
		if (clientList.size() == 0) {
			log.info("0 client results found");
			Assert.assertTrue(false, "0 client results found in Client list view");
		} else if (rowOfClient > 0) { // client found
			client.sClientName = clientList.get(rowOfClient-1); // 
			client.sClientID = this.getObjectText(this.getClientIdRowSelection(rowOfClient)); 
			log.info("Navigate to client in 1st row");
			//viewClientPage.selectFromClientListResults(1); 
			clientDetailPage = this.selectResult(client); 
		}
		
		return clientDetailPage;
	}
	
	/**
	 * Selects the specified clientID from the results list
	 * @param clientID
	 * @return ClientDetailPage object
	 
	 */
	
	public ClientDetailPage selectResult(String clientName) {
		sleep(5); //added sleep after isPresent
		isPresent(clearSearch);
		click(clearSearch);
		if(clientName.length()>0){
			for (int i = 0; i < 10; i++) {
				if(isPresent(getClientSelection(clientName))){
					click(getClientSelection(clientName));
					return new ClientDetailPage(exec);
				}
				sleep(1);
			}
		}
		else{
			Assert.assertTrue(false, "client Name Not present in Client");
		}
		return new ClientDetailPage(exec);
		
	}
	
	/**
	 * Selects the specified client from the results list
	 * @param client
	 * @return ClientDetailPage object
	 
	 */
	
	public ClientDetailPage selectResult(Client client) {
		sleep(5); //added sleep after isPresent
		isPresent(clearSearch);
		click(clearSearch);
		if(client.sClientID.length()>0 && client.sClientName.length()>0){
			sleep(20);
			for (int i = 0; i < 10; i++) {
				if(isPresent(getClientSelection(client.sClientID, client.sClientName))){
					click(getClientSelection(client.sClientID, client.sClientName));
					return new ClientDetailPage(exec);
				}
				sleep(2);
			}
		}
		else if(client.sSiteID.length()>0 && client.sClientName.length()>0){
			for (int i = 0; i < 10; i++) {
				if(isPresent(getClientSelection(client.sSiteID, client.sClientName))){
					click(getClientSelection(client.sSiteID, client.sClientName));
					return new ClientDetailPage(exec);
				}
				sleep(1);
			}
		}
		else if(client.sClientName.length()>0){
			for (int i = 0; i < 10; i++) {
				if(isPresent(getClientSelection(client.sClientName))){
					click(getClientSelection(client.sClientName));
					return new ClientDetailPage(exec);
				}
				sleep(1);
			}
		}
		else{
			Assert.assertTrue(false, "Neither client Name nor ClientID present in Client");
		}
		return new ClientDetailPage(exec);
		
	}
	
	/**
	 * Check if result is present
	 * @param client
	 * @return
	 */
	public Boolean checkResult(Client client) {
		isPresent(clearSearch);
		click(clearSearch);
		if(client.sClientID.length()>0 && client.sClientName.length()>0){
				if(checkForElement(getClientSelection(client.sClientID, client.sClientName),10000)){
					return true;
				}
				else {
					return false;				
				}

		}
		else if(client.sSiteID.length()>0 && client.sClientName.length()>0){
				if(checkForElement(getClientSelection(client.sSiteID, client.sClientName),10000)){
					return true;
				}
				else {
					return false;	
				}
		}
		else if(client.sClientName.length()>0){
				if(checkForElement(getClientSelection(client.sClientName),10000)){
					return true;
				}
				else {
					return false;
				}
		}
		else{
			return false;
		}	
	}
	
	public List<String> addAllVisibleClientsToFavorites(){
		List<String> opptys = new ArrayList<String>();
		
		List<Element> favoriteButtons = exec.getElements(getAllFavoriteButtons);
		List<Element> clientElements = exec.getElements(getAllClientNames);
		exec.executeScript("window.scrollBy(0,arguments[0])", 300);
		for(int x=0; x<favoriteButtons.size(); x++){
			
			favoriteButtons.get(x).click();
			opptys.add(clientElements.get(x).getText());
		}
		
		return opptys;
	}
	
	public List<String> getAllVisibleClients(){
		List<String> opptys = new ArrayList<String>();
		
		exec.executeScript("window.scrollBy(0,arguments[0])", 300);
		List<Element> clientElements = exec.getElements(getAllClientNames);
		
		for(int x=0; x<clientElements.size(); x++){
			opptys.add(clientElements.get(x).getText());
		}
		
		return opptys;
	}
	
	public List<String> removeAllVisibleClientsFromFavorites(){
		List<String> opptys = new ArrayList<String>();
		
		List<Element> favoriteButtons = exec.getElements(getAllUnfavoriteButtons);
		List<Element> clientElements = exec.getElements(getAllClientNames);
		exec.executeScript("window.scrollBy(0,arguments[0])", 300);
		for(int x=0; x<clientElements.size(); x++){
			
			favoriteButtons.get(x).click();
			opptys.add(clientElements.get(x).getText());
		}
		
		return opptys;
	}
}
