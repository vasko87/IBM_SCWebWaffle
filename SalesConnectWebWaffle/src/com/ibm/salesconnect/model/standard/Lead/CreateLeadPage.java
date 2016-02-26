/**
 * 
 */
package com.ibm.salesconnect.model.standard.Lead;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardCreatePage;
import com.ibm.salesconnect.objects.Lead;
/**
 * 
 * @author Veena H
 * @date March 15, 2015
 */
public class  CreateLeadPage extends StandardCreatePage {
	Logger log = LoggerFactory.getLogger(CreateLeadPage.class);

	/**
	 * @param exec
	 */
	public CreateLeadPage(Executor exec) {
		super(exec);
		//Assert.assertTrue(isPageLoaded(), "Create Lead page has not loaded within 60 seconds");	
	}


	@Override
	public boolean isPageLoaded() {
		return waitForElement(pageLoaded);
	}

	//Selectors
	public static String pageLoaded = "//input[@name='first_name']";

	public static String getTextArea_FirstName = "css=input[name=\"first_name\"]";
	public static String getTextArea_LasttName = "css=input[name=\"last_name\"]";
	public static String getTextArea_MobilePhone = "css=input[name=\"phone_mobile\"]";
	public static String getTextArea_OfficePhone = "css=input[name=\"phone_work\"]";
	static String getTextArea_EmailID="css=div[data-name=\"email\"] input";

	public static String getLink_Save = "//div[@id='drawers']//a[@name='save_button']";
	public static String getStatus = "//span[@data-fieldname='status']//span[@class='select2-chosen']";
	public static String leadDropdown = "//div[@data-name='lead_source']//span[@class='select2-chosen']";

	/**
	 * Enter information for the lead to be created
	 * @return CreateLeadPage object 
	 */
	public CreateLeadPage enterLeadInfo(Lead lead){
		
		if (lead.sFirstName.length() > 0) {
			type(getTextArea_FirstName, lead.sFirstName);
		}
		triggerChange(getTextArea_FirstName);
		
		if (lead.sLastName.length() > 0) {
			type(getTextArea_LasttName, lead.sLastName);
		}
		triggerChange(getTextArea_LasttName);
		
		if (lead.sOfficePhone.length() > 0) {
			type(getTextArea_OfficePhone, lead.sOfficePhone);
		}
		triggerChange(getTextArea_OfficePhone);
		if (lead.sEmailId.length() > 0)
		{
			type(getTextArea_EmailID, lead.sEmailId);
		}
		triggerChange(getTextArea_EmailID);
		if(lead.sLeadSource.length() >0)
		{
			select(leadDropdown, lead.sLeadSource);
		}
		if(lead.sStatus.length() > 0)
		{
			select(getStatus, lead.sStatus);
		}
		
		scrollElementToMiddleOfBrowser(getTextArea_MobilePhone);
		if (lead.sMobilePhone.length() > 0) {
				type(getTextArea_MobilePhone, lead.sMobilePhone);
		}

		
		click(getTextArea_FirstName);
		
		
		return this;
	}

	/**
	 * Save the current lead
	 */
	public void saveLead(){
		sleep(3);
		click(saveButton);
	}
	
	public ViewLeadPage saveLeadOnLeadViewPage(){
		click(saveButton);
		return new ViewLeadPage(exec);
	}
	
	public void editEmail(String email){

		type(getTextArea_EmailID, email);
		triggerChange(getTextArea_EmailID);
	}
}


