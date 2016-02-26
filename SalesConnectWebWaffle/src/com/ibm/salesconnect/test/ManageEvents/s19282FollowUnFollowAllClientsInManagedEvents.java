/**
 * 
 */
package com.ibm.salesconnect.test.ManageEvents;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Collab.ManageEventsDialog;


/**
 * @author Tyler Clayton
 * @date July 11, 2013
 */
public class s19282FollowUnFollowAllClientsInManagedEvents extends
		ProductBaseTest {
	Logger log = LoggerFactory.getLogger(s19282FollowUnFollowAllClientsInManagedEvents.class);

	User user1 = null;
	
	@Test(groups = {"ManageEvents"})
	public void Test_s19282FollowAllClientsInManagedEvents() throws SQLException, InterruptedException {
		log.info("Start of test for s19282FollowAllClientsInManagedEvents");
		try{
			user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		ManageEventsDialog manageEventsDialog = dashboard.openManageEventsDialog();
		
		manageEventsDialog.isPageLoaded();
		manageEventsDialog.switchToTab("Clients");
		log.info("If already following all, unfollow all");
//		if(manageEventsDialog.verifyFollowingAll("Clients")){
			manageEventsDialog.stopFollowingAllInTab();
//		}
		log.info("Follow all");
		manageEventsDialog.followAllInTab();
		manageEventsDialog.closeDialog();
		
		ManageEventsDialog manageEventsDialog2 = dashboard.openManageEventsDialog();
		manageEventsDialog2.switchToTab("Clients");
		
		log.info("Verify that all clients are being followed");
		if(!manageEventsDialog2.verifyFollowingAll("Clients")){
			manageEventsDialog2.stopFollowingAllInTab();
			Assert.assertFalse(true, "Failed to follow clients.");
		}
		}
		catch(Exception e){
			log.info(e.getMessage());
			commonUserAllocator.checkInAllGroupUsersWithToken(GC.busAdminGroup, "s19282FollowAllClientsInManagedEvents");
		}
		
		
		log.info("End of test method s19282FollowAllClientsInManagedEvents");
	}
	
	@Test(groups = {"ManageEvents"}, dependsOnMethods={"Test_s19282FollowAllClientsInManagedEvents"})
	public void Test_s19282UnFollowAllClientsInManagedEvents() throws SQLException, InterruptedException {
		log.info("Start of test setup for Test_s19282UnFollowAllClientsInManagedEvents");
		
		try{
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		ManageEventsDialog manageEventsDialog = dashboard.openManageEventsDialog();
		manageEventsDialog.switchToTab("Clients");

		//script requires 2 clicks on this button
		manageEventsDialog.stopFollowingAllInTab();
		manageEventsDialog.stopFollowingAllInTab();
	
		if(!manageEventsDialog.verifyFollowingNone("Clients")){
			Assert.assertFalse(true, "Failed to unfollow clients.");
		}
		}
		finally{
			commonUserAllocator.checkInAllGroupUsersWithToken(GC.busAdminGroup, "s19282FollowAllClientsInManagedEvents");
		}
		
		log.info("End of test method Test_s19282UnFollowAllClientsInManagedEvents");
	}
}