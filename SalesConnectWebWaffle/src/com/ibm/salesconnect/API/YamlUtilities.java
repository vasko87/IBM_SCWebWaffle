package com.ibm.salesconnect.API;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import junit.framework.Assert;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.common.HttpUtils;

public class YamlUtilities 
{

	private static final Logger log = LoggerFactory.getLogger(YamlUtilities.class);
	HashMap<String, ArrayList<String>> enums = new HashMap<String, ArrayList<String>>();

	/**
	 * Method to load yaml file and return test case list
	 * @param filepath location of yaml file
	 * @param sectionName name of section where the values are stored 
	 * @param outliers map of override values user_id, client_id etc
	 * @param baseURL env url for get enum request
	 * @param token OAuth-token for get enum request
	 * @return testcaselist
	 */
	@SuppressWarnings("unchecked")
	public static Object[][] loadYamlFile(String filepath, String sectionName, HashMap<String, String> outliers, String baseURL, String token, User user)
	{
		Yaml yaml = new Yaml();
		Map<String, Object> map = null;

		Object[][] testcaseList = null;
		try {map = (Map<String, Object>) yaml.load(new String(Files.readAllBytes(Paths.get(filepath)),Charset.defaultCharset()));
		} 
		catch (Exception e) 
		{
			log.info("failed to read yml file");
			log.info(e.getLocalizedMessage());
		}

		Map<String, Object> parameters = null;

		//Get properties section of yaml file
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getKey().equalsIgnoreCase("definitions")) {
				Map<String, Object> definitionsSection = (Map<String, Object>) entry
						.getValue();

				for (Map.Entry<String, Object> entry2 : definitionsSection.entrySet()) {
					if (entry2.getKey().equalsIgnoreCase(sectionName)) {
						Map<String, Object> sectionNameSection = (Map<String, Object>) entry2
								.getValue();

						for (Map.Entry<String, Object> entry3 : sectionNameSection.entrySet()) {
							if (entry3.getKey().equalsIgnoreCase("allOf")) {
								ArrayList<Object> allOf = (ArrayList<Object>) entry3
										.getValue();

								Map<String, Object> allOfSection = (Map<String, Object>) allOf.get(1);

								for (Map.Entry<String, Object> entry4 : allOfSection.entrySet()) {
									if (entry4.getKey().equalsIgnoreCase("properties")) {
										parameters = (Map<String, Object>) entry4.getValue();
									}
								}
							}
						}
						//create return data, 10 tests, to be changed
						testcaseList = new Object[10][1];
						int testcasecounter = 0;
						YamlUtilities yamlUtil = new YamlUtilities();
						for (int i = 0; i < 10; i++) {
							testcaseList[testcasecounter][0] = yamlUtil.getTestCaseMap(parameters, testcasecounter, outliers, baseURL, token, user);
							testcasecounter++;
						}

					}
				}
			}
		}
		return testcaseList;
	}
	
	/**
	 * Method to get the values for one test
	 * @param map properties seciton contents
	 * @param testcaseNumber
	 * @param outliers list of override values user_id, client_id etc
	 * @param baseURL env url for making enum get requests
	 * @param token OAuth-token for making enum get requests
	 * @return values for one test
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getTestCaseMap(Map<String, Object> map, int testcaseNumber, HashMap<String, String> outliers, 
			String baseURL, String token, User user) {

		int rand = new Random().nextInt(100000);
		String userID = "";
		String userName = "";

		Object value = "";
		
		HashMap<String, Object> testcase = new HashMap<String, Object>();

		//iterate through the properties
		for (Map.Entry<String, Object> entry : map.entrySet()) 
		{
			Map<String, Object> map1 = (Map<String, Object>) entry.getValue();
			
			for (Map.Entry<String, Object> entry1 : map1.entrySet()) 
				{
					if (entry1.getKey().equals("type")){
						if (entry1.getValue().equals("boolean")){
							value = false;
						}

						else if (entry1.getValue().equals("array")){
							value = entry.getKey().toString() + " " + rand;
						}
						else if (entry1.getValue().equals("string")) {		
							value = entry.getKey().toString() + " " + rand;
						}
						else if (entry1.getValue().equals("integer")) {		
							value = Integer.toString(rand);
						}
					}
					else if (entry1.getKey().equals("x-sc-enum")) {
						//check if there are values already stored for this enum in enums 
						Boolean flag = true;
						String apiString = (String) ((Map<String, Object>) entry1.getValue()).get("api");
						apiString = apiString.replace("opportunities", "Opportunities");
						for (Map.Entry<String, ArrayList<String>> enumEntry : enums.entrySet()) {
							if (enumEntry.getKey().equals(apiString)) {
								flag = false;
							}
						}
						
						if (!entry.getKey().equals("currency_id")) {	
						if (flag) {

							//add new entry to enums
							String headers[] = {"OAuth-Token", token};
							
							String response;
							try{
								response = new HttpUtils().getRequest(baseURL + "rest/v10" + apiString, headers);
								}
								catch (Exception e) {
									headers[1] = new LoginRestAPI().getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
									response = new HttpUtils().getRequest(baseURL + "rest/v10" + apiString, headers);
								}
							
							JSONObject jsonResponse = null;
							try {
								jsonResponse = (JSONObject)new JSONParser().parse(response);
							} catch (ParseException e) {
								e.printStackTrace();
								log.info("Response from parameter get request failed to parse");
								Assert.assertTrue(false);
							}
							ArrayList<String> values = new ArrayList<String>();
					        Iterator<Map.Entry<String, Object>> postit = jsonResponse.entrySet().iterator();
					        while (postit.hasNext()) 
					        {
					        	Map.Entry<String, Object> pairs1 = postit.next(); 
					        	values.add(pairs1.getKey().toString());
					        }		        		
							enums.put(apiString, values);
							}
						
						
						//add random value from enum list
			            int index = new Random().nextInt(enums.get(apiString).size());
						value=enums.get(apiString).get(index);
						}
					else{
						value="-99";
					}
					}//end enum
					//hard coding
					else if (entry1.getKey().equals("format")) {
						if (entry1.getValue().equals("date")) {
							value = "2014-07-05";
						}
					}

				}//end parameter for loop

				//check if param name is in the outliers list
				//if so add the parameter to the testcasemap with the value sent in 
				//may need to add another "if" in case we want to enter different values(dates etc)
				for(Map.Entry<String, String> outlierEntry : outliers.entrySet()){
					if (outlierEntry.getKey().equals(entry.getKey())) {
						value = outlierEntry.getValue();
					}
					
					if (outlierEntry.getKey().equals("assigned_user_id")) {
						userID=outlierEntry.getValue();
					}
					
					if (outlierEntry.getKey().equals("assigned_user_name")) {
						userName=outlierEntry.getValue();
					}
				}
				
				//hardcoding for business rules
				testcase.put(entry.getKey(), value);
			}//end parameter name check
		
		return addRevItem(testcase, userID, userName);
}
	HashMap<String, Object> addRevItem(HashMap<String, Object> map1, String userID, String userName){
		int salesStage = 0;
		int reason_won = 0;
		for (Map.Entry<String, Object> entry1 : map1.entrySet()) 
		{
			if (entry1.getKey().equals("sales_stage")){
				salesStage = Integer.parseInt((String) entry1.getValue());
			}
			else if (entry1.getKey().equals("reason_won")){
				reason_won = Integer.parseInt((String) entry1.getValue());
			}
			
		}
		if ((salesStage > 3) && (salesStage < 7 )) {
			map1.put("opportun_revenuelineitems", revenuelineitem(userID
					, userName));
		}
		else if (salesStage > 6) {
			map1.put("opportun_revenuelineitems", revenuelineitem(userID
					, userName));
			map1.put("reason_won", "");
		}
		
		return map1;
	}
	
	
	JSONObject revenuelineitem(String userID, String userName){
		JSONObject parameters = new JSONObject();
		
		parameters.put("alliance_partners", "");
		parameters.put("assigned_bp_id", "");
		parameters.put("assigned_user_id", userID);
		parameters.put("assigned_user_name", userName);
		parameters.put("base_rate", 1);
		parameters.put("bookable_value", "");
		parameters.put("currency_id", "-99");
		parameters.put("deleted", false);
		parameters.put("doc_owner", "");
		parameters.put("duration", "1");
		parameters.put("fcast_date_sign", "2015-12-02");
		parameters.put("fcast_date_tran", "2015-12-01");
		parameters.put("green_blue_revenue", "Green");
		parameters.put("igf_odds", "0");
		parameters.put("igf_term", "1");
		parameters.put("level10", "B7000");
		parameters.put("level10_name", "Systems Middleware");
		parameters.put("level15", "ISM");
		parameters.put("level15_name", "IT Service Management");
		parameters.put("level17", "17MSL");
		parameters.put("level17_name", "Manage Solutions");
		parameters.put("level20", "BK800");
		parameters.put("level20_name", "Software: Tivoli Automation (Open)");
		parameters.put("level30", "B71T4");
		parameters.put("level30_name", "Tivoli Monitoring for Databases");
		parameters.put("level40", "5724B96");
		parameters.put("level40_name", "IBM Tivoli Monitoring for Databases V6.2");
		parameters.put("level_search", "Level 40: IBM Tivoli Monitoring for Databases V6.2 (5724B96)");
		parameters.put("offering_type", "SW");
		parameters.put("probability", "10");
		parameters.put("revenue_amount", "1000000");
		parameters.put("revenue_type", "");
		parameters.put("roadmap_status", "NIR");
		parameters.put("search_type", "product");
		parameters.put("srv_billing_type", "");
		parameters.put("srv_inqtr", "");
		parameters.put("srv_inqtr_month1", "");
		parameters.put("srv_inqtr_month2", "");
		parameters.put("srv_inqtr_month3", "");
		parameters.put("srv_inqtr_status", "");
		parameters.put("srv_inqtr_total", "");
		parameters.put("srv_work_type", "");
		parameters.put("stg_fulfill_type", "DIRECT");
		parameters.put("stg_signings_type", "");
		parameters.put("swg_annual_value", "");
		parameters.put("swg_book_new", "TRANS");
		parameters.put("swg_book_rnwl", "");
		parameters.put("swg_book_serv", "");
		parameters.put("swg_contract", "NEW");
		parameters.put("swg_sign_det", "");
		parameters.put("swg_tran_det", "ONE");
		parameters.put("undefined", "5724B96");
		parameters.put("user_favorites", "");
		
		JSONArray lineItemArray = new JSONArray();
		lineItemArray.add(parameters);
		
		JSONObject lineItemObject = new JSONObject();
		lineItemObject.put("create", lineItemArray);

		return lineItemObject;
	}
	
}
