package com.ibm.salesconnect.test.Collab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.NotesSubpanel;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.ClientDetailPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.model.standard.Collab.ClientActivityStreamFrame;
import com.ibm.salesconnect.objects.Client;



/**
 * @author RaviSankar Panguluru
 * @date May 15th, 2015
 */


public class s62311ActivityStreamEvents  extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(S30658EmbeddedExperienceDisplayUI.class);

	@Test(groups = {"BVT","BVT1"})
	public void Test_s62311ActivityStreamEventsNote() {
		log.info("Start test method Test_s62311ActivityStreamEventsNote");
		
		//TODO:Log into UI
				log.info("Getting users");
				User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
				PoolClient poolClient = commonClientAllocator.getGroupClient("DC",this);
				Client client = new Client();
				client.sClientID = poolClient.getCCMS_ID();
				client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);		
				client.sCCMS_Level = "DC";
				client.sSearchIn= GC.showingInClientID;
				client.sSearchFor= GC.searchForAll;
				client.sSearchShowing=GC.showingForClients;
				int sRand = (int) Math.round((Math.random()) * 10000);
				String noteSubject = "NoteSubject" + sRand;
				
				log.info("Launch and log in");
				Dashboard dashboard = launchWithLogin(user1);
		
				ViewClientPage viewClientPage = dashboard.openViewClient();
				ClientDetailPage clientDetailPage;
				if (!viewClientPage.checkResult(client)) {
					viewClientPage.isPageLoaded();
					viewClientPage.searchForClient(client);
					clientDetailPage = viewClientPage.selectResult(client);
				}
				else {
					clientDetailPage = viewClientPage.selectResult(client);
				}
					
		log.info("Open Updates Tab for Client and follow Client");
		clientDetailPage.clientUpdates();
		clientDetailPage.followClient();
		NotesSubpanel notesSubpanel = clientDetailPage.opencompletedNotesAndActivitiesSubpanel();
		notesSubpanel.openCompletedActivitiesandNotesSubpanelForm();
		notesSubpanel.enterNotesInfo(noteSubject);
		notesSubpanel.saveForm();
		notesSubpanel.checkNotePresent(noteSubject);
		
		
		// Check for new event in act stream (UI) 
		//Note: will be a delay before the event appears
		
		log.info("Verify ActivityStreamEventsNote in Activity Stream");
		String event=noteSubject;
		//clientDetailPage.clientUpdates();
		ClientActivityStreamFrame clientActivityStreamFrame = dashboard.switchToClientActivityStreamFrame();
		
		Boolean found = false;
		for (int i = 0; i < 5; i++) {
			clientActivityStreamFrame.refreshEventsStream();
			
			log.info("Searching for note subject");
			if(clientActivityStreamFrame.verifyEntry(event)){
				log.info("Found event");
				found = true;
				i=5;
			}
			sleep(10);
		}
		
		if (!found) {
			Assert.assertTrue(false, "Note event was not found");
		}

		log.info("End test method Test_s62311ActivityStreamEventsNote");
	}
}
