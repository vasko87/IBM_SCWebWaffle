package com.ibm.salesconnect.test.mobile_disabled;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.APIUtilities;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.OpportunityRestAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.API.WinPlanRestAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.test.mobile.ContactRestAPITests;

/**
<br/><br/>
 * Test to validate the Create, Read, Update and delete of WinPlans
<br/><br/>
 * */
public class WinplanRestAPITests extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(WinplanRestAPITests.class);

	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>	 
	 * <li>Create test prerequisites</li>
	 * <li>Create winplan</li>
	 * <li>Get winplan</li>
	 * <li>Verify correct winplan returned</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_getWinplan(){
		log.info("Start test method Test_getWinplan.");
		
		log.info("Creating pre-requisites");
		String url = testConfig.getBrowserURL();
		User user = commonUserAllocator.getUser(this);

		String token = 	new LoginRestAPI().getOAuth2Token(url, user.getEmail(), user.getPassword());
		String session = new SugarAPI().getSessionID(url, user.getEmail(), user.getPassword());
		
		//oppty values
		String contactId = ContactRestAPITests.createContactHelper(user, log, url, token);
		String clientId = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String accountID = new APIUtilities().getClientBeanIDFromCCMSID(url, clientId, user.getEmail(), user.getPassword(), session);
		String assignedUID = new APIUtilities().getUserBeanIDFromEmail(url,user.getEmail(),user.getPassword(), session);
		String desc = "Oppty description";
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
		String source = "SLSP";
		String salesStage = "03";
		
		log.info("Creating oppty");
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String opptyID = opportunityRestAPI.createOpportunityreturnBean(url, token, desc, accountID, contactId, source, salesStage, date, assignedUID);
		
		WinPlanRestAPI winplanRestAPI = new WinPlanRestAPI();
		String winplanID = winplanRestAPI.createWinPlan(url+ OpportunityRestAPI.opportunitiesURLExtension + "/" + opptyID + "/link/opportun_winplans", token, opptyID).get("id").toString();
		
		JSONObject winplanObject = new WinPlanRestAPI().getWinPlan(url + WinPlanRestAPI.winplanURLExtension + "/" + winplanID, token, "200");
		
		Assert.assertEquals(winplanObject.get("opportunity_id"),
				opptyID, 
				"Opportunity ID does not match expected") ;
		Assert.assertEquals(winplanObject.get("opportunity_owner_id"),
				assignedUID, 
				"The assign user id does not match expected") ;

		log.info("End test method Test_getWinplan.");
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>	 
	 * <li>Create test prerequisites</li>
	 * <li>Create winplan</li>
	 * <li>Verify correct winplan returned</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_createWinplan(){
		log.info("Start test method Test_createWinplan.");
		
		log.info("Creating pre-requisites");
		String url = testConfig.getBrowserURL();
		User user = commonUserAllocator.getUser(this);

		String token = 	new LoginRestAPI().getOAuth2Token(url, user.getEmail(), user.getPassword());
		String session = new SugarAPI().getSessionID(url, user.getEmail(), user.getPassword());
		
		//oppty values
		String contactId = ContactRestAPITests.createContactHelper(user, log, url, token);
		String clientId = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String accountID = new APIUtilities().getClientBeanIDFromCCMSID(url, clientId, user.getEmail(), user.getPassword(), session);
		String assignedUID = new APIUtilities().getUserBeanIDFromEmail(url,user.getEmail(),user.getPassword(), session);
		String desc = "Oppty description";
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
		String source = "SLSP";
		String salesStage = "03";
		
		log.info("Creating oppty");
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String opptyID = opportunityRestAPI.createOpportunityreturnBean(url, token, desc, accountID, contactId, source, salesStage, date, assignedUID);
		
		WinPlanRestAPI winplanRestAPI = new WinPlanRestAPI();
		JSONObject winplan = (JSONObject) winplanRestAPI.createWinPlan(url+ OpportunityRestAPI.opportunitiesURLExtension + "/" + opptyID + "/link/opportun_winplans", token, opptyID);
		
		Assert.assertEquals(winplan.get("opportunity_id"),
				opptyID, 
				"Opportunity ID does not match expected") ;
		Assert.assertEquals(winplan.get("opportunity_owner_id"),
				assignedUID, 
				"The assign user id does not match expected") ;

		
		log.info("End test method Test_createWinplan.");
	}
	
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>	 
	 * <li>Create test prerequisites</li>
	 * <li>Create winplan</li>
	 * <li>Update the winplan</li>
	 * <li>Verify the value has been updated in the winplan</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_updateWinplan(){
		log.info("Start test method Test_updateWinplan.");
		
		log.info("Creating pre-requisites");
		String url = testConfig.getBrowserURL();
		User user = commonUserAllocator.getUser(this);

		String token = 	new LoginRestAPI().getOAuth2Token(url, user.getEmail(), user.getPassword());
		String session = new SugarAPI().getSessionID(url, user.getEmail(), user.getPassword());
		
		//oppty values
		String contactId = ContactRestAPITests.createContactHelper(user, log, url, token);
		String clientId = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String accountID = new APIUtilities().getClientBeanIDFromCCMSID(url, clientId, user.getEmail(), user.getPassword(), session);
		String assignedUID = new APIUtilities().getUserBeanIDFromEmail(url,user.getEmail(),user.getPassword(), session);
		String desc = "Oppty description";
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
		String source = "SLSP";
		String salesStage = "03";
		
		log.info("Creating oppty");
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String opptyID = opportunityRestAPI.createOpportunityreturnBean(url, token, desc, accountID, contactId, source, salesStage, date, assignedUID);
		
		WinPlanRestAPI winplanRestAPI = new WinPlanRestAPI();
		JSONObject winplan = (JSONObject) winplanRestAPI.createWinPlan(url+ OpportunityRestAPI.opportunitiesURLExtension + "/" + opptyID + "/link/opportun_winplans", token, opptyID);
		
		Assert.assertEquals(winplan.get("s03_client_needs_id"),
				false, 
				"s03_client_needs_id does not match expected") ;
		
		String winplanID = winplan.get("id").toString();
		String body = "{\"s03_client_needs_id\":true,\"id\":\"" + winplanID +"\"}";
		
		JSONObject winplanEdit = (JSONObject) winplanRestAPI.updateWinPlan(url+ WinPlanRestAPI.winplanURLExtension + "/" + winplanID, token, body, "200");

		Assert.assertEquals(winplanEdit.get("s03_client_needs_id"),
				true, 
				"s03_client_needs_id does not match expected") ;
		
		log.info("End test method Test_updateWinplan.");
	}
	
}
