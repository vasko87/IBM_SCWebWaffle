/**
 * 
 */
package com.ibm.salesconnect.API;

/**
 * @author timlehane
 * @date Jul 31, 2013
 */
public class OpportunityAPITemplates {
	
	public String createOpptyWithRLI(String opptyID,String RLIID, String contactExtRefID, String clientID, String userID, String[] opptyTeamMembers, String sessionID){
		APIUtilities apiUtilities = new APIUtilities();
		String opptyTeamMembersSelect = "";
		for (int i = 0; i < opptyTeamMembers.length; i++) {
			opptyTeamMembersSelect+=apiUtilities.getUserSelect(opptyTeamMembers[i]);
		}
		
		String opptyTeamMembersRelationship = "";
		for (int i = 0; i < opptyTeamMembers.length; i++) {
			opptyTeamMembersRelationship+=apiUtilities.getRel_Additional_UsersRelationship(opptyTeamMembers[i]);
		}
		
		return "<?xml version='1.0' encoding='UTF-8'?><SOAP-ENV:Envelope xmlns:ns1='http://www.sugarcrm.com/sugarcrm' xmlns:ns2='http://xml.apache.org/xml-soap' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:SOAP-ENC='http://schemas.xmlsoap.org/soap/encoding/' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'><SOAP-ENV:Header/><SOAP-ENV:Body>"+
		"<ns1:ibm_set_entry>"+
		"   <session>" + sessionID + "</session>"+
		"   <run_as/>"+
		"   <return_data>0</return_data>"+
		"   <records SOAP-ENC:arrayType='ns1:ibm_set_entry_record[6]' xsi:type='SOAP-ENC:Array'>"+
		apiUtilities.getClientSelect(clientID)+
		"      <ibm_set_entry_record>"+
		"         <id xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + contactExtRefID + "</id>"+
		"         <id_field xmlns:p0='http://www.sugarcrm.com/sugarcrm'>ext_ref_id1_c</id_field>"+
		"         <module_name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>Contacts</module_name>"+
		"         <action xmlns:p0='http://www.sugarcrm.com/sugarcrm'>select</action>"+
		"         <return_data xmlns:p0='http://www.sugarcrm.com/sugarcrm'>0</return_data>"+
		"         <related_records SOAP-ENC:arrayType='xsd:string[0]' xsi:type='SOAP-ENC:Array'/>"+
		"      </ibm_set_entry_record>"+
		apiUtilities.getUserSelect(userID) +
		opptyTeamMembersSelect +
		apiUtilities.getRLISelect(RLIID)+
		"      <ibm_set_entry_record>"+
		"         <id xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + opptyID + "</id>"+
		"         <id_field xmlns:p0='http://www.sugarcrm.com/sugarcrm'>id</id_field>"+
		"         <module_name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>Opportunities</module_name>"+
		"         <action xmlns:p0='http://www.sugarcrm.com/sugarcrm'>upsert</action>"+
		"         <return_data xmlns:p0='http://www.sugarcrm.com/sugarcrm'>1</return_data>"+
		"         <related_records SOAP-ENC:arrayType='xsd:string[19]' xsi:type='SOAP-ENC:Array'>"+
		"            <string>opportunities_ibm_ownerassignments</string>"+
		"            <string>opportunity_cmr_link</string>"+
		"            <string>created_by_link</string>"+
		"            <string>modified_user_link</string>"+
		"            <string>assigned_user_link</string>"+
		"            <string>assigned_bp_link</string>"+
		"            <string>accounts</string>"+
		"            <string>accounts/accounts_ibm_addresses</string>"+
		"            <string>contacts</string>"+
		"            <string>notes</string>"+
		"            <string>notes/assigned_user_link</string>"+
		"            <string>rel_additional_users</string>"+
		"            <string>rel_additional_ibm_businesspartners</string>"+
		"            <string>rel_campaign_code_c</string>"+
		"            <string>opportun_revenuelineitems</string>"+
		"            <string>opportun_revenuelineitems/rel_rli_alliancepartners</string>"+
		"            <string>opportun_revenuelineitems/assigned_user_link</string>"+
		"            <string>opportun_revenuelineitems/assigned_bp_link</string>"+
		"            <string>opportun_revenuelineitems/solution</string>"+
		"         </related_records>"+
		"         <version xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:integer'>99</version>"+
		"         <name_value_list SOAP-ENC:arrayType='xsd:string[13]' xsi:type='SOAP-ENC:Array'>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>name</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + opptyID + "</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>version</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>1</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>last_updating_system_c</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>GB01CBCS</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>date_entered</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>2013-07-30 10:34:28</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>description</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>Oppty created description</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>sales_stage</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>03</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>amount</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>1000000</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>currency_iso4217</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>USD</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>restricted</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>RESTNOT</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>date_closed</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>2013-10-28</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>lead_source</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>BRSP</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>international_c</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>0</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>risk_assessment</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>2</value>"+
		"            </name_value>"+
		"         </name_value_list>"+
		"         <relationships SOAP-ENC:arrayType='ns1:ibm_set_entry_relationship[7]' xsi:type='SOAP-ENC:Array'>"+
		apiUtilities.getClientRelationship(clientID)+
		"            <ibm_set_entry_relationship>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>pcontact_id_c</name>"+
		"               <action xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='ibm_set_entry_relationship_action'>create</action>"+
		"               <related_id xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>" + contactExtRefID + "</related_id>"+
		"            </ibm_set_entry_relationship>"+
		apiUtilities.getAssignedUserLinkRelationship(userID)+
		opptyTeamMembersRelationship +
		apiUtilities.getRel_Additional_UsersRelationship(userID)+
		"            <ibm_set_entry_relationship>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>rel_additional_users</name>"+
		"               <action xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='ibm_set_entry_relationship_action'>create</action>"+
		"               <related_id xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>" + userID + "</related_id>"+
		"               <relationship_attributes xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='SOAP-ENC:Array'>"+
		"                  <name_value xsi:type='tns:name_value'>"+
		"                     <name>user_role</name>"+
		"                     <value>IGF</value>"+
		"                  </name_value>"+
		"               </relationship_attributes>"+
		"            </ibm_set_entry_relationship>"+
		apiUtilities.getRLIRelationship(RLIID)+
		"         </relationships>"+
		"      </ibm_set_entry_record>"+
		"   </records>"+
		"</ns1:ibm_set_entry></SOAP-ENV:Body></SOAP-ENV:Envelope>";
	}
	
