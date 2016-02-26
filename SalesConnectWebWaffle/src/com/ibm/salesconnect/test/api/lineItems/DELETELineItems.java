
/**
 * 
 */
package com.ibm.salesconnect.test.api.lineItems;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.APIUtilities;
import com.ibm.salesconnect.API.ApiBaseTest;
import com.ibm.salesconnect.API.CollabWebAPI;
import com.ibm.salesconnect.API.ContactRestAPI;
import com.ibm.salesconnect.API.OpportunityRestAPI;
import com.ibm.salesconnect.API.LineItemRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.objects.RevenueItem;

/**
 * @author evafarrell
 * @date Sept 23, 2015
 */
public class DELETELineItems extends ApiBaseTest {
	private static final Logger log = LoggerFactory.getLogger(DELETELineItems.class);
	
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private DELETELineItems(@Optional("ibm_RevenueLineItems") String apiExtension,
			@Optional("SC Auto delete") String applicationName,
			@Optional("true") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);
	}

	private TestDataHolder testData;
    int rand = new Random().nextInt(100000);
	private String contactID = null;
	private String opptyID = null;
	private String clientBeanID = null;
	private String assignedUserID = null;
	private String clientID = null;
	private String baseOpptyID = null;
	private String favRLI = null;
	private String subRLI = null;
	private String rliID = null;
	private String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
	
	User user1 = null;
	User user2 = null;
	private String assignedUserName;
	private String rliID1 = null;
	private String rliID2 = null;
	private String rliOpptyID;
	private String rliOpptyID1;
	private String rliOpptyID2;
	private String subRLIOppty;
	private String favRLIOppty;
	private String rliOpptyID3;
	private String rliID3;
	private String rliOpptyID4;
	private String rliID4;
	
	private void createObjects(){
		log.info("Start creating prerequisites");

		user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		user2 = commonUserAllocator.getUser(this);
		String baseURL = testConfig.getBrowserURL();
		assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());	
		assignedUserName = user1.getDisplayName();
		PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);
		
		log.info("Getting client");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
		String headers[] = {"OAuth-Token", OAuthToken};
		
		clientID = poolClient.getCCMS_ID();
		clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, clientID, headers );
	
		log.info("Creating contacts");
		ContactRestAPI contactAPI = new ContactRestAPI();
		contactID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, clientBeanID);
		
		log.info("Creating opportunities with RLI's");
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		opptyID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Base Oppty to test DELETE Opportunity", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
			
		//line item values
		RevenueItem rli = new RevenueItem();
		LineItemRestAPI lineItemRestAPI = new LineItemRestAPI();
		rli.populate();
		
		rli.sL10_OfferingType = "B3000";rli.sL15_SubBrand = "PCO";rli.sL17_SegmentLine = "";rli.sL20_BrandCode = "B3U00";rli.sL30_ProductInformation = "B3U04";rli.sL40_MachineType = "5641NV3";
		rliOpptyID = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), OAuthToken, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		rliID = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), OAuthToken, rliOpptyID).get("id").toString();
		opportunityRestAPI.relateNewLineItemToOpportunity(testConfig.getBrowserURL(), OAuthToken, rliOpptyID, "500", "10", "", date, "B3000", "PCO", "", "B3U00", "B3U04", "5641NV3","-99", assignedUserName, assignedUserID);
		
		rli.sL10_OfferingType = "B3000";rli.sL15_SubBrand = "PCO";rli.sL17_SegmentLine = "";rli.sL20_BrandCode = "B3U00";rli.sL30_ProductInformation = "B3U04";rli.sL40_MachineType = "5641NV3";
		rliOpptyID1 = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), OAuthToken, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		rliID1 = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), OAuthToken, rliOpptyID1).get("id").toString();
		opportunityRestAPI.relateNewLineItemToOpportunity(testConfig.getBrowserURL(), OAuthToken, rliOpptyID1, "500", "10", "", date, "B3000", "PCO", "", "B3U00", "B3U04", "5641NV3","-99", assignedUserName, assignedUserID);
		
		rli.sL10_OfferingType = "B3000";rli.sL15_SubBrand = "PCO";rli.sL17_SegmentLine = "";rli.sL20_BrandCode = "B3U00";rli.sL30_ProductInformation = "B3U04";rli.sL40_MachineType = "5641NV3";
		rliOpptyID2 = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), OAuthToken, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		rliID2 = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), OAuthToken, rliOpptyID2).get("id").toString();
		opportunityRestAPI.relateNewLineItemToOpportunity(testConfig.getBrowserURL(), OAuthToken, rliOpptyID2, "500", "10", "", date, "B3000", "PCO", "", "B3U00", "B3U04", "5641NV3","-99", assignedUserName, assignedUserID);
		
		rli.sL10_OfferingType = "B3000";rli.sL15_SubBrand = "PCO";rli.sL17_SegmentLine = "";rli.sL20_BrandCode = "B3U00";rli.sL30_ProductInformation = "B3U04";rli.sL40_MachineType = "5641NV3";
		rliOpptyID3 = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), OAuthToken, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		rliID3 = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), OAuthToken, rliOpptyID3).get("id").toString();

		rli.sL10_OfferingType = "B3000";rli.sL15_SubBrand = "PCO";rli.sL17_SegmentLine = "";rli.sL20_BrandCode = "B3U00";rli.sL30_ProductInformation = "B3U04";rli.sL40_MachineType = "5641NV3";
		rliOpptyID4 = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), OAuthToken, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "04", date, assignedUserID, assignedUserName, rli);
		rliID4 = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), OAuthToken, rliOpptyID4).get("id").toString();
		opportunityRestAPI.relateNewLineItemToOpportunity(testConfig.getBrowserURL(), OAuthToken, rliOpptyID4, "500", "10", "", date, "B3000", "PCO", "", "B3U00", "B3U04", "5641NV3","-99", assignedUserName, assignedUserID);

		rli.sL10_OfferingType = "B3000";rli.sL15_SubBrand = "PCO";rli.sL17_SegmentLine = "";rli.sL20_BrandCode = "B3U00";rli.sL30_ProductInformation = "B3U04";rli.sL40_MachineType = "5641NV3";
		favRLIOppty = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), OAuthToken, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		favRLI = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), OAuthToken, favRLIOppty).get("id").toString();
