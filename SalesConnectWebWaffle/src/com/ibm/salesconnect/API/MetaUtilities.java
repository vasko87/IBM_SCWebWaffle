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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.ibm.salesconnect.common.HttpUtils;

public class MetaUtilities {
	private static final Logger log = LoggerFactory.getLogger(MetaUtilities.class);
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
	public static Object[][] loadMetaFile(String filepath, String sectionName, HashMap<String, String> outliers, String baseURL, String token)
	{

		JSONObject module = null;
		Object[][] testcaseList = null;
		String headers[] = {"OAuth-Token", token};
		
		HttpUtils httpUtils = new HttpUtils();
		try {
			module = (JSONObject)new JSONParser().parse(httpUtils.getRequest(baseURL + "rest/v10/metadata?type_filter=modules&module_filter="+sectionName+"&platform=mobile", headers));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

//		try {//map = (Map<String, Object>) yaml.load(new String(Files.readAllBytes(Paths.get(filepath)),Charset.defaultCharset()));
//			module = (JSONObject)new JSONParser().parse(new String(Files.readAllBytes(Paths.get(filepath)),Charset.defaultCharset()));
//		} 
//		catch (Exception e) 
//		{
//			log.info("failed to read meta file");
//			log.info(e.getLocalizedMessage());
//		}
		
		JSONObject moduleMeta = (JSONObject) module.get("modules");
		JSONObject sectionNameMeta = (JSONObject) moduleMeta.get(sectionName);
		JSONObject fields = (JSONObject) sectionNameMeta.get("fields");
		
		Map<String, JSONObject> parameters = new HashMap<String, JSONObject>();
		for(Iterator iterator = fields.keySet().iterator(); iterator.hasNext();) {
		    String key = (String) iterator.next();
		    System.out.println(key);
		    System.out.println(fields.get(key));
		    if (key.equals("created_by_link")) {
				log.info("breakpoint stop");
			}
		    if (!(key.equals("_hash")||key.equals(null))) {
		        parameters.put(key, (JSONObject) fields.get(key));
			}
		
		}
		
		
//
//		Map<String, Object> parameters = null;
//
//		//Get properties section of yaml file
//		for (Map.Entry<String, Object> entry : map.entrySet()) {
//			if (entry.getKey().equalsIgnoreCase("definitions")) {
//				Map<String, Object> definitionsSection = (Map<String, Object>) entry
//						.getValue();
//
//				for (Map.Entry<String, Object> entry2 : definitionsSection.entrySet()) {
//					if (entry2.getKey().equalsIgnoreCase(sectionName)) {
//						Map<String, Object> sectionNameSection = (Map<String, Object>) entry2
//								.getValue();
//
//						for (Map.Entry<String, Object> entry3 : sectionNameSection.entrySet()) {
//							if (entry3.getKey().equalsIgnoreCase("allOf")) {
//								ArrayList<Object> allOf = (ArrayList<Object>) entry3
//										.getValue();
//
//								Map<String, Object> allOfSection = (Map<String, Object>) allOf.get(1);
//
//								for (Map.Entry<String, Object> entry4 : allOfSection.entrySet()) {
//									if (entry4.getKey().equalsIgnoreCase("properties")) {
//										parameters = (Map<String, Object>) entry4.getValue();
//									}
//								}
//							}
//						}
//						//create return data, 10 tests, to be changed
						testcaseList = new Object[10][1];
					int testcasecounter = 0;
					MetaUtilities metaUtil = new MetaUtilities();

						for (int i = 0; i < 10; i++) {
							testcaseList[testcasecounter][0] = metaUtil.getTestCaseMap(parameters, testcasecounter, outliers, baseURL, token, sectionName);
							testcasecounter++;
						}
//
//					}
//				}
//			}
//		}
		return testcaseList;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getTestCaseMap(Map<String, JSONObject> map, int testcaseNumber, HashMap<String, String> outliers, 
			String baseURL, String token, String moduleName) {

		int rand = new Random().nextInt(100000);

		Object value = "";
		Boolean includeFlag=true;
		HashMap<String, Object> testcase = new HashMap<String, Object>();

		//iterate through the properties
		for (Map.Entry<String, JSONObject> entry : map.entrySet()) 
		{
			Map<String, Object> map1 = (Map<String, Object>) entry.getValue();
			
			for (Map.Entry<String, Object> entry1 : map1.entrySet()) 
				{
					if (entry1.getKey().equals("type")){
						if (entry1.getValue().equals("varchar")){
							value = entry.getKey().toString() + " " + rand;
						}
						else if (entry1.getValue().equals("enum")){
//							Boolean flag = true;
//							for (Map.Entry<String, ArrayList<String>> enumEntry : enums.entrySet()) {
//								if (enumEntry.getKey().equals(entry.getKey())) {
//									flag = false;
//								}
//							}
//							
//							if (flag) {
//								//add new entry to enums
//								String headers[] = {"OAuth-Token", token};
//								String response = new HttpUtils().getRequest(baseURL + "rest/v10/" + moduleName + "/enum/" + entry.getKey(), headers);
//								JSONObject jsonResponse = null;
//								try {
//									jsonResponse = (JSONObject)new JSONParser().parse(response);
//								} catch (ParseException e) {
//									e.printStackTrace();
//									log.info("Response from parameter get request failed to parse");
//									Assert.assertTrue(false);
//								}
//								ArrayList<String> values = new ArrayList<String>();
//								if (jsonResponse!=null) {	
//									Iterator<Map.Entry<String, Object>> postit = jsonResponse.entrySet().iterator();
//									while (postit.hasNext()){
//										Map.Entry<String, Object> pairs1 = postit.next(); 
//										values.add(pairs1.getKey().toString());
//									}
//								}
//								else {
//									values.add("no enum values available");
//								}
//								enums.put(entry.getKey(), values);
//							}
//							
//							//add random value from enum list
//				            int index = new Random().nextInt(enums.get(entry.getKey()).size());
//							value=enums.get(entry.getKey()).get(index);
							value="enum";
							
						}
					
						else if (entry1.getValue().equals("string")) {		
							value = entry.getKey().toString() + " " + rand;
						}
						else if (entry1.getValue().equals("integer")) {		
							value = Integer.toString(rand);
						}
						else{
							value = "not found";
						}
					}
				
					else if (entry1.getKey().equals("readonly")) {
						if (entry1.getValue().equals(true)) {
							System.out.println("READONLY: " + entry.getValue());
							includeFlag=false;
						}
					}
					else if (entry1.getKey().equals("relate")) {
						System.out.println("Relate type, excluding unless in outliers map");
						includeFlag=false;
					}

				}//end parameter for loop

				//check if param name is in the outliers list
				//if so add the parameter to the testcasemap with the value sent in 
				//may need to add another "if" in case we want to enter different values(dates etc)
				for(Map.Entry<String, String> outlierEntry : outliers.entrySet()){
					if (outlierEntry.getKey().equals(entry.getKey())) {
						includeFlag=true;
						value = outlierEntry.getValue();
					}
				}
				if (includeFlag) {
					testcase.put(entry.getKey(), value);
				}
				includeFlag=true;
				
			}//end parameter name check		
		return testcase;
}
}
