/**
 * 
 */
package com.ibm.salesconnect.model.standard.Opportunity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.StandardCreatePage;
import com.ibm.salesconnect.model.partials.ContactSelectPopup;
import com.ibm.salesconnect.model.partials.EditAdditionalDetailsSubpanel;
import com.ibm.salesconnect.model.partials.EditLineItemSubpanel;
import com.ibm.salesconnect.model.partials.EditTeamMembersSubpanel;
import com.ibm.salesconnect.model.partials.OpportunityLineItemPanel;
import com.ibm.salesconnect.model.standard.Contact.ContactSearchPage;
import com.ibm.salesconnect.objects.Opportunity;
import com.ibm.salesconnect.objects.RevenueItem;


/**
 * @author Administrator
 *
 */
public class CreateOpportunityPage extends StandardCreatePage {
	Logger log = LoggerFactory.getLogger(CreateOpportunityPage.class);

	/**
	 * @param exec
	 */
	public CreateOpportunityPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Create Opportunity page has not loaded within 60 seconds");
	}

	/* (non-Javadoc)
	 * @obsolete
	 * @see com.ibm.salesconnect.model.StandardPageFrame#isPageLoaded()
	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}
	*/

	
	//Selectors

	public static String contactSearchButton = "//button[@id='btn_pcontact_id_c']";
	public static String getTextField_OpptDescription = "//span[@data-fieldname='description']//textarea";
	public static String pageBackground = "//span[@data-fieldname='description']//textarea/..";
	public static String getListBox_SalesStage = "//span[@data-fieldname='sales_stage']//div[@id='sales_stage']//span[@class='select2-chosen']";
	public static String getListBox_ReasonLost = "//div[@data-name='sales_stage']//div[@id='reason_lost']//span[@class='select2-chosen']";
	public static String getListBox_ReasonWon = "//div[@data-name='sales_stage']//div[@id='reason_won']//span[@class='select2-chosen']";
	public static String getTextField_DueDate = "//span[@data-fieldname='date_closed']//input";
	public static String getListBox_CurrencyType = "//span[@data-fieldname='currency_id']//span[@class='select2-chosen']";
	public static String getTextField_Tags = "//span[@data-fieldname='tags']//li//input";
	public static String getListBox_LeadSource = "//span[@data-fieldname='lead_source']//span[@class='select2-chosen']";
	public static String getCloseArrow_LeadSource = "//span[@data-fieldname='lead_source']//abbr"; //Only shown when a selection has been made
	public static String getListBox_PrimaryContact = "//div[@data-name='pcontact_id_c']//a";
	public static String getListBox_Client = "//span[@data-fieldname='account_name']//span[@class='select2-chosen']";
	public String collapseLineItem(int i) { return getIthOpptySubform(i) + "//a[@data-toggle='collapse']//i";}
	public String getIthOpptySubform(int i) {return "//div[@id='oppty-rli-subform']/div["+String.valueOf(i)+"]";}
	public static String collapseTeamMembers = "//*[@id='detailpanel_3_img_hide']";
	public static String collapseAdditionalDetails = "//*[@id='detailpanel_4_img_hide']";
	public String RemoveRLI(int i){return getIthOpptySubform(i) + "//button[@name='remove']//i";}
	public String getSelection(String dropdown, String choice){return dropdown + "/..//li";};
	
	public static String getLink_Save = "//input[@id='SAVE_WITH_SERVER_VALIDATION_FOOTER']";
	public static String getLink_Edit = "//*[@id='edit_button']";

	
	//Methods
	public CreateOpportunityPage enterOpportunityInfo(Opportunity oppt,RevenueItem rli){
			if(oppt.sPrimaryContact.equals("")){
				oppt.sPrimaryContact = oppt.sPrimaryContactFirst;
			}
			
			if (oppt.sOpptDesc.length() > 0) {
				type(getTextField_OpptDescription,oppt.sOpptDesc);
			}

			triggerChange("css=span[data-fieldname=\"description\"] textarea");
			
			click(pageBackground);
			if (oppt.sPrimaryContact.length() > 0) {
				selectContact(oppt.sPrimaryContact);
			//	select(getListBox_PrimaryContact, oppt.sPrimaryContact);
			}
			
			if(oppt.sLeadSource.length() > 0){
				select(getListBox_LeadSource, oppt.sLeadSource);
			}
			
			if (oppt.sSalesStage.length() > 0) {
				select(getListBox_SalesStage, oppt.sSalesStage);
				
				if (oppt.sSalesStage==(GC.gsOppWonImplementing))
				{
					if (oppt.sReasonWonLost.length() > 0) {
						select(getListBox_ReasonWon, oppt.sReasonWonLost);
					}
					else{
						select(getListBox_ReasonWon, GC.gsOppWLowestPrice);
					}
				}
				else if (oppt.sSalesStage==(GC.gsOppWonComplete))
				{
					if (oppt.sReasonWonLost.length() > 0) {
						select(getListBox_ReasonWon, oppt.sReasonWonLost);
					}
					else{
						select(getListBox_ReasonWon, GC.gsOppWLowestPrice);
					}
				}
				else if (oppt.sSalesStage==(GC.gsOppNoBid))
				{
					if (oppt.sReasonWonLost.length() > 0) {
						select(getListBox_ReasonLost, oppt.sReasonWonLost);
					}
					else{
						select(getListBox_ReasonLost, GC.gsOppLLowOdds);
					}
				}
				else if (oppt.sSalesStage==(GC.gsOppCustomerNotPursue))
				{
					if (oppt.sReasonWonLost.length() > 0) {
						select(getListBox_ReasonLost, oppt.sReasonWonLost);
					}
					else{
						select(getListBox_ReasonLost, GC.gsOppLPriorityChange);
					}
				}
				else if (oppt.sSalesStage==(GC.gsOppLostToCompetition))
				{
					if (oppt.sReasonWonLost.length() > 0) {
						select(getListBox_ReasonLost, oppt.sReasonWonLost);
					}
					else{
						select(getListBox_ReasonLost, GC.gsOppLSoleOnPrice);
					}
				}
			}
						
			if (oppt.sCloseDateExact.length() > 0) {
				type(getTextField_DueDate,oppt.sCloseDateExact);
			}
			
			if (oppt.sCurrency.length() > 0) {
				select(getListBox_CurrencyType, oppt.sCurrency);
			}
			
			if (oppt.sTags.length() > 0) {
				typeEnter(getTextField_Tags,oppt.sTags);
			}
			
			if (oppt.sLeadSource.length() > 0) {
				select(getListBox_LeadSource,oppt.sLeadSource);
			}
			
			if(oppt.sClient.length() > 0){
				select(getListBox_Client, oppt.sClient);
			}

			//triggerChange("span[data-fieldname=\"account_name\"]");
			
			if(!(rli==null)){
				if(rli.sFindOffering.length()>0){
				OpportunityLineItemPanel elis = new OpportunityLineItemPanel(exec, 1);
				elis.enterInfo(rli);
				}
			}	
			else{
				click(RemoveRLI(1));
				}
			
			//EditTeamMembersSubpanel etms = new EditTeamMembersSubpanel(exec);
			//etms.enterTeamMembers(oppt);
			
			//EditAdditionalDetailsSubpanel eads = new EditAdditionalDetailsSubpanel(exec);
			//eads.enterAdditionalDetails(oppt);
			
			return this;
	}
	
	//Methods @deprecated
	public CreateOpportunityPage enterOpportunityInfoWithRLIs(Opportunity oppt,RevenueItem rli[]){
			if(oppt.sPrimaryContact.equals("")){
				oppt.sPrimaryContact = oppt.sPrimaryContactFirst;
			}
			
			if (oppt.sOpptDesc.length() > 0) {
				type(getTextField_OpptDescription,oppt.sOpptDesc);
			}
			
			if (oppt.sPrimaryContact.length() > 0) {
				ContactSelectPopup contactSelectPopup = openSelectContact();
				contactSelectPopup.searchForContact(oppt.sPrimaryContact);
				String firstName = oppt.sPrimaryContact.substring(0, oppt.sPrimaryContact.indexOf(" "));
				contactSelectPopup.selectResult(firstName);
//				switchToFrame(creationFrame);
			}
			
			if (oppt.sSalesStage.length() > 0) {
				
				select(getListBox_SalesStage, oppt.sSalesStage);
				
				if (oppt.sSalesStage==(GC.gsOppWonImplementing))
				{
					if (oppt.sReasonWonLost.length() > 0) {
						select(getListBox_ReasonWon, oppt.sReasonWonLost);
					}
					else{
						select(getListBox_ReasonWon, GC.gsOppWLowestPrice);
					}
				}
				else if (oppt.sSalesStage==(GC.gsOppWonComplete))
				{
					if (oppt.sReasonWonLost.length() > 0) {
						select(getListBox_ReasonWon, oppt.sReasonWonLost);
					}
					else{
						select(getListBox_ReasonWon, GC.gsOppWLowestPrice);
					}
				}
				else if (oppt.sSalesStage==(GC.gsOppNoBid))
				{
					if (oppt.sReasonWonLost.length() > 0) {
						select(getListBox_ReasonLost, oppt.sReasonWonLost);
					}
					else{
						select(getListBox_ReasonLost, GC.gsOppLLowOdds);
					}
				}
				else if (oppt.sSalesStage==(GC.gsOppCustomerNotPursue))
				{
					if (oppt.sReasonWonLost.length() > 0) {
						select(getListBox_ReasonLost, oppt.sReasonWonLost);
					}
					else{
						select(getListBox_ReasonLost, GC.gsOppLPriorityChange);
					}
				}
				else if (oppt.sSalesStage==(GC.gsOppLostToCompetition))
				{
					if (oppt.sReasonWonLost.length() > 0) {
						select(getListBox_ReasonLost, oppt.sReasonWonLost);
					}
					else{
						select(getListBox_ReasonLost, GC.gsOppLSoleOnPrice);
					}
				}
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
			
			for (int i=0; i<rli.length; i++) {
				if (rli[i].sFindOffering.length() > 0) {
					EditLineItemSubpanel elis = new EditLineItemSubpanel(exec);
					elis.enterRLIInfo(rli[i]);
				}
				if(i!=rli.length-1) { //if not last RLI
					click("//span[@class='rli_addAnotherLink']//a[contains(text(),'Add another')]");
				}
			}
			
			EditTeamMembersSubpanel etms = new EditTeamMembersSubpanel(exec);
			etms.enterTeamMembers(oppt);
			
			EditAdditionalDetailsSubpanel eads = new EditAdditionalDetailsSubpanel(exec);
			eads.enterAdditionalDetails(oppt);

			return this;
	}
	
	/**
	 * Open the select contact popup
	 * @return select contact object
	 */
	public ContactSelectPopup openSelectContact(){
		scrollElementToMiddleOfBrowser(contactSearchButton);
		click(contactSearchButton);
		switchToMainWindow();
		getPopUp();
		return  new ContactSelectPopup(exec);
	}
	
	public ViewOpportunityPage saveOpportunity(Opportunity opp){
		click(saveButton);
		String temp = waitToLoad();
		opp.setOpptyNumber(temp);
		return new ViewOpportunityPage(exec);
	}
	
	public OpportunityDetailPage saveEditedOpportunity(Opportunity opp){
		click(saveButton);
		waitToLoad();
		return new OpportunityDetailPage(exec);
	}
	
	public void selectContact(String primaryContact){
		click(getListBox_PrimaryContact);
		click("//div[@id='select2-drop']//div[contains(text(),'Search for more...')]");
		ContactSearchPage contactSearchPage = new ContactSearchPage(exec);
		contactSearchPage.selectContact(primaryContact);
	}
	
	public void editOpptyDescription(Opportunity oppt){
		type(getTextField_OpptDescription,oppt.sOpptDesc);
		triggerChange("css=span[data-fieldname=\"description\"] textarea");
	}
	
	public void saveOppty(){
		click(saveButton);
	}
	

}