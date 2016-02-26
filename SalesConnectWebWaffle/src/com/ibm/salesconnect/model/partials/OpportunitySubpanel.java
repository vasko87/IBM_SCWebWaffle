/**
 * 
 */
package com.ibm.salesconnect.model.partials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.objects.Opportunity;
import com.ibm.salesconnect.objects.RevenueItem;

/**
 * @author timlehane
 * @date Jun 20, 2013
 */
public class OpportunitySubpanel extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(OpportunitySubpanel.class);

	/**
	 * @param exec
	 */
	public OpportunitySubpanel(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "OpportunitySubpanel has not loaded within 60 seconds");	
	}


	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}

	//Selectors
	public static String pageLoaded = "//a[@id='opportunities_contacts_create_button_create_button' or @id='getRelatedDataOfClients_create_button']";
	public static String createFormLoaded = "//textarea[@id='description']";
	public static String createOpptyButton = "//a[@id='opportunities_contacts_create_button_create_button' or @id='getRelatedDataOfClients_create_button']";
	public String getBusinessCard(String userName) { return "//span[@sugar='slot3b']//a[contains(text(),'" + userName + "')]";};
	
	public static String getSortByOpptyOwner = "//div[@id='list_subpanel_opportunities']//a[contains(., 'Opportunity Owner')]";
	
	public static String contactSearchButton = "//button[@id='btn_pcontact_id_c']";
	public static String getTextField_OpptDescription = "//textarea[@id='description']";
	public static String getListBox_SalesStage = "//select[@id='sales_stage']";
	public static String getTextField_DueDate = "//input[@id='date_closed' or @id='decision_date_1']";
	public static String getListBox_CurrencyType = "//select[@id='currency_id_select']";
	public static String getListBox_LeadSource = "//select[@id='lead_source']";
	public static String getLink_Save = "//input[@id='Opportunities_dcmenu_save_button']";
	
	/**
	 * sort oppty subpanel's oppties by a specifc column
	 * @return: boolean if sort was successful 
	 */
	public boolean sortOpptySubpanelBy(String selector){
		if (checkForElement(selector)){
			click(getSortByOpptyOwner);
			sleep(1);
			return true;
		}
		return false;
	}
	
	public void openCreateOpportunityForm(){
		click(createOpptyButton);
		waitForSubpanelToLoad(createFormLoaded);
		sleep(1);
	}
	
	public void enterOpportunityInfo(Opportunity oppt,RevenueItem rli){
		
		oppt.sPrimaryContact = oppt.sPrimaryContactFirst + " " + oppt.sPrimaryContactLast;
		
		if (oppt.sOpptDesc.length() > 0) {
			type(getTextField_OpptDescription,oppt.sOpptDesc);
		}

		if (oppt.sPrimaryContact.length() > 0) {
			ContactSelectPopup contactSelectPopup = openSelectContact();
			contactSelectPopup.searchForContact(oppt.sPrimaryContact);
			contactSelectPopup.selectResult(oppt.sPrimaryContact);
		}
		
		if (oppt.sSalesStage.length() > 0) {
			select(getListBox_SalesStage, oppt.sSalesStage);
		}
		
		if (oppt.sCloseDate.length() > 0) {
			type(getTextField_DueDate,oppt.sCloseDate);
		}
		
		if (oppt.sCurrency.length() > 0) {
			select(getListBox_CurrencyType,oppt.sCurrency);
		}
		
		
		if (oppt.sLeadSource.length() > 0) {
			select(getListBox_LeadSource,oppt.sLeadSource);
		}
		
		if(rli.sFindOffering.length()>0){
			EditLineItemSubpanel elis = new EditLineItemSubpanel(exec);
			elis.enterRLIInfo(rli);
		}
		
		EditTeamMembersSubpanel etms = new EditTeamMembersSubpanel(exec);
		etms.enterTeamMembers(oppt);
}
	
	/**
	 * Open the select contact popup
	 * @return select contact object
	 */
	public ContactSelectPopup openSelectContact(){
		click(contactSearchButton);
		getPopUp();
		return  new ContactSelectPopup(exec);
	}
	
	public void saveOpportunity(){
		click(getLink_Save);
	}
	
	public void waitForOpptyToSave(Opportunity oppt){
		waitForPageToLoad(oppt.sOpptDesc);
	}
	
	public void verifyBusinessCard(User user){
		ConnectionsBusinessCard connectionsBusinessCard = openConnectionsBusinessCard(getBusinessCard(user.getFirstName()), exec);
		connectionsBusinessCard.verifyBusinessCardContents(user);
	}
}
