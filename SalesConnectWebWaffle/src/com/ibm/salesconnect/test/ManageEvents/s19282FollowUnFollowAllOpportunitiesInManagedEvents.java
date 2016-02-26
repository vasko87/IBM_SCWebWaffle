/**
 * 
 */
package com.ibm.salesconnect.test.ManageEvents;

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
public class s19282FollowUnFollowAllOpportunitiesInManagedEvents extends
		ProductBaseTest {
	Logger log = LoggerFactory.getLogger(s19282FollowUnFollowAllOpportunitiesInManagedEvents.class);

	User user1 = null;
	
	@Test(groups = {"ManageEvents"})
	public void Test_s19282FollowAllOpportunitiesInManagedEvents() {
		log.info("Start of test setup for s19282FollowAllOpportunitiesInManagedEvents");
		try{
			user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		ManageEventsDialog manageEventsDialog = dashboard.openManageEventsDialog();
		
		manageEventsDialog.switchToTab("Opportunities");
		log.info("If already following all, unfollow all");
//		if(manageEventsDialog.verifyFollowingAll("Opportunities")){
			manageEventsDialog.stopFollowingAllInTab();
//		}
			
			
		log.info("Follow all");
		manageEventsDialog.followAllInTab();
		manageEventsDialog.closeDialog();
		
		ManageEventsDialog manageEventsDialog2 = dashboard.openManageEventsDialog();
		manageEventsDialog2.switchToTab("Opportunities");
		
		log.info("Verify that all opptys are being followed");
		if(!manageEventsDialog2.verifyFollowingAll("Opportunities")){
			manageEventsDialog2.stopFollowingAllInTab();
			Assert.assertFalse(true, "Failed to follow opportuntities.");
		}
		}
		catch(Exception e){
			log.error(e.getMessage());
			commonUserAllocator.checkInAllGroupUsersWithToken(GC.busAdminGroup, "s19282FollowAllOpportunitiesInManagedEvents");
		}
		log.info("End of test method s19282FollowAllOpportunitiesInManagedEvents");
	}
	
	@Test(groups = {"ManageEvents"},dependsOnMethods={"Test_s19282FollowAllOpportunitiesInManagedEvents"})
	public void Test_s19282UnFollowAllOpportunitiesInManagedEvents(){
		log.info("Start of test setup for Test_s19282UnFollowAllOpportunitiesInManagedEvents");
		try{
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		ManageEventsDialog manageEventsDialog = dashboard.openManageEventsDialog();
		
		manageEventsDialog.switchToTab("Opportunities");

		manageEventsDialog.stopFollowingAllInTab();
		manageEventsDialog.stopFollowingAllInTab();
		if(!manageEventsDialog.verifyFollowingNone("Opportunities")){
			Assert.assertFalse(true, "Failed to unfollow opportuntities.");
		}
		}
		finally{
			commonUserAllocator.checkInAllGroupUsersWithToken(GC.busAdminGroup, "s19282FollowAllOpportunitiesInManagedEvents");	
		}
		log.info("End of test method Test_s19282UnFollowAllOpportunitiesInManagedEvents");
	}
}