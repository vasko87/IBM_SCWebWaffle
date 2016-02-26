/**
 * 
 */
package com.ibm.salesconnect.test.Collab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Collab.ActivityStreamFrame;
import com.ibm.salesconnect.model.standard.Collab.EmbeddedExperience;

/**
 * @author timlehane
 * @date Jul 3, 2013
 */
public class S30658EmbeddedExperienceDisplayUI extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(S30658EmbeddedExperienceDisplayUI.class);

	@Test(groups = {"LC", "Collab","BVT1"})
	public void Test_S30658EmbeddedExperienceDisplayUI() {
		log.info("Start test method Test_S30658EmbeddedExperienceDisplayUI");
		
		log.info("Getting users");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		
		log.info("Launch and log in");
		Dashboard dashboard = launchWithLogin(user1);
		
		ActivityStreamFrame activityStreamFrame = dashboard.switchToActivityStreamFrame();
		
		log.info("Opening first event in activity stream");
		EmbeddedExperience embeddedExperience = activityStreamFrame.openFirstEventsEmbeddedExperience();
		
		log.info("Verifying embedded experience");
		Assert.assertTrue(embeddedExperience.verifyOpened(),"ee_container div is not present, embedded experience has not opened");	
		Assert.assertTrue(activityStreamFrame.getEventTitle().contains(embeddedExperience.getEmbeddedExperienceValue("Description:")),"Event desctiption incorrect");
		Assert.assertTrue(embeddedExperience.verifyUnderlay(true),"Underlay not present when it should be");
		
		embeddedExperience.close();
		embeddedExperience.switchToMainWindow();
		embeddedExperience.switchToBWCFrame();
		
		Assert.assertTrue(embeddedExperience.verifyUnderlay(false),"Underlay present when it should not be");
		
		log.info("End test method Test_S30658EmbeddedExperienceDisplayUI");
	}

}
