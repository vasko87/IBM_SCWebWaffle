package com.ibm.salesconnect.test.examples;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;

import org.openqa.selenium.internal.Base64Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.APIUtilities;
import com.ibm.salesconnect.API.CollabWebAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.OpportunityRestAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Collab.ActivityStreamFrame;
import com.ibm.salesconnect.model.standard.Opportunity.CreateOpportunityPage;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Contact;
import com.ibm.salesconnect.objects.Opportunity;
import com.ibm.salesconnect.objects.RevenueItem;


public class Generate_Opps extends ProductBaseTest {

	Logger log = LoggerFactory.getLogger(Generate_Opps.class);
	Contact testContact;
	
	
	
	/**
	 * Generate 10 Opportunities with random IDs via GUI or edit the file to generate how ever many you want
	 */
	@Test(groups = { "GenOpps"})
	public void Test_AT_Opportunity(){
		log.info("Start of test method Test_AT_Opportunity");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);

		String contactID = "22SC-" + new Random().nextInt(99999);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		SugarAPI sugarAPI = new SugarAPI();
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");

		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		
		String[] oppNumbers = new String[10];
		
		for(int i = 0; i < 10; i++) {
			log.info("Creating Opportunity");
			Opportunity oppt = new Opportunity();
			RevenueItem rli = new RevenueItem();
			
			oppt.populate();
			rli.populate();
			oppt.sPrimaryContact = "ContactFirst" + " " + "ContactLast";
			CreateOpportunityPage createOpportunityPage = dashboard.openCreateOpportunity();
			createOpportunityPage.enterOpportunityInfo(oppt,rli);
			ViewOpportunityPage viewOpportunityPage = createOpportunityPage.saveOpportunity(oppt);
			OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
			oppt.sOpptNumber = opportunityDetailPage.getdisplayedOpportunityNumber();
			oppNumbers[i] = oppt.sOpptNumber;
		}
		commonClientAllocator.checkInAllGroupClients("SC");
		
		for(int i = 0; i < oppNumbers.length; i++) {
			System.out.println("OppNumber" + i + ": " + oppNumbers[i]);
		}
		
