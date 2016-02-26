package com.ibm.salesconnect.test.Level1;

import java.sql.SQLException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.APIUtilities;
import com.ibm.salesconnect.API.CallRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.DB2Connection;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Call.CreateCallPage;
import com.ibm.salesconnect.model.standard.Client.ClientDetailPage;
import com.ibm.salesconnect.model.standard.Client.CreateClientPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.model.standard.Contact.ContactDetailPage;
import com.ibm.salesconnect.model.standard.Contact.CreateContactPage;
import com.ibm.salesconnect.model.standard.Contact.ViewContactPage;
import com.ibm.salesconnect.model.standard.Home.UserProfilePage;
import com.ibm.salesconnect.model.standard.Lead.CreateLeadPage;
import com.ibm.salesconnect.model.standard.Lead.ViewLeadPage;
import com.ibm.salesconnect.model.standard.Note.CreateNotePage;
import com.ibm.salesconnect.model.standard.Note.NoteDetailPage;
import com.ibm.salesconnect.model.standard.Note.ViewNotePage;
import com.ibm.salesconnect.model.standard.Opportunity.CreateOpportunityPage;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Call;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Contact;
import com.ibm.salesconnect.objects.Lead;
import com.ibm.salesconnect.objects.Note;
import com.ibm.salesconnect.objects.Opportunity;
import com.ibm.salesconnect.objects.RevenueItem;

public class AT_Sugar extends ProductBaseTest {

	Logger log = LoggerFactory.getLogger(AT_Sugar.class);
	int rand = new Random().nextInt(100000);
	Client favoriteClient;
	
	
	//@Test(groups = { "Level1"})
	public void Test_AT_Sugar(){
		log.info("Start of test method AT_Sugar");
		User user1 = commonUserAllocator.getUser(this);

		Client client = new Client();
		Contact contact = new Contact();
		Opportunity oppt = new Opportunity();
		RevenueItem rli = new RevenueItem();

		client.populate();
		contact.populate();
		oppt.populate();
		rli.populate();

		try{
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);

			log.info("Creating Client");
			CreateClientPage createClientPage = dashboard.openCreateClient();
			createClientPage.enterClientInfo(client);
			createClientPage.saveClient();

			log.info("Searching for created client");
			ViewClientPage viewClientPage = dashboard.openViewClient();
			viewClientPage.searchForClient(client);
			ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);

			Assert.assertEquals(clientDetailPage.getdisplayedClientName(),client.sClientName,"Incorrect Client Detail Page was opened");

			log.info("Creating Contact");
			CreateContactPage createContactPage = dashboard.openCreateContact();
			createContactPage.enterContactInfo(contact, client);
			createContactPage.saveContact();

			log.info("Searching for created contact, will repeat until found for up to 15 minutes");
			ViewContactPage viewContactPage = dashboard.openViewContact();
			for (int i = 0; i < 48; i++) {
				log.info("Current waiting time " + Integer.toString(i-1) + " minutes" );
				Thread.sleep(20000);
				viewContactPage.searchForContact(contact);
				if(viewContactPage.checkResult(contact)){
					break;
				}
			}
			ContactDetailPage contactDetailPage = viewContactPage.selectResult(contact);
			
			Assert.assertTrue(contactDetailPage.getdisplayedContactName().contains(contact.sFirstName), "Incorrect contact detail page displayed");
			
			log.info("Adding primary contact to opportunity");
			oppt.sPrimaryContact=contact.sFirstName + " " + contact.sLastName;
			oppt.sPrimaryContactFirst=contact.sFirstName;
			oppt.sPrimaryContactLast=contact.sLastName;

			log.info("Creating Opportunity");
			CreateOpportunityPage createOpportunityPage = dashboard.openCreateOpportunity();
			createOpportunityPage.enterOpportunityInfo(oppt,rli);
			ViewOpportunityPage viewOpportunityPage = createOpportunityPage.saveOpportunity(oppt);

			log.info("Searching for created Opportunity");
			viewOpportunityPage.searchForOpportunity(oppt);
			OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppt);

			Assert.assertEquals(opportunityDetailPage.getdisplayedOpportunityNumber(),oppt.sOpptNumber,"Incorrect opportunity detail page was opened");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		log.info("End of test method AT_Sugar");
	}

	@Test(groups = { "Level1","AT_Sugar","BVT"})
	public void Test_AT_Contact() throws SQLException{
		log.info("Start of test method Test_AT_Contact");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		User db2User = commonUserAllocator.getGroupUser(GC.db2UserGroup,this);

		Contact contact = new Contact();

		PoolClient poolClient = commonClientAllocator.getGroupClient("SC");
		DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
		Client client = db2.retrieveClient(poolClient, user1.getEmail(),testConfig.getParameter(GC.testPhase));
		client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);
		client.sSearchIn= GC.showingInSiteID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForAll;

		contact.populate();

		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);

		log.info("Creating Contact");
		CreateContactPage createContactPage = dashboard.openCreateContact();
		createContactPage.enterContactInfo(contact, client);
		createContactPage.saveContact();
					
		log.info("Searching for created contact");
		ViewContactPage viewContactPage = dashboard.openViewContact();
		viewContactPage.searchForContact(contact);

		ContactDetailPage contactDetailPage = viewContactPage.selectResult(contact);

		Assert.assertEquals(contactDetailPage.getdisplayedContactName(),contact.sFirstName + " (" + contact.sPreferredName + ") " + contact.sLastName, "Incorrect contact detail page displayed");
