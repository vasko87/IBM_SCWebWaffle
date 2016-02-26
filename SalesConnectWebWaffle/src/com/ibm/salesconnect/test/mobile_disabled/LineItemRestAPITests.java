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
import com.ibm.salesconnect.API.LineItemRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.OpportunityRestAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.objects.RevenueItem;
import com.ibm.salesconnect.test.mobile.ContactRestAPITests;

public class LineItemRestAPITests extends ProductBaseTest {	
	
	Logger log = LoggerFactory.getLogger(LineItemRestAPITests.class);
	
	@Test(groups = {"MOBILE"})
	public void Test_editRLI(){
		log.info("Start test method Test_editRLI.");
		
		log.info("Creating pre-requisites");
		String url = testConfig.getBrowserURL();
		User user = commonUserAllocator.getUser(this);
		String assignedUserName = user.getDisplayName();

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

		log.info("Creating oppty and RLI");
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String opptyID = opportunityRestAPI.createOpportunityWithRLIreturnBean(url, token, desc, accountID, contactId, "SLSP", "03", date, assignedUID, assignedUserName, rli);
		
		String rliID = opportunityRestAPI.getRLIFromOppty(url, token, opptyID).get("id").toString();
		
		log.info("Editing RLI");
		rli.setRLIAmount("26000");
		String body="{\"revenue_amount\":\""+rli.getRLIAmount()+"\"}";
		Assert.assertEquals(new LineItemRestAPI().putLineItem(url + LineItemRestAPI.lineItemURLExtension + "/" + rliID, token, body, "200").get("revenue_amount").toString(),
				rli.getRLIAmount(), 
				"The revenue amount received in the edit rli response does not match the expected") ;
		
		log.info("End test method Test_editRLI.");
	}

	
	@Test(groups = {"MOBILE"})
	public void Test_getRLI(){
		log.info("Start test method Test_getRLI.");
		
		log.info("Creating pre-requisites");
		String url = testConfig.getBrowserURL();
		User user = commonUserAllocator.getUser(this);
		String assignedUserName = user.getDisplayName();

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

		log.info("Creating oppty and RLI");
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String opptyID = opportunityRestAPI.createOpportunityWithRLIreturnBean(url, token, desc, accountID, contactId, "SLSP", "03", date, assignedUID, assignedUserName, rli);
		
		String rliID = opportunityRestAPI.getRLIFromOppty(url, token, opptyID).get("id").toString();
		
		JSONObject rliObject = new LineItemRestAPI().getLineItem(url + LineItemRestAPI.lineItemURLExtension + "/" + rliID, token, "200");
		
		Assert.assertEquals(rliObject.get("revenue_amount"),
				rli.getRLIAmount(), 
				"The revenue amount received in the get rli response does not match the expected") ;
		Assert.assertEquals(rliObject.get("id"),
				rliID, 
				"The id received in the get rli response does not match the expected") ;

		
		log.info("End test method Test_getRLI.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_deleteRLI(){
		log.info("Start test method Test_deleteRLI.");
		
		log.info("Creating pre-requisites");
		String url = testConfig.getBrowserURL();
		User user = commonUserAllocator.getUser(this);
		String assignedUserName = user.getDisplayName();

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

		log.info("Creating oppty and RLI");
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String opptyID = opportunityRestAPI.createOpportunityWithRLIreturnBean(url, token, desc, accountID, contactId, "SLSP", "1", date, assignedUID, assignedUserName, rli);
		
		String rliID = opportunityRestAPI.getRLIFromOppty(url, token, opptyID).get("id").toString();
		

		opportunityRestAPI.relateNewLineItemToOpportunity(testConfig.getBrowserURL(), token, opptyID, "500", "10", "NEWNEW", "2015-09-29", "B6000", "IFS", "17SYS", "BJH00", "BJHE0", "","-99", user.getDisplayName(), assignedUID);
		
		new LineItemRestAPI().deleteLineItem(url + LineItemRestAPI.lineItemURLExtension + "/" + rliID, token, "", "200");
		
		new LineItemRestAPI().getLineItem(url + LineItemRestAPI.lineItemURLExtension + "/" + rliID, token, "404");

		log.info("End test method Test_deleteRLI.");
	}

}
