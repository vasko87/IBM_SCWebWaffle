package com.ibm.salesconnect.test.svt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mpm.textgen.TextGeneratorFacade;

import org.apache.http.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.common.DB2Connection;
import com.ibm.salesconnect.common.HttpUtils;

/**
 * 
 * @author Tim Lehane
 * @date 8th Aug 2014
 *
 */
public class API_CreateTasksQuick {

	Logger log = LoggerFactory.getLogger(API_CreateTasks.class);	
	 
	public static String APIManagerURL = "https://test.api.innovate.ibm.com/salesconnect/";
	public static String APIAppSecretID = "?appId=6b18f1c8-ad63-425b-ba24-a02e807cf0f3&appSecret=38Bl8fjNWSlIYI2dBb11";
	//public static String APIManagerURL = "https://test.api.innovate.ibm.com/salesconnectperf/";
	//public static String APIAppSecretID = "?appId=762fcdaf-3202-4572-ba91-447162ab0e45&appSecret=9NL2SyUj8z5V4iUjUYn3";
	public static int limit = 50; 
	//public static String type = "Contacts";
	//public static String id = "db098775205f9a9";
	//public static String name = "SUNDAY SIANEZ";
	//public static String userName = "1bonal@my.ibm.com";
	
	@Test
	public void API_Tester() {

		//log.info("Start of test: API Tester");

		try {
//			DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//			List<Map<String, String>> userDetailsList = readCSVtoMap();
//			Map<String, String> userOppties = userDetailsList.get(0);
//			//Map of UserNames to object Names
//			Map<String, String> userContacts = userDetailsList.get(1);
//			Map<String, String> userClients = userDetailsList.get(2);
//			//Map of ObjectNames to Object IDs
//			Map<String, String> contactsNameID = new HashMap<String, String>();
//			Map<String, String> clientsNameID = new HashMap<String, String>();
			
//			//DB query setup
//			log.debug("Getting Contact IDs from DB @" +df.format(Calendar.getInstance().getTime()));
//			contactsNameID = contactsDBQuery(userContacts);
//			
//			log.debug("Getting Clients IDs from DB @" +df.format(Calendar.getInstance().getTime()));
//			clientsNameID = clientsDBQuery(userClients);
//			
//			//Loop through users, creating a task for each user object.
//			for(Map.Entry<String, String> entryUserOppty : userOppties.entrySet()){
//				String userName = entryUserOppty.getKey();
//				String[] opptyArray = userOppties.get(entryUserOppty.getKey()).split("\\|");
//				String[] contactArray = userContacts.get(userName).split("\\|");
//				String[] clientArray = userClients.get(userName).split("\\|");

			List<Thread> threads = new ArrayList<Thread>();
			
				//log.info("Logging in user: " + "ie01@tst.ibm.com");
				String[] header = apiLogin("ie01@tst.ibm.com");
				for (int j = 0; j < 1000; j++) {
				for (int i = 0; i < 100; i++) {
					System.out.println(i);
					RunnableDemo task = new RunnableDemo(header);
					Thread worker = new Thread(task);
					worker.start();
					threads.add(worker);
					}
				Thread.sleep(60000);
				}

//				int running =0;
//				do{
//					running =0;
//					for (Thread thread : threads) {
//						if (thread.isAlive()) {
//							running++;
//							
//						}
//					}
//			
//				} while (running > 0);

					
					//createTaskRelOppty(header, opptyArray);
					//createTaskRel(header, contactsNameID, contactArray, "Contacts");
					//createTaskRel(header, clientsNameID, clientArray, "Accounts");					
				
			
//	Step through individual modules
//			log.debug("Creating Contact tasks @" +df.format(Calendar.getInstance().getTime()));
//			for(Map.Entry<String, String> entryUserContact : userContacts.entrySet()){
//				contactCount++;
//				String taskId = createTaskRel(entryUserContact, contactsNameID, "Contacts");
//				//log.info("Task ID created: " + taskId);
//				if (contactCount==limit)
//					break;
//			}
//			
//			//Tasks API loading
//			log.debug("Creating client tasks @" +df.format(Calendar.getInstance().getTime()));
//			for(Map.Entry<String, String> entryUserClient : userClients.entrySet()){
//				clientCount++;
//				String taskId = createTaskRel(entryUserClient, clientsNameID, "Accounts");
//				//log.info("Task ID created: " + taskId);
//				if (clientCount==limit)
//					break;
//			}
//			log.debug("Creating Oppty tasks @" +df.format(Calendar.getInstance().getTime()));
//			for(Map.Entry<String, String> entryUserOppty : userOppties.entrySet()){
//				opptyCount++;
//				String taskId = createTaskRelOppty(entryUserOppty);
//				//log.info("Task ID created: " + taskId);
//				if (opptyCount==limit)
//					break;
//			}

		} catch (Exception e) {
		//	log.error("\n*** Exception in Main: Exiting ***\n");
			e.printStackTrace();
			//System.exit(1);
		}
		
	}
	
