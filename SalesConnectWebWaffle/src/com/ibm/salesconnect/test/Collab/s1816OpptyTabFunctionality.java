/**
 * 
 */
package com.ibm.salesconnect.test.Collab;

import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.ActivitiesSubpanel;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Collab.EmbeddedExperience;
import com.ibm.salesconnect.model.standard.Collab.OpportunityActivityStreamFrame;
import com.ibm.salesconnect.model.standard.Collab.OpportunityMicroBloggingFrame;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Call;
import com.ibm.salesconnect.objects.Opportunity;

/**
 * @author evafarrell
 * @date July 16, 2013
 */


public class s1816OpptyTabFunctionality extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(s1816OpptyTabFunctionality.class);
	
	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String opptyID = null;
	String clientID = null;
	String clientName = null;
	SugarAPI sugarAPI = new SugarAPI();
	Opportunity oppt = new Opportunity();
	
	@Test(groups = { "Collab","LC"})
	public void testMain() {
		
		log.info("Start of test method s1816OpptyTabFunctionality");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		clientName = commonClientAllocator.getGroupClient(GC.SC,this).getClientName(testConfig.getBrowserURL(), user1);

		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		oppt.sOpptNumber = opptyID;
		oppt.sPrimaryContact = "ContactFirst ContactLast";
		oppt.sAccID = clientID;

			Call call = new Call();
			call.populate();
			
			String Status = "My Status "+GC.sUniqueNum;
			
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);
				
			log.info("Open Opportunity detail page");
			ViewOpportunityPage viewOpportunityPage = dashboard.openViewOpportunity();
			viewOpportunityPage.searchForOpportunity(oppt);
			OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
			
			log.info("Verify Updates Tab - Activity Stream Displays Correctly");
			opportunityDetailPage.isPageLoaded();
			opportunityDetailPage.opptyUpdates();
			opportunityDetailPage.followOpportunity();
			Assert.assertTrue(opportunityDetailPage.verifyActivityStream(), "Activity Stream did not load successfully");
						
			log.info("Verify Updates Tab - Oppty Team Member can Post or comment");
			opportunityDetailPage.postStatus(Status);
			OpportunityMicroBloggingFrame opportunityMicroBloggingFrame = dashboard.switchToOpptyMicroBloggingFrame();
			Assert.assertTrue(opportunityMicroBloggingFrame.verifyStatus(Status), "Status does not exist in Activity Stream");
			dashboard.switchToMainWindow();
			
			log.info("Verify Updates Tab - Activity Stream - Hover over entries");
			log.info("Create a call to add an entry to the Activity Stream");
			ActivitiesSubpanel activitiesSubpanel = opportunityDetailPage.openCreateActivitiesSubPanel();
			activitiesSubpanel.moreActions();
			activitiesSubpanel.openCreateCallForm();			
			activitiesSubpanel.enterCallInfo(call);
			activitiesSubpanel.saveCall();
			log.info("Refresh Activity Stream and verify that call info shows up");
			opportunityDetailPage.scrollToTopOfOpportunity();
			OpportunityActivityStreamFrame opportunityActivityStreamFrame = dashboard.switchToOpptyActivityStreamFrame();
			opportunityActivityStreamFrame.refreshEventsStream();
			Assert.assertTrue(opportunityActivityStreamFrame.verifyEntry(call.sSubject), "Status does not exist in Activity Stream");
			
			log.info("Verify Updates Tab - Activity Stream - Expand Entries");
			log.info("Opening first event in activity stream");
			EmbeddedExperience embeddedExperience = opportunityActivityStreamFrame.openFirstEventsEmbeddedExperience();
			
			log.info("Verifying embedded experience");
			Assert.assertEquals(embeddedExperience.getEmbeddedExperienceSubject(),call.sSubject, "EE title does not match event title from Activity Stream");

			log.info("Remove the oppty created for this test");
			sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
			sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
			
		log.info("End of test method s1816OpptyTabFunctionality");
		
		commonUserAllocator.checkInAllUsersWithToken(this);
		commonUserAllocator.checkInAllGroupUsersWithToken(GC.db2UserGroup,this);
	}
}
