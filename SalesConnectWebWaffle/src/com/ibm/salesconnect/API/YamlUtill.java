package com.ibm.salesconnect.API;

/**
 * @author AaronFortune
 * @date jan 28th, 2015
 */

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.ibm.salesconnect.test.api.leads.GETleads;

public class YamlUtill 
{

	private static final Logger log = LoggerFactory.getLogger(GETleads.class);

	public static Object[][] loadYamlFile()
	{
		Yaml yaml = new Yaml();
		Map<String, Object> map = null;

		Object[][] testcaseList = null;
		try {map = (Map<String, Object>) yaml.load(new String(Files.readAllBytes(Paths.get("test_config/extensions/api/lead/leads.yml")),Charset.defaultCharset()));
		} 
		catch (Exception e) 
		{
			log.info("failed to read yml file");
			log.info(e.getLocalizedMessage());
		}

		Map<String, Object> map4 = null;

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getKey().equalsIgnoreCase("definitions")) {
				Map<String, Object> map2 = (Map<String, Object>) entry
						.getValue();

				for (Map.Entry<String, Object> entry2 : map2.entrySet()) {
					if (entry2.getKey().equalsIgnoreCase("LeadObject")) {
						Map<String, Object> map3 = (Map<String, Object>) entry2
								.getValue();

						for (Map.Entry<String, Object> entry3 : map3.entrySet()) {
							if (entry3.getKey().equalsIgnoreCase("properties")) {
								map4 = (Map<String, Object>) entry3.getValue();

							}
						}
						int longistEnumpram = getLongestEnum(map4);
						testcaseList = new Object[longistEnumpram][1];
						int testcasecounter = 0;
						for (int i = 0; i < longistEnumpram; i++) {
							testcaseList[testcasecounter][0] = new YamlUtill()
									.getTestCaseMap(map4, longistEnumpram,
											testcasecounter, longistEnumpram);
							testcasecounter++;
						}

					}
				}
			}
		}
		return testcaseList;
	}

	/**
	 * 
	 * this method take in a map and runs thrown the whole map looking for the
	 * enum values once it has found those values it will scan all the value
	 * contain in that enum and set it to the highest, and it will keep doing
	 * that until its finds the once that all done the highest value will be
	 * returned
	 * 
	 * @param map
	 * @return int
	 * @throws IOException
	 * @throws Throwable
	 */
	public static int getLongestEnum(Map<String, Object> map) {

		int highest = 0;
		ArrayList arraylistvaules = null;
		HashMap<String, Object> testcase = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {

			Map<String, Object> map1 = (Map<String, Object>) entry.getValue();

			for (Map.Entry<String, Object> entry1 : map1.entrySet()) {

				if (entry1.getKey().equalsIgnoreCase("enum")) {
					arraylistvaules = (ArrayList) entry1.getValue();
					if (highest <= arraylistvaules.size()) {
						highest = arraylistvaules.size();
					}
				}
			}
		}

		return highest;
	}

	/**
	 * 
	 * @param map
	 * @param highestpara
	 * @param testcaseNumber
	 * @param totalTestNumber
	 * @return hashmap contain all the values to be used in the post leads
	 *         request
	 * @throws IOException
	 * @throws Throwable
	 *             goes though all the map looking key words once the key word
	 *             as been found it pull the key and its value in to a other
	 *             hashmap to be used when making a lead
	 */
	public HashMap<String, Object> getTestCaseMap(Map<String, Object> map,
			int highestpara, int testcaseNumber, int totalTestNumber) {

		int highest = highestpara;
		int rand = new Random().nextInt(100000);

		String StateValueFromMethod = "";
		Object value = "";
		
		HashMap<String, Object> testcase = new HashMap<String, Object>();
		List<String> vaulesList = new ArrayList<String>();

		String primaryAddressCountry = getPrimaryAddressCountry(map,
				testcaseNumber);
		String status = getStatus(map, testcaseNumber);

		for (Map.Entry<String, Object> entry : map.entrySet()) 
		{
			Map<String, Object> map1 = (Map<String, Object>) entry.getValue();

			if (entry.getKey().equalsIgnoreCase("date_entered")
					|| entry.getKey().equalsIgnoreCase("trial_start_date_c")
					|| entry.getKey().equalsIgnoreCase("date_modified")
					|| entry.getKey().equalsIgnoreCase("trial_start_date_c")
					|| entry.getKey().equalsIgnoreCase("date_entered")
					|| entry.getKey().equalsIgnoreCase("trial_end_date_c")) {
				testcase.put(entry.getKey(), "2015-12-16T12:00:00-05:00");
			}
			else if (entry.getKey().equalsIgnoreCase("birthdate")){
				testcase.put(entry.getKey(), "2015-12-16");
			}
			else if ((entry.getKey().equalsIgnoreCase("full_name"))
					|| (entry.getKey().equalsIgnoreCase("report_to_name"))){
				testcase.put(entry.getKey(), "fullName" + rand);
			}
			else if (entry.getKey().equalsIgnoreCase("id")) {
				value = "";
			}
			else if (entry.getKey().equalsIgnoreCase("deleted")) {
				value = "false";
			}
			else if (entry.getKey().equalsIgnoreCase("assigned_user_id")){
				testcase.put(entry.getKey(), "2593ac68-80f7-ee38-faac-546b5edc6748");
			}
			else if (entry.getKey().equalsIgnoreCase("reports_to_id")
					){
				testcase.put(entry.getKey(), "2593ac68-80f7-ee38-faac-546b5edc6748");
			}
			else if (entry.getKey().equalsIgnoreCase("report_to_name")){
				testcase.put(entry.getKey(), "ContactFirst ContactLast");
			}
			else if(entry.getKey().equalsIgnoreCase("Assigned User")){
				testcase.put(entry.getKey(), "ie01 bie01");
			}
			else if(entry.getKey().equalsIgnoreCase("first_name"))
			{
				testcase.put(entry.getKey(), "ie01");
			}
			else if(entry.getKey().equalsIgnoreCase("last_name"))
			{
				testcase.put(entry.getKey(), "bie01");
			}
			else if(entry.getKey().equalsIgnoreCase("email"))
			{
				JSONArray jsonArray = new JSONArray();
				JSONObject jsonObject = new JSONObject();
				try{
				jsonObject.put("email_address", "*email*");
				jsonObject.put("invalid_email", false);
				jsonObject.put("opt_out", false);
				jsonObject.put("primary_address", false);
				jsonObject.put("reply_to_address", false);
				}
				catch (Exception e) {
					// TODO: handle exception
				}
				
				jsonArray.add(jsonObject);
				testcase.put(entry.getKey(),jsonArray);
			}
			else if ((entry.getKey().equalsIgnoreCase("primary_address_country"))
					|| (entry.getKey().equalsIgnoreCase("alt_address_country"))) {
				testcase.put(entry.getKey(), primaryAddressCountry);
			} 
			else if ((entry.getKey().equalsIgnoreCase("primary_address_state"))
			|| (entry.getKey().equalsIgnoreCase("alt_address_state"))) {
				value = new CountriesStatesUtil().getState(primaryAddressCountry);
				testcase.put(entry.getKey(), value);
			}
			else if ((entry.getKey().equalsIgnoreCase("c_accept_status_fields"))
					//|| entry.getKey().equalsIgnoreCase("reports_to_id")
					|| entry.getKey().equalsIgnoreCase("employee_cnum")
					|| entry.getKey().equalsIgnoreCase("campaign_id")
					|| entry.getKey().equalsIgnoreCase("score_c")
					|| entry.getKey().equalsIgnoreCase("picture")
					|| entry.getKey().equalsIgnoreCase("doc_owner")){
				//Do nothing
			}
			else if (entry.getKey().equalsIgnoreCase("status")) {
				testcase.put(entry.getKey(), status);
			} 
			else if (entry.getKey().equalsIgnoreCase("status_detail_c")){

				testcase.put(entry.getKey(), getStatusDetailCFromStatus(status));
			}
			else if (entry.getKey().contains("phone")){ 
				testcase.put(entry.getKey(), "123456781");
			}
			else if (entry.getKey().equalsIgnoreCase("trial_applications_c")){ 
				ArrayList<String> values = new ArrayList<String>(Arrays.asList("APPBIG", "APPINT", "APPIOT","APPMOB", "APPWAT", "APPWEB"));
				testcase.put(entry.getKey(), getTrailApplication(values, highest, testcaseNumber));
			}
			else{
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
					else if (entry1.getKey().equals("enum")) {
						vaulesList = (ArrayList<String>) entry1.getValue();
						if (vaulesList.size() != highest){
							rand = new Random().nextInt(vaulesList.size());
						}	
						else {
							rand = testcaseNumber;
						}
						
						String SplitValue = vaulesList.get(new Random().nextInt(vaulesList.size()-1));
						if (SplitValue.contains("(")) {
							int temp = SplitValue.indexOf(' ');
							value = SplitValue.substring(0, temp);
						}
						else {
							value = vaulesList.get(rand);
						}
						
					}//end enum	
					else if (entry1.getValue().equals("x-sc-fieldmap")){
						value = entry.getKey().toString() + " " + rand;
					}
				}//end parameter for loop
				testcase.put(entry.getKey(), value);
			}//end parameter name check
		}//end main list		
		return testcase;
}
	
	public String getStatusDetailCFromStatus(String status) {

		String[] cold = {"COLDBADD", "COLDIBM1", "COLDIBM2", "COLDNOFO",
				"COLDNORE", "COLDNOFT", "COLDOOUT", "COLDOTHR", "COLDPRTN",
				"COLDIBM3", "COLDSTDT" };
		//	String[] In_progress = { "PROGMEET", "PROGDEMO", "PROGINIT",
		//			"PROGOFFC", "PROGCALL", "PROGWEBN" };
			String[] In_progress = { "PROGNURT", "PROGINIT",
					 "PROGQUAL" };
		String Newstatu = "NEWTBA";
		String contact = "CONTACT";
		String returedValue = "";
		int rand;
		
		if (status.equals("LEADCOLD")) 
		{
			rand = new Random().nextInt(cold.length);
			returedValue = cold[rand].toString();
			return returedValue;
		} 
		else if (status.equals("LEADCONV")) 
		{
			returedValue = contact;
			return returedValue;
		} 
		else if (status.equals("LEADPROG")) 
		{
			rand = new Random().nextInt(In_progress.length);
			returedValue = In_progress[rand].toString();
			return returedValue;
		} 
		else if (status.equals("LEADNEW")) 
		{
			returedValue = Newstatu;
			return returedValue;
		}

		return null;

	}
	
	public JSONArray getTrailApplication(ArrayList<String> vaulesList, int highest, int testcaseNumber){
		int rand;
		if (vaulesList.size() != highest){
			rand = new Random().nextInt(vaulesList.size());
		}	
		else {
			rand = testcaseNumber;
		}
		
		String SplitValue = vaulesList.get(new Random().nextInt(vaulesList.size()-1));
		if (SplitValue.contains("(")) {
			int temp = SplitValue.indexOf(' ');
			JSONArray json = new JSONArray();
			json.add(SplitValue.substring(0, temp));
			return json;
		}
		else {
			JSONArray json = new JSONArray();
			json.add(vaulesList.get(rand));
			return json;
		}
	}

	public String getStatus(Map<String, Object> map, int testcaseNumber) 
	{
		int randNumberPicker;

		for (Map.Entry<String, Object> entry : map.entrySet()) 
		{
			Map<String, Object> map2 = (Map<String, Object>) entry.getValue();
			if (entry.getKey().equalsIgnoreCase("status"))
			{
				for (Map.Entry<String, Object> entry1 : map2.entrySet()) 
				{
					if (entry1.getKey().equals("enum")) {
						randNumberPicker = new Random().nextInt(4);
						List<String> StautList = (ArrayList<String>) entry1.getValue();
						String status = StautList.get(randNumberPicker);
						String statutSplit = status.toString();

						int temp = statutSplit.indexOf(' ');
						String p = statutSplit.substring(0, temp);
						
						return p;
					}
				}
			}

		}
		return null;

	}

	public String getPrimaryAddressCountry(Map<String, Object> map,int testcaseNumber) 
	{

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			Map<String, Object> map1 = (Map<String, Object>) entry.getValue();
			if (entry.getKey().equalsIgnoreCase("primary_address_country"))
			{
				for (Map.Entry<String, Object> entry1 : map1.entrySet()) 
				{
					if (entry1.getKey().equals("enum")) 
					{
						List<String> countryList = (ArrayList<String>) entry1.getValue();
						String primaryAdddressCountryUnParsed = countryList.get(testcaseNumber);
						return primaryAdddressCountryUnParsed.toString().substring(0,primaryAdddressCountryUnParsed.indexOf(' '));
					}
				}
			}
		}
		return null;
	}
}
