/**
 * 
 */
package com.ibm.salesconnect.API;

import com.ibm.salesconnect.objects.Client;

/**
 * @author timlehane
 * @date Aug 1, 2013
 */
public class ClientAPITemplates {
	
	public String selectClient(String clientID, String sessionID, String userID){
		
		return "<?xml version='1.0' encoding='UTF-8'?><SOAP-ENV:Envelope xmlns:ns1='http://www.sugarcrm.com/sugarcrm' xmlns:ns2='http://xml.apache.org/xml-soap' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:SOAP-ENC='http://schemas.xmlsoap.org/soap/encoding/' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'><SOAP-ENV:Header/><SOAP-ENV:Body>"+
		"<ns1:ibm_set_entry>"+
		"   <session>" + sessionID + "</session>"+
		"   <run_as/>"+
		"   <return_data>0</return_data>"+
		"   <records SOAP-ENC:arrayType='ns1:ibm_set_entry_record[6]' xsi:type='SOAP-ENC:Array'>"+
		"      <ibm_set_entry_record>"+
		"         <id xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + clientID + "</id>"+
		"         <id_field xmlns:p0='http://www.sugarcrm.com/sugarcrm'>ccms_id</id_field>"+
		"         <module_name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>Accounts</module_name>"+
		"         <action xmlns:p0='http://www.sugarcrm.com/sugarcrm'>select</action>"+
		"         <return_data xmlns:p0='http://www.sugarcrm.com/sugarcrm'>1</return_data>"+
		"         <related_records SOAP-ENC:arrayType='xsd:string[0]' xsi:type='SOAP-ENC:Array'/>"+
		"      </ibm_set_entry_record>"+
		"   </records>"+
		"</ns1:ibm_set_entry></SOAP-ENV:Body></SOAP-ENV:Envelope>";
	}
	
