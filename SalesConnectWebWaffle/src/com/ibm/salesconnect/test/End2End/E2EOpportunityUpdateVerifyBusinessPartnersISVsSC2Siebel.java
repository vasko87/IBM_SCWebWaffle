
package com.ibm.salesconnect.test.End2End;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.PoolHandling.businessPartner.BusinessPartner;
import com.ibm.salesconnect.common.DB2Connection;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Opportunity.CreateOpportunityPage;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Opportunity;

@Test(groups = {"End2End"})
public class E2EOpportunityUpdateVerifyBusinessPartnersISVsSC2Siebel extends ProductBaseTest {
	/**
	 * Script Name : <b>E2EOpportunityUpdateVerifyBusinessPartnersISVsSC2Siebel</b> Generated : <b>20 June
	 * 2013 15:32:26</b> Original Host : WinNT Version 6.1 Build 7601 (S)
	 * 
	 * Create contacts with various status and properties:
	 * 1) Create contact
	 * 2) Create opportunity using contact and client
	 * 3) Add business partners to opportunity
	 * 
	 * 
	 * @since 2013/06/20
	 * @author Tyler Clayton
	 */

	public void testMain() {
		Logger log = LoggerFactory.getLogger(E2EOpportunityUpdateVerifyBusinessPartnersISVsSC2Siebel.class);
		log.info("Start of test method E2EOpportunityUpdateVerifyBusinessPartnersISVsSC2Siebel");
		User e2eUser = commonUserAllocator.getGroupUser("e2e_users",this);
		User db2User = commonUserAllocator.getGroupUser(GC.db2UserGroup,this);

		Opportunity oppt = new Opportunity();
		oppt.populate();
				
		try {
			//Instantiate desired classes
			PoolClient poolClient = commonClientAllocator.getGroupClient("E2EAG",this);
			
			DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
			Client client = db2.retrieveClient(poolClient, e2eUser.getEmail(),testConfig.getParameter(GC.testPhase));
			System.out.println("Client returned from DB: " + client.sClientName);
			
			client.sSearchIn= GC.showingInSiteID;
			client.sSearchFor= GC.searchForAll;
			client.sSearchShowing=GC.showingForAll;
		
			//Define variables for oppt
			BusinessPartner bp = new BusinessPartner(businessPartnerfilePath);
			String BP1 = bp.getBusinessPartnerCeid("1");
			oppt.vBusinessParters.addElement(BP1);
			oppt.vBusinessRole.add(GC.gsOppRoleBusinessPartner);
			oppt.sPrimaryContact="Full_AG" + GC.sUniqueNum;
			oppt.sPrimaryContactWithPreferred = "Full_AG" + GC.sUniqueNum;
			oppt.sOpptDesc = "E2E (Opportunity - Update) Verify Business Partners ISVs SC >> Siebel - AG " + GC.sUniqueNum;
			
			//TESTCASE BEGIN
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(e2eUser);
			
			log.info("Creating Opportunity "+oppt.sOpptDesc+" to synch to AG Siebel Instance ");
			CreateOpportunityPage createOpportunityPage = dashboard.openCreateOpportunity();
			createOpportunityPage.enterOpportunityInfo(oppt, null);
			ViewOpportunityPage viewOpportunityPage = createOpportunityPage.saveOpportunity(oppt);
			OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
			
			
			log.info("AG Opportunity " + opportunityDetailPage.getdisplayedOpportunityNumber() + " created");
					
			
		} catch (Exception e) {
			log.error(e.toString());
		}
		
		try {
			//Instantiate desired classes
			PoolClient poolClient = commonClientAllocator.getGroupClient("E2EEU",this);
			
			DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
			Client client = db2.retrieveClient(poolClient, e2eUser.getEmail(),testConfig.getParameter(GC.testPhase));
			System.out.println("Client returned from DB: " + client.sClientName);
			
			client.sSearchIn= GC.showingInSiteID;
			client.sSearchFor= GC.searchForAll;
			client.sSearchShowing=GC.showingForAll;
			
			//Define variables for oppt
			BusinessPartner bp = new BusinessPartner(businessPartnerfilePath);
			String BP1 = bp.getBusinessPartnerCeid("4");
			oppt.vBusinessParters.addElement(BP1);
			oppt.vBusinessRole.add(GC.gsOppRoleBusinessPartner);
			oppt.sPrimaryContact="Full_EU" + GC.sUniqueNum;
			oppt.sPrimaryContactWithPreferred = "Full_EU" + GC.sUniqueNum;
			oppt.sOpptDesc = "E2E (Opportunity - Update) Verify Business Partners ISVs SC >> Siebel - EU " + GC.sUniqueNum;
			
			//TESTCASE BEGIN
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(e2eUser);
			
			log.info("Creating Opportunity "+oppt.sOpptDesc+" to synch to EU Siebel Instance ");
			CreateOpportunityPage createOpportunityPage = dashboard.openCreateOpportunity();
			createOpportunityPage.enterOpportunityInfo(oppt, null);
			ViewOpportunityPage viewOpportunityPage = createOpportunityPage.saveOpportunity(oppt);
			OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
			
			
			log.info("EU Opportunity " + opportunityDetailPage.getdisplayedOpportunityNumber() + " created");
					
			
		} catch (Exception e) {
			log.error(e.toString());
		}
		
		try {
			//Instantiate desired classes
			PoolClient poolClient = commonClientAllocator.getGroupClient("E2EAP",this);

			DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
			Client client = db2.retrieveClient(poolClient, e2eUser.getEmail(),testConfig.getParameter(GC.testPhase));
			System.out.println("Client returned from DB: " + client.sClientName);
			
			client.sSearchIn= GC.showingInSiteID;
			client.sSearchFor= GC.searchForAll;
			client.sSearchShowing=GC.showingForAll;
				
			//Define variables for oppt
			BusinessPartner bp = new BusinessPartner(businessPartnerfilePath);
			String BP1 = bp.getBusinessPartnerCeid("1");
			oppt.vBusinessParters.addElement(BP1);
			oppt.vBusinessRole.add(GC.gsOppRoleBusinessPartner);
			oppt.sPrimaryContact="Full_AP" + GC.sUniqueNum;
			oppt.sPrimaryContactWithPreferred = "Full_AP" + GC.sUniqueNum;
			oppt.sOpptDesc = "E2E (Opportunity - Update) Verify Business Partners ISVs SC >> Siebel - AP " + GC.sUniqueNum;
			
			//TESTCASE BEGIN
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(e2eUser);
			
			log.info("Creating Opportunity "+oppt.sOpptDesc+" to synch to AP Siebel Instance ");
			CreateOpportunityPage createOpportunityPage = dashboard.openCreateOpportunity();
			createOpportunityPage.enterOpportunityInfo(oppt, null);
			ViewOpportunityPage viewOpportunityPage = createOpportunityPage.saveOpportunity(oppt);
			OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
			
			log.info("AP Opportunity " + opportunityDetailPage.getdisplayedOpportunityNumber() + " created");

			
		} catch (Exception e) {
			log.error(e.toString());
		}
		
		log.info("End of test method E2EOpportunityUpdateVerifyBusinessPartnersISVsSC2Siebel");
	}
	
}
