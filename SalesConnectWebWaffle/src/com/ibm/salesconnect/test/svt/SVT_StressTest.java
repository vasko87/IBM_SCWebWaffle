package com.ibm.salesconnect.test.svt;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.common.DateTimeUtility;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.LineItemDetailPage;
import com.ibm.salesconnect.model.partials.WinPlanTab;
import com.ibm.salesconnect.model.partials.dashlets.MyOwnedOpportunities;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.CreateClientPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.model.standard.Contact.CreateContactPage;
import com.ibm.salesconnect.model.standard.Home.HomePage;
import com.ibm.salesconnect.model.standard.Opportunity.CreateOpportunityPage;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.QuickCreateOpportunityPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Contact;
import com.ibm.salesconnect.objects.Opportunity;
import com.ibm.salesconnect.objects.RevenueItem;

public class SVT_StressTest extends ProductBaseTest {

	Logger log = LoggerFactory.getLogger(SVT_StressTest.class);	

	//set to true to run a quick test run based on iTest iterations, 
	//or false to run the iterations for each task
	
	boolean bTest = false;

	//number of iterations for each task area for a quick test run
	int iTest = 1;

	//number of test iterations for each task area
	int iNumClientsToCreate			= bTest ? iTest : 0;
	int iNumOpptsToCreate 			= bTest ? iTest : 5;
	int iNumContactsToCreate 		= bTest ? iTest : 0;
	int iNumRevenuesToUpdate		= bTest ? iTest : 0;
	int iNumWinPlansToUpdate        = bTest ? iTest : 5;
	
	public static final int CREATE_CLIENT = 1;
	public static final int CREATE_CONTACT = 2;
	public static final int CREATE_OPPORTUNITY = 3;
	public static final int UPDATE_REVENUE_LINE_ITEM = 4;
	public static final int UPDATE_WINPLAN = 5;


	//path of output file for runtime stats. initialized globally, but the value is set in main,
	//after the default output path is loaded from the config file	
	String STATISTICS_FILE_PATH = "test-output\\statistics";
	String STATISTICS_FILE = STATISTICS_FILE_PATH + "\\SVT_StressTests_Stats.csv";
	
