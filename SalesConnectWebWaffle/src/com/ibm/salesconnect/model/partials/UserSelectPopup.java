/**
 * 
 */
package com.ibm.salesconnect.model.partials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

/**
 * @author Administrator
 *
 */
public class UserSelectPopup extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(UserSelectPopup.class);
	/**
	 * @param exec
	 */
	public UserSelectPopup(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "User Select Popup has not loaded within 60 seconds");	
	}

	/* (non-Javadoc)
	 * @see com.ibm.salesconnect.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForElement(pageLoaded);
	}

	//Selectors
	public static String pageLoaded = "//*";
	
	public static String MainPageFrame = "//iframe[@id='bwc-frame']";
	
	public static String getTextField_SearchNameAdvanced = "//input[@id='search_name_advanced']";
	public static String getButton_Search = "//input[@value='Search']";
	public static String getLink_UserName(String sUserName){return ("//a[contains(text(),'" + sUserName + "')]");}
	public static String getcheckBox_UserName(String userName){return ("//a[contains(text(),'" + userName + "')]/../../../td[1]/input");}
	public static String getLink_BusinessPartner = "//a[contains(@href, 'javascript:void(0)')]";
	public static String getRadioButton_BusinessPartner = "//*[@id='EmployeeOrBPRadio_input_bp']";
	public static String getCheckBox_MyFavoriteBusinessPartners = "//*[@id='favorites_only_ctm_advanced']";
	public static String getTextField_CEID = "//*[@id='ceid_advanced']";
	public static String getTextField_BusinessPartnerCEID = "//*[@id='ceid_advanced']";
	public static String getCheckBox_ClientTeam = "//input[@id='client_team_only_advanced']";
	public static String addSelectedUsersButton = "//input[@id='MassUpdate_select_button']";
	
	/**
	 * Searches for a user
	 * @param user name
	 */
	public void searchForUser(String searchTerm){
		if(isChecked(getCheckBox_ClientTeam)){
			click(getCheckBox_ClientTeam);
		}
		type(getTextField_SearchNameAdvanced,searchTerm);
		click(getButton_Search);
	}
	
	public void searchForBusinessPartner(String searchTerm){
		click(getRadioButton_BusinessPartner);
		isPageLoaded();
		click(getCheckBox_MyFavoriteBusinessPartners);
		
		type(getTextField_BusinessPartnerCEID, searchTerm);
		click(getButton_Search);
		
		
	}
	
	/**
	 * Selects the specified contact from the results list
	 * @param Contact Name
	 */
	public void selectResult(String userName) {
		String start = userName.substring(0, userName.indexOf(" "));
		String end = userName.substring(userName.indexOf(" ") + 1, userName.length());
		String sUserFull = start + " " + "(" + start + ") " + end;
		
		if(isPresent(getcheckBox_UserName(sUserFull))){
			click(getcheckBox_UserName(sUserFull));
		}
		else{
			click(getcheckBox_UserName(userName));
		}
		
		click(addSelectedUsersButton);
	
		switchToMainWindow();
	}
	
	/**
	 * Selects the specified contact from the results list
	 * @param Contact Name
	 */
	public void selectResultUsingName(String userName) {
		String start = userName.substring(0, userName.indexOf(" "));
		String end = userName.substring(userName.indexOf(" ") + 1, userName.length());
		String sUserFull = start + " " + "(" + start + ") " + end;
		
		if(isPresent(getLink_UserName(sUserFull))){
			click(getLink_UserName(sUserFull));
		}
		else{
			click(getLink_UserName(userName));
		}
	
		switchToMainWindow();
		
		switchToFrame(MainPageFrame);
	}
	
	/**
	 * Selects the business partner from the results list
	 */
	public void selectBusinessPartnerResult() {
		click(getLink_BusinessPartner);

		switchToMainWindow();
	}
}