//		lineItemRestAPI.postLineItem(getRequestUrl(favRLI+"/favorite", null),OAuthToken,"", "200");
		opportunityRestAPI.relateNewLineItemToOpportunity(testConfig.getBrowserURL(), OAuthToken, favRLIOppty, "500", "10", "", date, "B3000", "PCO", "", "B3U00", "B3U04", "5641NV3","-99", assignedUserName, assignedUserID);
		
		rli.sL10_OfferingType = "B3000";rli.sL15_SubBrand = "PCO";rli.sL17_SegmentLine = "";rli.sL20_BrandCode = "B3U00";rli.sL30_ProductInformation = "B3U04";rli.sL40_MachineType = "5641NV3";
		subRLIOppty = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), OAuthToken, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		subRLI = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), OAuthToken, subRLIOppty).get("id").toString();
		lineItemRestAPI.postLineItem(getRequestUrl(subRLI+"/subscribe", null),OAuthToken,"", "200");
		opportunityRestAPI.relateNewLineItemToOpportunity(testConfig.getBrowserURL(), OAuthToken, subRLIOppty, "500", "10", "", date, "B3000", "PCO", "", "B3U00", "B3U04", "5641NV3","-99", assignedUserName, assignedUserID);
		
		log.info("Finish creating prerequisites");
	}
	
    public void addDataFile(String filePath){
        if(this.testData== null){
            this.testData = new TestDataHolder();
        }
         this.testData.addDataLocation(filePath);
     }

     	@DataProvider(name="jsonProvider")
	    public Object[][] getTestData(){
		this.createObjects();
		this.addDataFile("test_config/extensions/api/lineItems/deleteLineItem.csv");
		return testData.getAllDataRows();
    }
	
	@Test(dataProvider = "jsonProvider")
	public void TestDeleteLineItem(HashMap<String,Object> parameterValues){
		Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
        log.info("Getting user.");		
     	String expectedResponseCode = null;
     	String urlOpportunityID = null;
     	String urlExtension = null;
     	String relatedObject = null;
     	
     	User user = null;
		
         while (it.hasNext()) {
             Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
             System.out.println(pairs.getKey());
             System.out.println(pairs.getValue());
             if(pairs.getKey().equals("expectedResponse")){
             	expectedResponseCode=(String) pairs.getValue();	
             	it.remove();
             }
             else if (pairs.getKey().equals("TC_Name")) {
 				log.info("This is test " + pairs.getValue().toString());
 			}
             else if (pairs.getKey().equalsIgnoreCase("url")){
 				urlOpportunityID = geturlOpportunityID((String) pairs.getValue());
 			}
             else if (pairs.getKey().equalsIgnoreCase("user")){
  				if (pairs.getValue().toString().equalsIgnoreCase("same")) {
					user = user1;
				}
  				else if (pairs.getValue().toString().equalsIgnoreCase("other")) {
					user = user2;
				}
  			}
             else if (pairs.getKey().equalsIgnoreCase("urlextension")) {
 				if (pairs.getValue().toString().equalsIgnoreCase("*blank*")) {
 					urlExtension = "";
 				}
 				else {
 					urlExtension = pairs.getValue().toString();
 				}
 			}
             else if (pairs.getKey().equalsIgnoreCase("related")) {
 				if (pairs.getValue().toString().equalsIgnoreCase("*blank*")) {
 					relatedObject = "";
 				}
 				else {
 					relatedObject = getRelatedObjectID(pairs.getValue().toString());
 				}
 				
 			}
         }  
		
		//Build request URL
		String url = "";
		if (!urlOpportunityID.equalsIgnoreCase("")) {
			url+=urlOpportunityID;
		}
		if (!urlExtension.equalsIgnoreCase("")) {
			url+=urlExtension;
		}
		if (!relatedObject.equalsIgnoreCase("")) {
			url+="/";
			url+=relatedObject;
		}	
		System.out.println("URL is "+url);
	
	
		log.info("Sending DELETE request");
		String token = getOAuthToken(user);
		
		try{
			log.info("Sending DELETE request ");
			if (urlExtension.contains("opportun_revenuelineitems")||url.contains("opportun_revenuelineitems")){
				OpportunityRestAPI opportunityrestAPI = new OpportunityRestAPI();
				if(super.APIm==false){
					
					opportunityrestAPI.deleteOpportunity(baseURL+"rest/v10/Opportunities/"+url, token,"", expectedResponseCode);
				}
				else if(super.APIm==true){
					opportunityrestAPI.deleteOpportunity(baseURL+"rest/v10/opportunities/"+url, token,"", expectedResponseCode);
				}
			}
			else {
				LineItemRestAPI lineItemRestAPI = new LineItemRestAPI();
				lineItemRestAPI.deleteLineItem(getRequestUrl(url, null), token,"", expectedResponseCode);
			}
		}
		catch (Exception e) {
			LineItemRestAPI lineItemRestAPI = new LineItemRestAPI();
			lineItemRestAPI.deleteLineItem(getRequestUrl(url, null), token,"", expectedResponseCode);
		}
		log.info("Verify Response");
		if (expectedResponseCode.equalsIgnoreCase("200")) {
			if (!relatedObject.equalsIgnoreCase("")) {
				LineItemRestAPI lineItemRestAPI = new LineItemRestAPI();
				lineItemRestAPI.getLineItem(getRequestUrl(url, null), new LoginRestAPI().getOAuth2Token(baseURL, user.getEmail(), user.getPassword()), "404");
				
			}
		}	
       }
     
     @Test
     public void MassDelete(){
    	log.info("Getting User");
    	User user = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		
    	log.info("Getting token");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
		String headers[] = {"OAuth-Token", OAuthToken};
		assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user.getEmail(), user.getPassword());	
		assignedUserName = user1.getDisplayName();

		log.info("Getting client");
		PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);
		clientID = poolClient.getCCMS_ID();
		clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, clientID, headers );

		log.info("Creating contacts");
		ContactRestAPI contactAPI = new ContactRestAPI();
		contactID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, clientBeanID);
		
		log.info("Creating opportunities with RLI's for Mass Delete");
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();

		//line item values
		RevenueItem rli = new RevenueItem();
		LineItemRestAPI lineItemRestAPI = new LineItemRestAPI();
		rli.populate();
		
		rli.sL10_OfferingType = "B3000";rli.sL15_SubBrand = "PCO";rli.sL17_SegmentLine = "";rli.sL20_BrandCode = "B3U00";rli.sL30_ProductInformation = "B3U04";rli.sL40_MachineType = "5641NV3";
		String OpptyID = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), OAuthToken, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		String RLIID = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), OAuthToken, OpptyID).get("id").toString();
		opportunityRestAPI.relateNewLineItemToOpportunity(testConfig.getBrowserURL(), OAuthToken, OpptyID, "500", "10", "", date, "B3000", "PCO", "", "B3U00", "B3U04", "5641NV3","-99", user.getDisplayName(), assignedUserID);
		
		rli.sL10_OfferingType = "B3000";rli.sL15_SubBrand = "PCO";rli.sL17_SegmentLine = "";rli.sL20_BrandCode = "B3U00";rli.sL30_ProductInformation = "B3U04";rli.sL40_MachineType = "5641NV3";
		OpptyID = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), OAuthToken, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		String RLIID1 = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), OAuthToken, OpptyID).get("id").toString();
		opportunityRestAPI.relateNewLineItemToOpportunity(testConfig.getBrowserURL(), OAuthToken, OpptyID, "500", "10", "", date, "B3000", "PCO", "", "B3U00", "B3U04", "5641NV3","-99", user.getDisplayName(), assignedUserID);
		
		rli.sL10_OfferingType = "B3000";rli.sL15_SubBrand = "PCO";rli.sL17_SegmentLine = "";rli.sL20_BrandCode = "B3U00";rli.sL30_ProductInformation = "B3U04";rli.sL40_MachineType = "5641NV3";
		OpptyID = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), OAuthToken, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		String RLIID2 = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), OAuthToken, OpptyID).get("id").toString();
		opportunityRestAPI.relateNewLineItemToOpportunity(testConfig.getBrowserURL(), OAuthToken, OpptyID, "500", "10", "", date, "B3000", "PCO", "", "B3U00", "B3U04", "5641NV3","-99", user.getDisplayName(), assignedUserID);
		
		String body = "{\"massupdate_params\":{\"uid\":[\""+RLIID+"\", \""+RLIID1+"\",\""+RLIID2+"\"]}}";
		String token3 = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());	
		
		lineItemRestAPI.deleteLineItem(getRequestUrl("/MassUpdate", null), OAuthToken,body, "200");
 		
 		log.info("Check RLI's got deleted after Mass Delete");
 		lineItemRestAPI.getLineItem(getRequestUrl(RLIID, null), token3, "404");
 		lineItemRestAPI.getLineItem(getRequestUrl(RLIID1, null), token3, "404");
 		lineItemRestAPI.getLineItem(getRequestUrl(RLIID2, null), token3, "404");
     }

     @Test
     public void deleteSalesStage04To03(){
    	log.info("Getting User");
    	User user = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		
    	log.info("Getting token");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
		String headers[] = {"OAuth-Token", OAuthToken};
		assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user.getEmail(), user.getPassword());	
		assignedUserName = user1.getDisplayName();

		log.info("Getting client");
		PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);
		clientID = poolClient.getCCMS_ID();
		clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, clientID, headers );

		log.info("Creating contacts");
		ContactRestAPI contactAPI = new ContactRestAPI();
		contactID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, clientBeanID);
		
		log.info("Creating Sales Stage 04 opportunity with RLI's");
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		//line item values
		RevenueItem rli = new RevenueItem();
		LineItemRestAPI lineItemRestAPI = new LineItemRestAPI();
		rli.populate();
		
		rli.sL10_OfferingType = "B3000";rli.sL15_SubBrand = "PCO";rli.sL17_SegmentLine = "";rli.sL20_BrandCode = "B3U00";rli.sL30_ProductInformation = "B3U04";rli.sL40_MachineType = "5641NV3";
		String OpptyID = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), OAuthToken, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "04", date, assignedUserID, assignedUserName, rli);
		String RLIID = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), OAuthToken, OpptyID).get("id").toString();
		opportunityRestAPI.relateNewLineItemToOpportunity(testConfig.getBrowserURL(), OAuthToken, OpptyID, "500", "10", "", date, "B3000", "PCO", "", "B3U00", "B3U04", "5641NV3","-99", user.getDisplayName(), assignedUserID);
		
		log.info("Updating Sales Stage to 03");
		String body = "{\"sales_stage\":\"03\"}";
		opportunityRestAPI.putOpportunity(baseURL+"rest/v10/Opportunities/"+OpptyID, OAuthToken, body, "200");
		
		log.info("Delete RLI and ensure it cannot be deleted");
		lineItemRestAPI.deleteLineItem(getRequestUrl(RLIID, null), OAuthToken,"", "404");

     }
     
     private String geturlOpportunityID(String csvOpportunity){
     	if (csvOpportunity.equalsIgnoreCase("opptyID")) {
 			return opptyID;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("rliOpptyID2")) {
  			return rliOpptyID2;
  		}
      	else if (csvOpportunity.equalsIgnoreCase("rliID")) {
 			return rliID;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("rliOpptyID1")) {
 			return rliOpptyID1;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("rliID3")) {
 			return rliID3;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("rliID4")) {
 			return rliID4;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("favRLI")) {
 			return favRLI;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("subRLI")) {
 			return subRLI;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("*blank*")) {
 			return "";
 		}
     	else if (csvOpportunity.contains("invalidRLI")) {
 			return "invalid";
 		}
     	else {
 			return null;
 		}
     }
     
     private String getRelatedObjectID(String relatedFromCSV){
     	if (relatedFromCSV.equalsIgnoreCase("validRLI2")) {
 			return rliID2;
 		}
     	else if (relatedFromCSV.contains("validRLI1")) {
 			return "rliID1";
 		}
     	else if (relatedFromCSV.contains("invalidRLI")) {
 			return "invalidID";
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("*blank*")) {
 			return "";
 		}
     	return null;
     }
     
     /**
      * Get the oauth string
      */
 	public String getOAuthExtension(){		
 		return "oauth?" + clientIDandSecret;
 	}
     
}