	@Test
	public void Test_SVT_StressTest() {

		log.info("Start of test method: Test_SVT_StressTest");

		try {

			//Create test-output directory
			createDir(STATISTICS_FILE_PATH);
			
			//create PrintWriter object to log the time statistics
			PrintWriter outStream = new PrintWriter(new FileWriter(STATISTICS_FILE));

			// Login User
			User user1 = commonUserAllocator.getUser(this);

			log.info("Logging in user: " + user1);

			//Capture the start time at Login
			String sRunTime = ("Login start time: " + DateTimeUtility.getDateTimeStamp() + "\n");

			//Start the timer for login
			DateTimeUtility.startTimer("Logging");
			Dashboard dashboard = launchWithLogin(user1);

			//log the time elapsed during login
			outStream.println(sRunTime + DateTimeUtility.stopTimer("Logging") + "\n");			

			outStream.flush();
			
			// Test Preparation //
			log.info("Test Preparation: ");

			//TODO REMOVE
			//updateWinPlanTest(iNumWinPlansToUpdate, dashboard, outStream, user1);
			//Assert.assertTrue(false);
			
			// Create a Client to be used in load tests
			//TODO: UNDELETE: Client baseClient = createClient(dashboard);

			Client baseClient = new Client();
			baseClient.populate();
						
			baseClient.sClientName = "55SC-58937";
					
			//baseClient.myClients = true;

			//Create a Contact to be used in load tests
			//TODO: UNDELETE: Contact baseContact = createContact(dashboard, baseClient);	
			
			Contact baseContact = new Contact();
			baseContact.populate();

			baseContact.sFirstName = "ContactFirst1402051328 ";
			baseContact.sLastName = " ContactLast1402051328";
						

			// Create an Opportunity to be used in load tests
			//Opportunity baseOpportunity = createOpportunity(dashboard, baseClient, baseContact);


			// Verify that the browser is displaying the home page before starting the tests
			dashboard.openHomePage();
			HomePage homepage = new HomePage(exec);

			// End Test Preparation //
			log.info("End Test Preparation: ");
			
			//Randomly execute a test until all tests have completed iNumberOfTimes specified above
			ArrayList<Integer> tests = new ArrayList<Integer>();
			if (iNumClientsToCreate > 0) tests.add(CREATE_CLIENT);
			if (iNumContactsToCreate > 0) tests.add(CREATE_CONTACT);
			if (iNumOpptsToCreate > 0) tests.add(CREATE_OPPORTUNITY);
			if (iNumRevenuesToUpdate > 0) tests.add(UPDATE_REVENUE_LINE_ITEM);
			if (iNumWinPlansToUpdate > 0) tests.add(UPDATE_WINPLAN);
			
			Random r = new Random();
			int j = 0;

			while (tests.size() > 0){ 
				j = r.nextInt(tests.size());
				switch(tests.get(j)){
			    case 1: 
			    	log.info(iNumClientsToCreate + " createClientTest\n");
					createClientTest(iNumClientsToCreate, dashboard, outStream);
					iNumClientsToCreate--;
					if (iNumClientsToCreate == 0) tests.remove(tests.indexOf(CREATE_CLIENT)); 
					outStream.flush();
					break;
				case 2: 
					log.info(iNumContactsToCreate + " createContactTest\n");
					createContactTest(iNumContactsToCreate, dashboard, outStream, baseClient);
					iNumContactsToCreate--;
					if (iNumContactsToCreate == 0) tests.remove(tests.indexOf(CREATE_CONTACT));
					outStream.flush();
					break;
				case 3: 
					log.info(iNumOpptsToCreate + " createOpportunityTest\n");
					createOpportunityTest(iNumOpptsToCreate, dashboard, outStream, baseClient, baseContact);
					iNumOpptsToCreate--;
					if (iNumOpptsToCreate == 0) tests.remove(tests.indexOf(CREATE_OPPORTUNITY));
					outStream.flush();
					break;
				case 4: 
					log.info(iNumRevenuesToUpdate + " updateRevenueTest\n");
					updateRevenueTest(iNumRevenuesToUpdate, dashboard, outStream);
					iNumRevenuesToUpdate--;
					if (iNumRevenuesToUpdate == 0) tests.remove(tests.indexOf(UPDATE_REVENUE_LINE_ITEM));
					outStream.flush();
					break;
				case 5: 
					log.info(iNumWinPlansToUpdate + " iNumWinPlansToUpdate\n");
					updateWinPlanTest(iNumWinPlansToUpdate, dashboard, outStream, user1);
					iNumWinPlansToUpdate--;
					if (iNumWinPlansToUpdate == 0) tests.remove(tests.indexOf(UPDATE_WINPLAN));
					outStream.flush();
					break;
				default:
					System.out.println("No Method Found");

				}
				outStream.flush();
			}
			outStream.close();

		} catch (Exception e) {
			log.error("\n*** Exception in Main: Exiting ***\n");
			e.printStackTrace();
			//System.exit(1);
		}
		
	}
	
	//Method to create the test-output folder
	public void createDir(String folder){
		
		//Directory flag set as true
		boolean dirFlag = false;

		//Create folder object
		File dir = new File(folder);

		//Flag becomes true if folder is created successfully
		try {
		   dirFlag = dir.mkdir();
		} 
		catch (SecurityException Se) {
		log.info("Error creating directory:" + Se);
		}

		if (dirFlag)
		   log.info("Directory created");
		else
		   log.info("Directory was not created");
		
	}

	public Client createClient(Dashboard dashboard)throws Exception{

		log.info("Creating Client");

		Client client = new Client();
		client.populate();

		CreateClientPage createClientPage = dashboard.openCreateClient();

		createClientPage.enterClientInfo(client);

		createClientPage.saveClient();

		ViewClientPage viewClientPage = new ViewClientPage(exec);

		return client;

	}

	public Contact createContact(Dashboard dashboard, Client client) throws Exception{

		log.info("Creating Contact");

		Contact contact = new Contact();
		contact.populate();

		log.info("Creating Contact with name: " + contact.sFirstName + " " + contact.sLastName);
		CreateContactPage createContactPage = dashboard.openCreateContact();
		createContactPage.enterContactInfo(contact, client);
		createContactPage.saveContact();

		return contact;
	}

	public Opportunity createOpportunity(Dashboard dashboard, Client client, Contact contact) throws Exception{

		log.info("Creating Oportunity");

		Opportunity opportunity = new Opportunity();
		RevenueItem rli = new RevenueItem();

		opportunity.populate();
		opportunity.sPrimaryContact = contact.sFirstName + " " + contact.sLastName;
		opportunity.sPrimaryContactFirst = contact.sFirstName;
		opportunity.sPrimaryContactLast = contact.sLastName;
		opportunity.sPrimaryContactPreferred = contact.sPreferredName;
		opportunity.sPrimaryContactWithPreferred = opportunity.sPrimaryContactFirst +" ("+opportunity.sPrimaryContactPreferred+") "+ opportunity.sPrimaryContactLast;

		log.info("Creating Opportunity with description: " + opportunity.sOpptDesc);

		rli.populate();

		log.info("Creating Opportunity");
		CreateOpportunityPage createOpportunityPage = dashboard.openCreateOpportunity();
		createOpportunityPage.enterOpportunityInfo(opportunity,rli);
		createOpportunityPage.saveOpportunity(opportunity);

		ViewOpportunityPage viewOpportunity = new ViewOpportunityPage(exec);

		return opportunity;
	}