	public String createOppty(String opptyID, String contactExtRefID, String clientID, String userID, String[] opptyTeamMembers, String sessionID){
		APIUtilities apiUtilities = new APIUtilities();
		String opptyTeamMembersSelect = "";
		for (int i = 0; i < opptyTeamMembers.length; i++) {
			opptyTeamMembersSelect+=apiUtilities.getUserSelect(opptyTeamMembers[i]);
		}
		
		String opptyTeamMembersRelationship = "";
		for (int i = 0; i < opptyTeamMembers.length; i++) {
			opptyTeamMembersRelationship+=apiUtilities.getRel_Additional_UsersRelationship(opptyTeamMembers[i]);
		}
		
		return "<?xml version='1.0' encoding='UTF-8'?><SOAP-ENV:Envelope xmlns:ns1='http://www.sugarcrm.com/sugarcrm' xmlns:ns2='http://xml.apache.org/xml-soap' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:SOAP-ENC='http://schemas.xmlsoap.org/soap/encoding/' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'><SOAP-ENV:Header/><SOAP-ENV:Body>"+
		"<ns1:ibm_set_entry>"+
		"   <session>" + sessionID + "</session>"+
		"   <run_as/>"+
		"   <return_data>0</return_data>"+
		"   <records SOAP-ENC:arrayType='ns1:ibm_set_entry_record[6]' xsi:type='SOAP-ENC:Array'>"+
		apiUtilities.getClientSelect(clientID)+
		"      <ibm_set_entry_record>"+
		"         <id xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + contactExtRefID + "</id>"+
		"         <id_field xmlns:p0='http://www.sugarcrm.com/sugarcrm'>ext_ref_id1_c</id_field>"+
		"         <module_name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>Contacts</module_name>"+
		"         <action xmlns:p0='http://www.sugarcrm.com/sugarcrm'>select</action>"+
		"         <return_data xmlns:p0='http://www.sugarcrm.com/sugarcrm'>0</return_data>"+
		"         <related_records SOAP-ENC:arrayType='xsd:string[0]' xsi:type='SOAP-ENC:Array'/>"+
		"      </ibm_set_entry_record>"+
		apiUtilities.getUserSelect(userID) +
		opptyTeamMembersSelect +
		"      <ibm_set_entry_record>"+
		"         <id xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + opptyID + "</id>"+
		"         <id_field xmlns:p0='http://www.sugarcrm.com/sugarcrm'>id</id_field>"+
		"         <module_name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>Opportunities</module_name>"+
		"         <action xmlns:p0='http://www.sugarcrm.com/sugarcrm'>upsert</action>"+
		"         <return_data xmlns:p0='http://www.sugarcrm.com/sugarcrm'>1</return_data>"+
		"         <related_records SOAP-ENC:arrayType='xsd:string[19]' xsi:type='SOAP-ENC:Array'>"+
		"            <string>opportunities_ibm_ownerassignments</string>"+
		"            <string>opportunity_cmr_link</string>"+
		"            <string>created_by_link</string>"+
		"            <string>modified_user_link</string>"+
		"            <string>assigned_user_link</string>"+
		"            <string>assigned_bp_link</string>"+
		"            <string>accounts</string>"+
		"            <string>accounts/accounts_ibm_addresses</string>"+
		"            <string>contacts</string>"+
		"            <string>notes</string>"+
		"            <string>notes/assigned_user_link</string>"+
		"            <string>rel_additional_users</string>"+
		"            <string>rel_additional_ibm_businesspartners</string>"+
		"            <string>rel_campaign_code_c</string>"+
		"         </related_records>"+
		"         <version xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:integer'>1</version>"+
		"         <name_value_list SOAP-ENC:arrayType='xsd:string[13]' xsi:type='SOAP-ENC:Array'>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>name</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + opptyID + "</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>version</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>1</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>last_updating_system_c</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>GB01CBCS</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>date_entered</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>2013-07-30 10:34:28</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>description</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>Oppty created description</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>sales_stage</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>03</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>amount</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>6000</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>currency_iso4217</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>CHF</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>restricted</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>RESTNOT</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>date_closed</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>2013-10-28</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>lead_source</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>BRSP</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>international_c</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>0</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>risk_assessment</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>2</value>"+
		"            </name_value>"+
		"         </name_value_list>"+
		"         <relationships SOAP-ENC:arrayType='ns1:ibm_set_entry_relationship[7]' xsi:type='SOAP-ENC:Array'>"+
		apiUtilities.getClientRelationship(clientID)+
		"            <ibm_set_entry_relationship>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>pcontact_id_c</name>"+
		"               <action xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='ibm_set_entry_relationship_action'>create</action>"+
		"               <related_id xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>" + contactExtRefID + "</related_id>"+
		"            </ibm_set_entry_relationship>"+
		apiUtilities.getAssignedUserLinkRelationship(userID)+
		opptyTeamMembersRelationship +
		apiUtilities.getRel_Additional_UsersRelationship(userID)+
		"            <ibm_set_entry_relationship>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>rel_additional_users</name>"+
		"               <action xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='ibm_set_entry_relationship_action'>create</action>"+
		"               <related_id xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:string'>" + userID + "</related_id>"+
		"               <relationship_attributes xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='SOAP-ENC:Array'>"+
		"                  <name_value xsi:type='tns:name_value'>"+
		"                     <name>user_role</name>"+
		"                     <value>IGF</value>"+
		"                  </name_value>"+
		"               </relationship_attributes>"+
		"            </ibm_set_entry_relationship>"+
		"         </relationships>"+
		"      </ibm_set_entry_record>"+
		"   </records>"+
		"</ns1:ibm_set_entry></SOAP-ENV:Body></SOAP-ENV:Envelope>";
	}
	