//		Assert.assertEquals(contactDetailPage.getdisplayedContactName().contains(contact.sFirstName + " " + contact.sLastName), "Incorrect contact detail page displayed");
		commonClientAllocator.checkInAllGroupClients("SC");
		log.info("End of test method Test_AT_Contact");
	}


	//	@Test(groups = {})
	//TODO Cleanup opportunity page shenanigans
	public void Test_AT_Opportunities(){
		log.info("Start of test method Test_AT_Opportunity");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);

		String contactID = "22SC-" + new Random().nextInt(99999);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		SugarAPI sugarAPI = new SugarAPI();
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");

		int i;

		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		OpportunityDetailPage opportunityDetailPage;
		ViewOpportunityPage viewOpportunityPage;

		//generating a freaking ton of opps, OK like 1000, OK exactly 1000...that's a lot of opportunity cost...
		for(i=0; i<1000; i++) {
			Opportunity oppt = new Opportunity();
			oppt.populate();
			oppt.sPrimaryContact = "ContactFirst" + " " + "ContactLast";
			int pickedNumber = 0;

			log.info("Creating Opportunity number: " + i);
			CreateOpportunityPage createOpportunityPage = dashboard.openCreateOpportunity();

			//		if((i % 2) == 1) { //create opp with random number of RLIs between 1-5
			Random rand = new Random(); 
			pickedNumber = rand.nextInt(3) + 1;
			RevenueItem[] rliArray = new RevenueItem[pickedNumber];

			for(int a=0; a<pickedNumber; a++) { //generate list of RLIs
				RevenueItem rli = new RevenueItem();
				rli.populate();
				if(a==0) {
					rli.sFindOffering = "Tivoli";
				}
				else if(a==1) {
					rli.sFindOffering = "Sametime";
				}
				else if(a==2) {
					rli.sFindOffering = "Symphony";
				}
				else {
					rli.sFindOffering = "Notes";
				}

				rliArray[a] = rli;
			}
			createOpportunityPage.enterOpportunityInfoWithRLIs(oppt,rliArray);
			viewOpportunityPage = createOpportunityPage.saveOpportunity(oppt); //trying this with global method dec
			opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
			/*		}
			else { //create opp without an RLI
				createOpportunityPage.enterOpportunityInfo(oppt,null);
				opportunityDetailPage = createOpportunityPage.saveOpportunity();
			}
			 */		log.info("Created Opportunity " + i + " with " + pickedNumber + " RLIs");

			 Assert.assertTrue(opportunityDetailPage.isPageLoaded(), "Create Opportunity page has not loaded within 60 seconds");
			 oppt.sOpptNumber = opportunityDetailPage.getdisplayedOpportunityNumber();

			 //search for created opp
			 ViewOpportunityPage viewOpportunityPage2 = dashboard.openViewOpportunity();
			 //		exec.navigate().refresh();
			 viewOpportunityPage.searchForOpportunity(oppt);
			 opportunityDetailPage = viewOpportunityPage.selectResult(oppt);

			 Assert.assertEquals(opportunityDetailPage.getdisplayedOpportunityNumber(),oppt.sOpptNumber,"Incorrect opportunity detail page was opened");
		}
		log.info("Total Opps Created: " + i);

		commonClientAllocator.checkInAllGroupClients("SC");
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		log.info("End of test method Test_AT_Opportunity");
	}




	/**
	 * Debug method for 63704
	 */
	@Test(groups = { "Level1","AT_Sugar"})
	public void Test_AT_MultiUser(){
		log.info("Start of test method Test_AT_MultiUser");
		int loginCount = 0;

		while(commonUserAllocator.getUser(this) != null) {
			Call call = new Call();
			call.populate();
			User currentUser = commonUserAllocator.getUser(this);

			Dashboard dashboard = launchWithLogin(currentUser);

			log.info("Navigate to User Profile");
			dashboard.isPageLoaded();
			UserProfilePage userProfilePage = dashboard.openUserProfilePage();

			//confirm correct user is logged in
			Assert.assertTrue(userProfilePage.userIsLoggedIn(currentUser));
			log.info("User Logged In Count: " + loginCount++);

			log.info("Create a call");
			CreateCallPage createCallPage = dashboard.openCreateCall();
			createCallPage.enterCallInfo(call);
			createCallPage.saveCall();

			//close the browser

			exec.close();
		}

		log.info("End of test method Test_AT_MultiUser");
	}

			@Test(groups = {"APICREATETEST"})
			public void Test_API_CALL_CREATE()
			{
				log.info("Start of test method Test_APICREATETEST");
				User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
				log.info(user1.getEmail());
				Call call = new Call();
				call.populate();
				call.sAssignedToID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());
				String body = "{\"name\":\"" + call.sSubject + "\",\"date_start\":\"" + call.sCallDate + "\",\"status\":\"" + call.sCallStatus + "\",\"call_type\":\"" + call.sCallType + "\",\"duration_minutes\":\"" + call.sDuration + "\",\"assigned_user_id\":\"" + call.sAssignedToID + "\"}";
				log.info(body);
				//log.info(leadsRestAPI.createLeadPostwithDefaults(leadToContact));
				new CallRestAPI().createCallFromCall(testConfig.getBrowserURL(), new LoginRestAPI().getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword()), call);
			}

}