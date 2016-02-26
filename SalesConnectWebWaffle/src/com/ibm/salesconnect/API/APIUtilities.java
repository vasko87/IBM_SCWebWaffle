/**
 * 
 */
package com.ibm.salesconnect.API;

import java.io.ByteArrayOutputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.salesconnect.common.GC;

/**
 * @author timlehane
 * @date Jul 31, 2013
 */
public class APIUtilities {
	Logger log = LoggerFactory.getLogger(APIUtilities.class);

	public String getUserSelect(String userID){
		return "      <ibm_set_entry_record>"+
		"         <id xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + userID + "</id>"+
		"         <id_field xmlns:p0='http://www.sugarcrm.com/sugarcrm'>user_name</id_field>"+
		"         <module_name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>Users</module_name>"+
		"         <action xmlns:p0='http://www.sugarcrm.com/sugarcrm'>select</action>"+
		"         <return_data xmlns:p0='http://www.sugarcrm.com/sugarcrm'>0</return_data>"+
		"         <related_records SOAP-ENC:arrayType='xsd:string[0]' xsi:type='SOAP-ENC:Array'/>"+
		"      </ibm_set_entry_record>";
	}
	
	public String getClientSelect(String clientID){
		return 		"      <ibm_set_entry_record>"+
		"         <id xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + clientID + "</id>"+
		"         <id_field xmlns:p0='http://www.sugarcrm.com/sugarcrm'>ccms_id</id_field>"+
		"         <module_name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>Accounts</module_name>"+
		"         <action xmlns:p0='http://www.sugarcrm.com/sugarcrm'>select</action>"+
		"         <return_data xmlns:p0='http://www.sugarcrm.com/sugarcrm'>1</return_data>"+
		"         <related_records SOAP-ENC:arrayType='xsd:string[0]' xsi:type='SOAP-ENC:Array'/>"+
		"      </ibm_set_entry_record>";
	}
	
	public String getRLISelect(String RLIID){
		if (RLIID.length()==0) {
			return "";
		}
		return 		"      <ibm_set_entry_record>"+
		"         <id xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + RLIID + "</id>"+
		"         <id_field xmlns:p0='http://www.sugarcrm.com/sugarcrm'>id</id_field>"+
		"         <module_name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>ibm_RevenueLineItems</module_name>"+
		"         <action xmlns:p0='http://www.sugarcrm.com/sugarcrm'>select</action>"+
		"         <return_data xmlns:p0='http://www.sugarcrm.com/sugarcrm'>0</return_data>"+
		"         <related_records SOAP-ENC:arrayType='xsd:string[0]' xsi:type='SOAP-ENC:Array'/>"+
		"      </ibm_set_entry_record>";
	}
	
	public String getRel_Additional_UsersRelationship(String userID){
		return 		"            <ibm_set_entry_relationship>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>rel_additional_users</name>"+
		"               <action xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='ibm_set_entry_relationship_action'>create</action>"+
		"               <related_id xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>" + userID + "</related_id>"+
		"               <relationship_attributes xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='SOAP-ENC:Array'>"+
		"                  <name_value xsi:type='tns:name_value'>"+
		"                     <name>user_role</name>"+
		"                     <value>IBMMEMBER</value>"+
		"                  </name_value>"+
		"               </relationship_attributes>"+
		"            </ibm_set_entry_relationship>";
	}
	
	public String getClientRelationship(String clientID){
		return 		"            <ibm_set_entry_relationship>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>accounts</name>"+
		"               <action xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='ibm_set_entry_relationship_action'>create</action>"+
		"               <related_id xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>" + clientID + "</related_id>"+
		"            </ibm_set_entry_relationship>";
	}
	
	public String getAssignedUserLinkRelationship(String userID){
		return 		"            <ibm_set_entry_relationship>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>assigned_user_link</name>"+
		"               <action xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='ibm_set_entry_relationship_action'>create</action>"+
		"               <related_id xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>" + userID + "</related_id>"+
		"            </ibm_set_entry_relationship>";
	}
	
