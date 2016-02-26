package com.ibm.salesconnect.test.Search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Home.UserProfilePage;
import com.ibm.salesconnect.model.standard.Lead.ViewLeadPage;
import com.ibm.salesconnect.objects.Lead;

public class SidecarSearchTests extends ProductBaseTest{
	Logger log = LoggerFactory.getLogger(SidecarSearchTests.class);
	
	@Test(groups = {"SidecarFilter"})
	public void TestLeadNameFilter(){
		log.info("Start of test method Test_AT_Lead");

		Lead lead = new Lead();
		String column = "fullname";
		String realColumn = "Name";
		String field = "First Name";
		lead.populate();
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
		
		/*
		 * TODO - add API to create a group of leads to test
		 */
		
		log.info("Creating Leads");
		ViewLeadPage viewLeadPage = dashboard.openViewLead();
		
		log.info("Create the filter");
		viewLeadPage.createFilterOneElement("BVTFilter", field, "starts with" ,"BVT");
		
		log.info("Check the filtered results");
		Assert.assertTrue(viewLeadPage.verifyEntries(realColumn, "BVT"),"There are incorrect entries in the" + column + " column.");
	}
	
	
	
}