	public Opportunity quickCreateOpportunity(Dashboard dashboard, Client client, Contact contact)throws Exception{
		log.info("Quick Create Opportunity");
		
		Opportunity opportunity = new Opportunity();
		RevenueItem rli = new RevenueItem();
		MyOwnedOpportunities oppty = new MyOwnedOpportunities(exec);

		opportunity.populate();
		opportunity.sPrimaryContact = contact.sFirstName + " " + contact.sLastName;
		opportunity.sPrimaryContactFirst = contact.sFirstName;
		opportunity.sPrimaryContactLast = contact.sLastName;
		opportunity.sPrimaryContactPreferred = contact.sPreferredName;
		opportunity.sPrimaryContactWithPreferred = opportunity.sPrimaryContactFirst +" ("+opportunity.sPrimaryContactPreferred+") "+ opportunity.sPrimaryContactLast;

		rli.populate();

		log.info("Quick Create Opportunity with description: " + opportunity.sOpptDesc);
		QuickCreateOpportunityPage quickCreateOpportunityPage = dashboard.openQuickCreateOpportunity();
		quickCreateOpportunityPage.enterOpportunityInfo(opportunity,rli);
		quickCreateOpportunityPage.saveOpportunity();
		dashboard.openHomePage();
		
		return opportunity;
	}
	
	public void updateRLI(Dashboard dashboard, int iNumOpptsToCreate)throws Exception{
		log.info("Updating RLI");
		//Loop to edit each RLI for every oppty created
		for (int i = 1; i < iNumOpptsToCreate +1; i++){
			MyOwnedOpportunities myOwnedOppty = new MyOwnedOpportunities(exec);
		
			//Navigate to an Opportunity
			OpportunityDetailPage odp = myOwnedOppty.openOpptyInMyOpenOpptysDashlet(i);
			odp.isPageLoaded();
			
			//Scroll down and edit Revenue Line Item
			LineItemDetailPage eli = odp.selectEditRli();
			
			RevenueItem rli = new RevenueItem();
			rli.sProbability = "50";
			rli.sRevenueAmount = "200";
			rli.sFindOffering = "SmartCloud for Social Business";
			eli.editLineItemInfo(rli);

			//Return to Homepage after saving edited line item
			dashboard.openHomePage();
		}
	}
	
	public void updateWinPlan(Dashboard dashboard, int iNumOpptsToCreate, User user)throws Exception{
		log.info("Updating WinPlan");
		
		String userName = user.getDisplayName();
		
		//Loop to update each wp for every oppty created
		for (int i = 1; i < iNumOpptsToCreate +1; i++){
			MyOwnedOpportunities oppty = new MyOwnedOpportunities(exec);
			
			//Navigate to an Opportunity
			OpportunityDetailPage odp = oppty.openOpptyInMyOpenOpptysDashlet(i);
			odp.isPageLoaded();
			
			//Edit the wp
			odp.openWinPlanTab();
			
			WinPlanTab wpt = new WinPlanTab(exec);
			
			//Edit Summary
			wpt.addIBMBrandSponsor(userName);
			wpt.addIBMSDSponsor(userName);
			wpt.setOwnerIGF(true);
			wpt.setInternationalDeal(true);
			wpt.setIGFOwnerIGF(true);		
			wpt.saveSummary();
			
			//Randomized selection of which WinPlan section to be completed
			int random = (int)(Math.random()*4+1);
			//switch (random)
			switch (1)
			{
				case 1:
					//Edit IV
					wpt.openIVSection();
					wpt.setClientNeeds();
					wpt.setSOW();
					wpt.setUniqueBusValue("high");
					wpt.setUniqueBusValueDesc("Better functionality proven IBM solution");
					wpt.populateIVOtherComments("IV comment");
					wpt.saveIV();
					//break;
				case 2:
					//Edit Validating and Qualifying
					wpt.openVQSection();
					wpt.populateVQOpptyOwnerSection(userName);
					wpt.saveVQ();
					//break;
				case 3:
					//Edit QGCA
					wpt.openQGCPSection();
					wpt.populateQGCP();
					wpt.saveQGCP();
					//break;
				case 4:
					//Edit CAW
					wpt.openCAWSection();
					wpt.populateCAW();
					wpt.saveCAW();
					//break;
			}
			
			//Return to the homepage
			dashboard.openHomePage();
		}
	}

