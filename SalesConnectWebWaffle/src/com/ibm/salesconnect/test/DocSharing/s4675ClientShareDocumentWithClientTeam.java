package com.ibm.salesconnect.test.DocSharing;

//import java.sql.SQLException;
import java.util.Random;

import org.openqa.selenium.internal.Base64Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
//import com.ibm.salesconnect.API.CollabWebAPI;
import com.ibm.salesconnect.API.CommunityMappingRestAPI;
import com.ibm.salesconnect.API.ConnectionsAPI;
import com.ibm.salesconnect.API.ConnectionsCommunityAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
//import com.ibm.salesconnect.common.DB2Connection;
import com.ibm.salesconnect.common.GC;
//import com.ibm.salesconnect.common.HttpUtils;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.DocumentsSubpanel;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.ClientDetailPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Document;


public class s4675ClientShareDocumentWithClientTeam extends ProductBaseTest{
	/**
	 * Script Name : <b>s4675VerifyClientShareDocumentWithClientTeam</b>
	 * Generated : <b>27 Jan 2012 15:01:51</b> Original Host : WinNT Version 6.1
	 *
	 * Create a client community file in SC, share it with all Client Team users, confirm users can access file in LC
	 *
	 * @since 2012/01/27
	 * @author mxu
	 */
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Logon to Salesconnect as bizadmin</li>
	 * <li>Navigate to a DC client</li>
	 * <li> Create community & map it to this DC client </li>
	 * <li>Setup: Ensure bizadmin User1 are added to Client's community membership</li>
	 * <li>Setup: Ensure bizadmin & User1 are added to client team</li>
	 * <li>bizadmin shares doc to global client team via client details page->doc subpanel</li>
	 * <li>Verify doc is now listed in client's doc subpanel</li>
	 * <li>Verify User1 can see uploaded file in community's file feed (via API)</li>
	 * <li>Logon to Salesconnect as User1</li>
	 * <li>Navigate to same DC client</li>
	 * <li>Verify doc is now listed in client's doc subpanel</li>
	 * </ol>
	 */
	
	@Test(groups = {"DocSharing","AT","BVT1"})
	public void Test_s4675ClientShareDocumentWithClientTeam() {
		Logger log = LoggerFactory.getLogger(s4675ClientShareDocumentWithClientTeam.class);
		log.info("Start of test method s4675ClientShareDocumentWithClientTeam");
		
		String contentType = "application/json";
		String responseString = null;
		String token = null;
		boolean isCCH = false;
		String ccmsId = null;
		String communityId = null;
		String headers[] = {null,null}; // initialize to array size = 2
		CommunityMappingRestAPI mappingApi = new CommunityMappingRestAPI(); // for path extension methods
		
		try {
			// Populate variable from users.cvs, clients.csv files.
			User bizAdminUser = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);		
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup,this);
			User user1 = commonUserAllocator.getUser(this);

