package com.ibm.salesconnect.API;

/**
 * @author AaronFortune
 * @date jan 5th, 2015
 */
import java.io.FileReader;

import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author AaronFortune
 * @date feb 9th, 2015
 */

public class CountriesStatesUtil {
	private static final Logger log = LoggerFactory.getLogger(CountriesStatesUtil.class);

	
	public String getState(String country){
		JSONParser parser = new JSONParser();
		try{
			Object obj = parser.parse(new FileReader("test_config/extensions/api/lead/CountryState.json"));
			org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
			org.json.simple.JSONObject stateList = (org.json.simple.JSONObject) jsonObject.get(country);

			return (String) stateList.get("0");
		}
		catch (Exception e) {
			log.info("Returning empty string as no states could be found");
		}
		return "";
		
        
	}
}
