package com.ibm.salesconnect.test.SocialAnalytics;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.RecommendedDocsTab;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Opportunity;

/**
/**
 * Description   : [R1.2] Verify the recommended doc ranking for Sales kit types are raised to top when doc matches L20 product level of oppty.
 * @author KevinLau
 * @date 29/11/13
 */

public class cr4884RecommendedDocSalesKitRankingRaised extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(cr4884RecommendedDocSalesKitRankingRaised.class);
	
	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String opptyID = null;
	String clientID = null;
	SugarAPI sugarAPI = new SugarAPI();
	Opportunity oppt = new Opportunity();
	
	@Test(groups = { ""}) // this test is no longer valid in 2.0, removed from suite, may become valid later
	public void test_cr4884RecommendedDocSalesKitRankingRaised() {
		
		log.info("Start of test method cr4884RecommendedDocSalesKitRankingRaised");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();

		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		
		log.info("Create L40 RLI (i.e. offering 'Level 40: Miscellaneous - Other/Unk IBM HW (483813V)' " ); 
		String sUrl = testConfig.getBrowserURL();
		String sRliID = "44SC-" + rand;
		sugarAPI.createRLI(sUrl, sRliID, user1.getEmail(), user1.getPassword(), "level40","483813V");
				
		//sugarAPI.createOppty(testConfig.getBrowserURL(), opptyID, "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		log.info("Creating oppty (with RLI): " + opptyID.toString() );
		oppt.sOpptNumber = opptyID;
		oppt.sPrimaryContact = "ContactFirst ContactLast";
		oppt.sAccID = clientID;
		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), sRliID, contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		
		try {			
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);
			//launchWithoutLogin();
				
			log.info("Open Opportunity detail page");
			ViewOpportunityPage viewOpportunityPage = dashboard.openViewOpportunity();
			viewOpportunityPage.searchForOpportunity(oppt);
			OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
			
			
			log.info("Open Recommended Documents Tab & verify it loads correctly");
			RecommendedDocsTab recdoc = opportunityDetailPage.openRecommendedDocsTab();
			Assert.assertTrue(recdoc.isPageLoaded(), "Did not load Recommended documents tab");
					
			//log.info("Search for docs related to: e.g. 'Sametime' ");
			//recdoc.findDocRelatedTo("Sametime");
			
			log.info("[Test] Verify top ranking docs in results have doc type of 'Sales kit' ");
			Assert.assertTrue(recdoc.verifyResultsTopRankingHasTypeSalesKit(), 
					          "Top rankings of Recommended doc results, are not of doc type 'Sales kit' ");			
					
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Remove the oppty created for this test");
			sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
			sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
			Assert.assertTrue(false);
		}

		log.info("Remove the oppty created for this test");
		sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		log.info("End of test method cr4884RecommendedDocSalesKitRankingRaised");
	}
}