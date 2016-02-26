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
 * Description   : [R1.3] Verify Recommended documents can filter for doc type 'Competitive Sales Support'.
 * @author KevinLau
 * @date 10/12/13
 */

public class s2663RecommendedDocFilterCompetitiveSalesSupport extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(s2663RecommendedDocFilterCompetitiveSalesSupport.class);
	
	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String opptyID = null;
	String clientID = null;
	SugarAPI sugarAPI = new SugarAPI();
	Opportunity oppt = new Opportunity();
	
	@Test(groups = { "SocialAnalytics"})
	public void test_s2663RecommendedDocFilterCompetitiveSalesSupport() {
		
		log.info("Start of test method s2663RecommendedDocFilterCompetitiveSalesSupport");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();

		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		
		log.info("Create L40 RLI (i.e. offering 'Level 40: Miscellaneous - Other/Unk IBM HW (483813V)' " ); 
		String sUrl = testConfig.getBrowserURL();
		String sRliID = "44SC-" + rand;
		sugarAPI.createRLI(sUrl, sRliID, user1.getEmail(), user1.getPassword(), "level40", "483813V");
				
		//sugarAPI.createOppty(testConfig.getBrowserURL(), opptyID, "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		log.info("Creating oppty (with RLI): " + opptyID.toString() );
		oppt.sOpptNumber = opptyID;
		oppt.sPrimaryContact = "ContactFirst ContactLast";
		oppt.sAccID = clientID;
		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(),  sRliID, contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		
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
					
			log.info("Search for docs related to: e.g. 'Websphere' ");
			recdoc.findDocRelatedTo("Websphere");
			
			log.info("Verify doc type filter 'Competitive Sales Support' exists");
			Assert.assertTrue(recdoc.verifyDocFilterExists(RecommendedDocsTab.docTypeCompSalesSupportLabel), 
					           "Did not find Doc type filter 'Competitive Sales Support'");
			
			log.info("Filter recommended docs with doc type filter 'Competitive Sales Support'");
			recdoc.selectDocTypeFilterOption(RecommendedDocsTab.docTypeCompSalesSupportLabel);
			
			log.info("Verify that filter returns only docs of type 'Competitive Sales Support'");
			Assert.assertTrue(recdoc.verifyDocTypeOfResults(RecommendedDocsTab.docTypeCompSalesSupportLabel), 
								"Failed as doc types other than 'Competitive Sales Support' where found");
			
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
		
		log.info("End of test method s2663RecommendedDocFilterCompetitiveSalesSupport");
	}
}