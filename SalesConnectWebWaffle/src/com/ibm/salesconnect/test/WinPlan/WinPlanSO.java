/**
 * 
 */
package com.ibm.salesconnect.test.WinPlan;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.LineItemDetailPage;
import com.ibm.salesconnect.model.partials.WinPlanTab;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Opportunity;
import com.ibm.salesconnect.objects.RevenueItem;

/**
 * @author timlehane
 * @date Nov 25, 2013
 */
public class WinPlanSO extends ProductBaseTest {
		
		@Test(groups = {"WinPlan"})
		public void Test_WinPlanSO() {
			Logger log = LoggerFactory.getLogger("Test_WinPlanSO");
			log.info("Start of test method Test_WinPlanSO");
			
				int rand = new Random().nextInt(100000);
				String contactID = "22SC-" + rand;
				String opptyID = "33SC-" + rand;
				String RLIID = "44SC-" + rand;
				PoolClient poolClient = null;
				String clientID = null;
				SugarAPI sugarAPI = new SugarAPI();
				Opportunity oppty = new Opportunity();
				
				User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
				poolClient = commonClientAllocator.getGroupClient(GC.DC,this);
				clientID = poolClient.getCCMS_ID();
				String[] opptyTeam = getMultipleUsers(2, this);
				sugarAPI.createRLI(testConfig.getBrowserURL(), RLIID, user1.getEmail(), user1.getPassword(), "level20","BLT00");
				sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
				sugarAPI.createOpptySOAP(testConfig.getBrowserURL(), opptyID, RLIID, contactID, clientID, user1.getEmail(), user1.getPassword(), opptyTeam);
				oppty.sOpptNumber = opptyID;
				oppty.sPrimaryContact = "ContactFirst ContactLast";
				oppty.sAccID = clientID;
			
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);
			
			log.info("Open Opportunity Details Page");
			ViewOpportunityPage viewOppPage = dashboard.openViewOpportunity();
			viewOppPage.searchForOpportunity(oppty);
			viewOppPage.isPageLoaded();
			OpportunityDetailPage oppDetailPage = viewOppPage.selectResult(oppty);
			
			log.info("Following oppty");
			oppDetailPage.openUpdatesTab();
			oppDetailPage.followOppty();
			
			log.info("Open win plan tab");
			WinPlanTab winPlan = oppDetailPage.openWinPlanTab();
			
			winPlan.openQGCPSection();
			winPlan.populateSOQGCPLI();
			
			LineItemDetailPage editLineItem = oppDetailPage.selectEditRli();
			
			RevenueItem rli = new RevenueItem();
			rli.sOwner = user1.getDisplayName();
			rli.sIGFRoadmapStatus = "Stretch";
			rli.sIGFVolumesDate = "04/09/2019";
			rli.sIGFCloseDate = "04/09/2019";
			
			editLineItem.editLineItemInfo(rli);
			
			log.info("Verify events");
			String[] eventList = {"updated proposal / sales stage to", "updated scope to", 
					"updated pricing to P01", "updated Contract / T&Cs to", "updated Target Sign Date to", "updated competitive status",
					"updated the roadmap status"};
			winPlan.navigateToOpportunityOverview();
			
			oppDetailPage.checkEvents(eventList);

			log.info("Remove the oppty created for this test");
			sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
			sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		}
}
