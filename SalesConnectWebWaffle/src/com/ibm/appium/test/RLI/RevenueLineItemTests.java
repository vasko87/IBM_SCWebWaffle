package com.ibm.appium.test.RLI;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.appium.MobileBaseTest;
import com.ibm.appium.Objects.LineItem;
import com.ibm.appium.model.Dashboard;
import com.ibm.appium.model.Menus.MainMenu;
import com.ibm.appium.model.Opportunity.OpportunityDetailPage;
import com.ibm.appium.model.Opportunity.OpportunityListPage;
import com.ibm.appium.model.RLI.CreateRLIPage;
import com.ibm.appium.model.RLI.RLIDetailPage;
import com.ibm.appium.model.RLI.RLIListPage;
import com.ibm.atmn.waffle.extensions.user.User;

public class RevenueLineItemTests extends MobileBaseTest {

	LineItem lineItem = new LineItem();
	
	@Test
	public void s27809CreateLineItem() {
				
		log.info("Starting method s27809CreateLineItem");		
		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		log.info("Using user: " + user.getEmail());
		
		lineItem.populate();
		
		try {
		
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
	
			log.info("Navigating to oppty list view");
			MainMenu mM = dashBoard.openMainMenu();
			OpportunityListPage opptyListView = mM.openOpptyListView();
			log.info("Navigate to first oppty in the list");
			
			OpportunityDetailPage opptyDetailPage = opptyListView.selectFirstOppty();
			log.info("Create a new RLI");
			CreateRLIPage createRLIPage = opptyDetailPage.createRelatedLineItem();
			createRLIPage.enterRLIInfo(lineItem);
			
			RLIDetailPage rliDetailPage = createRLIPage.saveRLI();
			
			log.info("Verifying RLI creation");

			Assert.assertTrue(rliDetailPage.getRLIAmount(lineItem.getsAmountInK()).contains(lineItem.getsAmountInK() + "k"),"RLI amount does not match expected");
			
			Assert.assertEquals(rliDetailPage.getRLIL10(),
					lineItem.getsL10_OfferingType(),
					"RLI Offering Type L-10 does not match expected");
			
			Assert.assertEquals(rliDetailPage.getRLIL15(),
					lineItem.getsL15_SubBrand(),
					"RLI Sub-Brand L-15 does not match expected");
			
			Assert.assertEquals(rliDetailPage.getRLIL20(),
					lineItem.getsL20_BrandCode(),
					"RLI Brand Code L-20 does not match expected");
			
			Assert.assertEquals(rliDetailPage.getRLIL30(),
					lineItem.getsL30_ProductInformation(),
					"RLI Product Information L-30 does not match expected");
						
			Assert.assertEquals(rliDetailPage.getRLIRoadmapStatus(),
					lineItem.getsRoadmapStatus(),
					"RLI Roadmap status does not match expected");
			
			Assert.assertEquals(rliDetailPage.getRLIProbability(),
					lineItem.getsProbability(),
					"RLI Probability does not match expected");
			
			log.info("All's good. RLI created.");
			
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method s27809CreateLineItem");		
		}	
	}
	
	@Test(groups={"Mobile"}, dependsOnMethods = { "s27809CreateLineItem" })
	public void s27809EditLineItem() {
				
		log.info("Starting method s27809EditLineItem");		
		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		log.info("Using user: " + user.getEmail());
		
		lineItem.updateDetails();
		
		try {
		
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
	
			log.info("Navigating to oppty list view");
			MainMenu mM = dashBoard.openMainMenu();
			OpportunityListPage opptyListView = mM.openOpptyListView();
			log.info("Navigate to first oppty in the list");
			
			OpportunityDetailPage opptyDetailPage = opptyListView.selectFirstOppty();
			log.info("Navigate to related RLIs");
			RLIListPage rliListPage = opptyDetailPage.openRelatedLineItemListPage();
			
			RLIDetailPage rliDetailPage = rliListPage.openFirstRLI();
			CreateRLIPage editRLIPage = rliDetailPage.editRLI();
			
			editRLIPage.enterRLIInfo(lineItem);
			
			rliDetailPage = editRLIPage.saveRLI();
			
			log.info("Verifying RLI creation");
			Assert.assertTrue(rliDetailPage.getRLIAmount(lineItem.getsAmountInK()).contains(lineItem.getsAmountInK() + "k"),
					"RLI amount does not match expected");
			
			Assert.assertEquals(rliDetailPage.getRLIL10(),
					lineItem.getsL10_OfferingType(),
					"RLI Offering Type L-10 does not match expected");
			
			Assert.assertEquals(rliDetailPage.getRLIL15(),
					lineItem.getsL15_SubBrand(),
					"RLI Sub-Brand L-15 does not match expected");
			
			Assert.assertEquals(rliDetailPage.getRLIL20(),
					lineItem.getsL20_BrandCode(),
					"RLI Brand Code L-20 does not match expected");
			
			Assert.assertEquals(rliDetailPage.getRLIL30(),
					lineItem.getsL30_ProductInformation(),
					"RLI Product Information L-30 does not match expected");
						
			Assert.assertEquals(rliDetailPage.getRLIRoadmapStatus(),
					lineItem.getsRoadmapStatus(),
					"RLI Roadmap status does not match expected");
			
			Assert.assertEquals(rliDetailPage.getRLIProbability(),
					lineItem.getsProbability(),
					"RLI Probability does not match expected");
			
			log.info("All's good. RLI edited.");
			
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method s27809EditLineItem");		
		}	
	}
}
