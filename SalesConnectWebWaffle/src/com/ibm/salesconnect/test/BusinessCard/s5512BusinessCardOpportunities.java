package com.ibm.salesconnect.test.BusinessCard;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Opportunity;

/*
 * Tests functionality of business cards within Connections
 * 
 * Tyler Clayton
 * 6-27-2013
 * 
 * UNSURE OF WHAT SHOULD BE DONE HERE
 */
@Test(groups = { ""})
//12/9/13 - Eva - Removing Level3 Tag as the contents of this script is covered in Level 2 script s9892BusinessCardOpportunities
public class s5512BusinessCardOpportunities extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(s5512BusinessCardOpportunities.class);

	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String opptyID = null;
	String clientID = null;
	SugarAPI sugarAPI = new SugarAPI();
	Opportunity oppt = new Opportunity();
	
	@Test
	public void testMain() {
		
		log.info("Start of test method s5512BusinessCardOpportunities");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
	
		
		try {
			//Create Contact and Oppty using API
			clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
			sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
			opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
			oppt.sOpptNumber = opptyID;
			oppt.sPrimaryContact = "ContactFirst ContactLast";
			oppt.sAccID = clientID;
			
			//Login
			Dashboard dashboard = launchWithLogin(user1);
			
			//Search for opportunity
			ViewOpportunityPage viewOpptyPage = dashboard.openViewOpportunity();
			viewOpptyPage.searchForOpportunity(oppt);
			
			//Verify Opportunity business card
			viewOpptyPage.verifyBusinessCard(oppt.sOpptNumber, user1);
			sleep(3);
			//Open Opportunity and verify all business cards
			OpportunityDetailPage opptyDetailPage = viewOpptyPage.selectResult(oppt);
			opptyDetailPage.verifyBusinessCard(user1);
			sleep(3);
			opptyDetailPage.verifyAllTeamBusinessCard();
			
			
			

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("End of test method s5512BusinessCardOpportunities");
		
		commonUserAllocator.checkInAllUsersWithToken(this);
		commonUserAllocator.checkInAllGroupUsersWithToken(GC.db2UserGroup,this);
	}
}
