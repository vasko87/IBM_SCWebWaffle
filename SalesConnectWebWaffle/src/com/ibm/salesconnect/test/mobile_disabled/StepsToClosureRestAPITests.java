package com.ibm.salesconnect.test.mobile_disabled;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.APIUtilities;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.OpportunityRestAPI;
import com.ibm.salesconnect.API.StepsToClosureRestAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.objects.RevenueItem;
import com.ibm.salesconnect.test.mobile.ContactRestAPITests;

/**
 * <strong>Description:</strong>
 * Test to validate the Create, Read, Update and delete of Steps To Closure
 * <br />
 * <br />
 * @author 
 * Peter Poliwoda
 * 
 */
public class StepsToClosureRestAPITests extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(StepsToClosureRestAPITests.class);

	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>	 
	 * <li>Create test prerequisites</li>
	 * <li>Create an Opportunity</li>
	 * <li>Get its Steps to closure</li>
	 * <li>Verify correct id in all steps to closure returned</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_getStepsToClosure(){
		log.info("Start test method Test_getStepsToClosure.");
		
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
						
		JSONArray stepsToClosureArray = new StepsToClosureRestAPI().getStepsToClosure(url + StepsToClosureRestAPI.stepsToClosureURLExtension+ "/bean-acl?id=" + opptyID, token, "200");
		JSONObject step;
		
		for(Object o : stepsToClosureArray){
			step = (JSONObject) o;
			Assert.assertEquals(step.get("id"),	opptyID, 
					"Opportunity ID does not match expected") ;
		}

		log.info("End test method Test_getStepsToClosure.");
	}
	
		
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>	 
	 * <li>Create test prerequisites</li>
	 * <li>Create Steps to Closure</li>
	 * <li>Verify the test has created a Steps to Closure item</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"}, dataProvider = "StepsToClosure")
	public void Test_createStepsToClosure(String type, boolean isOwner){
		log.info("Start test method Test_createStepsToClosure.");
		log.info("Steps type: " + type + " Is Oppty owner: " + isOwner);
		
		log.info("Creating pre-requisites");
		
		String url = testConfig.getBrowserURL();
		User user = commonUserAllocator.getUser(this);
		String stepsToken;
		String stepsAssignedUID;
		
		String token = 	new LoginRestAPI().getOAuth2Token(url, user.getEmail(), user.getPassword());
		String session = new SugarAPI().getSessionID(url, user.getEmail(), user.getPassword());
		
		//oppty values
		String contactId = ContactRestAPITests.createContactHelper(user, log, url, token);
		String clientId = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String accountID = new APIUtilities().getClientBeanIDFromCCMSID(url, clientId, user.getEmail(), user.getPassword(), session);
		String assignedUID = new APIUtilities().getUserBeanIDFromEmail(url,user.getEmail(),user.getPassword(), session);
		String desc = "Oppty description";
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
		
		//line item values
		RevenueItem rli = new RevenueItem();
		rli.populate(true);
		rli.setRLIAmount("25000");
		String source = "SLSP";
		String salesStage = "03";
		
		stepsToken = token;
		stepsAssignedUID = assignedUID;

		if(!isOwner){
			User user2 = commonUserAllocator.getUser(this);
			stepsToken = new LoginRestAPI().getOAuth2Token(url, user2.getEmail(), user2.getPassword());
			String session2 = new SugarAPI().getSessionID(url, user2.getEmail(), user2.getPassword());
			stepsAssignedUID = new APIUtilities().getUserBeanIDFromEmail(url,user2.getEmail(),user2.getPassword(), session2);
		}
		
		String opptyID;
		String rliID;
		JSONObject stepsToClosureObject;		
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		StepsToClosureRestAPI stepsToClosureRestAPI = new StepsToClosureRestAPI();
		String stepComment = "StepsToClosure Comment of type " + type + " " + System.currentTimeMillis();
		
		if(isOwner){
			if(type=="RLC"){
				log.info("Creating oppty and RLI");
				opptyID = opportunityRestAPI.createOpportunityWithRLIreturnBean(url, token, desc, accountID, contactId, source, salesStage, date, assignedUID, user.getDisplayName());
				rliID = opportunityRestAPI.getRLIFromOppty(url, token, opptyID).get("id").toString();
				stepsToClosureObject = stepsToClosureRestAPI.createStepsToClosure(url + StepsToClosureRestAPI.stepsToClosureURLExtension, stepsToken, opptyID, stepsAssignedUID, type, rliID, stepComment, "200");	
			}
			else {
				opptyID = opportunityRestAPI.createOpportunityreturnBean(url, token, desc, accountID, contactId, source, salesStage, date, assignedUID);
				stepsToClosureObject = stepsToClosureRestAPI.createStepsToClosure(url + StepsToClosureRestAPI.stepsToClosureURLExtension, stepsToken, opptyID, stepsAssignedUID, type, stepComment, "200");	
			}
			
			Assert.assertEquals(stepsToClosureObject.get("opportunity_id"),
					opptyID, 
					"Opportunity ID does not match expected");
			Assert.assertEquals(stepsToClosureObject.get("assigned_user_id"),
					stepsAssignedUID, 
					"The assigned user id does not match expected");
			Assert.assertEquals(stepsToClosureObject.get("comments"),
					stepComment, 
					"Comment returned does not match the posted comment");
		}
		else{
			if(type=="RLC"){
				log.info("Creating oppty and RLI");
				opptyID = opportunityRestAPI.createOpportunityWithRLIreturnBean(url, token, desc, accountID, contactId, source, salesStage, date, assignedUID, user.getDisplayName());
				rliID = opportunityRestAPI.getRLIFromOppty(url, token, opptyID).get("id").toString();
				stepsToClosureObject = stepsToClosureRestAPI.createStepsToClosure(url + StepsToClosureRestAPI.stepsToClosureURLExtension, stepsToken, opptyID, stepsAssignedUID, type, rliID, stepComment, "403");	
			}
			else {
				opptyID = opportunityRestAPI.createOpportunityreturnBean(url, token, desc, accountID, contactId, source, salesStage, date, assignedUID);
				stepsToClosureObject = stepsToClosureRestAPI.createStepsToClosure(url + StepsToClosureRestAPI.stepsToClosureURLExtension, stepsToken, opptyID, stepsAssignedUID, type, stepComment, "403");	
			}
			
			Assert.assertEquals(stepsToClosureObject.get("error"),"not_authorized", 
					"Wrong error message returned.");
		}
		log.info("End test method Test_createStepsToClosure.");
	}
	
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>	 
	 * <li>Create test prerequisites</li>
	 * <li>Create Steps to Closure</li>
	 * <li>Update Steps to Closure item comment</li>
	 * <li>Verify the test has updated a Steps to Closure item</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"}, dataProvider = "StepsToClosure")
	public void Test_updateStepsToClosure(String type, boolean isOwner){
		log.info("Start test method Test_updateStepsToClosure.");
		log.info("Steps type: " + type + " Is Oppty owner: " + isOwner);
		
		log.info("Creating pre-requisites");
		
		String url = testConfig.getBrowserURL();
		User user = commonUserAllocator.getUser(this);
		String stepsToken;
		String stepsAssignedUID;
		
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
		
		stepsToken = token;
		stepsAssignedUID = assignedUID;

		if(!isOwner){
			User user2 = commonUserAllocator.getUser(this);
			stepsToken = new LoginRestAPI().getOAuth2Token(url, user2.getEmail(), user2.getPassword());
			String session2 = new SugarAPI().getSessionID(url, user2.getEmail(), user2.getPassword());
			stepsAssignedUID = new APIUtilities().getUserBeanIDFromEmail(url,user2.getEmail(),user2.getPassword(), session2);
		}
		
		String stepCommentUpdated = "Updated StepsToClosure Comment of type " + type + " " + System.currentTimeMillis();
		log.info("Creating oppty");
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();	
		
		String opptyID;
		String stepsToClosureId;
		StepsToClosureRestAPI stepsToClosureRestAPI = new StepsToClosureRestAPI();
		String body;
		
		if(type=="RLC"){
			opptyID = opportunityRestAPI.createOpportunityWithRLIreturnBean(url, token, desc, accountID, contactId, source, salesStage, date, assignedUID, user.getDisplayName());
			String rliID = opportunityRestAPI.getRLIFromOppty(url, token, opptyID).get("id").toString();
			stepsToClosureId = (String) stepsToClosureRestAPI.createStepsToClosureHelper(url + StepsToClosureRestAPI.stepsToClosureURLExtension, stepsToken, opptyID, stepsAssignedUID, type, rliID, "200").get("id");
			body = "{\"opportunity_id\":\"" + opptyID + "\",\"assigned_user_id\":\"" + assignedUID + "\",\"comments\":\"" + stepCommentUpdated +"\",\"type\":\"" + type + "\",\"rli_id\": \"" + rliID + "\"}";
		}
		else {
			opptyID = opportunityRestAPI.createOpportunityreturnBean(url, token, desc, accountID, contactId, source, salesStage, date, assignedUID);
			stepsToClosureId = (String) stepsToClosureRestAPI.createStepsToClosureHelper(url + StepsToClosureRestAPI.stepsToClosureURLExtension, stepsToken, opptyID, stepsAssignedUID, type, "200").get("id");
			body = "{\"opportunity_id\":\"" + opptyID + "\",\"assigned_user_id\":\"" + assignedUID + "\",\"comments\":\"" + stepCommentUpdated +"\",\"type\":\"" + type + "\"}";
		}
		
		JSONObject stepsToClosureObject;
		
		if(isOwner){	
			stepsToClosureObject = stepsToClosureRestAPI.updateStepsToClosure(url + StepsToClosureRestAPI.stepsToClosureURLExtension + "/" + stepsToClosureId, stepsToken, body,  "200");
			
			Assert.assertEquals(stepsToClosureObject.get("opportunity_id"),
					opptyID, 
					"Opportunity ID does not match expected");
			Assert.assertEquals(stepsToClosureObject.get("assigned_user_id"),
					stepsAssignedUID, 
					"The assigned user id does not match expected");
			Assert.assertEquals(stepsToClosureObject.get("comments"),
					stepCommentUpdated, 
					"Comment returned does not match the updated comment");
		}
		else{
			stepsToClosureObject = stepsToClosureRestAPI.updateStepsToClosure(url + StepsToClosureRestAPI.stepsToClosureURLExtension + "/" + stepsToClosureId, stepsToken, body,  "403");
			
			Assert.assertEquals(stepsToClosureObject.get("error"),"not_authorized", 
					"Wrong error message returned.");
		}
		
		log.info("End test method Test_updateStepsToClosure.");
	}
	
	
	@DataProvider(name="StepsToClosure")
    public Object[][] getTestData(){
       return new Object[][] {
				{"OPC", true},
				{"RLC", true},
				{"OSC", true},
				{"OPC", false},
				{"RLC", false},
				{"OSC", false}
			    			};
    			}
}
