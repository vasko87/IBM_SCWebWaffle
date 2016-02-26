package com.ibm.salesconnect.test.End2End;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.DB2Connection;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Opportunity.CreateOpportunityPage;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Opportunity;
import com.ibm.salesconnect.objects.RevenueItem;

public class E2EOpportunityCreateVerifySalesStage10CustomerDidNotPursueAndRLIwithMinimumLevel20SC2Siebel extends ProductBaseTest {

	Logger log = LoggerFactory.getLogger(E2EOpportunityCreateVerifySalesStage10CustomerDidNotPursueAndRLIwithMinimumLevel20SC2Siebel.class);

	@Test(groups = { "End2End"})
	public void Test_E2EOpportunityCreateVerifySalesStage10CustomerDidNotPursueAndRLIwithMinimumLevel20SC2Siebel(){
		log.info("Start of test method E2EOpportunityCreateVerifySalesStage10CustomerDidNotPursueAndRLIwithMinimumLevel20SC2Siebel");
		User db2User = commonUserAllocator.getGroupUser(GC.db2UserGroup,this);

		Opportunity oppt = new Opportunity();
		RevenueItem rli = new RevenueItem();
		
		oppt.populate();
		rli.populate();
				
		try{
			User e2eUser = commonUserAllocator.getGroupUser(GC.e2eUserGroupAG,this);
			PoolClient poolClient = commonClientAllocator.getGroupClient("E2EAG",this);

			DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
			Client client = db2.retrieveClient(poolClient, e2eUser.getEmail(),testConfig.getParameter(GC.testPhase));
			System.out.println("Client returned from DB: " + client.sClientName);
			
			client.sSearchIn= GC.showingInSiteID;
			client.sSearchFor= GC.searchForAll;
			client.sSearchShowing=GC.showingForAll;
		
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(e2eUser);
						
			log.info("Adding primary contact to opportunity, Setting Sales Stage to 10 - Customer Did Not Pursue & RLI with Level 20");
			oppt.sPrimaryContact="Full_AG" + GC.sUniqueNum;
			oppt.sPrimaryContactWithPreferred = "Full_AG" + GC.sUniqueNum;
			oppt.sOpptDesc = "E2E (Opportunity - Create) Verify Sales Stage = 10-Customer Did Not Pursue and RLI with minimum Level 15 SC >> Siebel - AG "+ GC.sUniqueNum;
			oppt.sSalesStage = GC.gsOppCustomerNotPursue;
			rli.sFindOffering = GC.gsBigData;
			
			log.info("Creating Opportunity "+oppt.sOpptDesc+" to synch to AG Siebel Instance ");
			CreateOpportunityPage createOpportunityPage = dashboard.openCreateOpportunity();
			createOpportunityPage.enterOpportunityInfo(oppt,rli);
			ViewOpportunityPage viewOpportunityPage = createOpportunityPage.saveOpportunity(oppt);
			OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
			oppt.sOpptNumber = opportunityDetailPage.getdisplayedOpportunityNumber();
			
			log.info("AG Opportunity "+oppt.sOpptNumber+" Successfully Created");
		
			} catch (Exception e) {
				log.error(e.toString());
			}

			try{
				User e2eUser = commonUserAllocator.getGroupUser(GC.e2eUserGroupEU,this);
				PoolClient poolClient = commonClientAllocator.getGroupClient("E2EEU",this);

				DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
				Client client = db2.retrieveClient(poolClient, e2eUser.getEmail(),testConfig.getParameter(GC.testPhase));
				System.out.println("Client returned from DB: " + client.sClientName);
				
				client.sSearchIn= GC.showingInSiteID;
				client.sSearchFor= GC.searchForAll;
				client.sSearchShowing=GC.showingForAll;
			
				log.info("Logging in");
				Dashboard dashboard = launchWithLogin(e2eUser);
						
				log.info("Adding primary contact to opportunity, Setting Sales Stage to 10 - Customer Did Not Pursue & RLI with Level 20");
				oppt.sPrimaryContact="Full_EU" + GC.sUniqueNum;
				oppt.sPrimaryContactWithPreferred = "Full_EU" + GC.sUniqueNum;
				oppt.sOpptDesc = "E2E (Opportunity - Create) Verify Sales Stage = 10-Customer Did Not Pursue and RLI with minimum Level 15 SC >> Siebel - EU "+ GC.sUniqueNum;
				oppt.sSalesStage = GC.gsOppCustomerNotPursue;
				rli.sFindOffering = GC.gsBigData;
				
				log.info("Creating Opportunity "+oppt.sOpptDesc+" to synch to EU Siebel Instance ");
				CreateOpportunityPage createOpportunityPage = dashboard.openCreateOpportunity();
				createOpportunityPage.enterOpportunityInfo(oppt,rli);
				ViewOpportunityPage viewOpportunityPage = createOpportunityPage.saveOpportunity(oppt);
				OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
				oppt.sOpptNumber = opportunityDetailPage.getdisplayedOpportunityNumber();
				
				log.info("EU Opportunity "+oppt.sOpptNumber+" Successfully Created");
				
				} catch (Exception e) {
					log.error(e.toString());
				}

				try{
					User e2eUser = commonUserAllocator.getGroupUser(GC.e2eUserGroupAP,this);
					PoolClient poolClient = commonClientAllocator.getGroupClient("E2EAP",this);

					DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
					Client client = db2.retrieveClient(poolClient, e2eUser.getEmail(),testConfig.getParameter(GC.testPhase));
					System.out.println("Client returned from DB: " + client.sClientName);
					
					client.sSearchIn= GC.showingInSiteID;
					client.sSearchFor= GC.searchForAll;
					client.sSearchShowing=GC.showingForAll;
				
					log.info("Logging in");
					Dashboard dashboard = launchWithLogin(e2eUser);
				
					log.info("Adding primary contact to opportunity, Setting Sales Stage to 10 - Customer Did Not Pursue & RLI with Level 20");
					oppt.sPrimaryContact="Full_AP" + GC.sUniqueNum;
					oppt.sPrimaryContactWithPreferred = "Full_AP" + GC.sUniqueNum;
					oppt.sOpptDesc = "E2E (Opportunity - Create) Verify Sales Stage = 10-Customer Did Not Pursue and RLI with minimum Level 15 SC >> Siebel - AP "+ GC.sUniqueNum;
					oppt.sSalesStage = GC.gsOppCustomerNotPursue;
					rli.sFindOffering = GC.gsBigData;
					
					log.info("Creating Opportunity "+oppt.sOpptDesc+" to synch to AP Siebel Instance ");
					CreateOpportunityPage createOpportunityPage = dashboard.openCreateOpportunity();
					createOpportunityPage.enterOpportunityInfo(oppt,rli);
					ViewOpportunityPage viewOpportunityPage = createOpportunityPage.saveOpportunity(oppt);
					OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
					oppt.sOpptNumber = opportunityDetailPage.getdisplayedOpportunityNumber();
					
					log.info("EU Opportunity "+oppt.sOpptNumber+" Successfully Created");
					
				} catch (Exception e) {
						log.error(e.toString());
					}
					
			log.info("End of test method E2EOpportunityCreateVerifySalesStage10CustomerDidNotPursueAndRLIwithMinimumLevel20SC2Siebel");

	}
}
