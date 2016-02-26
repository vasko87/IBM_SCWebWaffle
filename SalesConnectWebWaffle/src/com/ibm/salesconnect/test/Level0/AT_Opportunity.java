package com.ibm.salesconnect.test.Level0;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.OpportunityRestAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Opportunity.CreateOpportunityPage;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Opportunity;
import com.ibm.salesconnect.objects.RevenueItem;

/**
 * <strong>Description:</strong>
 * <br/><br/>
 * Test to validate the create, read update and delete functionality of the Opportunity module
 * <br/><br/>
 * 
 * @author 
 * Tim Lehane
 * 
 */
public class AT_Opportunity extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(AT_Opportunity.class);
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Open browser and log in</li>
	 * <li>Create Opportunity</li>
	 * <li>Search for created Opportunity</li>
	 * <li>Edit the opportunity description</li>
	 * <li>Confirm description edited</li>
	 * <li>Delete the opportunity</li>
	 * <li>Confirm that the opportunity was deleted by api call</li>
	 * </ol>
	 */	
	@Test(groups = { "Level1","AT_Sugar","AT","BVT","BVT0"})
	public void Test_AT_Opportunity(){
		log.info("Start of test method Test_AT_Opportunity");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);

		Opportunity oppt = new Opportunity();
		RevenueItem rli = new RevenueItem();

		oppt.populate();
		rli.populate();
		String Random = String.valueOf(new Random().nextInt(99999));
		String contactID = "22SC-" + Random;
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		SugarAPI sugarAPI = new SugarAPI();
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst" + Random, "ContactLast" + Random);

		oppt.sPrimaryContact = "ContactFirst" + Random + " " + "ContactLast" + Random;
		//oppt.sClient= clientID;

		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);

		log.info("Creating Opportunity");
		CreateOpportunityPage createOpportunityPage = dashboard.openCreateOpportunity();
		createOpportunityPage.enterOpportunityInfo(oppt,rli);
		ViewOpportunityPage viewOpportunityPage = createOpportunityPage.saveOpportunity(oppt);
		System.out.println(oppt.getOpptyNumber());
		

		log.info("Searching for created Opportunity");
		viewOpportunityPage.searchForOpportunity(oppt);
		viewOpportunityPage.isPageLoaded();
		OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResultfromID(oppt);
		Assert.assertEquals(opportunityDetailPage.getDisplayedOpportunityDescription(),oppt.getOpptyDesc(),"Incorrect opportunity detail page was opened");
		
		oppt.setOpptyDesc(oppt.sOpptDesc + "Edit");
		log.info("Editing opportunity");
		CreateOpportunityPage editOpportunityPage= opportunityDetailPage.openEditPage();
		editOpportunityPage.editOpptyDescription(oppt);
		createOpportunityPage.saveEditedOpportunity(oppt);
		
		log.info("Searching for edited Note ");	
		sleep(10);
		Assert.assertEquals(opportunityDetailPage.getDisplayedOpportunityDescription(),oppt.getOpptyDesc(), "Subject value on page does not equal the edited value");

		opportunityDetailPage.deleteItem();
		opportunityDetailPage.confirmDelete();
		
		log.info("Confirming opportunity has been deleted via API");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword());
		
		OpportunityRestAPI opptyRestAPI = new OpportunityRestAPI();
		opptyRestAPI.getOpportunity(testConfig.getBrowserURL(), token, oppt.getOpptyNumber(), "404");

		log.info("End of test method Test_AT_Opportunity");
	}
}
