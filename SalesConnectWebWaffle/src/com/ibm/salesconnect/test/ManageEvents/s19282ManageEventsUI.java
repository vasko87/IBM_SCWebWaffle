package com.ibm.salesconnect.test.ManageEvents;

/**
 * 
 */


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
 * @author timlehane
 * @date Jul 24, 2013
 */
/**
 * <strong>Description:</strong>
 * <br/><br/>
 * Test to validate the Manage Events Tabs like Client and Oppty tabs*
 * <br/><br/>
 * 
 * @author 
 * Ramesh Rangoju 
 * */

public class s19282ManageEventsUI extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(s19282ManageEventsUI.class);
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Open browser and log in</li>
	 * <li>Click on Manage Events</li>
	 * <li>Verify Clients and Oppty tabs</li>
	  * </ol>
	 **/
	@Test(groups = {"BVT","BVT1"})
	public void Test_s19282ManageEventsUI() {
		log.info("Start test method Test_s19282ManageEventsUI");
		
		log.info("Getting users");
		User user1 = commonUserAllocator.getUser(this);
		
		log.info("Launch and log in");
		Dashboard dashboard = launchWithLogin(user1);
		
		log.info("Open Manage Events dialog");
		ManageEventsDialog manageEventsDialog = dashboard.openManageEventsDialog();
		
		//There are no results to display in Manage Events at the moment so there is no paging
		//log.info("Checking paging");
		//Assert.assertTrue(manageEventsDialog.checkPagingFormat(),"Paging does not follow correct format");
				
		log.info("Verify tabs and sub tabs");
		Assert.assertTrue(manageEventsDialog.isTabPresent(GC.Clients), "Client tab is not present");
		Assert.assertTrue(manageEventsDialog.isTabPresent(GC.Opportunities), "Opportunities tab is not present");
		
		Assert.assertTrue(manageEventsDialog.isSubTabPresent(GC.ClientsImFollowing), "All clients that I am following sub tab is not present");
		Assert.assertTrue(manageEventsDialog.isSubTabPresent(GC.ClientsImNotFollowing), "My clients that I am not following sub tab is not present");
		
		manageEventsDialog.switchToTab(GC.Opportunities);
		Assert.assertTrue(manageEventsDialog.isSubTabPresent(GC.OpportunitiesImFollowing), "All opportunities that I am following sub tab is not present");
		Assert.assertTrue(manageEventsDialog.isSubTabPresent(GC.OpportunitiesImNotFollowing), "Opportunities that I am not following sub tab is not present");
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}

}