	private void createClientTest(int iNumToCreate, Dashboard dashboard, PrintWriter outStream)
	{
		/****************Start Test: Create Client****************/
		log.info("Start of new task area: Create Clients");

		for (int i = 0; i < iNumToCreate; i++)
		{
			try {
				
				String sRunTime = "Create client: " + i +" [create client], [return to home page]\n" +
				"Start time: " + DateTimeUtility.getDateTimeStamp() + " ";
				
				log.info("Creating Client Iteration: " + i);
				DateTimeUtility.startTimer("createClient");

				createClient(dashboard);

				//log time elapsed while creating opportunity
				sRunTime += DateTimeUtility.stopTimer("createClient") + ", ";

				//return to home page and log the time elapsed
				DateTimeUtility.startTimer("home");
				dashboard.openHomePage();
				HomePage homepage = new HomePage(exec);
				homepage.isPageLoaded();
				sRunTime += DateTimeUtility.stopTimer("home") + "\n";

				//send the statistics to the output file stream
				outStream.println(sRunTime);
				outStream.flush();
				
				// Reset the timers
				DateTimeUtility.clearTimer("createClient");
				DateTimeUtility.clearTimer("home");


			} catch (Exception e) {
				outStream.println("Create Client Failed: Exiting");
				outStream.flush();
				log.error("Exception in loop " +  e);
				//reset the browser to the home page after a failure
				dashboard.openHomePage();
				HomePage homepage = new HomePage(exec);
				homepage.isPageLoaded();				
			} finally {
			}
		}
	}

	/**
	 * Creates iNumToCreate contacts with first name "SVTFirst<epoch time at time of creation>"
	 * and last name "SVTLast<epoch time at time of creation>"
	 * Alternates between creating with long and short form
	 * 
	 * @param iNumToCreate
	 */
	private void createContactTest(int iNumToCreate, Dashboard dashboard, PrintWriter outStream, Client baseClient)
	{
		/****************Start Test: Create Contact****************/
		log.info("Start of new task area: Create Contacts");



		for (int i = 0; i < iNumToCreate; i++)
		{
			try {
				
				String sRunTime = "Create contact:  " + i +" [create contact], [return to home page]\n" +
				"Start time: " + DateTimeUtility.getDateTimeStamp() + " ";

				log.info("Creating Contact Iteration: " + i);

				DateTimeUtility.startTimer("createContact");

				createContact(dashboard, baseClient);

				//log time elapsed while creating opportunity
				sRunTime += DateTimeUtility.stopTimer("createContact") + ", ";

				//return to home page and log the time elapsed
				DateTimeUtility.startTimer("home");
				dashboard.openHomePage();
				HomePage homepage = new HomePage(exec);
				homepage.isPageLoaded();					
				sRunTime += DateTimeUtility.stopTimer("home") + "\n";

				//send the statistics to the output file stream
				outStream.println(sRunTime);
				outStream.flush();
				
				// Reset the timers
				DateTimeUtility.clearTimer("createContact");
				DateTimeUtility.clearTimer("home");
				

			} catch (Exception e) {
				outStream.println("Create Contact Failed: Exiting");
				outStream.flush();
				log.error("Exception in loop " +  e);
				//reset the browser to the home page after a failure
				dashboard.openHomePage();
				HomePage homepage = new HomePage(exec);
				homepage.isPageLoaded();				
			} finally {
			}
		}
	}


