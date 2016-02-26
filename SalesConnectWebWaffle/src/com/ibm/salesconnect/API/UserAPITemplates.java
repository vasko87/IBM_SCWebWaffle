/**
 * 
 */
package com.ibm.salesconnect.API;

/**
 * @author timlehane
 * @date Nov 6, 2013
 */
public class UserAPITemplates {

	public String selectUser(String sessionID, String userID){
		
		return "<?xml version='1.0' encoding='UTF-8'?><SOAP-ENV:Envelope xmlns:ns1='http://www.sugarcrm.com/sugarcrm' xmlns:ns2='http://xml.apache.org/xml-soap' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:SOAP-ENC='http://schemas.xmlsoap.org/soap/encoding/' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'><SOAP-ENV:Header/><SOAP-ENV:Body>"+
		"<ns1:ibm_set_entry>"+
		"   <session>" + sessionID + "</session>"+
		"   <run_as/>"+
		"   <return_data>0</return_data>"+
		"   <records SOAP-ENC:arrayType='ns1:ibm_set_entry_record[6]' xsi:type='SOAP-ENC:Array'>"+
		"      <ibm_set_entry_record>"+
		"         <id xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + userID + "</id>"+
		"         <id_field xmlns:p0='http://www.sugarcrm.com/sugarcrm'>user_name</id_field>"+
		"         <module_name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>Users</module_name>"+
		"         <action xmlns:p0='http://www.sugarcrm.com/sugarcrm'>select</action>"+
		"         <return_data xmlns:p0='http://www.sugarcrm.com/sugarcrm'>1</return_data>"+
		"         <related_records SOAP-ENC:arrayType='xsd:string[0]' xsi:type='SOAP-ENC:Array'/>"+
		"      </ibm_set_entry_record>"+
		"   </records>"+
		"</ns1:ibm_set_entry></SOAP-ENV:Body></SOAP-ENV:Envelope>";
	}
	
}