	private Map<String, String> clientsDBQuery(Map<String, String> userClients) {			
		ResultSet rsClients =null; 
		Map<String, String> clientsNameID = new HashMap<String, String>();
		Statement statement = null;
		Connection conn = connectToDB();
		int count =0;
		//Query DB for contact/client name and IDs
		//log.debug("Getting Clients IDs from DB");
		try{
			for(Map.Entry<String, String> entryUserClients : userClients.entrySet()){
				count++;
				//Query the DB for the Client ID
				String clientQueryString="";
				String[] clients = entryUserClients.getValue().split("\\|");
				for (int i=0; i< clients.length; i++){
					clientQueryString=clientQueryString+ "'"+clients[i] +"',";
				}
				clientQueryString = clientQueryString.substring(0, clientQueryString.length()-1);
				String query = "SELECT ACCOUNTS.ID, ACCOUNTS.NAME FROM SCTID.ACCOUNTS WHERE ACCOUNTS.DELETED=0 AND ACCOUNTS.NAME IN ("+clientQueryString+")";
				//log.info(query);
				statement = conn.createStatement();
				rsClients = statement.executeQuery(query);
				
				for (int i=0; rsClients.next(); i++){
					clientsNameID.put(rsClients.getString("NAME"),rsClients.getString("ID"));
				}
			
				if (count==limit)
					break;
			}
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return clientsNameID;
	}

//	private ResultSet contactsDBQuery(Connection conn, Map.Entry<String, String> entryUserContacts) {
//		String[] contacts = entryUserContacts.getValue().split("\\|");
//		Statement statement = null;
//		String contactQueryString="";
//		ResultSet rs = null;
//		try {
//			for (int i=0; i< contacts.length; i++){
//				contactQueryString=contactQueryString+ "'"+contacts[i] +"',";
//			}
//			contactQueryString = contactQueryString.substring(0, contactQueryString.length()-1);
//			//String query = "SELECT CONTACT.ID, USER_NAME FROM SCTID.CONTACTS_USERS, SCTID.USERS WHERE USER.ID=CONTACTS_USERS.USER_ID AND USER_NAME='"
//			//	+ user_name + "'";
//			String query = "SELECT CONTACTS.ID, NAME_FORWARD FROM SCTID.CONTACTS WHERE CONTACTS.NAME_FORWARD IN ("+contactQueryString+")";
//			
//			//log.debug("Contact DB Query: " + query);
//		
//			statement = conn.createStatement();
//			rs = statement.executeQuery(query);	
//			conn.close();
//
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return rs;
//	}

	
	private Map<String, String> contactsDBQuery(Map<String, String> userContacts) {
		
		ResultSet rsContacts =null; 
		Map<String, String> contactsNameID = new HashMap<String, String>();
//		Statement statement = null;
//		Connection conn = connectToDB();
		int count =0;
		//Query DB for contact/client name and IDs
		//log.debug("Getting Contact IDs from DB");
		try{
			for(Map.Entry<String, String> entryUserContacts : userContacts.entrySet()){
				Statement statement = null;
				Connection conn = connectToDB();
				count++;
				//Query the DB for the contact ID
				String contactQueryString="";
				String[] contacts = entryUserContacts.getValue().split("\\|");
				for (int i=0; i< contacts.length; i++){
					contactQueryString=contactQueryString+ "'"+contacts[i] +"',";
				}
				contactQueryString = contactQueryString.substring(0, contactQueryString.length()-1);
				String query = "SELECT CONTACTS.ID, NAME_FORWARD FROM SCTID.CONTACTS WHERE CONTACTS.DELETED=0 AND CONTACTS.NAME_FORWARD IN ("+contactQueryString+")";
				//log.info(query);
				statement = conn.createStatement();
				rsContacts = statement.executeQuery(query);	
				//rsContacts = contactsDBQuery(conn, entryUserContacts);
				//String[] userContactIDs = {};
				
				for (int i=0; rsContacts.next(); i++){
					//userContactIDs[i] = rsContacts.getString("ID");
					contactsNameID.put(rsContacts.getString("NAME_FORWARD"),rsContacts.getString("ID"));
				}
				//Store the Contact ID into a map (Contact Name | Contact ID)
				//contactsNameID.put(rsContacts.getString("NAME_FORWARD"),userContactIDs);
				//log.info("Task ID created: " + taskId);
				if (count==limit)
					break;
				statement.close();
				conn.close();
			}
//			statement.close();
//			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactsNameID;
	}
	
	private Connection connectToDB(){
		Connection conn =null;
		try {
			DB2Connection db2 = new DB2Connection("jdbc:db2://svt1oltp01.rtp.raleigh.ibm.com:50000/saleconn","sctid","jun13pwd");
			conn = db2.connectToDb2();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
//	private String createTaskRelOppty(String[] header ,String[] opptyArray) {
//
//		String id = "";
//		String name = "";
//		String module = "Opportunities";
//		String result = "";
//		API_Threading[] API_Thread = new API_Threading[opptyArray.length]; 
//		
//		try {
//			//Loop through each oppty
//			
//			for(int i=0; i<opptyArray.length;i++){ 
//				id = opptyArray[i];
//				name = id;
//				
//				//log.info("Creating a Task");
//				API_Thread[i] = new API_Threading(header, id, name, module, i);
//				API_Thread[i].start();
//				//result = api_createTask(header, id, name, module);
//			}
//			for(int i=0; i<opptyArray.length;i++){ 
//				while(API_Thread[i].isAlive()){
//					//Do nothing
//				}
//			}
//		}
//			catch (Exception e) {
//				e.printStackTrace();
//			}
//		return result;
//	}
	
//	private String createTaskRel(Entry<String, String> entryUserObject,
//			Map<String, String> objectNameID, String module) {
//		String userName = entryUserObject.getKey();
//		String[] object = entryUserObject.getValue().split("\\|");
//		
//		
//		
//		String id = "";
//		String name = "";
//		//String module = "Contacts";
//		String result = "";
//		API_Threading[] API_Thread = new API_Threading[object.length]; 
//		
//		try {
//			//Loop through each object
//			log.info("Logging in user: " + userName);
//			String[] header = apiLogin(userName);
//			if (!header[1].contains("need_login")){
//				for(int i=0; i<object.length;i++){ 
//					name = object[i];
//					id = objectNameID.get(name);
//					//log.info("Contact " +name + " - ID:" +id);
//					if(!id.isEmpty()){
//						//log.info("Creating a Task");
//						API_Thread[i] = new API_Threading(header, id, name, module, i);
//						API_Thread[i].start();
//						//result = api_createTask(header, id, name, module);
//					}
//					else{
//						log.error("Contact " +name + " has no ID:" +id);
//					}
//				}
//				for(int i=0; i<object.length;i++){ 
//					while(API_Thread[i].isAlive()){
//						//Do nothing
//					}
//				}
//			}
//		}
//			catch (Exception e) {
//				e.printStackTrace();
//			}
//		return result;
//	}

//	private String createTaskRel(String[] header, Map<String, String> objectID, String[] objectArray, String module) {
//				
//		String id = "";
//		String name = "";
//		//String module = "Contacts";
//		String result = "";
//		API_Threading[] API_Thread = new API_Threading[objectArray.length]; 
//		
//		try {
//			//Loop through each object
//			
//				for(int i=0; i<objectArray.length;i++){ 
//					name = objectArray[i];
//					id = objectID.get(name);
//					//log.info("Contact " +name + " - ID:" +id);
//					if(!id.isEmpty()){
//						//log.info("Creating a Task");
//						API_Thread[i] = new API_Threading(header, id, name, module, i);
//						API_Thread[i].start();
//						//result = api_createTask(header, id, name, module);
//					}
//					else{
//						log.error("Contact " +name + " has no ID:" +id);
//					}
//				}
//				for(int i=0; i<objectArray.length;i++){ 
//					while(API_Thread[i].isAlive()){
//						//Do nothing
//					}
//				}
//			
//		}
//			catch (Exception e) {
//				e.printStackTrace();
//			}
//		return result;
//	}
	
//	private String createTask(String[] header, String[] opptyArray, String[] contactArray, String[] clientArray, 
//			Map<String, String> contactsNameID, Map<String, String> clientsNameID) {
//		String id = "";
//		String name = "";
//		//String module = "Contacts";
//		String result = "";
//		int threadLength = opptyArray.length + contactArray.length + clientArray.length;
//		API_Threading[] API_Thread = new API_Threading[threadLength]; 
//		
//		try {
//			//Loop through each object
//			//for (int i=0; i<threadLength; i++){
//			int i=0;
//				//Loop oppties
//				for(int j=0; j<opptyArray.length;j++){ 
//					name = opptyArray[j];
//					id = name;
//					//log.info("Contact " +name + " - ID:" +id);
//					if(!id.isEmpty()){
//						//log.info("Creating a Task");
//						API_Thread[i++] = new API_Threading(header, id, name, "Opportunities", i);
//						//API_Thread[i].start();
//						//result = api_createTask(header, id, name, module);
//					}
//					else{
//						log.error("Oppty " +name + " has no ID:" +id);
//					}
//				}
//				//Loop Contacts
//				for(int j=0; j<contactArray.length;j++){ 
//					name = contactArray[j];
//					id = contactsNameID.get(name);
//					//log.info("Contact " +name + " - ID:" +id);
//					if(!id.isEmpty()){
//						//log.info("Creating a Task");
//						API_Thread[i++] = new API_Threading(header, id, name, "Contacts", i);
//						//API_Thread[i].start();
//						//result = api_createTask(header, id, name, module);
//					}
//					else{
//						log.error("Contact " +name + " has no ID:" +id);
//					}
//				}
//				//Loop clients
//				for(int j=0; j<clientArray.length;j++){ 
//					name = clientArray[j];
//					id = clientsNameID.get(name);
//					//log.info("Contact " +name + " - ID:" +id);
//					if(!id.isEmpty()){
//						//log.info("Creating a Task");
//						API_Thread[i++] = new API_Threading(header, id, name, "Accounts", i);
//						//API_Thread[i].start();
//						//result = api_createTask(header, id, name, module);
//					}
//					else{
//						log.error("Client " +name + " has no ID:" +id);
//					}
//				}
//				for(i=0; i<threadLength;i++)
//					API_Thread[i].start();
//				
//				for(i=0; i<threadLength;i++){ 
//					while(API_Thread[i].isAlive()){
//						//Do nothing
//					}
//				}
//			//}
//		}
//			catch (Exception e) {
//				e.printStackTrace();
//			}
//		return result;
//	}
	
	private String createTask(String[] header) {
		String id = "";
		String name = "test";
		//String module = "Contacts";
		String result = "";
		//int threadLength = 10;//opptyArray.length + contactArray.length + clientArray.length;
		
		try {
			result = api_createTask(header, name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			//Loop through each object
//			for (int i=0; i<threadLength; i++){
//			int i=0;
//				//Loop oppties
//				for(int j=0; j<opptyArray.length;j++){ 
//					name = opptyArray[j];
//					id = name;
//					//log.info("Contact " +name + " - ID:" +id);
//					if(!id.isEmpty()){
//						//log.info("Creating a Task");
//						API_Thread[i++] = new API_Threading(header, id, name, "Opportunities", i);
//						//API_Thread[i].start();
//						//result = api_createTask(header, id, name, module);
//					}
//					else{
//						log.error("Oppty " +name + " has no ID:" +id);
//					}
//				}
//				//Loop Contacts
//				for(int j=0; j<contactArray.length;j++){ 
//					name = contactArray[j];
//					id = contactsNameID.get(name);
//					//log.info("Contact " +name + " - ID:" +id);
//					if(!id.isEmpty()){
//						//log.info("Creating a Task");
//						API_Thread[i++] = new API_Threading(header, id, name, "Contacts", i);
//						//API_Thread[i].start();
//						//result = api_createTask(header, id, name, module);
//					}
//					else{
//						log.error("Contact " +name + " has no ID:" +id);
//					}
//				}
//				//Loop clients
//				for(int j=0; j<clientArray.length;j++){ 
//					name = clientArray[j];
//					id = clientsNameID.get(name);
//					//log.info("Contact " +name + " - ID:" +id);
//					if(!id.isEmpty()){
//						//log.info("Creating a Task");
//						API_Thread[i++] = new API_Threading(header, id, name, "Accounts", i);
//						//API_Thread[i].start();

//					}
//					else{
//						log.error("Client " +name + " has no ID:" +id);
//					}
//				}
//				for(i=0; i<threadLength;i++)
//					API_Thread[i].run();
//				
//				for(i=0; i<threadLength;i++){ 
//					while(API_Thread[i].isAlive()){
//						try {
//							result = api_createTask(header, name);
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//		}
//			catch (Exception e) {
//				e.printStackTrace();
//			}
		return result;
	}
	private List<Map<String, String>> readCSVtoMap() {
		//String csvFile = "/root/rpt_mattbyrn_8_5_0_2/SalesConnectSVTUtil/datapools/UserCombinedInfo.csv";
		String csvFile = "C:\\SalesConnect\\Performance\\UserCombinedInfo.csv";
		BufferedReader br = null;
		String line = "";
		String split = ",";
		Map<String, String> usersOppties = new HashMap<String, String>();
		Map<String, String> usersContacts = new HashMap<String, String>();
		Map<String, String> usersClients = new HashMap<String, String>();
		
		try{
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null){
				line = line.replace("\"", "");
				String[] userData = line.split(split);
				//String[] opptiesString = userData[5].split("\\|");
				//for(int i=0; i<opptiesString.length;i++){ 
				//	usersOppties.put(userData[0], opptiesString[i]);
				//}
				usersOppties.put(userData[0], userData[5]);
				usersContacts.put(userData[0], userData[3]);
				usersClients.put(userData[0], userData[4]);
				
			}
		}
		
		catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Map<String, String>> list = Arrays.asList(usersOppties, usersContacts,  usersClients);

		return  list;
		//return usersOppties;
	}

	public static String[] apiLogin(String userName){
		//String appSecretID ="oauth"+APIAppSecretID;
		//log.info("Starting TestTask_createTask");
		//User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		
		//log.info("Retrieving OAuth2Token.");		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String headers[]={"OAuth-Token", loginRestAPI.getOAuth2Token("https://svt4sugarlb01a.rtp.raleigh.ibm.com/sales/salesconnect/", "ie01@tst.ibm.com", "passw0rd")};
		//String headers[]={"OAuth-Token", loginRestAPI.getOAuth2TokenViaAPIManager(APIManagerURL, userName, "passw0rd", "999", appSecretID)};
		
		return headers;
	}
	//Method to create the test-output folder
//	public static String[] apiLogin(String userName){
//		//String appSecretID ="oauth"+APIAppSecretID;
//		//log.info("Starting TestTask_createTask");
//		//User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
//		
//		//log.info("Retrieving OAuth2Token.");		
//		LoginRestAPI loginRestAPI = new LoginRestAPI();
//		String headers[]={"OAuth-Token", loginRestAPI.getOAuth2TokenViaAPIManager("https://w3-dev.api.ibm.com/sales/development/salesconnect/oauth?client_id=b8dd9731-c359-409b-ba53-564e6e197a86&client_secret=V3kL7kV4wH5gE0wP7rG4sI7jX5yB8hH7wI7uB5gB8qA0oT4jR8", userName, "passw0rd", "200")};
//		//String headers[]={"OAuth-Token", loginRestAPI.getOAuth2TokenViaAPIManager(APIManagerURL, userName, "passw0rd", "999", appSecretID)};
//		for (int i = 0; i < headers.length; i++) {
//			//log.info("Header Info: " + headers[i]);
//		}
//		
//		return headers;
//	}

	public static String api_createTask(String[] headers, String id, String name, String type)throws Exception{

		//log.info("Task Parameters: " + id + "-" + name + "-" + type );
		JSONObject json = new JSONObject(getValidTaskBody(name));
		//json.put("description", "First Description: " + name);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(populateRelated(type, id, name));
		json.put("related_to_c", jsonArray);
		//String body = json.toString().replace("}", ",\"description\":\"Second Description\"}");
		String body = json.toString();
		
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(APIManagerURL + "tasks"+APIAppSecretID,
				headers, body, "application/json", "200");
		 
		//log.info("Response String "+ postResponseString);
		try {
			//check for a valid JSON response
			new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
		//	Assert.assertTrue(false, "Parse exception with post response");
		}
			
		//log.info("Ending TestTask_createTask");
		
		return postResponseString;

	}
	
	public static String api_createTask(String[] headers,String name)throws Exception{

		//log.info("Task Parameters: " + id + "-" + name + "-" + type );
		JSONObject json = new JSONObject(getValidTaskBody(name));
		//json.put("description", "First Description: " + name);
//		JSONArray jsonArray = new JSONArray();
//		jsonArray.add(populateRelated(type, id, name));
//		json.put("related_to_c", jsonArray);
//		//String body = json.toString().replace("}", ",\"description\":\"Second Description\"}");
//		String body = json.toString();
		
		HttpUtils restCalls = new HttpUtils();
		System.out.println("sending get request");
		String postResponseString = restCalls.getRequest("https://w3-dev.api.ibm.com/sales/development/salesconnect/opportunities?my_items=1&client_id=b8dd9731-c359-409b-ba53-564e6e197a86&client_secret=V3kL7kV4wH5gE0wP7rG4sI7jX5yB8hH7wI7uB5gB8qA0oT4jR8",
				headers, "200");
		 
		//log.info("Response String "+ postResponseString);
		try {
			//check for a valid JSON response
			new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
		//	Assert.assertTrue(false, "Parse exception with post response");
		}
			
		//log.info("Ending TestTask_createTask");
		
		return postResponseString;

	}

	public static HashMap<String,String> getValidTaskBody(String name){
		HashMap<String, String> map = new HashMap<String, String>();
		//map.put("name", "Task Name: " +name);
		map.put("name", "Task: " + name + " - " + TextGeneratorFacade.generateHeadline());
		map.put("description", TextGeneratorFacade.generateParagraph());
		map.put("date_due", "2013-10-28T15:14:00.000");
		map.put("priority", "High");
		map.put("status", "Not Started");
		map.put("call_type", "Close_out_call");
		
    	return map;
	}

	public static HashMap<String,String> populateRelated(String type, String ID, String name){
 		HashMap<String,String> map = new HashMap<String, String>();
 		map.put("related_id", ID);
 		map.put("related_type", type);
 		map.put("related_name", name);
 		return map;
	    }
	
}