	public String getRLIRelationship(String RLIID){
		if (RLIID.length()==0) {
			return "";
		}
		return 		"            <ibm_set_entry_relationship>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>opportun_revenuelineitems</name>"+
		"               <action xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='ibm_set_entry_relationship_action'>create</action>"+
		"               <related_id xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>" + RLIID + "</related_id>"+
		"            </ibm_set_entry_relationship>";
	}
	
/**
 * Utility method to create the soapmessage to request the data for a given object
 * @param sessionID
 * @param id
 * @param id_field
 * @param module_name
 * @return soap message
 */
	public SOAPMessage createSOAPSelectRequest(String sessionID, String id, String id_field, String module_name){

        MessageFactory factory;
        SOAPMessage soapMessage = null;
        
		try {
			factory = MessageFactory.newInstance();

        soapMessage = factory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("SOAP-ENC", "http://schemas.xmlsoap.org/soap/encoding/");
        envelope.addNamespaceDeclaration("ns1", "http://www.sugarcrm.com/sugarcrm" );
        envelope.addNamespaceDeclaration("ns2", "http://xml.apache.org/xml-soap");
        envelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema" );
        envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        
        SOAPBody soapBody = envelope.getBody();
        SOAPElement ibm_set_entry = soapBody.addChildElement("ibm_set_entry","ns1");

        ibm_set_entry.addChildElement("session").addTextNode(sessionID);
        ibm_set_entry.addChildElement("run_as");
        ibm_set_entry.addChildElement("return_data").addTextNode("0");
        
        SOAPElement records = ibm_set_entry.addChildElement("records");
        records.addAttribute(new QName(null,"arrayType","SOAP-ENC"), "ns1:ibm_set_entry_record[6]");
        records.addAttribute(new QName(null,"type","xsi"), "SOAP-ENC:Array");
        
        SOAPElement ibm_set_entry_record = records.addChildElement("ibm_set_entry_record");
        ibm_set_entry_record.addChildElement("id").addAttribute(new QName(null,"p0","xmlns"),"http://www.sugarcrm.com/sugarcrm").addTextNode(id);
        ibm_set_entry_record.addChildElement("id_field").addAttribute(new QName(null,"p0","xmlns"),"http://www.sugarcrm.com/sugarcrm").addTextNode(id_field);
        ibm_set_entry_record.addChildElement("module_name").addAttribute(new QName(null,"p0","xmlns"),"http://www.sugarcrm.com/sugarcrm").addTextNode(module_name);
        ibm_set_entry_record.addChildElement("action").addAttribute(new QName(null,"p0","xmlns"),"http://www.sugarcrm.com/sugarcrm").addTextNode("select");
        ibm_set_entry_record.addChildElement("return_data").addAttribute(new QName(null,"p0","xmlns"),"http://www.sugarcrm.com/sugarcrm").addTextNode("1");
        SOAPElement related_records = ibm_set_entry_record.addChildElement("related_records");
        related_records.addAttribute(new QName(null,"arrayType","SOAP-ENC"), "xsd:string[0]");
        related_records.addAttribute(new QName(null,"type","xsi"), "SOAP-ENC:Array");

        MimeHeaders hd = soapMessage.getMimeHeaders();
        hd.addHeader("IBM-salesconnect-key","passw0rd");
        soapMessage.saveChanges();
		} catch (SOAPException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Creation of SOAP check request failed");
		}

		return soapMessage;
	}
	
