/**
 * 
 */
package com.ibm.salesconnect.model.standard.Home;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.model.standard.Dashboard;

/**
 * @author evafarrell
 * @date May 27, 2013
 */
public class UserProfilePage extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(UserProfilePage.class);

	/**
	 * @param exec
	 */
	public UserProfilePage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "User Profile Page has not loaded within 60 seconds");	
	}


	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}
	
	//Selectors
	public static String pageLoaded = "//*";
	public static String externalAccountsTab = "//em[contains(text(),'External Accounts')]";
	public static String createExternalAccount = "//input[@id='EAPM_create_button']";
	public static String application = "//select[@id='application']";
	public static String Google = "//option[@value='Google']";
	public static String LinkedIn = "//option[@value='LinkedIn']";
	public static String editButton = "//a[@id='edit_button']";
	
	//Methods
	public boolean userIsLoggedIn(User currentUser) {
		if(isPresent("//*[contains(text(),'" + currentUser.getEmail() + "')]")) {
			log.info("User Logged In: " + currentUser.getEmail());
			return true;
		}
		else
			return false;
	}
	/**
	 * Clicks on External Accounts Tab
	 *
	 */
	public void OpenExternalAccountsTab(){
		click(externalAccountsTab);
	}
	
	public void enableLeads(){
		UserProfileEditPage userProfileEditPage = getEditUserProfile();
		userProfileEditPage.enableLeads();
	}
	
	public Dashboard returnToDash(){
		exec.switchToFrame().returnToTopFrame();
		click("//div[@title='Dashboard']//a");
		return new Dashboard(exec);
	}
	
	public UserProfileEditPage getEditUserProfile(){
		click(editButton);
		return new UserProfileEditPage(exec);
	}
	
	
	/**
	 * Clicks on Create in External Accounts Tab
	 *
	 */
	public void CreateExternalAccounts(){
		click(createExternalAccount);
	}
	
	/**
	 * Verify that LinkedIn is no longer an option in Applications
	 *
	 */
	public boolean VerifyLinkedInNotListedInApplications(){
		if(isPresent(Google)){
			log.info("Google is an option in Applications, this is expected");
		}
		if(isPresent(LinkedIn)){
			log.info("LinkedIn is an option in Applications, this is not expected");
			return false;
		}
		log.info("LinkedIn is not an option in Applications, this is expected");
		return true;
	}

}
