/**
 * 
 */
package com.ibm.salesconnect.test.Membership;

import java.sql.SQLException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.EditTeamMembersSubpanel;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Opportunity.EditOpportunityPage;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Opportunity;

/**
 * @author evafarrell
 * @date Aug 22, 2013
 */
public class s30373NoUserAccessToGUCByOpportunity extends
		ProductBaseTest {
	
	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String opptyID = null;
	String clientID = null;
	SugarAPI sugarAPI = new SugarAPI();
	Opportunity oppty = new Opportunity();
	
	@Test(groups = {"Membership"})
	public void Test_s30373NoUserAccessToGUCByOpportunity() throws SQLException {
		Logger log = LoggerFactory.getLogger(s30373NoUserAccessToGUCByOpportunity.class);
		log.info("Start of test method Test_s30373NoUserAccessToGUCByOpportunity");
		
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		User user2 = commonUserAllocator.getGroupUser(GC.noMemUserGroup,this);
		clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();

		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		oppty.sOpptNumber = opptyID;
		oppty.sPrimaryContact = "ContactFirst ContactLast";
		oppty.sAccID = clientID;
		
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		
		log.info("Go to oppty detail page");
		ViewOpportunityPage viewOpptyPage = dashboard.openViewOpportunity();
		viewOpptyPage.searchForOpportunity(oppty);
		OpportunityDetailPage opptyDetailPage = viewOpptyPage.selectResult(oppty);
		
		int rowNumber = opptyDetailPage.getTeamMemberRowNumber(user2.getDisplayName());
		
		log.info("Edit Opportunity - Add User 2 to Oppty Team ");
		EditOpportunityPage editOpptyPage = opptyDetailPage.openEditOpportunity();
		EditTeamMembersSubpanel editTeamMemSub = new EditTeamMembersSubpanel(exec);
		
		editTeamMemSub.addOpportunityTeamMember(user2.getDisplayName(), rowNumber+1, GC.gsTeamMember);
		
		editOpptyPage.saveOpportunity();

		log.info("Logging in as User2");
		dashboard = launchWithLogin(user2);
		
		log.info("Go to oppty detail page");
		viewOpptyPage = dashboard.openViewOpportunity();
		viewOpptyPage.searchForOpportunity(oppty);
		opptyDetailPage = viewOpptyPage.selectResult(oppty);
	
		log.info("Verify that Connections Community Tab is not available to this user");
		Assert.assertFalse(viewOpptyPage.communityTabExists(), "User should not be able to view the Connections Community Tab");

		log.info("Remove the oppty created for this test");
		sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		
		log.info("End of test method Test_s30373NoUserAccessToGUCByOpportunity");
		}
		
}
