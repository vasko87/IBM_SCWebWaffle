package com.ibm.salesconnect.test.Collab;

//import java.util.ArrayList;

//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
//import org.testng.annotations.Optional;
//import org.testng.annotations.Parameters;
//import org.testng.annotations.Test;
import com.ibm.atmn.waffle.extensions.user.User;
//import com.ibm.salesconnect.API.ApiBaseTest;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
//import com.ibm.salesconnect.API.ConnectionsCommunityAPI;
//import com.ibm.salesconnect.API.CommunityMappingRestAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
//import com.ibm.salesconnect.model.partials.OpportunitySubpanel;
//import com.ibm.salesconnect.common.HttpUtils;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.ClientDetailPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.objects.Client;

/**
 * @author kvnlau@ie.ibm.com
 * @date Jul 07, 2015
 */

public class s37932CommunityTabVisibleForNonTeamMembers  extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(s37932CommunityTabVisibleForNonTeamMembers.class);
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Open browser and logon as biz admin</li>
	 * <li>Navigate to client from client pool</li>
	 * <li>Ensure test user is removed from client's team</li>
	 * <li>Ensure test user is removed from all child oppty teams </li>
	 * <li>Re-logon as test user</li>
	 * <li>Navigate to same client</li>
	 * <li>Verify non-member test user has no access to Community tab content</li>
	 * </ol>
	 */
	
	//@Test(groups = {"BVT"})	
	/*
	 * As discussed with Tim Lehane, this test is not suitable for automation since 
	 * since the spec has changed for the community mapping to include the entire GUC. 
	 * So as long as user is a member of any child within GUC the user will have access 
	 * to the community for any child within the GUC, regardless if user is member of 
	 * client/oppty team or not.
	 */
	public void Test_s37932CommunityTabVisibleForNonTeamMembers() {

		String searchType = null;
		
		log.info("Start of test method Test_s37932CommunityTabVisibleForNonTeamMembers()");
		try {
			// Populate variable from users.cvs, clients.csv files.
			User bizAdminUser = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			User user = commonUserAllocator.getGroupUser(GC.noMemUserGroup, this);
			
			PoolClient poolClient = commonClientAllocator.getGroupClient("DC",this);
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
			ClientDetailPage clientDetailPage = null;
			viewClientPage.isPageLoaded();

			log.info("Navigate to Details page of client=" + client.sClientID);
			dashboard.openViewClient();
			if (!viewClientPage.checkResult(client)) {
				viewClientPage.isPageLoaded();
				viewClientPage.searchForClient(client); // 
				clientDetailPage = viewClientPage.selectResult(client);
			}
			else {
				clientDetailPage = viewClientPage.selectResult(client);
			}

			log.info("Open the client team subpanel");
			clientDetailPage.openClientTeamSubpanel();
			clientDetailPage.isPageLoaded();
			
			// DO NOT REMOVE THE BIZ ADMIN, OR CREATE NEW GROUP INSTEAD.
			log.info("Ensure that test user " + user.getEmail() + " is removed from client team");
			clientDetailPage.removeUserFromClientTeam(user.getDisplayName()); 
								
			log.info("==================== Logout============================");
			exec.quit();
			
			log.info("Logon as " + user.getEmail() + " in separate browser as userB & navigate to same client Update tab");
			launchWithLogin(user);
			
			log.info("Navigate to Details page of client=" + client.sClientID);
			dashboard.openViewClient();
			if (!viewClientPage.checkResult(client)) {
				viewClientPage.isPageLoaded();
				viewClientPage.searchForClient(client); // 
				clientDetailPage = viewClientPage.selectResult(client);
			}
			else {
				clientDetailPage = viewClientPage.selectResult(client);
			}	
			log.info("//--------------------- Begin test --------------------------------");			
			log.info("Verify that community tab exists for non team members of client also");
			clientDetailPage.isPageLoaded();
			Assert.assertTrue(clientDetailPage.isCommunityTabDisplayed(), "Non members of Client should see, but was not shown Community Tab");
			
			log.info("Navigate to Client's Community tab");
			Assert.assertTrue(clientDetailPage.openCommunityTab(), "Failed to open community tab"); // FAILED
			
			log.info("Verify message displayed, indicates that user has no access to this tab");
			Assert.assertFalse(clientDetailPage.isClientMemberCommunityTab(),
								"user who is not member of client, incorrectly has access to commmunity tab content");

		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, "test failed due to Exception");
		} finally {
			log.info("End of test method Test_s37932CommunityTabVisibleForNonTeamMembers()");
		}
	}		
	
}

