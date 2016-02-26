package com.ibm.salesconnect.test.Search;

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
import com.ibm.salesconnect.model.partials.ContactSubpanel;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.ClientDetailPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Opportunity;

/**
 * @author evafarrell
 * @date Sept 20, 2013
 */


public class s29860ExternalSearchDisabledForContacts extends ProductBaseTest {
	
	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String opptyID = "33SC-" + rand;
	String clientID = null;
	SugarAPI sugarAPI = new SugarAPI();
	Opportunity oppt = new Opportunity();

	@Test(groups = { "Search"})
	public void Test_s29860ExternalSearchDisabledForContacts() {
		Logger log = LoggerFactory.getLogger(s29860ExternalSearchDisabledForContacts.class);
		log.info("Start of test method s29860ExternalSearchDisabledForContacts");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);

		PoolClient poolClient = commonClientAllocator.getGroupClient("DC",this);
		Client client = new Client();
		client.sClientID = poolClient.getCCMS_ID();
		client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);		
		client.sCCMS_Level = "DC";
		
		//Client variables
		client.sSearchIn= GC.showingInClientID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForClients;
		
		try {

			log.info("Logging in to SalesConnect");
			Dashboard dashboard = launchWithLogin(user1);
			
			log.info("Open Client detail page");
			ViewClientPage viewClientPage = dashboard.openViewClient();
			viewClientPage.searchForClient(client);
			viewClientPage.isPageLoaded();
			ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);
						
			log.info("In Contacts Subpanel, open the drop-down menu beside the 'Create contact' button.");
			ContactSubpanel contactSubpanel = clientDetailPage.openContactsSubpanel();
			
			log.info("Verify that 'Search external sites' option is no longer displayed, leaving only 'Select' option.");
			Assert.assertTrue(contactSubpanel.verifyContactsMoreActions(),"Incorrect More Actions Options Found");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("End of test method s29860ExternalSearchDisabledForContacts");
		
		commonUserAllocator.checkInAllUsersWithToken(this);
		commonUserAllocator.checkInAllGroupUsersWithToken(GC.db2UserGroup,this);
	}
}
