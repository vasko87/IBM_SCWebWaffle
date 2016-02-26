/**
 * 
 */
package com.ibm.salesconnect.model.partials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.objects.Opportunity;

/**
 * @author Administrator
 *
 */
public class EditAdditionalDetailsSubpanel extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(EditAdditionalDetailsSubpanel.class);

	/**
	 * @param exec
	 */
	public EditAdditionalDetailsSubpanel(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Edit Additionasl Deatils subpanel has not loaded within 60 seconds");
	}

	/* (non-Javadoc)
	 * @see com.ibm.salesconnect.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}
	
	//Selectors
	public static String pageLoaded = "//input[@id='campaign_code_c_ac']";
	
	public static String getTextField_CampaignCodes = "//input[@id='campaign_code_c_ac']";
	public String getCampaignCodePick(String sCampaignCode) {return  "//ul[@role='listbox']/li[@role='option'][contains(@data-text,'"+sCampaignCode+"')]";}
	public static String getListBox_Restricted = "//select[@id='restricted']";
	public static String getCheckBox_International = "//input[@id='international_c']";
	

	/**
	 * Enter details into the additional details subpanel
	 * @param oppt
	 */
	public void enterAdditionalDetails(Opportunity oppt){
		if (oppt.sCampaign.length() > 0) {
			click(getTextField_CampaignCodes);
			type(getTextField_CampaignCodes,oppt.sCampaign);
			click(getCampaignCodePick(oppt.sCampaign));
		}
		
		if (oppt.sRestrict.length() > 0){
			select(getListBox_Restricted, oppt.sRestrict);
		}
		
		if(oppt.bInternational){
			if(!isChecked(getCheckBox_International)){
				click(getCheckBox_International);
			}
		}
		else {
			if(isChecked(getCheckBox_International)){
				click(getCheckBox_International);
			}
		}

		
	}
}
