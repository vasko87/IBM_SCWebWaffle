package com.ibm.salesconnect.model.standard.Home;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

public class UserProfileEditPage extends StandardPageFrame{

	public static String saveButton = "//input[@id='Save']";
	public static String advancedTab = "//a[@id='tab4']";
	public static String leadsOption = "//option[@value='Leads']";
	public static String chooserLeftArrow = "//*[@id='chooser_display_tabs_left_arrow']";
	
	public UserProfileEditPage(Executor exec) {
		super(exec);
		isPageLoaded();
	}
	
	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(saveButton);
	} 
	
	public UserProfilePage enableLeads(){
		click(advancedTab);
		click(leadsOption);
		click(chooserLeftArrow);
		click(saveButton);
		return new UserProfilePage(exec);
	}

}
