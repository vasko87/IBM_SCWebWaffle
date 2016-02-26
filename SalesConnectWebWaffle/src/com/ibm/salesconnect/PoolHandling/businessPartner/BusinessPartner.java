/**
 * 
 */
package com.ibm.salesconnect.PoolHandling.businessPartner;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

/**
 * @author timlehane
 * @date Jun 27, 2013
 */
public class BusinessPartner {
	private static final Logger log = LoggerFactory.getLogger(BusinessPartner.class);
	private List<String[]> myList;


	public BusinessPartner(String filePath){
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(filePath));
			myList = reader.readAll();
			          
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public String getBusinessPartnerCeid(String index){
		for(int i=0; i < myList.size(); i++)
		{
			String [] newline = (String[]) myList.get(i);
			if(newline[0].equals(index)){
				return newline[1]; 
			}
		}
		log.info("No business partner found");
		return null;
	}
	
}
