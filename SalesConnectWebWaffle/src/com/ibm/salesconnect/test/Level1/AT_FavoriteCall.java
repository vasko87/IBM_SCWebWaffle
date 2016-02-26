package com.ibm.salesconnect.test.Level1;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.APIUtilities;
import com.ibm.salesconnect.API.CallRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Call.CallDetailPage;
import com.ibm.salesconnect.model.standard.Call.ViewCallPage;

/**
 * <strong>Description:</strong>
 * <br/><br/>
 * Test to validate the Create, Read, Convert to Favorite and Search for Favorite Call functionality of the Calls module
 * <br/><br/>
 * 
 * @author 
 * Christeena J Prabhu
 * 

 */
public class AT_FavoriteCall extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(AT_Sugar.class);
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Open browser and log in</li>
	 * <li>Create Call</li>
	 * <li>Search for created Call</li>
	 * <li>Open Call detail page</li>
	 * <li>Convert as Favorite</li>
	 * <li>Search for Favorite Call</li>
	 * </ol>
	 */
	
	@Test(groups = { "Level1","AT_Sugar","BVT1"})
	public void Test_AT_FavoriteCall() throws SQLException, InterruptedException{


		log.info("Start of test method Test_AT_FavoriteCall");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
		String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());	

		log.info("Creating call via API");
		CallRestAPI callRestAPI = new CallRestAPI();
		String callSubject = "Favorite Call Subject";
		callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, callSubject, "2013-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);

		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);

		log.info("Searching for created Call");
		ViewCallPage viewCallPage = dashboard.openViewCall();
		viewCallPage.searchForCall(callSubject, false, false);

		CallDetailPage callDetailPage = viewCallPage.selectResult(callSubject);
		callDetailPage.addCallToMyFavorites();

		viewCallPage= dashboard.openViewCall();
		viewCallPage.searchForCall(callSubject, false, true);
		callDetailPage= viewCallPage.selectResult(callSubject);

		Assert.assertEquals(callDetailPage.getdisplayedCallSubject().trim(),callSubject.trim(),"Incorrect call detail page was opened");
	}
	

}
