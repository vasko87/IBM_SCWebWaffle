package com.ibm.salesconnect.test.api.tasks;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * @author John Clarke
 * @date 09 Jan 2015
 */
import au.com.bytecode.opencsv.CSVReader;
public class GETclientAndsecret {
	
		public Map<String, String> getClientIDAndSecretData() {
			CSVReader csvReader = null;
			Map<String, String> clientIDSecretData = new HashMap<String, String>();
			
			try {
				csvReader = new CSVReader(new FileReader("test_config/extensions/user/clientsecret.csv"),',','\'',1);
				
				String [] nextLine;
	            //Read one line at a time
	            while ((nextLine = csvReader.readNext()) != null) {
	            	String clientSecret = "client_id="+nextLine[0] + "&client_secret=" + nextLine[1];
	            	clientIDSecretData.put(nextLine[2], clientSecret);
	            	System.out.println("**** Added client_id and secret " + nextLine[2] + ": " + clientSecret);
	            }
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (csvReader != null) {
					try {
						csvReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			/////////////////////////////////////////////////////////
			/*String createReadUpdate = clientIDSecretData.get("SC Auto create+read+update");
			if (createReadUpdate != null) {
				
			}
			
			if (clientIDSecretData.containsKey("SC Auto create+read+update")) {
				System.out.println("Data contains SC Auto create+read+update: " + clientIDSecretData.get("SC Auto create+read+update"));
			} else {
				System.out.println("Does not contain SC Auto create+read+update");
			}*/
			
			return clientIDSecretData;
		}
		public Map<String, String> getClientID() {
			CSVReader csvReader = null;
			Map<String, String> clientIDData = new HashMap<String, String>();
			
			try {
				csvReader = new CSVReader(new FileReader("test_config/extensions/user/clientsecret.csv"),',','\'',1);
				
				String [] nextLine;
	            //Read one line at a time
	            while ((nextLine = csvReader.readNext()) != null) {
	            	String clientSecret = "client_id="+nextLine[0];
	            	clientIDData.put(nextLine[2], clientSecret);
	            	System.out.println("**** Added client_id " + nextLine[2] + ": " + clientSecret);
	            }
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (csvReader != null) {
					try {
						csvReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			/////////////////////////////////////////////////////////
			/*String createReadUpdate = clientIDData.get("SC Auto create+read+update");
			if (createReadUpdate != null) {
				
			}
			
			if (clientIDData.containsKey("SC Auto create+read+update")) {
				System.out.println("Data contains SC Auto create+read+update: " + clientIDData.get("SC Auto create+read+update"));
			} else {
				System.out.println("Does not contain SC Auto create+read+update");
			}*/
			
			return clientIDData;
		}
		public Map<String, String> getClientSecret() {
			CSVReader csvReader = null;
			Map<String, String> clientSecretData = new HashMap<String, String>();
			
			try {
				csvReader = new CSVReader(new FileReader("test_config/extensions/user/clientsecret.csv"),',','\'',1);
				
				String [] nextLine;
	            //Read one line at a time
	            while ((nextLine = csvReader.readNext()) != null) {
	            	String clientSecret = "&client_secret=" + nextLine[1];
	            	clientSecretData.put(nextLine[2], clientSecret);
	            	System.out.println("**** Added client_secret" + nextLine[2] + ": " + clientSecret);
	            }
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (csvReader != null) {
					try {
						csvReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			/////////////////////////////////////////////////////////
			/*String createReadUpdate = clientSecretData.get("SC Auto create+read+update");
			if (createReadUpdate != null) {
				
			}
			
			if (clientSecretData.containsKey("SC Auto create+read+update")) {
				System.out.println("Data contains SC Auto create+read+update: " + clientSecretData.get("SC Auto create+read+update"));
			} else {
				System.out.println("Does not contain SC Auto create+read+update");
			}*/
			
			return clientSecretData;
		}
		
}
		



