package com.ibm.salesconnect.test.Level0;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Collab.ActivityStreamFrame;

/**
 * <strong>Description:</strong>
 * <br/><br/>
 * Test to validate the appearance of the activity stream on the home page, only checks if it is appearing not that new events are being handled correctly
 * <br/><br/>
 * 
 * @author 
 * Tim Lehane
 * 

 */
public class AT_ActivityStream extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(AT_ActivityStream.class);
	
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Open browser and log in</li>
	 * <li>Verify that the activity stream has loaded by checking for either an event or the no updates available message
	 * </ol>
	 */
	@Test(groups = {"BVT0"})
	public void Test_AT_ActivityStream() {
		log.info("Start test method Test_AT_ActivityStream");

		log.info("Getting users");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);

		log.info("Launch and log in");
		Dashboard dashboard = launchWithLogin(user1);

		
		log.info("Switch to updates Act stream");
		ActivityStreamFrame actframe = dashboard.switchToActivityStreamFrame();

		actframe.checkEventsVisible();

		log.info("End test method Test_AT_ActivityStream");
	}
}
