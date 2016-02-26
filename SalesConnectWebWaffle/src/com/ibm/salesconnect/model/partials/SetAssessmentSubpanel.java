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

/**
 * @author timlehane
 * @date Jun 20, 2013
 */
public class SetAssessmentSubpanel extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(SetAssessmentSubpanel.class);

	/**
	 * @param exec
	 */
	public SetAssessmentSubpanel(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "SetAssessmentSubpanel has not loaded within 60 seconds");	
	}


	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}
	
	//Selectors
	public static String pageLoaded = "//div[contains(@id,'list_subpanel_ibm_assessment')]//a[contains(text(),'Completed')]";
	public static String createAssessmentButton = "//a[contains(@id,'button_create_button')]";
	public static String saveButton = "//form[@id='form_SubpanelQuickCreate_ibm_Assessment']//input[@id='Contacts_subpanel_save_button']";
	public String getBusinessCard(String userName) { return "//span[@sugar='slot2b']//a[contains(text(),'" + userName + "')]";};
	
	//Methods
	public void openAssessmentForm(){
		click(createAssessmentButton);
		waitForSubpanelToLoad(saveButton);
		sleep(1);
	}
	
	public void saveAssessment(){
		click(saveButton);
		waitForSubpanelToLoad(pageLoaded);
	}
	
	public void verifyBusinessCard(User user){
		ConnectionsBusinessCard connectionsBusinessCard = openConnectionsBusinessCard(getBusinessCard(user.getFirstName()), exec);
		connectionsBusinessCard.verifyBusinessCardContents(user);
	}
}
