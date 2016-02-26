/**
 * 
 */
package com.ibm.salesconnect.test.api.lineItems;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.ContainerFactory;
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
import com.ibm.salesconnect.objects.RevenueItem;

/**
 * @author Eva Farrell
 * @date Sept 16, 2015
 */
public class PUTLineItems extends ApiBaseTest {
	private static final Logger log = LoggerFactory.getLogger(PUTLineItems.class);
	
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private PUTLineItems(@Optional("ibm_RevenueLineItems") String apiExtension,
			@Optional("SC Auto update") String applicationName,
			@Optional("true") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);
	}
	private TestDataHolder testData;
	int rand = new Random().nextInt(100000);
	private String clientBeanID = null;
	private String accountID = null;
	private String contactID = null;
	private String assignedUserID = null;
	private String assignedUserName = null;
	private String RLIOpptyID = null;
	private String RLIOpptyID1 = null;
	private String RLIOpptyID2 = null;
	private String RLILevel20OpptyID = null;
	private String RLILevel30OpptyID = null;
	private String RLILevel40OpptyID = null;
	private String RLIID = null;
	private String RLIID1 = null;
	private String RLIID2 = null;
	private String RLIIDLevel20 = null;
	private String RLIIDLevel30 = null;
	private String RLIIDLevel40 = null;
	private String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
	
	private void createObjects()
	{
		log.info("Start creating prerequisites");
		User user = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		String baseURL = testConfig.getBrowserURL();
		PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);
		
		assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user.getEmail(), user.getPassword());	
		assignedUserName = user.getDisplayName();
		
		log.info("Getting client");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
		String headers[] = {"OAuth-Token", token};
		accountID = poolClient.getCCMS_ID();
		clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, accountID, headers );
		
		log.info("Creating contacts");
		ContactRestAPI contactAPI = new ContactRestAPI();
		contactID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, clientBeanID);
		
		log.info("Creating opportunities with RLI's");
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
   			
		//line item values
		RevenueItem rli = new RevenueItem();
		rli.populate();
		rli.sL10_OfferingType = "B3000";rli.sL15_SubBrand = "PCO";rli.sL17_SegmentLine = "";rli.sL20_BrandCode = "B3U00";rli.sL30_ProductInformation = "B3U04";rli.sL40_MachineType = "5641NV3";
		RLIOpptyID = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), token, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		RLIID = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), token, RLIOpptyID).get("id").toString();
		
		rli.sL10_OfferingType = "B3000";rli.sL15_SubBrand = "PCO";rli.sL17_SegmentLine = "";rli.sL20_BrandCode = "B3U00";rli.sL30_ProductInformation = "B3U04";rli.sL40_MachineType = "5641NV3";
		RLIOpptyID1 = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), token, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		RLIID1 = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), token, RLIOpptyID1).get("id").toString();

		rli.sL10_OfferingType = "B3000";rli.sL15_SubBrand = "PCO";rli.sL17_SegmentLine = "";rli.sL20_BrandCode = "B3U00";rli.sL30_ProductInformation = "B3U04";rli.sL40_MachineType = "5641NV3";
		RLIOpptyID2 = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), token, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		RLIID2 = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), token, RLIOpptyID2).get("id").toString();

		rli.sL10_OfferingType = "B3000";rli.sL15_SubBrand = "LSE";rli.sL17_SegmentLine = "";rli.sL20_BrandCode = "B3W00";rli.sL30_ProductInformation = "";rli.sL40_MachineType = "";
		RLILevel20OpptyID = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), token, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		RLIIDLevel20 = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), token, RLILevel20OpptyID).get("id").toString();

		rli.sL10_OfferingType = "BX700";rli.sL15_SubBrand = "CUR";rli.sL17_SegmentLine = "17CUR";rli.sL20_BrandCode = "BNU00";rli.sL30_ProductInformation = "BNU70";rli.sL40_MachineType = "";
		RLILevel30OpptyID = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), token, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		RLIIDLevel30 = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), token, RLILevel30OpptyID).get("id").toString();

		rli.populate();
		rli.sL10_OfferingType = "B7000";rli.sL15_SubBrand = "MWZ";rli.sL17_SegmentLine = "17SYZ";rli.sL20_BrandCode = "BDR00";rli.sL30_ProductInformation = "B7FH0";rli.sL40_MachineType = "5706291";
		RLILevel40OpptyID = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), token, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		RLIIDLevel40 = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), token, RLILevel40OpptyID).get("id").toString();
				
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
	this.addDataFile("test_config/extensions/api/lineItems/putLineItems.csv");
	return testData.getAllDataRows();
	}
    
	@Test(dataProvider = "jsonProvider")
	public void TestUpdateLineItem(HashMap<String,Object> parameterValues){
		Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
        log.info("Getting user.");		
        User user = null;
		String body = "";
		String expectedResponse = "";
		String urlExtension = "";
		String jsonString = "";
		
        while (it.hasNext()) 
        {
          Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
          System.out.println(pairs.getKey());
          System.out.println(pairs.getValue());
          if (pairs.getKey().equalsIgnoreCase("body")) {
        	  body=(String) pairs.getValue();	
        	  body = body.replace("*RLIID1*", RLIID1);
        	  body = body.replace("*RLIID2*", RLIID2);
         }
          else if (pairs.getKey().equalsIgnoreCase("expected_response")) {
        	  expectedResponse=(String) pairs.getValue();	
          }
          else if (pairs.getKey().equalsIgnoreCase("URL")) {
			urlExtension = (String) pairs.getValue();
			urlExtension = urlExtension.replace("*RLIOpptyID*", RLIOpptyID);
			urlExtension = urlExtension.replace("*RLIOpptyID1*", RLIOpptyID1);
			urlExtension = urlExtension.replace("*RLIOpptyID2*", RLIOpptyID2);
			urlExtension = urlExtension.replace("*RLILevel20OpptyID*", RLILevel20OpptyID);
			urlExtension = urlExtension.replace("*RLILevel30OpptyID*", RLILevel30OpptyID);
			urlExtension = urlExtension.replace("*RLILevel40OpptyID*", RLILevel40OpptyID);
			urlExtension = urlExtension.replace("*RLIID*", RLIID);
			urlExtension = urlExtension.replace("*RLIID1*", RLIID1);
			urlExtension = urlExtension.replace("*RLIID2*", RLIID2);
			urlExtension = urlExtension.replace("*RLIIDLevel20*", RLIIDLevel20);
			urlExtension = urlExtension.replace("*RLIIDLevel30*", RLIIDLevel30);
			urlExtension = urlExtension.replace("*RLIIDLevel40*", RLIIDLevel40);
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
        
		log.info("Sending PUT request");
		String token = getOAuthToken(user);
		String putResponseString = null;
		
		try{
			log.info("Sending PUT request ");
			if (urlExtension.contains("opportun_revenuelineitems")){
				OpportunityRestAPI opportunityrestAPI = new OpportunityRestAPI();
				if(super.APIm==false){
						putResponseString = opportunityrestAPI.putOpportunity(baseURL+"rest/v10/Opportunities"+urlExtension, token, body, expectedResponse);
				}
				else if(super.APIm==true){
					putResponseString = opportunityrestAPI.putOpportunity(baseURL+"rest/v10/opportunities"+urlExtension, token, body, expectedResponse);	
				}
			}
			else {
				LineItemRestAPI lineItemRestAPI = new LineItemRestAPI();
				putResponseString = lineItemRestAPI.putLineItem(getRequestUrl(urlExtension, null), token, body, expectedResponse).toString();
			}
		}
		catch (Exception e) {
			LineItemRestAPI lineItemRestAPI = new LineItemRestAPI();
			putResponseString = lineItemRestAPI.putLineItem(getRequestUrl(urlExtension, null), token, body, expectedResponse).toString();
		}
		
		//Verify response
		if (expectedResponse.equalsIgnoreCase("200")&&!(body.isEmpty()))
		{
			log.info("Verifying values in Put Request present in Put Response");
			try {
				JSONObject putRequest = (JSONObject)new JSONParser().parse(body);
				Iterator<Map.Entry<String, Object>> it1 = putRequest.entrySet().iterator();
				while (it1.hasNext()) 
		        {
					Map.Entry<String, Object> pairs = it1.next(); 
					String requestKey=pairs.getKey().toString();
					String requestValue=pairs.getValue().toString();
					if(requestKey.contains("massupdate_params")) {
						//ignore
        			}
					else if(requestKey.contains("ids")||requestKey.contains("record")) {
						log.info("Verifying Link PUT Response contains "+requestValue);
						Assert.assertTrue(putResponseString.contains(requestValue)||putResponseString.contains(requestKey)||putResponseString.contains("ids")||putResponseString.contains("related_records"),requestValue+" not found in Link Put Response as expected. Put Response: "+putResponseString);
        			}
					else if (requestKey.contains("link_name")) {
						log.info("Verifying Link PUT Response contains "+requestValue);
						Assert.assertTrue(putResponseString.contains(requestValue)||putResponseString.contains(requestKey)||putResponseString.contains("ids")||putResponseString.contains("related_records"),requestValue+" not found in Link Put Response as expected. Put Response: "+putResponseString);
        			}
					
					else{
						log.info("Verifying PUT Response contains "+requestKey+" and " +requestValue);
						Assert.assertTrue(putResponseString.contains(requestKey)&&putResponseString.contains(requestValue), requestKey+" and "+requestValue+" not found in Put Response as expected. Put Response: "+putResponseString);
					}
		        }
		        	

			} catch (Exception e) {
				e.printStackTrace();
				log.info(body);
				Assert.assertTrue(false, "Line Item was not updated as expected.");
			}
			
			log.info("Line Item successfully updated.");	    
		}

	}
	
	
/*	This is not working 
    @Test
    public void UpdateAllParameters(){
	    	log.info("Starting UpdateAllParameters");
			User user1 = commonUserAllocator.getUser(this);
			String baseURL = testConfig.getBrowserURL();
			PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);
			
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String token = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
			String headers[] = {"OAuth-Token", token};
			assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());	
			
			log.info("Getting client");
			accountID = poolClient.getCCMS_ID();
			clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, accountID, headers );
				
			log.info("Creating contact");
			SugarAPI sugarAPI = new SugarAPI();
			contactID = "Contact"+rand;			
			sugarAPI.createContact(baseURL, contactID, accountID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
			
			log.info("Creating opportunity");
			OpportunityRestAPI opportunitiesRestApi = new OpportunityRestAPI();
			String updateOpportunity = opportunitiesRestApi.createOpportunityreturnBean(baseURL, token, "API created Oppty to Test PUT Opportunity Linking Task", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
			System.out.println("Update Opportunity "+updateOpportunity);
			
			String before = getPUTInput();
			System.out.println("Before "+before);
			
			//String token = getOAuthToken(user1.getEmail(), user1.getPassword());
			String after ="";
			log.info("Sending PUT request");
			OpportunityRestAPI opportunityrestAPI = new OpportunityRestAPI();
			after = opportunityrestAPI.putOpportunity(getRequestUrl("/" + updateOpportunity, null), getOAuthToken(user1), before,  "200");
			System.out.println("After "+after);
			
			Map beforeMap = getMap(before);
			Map afterMap = getMap(after);
			
			final Map<String, Boolean> comparisonResult = compareEntries(beforeMap, afterMap);
	
		    compareBeforeAfter(comparisonResult);

			log.info("Ending UpdateAllParameters");	
	}
*/	        
	public static <K extends Comparable<? super K>, V>
	Map<K, Boolean> compareEntries(final Map<K, V> map1,
	    final Map<K, V> map2){
	    final Collection<K> allKeys = new HashSet<K>();
	    allKeys.addAll(map1.keySet());
	    allKeys.addAll(map2.keySet());
	    final Map<K, Boolean> result = new TreeMap<K, Boolean>();
	    for(final K key : allKeys){
	        result.put(key,
	            map1.containsKey(key) == map2.containsKey(key) &&
	            Boolean.valueOf(equal(map1.get(key), map2.get(key))));
	    }
	    return result;
	}
	
	private void compareBeforeAfter(Map<String, Boolean> comparisonResult){
	    for(final Entry<String, Boolean> entry : comparisonResult.entrySet()){

	    	if (!((entry.getKey().toString().equalsIgnoreCase("my_favorite"))||
	    		(entry.getKey().toString().equalsIgnoreCase("id"))||
	    		(entry.getKey().toString().equalsIgnoreCase("name"))||
	    		(entry.getKey().toString().equalsIgnoreCase("date_entered"))||
	    		(entry.getKey().toString().equalsIgnoreCase("date_modified"))||
	    		(entry.getKey().toString().equalsIgnoreCase("modified_user_id"))||	
	    		(entry.getKey().toString().equalsIgnoreCase("modified_by_name"))||	
	    		(entry.getKey().toString().equalsIgnoreCase("created_by"))||	
	    		(entry.getKey().toString().equalsIgnoreCase("created_by_name"))||
	    		(entry.getKey().toString().equalsIgnoreCase("doc_owner"))||	
	    		(entry.getKey().toString().equalsIgnoreCase("user_favorites"))||	
	    		(entry.getKey().toString().equalsIgnoreCase("description"))||	
	    		(entry.getKey().toString().equalsIgnoreCase("deleted"))||
	    		(entry.getKey().toString().equalsIgnoreCase("assigned_user_id"))||
	    		(entry.getKey().toString().equalsIgnoreCase("assigned_user_name"))||
	    		(entry.getKey().toString().equalsIgnoreCase("team_count"))||
	    		(entry.getKey().toString().equalsIgnoreCase("team_name"))||
	    		(entry.getKey().toString().equalsIgnoreCase("opportunity_type"))||
	    		(entry.getKey().toString().equalsIgnoreCase("account_name"))||
	    		(entry.getKey().toString().equalsIgnoreCase("account_id"))||
	    		(entry.getKey().toString().equalsIgnoreCase("campaign_id"))||
	    		(entry.getKey().toString().equalsIgnoreCase("campaign_name"))||
	    		(entry.getKey().toString().equalsIgnoreCase("lead_source"))||
	    		(entry.getKey().toString().equalsIgnoreCase("amount"))||
	    		(entry.getKey().toString().equalsIgnoreCase("base_rate"))||
	    		(entry.getKey().toString().equalsIgnoreCase("amount_usdollar"))||
	    		(entry.getKey().toString().equalsIgnoreCase("currency_id"))||
	    		(entry.getKey().toString().equalsIgnoreCase("currency_name"))||
	    		(entry.getKey().toString().equalsIgnoreCase("currency_symbol"))||
	    		(entry.getKey().toString().equalsIgnoreCase("date_closed"))||
	    		(entry.getKey().toString().equalsIgnoreCase("date_closed_timestamp"))||
	    		(entry.getKey().toString().equalsIgnoreCase("next_step"))||
	    		(entry.getKey().toString().equalsIgnoreCase("sales_stage"))||
	    		(entry.getKey().toString().equalsIgnoreCase("sales_status"))||
	    		(entry.getKey().toString().equalsIgnoreCase("probability"))||
	    		(entry.getKey().toString().equalsIgnoreCase("best_case"))||
	    		(entry.getKey().toString().equalsIgnoreCase("worst_case"))||
	    		(entry.getKey().toString().equalsIgnoreCase("commit_stage"))||
	    		(entry.getKey().toString().equalsIgnoreCase("total_revenue_line_items"))||
	    		(entry.getKey().toString().equalsIgnoreCase("closed_revenue_line_items"))||
	    		(entry.getKey().toString().equalsIgnoreCase("contact_role"))||
	    		(entry.getKey().toString().equalsIgnoreCase("mkto_sync"))||
	    		(entry.getKey().toString().equalsIgnoreCase("mkto_id"))||
	    		(entry.getKey().toString().equalsIgnoreCase("date_closed_days_out"))||
	    		(entry.getKey().toString().equalsIgnoreCase("assignment_date"))||
	    		(entry.getKey().toString().equalsIgnoreCase("user_role"))||
	    		(entry.getKey().toString().equalsIgnoreCase("acl_prefetch"))||
	    		(entry.getKey().toString().equalsIgnoreCase("reason_lost_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("restricted"))||
	    		(entry.getKey().toString().equalsIgnoreCase("reason_won_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("risk_assessment"))||
	    		(entry.getKey().toString().equalsIgnoreCase("roadmap_status_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("team_member_identifier"))||
	    		(entry.getKey().toString().equalsIgnoreCase("sales_stage_qualified"))||
	    		(entry.getKey().toString().equalsIgnoreCase("owner_acceptance_status_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("prev_assigned_user_id"))||
	    		(entry.getKey().toString().equalsIgnoreCase("ext_ref_id2_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("last_updating_system_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("last_updating_system_date_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("international_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("external_identifiers_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("ext_ref_id1_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("assigned_bp_id"))||
	    		(entry.getKey().toString().equalsIgnoreCase("assigned_bp_name"))||
	    		(entry.getKey().toString().equalsIgnoreCase("solution_codes_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("team_members"))||
	    		(entry.getKey().toString().equalsIgnoreCase("survey_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("currency_iso4217"))||
	    		(entry.getKey().toString().equalsIgnoreCase("contact_id_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("campaign_code_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("cross_lob"))||
	    		(entry.getKey().toString().equalsIgnoreCase("related_opportunities"))||
	    		(entry.getKey().toString().equalsIgnoreCase("version"))||
	    		(entry.getKey().toString().equalsIgnoreCase("edit_version"))||
	    		(entry.getKey().toString().equalsIgnoreCase("additional_ibm_businesspartners"))||
	    		(entry.getKey().toString().equalsIgnoreCase("update_contacts_last_interaction_date"))||
	    		(entry.getKey().toString().equalsIgnoreCase("business_name_kana"))||
	    		(entry.getKey().toString().equalsIgnoreCase("contact_name_kana"))||
	    		(entry.getKey().toString().equalsIgnoreCase("pcontact_id_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("rel_pcontact_id_c_first_name"))||
	    		(entry.getKey().toString().equalsIgnoreCase("rel_pcontact_id_c_preferred_name_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("rel_pcontact_id_c_last_name"))||
	    		(entry.getKey().toString().equalsIgnoreCase("cmr_id"))||
	    		(entry.getKey().toString().equalsIgnoreCase("cmr_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("additional_users"))||
	    		(entry.getKey().toString().equalsIgnoreCase("assigned_user_name_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("offering_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("tags"))||
	    		(entry.getKey().toString().equalsIgnoreCase("mergedtags"))||
	    		(entry.getKey().toString().equalsIgnoreCase("relate_beans"))||
	    		(entry.getKey().toString().equalsIgnoreCase("ibm_visibility"))||
	    		(entry.getKey().toString().equalsIgnoreCase("sales_stage_values"))||
	    		(entry.getKey().toString().equalsIgnoreCase("lead_source_values"))||
	    		(entry.getKey().toString().equalsIgnoreCase("facet_myitems_igf_owner"))||
	    		(entry.getKey().toString().equalsIgnoreCase("assigned_user_name_address_country"))||
	    		(entry.getKey().toString().equalsIgnoreCase("id_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("probability_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("duration_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("fcast_date_sign_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("fcast_date_tran_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("level10_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("level15_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("level17_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("level20_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("level30_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("level40_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("revenue_amount_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("competitor_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("roadmap_status_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("rli_browseSolution_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("revenue_type_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("green_blue_revenue_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("stg_signings_type_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("stg_fulfill_type_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("srv_work_type_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("swg_contract_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("swg_book_new_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("swg_book_rnwl_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("swg_book_serv_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("swg_tran_det_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("swg_sign_det_template"))||
	    		(entry.getKey().toString().equalsIgnoreCase("opportun_revenuelineitemrow"))||
	    		(entry.getKey().toString().equalsIgnoreCase("account_client_id"))||
	    		(entry.getKey().toString().equalsIgnoreCase("_acl"))||
	    		(entry.getKey().toString().equalsIgnoreCase("_module"))||
	    		(entry.getKey().toString().equalsIgnoreCase("doc_owner"))||
	    		(entry.getKey().toString().equalsIgnoreCase("user_favorites"))||
	    		(entry.getKey().toString().equalsIgnoreCase("deleted"))||
	    		(entry.getKey().toString().equalsIgnoreCase("currency_id"))||
	    		(entry.getKey().toString().equalsIgnoreCase("sales_stage"))||
	    		(entry.getKey().toString().equalsIgnoreCase("mkto_sync"))||
	    		(entry.getKey().toString().equalsIgnoreCase("date_closed_days_out"))||
	    		(entry.getKey().toString().equalsIgnoreCase("contact_id_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("prev_assigned_user_id"))||
	    		(entry.getKey().toString().equalsIgnoreCase("restricted"))||
	    		(entry.getKey().toString().equalsIgnoreCase("risk_assessment"))||
	    		(entry.getKey().toString().equalsIgnoreCase("sales_stage_qualified"))||
	    		(entry.getKey().toString().equalsIgnoreCase("international_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("survey_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("update_contacts_last_interaction_date"))||
	    		(entry.getKey().toString().equalsIgnoreCase("solution_codes_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("assigned_bp_id"))||
	    		(entry.getKey().toString().equalsIgnoreCase("owner_acceptance_status_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("assigned_user_id"))||
	    		(entry.getKey().toString().equalsIgnoreCase("date_closed_timestamp"))||
	    		(entry.getKey().toString().equalsIgnoreCase("closed_revenue_line_items"))||
	    		(entry.getKey().toString().equalsIgnoreCase("base_rate"))||
	    		(entry.getKey().toString().equalsIgnoreCase("date_closed"))||
	    		(entry.getKey().toString().equalsIgnoreCase("additional_users"))||
	    		(entry.getKey().toString().equalsIgnoreCase("lead_source"))||
	    		(entry.getKey().toString().equalsIgnoreCase("opportunity_owner_type"))||
	    		(entry.getKey().toString().equalsIgnoreCase("probability"))||
	    		(entry.getKey().toString().equalsIgnoreCase("description"))||
	    		(entry.getKey().toString().equalsIgnoreCase("account_id"))||
	    		(entry.getKey().toString().equalsIgnoreCase("cmr_id"))||
	    		(entry.getKey().toString().equalsIgnoreCase("cross_lob"))||
	    		(entry.getKey().toString().equalsIgnoreCase("tags"))||
	    		(entry.getKey().toString().equalsIgnoreCase("opportun_revenuelineitems"))||
	    		(entry.getKey().toString().equalsIgnoreCase("webtoopportunity_invalid_email")))){
		    	if (!entry.getValue()) {	
						        Assert.assertTrue(false, "Entry incorrect: " + entry.getKey() + ", value: "
							            + entry.getValue());
		    							}
	    	}
	    }
	}

				
			
	    

	private static boolean equal(final Object obj1, final Object obj2){
	    return obj1 == obj2 || (obj1 != null && obj1.equals(obj2));
	}
	
	private Map getMap(String jsonText){
		  JSONParser parser = new JSONParser();
		  ContainerFactory containerFactory = new ContainerFactory(){
		    public List creatArrayContainer() {
		      return new LinkedList();
		    }

		    public Map createObjectContainer() {
		      return new LinkedHashMap();
		    }
		                        
		  };
		    Map json = null;            
		  try{
		    json = (Map)parser.parse(jsonText, containerFactory);
		    Iterator iter = json.entrySet().iterator();
		    while(iter.hasNext()){
		      Map.Entry entry = (Map.Entry)iter.next();
		    }
		  }
		  catch(Exception e){
		    System.out.println(e);
		  }
		 return json;
	}
    
    
	private String getPUTInput(){
		JSONParser parser = new JSONParser();
		try{
			Object obj = parser.parse(new FileReader("test_config/extensions/api/opportunity/put.json"));
			org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
			return jsonObject.toString();
		}
		catch (Exception e) {
			log.info("Returning empty string as no states could be found");
		}
		return "";    
	}
    
    /**
     * Get the oauth string
     */
	public String getOAuthExtension(){		
		return "oauth?" + clientIDandSecret;
	}
}