			// Setup client info
			PoolClient poolClient = commonClientAllocator.getGroupClient(GC.CommunityClient,this);
			Client client = new Client();
			client.sClientID = poolClient.getCCMS_ID();	
			client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), bizAdminUser);		
			client.sCCMS_Level = "DC";
			client.sSearchIn= GC.showingInClientID;
			client.sSearchFor= GC.searchForAll;
			client.sSearchShowing=GC.showingForClients;

			//TESTCASE ENVIRONMENT SETUP
			log.info("Setup: prepare document that will be uploaded later");
			Document subPanelDoc = new Document();
			subPanelDoc.sDocName = "s4675_" + new Random().nextInt(100000);
			subPanelDoc.sDocLocation = testConfig.getParameter("root_folder_name");
			subPanelDoc.sDescription = "Doc sharing description " + subPanelDoc.sDocName;
			subPanelDoc.vShareWithOptions.add(GC.gsShareWithClientTeam);
			
			log.info("Creating Document " + subPanelDoc.sDocName + "...");
			Assert.assertEquals(true, createFile(subPanelDoc.sDocLocation, subPanelDoc.sDocName, "Test"), "Document " + subPanelDoc.sDocName + " not created successfully");
			
			//Navigate to Homepage, workaround for defect 11764. Should be remove after defect fixed
			log.info("Logging in as bizadmin: " + bizAdminUser.getEmail());
			Dashboard dashboard = launchWithLogin(bizAdminUser);
			
			//Navigate to provisioned Client detail page and create document from sub-panel
			ViewClientPage viewClientPage = dashboard.openViewClient();
			ClientDetailPage clientDetailPage = null;
			if (!viewClientPage.checkResult(client)) {
				viewClientPage.isPageLoaded();
				viewClientPage.searchForClient(client);
				clientDetailPage = viewClientPage.selectResult(client);
			}
			else {
				clientDetailPage = viewClientPage.selectResult(client);
			}
			
			log.info("Open the client team subpanel");
			clientDetailPage.isPageLoaded();
			clientDetailPage.openClientTeamSubpanel(); //FAIL if performance is slow
			clientDetailPage.isPageLoaded();
			
			log.info("Add user " + bizAdminUser.getEmail() + " to client " + client.sClientID);
			clientDetailPage.addUserToClientTeam(bizAdminUser.getDisplayName()); //FAIL if performance is slow
			clientDetailPage.isPageLoaded();
			
			log.info("Add user " + user1.getEmail() + " to client " + client.sClientID);
			Assert.assertTrue(clientDetailPage.addUserToClientTeam(user1.getDisplayName()), "Failed to add user" + user1.getEmail() + " to client " + client.sClientID); //FAIL if performance is slow
			clientDetailPage.isPageLoaded();
			
			log.info("Pull ccmsId from breadcrumb of client=" + client.sClientID);
			ccmsId = clientDetailPage.getCcmsIdFromBreadcrumbGU();
			
			log.info("Retrieving OAuth2Token for cchFnIdUser aka:" + cchFnIdUser.getEmail());		
			token = new LoginRestAPI().getOAuth2Token(this.getBrowserUrl(), cchFnIdUser.getEmail(), cchFnIdUser.getPassword());
			headers[0] = "OAuth-Token";
			headers[1] = token;

			// ensure no mappings exists for cmmsId -------------------------------------
			log.debug("Test setup: checking if community already exists for ccmsId=" + ccmsId);
			communityId = mappingApi.getCommunityIdIfMappingExistsForCcmsId(
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
			
			log.info("Creating community");
			ConnectionsCommunityAPI connApi = new ConnectionsCommunityAPI();
			communityId = connApi.createConnectionsCommunity(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), getCnxnCommunity());
			log.info("Created community = '" + communityId + "' with owner " + cchFnIdUser.getEmail());
			
			log.info("Ensure User1 (aka: " + user1.getEmail() + ") is added to client's community");
			log.info("Adding member: " + connApi.funcIdEmail + " to client's community"); 
			responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), connApi.funcIdEmail, communityId, getCnxnCommunity());
			log.info("Adding member: " + user1.getEmail() + " to client's community");
			responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), user1.getPassword(), user1.getEmail(), communityId, getCnxnCommunity());
			
			log.info("Create a single mapping first with ccmsId=" + ccmsId);
			responseString = mappingApi.createCommunityMappingForCcmsId(
												this.getBrowserUrl(), token, cchFnIdUser, 
												connApi.funcIdEmail, communityId, ccmsId, isCCH);
			
			
			//TESTCASE ENVIRONMENT SETUP COMPLETE
			
			//Create Doc (since we are still on the same page)
			log.info("Attempt to upload a doc to this client:" + client.sClientID);
			clientDetailPage.isPageLoaded(); // re-focus frame
			DocumentsSubpanel docSubpanel = clientDetailPage.openDocumentsSubpanel(); // PASSING NOW
			docSubpanel.enterDocumentInfo(subPanelDoc);
			docSubpanel.saveDocument(); // FAILING: even though fixed incorrect XPath for verification that saving is completed
			
			log.info("Verify that doc is uploaded to client's doc subpanel");
			docSubpanel.verifyDocumentPresent(subPanelDoc);
			exec.quit();
		
			log.info("Starting to verify that doc is shared with community in connections");
			SugarAPI sugarAPI = new SugarAPI();
			log.info("Getting cookie header");
			//String cookieHeaders[] = {"Cookie","PHPSESSID="+ sugarAPI.getSessionID(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
			
			//CollabWebAPI collabWebAPI = new CollabWebAPI();	
			log.info("User1 (aka: " + user1.getEmail() + ") authenticates into Connections via API");
			Base64Encoder b64e = new Base64Encoder();
			String userPass = user1.getEmail()+":"+user1.getPassword();
			String Connheaders[]={"Authorization","Basic "+b64e.encode(userPass.getBytes())};
			
			ConnectionsAPI connectionsAPI = new ConnectionsAPI();
			log.info("User1 (aka: " + user1.getEmail() + ") retrieving community file feed for connections community"); // FAILED access denied for bizadmin or user
			String communityFileFeed = connectionsAPI.getCommunityFileFeed(testConfig.getParameter(GC.cxnURL),Connheaders, communityId);
			
			log.info("Verify User1 (aka: " + user1.getEmail() + ") can see file in community file feed");
			Assert.assertTrue(communityFileFeed.contains(subPanelDoc.sDocName), "Document not found in community document feed");

			log.info("Logging in as User1: " + user1.getEmail());
			dashboard = launchWithLogin(user1);
			
			//Navigate to provisioned Client detail page and create document from sub-panel
			viewClientPage = dashboard.openViewClient();
			clientDetailPage = null;
			if (!viewClientPage.checkResult(client)) {
				viewClientPage.isPageLoaded();
				viewClientPage.searchForClient(client);
				clientDetailPage = viewClientPage.selectResult(client);
			}
			else {
				clientDetailPage = viewClientPage.selectResult(client);
			}		
			
			log.info("Verify User1 (aka: " + user1.getEmail() + ") can see that doc is uploaded to client's doc subpanel");
			clientDetailPage.isPageLoaded(); // re-focus frame
			docSubpanel = clientDetailPage.openDocumentsSubpanel(); // PASSING NOW
			docSubpanel.verifyDocumentPresent(subPanelDoc);
			exec.quit();			
			
			//TESTCASE COMPLETE
			commonClientAllocator.checkInAllGroupClients("DC");
			commonUserAllocator.checkInAllUsersWithToken(this);
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, "test failed due to Exception");
		} finally {		
			log.info("End of test method s4675ClientShareDocumentWithClientTeam");
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(
											this.getBrowserUrl(), token, communityId, null,
											 "999"); // don't care about response code
		}
	}
}