	public String getValueFromSelectResponse(SOAPMessage soapResponse, String name){
		try{
        SOAPFactory soapFactory = SOAPFactory.newInstance();
        SOAPBody MyBody = soapResponse.getSOAPBody();
        SOAPElement e = (SOAPBodyElement) MyBody.getChildElements(soapFactory.createName("ibm_set_entryResponse","ns1","http://www.sugarcrm.com/sugarcrm")).next();
        SOAPElement f = (SOAPElement) e.getChildElements(soapFactory.createName("return")).next();
        SOAPElement g  = (SOAPElement) f.getChildElements(soapFactory.createName("record_status")).next();
        SOAPElement h  = (SOAPElement) g.getChildElements(soapFactory.createName("item")).next();
        SOAPElement i  = (SOAPElement) h.getChildElements(soapFactory.createName("return_data")).next();
        Iterator itr  = i.getChildElements(soapFactory.createName("item"));

        while (itr.hasNext()) {
        	SOAPElement k  = (SOAPElement) itr.next();
        	SOAPElement m = (SOAPElement) k.getChildElements(soapFactory.createName("name")).next();
        	if (m.getFirstChild().getNodeValue().equals(name)) {
				return (m.getNextSibling().getFirstChild().getNodeValue());
			}
        }
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false); 
		}
		Assert.assertTrue(false, "Client name has not been returned correctly in SOAP response");
        return ""; 
	}
	
	/**
	 * Retrieve the client bean id (the sugar id) from the ccms id
	 * @param URL
	 * @param CCMS_ID
	 * @param username
	 * @param password
	 * @param session
	 * @return client bean id
	 */
	public String getClientBeanIDFromCCMSID(String URL, String CCMS_ID, String username, String password, String session){
		APIUtilities apiUtilities = new APIUtilities();
		SOAPMessage soapResponse = null;
		try{
			log.info("Sending request to get client bean id from ccms id");
			SSLContext.setDefault(getSSLContext());
			HttpsURLConnection.setDefaultHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			// Send SOAP Message to SOAP Server
			log.info("Getting bean id for client: "+CCMS_ID);
			SOAPMessage soap = apiUtilities.createSOAPSelectRequest(session, CCMS_ID, "ccms_id", "Accounts");
			soapResponse = soapConnection.call(soap, URL+GC.soapURLExtension);
			
			//DEBUGCODE
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			soapResponse.writeTo(out);
			String strMsg = new String(out.toByteArray());
			System.out.println(strMsg);
	//DEBUGCODE	
				
		}
		catch (Exception e) {
			log.info("SOAP Exception" + e.getLocalizedMessage());
			Assert.assertTrue(false);
		}

        return getValueFromSelectResponse(soapResponse, "id");
	}

	/**
	 * Retrieve the client bean id (the sugar id) from the ccms id
	 * @param URL
	 * @param CCMS_ID
	 * @param username
	 * @param password
	 * @return client bean id
	 */
	public String getClientBeanIDFromCCMSID(String URL, String CCMS_ID, String username, String password){
		SugarAPI sugarAPI = new SugarAPI();
		APIUtilities apiUtilities = new APIUtilities();
		String session = sugarAPI.getSessionID(URL, username, password);
		SOAPMessage soapResponse = null;
		try{
			log.info("Sending request to get client bean id from ccms id");
			SSLContext.setDefault(getSSLContext());
			HttpsURLConnection.setDefaultHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			// Send SOAP Message to SOAP Server
			SOAPMessage soap = apiUtilities.createSOAPSelectRequest(session, CCMS_ID, "ccms_id", "Accounts");
			soapResponse = soapConnection.call(soap, URL+GC.soapURLExtension);
		}
		catch (Exception e) {
			log.info("SOAP Exception" + e.getLocalizedMessage());
			Assert.assertTrue(false);
		}
        return getValueFromSelectResponse(soapResponse, "id");
	}
	
	/**
	 * Retrieve the user bean id from the email address of that user
	 * @param URL
	 * @param username
	 * @param password
	 * @param session
	 * @return user bean id
	 */
	public String getUserBeanIDFromEmail(String URL, String username, String password, String session){
		APIUtilities apiUtilities = new APIUtilities();
		SOAPMessage soapResponse = null;
		try{
			log.info("Sending request to get user bean id from email");

			SSLContext.setDefault(getSSLContext());
			HttpsURLConnection.setDefaultHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        
			// Send SOAP Message to SOAP Server
			SOAPMessage soap = apiUtilities.createSOAPSelectRequest(session, username, "user_name", "Users");
			soapResponse = soapConnection.call(soap, URL+GC.soapURLExtension);
		}
		catch (Exception e) {
			log.info("SOAP Exception" + e.getLocalizedMessage());
			e.printStackTrace();
		}
        return getValueFromSelectResponse(soapResponse, "id");
	}
	
	/**
	 * Retrieve the user bean id from the email address of that user
	 * @param URL
	 * @param username
	 * @param password
	 * @return user bean id
	 */
	public String getUserBeanIDFromEmail(String URL, String username, String password){
		SugarAPI sugarAPI = new SugarAPI();
		APIUtilities apiUtilities = new APIUtilities();
		String session = sugarAPI.getSessionID(URL, username, password);
		SOAPMessage soapResponse = null;
		try{
			log.info("Sending request to get user bean id from email");

			SSLContext.setDefault(getSSLContext());
			HttpsURLConnection.setDefaultHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        
			// Send SOAP Message to SOAP Server
			SOAPMessage soap = apiUtilities.createSOAPSelectRequest(session, username, "user_name", "Users");
			soapResponse = soapConnection.call(soap, URL+GC.soapURLExtension);
		}
		catch (Exception e) {
			log.info("SOAP Exception" + e.getLocalizedMessage());
			e.printStackTrace();
		}
        return getValueFromSelectResponse(soapResponse, "id");
	}
	
	private SSLContext getSSLContext(){
		  SSLContext sslContext = null;;
			try {
				sslContext = SSLContext.getInstance("SSL");
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
				Assert.assertTrue(false,"NoSuchAlgorithmException failure when sending post request");
			}

			// set up a TrustManager that trusts everything
			try {
				sslContext.init(null, new TrustManager[] { new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers() {
						log.info("getAcceptedIssuers =============");
					            return null;
					}

					public void checkClientTrusted(X509Certificate[] certs,String authType) {
					    log.info("checkClientTrusted =============");
					}

					public void checkServerTrusted(X509Certificate[] certs,String authType) {
					    log.info("checkServerTrusted =============");
					}
				} }, new SecureRandom());
			} catch (KeyManagementException e1) {
				e1.printStackTrace();
				Assert.assertTrue(false,"KeyManagementException failure when sending post request");
			}
			return sslContext;
	}
	
	/**
	 * Check if a key value pair is present in a given json string
	 * @param jsonString
	 * @param id
	 * @param value
	 * @return true if present false if not
	 */
	public Boolean checkIfValuePresentInJson(String jsonString, String id, String value){
		  JSONParser parser = new JSONParser();
		  KeyFinder finder = new KeyFinder();
		  finder.setMatchKey(id);
		  
		  try{
		    while(!finder.isEnd()){
		      parser.parse(jsonString, finder, true);
		      if(finder.isFound()){
		        finder.setFound(false);
		        if (finder.getValue().equals(value)) {
					return true;
				}
		      }
		    }           
		  }
		  catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return false;
	}
	
	/**
	 * Check if a key value pair is present in a given json string
	 * @param jsonString
	 * @param id
	 * @param value
	 * @return true if present false if not
	 */
	public Boolean checkIfValueContainedInJson(String jsonString, String id, String value){
		  JSONParser parser = new JSONParser();
		  KeyFinder finder = new KeyFinder();
		  finder.setMatchKey(id);
		  
		  try{
		    while(!finder.isEnd()){
		      parser.parse(jsonString, finder, true);
		      if(finder.isFound()){
		        finder.setFound(false);
		        if (finder.getValue().toString().contains(value)) {
					return true;
				}
		      }
		    }           
		  }
		  catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return false;
	}
	
	/**
	 * Check if a key value pair is present in a given json string
	 * @param jsonString
	 * @param id
	 * @param value
	 * @return true if present false if not
	 */
	public Boolean checkIfValuePresentInJson(String jsonString, String id, int value){
		  JSONParser parser = new JSONParser();
		  KeyFinder finder = new KeyFinder();
		  finder.setMatchKey(id);
		  
		  try{
		    while(!finder.isEnd()){
		      parser.parse(jsonString, finder, true);
		      if(finder.isFound()){
		        finder.setFound(false);
		        if (finder.getValue().toString().equals(String.valueOf(value))) {
					return true;
				}
		      }
		    }           
		  }
		  catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return false;
	}
	
	/**
	 * Check if a key value pair is present in a given json string
	 * @param jsonString
	 * @param id
	 * @param value
	 * @return true if present false if not
	 */
	public String returnValuePresentInJson(String jsonString, String id){
		  JSONParser parser = new JSONParser();
		  KeyFinder finder = new KeyFinder();
		  finder.setMatchKey(id);
		  
		  try{
		    while(!finder.isEnd()){
		      parser.parse(jsonString, finder, true);
		      if(finder.isFound()){
		        finder.setFound(false);
		        return finder.getValue().toString();
		      }
		    }           
		  }
		  catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return "false";
	}
	/**
	 * Get the count of times a key is found in a json string
	 * @param jsonString
	 * @param id
	 * @param value
	 * @return count for times the key is found in the string
	 */
	public int getKeyCountInJsonString(String jsonString, String key){
		System.out.println(jsonString);
		  JSONParser parser = new JSONParser();
		  KeyFinder finder = new KeyFinder();
		  finder.setMatchKey(key);
		  int count = 0;
		  try{
		    while(!finder.isEnd()){
		      parser.parse(jsonString, finder, true);
		      if(finder.isFound()){
		        finder.setFound(false);
		        count++;
		      }
		    }           
		  }
		  catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return count;
	}

}