	public String checkOppty(String opptyID, String sessionID){
		
		return "<?xml version='1.0' encoding='UTF-8'?><SOAP-ENV:Envelope xmlns:ns1='http://www.sugarcrm.com/sugarcrm' xmlns:ns2='http://xml.apache.org/xml-soap' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:SOAP-ENC='http://schemas.xmlsoap.org/soap/encoding/' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'><SOAP-ENV:Header/><SOAP-ENV:Body>"+
		"<ns1:ibm_set_entry>"+
		"   <session>" + sessionID + "</session>"+
		"   <run_as/>"+
		"   <return_data>0</return_data>"+
		"   <records SOAP-ENC:arrayType='ns1:ibm_set_entry_record[6]' xsi:type='SOAP-ENC:Array'>"+
		"      <ibm_set_entry_record>"+
		"         <id xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + opptyID + "</id>"+
		"         <id_field xmlns:p0='http://www.sugarcrm.com/sugarcrm'>id</id_field>"+
		"         <module_name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>Opportunities</module_name>"+
		"         <action xmlns:p0='http://www.sugarcrm.com/sugarcrm'>select</action>"+
		"         <return_data xmlns:p0='http://www.sugarcrm.com/sugarcrm'>0</return_data>"+
		"         <related_records SOAP-ENC:arrayType='xsd:string[0]' xsi:type='SOAP-ENC:Array'/>"+
		"      </ibm_set_entry_record>"+
		"   </records>"+
		"</ns1:ibm_set_entry></SOAP-ENV:Body></SOAP-ENV:Envelope>";
	}

	public String deleteOppty(String opptyID, String sessionID){
		
		return "<?xml version='1.0' encoding='UTF-8'?><SOAP-ENV:Envelope xmlns:ns1='http://www.sugarcrm.com/sugarcrm' xmlns:ns2='http://xml.apache.org/xml-soap' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:SOAP-ENC='http://schemas.xmlsoap.org/soap/encoding/' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'><SOAP-ENV:Header/><SOAP-ENV:Body>"+
		"<ns1:ibm_set_entry>"+
		"   <session>" + sessionID + "</session>"+
		"   <run_as/>"+
		"   <return_data>0</return_data>"+
		"   <records SOAP-ENC:arrayType='ns1:ibm_set_entry_record[6]' xsi:type='SOAP-ENC:Array'>"+
		"      <ibm_set_entry_record>"+
		"         <id xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + opptyID + "</id>"+
		"         <id_field xmlns:p0='http://www.sugarcrm.com/sugarcrm'>id</id_field>"+
		"         <module_name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>Opportunities</module_name>"+
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
