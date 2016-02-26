package com.ibm.salesconnect.test.Level0;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.appium.model.Task.CreateTaskPage;
import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.CallRestAPI;
import com.ibm.salesconnect.API.LeadsRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Call.CallDetailPage;
import com.ibm.salesconnect.model.standard.Call.CreateCallPage;
import com.ibm.salesconnect.model.standard.Call.ViewCallPage;
import com.ibm.salesconnect.model.standard.Home.UserProfilePage;
import com.ibm.salesconnect.model.standard.Lead.CreateLeadPage;
import com.ibm.salesconnect.model.standard.Lead.LeadDetailPage;
import com.ibm.salesconnect.model.standard.Lead.ViewLeadPage;
import com.ibm.salesconnect.objects.Lead;

/**
 * <strong>Description:</strong>
 * <br/><br/>
 * Test to validate the create, read update and delete functionality of the Leads module
 * <br/><br/>
 * 
 * @author 
 * Tim Lehane
 * 
 */
public class AT_Lead extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(AT_Lead.class);

	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Open browser and log in</li>
	 * <li>Create Lead</li>
	 * <li>Search for created Lead</li>
	 * <li>Edit the lead</li>
	 * <li>Confirm that the lead has been edited</li>
	 * <li>Delete the Lead</li>
	 * <li>Confirm that the lead has been deleted</li>
	 * </ol>
	 */	
	@Test(groups = { "Level1","AT_Sugar","BVT", "BVT0"})
	public void Test_AT_Lead(){
		log.info("Start of test method Test_AT_Lead");

		Lead lead = new Lead();
		lead.populate();
		//leadToContact=lead;
		User user1 = commonUserAllocator.getUser(this);
		Dashboard dashboard = launchWithLogin(user1);

		log.info("Check for Leads tab");
		exec.switchToFrame().returnToTopFrame();
		if(!dashboard.checkForElement(dashboard.leadListItem))
		{
			log.info("Activating leads tab");
			UserProfilePage userProfilePage = dashboard.openUserProfilePage();
			userProfilePage.enableLeads();
			dashboard = userProfilePage.returnToDash();
		}
		
		log.info("Create a Lead");
		CreateLeadPage createLeadPage = dashboard.openCreateLead();
		createLeadPage.enterLeadInfo(lead);
		ViewLeadPage viewLeadPage = createLeadPage.saveLeadOnLeadViewPage();

		log.info("Searching for created Lead");	
		sleep(10);
		LeadDetailPage leadDetailPage = viewLeadPage.checkLead(lead);
		String leadID = leadDetailPage.getLeadID(); 
		
		log.info("Editing lead");
		CreateLeadPage editLeadPage = leadDetailPage.openEditPage();
		lead.setEmailID("edited@email.com");
		editLeadPage.editEmail("edited@email.com");
		createLeadPage.saveLeadOnLeadViewPage();
		
		log.info("Searching for created Lead");	
		sleep(10);
		
		Assert.assertEquals(leadDetailPage.getEmail(), lead.sEmailId, "Email value on page does not equal the edited value");
		
		leadDetailPage.deleteLead();
		
		log.info("Confirming lead has been deleted via API");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword());
		
		LeadsRestAPI leadsRestAPI = new LeadsRestAPI();
		leadsRestAPI.getLead(testConfig.getBrowserURL(), token, leadID,  "404");

		log.info("End of test method Test_AT_Lead");
	}
}
