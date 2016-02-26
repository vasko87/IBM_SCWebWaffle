/**
 * 
 */
package com.ibm.salesconnect.API;

/**
 * @author timlehane
 * @date Jul 31, 2013
 */
public class RLIAPITemplates {

	public String createRLI(String RLIID, String userID, String sessionID){
		APIUtilities apiUtilities = new APIUtilities();
		
		return "<?xml version='1.0' encoding='UTF-8'?><SOAP-ENV:Envelope xmlns:ns1='http://www.sugarcrm.com/sugarcrm' xmlns:ns2='http://xml.apache.org/xml-soap' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:SOAP-ENC='http://schemas.xmlsoap.org/soap/encoding/' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'><SOAP-ENV:Header/><SOAP-ENV:Body>"+
		"<ns1:ibm_set_entry>"+
		"   <session>" + sessionID + "</session>"+
		"   <run_as/>"+
		"   <return_data>0</return_data>"+
		"   <records SOAP-ENC:arrayType='ns1:ibm_set_entry_record[6]' xsi:type='SOAP-ENC:Array'>"+
		apiUtilities.getUserSelect(userID) +
		"      <ibm_set_entry_record>"+
		"         <id xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + RLIID + "</id>"+
		"         <id_field xmlns:p0='http://www.sugarcrm.com/sugarcrm'>id</id_field>"+
		"         <module_name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>ibm_RevenueLineItems</module_name>"+
		"         <action xmlns:p0='http://www.sugarcrm.com/sugarcrm'>upsert</action>"+
		"         <return_data xmlns:p0='http://www.sugarcrm.com/sugarcrm'>0</return_data>"+
		"         <related_records SOAP-ENC:arrayType='xsd:string[0]' xsi:type='SOAP-ENC:Array'/>"+
		"         <version xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:integer'/>"+
		"         <name_value_list SOAP-ENC:arrayType='xsd:string[13]' xsi:type='SOAP-ENC:Array'>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>deleted</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>0</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>level10</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>BX300</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>level15</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>KXA</value>"+
		"            </name_value>"+
//		"            <name_value>"+
//		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>level20</name>"+
//		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>BNL00</value>"+
//		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>revenue_type</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>Transactional</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>green_blue_revenue</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>Green</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>swg_contract</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>NEW</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>swg_book_new</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>TRANS</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>swg_tran_det</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>ONE</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>revenue_amount</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>6000</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>currency_iso4217</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>CHF</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>probability</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>10</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>duration</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>1</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>igf_financed_amount</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>0</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>fcast_date_tran</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>2013-10-28</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>fcast_date_sign</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>2013-10-28</value>"+
		"            </name_value>"+
		"         </name_value_list>"+
		"         <relationships SOAP-ENC:arrayType='ns1:ibm_set_entry_relationship[1]' xsi:type='SOAP-ENC:Array'>"+
		apiUtilities.getAssignedUserLinkRelationship(userID)+
		"         </relationships>"+
		"      </ibm_set_entry_record>"+
		"   </records>"+
		"</ns1:ibm_set_entry></SOAP-ENV:Body></SOAP-ENV:Envelope>";
	}
	
	public String createRLI(String RLIID, String userID, String sessionID, String productLevel, String product){
		APIUtilities apiUtilities = new APIUtilities();
		
		return "<?xml version='1.0' encoding='UTF-8'?><SOAP-ENV:Envelope xmlns:ns1='http://www.sugarcrm.com/sugarcrm' xmlns:ns2='http://xml.apache.org/xml-soap' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:SOAP-ENC='http://schemas.xmlsoap.org/soap/encoding/' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'><SOAP-ENV:Header/><SOAP-ENV:Body>"+
		"<ns1:ibm_set_entry>"+
		"   <session>" + sessionID + "</session>"+
		"   <run_as/>"+
		"   <return_data>0</return_data>"+
		"   <records SOAP-ENC:arrayType='ns1:ibm_set_entry_record[6]' xsi:type='SOAP-ENC:Array'>"+
		apiUtilities.getUserSelect(userID) +
		"      <ibm_set_entry_record>"+
		"         <id xmlns:p0='http://www.sugarcrm.com/sugarcrm'>" + RLIID + "</id>"+
		"         <id_field xmlns:p0='http://www.sugarcrm.com/sugarcrm'>id</id_field>"+
		"         <module_name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>ibm_RevenueLineItems</module_name>"+
		"         <action xmlns:p0='http://www.sugarcrm.com/sugarcrm'>upsert</action>"+
		"         <return_data xmlns:p0='http://www.sugarcrm.com/sugarcrm'>0</return_data>"+
		"         <related_records SOAP-ENC:arrayType='xsd:string[0]' xsi:type='SOAP-ENC:Array'/>"+
		"         <version xmlns:p0='http://www.sugarcrm.com/sugarcrm' xsi:type='xsd:integer'/>"+
		"         <name_value_list SOAP-ENC:arrayType='xsd:string[13]' xsi:type='SOAP-ENC:Array'>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>deleted</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>0</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>"+productLevel+"</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>"+product+"</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>revenue_type</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>Transactional</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>green_blue_revenue</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>Green</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>swg_contract</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>NEW</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>swg_book_new</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>TRANS</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>swg_tran_det</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>ONE</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>revenue_amount</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>6000</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>currency_iso4217</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>CHF</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>probability</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>10</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>duration</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>1</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>igf_financed_amount</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>0</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>fcast_date_tran</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>2013-10-28</value>"+
		"            </name_value>"+
		"            <name_value>"+
		"               <name xmlns:p0='http://www.sugarcrm.com/sugarcrm'>fcast_date_sign</name>"+
		"               <value xmlns:p0='http://www.sugarcrm.com/sugarcrm'>2013-10-28</value>"+
		"            </name_value>"+
		"         </name_value_list>"+
		"         <relationships SOAP-ENC:arrayType='ns1:ibm_set_entry_relationship[1]' xsi:type='SOAP-ENC:Array'>"+
		apiUtilities.getAssignedUserLinkRelationship(userID)+
		"         </relationships>"+
		"      </ibm_set_entry_record>"+
		"   </records>"+
		"</ns1:ibm_set_entry></SOAP-ENV:Body></SOAP-ENV:Envelope>";
	}
}
