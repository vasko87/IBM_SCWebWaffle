package com.ibm.salesconnect.test.Level0;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.CallRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Call.CallDetailPage;
import com.ibm.salesconnect.model.standard.Call.CreateCallPage;
import com.ibm.salesconnect.model.standard.Call.ViewCallPage;
import com.ibm.salesconnect.objects.Call;


/**
 * <strong>Description:</strong>
 * <br/><br/>
 * Test to validate the Create, Read, Update and Delete functionality of the Calls module
 * <br/><br/>
 * 
 * @author 
 * Tim Lehane
 * 

 */
public class AT_Call extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(AT_Call.class);
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Open browser and log in</li>
	 * <li>Create Call</li>
	 * <li>Search for created Call</li>
	 * <li>Open Call detail page</li>
	 * <li>Update Call description</li>
	 * <li>Delete Call</li>
	 * <li>Confirm deletion</li>
	 * </ol>
	 */
	@Test(groups = { "Level1","AT_Sugar","BVT", "BVT0"})
	public void Test_AT_Call(){
		log.info("Start of test method Test_AT_Call");

		Call call = new Call();
		call.populate();
		User user1 = commonUserAllocator.getUser(this);

		Dashboard dashboard = launchWithLogin(user1);

		log.info("Create a call");
		CreateCallPage createCallPage = dashboard.openCreateCall();
		createCallPage.enterCallInfo(call);
		createCallPage.saveCall();

		log.info("Searching for created Call");
		ViewCallPage viewCallPage = dashboard.openViewCall();
		viewCallPage.searchForCall(call);
		CallDetailPage callDetailPage = viewCallPage.selectResult(call);
		
		Assert.assertEquals(callDetailPage.getdisplayedCallSubject().trim(),call.sSubject,"Incorrect call detail page was opened");
		
		log.info("Editing call");
		CreateCallPage editCallPage = callDetailPage.openEditPage();
		call.setCallSubject(call.sSubject + "Edited");
		editCallPage.editSubject(call.sSubject);
		editCallPage.saveCall();
		
		log.info("Searching for edited Call");
		ViewCallPage viewEditedCallPage = dashboard.openViewCall();
		viewEditedCallPage.searchForCall(call);
		CallDetailPage callEditDetailPage = viewEditedCallPage.selectResult(call);
		
		Assert.assertEquals(callEditDetailPage.getdisplayedCallSubject().trim(),call.sSubject,"Incorrect call detail page was opened");
		
		callEditDetailPage.deleteCall();
	
		log.info("Confirming call has been deleted via API");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword());
		
		CallRestAPI callRestAPI = new CallRestAPI();
		callRestAPI.getCall(testConfig.getBrowserURL(), token, call.getCallSubject(), "404");
		
		log.info("End of test method Test_AT_Call");
	}

}
