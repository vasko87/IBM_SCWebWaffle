package com.ibm.salesconnect.test.Level1;

import java.sql.SQLException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.APIUtilities;
import com.ibm.salesconnect.API.CollabWebAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.OpportunityRestAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;


/**
 * <strong>Description:</strong>
 * <br/><br/>
 * Test to validate the Create, Read, Update-Convert to Favorite Opportunity and Search for the Favorite Opportunity functionality of the Opportunity module
 * <br/><br/>
 * 
 * @author 
 * Veena Hurukadli
 * 
 */
public class AT_FavoriteOpportunity extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(AT_Sugar.class);
	int rand = new Random().nextInt(100000);
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Open browser and log in</li>
	 * <li>Create Opportunity</li>
	 * <li>Search for created Opportunity</li>
	 * <li>Open Opportunity detail page</li>
	 * <li>Update Contact-Add to Opportunity</li>
	 * <li>Search for the Favorite Opportunity</li>
	 * </ol>
	 */
	@Test(groups = { "Level1","AT_Sugar","BVT","BVT1"})
	public void Test_AT_FavoriteOpportunity() throws SQLException, InterruptedException{
		log.info("Start of test method Test_AT_FavoriteOpportunity");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
		String headers[] = {"OAuth-Token", OAuthToken};
		String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());	

		log.info("Getting client Via API");
		PoolClient poolClient = commonClientAllocator.getGroupClient("SC");
		String clientID = poolClient.getCCMS_ID();
		String clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, clientID, headers );
			
		log.info("Creating contact via API");
		String contactFirst = "contactFirst"+rand;
		String contactLast = "contactLast"+rand;
		String contactID = "22SC-" + rand;
		SugarAPI sugarAPI = new SugarAPI();
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), contactFirst, contactLast);
		log.info("Creating Opportunity");
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
   		String opptyID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "Oppty created description", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);

		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);

		log.info("Searching for created Opportunity");
		ViewOpportunityPage viewOpportunityPage = dashboard.openViewOpportunity();
		viewOpportunityPage.searchForOpportunityID(opptyID);

		OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResultfromID(opptyID);
		opportunityDetailPage.addToFavorite();
		
		viewOpportunityPage= dashboard.openViewOpportunity();
		viewOpportunityPage.searchForOpportunityID(opptyID);
		viewOpportunityPage.selectFilterElement("My Favorites");
		opportunityDetailPage= viewOpportunityPage.selectResultfromID(opptyID);

		Assert.assertEquals(opportunityDetailPage.getDisplayedOpportunityDescription(),"Oppty created description","Incorrect opportunity detail page was opened");
	}

}
