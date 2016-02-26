/**
 * 
 */
package com.ibm.salesconnect.model.standard.Opportunity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.model.partials.ClientSelectPopup;
import com.ibm.salesconnect.model.partials.ContactSelectPopup;
import com.ibm.salesconnect.model.partials.EditAdditionalDetailsSubpanel;
import com.ibm.salesconnect.model.partials.EditTeamMembersSubpanel;
import com.ibm.salesconnect.model.partials.UserSelectPopup;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Opportunity;


/**
 * @author Administrator
 * @deprecated
 */
public class EditOpportunityPage extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(EditOpportunityPage.class);

	/**
	 * @param exec
	 */
	public EditOpportunityPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Create Client page has not loaded within 60 seconds");
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
	public static String pageLoaded = "//input[@id='SAVE_WITH_SERVER_VALIDATION_FOOTER']";
	
	public static String contactSearchButton = "//button[@id='btn_pcontact_id_c']";
	public static String getTextField_OpptDescription = "//textarea[@id='description']";
	public static String getListBox_SalesStage = "//select[@id='sales_stage']";
	public static String getTextField_DueDate = "//input[@id='date_closed' or @id='decision_date_1']";
	public static String getListBox_CurrencyType = "//select[@id='currency_id_select']";
	public static String getTextField_Tags = "//*[@id='tags']";
	public static String getListBox_LeadSource = "//select[@id='lead_source']";
	public static String collapseTeamMembers = "//*[@id='detailpanel_3_img_hide']";
	public static String collapseAdditionalDetails = "//*[@id='detailpanel_4_img_hide']";
	public static String getListBox_ReasonLost = "//*[@id='reason_lost_c']";
	
	public static String getLink_Save = "//input[@id='SAVE_WITH_SERVER_VALIDATION_FOOTER']";
	public static String decisionDate = "//input[@id='date_closed']";
	public static String getButton_OpptOwner = "//button[@id='btn_assigned_user_name']";
	public static String clientSearchButton = "//button[@id='btn_account_name']";
	
	//Methods
	public EditOpportunityPage enterOpportunityInfo(Opportunity oppt){
			
			if(oppt.sPrimaryContact.equals("")){
				oppt.sPrimaryContact = oppt.sPrimaryContactFirst;
			}
			
			if (oppt.sOpptDesc.length() > 0) {
				type(getTextField_OpptDescription,oppt.sOpptDesc);
			}

			if ((oppt.sPrimaryContact.length() > 0)&& (oppt.sPrimaryContactWithPreferred.length() > 0)) {
				ContactSelectPopup contactSelectPopup = openSelectContact();
				contactSelectPopup.searchForContact(oppt.sPrimaryContact);
				contactSelectPopup.selectResult(oppt.sPrimaryContactWithPreferred);
			}
			
			if (oppt.sSalesStage.length() > 0) {
				select(getListBox_SalesStage, oppt.sSalesStage);
			}
			
			if(oppt.sReasonWonLost.length() > 0){
				select(getListBox_ReasonLost, oppt.sReasonWonLost);
			}
			
			if (oppt.sCloseDate.length() > 0) {
				type(getTextField_DueDate,oppt.sCloseDate);
			}
			
			if (oppt.sCurrency.length() > 0) {
				select(getListBox_CurrencyType,oppt.sCurrency);
			}
			
			if (oppt.sTags.length() > 0) {
				type(getTextField_Tags,oppt.sTags);
			}
			
			if (oppt.sLeadSource.length() > 0) {
				select(getListBox_LeadSource,oppt.sLeadSource);
			}
			
			
			EditTeamMembersSubpanel etms = new EditTeamMembersSubpanel(exec);
			etms.enterTeamMembers(oppt);
			
			EditAdditionalDetailsSubpanel eads = new EditAdditionalDetailsSubpanel(exec);
			eads.enterAdditionalDetails(oppt);

			return this;
	}
	
	public void editOpptyOwner(String opptyOwner){
		UserSelectPopup userSelectPopup = openSelectOwner();
		userSelectPopup.searchForUser(opptyOwner);
		userSelectPopup.selectResultUsingName(opptyOwner);
	}
	
	public void editClientName(Client client){
		ClientSelectPopup clientSelectPopup = openSelectClient();
		clientSelectPopup.searchForClient(client);
		clientSelectPopup.selectResult(client);
	}
	
	public void editCMRID(String newCMRID){
		
	}
	
	public void editSalesStage(String newSalesStage){
		select(getListBox_SalesStage, newSalesStage);
	}
	
	public void editDecisionDate(String newDecisionDate){
		type(decisionDate, newDecisionDate);
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
		click(collapseTeamMembers);
		click(collapseAdditionalDetails);
		click(getLink_Save);
	}
	
	private UserSelectPopup openSelectOwner() {
		click(getButton_OpptOwner);
		getPopUp();
		return new UserSelectPopup(exec);
	}
	
	private ClientSelectPopup openSelectClient(){
		click(clientSearchButton);
		getPopUp();
		return  new ClientSelectPopup(exec);
	}
}
