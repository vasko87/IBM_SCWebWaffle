/**
 * 
 */
package com.ibm.salesconnect.model.standard.Opportunity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.model.partials.ContactSelectPopup;
import com.ibm.salesconnect.model.partials.EditLineItemSubpanel;
import com.ibm.salesconnect.model.partials.EditTeamMembersSubpanel;
import com.ibm.salesconnect.objects.Opportunity;
import com.ibm.salesconnect.objects.RevenueItem;


/**
 * @author Administrator
 * @deprecated
 */
public class QuickCreateOpportunityPage extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(QuickCreateOpportunityPage.class);

	/**
	 * @param exec
	 */
	public QuickCreateOpportunityPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Quick Create Opportunity page has not loaded within 60 seconds");
	}

	/* (non-Javadoc)
	 * @see com.ibm.salesconnect.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}

	
	//Selectors
	public static String pageLoaded = "//input[@id='Opportunities_subpanel_full_form_button']";
	
	public static String contactSearchButton = "//button[@id='btn_pcontact_id_c']";
	public static String getTextField_OpptDescription = "//textarea[@id='description']";
	public static String getListBox_SalesStage = "//select[@id='sales_stage']";
	public static String getTextField_DueDate = "//input[@id='date_closed' or @id='decision_date_1']";
	public static String getListBox_CurrencyType = "//select[@id='currency_id_select']";
	public static String getListBox_LeadSource = "//select[@id='lead_source']";
	
	public static String getLink_Save = "//input[@id='Opportunities_dcmenu_save_button']";

	
	//Methods
	public QuickCreateOpportunityPage enterOpportunityInfo(Opportunity oppt,RevenueItem rli){
			
			oppt.sPrimaryContact = oppt.sPrimaryContactFirst + " " + oppt.sPrimaryContactLast;
			
			if (oppt.sOpptDesc.length() > 0) {
				type(getTextField_OpptDescription,oppt.sOpptDesc);
			}

			if (oppt.sPrimaryContact.length() > 0) {
				ContactSelectPopup contactSelectPopup = openSelectContact();
				contactSelectPopup.searchForContact(oppt.sPrimaryContact);
				contactSelectPopup.selectResult(oppt.sPrimaryContactWithPreferred);
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

			return this;
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
	
}
