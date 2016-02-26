package com.ibm.salesconnect.test.Collab;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.OpportunitySubpanel;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.ClientDetailPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.model.standard.Collab.ClientActivityStreamFrame;
import com.ibm.salesconnect.model.standard.Collab.EmbeddedExperience;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Opportunity;
import com.ibm.salesconnect.objects.RevenueItem;

/**
 * @author evafarrell
 * @date Mar 4, 2014
 */


public class s39141EEOpptyinClientActivityStreamEvents extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(s39141EEOpptyinClientActivityStreamEvents.class);
		
	@Test
	public void test_s39141EEOpptyinClientActivityStreamEvents() {
		
		log.info("Start of test method s39141EEOpptyinClientActivityStreamEvents");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		PoolClient poolClient = commonClientAllocator.getGroupClient("DC",this);
		Client client = new Client();
		client.sClientID = poolClient.getCCMS_ID();
		client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);		
		client.sCCMS_Level = "DC";
		
		SugarAPI sugarAPI = new SugarAPI();
		int rand = new Random().nextInt(100000);
		String contactID = "22SC-" + rand;
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, client.sClientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");

		RevenueItem rli = new RevenueItem();
		rli.populate();
		
		Opportunity oppt = new Opportunity();
		oppt.populate();
		oppt.sPrimaryContactFirst = "ContactFirst";
		oppt.sPrimaryContactLast = "ContactLast";
	
		//Client variables
		client.sSearchIn= GC.showingInClientID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForClients;
		
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);

			log.info("Open Client detail page");
			ViewClientPage viewClientPage = dashboard.openViewClient();
			viewClientPage.searchForClient(client);
			ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);
						
			log.info("Open Updates Tab for Client and follow Client");
			clientDetailPage.clientUpdates();
			clientDetailPage.followClient();
			
			log.info("Create Opportunity from Subpanel");
			OpportunitySubpanel opportunitySubpanel = clientDetailPage.openOpportunitySubpanel();
			opportunitySubpanel.openCreateOpportunityForm();
			opportunitySubpanel.enterOpportunityInfo(oppt, rli);
			opportunitySubpanel.saveOpportunity();
			oppt.sOpptNumber = clientDetailPage.getdisplayedOpportunityNumber();
			log.info("Opportunity Number: "+oppt.sOpptNumber);
			
			log.info("Verify Opportunity Event in Activity Stream");
			String event = user1.getDisplayName()+" created the opportunity "+ oppt.sOpptNumber;
			String embeddedExpEvent = oppt.sOpptNumber;
			clientDetailPage.clientUpdates();
			ClientActivityStreamFrame clientActivityStreamFrame = dashboard.switchToClientActivityStreamFrame();
			clientActivityStreamFrame.refreshEventsStream();
			Assert.assertTrue(clientActivityStreamFrame.verifyEntry(event), "Event "+event+" does not exist in Activity Stream");
			log.info("Verify Updates Tab - Activity Stream - Expand Entries");
			log.info("Opening first event in activity stream");
			EmbeddedExperience embeddedExperience = clientActivityStreamFrame.openFirstEventsEmbeddedExperience();
			log.info("Verifying embedded experience");
			Assert.assertEquals(embeddedExperience.getEmbeddedExperienceSubject(),embeddedExpEvent, "EE title does not match event title from Activity Stream");

		log.info("End of test method s39141EEOpptyinClientActivityStreamEvents");
		
		commonUserAllocator.checkInAllUsersWithToken(this);
		commonUserAllocator.checkInAllGroupUsersWithToken(GC.db2UserGroup,this);
	}
}
