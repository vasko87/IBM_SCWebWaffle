/**
 * 
 */
package com.ibm.salesconnect.model.partials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.objects.RevenueItem;

/**
 * @author Administrator
 *
 */
public class EditLineItemSubpanel extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(EditLineItemSubpanel.class);
	
	/**
	 * @param exec
	 */
	public EditLineItemSubpanel(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Edit Line Item subpanel has not loaded within 60 seconds");
	}

	/* (non-Javadoc)
	 * @see com.ibm.salesconnect.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}
	
	//Selectors
	public static String pageLoaded = "//input[@id='assigned_user_name_1']";
	
	public String getTextField_FindOffering(){
		return ("//input[@id='offering_"+ getLastInstance() +"-input']");}
	public static String getOfferingPick(String sOffering){
		return ("//ul[@role='listbox']/li[@role='option'][contains(@data-text,'"+sOffering+"')]");}
	public static String getCompetitor(String sCompetitor){
		return ("//li[@role='option'][contains(@data-text,'"+sCompetitor+"')]");}
	
	public String getTextField_RevenueAmount(){
		return ("//input[@id='revenue_amount_"+ getLastInstance() + "']");}
	
	public String getListBox_Probability(){
		return ("//select[@id='probability_"+ getLastInstance() + "']");}
	
	public String getTextField_DecisionDate(){
		return ("//input[@id='fcast_date_sign_"+ getLastInstance() + "']");}
	
	public String getTextField_AssignedTo(){
		return ("//input[@id='assigned_user_name_"+ getLastInstance() + "']"); }
	
	public static String getTextField_Competitors = "//*[@id='competitor_1_ac']";
	public static String getListBox_ContractType = "//*[@id='srv_work_type_1']";
	
	//Methods
	public void enterRLIInfo(RevenueItem rli){	
		if (rli.sFindOffering.length()>0) {
			click(getTextField_FindOffering());
			type(getTextField_FindOffering(),rli.sFindOffering);
			click(getOfferingPick(rli.sFindOffering));
		}
	
		if(rli.sRevenueAmount.length()>0){
			type(getTextField_RevenueAmount(),rli.sRevenueAmount);
		}
		
		if(rli.sProbability.length()>0){
			select(getListBox_Probability(), rli.sProbability+"%");
		}
		
		// RLI  Date
		if (rli.sDecisionDate.length() > 0) {
			type(getTextField_DecisionDate(),rli.sDecisionDate);
		}
		
		// RLI Owner
		if (rli.sOwner.length() > 0) {
			type(getTextField_AssignedTo(),rli.sOwner);
		}
		
		if (rli.sCompetitor.length() > 0){
			click(getTextField_Competitors);
			type(getTextField_Competitors,rli.sCompetitor);
			click(getCompetitor(rli.sCompetitor));
		}
		
		//uncommented this as it is required on SVT5 build 2.2.6.3 sev1-80
		if (rli.sContractType.length() > 0){
			select(getListBox_ContractType, rli.sContractType);
		}
	
	}
	
	/**
	 * Get the last present instance of an rli
	 * @return the last instance 
	 */
	private String getLastInstance(){
		String sInstance = "";	
		int iMax = 5; 

		for (int i = 1; i<iMax; i++){
			String sName = "//input[@id='offering_"+i+"-input']";
			
			if(!isPresent(sName)){
				//found the first instance that did not exist -- return previous instance
				sInstance = Integer.toString(i-1);
				return sInstance;
			}
		}
		return sInstance; //instance not found, return empty string
	}
}