/**
 * 
 */
package com.ibm.salesconnect.API;

/**
 * @author timlehane
 * @date Aug 1, 2013
 */
public class ContactAPITemplate {

	public String createContact(String contactID, String clientID, String sessionID, String firstName, String lastName){
		APIUtilities apiUtilities = new APIUtilities();
		return "<?xml version='1.0' encoding='UTF-8'?><SOAP-ENV:Envelope xmlns:ns1='http://www.sugarcrm.com/sugarcrm' xmlns:ns2='http://xml.apache.org/xml-soap' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:SOAP-ENC='http://schemas.xmlsoap.org/soap/encoding/' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'><SOAP-ENV:Header/><SOAP-ENV:Body>"+
		"<ns1:ibm_set_entry>"+
		"   <session>" + sessionID + "</session>"+
		"   <run_as/>"+
		"   <return_data>1</return_data>"+
		"   <records SOAP-ENC:arrayType='ns1:ibm_set_entry_record[2]' xsi:type='SOAP-ENC:Array'>"+
		apiUtilities.getClientSelect(clientID)+
		"      <ibm_set_entry_record>"+
		"         <id xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + contactID + "</id>"+
		"         <id_field xmlns:p0='http://www.sugarcrm.com/sugarcrm'>id</id_field>"+
		"         <module_name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>Contacts</module_name>"+
		"         <action xmlns:p0='http://www.sugarcrm.com/sugarcrm'>create</action>"+
		"         <return_data xmlns:p0='http://www.sugarcrm.com/sugarcrm'>0</return_data>"+
		"         <related_records SOAP-ENC:arrayType='xsd:string[0]' xsi:type='SOAP-ENC:Array'/>"+
		"         <version xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:integer'>1</version>"+
		"         <name_value_list SOAP-ENC:arrayType='xsd:string[15]' xsi:type='SOAP-ENC:Array'>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>version</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>1</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>update_contacts_last_interaction_date</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>false</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>last_updating_system_c</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>SFASCCIS</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>last_interaction_date_c</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>2013-08-01T00:00:00+00:00</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>alt_lang_first_c</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + firstName + "</value>"+
		"            </name_value>"+
		"            <name_value>          "+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>alt_lang_last_c</name>        "+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + lastName + "</value>   "+
		"            </name_value>"+
		"            <name_value>            "+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>phone_code</name>       "+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>1</value>    "+
		"            </name_value>"+
		"            <name_value>            "+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>phone_work</name>       "+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>5053920</value>    "+
		"            </name_value>"+
		"            <name_value>       "+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>first_name</name>      "+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + firstName + "</value>  "+
		"            </name_value>"+
		"            <name_value>        "+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>last_name</name>     "+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + lastName + "</value>  "+
		"            </name_value>"+
		"            <name_value>        "+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>email1</name>     "+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + contactID + "@tst.ibm.com</value>  "+
		"            </name_value>"+
		"            <name_value>    "+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>contact_status_c</name>     "+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>P</value>"+
		"            </name_value>"+
		"            <name_value>    "+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>contact_coverage_strategy_c</name>     "+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'></value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>primary_address_city</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>GRAND RAPIDS</value>"+
		"            </name_value>"+
		"            <name_value> "+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>primary_address_country</name> "+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>US</value>       "+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>primary_address_postalcode</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>495123934</value>       "+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>primary_address_state</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>MI</value>       "+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>primary_address_street</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>4201 BROADMOOR AVE SE</value>       "+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>ext_ref_id2_c</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>^siebel_ccmsid|S002N1WRHK^</value>"+
		"            </name_value>"+
		"         </name_value_list>"+
		"         <relationships SOAP-ENC:arrayType='ns1:ibm_set_entry_relationship[1]' xsi:type='SOAP-ENC:Array'>"+
		apiUtilities.getClientRelationship(clientID)+
		"         </relationships>"+
		"      </ibm_set_entry_record>"+
		"   </records>"+
		"</ns1:ibm_set_entry></SOAP-ENV:Body></SOAP-ENV:Envelope>";
	}
	
	public String deleteContact(String contactID, String sessionID){
		
		return "<?xml version='1.0' encoding='UTF-8'?><SOAP-ENV:Envelope xmlns:ns1='http://www.sugarcrm.com/sugarcrm' xmlns:ns2='http://xml.apache.org/xml-soap' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:SOAP-ENC='http://schemas.xmlsoap.org/soap/encoding/' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'><SOAP-ENV:Header/><SOAP-ENV:Body>"+
		"<ns1:ibm_set_entry>"+
		"   <session>" + sessionID + "</session>"+
		"   <run_as/>"+
		"   <return_data>0</return_data>"+
		"   <records SOAP-ENC:arrayType='ns1:ibm_set_entry_record[6]' xsi:type='SOAP-ENC:Array'>"+
		"      <ibm_set_entry_record>"+
		"         <id xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + contactID + "</id>"+
		"         <id_field xmlns:p0='http://www.sugarcrm.com/sugarcrm'>id</id_field>"+
		"         <module_name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>Contacts</module_name>"+
		"         <action xmlns:p0='http://www.sugarcrm.com/sugarcrm'>upsert</action>"+
		"         <return_data xmlns:p0='http://www.sugarcrm.com/sugarcrm'>1</return_data>"+
		"         <related_records SOAP-ENC:arrayType='xsd:string[0]' xsi:type='SOAP-ENC:Array'/>"+
		"         <version xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:integer'>1</version>"+
		"         <name_value_list SOAP-ENC:arrayType='xsd:string[13]' xsi:type='SOAP-ENC:Array'>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>deleted</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>1</value>"+
		"            </name_value>"+
		"         </name_value_list>"+
		"      </ibm_set_entry_record>"+
		"   </records>"+
		"</ns1:ibm_set_entry></SOAP-ENV:Body></SOAP-ENV:Envelope>";
}}
