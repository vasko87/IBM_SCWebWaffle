package com.ibm.salesconnect.test.api.opportunities;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONObject;
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
import com.ibm.salesconnect.API.LeadsRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.MetaUtilities;
import com.ibm.salesconnect.API.OpportunityRestAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.API.YamlUtilities;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;


public class yamlTestFile extends ApiBaseTest
{

	private static final Logger log = LoggerFactory.getLogger(yamlTestFile.class);
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private yamlTestFile(@Optional("opportunities") String apiExtension,
			@Optional("SC Auto create") String applicationName,
			@Optional("false") String APIM,
			@Optional("development") String environment) { 

		super(apiExtension, applicationName, APIM, environment);
	}
	

    int rand = new Random().nextInt(100000);
    String assignedUserID = "";
    String assigned_user_name = "";
    String clientID = "";
    String clientBeanID = "";
    String contactID = "";
    String token = "";
    String oppty = "";
    private User user = null;

	private Object[][] loadYamlFile=null;

	
	private void createYamlObjects(){
		user = commonUserAllocator.getUser(this);
		assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user.getEmail(), user.getPassword());
		assigned_user_name = user.getDisplayName();

		PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);
		token = new LoginRestAPI().getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
		String headers[] = {"OAuth-Token", token};
		clientID = poolClient.getCCMS_ID();
		clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, clientID, headers );
		
		ContactRestAPI contactAPI = new ContactRestAPI();
		contactID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, clientBeanID);

		SugarAPI sugarAPI = new SugarAPI();
		//String opptyID = sugarAPI.createOppty(baseURL, "", contactID, clientID, user.getEmail(), user.getPassword(), GC.emptyArray);
		
	}
	
	@DataProvider(name="YamlParameters")
    public Object[][] getYamlData(){
    	this.createYamlObjects();
    	HashMap<String, String> outliers = new HashMap<String, String>();
    	outliers.put("assigned_user_id", assignedUserID);
    	outliers.put("account_id", clientBeanID);
    	outliers.put("contact_id_c", contactID);
    	outliers.put("prev_assigned_user_id", "");
    	outliers.put("assigned_user_name", assigned_user_name);
    	outliers.put("last_updating_system_date_c", "");
//    	loadYamlFile=MetaUtilities.loadMetaFile("test_config/extensions/api/opportunity/metadata.json", "Opportunities", 
//				outliers, baseURL, token);
		loadYamlFile=YamlUtilities.loadYamlFile("test_config/extensions/api/opportunity/opportunities.yaml", "OpportunityTBDMutableObject", 
				outliers, baseURL, token, user);
		System.out.println(loadYamlFile);
		return loadYamlFile;
    }
	
	
	@Test(dataProvider = "YamlParameters")
    public void Test_YAML(HashMap<String,Object> parameterValues)
    {
		log.info("Start test");
//        Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
//        Map<String, Object> request = new HashMap();
//        while (it.hasNext()) 
//        {
//        Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
//        request.put(pairs.getKey(), pairs.getValue());
//        }
		String jsonString;
        JSONObject json = new JSONObject(parameterValues);
        
        
        
		log.info("Sending POST request");
		OpportunityRestAPI opptyrestAPI = new OpportunityRestAPI();
		String token = getOAuthToken(user);
		try{
		jsonString = opptyrestAPI.postOpportunity(getRequestUrl("", null), token, json, "200");
		}
		catch (Exception e) {
			jsonString = opptyrestAPI.postOpportunity(getRequestUrl("", null), getOAuthToken(user), json, "200");
		}
		System.out.println(jsonString);
		
		log.info("End test");	
    }
}