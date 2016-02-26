package com.ibm.salesconnect.test.Collab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.CommunityMappingRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.ConnectionsCommunityTab;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.ClientDetailPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.objects.Client;

/**
 * @author kvnlau@ie.ibm.com
 * @date Jul 07, 2015
 */

public class s62306ClientCommunityTabCreateCommunity  extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(s62306ClientCommunityTabCreateCommunity.class);
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Open browser and log in as bizadmin</li>
	 * <li>Navigate to a DC client</li>
	 * <li>Delete community (via API), if exists for client</li>
	 * <li>Add test user to client team</li>
	 * <li>Try to create community via Client's Community tab</li>
	 * <li>confirm community creation via API & UI</li>
	 * <li>Cleanup: delete community mapping</li>
	 * </ol>
	 */
	
	@Test(groups = {"BVT1"})	
	public void Test_s62306ClientCommunityTabCreateCommunity() {
		String ccmsId = null;
		String communityId = null;
		String token = null;
		String responseString = null;
		String headers[] = {null,null}; // initialize to array size = 2
		CommunityMappingRestAPI communityApi = new CommunityMappingRestAPI(); // for path extension methods
		ClientDetailPage clientDetailPage = null;
		User bizAdminUser = null;
		
		log.info("Start of test method Test_s62306ClientCommunityTabCreateCommunity()");
		try {
			// Populate variable from users.cvs, clients.csv files.
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			bizAdminUser = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			// Setup client info
			PoolClient poolClient = commonClientAllocator.getGroupClient(GC.CommunityClient,this);
			Client client = new Client();
			client.sClientID = poolClient.getCCMS_ID();	
			client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), bizAdminUser);		
			client.sCCMS_Level = "DC";
			client.sSearchIn= GC.showingInClientID;
			client.sSearchFor= GC.searchForAll;
			client.sSearchShowing=GC.showingForClients;
			
			log.info("Launch and log in");
			Dashboard dashboard = launchWithLogin(bizAdminUser);

			log.info("Navigate to Details page of client=" + client.sClientID);
			ViewClientPage viewClientPage = dashboard.openViewClient();
			if (!viewClientPage.checkResult(client)) {
				viewClientPage.isPageLoaded();
				viewClientPage.searchForClient(client);
				clientDetailPage = viewClientPage.selectResult(client);
			}
			else {
				clientDetailPage = viewClientPage.selectResult(client);
			}
			
			//------------- Test Setup ---------------------------------------
			log.info("Pull ccmsId from breadcrumb of client=" + client.sClientID);
			ccmsId = clientDetailPage.getCcmsIdFromBreadcrumbGU();
			
			log.info("Retrieving OAuth2Token.");		
			token = new LoginRestAPI().getOAuth2Token(this.getBrowserUrl(), cchFnIdUser.getEmail(), cchFnIdUser.getPassword());
			headers[0] = "OAuth-Token";
			headers[1] = token;

			// ensure no mappings exists for cmmsId -------------------------------------
			log.debug("Test setup: checking if community already exists for ccmsId=" + ccmsId);
			communityId = communityApi.getCommunityIdIfMappingExistsForCcmsId(
												this.getBrowserUrl(), token, cchFnIdUser, 
												ccmsId);
			
			log.info("ensure no mappings exists for cmmsId=" + ccmsId);
			if (communityId!=null) { 
				log.debug("Test setup: community already exists for ccmsId=" + ccmsId + " so deleting it now");
				responseString = new CommunityMappingRestAPI().deleteCommunityMapping(
											this.getBrowserUrl(), token, communityId, null,
						 					"999"); // don't care about response code
			} else {
				log.debug("Test setup: no community exists for ccmsId=" + ccmsId + " so pre-requisites completed");
			}
			
			
			log.info("Open the client team subpanel");
			clientDetailPage.isPageLoaded();
			clientDetailPage.openClientTeamSubpanel(); //FAIL if performance is slow
			//clientDetailPage.isPageLoaded();
			
			log.info("Add user " + bizAdminUser.getEmail() + " to client " + client.sClientID);
			clientDetailPage.addUserToClientTeam(bizAdminUser.getDisplayName()); //FAIL if performance is slow
			clientDetailPage.isPageLoaded();
			
			log.info("//--------------------- Begin test --------------------------------");
			log.info("Navigate to client's Community tab, where community doesn't exist");
			Assert.assertTrue(clientDetailPage.openCommunityTab(), "Could not open communityTab");
			Assert.assertTrue(clientDetailPage.isCommunityTabCreateButtonDisplayed(),"No 'Create a Community' button dislpayed");
			
			log.info("Click on Create community button if it exists");
			Assert.assertTrue(clientDetailPage.clickCommunityTabCreateCommunityButton(), "Could not click on Community Tab->Create community button");
			
			log.info("Wait for Community Tab page to load fully before verification");
			ConnectionsCommunityTab communityTab = new ConnectionsCommunityTab(exec); // Note: launches the Connection community portal
			communityTab.isPageLoaded(); // ensure focus is back on Community Tab
			
			log.info("Verify community mapping is created for this GU"); 
			communityId = communityApi.getCommunityIdIfMappingExistsForCcmsId(
											this.getBrowserUrl(), token, cchFnIdUser, 
											ccmsId);
			
			Assert.assertTrue(communityId!=null, "Community mapping was not successfully created for ccmsId=" + ccmsId);
			
			
			log.info("Verify community is created, joined & displays content in Community Tab");
			Assert.assertTrue(communityTab.checkForElement(communityTab.RecentUpdatesArea),"Recent Updates Area not found");
			Assert.assertTrue(communityTab.checkForElement(communityTab.ForumsArea),"Forums Area not found");
			Assert.assertTrue(communityTab.checkForElement(communityTab.BookmarksArea),"Bookmarks Area not found");
			Assert.assertTrue(communityTab.checkForElement(communityTab.ActivitiesArea),"Activities Area not found");
			Assert.assertTrue(communityTab.checkForElement(communityTab.WikisArea),"Wikis Area not found");
			Assert.assertTrue(communityTab.checkForElement(communityTab.RelatedCommunitiesArea),"Related Communities Area not found");
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, "test failed due to Exception");
		} finally {
			
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(
											this.getBrowserUrl(), token, communityId, null,
											 "999"); // don't care about response code
			
			log.info("End of test method: Test_s62306ClientCommunityTabCreateCommunity");
		}
		
	}		
	
}

