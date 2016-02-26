package com.ibm.salesconnect.test.api.lineItems;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.APIUtilities;
import com.ibm.salesconnect.API.ApiBaseTest;
import com.ibm.salesconnect.API.CollabWebAPI;
import com.ibm.salesconnect.API.ContactRestAPI;
import com.ibm.salesconnect.API.LineItemRestAPI;
import com.ibm.salesconnect.API.OpportunityRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;

/**
 * @author Eva Farrell	
 * @date Sept 03, 2015
 */

public class POSTLineItems extends ApiBaseTest
{
	
	private static final Logger log = LoggerFactory.getLogger(POSTLineItems.class);
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private POSTLineItems(@Optional("ibm_RevenueLineItems") String apiExtension,
			@Optional("SC Auto create") String applicationName,
			@Optional("false") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);
	}
	protected String testCaseName = "";

		private TestDataHolder testData;
    int rand = new Random().nextInt(100000);

    private String clientBeanID = null;
    private String clientID = null;
    private String contactID = null;
    private String assignedUserID = null;
	private String assignedUserName = null;
	private String assignedUserName2 = null;
	private String baseID = null;
	private String urlExtension = "";
	private String token = null;
	private String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
	

	
	private void createObjects()
	{
		log.info("Start creating prerequisites");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		User user2 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		
		String baseURL = testConfig.getBrowserURL();
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		token = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());

		assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());	
		assignedUserName = user1.getDisplayName();
		assignedUserName2 = user2.getDisplayName();
		
		PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);
				
		log.info("Getting clients");
		String headers[] = {"OAuth-Token", token};
		clientID = poolClient.getCCMS_ID();
		clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, clientID, headers );
			
		log.info("Creating contacts");
		ContactRestAPI contactAPI = new ContactRestAPI();
		contactID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, clientBeanID);
		
		log.info("Creating Opportunity");
   		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
   		baseID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), token, "API created Oppty to link components to", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
   		
	}

    public void addDataFile(String filePath){
        if(this.testData== null){
            this.testData = new TestDataHolder();
        }
         this.testData.addDataLocation(filePath, ";");
     }

	@DataProvider(name="jsonProvider")
	    public Object[][] getTestData(){
		this.createObjects();
		this.addDataFile("test_config/extensions/api/lineItems/postLineItems.csv");
		return testData.getAllDataRows();
    }
	
	@Test(dataProvider = "jsonProvider")
	public void TestCreateLineItems(HashMap<String,Object> parameterValues){
		Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
        log.info("Getting user.");		
        User user = null;
		String body = "";
		String expectedResponse = "";
		
        while (it.hasNext()) 
        {
          Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
          System.out.println(pairs.getKey());
          System.out.println(pairs.getValue());
          if (pairs.getKey().equalsIgnoreCase("body")) {
        	  body=(String) pairs.getValue();	
        	  body = body.replace("*assignedUserName2*", assignedUserName2);
        	  body = body.replace("*date*", date);        	  
          }
          else if (pairs.getKey().equalsIgnoreCase("expected_response")) {
        	  expectedResponse=(String) pairs.getValue();	
          }
          else if (pairs.getKey().equalsIgnoreCase("URL")) {
			urlExtension = (String) pairs.getValue();
			urlExtension = urlExtension.replace("*opportunity-id*", baseID);
          }
          else if (pairs.getKey().equalsIgnoreCase("user")) {
        	if (pairs.getValue().equals("user2")) {
        		user = commonUserAllocator.getUser(this);
        		
			}
        	else {
        		user = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			}	
          }
          else if (pairs.getKey().equals("TC_Name")) {
				log.info("This is test " + pairs.getValue().toString());
			}
          
        }
        
		log.info("Sending POST request ");
		String postResponseString = null;
		if (urlExtension.contains("opportun_revenuelineitems")){
			OpportunityRestAPI opportunityrestAPI = new OpportunityRestAPI();
			if(super.APIm==false){
					postResponseString = opportunityrestAPI.postOpportunity(testConfig.getBrowserURL()+"rest/v10/Opportunities"+urlExtension, getOAuthToken(user), body, expectedResponse);
			}
			else if(super.APIm==true){
				postResponseString = opportunityrestAPI.postOpportunity(testConfig.getBrowserURL()+"rest/v10/opportunities"+urlExtension, getOAuthToken(user), body, expectedResponse);	
			}
		}
		else {
			LineItemRestAPI lineItemRestAPI = new LineItemRestAPI();
			postResponseString = lineItemRestAPI.postLineItem(getRequestUrl(urlExtension, null), getOAuthToken(user), body, expectedResponse);
		}
		//Verify response
		if (expectedResponse.equalsIgnoreCase("200")&&!(body.isEmpty()))
		{
			log.info("Verifying values in Post Request present in Post Response");
			try {
				JSONObject postRequest = (JSONObject)new JSONParser().parse(body);
				Iterator<Map.Entry<String, Object>> it1 = postRequest.entrySet().iterator();
				while (it1.hasNext()) 
		        {
					Map.Entry<String, Object> pairs = it1.next(); 
					String requestKey=pairs.getKey().toString();
					String requestValue=pairs.getValue().toString();
					
					
					if(requestKey.contains("alliance_partners")||requestKey.contains("currency_id")) {
						//Ignore
					}
					else if(requestKey.contains("ids")) {
						log.info("Verifying Link POST Response contains "+requestValue);
						Assert.assertTrue(postResponseString.contains(requestValue)||postResponseString.contains(requestKey)||postResponseString.contains("ids")||postResponseString.contains("related_records"),requestValue+" not found in Link Post Response as expected. Post Response: "+postResponseString);
        			}
					else if (requestKey.contains("link_name")) {
						log.info("Verifying Link POST Response contains "+requestValue);
						Assert.assertTrue(postResponseString.contains(requestValue)||postResponseString.contains(requestKey)||postResponseString.contains("ids")||postResponseString.contains("related_records"),requestValue+" not found in Link Post Response as expected. Post Response: "+postResponseString);
        			}
					
					else{
						log.info("Verifying POST Response contains "+requestKey+" and " +requestValue);
						Assert.assertTrue(postResponseString.contains(requestKey)&&postResponseString.contains(requestValue), requestKey+" and "+requestValue+" not found in Post Response as expected. Post Response: "+postResponseString);
					}
		        }
		        	

			} catch (Exception e) {
				e.printStackTrace();
				log.info(body);
				Assert.assertTrue(false, "Line Item was not created as expected.");
			}
			
			log.info("Line Item successfully created.");	    
		}
		
	}

}