	/**
	 * Creates iNumToCreate opportunities with the description "SVT Opportunity <epoch time at creation>"
	 * The search alternates between creating with long and short form
	 * 
	 * @param iNumToCreate The number of opportunities to create
	 */
	private void createOpportunityTest(int iNumToCreate, Dashboard dashboard, PrintWriter outStream, Client baseClient, Contact baseContact)
	{
		/****************Start Test: Create Opportunities****************/
		log.info("Start of new task area: Create opportunities");

		for (int i = 0; i < iNumToCreate; i++)
		{
			try {
				String sRunTime = "Create opportunity: " + i +" [create opportunity], [return to home page]\n" +
				"Start time: " + DateTimeUtility.getDateTimeStamp() + " ";
				
				log.info("Creating Opportunity Iteration: " + i);

				DateTimeUtility.startTimer("createOpportunity");

				//alternates between creating with short and long form
				boolean quickCreate = (i % 2 == 1);
				//boolean quickCreate = true;

				if (quickCreate) {
					quickCreateOpportunity(dashboard, baseClient, baseContact);
				} else {
					createOpportunity(dashboard, baseClient, baseContact);
				}

				//log time elapsed while creating opportunity
				sRunTime += DateTimeUtility.stopTimer("createOpportunity") + ", ";

				//return to home page and log the time elapsed
				DateTimeUtility.startTimer("home");
				dashboard.openHomePage();
				HomePage homepage = new HomePage(exec);
				homepage.isPageLoaded();
				sRunTime += DateTimeUtility.stopTimer("home") + "\n";

				//send the statistics to the output file stream
				outStream.println(sRunTime);
				outStream.flush();
				
				// Reset the timers
				DateTimeUtility.clearTimer("createOpportunity");
				DateTimeUtility.clearTimer("home");

			} catch (Exception e) {
				outStream.println("Create Opportinity Failed: Exiting");
				outStream.flush();
				log.error("Exception in Create Opportunity Loop" +  e);
				//reset the browser to the home page after a failure
				dashboard.openHomePage();
				HomePage homepage = new HomePage(exec);
				homepage.isPageLoaded();				
			} finally {
			}
		}
	}
	
	private void updateRevenueTest(int iNumToUpdate, Dashboard dashboard, PrintWriter outStream)
	{
		/****************Start Test: Update Revenue Line Item****************/
		log.info("Start of new task area: Update Revenue Line Items");

		for (int i = 0; i < iNumToUpdate; i++)
		{
			try {
				
				String sRunTime = "Update RLI: " + i +" [update rli], [return to home page]\n" +
				"Start time: " + DateTimeUtility.getDateTimeStamp() + " ";
				
				log.info("Updating RLI Iteration: " + i);
				DateTimeUtility.startTimer("updateRLI");
	
				updateRLI(dashboard, iNumOpptsToCreate);

				//log time elapsed while creating opportunity
				sRunTime += DateTimeUtility.stopTimer("updateRLI") + ", ";

				//return to home page and log the time elapsed
				DateTimeUtility.startTimer("home");
				dashboard.openHomePage();
				HomePage homepage = new HomePage(exec);
				homepage.isPageLoaded();
				sRunTime += DateTimeUtility.stopTimer("home") + "\n";

				//send the statistics to the output file stream
				outStream.println(sRunTime);
				outStream.flush();
		
				// Reset the timers
				DateTimeUtility.clearTimer("updateRLI");
				DateTimeUtility.clearTimer("home");


			} catch (Exception e) {
				outStream.println("Update RLI Failed: Exiting");
				outStream.flush();
				log.error("Exception in loop " +  e);
				//reset the browser to the home page after a failure
				dashboard.openHomePage();
				HomePage homepage = new HomePage(exec);
				homepage.isPageLoaded();				
			} finally {
			}
		}
	}
	private void updateWinPlanTest(int iNumToUpdate, Dashboard dashboard, PrintWriter outStream, User user)
	{
		/****************Start Test: Update Revenue Line Item****************/
		log.info("Start of new task area: Update WinPlan");

		for (int i = 0; i < iNumToUpdate; i++)
		{
			try {
				
				String sRunTime = "Update WinPlan: " + i +" [update winplan], [return to home page]\n" +
				"Start time: " + DateTimeUtility.getDateTimeStamp() + " ";
				
				log.info("Updating WinPlan Iteration: " + i);
				DateTimeUtility.startTimer("updateWinPlan");
	
				updateWinPlan(dashboard, iNumOpptsToCreate, user);

				//log time elapsed while creating opportunity
				sRunTime += DateTimeUtility.stopTimer("updateWinPlan") + ", ";

				//return to home page and log the time elapsed
				//DateTimeUtility.startTimer("home");
				//dashboard.openHomePage();
				//HomePage homepage = new HomePage(exec);
				//homepage.isPageLoaded();
				//sRunTime += DateTimeUtility.stopTimer("home") + "\n";

				//send the statistics to the output file stream
				outStream.println(sRunTime);
				outStream.flush();
		
				// Reset the timers
				DateTimeUtility.clearTimer("updateWinPlan");
				DateTimeUtility.clearTimer("home");


			} catch (Exception e) {
				outStream.println("Update WinPlan Failed: Exiting");
				outStream.flush();
				log.error("Exception in loop " +  e);
				//reset the browser to the home page after a failure
				dashboard.openHomePage();
				HomePage homepage = new HomePage(exec);
				homepage.isPageLoaded();				
			} finally {
			}
		}
	}
	
}