	public String createClient(Client client,String sessionID,String userID){
		APIUtilities apiUtilities = new APIUtilities();
		String clientTeamMembersSelect = "";
		for (int i = 0; i < client.vClientTeam.size(); i++) {
			clientTeamMembersSelect+=apiUtilities.getUserSelect(client.vClientTeam.get(i));
		}
		
		String clientTeamMembersRelationship = "";
		for (int i = 0; i < client.vClientTeam.size(); i++) {
			clientTeamMembersRelationship+=apiUtilities.getRel_Additional_UsersRelationship(client.vClientTeam.get(i));
		}
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
"         <return_data xmlns:p0='http://www.sugarcrm.com/sugarcrm'>0</return_data>"+
"         <related_records SOAP-ENC:arrayType='xsd:string[0]' xsi:type='SOAP-ENC:Array'/>"+
"      </ibm_set_entry_record>"+
clientTeamMembersSelect +
"      <ibm_set_entry_record>"+
"         <id xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + client.sClientID + "</id>"+
"         <id_field xmlns:p0='http://www.sugarcrm.com/sugarcrm'>ccms_id</id_field>"+
"         <module_name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>Accounts</module_name>"+
"         <action xmlns:p0='http://www.sugarcrm.com/sugarcrm'>upsert</action>"+
"         <return_data xmlns:p0='http://www.sugarcrm.com/sugarcrm'>1</return_data>"+
"         <related_records SOAP-ENC:arrayType='xsd:string[0]' xsi:type='SOAP-ENC:Array'/>"+
"         <version xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:integer'>1</version>"+
"         <name_value_list SOAP-ENC:arrayType='xsd:string[15]' xsi:type='SOAP-ENC:Array'>"+
	"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>assigned_user_name</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>team_name</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>Global</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>modified_by_name</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>ie01 (ie01) bie01</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>created_by_name</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>SFA (SFA) SFA</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>id</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>" + client.sClientID + "</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>name</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>" + client.sClientID + "</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>date_entered</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>2012-08-25 12:07:49</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>date_modified</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>2013-10-10 12:22:52</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>modified_user_id</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>149b8ff5-954e-07b6-8be7-524282250e27</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>created_by</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>5ab34eb6-7429-a415-a76f-5037c7802be0</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>description</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>deleted</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>0</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>assigned_user_id</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>team_id</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>1</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>team_set_id</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>1</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>account_type</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>industry</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>^RR^</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>annual_revenue</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>phone_fax</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>billing_address_street</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>2763 FM 751 # 5175</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>billing_address_city</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>Antofagasta</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>billing_address_state</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>billing_address_postalcode</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>1240000</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>billing_address_country</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>CL</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>rating</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>phone_office</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>123456789</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>phone_alternate</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>website</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>ownership</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>employees</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>ticker_symbol</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>shipping_address_street</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>shipping_address_city</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>shipping_address_state</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>shipping_address_postalcode</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>shipping_address_country</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>email1</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>"+client.sClientID + "@tst.ibm.com</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>parent_id</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>"+client.sParentID + "</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>sic_code</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>parent_name</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>"+client.sParentName + "</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>campaign_id</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>campaign_name</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>account_value_assessment</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>billing_address_county</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>shipping_address_county</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>alt_lang_name_lang_code</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>EN</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>alt_lang_name_sub_lang_code</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>alt_language_name</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>ANTUNA EXILUS</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>anchor_site</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>business_name_kana</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>business_name_kana_lang_code</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>business_name_kana_sub_lang_code</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>buying_group_id</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>buying_group_name</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>buying_group_ccms_id</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>gb_buying_group_ccms_id</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>ccms_id</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>" + client.sClientID + "</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>ccms_level</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>S</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>cmr_number</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>coverage_ids</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>cover_id_rollup</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>^T0000682^</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>default_cmr</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>default_site_name</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>"+client.sClientName + "</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>duns</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>parent_duns</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>dom_ultimate_duns</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>gbl_ultimate_duns</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>empty_var_def</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>indus_class_rollup</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>^RR^</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>indus_sector_rollup</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>^DIST^</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>geo_location_code</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>CLL9999</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>geo_branch_code</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>ZZB999</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>geo_location_code_raw</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>CLL9999</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>geo_branch_type</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>9</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>geo_branch_code_raw</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>ZZB999</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>global_client_id</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>"+ client.sGlobalClientID + "</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>global_client_name</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>"+ client.sGlobalClientName + "</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>ultimate_client_ccms_id</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>"+ client.sUltimateClientCCMS_ID + "</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>global_client_ccms_id</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>"+ client.sGlobalClientCCMS_ID + "</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>ibm_customer_indicator</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>A</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>indus_sic_code</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>indus_class_name</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>indus_isu_name</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>indus_sector_name</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>indus_quad_tier_code</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>name_lang_code</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>EN</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>name_sub_lang_code</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>org_party_id</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>O0074ND4E8</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>privacy_cd</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>W</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>quad_tier_code</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>security_class_cd</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>REG</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>sfa_status</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>5</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>site_name_kana</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>ext_ref_id1_c</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>ext_ref_id2_c</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>last_updating_system_c</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>CCMSSitePubUpdateSugar</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>last_updating_system_date_c</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>leadclient_rep</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>Array</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>twitter_id</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>default_site_id</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>"+ client.sDefaultSiteID + "</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>billing_address_auxiliary</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>shipping_address_auxiliary</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>parent_site_id</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>restricted</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>0</value>"+
	"            </name_value>"+
		"            <name_value>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>ibm_logo_c</name>"+
	"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'/>"+
	"            </name_value>"+
	"         </name_value_list>"+
	"         <relationships SOAP-ENC:arrayType='ns1:ibm_set_entry_relationship[7]' xsi:type='SOAP-ENC:Array'>"+
	"            <ibm_set_entry_relationship>"+
	"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>rel_additional_users</name>"+
	"               <action xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='ibm_set_entry_relationship_action'>create</action>"+
	"               <related_id xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>" + userID + "</related_id>"+
	"               <relationship_attributes xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='SOAP-ENC:Array'>"+
	"                  <name_value xsi:type='tns:name_value'>"+
	"                     <name>user_role</name>"+
	"                     <value>IBMMEMBER</value>"+
	"                  </name_value>"+
	"               </relationship_attributes>"+
	"            </ibm_set_entry_relationship>"+
	clientTeamMembersRelationship +
	"         </relationships>"+
"      </ibm_set_entry_record>"+
"   </records>"+
"</ns1:ibm_set_entry></SOAP-ENV:Body></SOAP-ENV:Envelope>";
	}
	
	/**
	 * Get soap body for soft delete of a site
	 * @param siteID
	 * @param sessionID
	 * @return
	 */
	public String deleteSite(String siteID, String sessionID){
		
		return "<?xml version='1.0' encoding='UTF-8'?><SOAP-ENV:Envelope xmlns:ns1='http://www.sugarcrm.com/sugarcrm' xmlns:ns2='http://xml.apache.org/xml-soap' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:SOAP-ENC='http://schemas.xmlsoap.org/soap/encoding/' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'><SOAP-ENV:Header/><SOAP-ENV:Body>"+
		"<ns1:ibm_set_entry>"+
		"   <session>" + sessionID + "</session>"+
		"   <run_as/>"+
		"   <return_data>0</return_data>"+
		"   <records SOAP-ENC:arrayType='ns1:ibm_set_entry_record[6]' xsi:type='SOAP-ENC:Array'>"+
		"      <ibm_set_entry_record>"+
		"         <id xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + siteID + "</id>"+
		"         <id_field xmlns:p0='http://www.sugarcrm.com/sugarcrm'>id</id_field>"+
		"         <module_name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>Accounts</module_name>"+
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
}
}
