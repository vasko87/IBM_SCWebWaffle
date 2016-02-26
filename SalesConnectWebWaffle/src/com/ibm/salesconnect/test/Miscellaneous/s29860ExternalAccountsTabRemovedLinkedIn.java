package com.ibm.salesconnect.test.Miscellaneous;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Home.UserProfilePage;

/**
 * @author evafarrell
 * @date Oct 17th, 2013
 */
@Test(groups = { "Miscellaneous"})
public class s29860ExternalAccountsTabRemovedLinkedIn extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(s29860ExternalAccountsTabRemovedLinkedIn.class);

	
	public void Test_s29860ExternalAccountsTabRemovedLinkedIn() {
		log.info("Start test method Test_s29860ExternalAccountsTabRemovedLinkedIn");
		
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);

		log.info("Navigate to User Profile");
		dashboard.isPageLoaded();
		UserProfilePage userProfilePage = dashboard.openUserProfilePage();
		
		log.info("Click External Accounts Tab");
		userProfilePage.OpenExternalAccountsTab();
		
		log.info("Create External Accounts and Verify that LinkedIn is no longer an option");
		userProfilePage.CreateExternalAccounts();
		userProfilePage.VerifyLinkedInNotListedInApplications();
				
		log.info("End of test method Test_s29860ExternalAccountsTabRemovedLinkedIn");
		}

	}
