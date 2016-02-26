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
 * Description   : [R1.0] Verify addition of Sales Kit data type for document recommendations
 * @author KevinLau
 * @date 12/12/13
 */

public class s2667RecommendedDocFilterSalesKit extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(s2667RecommendedDocFilterSalesKit.class);
	
	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String opptyID = null;
	String clientID = null;
	SugarAPI sugarAPI = new SugarAPI();
	Opportunity oppt = new Opportunity();
	/**
	Test Steps
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Logon to Salesconnect </li>
		 * <li>Navigate view Oppties</li>
		 * <li>Click on Oppty created by login user </li>
		 * <li>Navigate to Oppty Detail page</li>
		 * <li>Click on Recomended documents tab  and verify Recomended doc tab loaded properly</li>
		 * <li>"Search for docs related to: e.g. 'Websphere' "</li>
		 * <li>Verify Filter recommended docs with doc type filter 'Sales kit' and Verify that filter                  returns only docs of type 'Sales kit</li>
	     *<li>Click Clear all links and verify filter cleared<li>
		 * <li>Verify Filter recommended docs with doc type filter 'Sales Play' and Verify that filter                  returns only docs of type 'Sales Play</li>
		 * <li>Navigate using link 'Back to search results</li>
	     *<li>Click clear filter in Doc type menu</li>
	     *<li>Verify Document type filter is cleared</li>
		 * </ol>
		 */
	
	@Test(groups = { "SocialAnalytics","AT","BVT","BVT1"})
	public void test_s2667RecommendedDocFilterSalesKit() {
		
		log.info("Start of test method s2663RecommendedDocFilterCompetitiveSalesSupport");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		
		log.info("Create L40 RLI (i.e. offering 'Level 40: Miscellaneous - Other/Unk IBM HW (483813V)' " ); 
		String sUrl = testConfig.getBrowserURL();
		String sRliID = "44SC-" + rand;
		
		sugarAPI.createRLI(sUrl, sRliID, user1.getEmail(), user1.getPassword(), "level40", "483813V");				
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");		
		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), sRliID, contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		
		oppt.sOpptNumber = opptyID;
		oppt.sAccID = clientID;
		oppt.sPrimaryContact = "ContactFirst ContactLast";

		log.info("Creating oppty (with RLI): " + opptyID.toString() );
		try {			
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);
			//launchWithoutLogin();
				
			log.info("Open Opportunity detail page");
			ViewOpportunityPage viewOpportunityPage = dashboard.openViewOpportunity();
			viewOpportunityPage.searchForOpportunityID(opptyID);
			OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResultfromID(opptyID);
			
			log.info("Open Recommended Documents Tab & verify it loads correctly");
			RecommendedDocsTab recdoc = opportunityDetailPage.openRecommendedDocsTab();
			
			recdoc.isPageLoaded();
			Assert.assertTrue(recdoc.isPageLoaded(), "Did not load Recommended documents tab");
					
			log.info("Search for docs related to: e.g. 'Websphere' ");
			recdoc.findDocRelatedTo("Websphere"); // Websphere lists more recommended docs
			
			sleep(3);
			
			log.info("Verify doc type filter 'Sales kit' exists");
			Assert.assertTrue(recdoc.verifyDocFilterExists(RecommendedDocsTab.docTypeSalesKitLabel), 
					"Did not find Doc type filter 'Sales kit'");
			
			log.info("Filter recommended docs with doc type filter 'Sales kit'");
			recdoc.selectDocTypeFilterOption(RecommendedDocsTab.docTypeSalesKitLabel);
			
			log.info("Verify that filter returns only docs of type 'Sales kit'");
			Assert.assertTrue(recdoc.verifyDocTypeOfResults(RecommendedDocsTab.docTypeSalesKitLabel),
					"Sale kit Filter returned incorrect doc types ");
					
			log.info("Open & verify doc contents (of 1st eligible one found) " +
					 "are loading correctly in Recommended docs tab");
			Assert.assertTrue(recdoc.verifyDocContentsInRecDocsTab(), 
					"doc's contents are not loading in Recommended tab");
			
			log.info("Navigate using link 'Back to search results");
			Assert.assertTrue(recdoc.verifyReturnToSearchResultsFromContentsPage(),
					"Could not navigate back to search results via 'Back to search results' link");
			
			log.info("Click clear filter in Doc type menu");
			recdoc.click(RecommendedDocsTab.clearAllLink);
			
			log.info("Verify Document type filter is cleared");
			Assert.assertTrue(recdoc.verifyFilterIsCleared(RecommendedDocsTab.docTypeFilter),
					"Document type filter was not cleared");
			
			log.info("Filter recommended docs with doc type filter 'Sales play'");
			recdoc.selectDocTypeFilterOption(RecommendedDocsTab.docTypeSalesPlayLabel);
			
			// PART2 of test case to check 'Sales play' filter option
			
			log.info("Verify that filter returns only docs of type 'Sales play'");
			Assert.assertTrue(recdoc.verifyDocTypeOfResults(RecommendedDocsTab.docTypeSalesPlayLabel),
					"'Sales play' filter is not filtering all doc results correctly");
			
			log.info("Open & verify doc contents (of 1st eligible one found) " +
						"are loading correctly in Recommended docs tab");
			Assert.assertTrue(recdoc.verifyDocContentsInRecDocsTab(), 
					"doc's contents are not loading in Recommended tab");
	
			log.info("Navigate using link 'Back to search results");
			Assert.assertTrue(recdoc.verifyReturnToSearchResultsFromContentsPage(),
					"Could not navigate back to search results via 'Back to search results' link");
			
			log.info("Click clear filter in Doc type menu");
			recdoc.click(RecommendedDocsTab.clearAllLink);
			
			log.info("Verify Document type filter is cleared");
			Assert.assertTrue(recdoc.verifyFilterIsCleared(RecommendedDocsTab.docTypeFilter),
					"Document type filter was not cleared");
			 
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
		log.info("End of test method s2667RecommendedDocFilterSalesKit");
	}
}