		log.info("End of test method Test_AT_Opportunity");
	}
	
	@Test(groups = { "GenOpps"})
	public void Test_Gen_OpptyViaAPI(){
		log.info("Start of test method Test_Gen_OpptyViaAPI");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		SugarAPI sugarAPI = new SugarAPI();
		String[] oppNumbers = new String[10];
		String URL = testConfig.getBrowserURL();
		
		for(int i = 0; i < 1; i++) {
			int rand = new Random().nextInt(99999);
			String contactID = "22SC-" + rand;
			String opptyID = "33SC-" + rand;
			String rliID = "44SC-" + rand;
			
			sugarAPI.createContact(URL, contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst_"+rand, "ContactLast_"+rand);
			sugarAPI.createRLI(URL, rliID, user1.getEmail(), user1.getPassword(),"level30","B7N40");
			sugarAPI.createOpptySOAP(URL, opptyID, rliID, contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);

			oppNumbers[i] = opptyID;
		}
		
		for(int i = 0; i < oppNumbers.length; i++) {
			System.out.println("OppNumber" + i + ": " + oppNumbers[i]);
		}
		
		log.info("End of test method Test_Gen_OpptyViaAPI");
	}
	
	@Test(groups = { "GenOpps"})
	public void Test_Gen_OpptyViaRestAPI(){
		log.info("Start of test method Test_Gen_OpptyViaAPI");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		SugarAPI sugarAPI = new SugarAPI();
		String[] oppNumbers = new String[10];
		String URL = testConfig.getBrowserURL();
		
		for(int i = 0; i < 1; i++) {
			int rand = new Random().nextInt(99999);
			String contactID = "22SC-" + rand;
			String opptyID = "33SC-" + rand;
			String rliID = "44SC-" + rand;
			
			sugarAPI.createContact(URL, contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst_"+rand, "ContactLast_"+rand);
			sugarAPI.createRLI(URL, rliID, user1.getEmail(), user1.getPassword(),"level30","B7N40");
			String[] opptyTeam = getMultipleUsers(2, this);
			opptyID = sugarAPI.createOppty(URL, rliID, contactID, clientID, user1.getEmail(), user1.getPassword(), opptyTeam);

			oppNumbers[i] = opptyID;
		}
		
		for(int i = 0; i < oppNumbers.length; i++) {
			System.out.println("OppNumber" + i + ": " + oppNumbers[i]);
		}
		
		log.info("End of test method Test_Gen_OpptyViaAPI");
	}
	
	@Test
	public void testgetclientid() throws UnsupportedOperationException, SOAPException, IOException{
		log.info("Checking that opportunity was created");
		APIUtilities apiUtilities = new APIUtilities();
		String URL = testConfig.getBrowserURL();
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		log.info("Logging in to get session ID");
		SugarAPI sugarAPI = new SugarAPI();
		String session = sugarAPI.getSessionID(URL, user1.getEmail(), user1.getPassword());
		
		log.info("Checking opportunity");

        // Create SOAP Connection
			log.info("Creating factory");
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        log.info("Creating connection");
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        log.info("Sending message");
        // Send SOAP Message to SOAP Server
        SOAPMessage soapResponse = soapConnection.call(apiUtilities.createSOAPSelectRequest(session, "DC02L0V4", "ccms_id", "Accounts"), URL+GC.soapURLExtension);
        //SOAPMessage soapResponse = soapConnection.call(apiUtilities.createSOAPSelectRequest(session, "ee487723-3db2-7df0-c12c-510e2ec79806", "Accounts"), URL+GC.soapURLExtension);
        //SOAPMessage soapResponse = soapConnection.call(apiUtilities.createSOAPSelectRequest(session, "33SC-99335", "Opportunities"), URL+GC.soapURLExtension);

        // print SOAP Response
        System.out.print("Response SOAP Message:");
        soapResponse.writeTo(System.out);
        System.out.println("/n");
        
        SOAPFactory soapFactory = SOAPFactory.newInstance();
        SOAPBody MyBody = soapResponse.getSOAPBody();
        SOAPElement e = (SOAPBodyElement) MyBody.getChildElements(soapFactory.createName("ibm_set_entryResponse","ns1","http://www.sugarcrm.com/sugarcrm")).next();
        SOAPElement f = (SOAPElement) e.getChildElements(soapFactory.createName("return")).next();
        SOAPElement g  = (SOAPElement) f.getChildElements(soapFactory.createName("record_status")).next();
        SOAPElement h  = (SOAPElement) g.getChildElements(soapFactory.createName("item")).next();
        SOAPElement i  = (SOAPElement) h.getChildElements(soapFactory.createName("return_data")).next();
        Iterator itr  = i.getChildElements(soapFactory.createName("item"));

        while (itr.hasNext()) {
        	SOAPElement k  = (SOAPElement) itr.next();
        	SOAPElement m = (SOAPElement) k.getChildElements(soapFactory.createName("name")).next();
        	if (m.getFirstChild().getNodeValue().equals("id")) {
				System.out.println((m.getNextSibling().getFirstChild().getNodeValue()));
			}
			
		}
//        SOAPElement k  = (SOAPElement) j.getChildElements(soapFactory.createName("name")).next();
//        System.out.println("/n");
//        System.out.println(k.getAttribute("value").toString());
//        System.out.println("/n");
//        System.out.println(j.toString());

        soapConnection.close();
		
	}
	
	@Test()
	public void TestGetOppty(){
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String URL = testConfig.getBrowserURL();
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(URL, user1.getEmail(), user1.getPassword());
		
		SugarAPI sugarAPI = new SugarAPI();
		if (sugarAPI.checkOpptySOAP(URL, "33SC-99335", user1.getEmail(), user1.getPassword()).equals("100")) {
		log.info("Opportunity was created successfully");
		}
		else {
			System.out.println("failed");
		}		
	}

	@Test()
	public void TestDeleteOppty(){
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String URL = testConfig.getBrowserURL();
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(URL, user1.getEmail(), user1.getPassword());
		
		SugarAPI sugarAPI = new SugarAPI();
		if (sugarAPI.deleteOpptySOAP(URL, "33SC-92379", user1.getEmail(), user1.getPassword()).equals("100")) {
		log.info("Opportunity was deleted successfully");
		}
		else {
			System.out.println("failed");
		}
		
	}
	
	@Test()
	public void TestDeleteClient(){
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		String URL = testConfig.getBrowserURL();
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(URL, user1.getEmail(), user1.getPassword());
		
		SugarAPI sugarAPI = new SugarAPI();
		if (sugarAPI.deleteSiteSOAP(URL, "55SC-72285", user1.getEmail(), user1.getPassword()).equals("100")) {
		log.info("Site was deleted successfully");
		}
		else {
			System.out.println("failed");
		}
		
	}
	
	@Test()
	public void TestDeleteContact(){
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		int rand = new Random().nextInt(99999);
		String contactID = "22SC-" + rand;
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String URL = testConfig.getBrowserURL();
		SugarAPI sugarAPI = new SugarAPI();	
		sugarAPI.createContact(URL, contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst_"+rand, "ContactLast_"+rand);
		
		if (sugarAPI.deleteContactSOAP(URL, contactID, user1.getEmail(), user1.getPassword()).equals("100")) {
			log.info("Contact was created successfully");
			}
			else {
				System.out.println("failed");
			}
			
		
	}
	
	
	@Test
	public void Test_JoinCommunityViaAPI(){
		log.info("Start of test to join commujnity via api");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		String clientID = "DC01NBFH";//commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		SugarAPI sugarAPI = new SugarAPI();
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		Base64Encoder b64e = new Base64Encoder();
		String userPass = user1.getEmail()+":"+user1.getPassword();
		
		log.info("Getting OAuthToken");
		String token = collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), "ie01@tst.ibm.com", "passw0rd");
		String headers[]={"OAuth-Token",token};
		
		log.info("Getting session ID");
		String cookieHeaders[] = {"Cookie","PHPSESSID="+ sugarAPI.getSessionID(testConfig.getBrowserURL(), "ie01@tst.ibm.com", "passw0rd")};
		String beanId=collabWebAPI.getbeanIDfromClientID(testConfig.getBrowserURL(), clientID, headers);
		String communityId=collabWebAPI.getcommunityIDFromBeanID(testConfig.getBrowserURL(), beanId,GC.accountsModule, cookieHeaders);
		
		String Connheaders[]={"Authorization","Basic "+b64e.encode(userPass.getBytes())};
		
		Boolean check = collabWebAPI.checkCommunityMembership("https://svt1ic20.rtp.raleigh.ibm.com/", communityId, Connheaders, "ie01@tst.ibm.com");
		System.out.println(check);
		Boolean check2 = collabWebAPI.checkCommunityMembership("https://svt1ic20.rtp.raleigh.ibm.com/", communityId, Connheaders, "ie02@tst.ibm.com");
		System.out.println(check2);
		Boolean check3 = collabWebAPI.joinCommunity(testConfig.getBrowserURL(), beanId, GC.accountsModule, cookieHeaders);	
		System.out.println(check3);
		Boolean check3b = collabWebAPI.checkCommunityMembership("https://svt1ic20.rtp.raleigh.ibm.com/", communityId, Connheaders, "ie01@tst.ibm.com");
		System.out.println(check3b);
		
		System.out.println("Sleeping to give time to check manually");
		sleep(30);
		log.info("Leaving Community");
		collabWebAPI.leaveCommunity("https://svt1ic20.rtp.raleigh.ibm.com/", communityId, Connheaders, "ie01@tst.ibm.com" );
		Boolean check5 = collabWebAPI.checkCommunityMembership("https://svt1ic20.rtp.raleigh.ibm.com/", communityId, Connheaders, "ie01@tst.ibm.com");
		System.out.println(check5);
	}
	
	@Test
	public void Test_ClientMark2(){
		log.info("Start of test method Test_Gen_OpptyViaAPI");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		SugarAPI sugarAPI = new SugarAPI();
		String URL = testConfig.getBrowserURL();
		Client dc = new Client();
		Client site = new Client();

		dc = sugarAPI.selectClient("DC02L0V4", URL, user1.getEmail(), user1.getPassword());
		

		site = sugarAPI.createSite(URL, dc, getMultipleUsers(3, this), user1.getEmail(), user1.getPassword());
		
		System.out.println(site.sClientID);


		log.info("End of test method Test_Gen_OpptyViaAPI");
	}
	
	@Test
	public void Test_User(){
		log.info("Start of test");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		SugarAPI sugarAPI = new SugarAPI();
		
		String id = sugarAPI.getUserID(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword());
		System.out.println("User id: " + id);

		log.info("End of test");
	}
	
	@Test
	public void TestXpath(){
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		log.info("Logging in");
		Opportunity oppt = new Opportunity();
		oppt.sOpptNumber = "33SC-299";
		String[] eventList = {"client"};
		
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
			
		log.info("Open Opportunity detail page");
		ActivityStreamFrame act = dashboard.switchToActivityStreamFrame();
		
		log.info("Verify Updates Tab - Activity Stream Displays Correctly");
		act.checkEvents(eventList);

		
		
	}
	
	
}
