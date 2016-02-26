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


/**
 * @author evafarrell
 * @date June 21, 2013
 */

public class E2EOpportunityCreateVerifyPartnersTabOpportunityTeamRoleWithSetTeamRolesSC2Siebel extends ProductBaseTest {

	Logger log = LoggerFactory.getLogger(E2EOpportunityCreateVerifyPartnersTabOpportunityTeamRoleWithSetTeamRolesSC2Siebel.class);

	@Test(groups = { "End2End"})
	public void Test_E2EOpportunityCreateVerifyPartnersTabOpportunityTeamRoleWithSetTeamRolesSC2Siebel(){
		log.info("Start of test method E2EOpportunityCreateVerifyPartnersTabOpportunityTeamRoleWithSetTeamRolesSC2Siebel");
		User db2User = commonUserAllocator.getGroupUser(GC.db2UserGroup,this);

		Opportunity oppt = new Opportunity();
		oppt.populate();
			
		try {
			//Instantiate desired classes
			User e2eUser = commonUserAllocator.getGroupUser(GC.e2eUserGroupAG,this);
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
			String BP2 = bp.getBusinessPartnerCeid("2");
			String BP3 = bp.getBusinessPartnerCeid("3");
			oppt.vBusinessParters.addElement(BP1);
			oppt.vBusinessParters.addElement(BP2);
			oppt.vBusinessParters.addElement(BP3);
			oppt.vBusinessRole.addElement(GC.gsOppRoleBusinessPartner);
			oppt.vBusinessRole.addElement(GC.gsOppRoleMarketingServiceProv);
			oppt.vBusinessRole.addElement(GC.gsOppRoleDistVAD);
			oppt.sPrimaryContact="Full_AG" + GC.sUniqueNum;
			oppt.sPrimaryContactWithPreferred = "Full_AG" + GC.sUniqueNum;
			oppt.sOpptDesc = "E2E (Opportunity - Create) Verify Partners tab - Opportunity team Role with set team roles SC >> Siebel - AG " + GC.sUniqueNum;
						
			//TESTCASE BEGIN
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(e2eUser);
		
			log.info("Creating Opportunity "+oppt.sOpptDesc+" to synch to AG Siebel Instance ");
			
			//Create Opportunity
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
			User e2eUser = commonUserAllocator.getGroupUser(GC.e2eUserGroupAP,this);
			PoolClient poolClient = commonClientAllocator.getGroupClient("E2EAP",this);
			
			DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
			Client client = db2.retrieveClient(poolClient, e2eUser.getEmail(),testConfig.getParameter(GC.testPhase));
			System.out.println("Client returned from DB: " + client.sClientName);	
			
			client.sSearchIn= GC.showingInSiteID;
			client.sSearchFor= GC.searchForAll;
			client.sSearchShowing=GC.showingForAll;
			
			//Define variables for oppt
			BusinessPartner bp = new BusinessPartner(businessPartnerfilePath);
			String BP1 = bp.getBusinessPartnerCeid("5");
			String BP2 = bp.getBusinessPartnerCeid("6");
			String BP3 = bp.getBusinessPartnerCeid("7");
			oppt.vBusinessParters.addElement(BP1);
			oppt.vBusinessParters.addElement(BP2);
			oppt.vBusinessParters.addElement(BP3);
			oppt.vBusinessRole.addElement(GC.gsOppRoleBusinessPartner);
			oppt.vBusinessRole.addElement(GC.gsOppRoleMarketingServiceProv);
			oppt.vBusinessRole.addElement(GC.gsOppRoleDistVAD);
			oppt.sPrimaryContact="Full_AP" + GC.sUniqueNum;
			oppt.sPrimaryContactWithPreferred = "Full_AP" + GC.sUniqueNum;
			oppt.sOpptDesc = "E2E (Opportunity - Create) Verify Partners tab - Opportunity team Role with set team roles SC >> Siebel - AP " + GC.sUniqueNum;
						
			//TESTCASE BEGIN
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(e2eUser);
					
			log.info("Creating Opportunity "+oppt.sOpptDesc+" to synch to AP Siebel Instance ");
			
			//Create Opportunity
			CreateOpportunityPage createOpportunityPage = dashboard.openCreateOpportunity();
			createOpportunityPage.enterOpportunityInfo(oppt, null);
			ViewOpportunityPage viewOpportunityPage = createOpportunityPage.saveOpportunity(oppt);
			OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
			
			
			log.info("AP Opportunity " + opportunityDetailPage.getdisplayedOpportunityNumber() + " created");
					
			
		} catch (Exception e) {
			log.error(e.toString());
		}
		try {
			//Instantiate desired classes
			User e2eUser = commonUserAllocator.getGroupUser(GC.e2eUserGroupEU,this);
			PoolClient poolClient = commonClientAllocator.getGroupClient("E2EEU",this);

			DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
			Client client = db2.retrieveClient(poolClient, e2eUser.getEmail(),testConfig.getParameter(GC.testPhase));
			System.out.println("Client returned from DB: " + client.sClientName);
			
			client.sSearchIn= GC.showingInSiteID;
			client.sSearchFor= GC.searchForAll;
			client.sSearchShowing=GC.showingForAll;
			
			//Define variables for oppt
			BusinessPartner bp = new BusinessPartner(businessPartnerfilePath);
			String BP1 = bp.getBusinessPartnerCeid("9");
			String BP2 = bp.getBusinessPartnerCeid("10");
			String BP3 = bp.getBusinessPartnerCeid("11");
			oppt.vBusinessParters.addElement(BP1);
			oppt.vBusinessParters.addElement(BP2);
			oppt.vBusinessParters.addElement(BP3);
			oppt.vBusinessRole.addElement(GC.gsOppRoleBusinessPartner);
			oppt.vBusinessRole.addElement(GC.gsOppRoleMarketingServiceProv);
			oppt.vBusinessRole.addElement(GC.gsOppRoleDistVAD);
			oppt.sPrimaryContact="Full_EU" + GC.sUniqueNum;
			oppt.sPrimaryContactWithPreferred = "Full_EU" + GC.sUniqueNum;
			oppt.sOpptDesc = "E2E (Opportunity - Create) Verify Partners tab - Opportunity team Role with set team roles SC >> Siebel - EU " + GC.sUniqueNum;
									
			//TESTCASE BEGIN
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(e2eUser);
				
			log.info("Creating Opportunity "+oppt.sOpptDesc+" to synch to EU Siebel Instance ");
			
			//Create Opportunity
			CreateOpportunityPage createOpportunityPage = dashboard.openCreateOpportunity();
			createOpportunityPage.enterOpportunityInfo(oppt, null);
			ViewOpportunityPage viewOpportunityPage = createOpportunityPage.saveOpportunity(oppt);
			OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
			
			
			log.info("EU Opportunity " + opportunityDetailPage.getdisplayedOpportunityNumber() + " created");
					
			
		} catch (Exception e) {
			log.error(e.toString());
		}
	
		log.info("End of test method E2EOpportunityCreateVerifyPartnersTabOpportunityTeamRoleWithSetTeamRolesSC2Siebel");

	}

}
