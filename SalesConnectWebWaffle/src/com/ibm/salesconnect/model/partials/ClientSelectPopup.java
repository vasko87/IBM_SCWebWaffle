/**
 * 
 */
package com.ibm.salesconnect.model.partials;

import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchWindowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.objects.Client;

/**
 * @author Administrator
 *
 */
public class ClientSelectPopup extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(ClientSelectPopup.class);

	/**
	 * @param exec
	 */
	public ClientSelectPopup(Executor exec) {
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
	public static String pageLoaded = "//button[@id='popupViewNextButton']";
	public String getSearchField(String searchType){ return  "//input[@id='name_" + searchType + "']";}
	public String getSearchIn(String searchType){ return  "//select[@id='field_name_" + searchType + "']";}
	public String getSearchFor(String searchType){ return  "//select[@id='billing_address_country_" + searchType + "']";}
	public String getSearchShowing(String searchType){ return  "//select[@name='accounts_showing_" + searchType + "']";}
	public String getMyClients(String searchType){ return  "//input[@id='current_user_only_" + searchType + "']";}
	public static String searchButton = "//*[@id='search_form_submit']";
	
	//public String getClientSelection(String clientID, String clientName){ return "//table[contains(@class,'list')]//span[contains(text(),'" 
		//+ clientID + "') and @class = 'LEVEL_DC_ID_PLAIN']/../..//a[contains(text(),'" + clientName + "')]";}
//	public String getClientSelection(String clientID, String clientName){ return "//table[contains(@class,'list')]//tr//td[contains(text(),'"
//			+ clientID + "')]/..//td/a[contains(text(),'" + clientName + "')]";}
	public String getClientSelection(String clientID, String clientName){ return "//table[contains(@class,'list')]//a[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'"+clientName.toLowerCase()+"')]";}
	public String getClientSelection(String clientName){ return "//table[contains(@class,'list')]//a[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'"+clientName.toLowerCase()+"')]";}
	
	//Tasks
	/**
	 * Searches for a client based on the parameters
	 * @param client
	 */
	public void searchForClient(Client client){
		String searchType="advanced";
		String searchTerm="";
		if(client.sSearchIn.equals(GC.showingInClientName)){
			searchTerm=client.sClientName;
		}
		else if(client.sSearchIn.equals(GC.showingInClientID)){
			searchTerm=client.sClientID;
		}
		else if(client.sSearchIn.equals(GC.showingInSiteID)){
			if(client.sSiteID.length()>0){
				searchTerm=client.sSiteID;
			}
			else {
				searchTerm=client.sClientID;
			}
			
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
		
		click(searchButton);
	}
	
	/**
	 * Selects the specified client from the results list
	 * @param client
	 */
	public void selectResult(Client client) {
		if(client.sClientID.length()>0 && client.sClientName.length()>0){
			sleep(2);
			click(getClientSelection(client.sClientID, client.sClientName));
		}
		else if(client.sClientName.length()>0){
			click(getClientSelection(client.sClientName));
		}
		else{
			Assert.assertTrue(false, "Neither client Name nor ClientID present in Client");
		}
		try{
			acceptAlert();
		}
		catch(NoSuchWindowException nswe){
			log.debug("No accept alert appeared");
		}
		catch(NoAlertPresentException nape){
			log.debug("No accept alert appeared");
		}
		switchToMainWindow();
	}
}
