package com.ibm.appium.test.Opportunity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.appium.MobileBaseTest;
import com.ibm.appium.Objects.Opportunity;
import com.ibm.appium.model.Dashboard;
import com.ibm.appium.model.Menus.MainMenu;
import com.ibm.appium.model.Menus.QuickCreateMenu;
import com.ibm.appium.model.Opportunity.CreateOpportunityPage;
import com.ibm.appium.model.Opportunity.OpportunityDetailPage;
import com.ibm.appium.model.Opportunity.OpportunityListPage;
import com.ibm.atmn.waffle.extensions.user.User;

public class OpportunitiesTests extends MobileBaseTest {

	public static String showMore = "//span[@class='show-more']";

	Logger log = LoggerFactory.getLogger(OpportunitiesTests.class);
	Opportunity oppty = new Opportunity();
	
	/**
	 * Login to Sales Connect mobile, create new opportunity using quick menu
	 * and verify opportunity has been created.
	 */
	
	@Test(groups={"Mobile"})
	public void s2626OpportunitiesCreate() {
		log.info("Starting method s2626OpportunitiesCreate");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		log.info("Using user: " + user.getEmail());

		try {
			oppty.populate();
	
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
	
			log.info("Opening quick create menu");
			QuickCreateMenu qCM = dashBoard.openQuickCreateMenu();
	
			log.info("Creating opportunity");
			CreateOpportunityPage createOpportunityPage = qCM
					.openCreateOpportunityPage();
			createOpportunityPage.enterOpportunityInfo(oppty);
			OpportunityDetailPage opportunityDetailPage = createOpportunityPage
					.saveOpportunity();
	
			log.info("Verifying opportunity creation");
			Assert.assertEquals(opportunityDetailPage.getOpportunityDescription(),
					oppty.getsDescription(),
					"Opportunty description does not match expected");
			log.info("Opportunity OK. Getting ID for future tests.");
			oppty.setsId(opportunityDetailPage.getOpportunityId());
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method s2626OpportunitiesCreate");		
		}	
	}

	@Test(groups={"Search"}, dependsOnMethods = { "s2626OpportunitiesCreate" })
	public void sOpportunitiesDashboardSearch() {
		log.info("Starting method sOpportunitiesDashboardSearch");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		log.info("Using user: " + user.getEmail());

		try {
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
			dashBoard.openGlobalSearchPage();
			OpportunityDetailPage opportunityDetailPage = dashBoard.searchForItem(oppty);
	
			log.info("Verifying oppty contents");
			
			Assert.assertEquals(opportunityDetailPage.getOpportunityDescription(),
					oppty.getsDescription(),
					"Opportunty description does not match expected");		
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method sOpportunitiesDashboardSearch");		
		}	
	}

	@Test(groups={"Search"},dependsOnMethods = { "s2626OpportunitiesCreate" })
	public void sOpportunitiesListViewSearchMyItems() {
		log.info("Starting method sOpportunitiesListViewSearchMyItems");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		log.info("Using user: " + user.getEmail());

		try {
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
			
			MainMenu mM = dashBoard.openMainMenu();
			OpportunityListPage opportunityListPage = mM.openOpptyListView();
			
			OpportunityDetailPage opportunityDetailPage = opportunityListPage.searchForOppty(oppty);
	
			log.info("Verifying oppty contents");
			
			Assert.assertEquals(opportunityDetailPage.getOpportunityDescription(),
					oppty.getsDescription(),
					"Opportunty description does not match expected");		
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method sOpportunitiesListViewSearchMyItems");		
		}	
	}
	
	@Test(groups={"Mobile"}, dependsOnMethods = { "s2626OpportunitiesCreate" })
	public void s2627OpportunitiesEdit() {
		log.info("Starting method s2627OpportunitiesEdit");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		log.info("Using user: " + user.getEmail());

		try {
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
	
			log.info("Navigating to opportuntiy list view");
			MainMenu mM = dashBoard.openMainMenu();
			OpportunityListPage opportunityListPage = mM.openOpptyListView();
			
			OpportunityDetailPage opportunityDetailPage = opportunityListPage.searchForOppty(oppty);
	
			log.info("Editing opportunity");
			CreateOpportunityPage createOpportunityPage = opportunityDetailPage
					.editOppty();
			createOpportunityPage.updateOpportunityInfo(oppty);
			createOpportunityPage.saveOpportunity();
	
			log.info("Verifying opportunity creation");
			Assert.assertEquals(opportunityDetailPage.getOpportunityDescription(),
					oppty.getsDescription() + oppty.sOpptyDescriptionUPD,
					"Updated Opportunty description does not match expected");
			oppty.updateDetails();
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method s2627OpportunitiesEdit");		
		}	
	}
}
