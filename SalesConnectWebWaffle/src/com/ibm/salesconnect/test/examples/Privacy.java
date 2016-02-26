/**
 * 
 */
package com.ibm.salesconnect.test.examples;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.salesconnect.common.DB2Connection;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;

/**
 * @author timlehane
 * @date Sep 2, 2013
 */
public class Privacy extends ProductBaseTest {

	Logger log = LoggerFactory.getLogger(Privacy.class);

	
	@Test(groups = { ""})
	public void Test_Privacy() throws SQLException{
		DB2Connection eurDB2test = new DB2Connection("jdbc:db2://svt1eudb2.rtp.raleigh.ibm.com:50000/saleeur", "sceid", "jan14pwd");
		Connection eurConntest;

		eurConntest = eurDB2test.connectToDb2();
		
		eurConntest.close();
		
		
		log.info("Opening connection to main db");
		//DB2Connection mainDB2 = new DB2Connection("jdbc:db2://svt5oltp01.rtp.raleigh.ibm.com:50000/saleconn", "sctid", "ustay0ut");
//		DB2Connection mainDB2 = new DB2Connection("jdbc:db2://svt4oltp01.rtp.raleigh.ibm.com:50000/saleconn", "sctid", "g0away45");
		DB2Connection mainDB2 = new DB2Connection("jdbc:db2://svt1oltp01.rtp.raleigh.ibm.com:50000/saleconn", "sctid", "jun13pwd");
		Connection conn;

		conn = mainDB2.connectToDb2();

		Statement statement = null;
		boolean resultSetIsEmpty = true;
		
		log.info("Generating SQL select statement");
		String sqlSelectStatement = "select con.ID,ADDRESS_ALTERNATE_EXT_REF_C,ADDRESS_PRIMARY_EXT_REF_C,EXT_REF_ID1_C,EXT_REF_ID2_C,KEY_CONTACT_C,KEY_CONTACT_RELATION_C,"+
			"CONTACT_STATUS_C,SUPP_PERM_C, PRIMARY_ADDRESS_COUNTRY, PHONE_CODE, EMAIL_ADDRESS_ID, PREFERRED_NAME_C ,LANGUAGE_C,LAST_UPDATING_SYSTEM_C,LAST_UPDATING_SYSTEM_DATE_C,"+ 
			"ALT_LANG_FIRST_C,ALT_LANG_LAST_C,ALT_LANG_PREFERRED_NAME_C,LAST_INTERACTION_DATE_C,ALT_LANGUAGE_C,CLIENT_VALUE_SURVEY_C,CONTACT_COVERAGE_STRATEGY_C, "+
			"DATE_ENTERED, con.DATE_MODIFIED, MODIFIED_USER_ID, CREATED_BY, DESCRIPTION, con.DELETED, ASSIGNED_USER_ID, TEAM_ID, TEAM_SET_ID, SALUTATION, FIRST_NAME, LAST_NAME, "+
			"TITLE, DEPARTMENT, DO_NOT_CALL, PHONE_HOME, PHONE_WORK, PHONE_MOBILE, PHONE_OTHER, PHONE_FAX, PRIMARY_ADDRESS_STREET, PRIMARY_ADDRESS_CITY, PRIMARY_ADDRESS_STATE, "+
			"PRIMARY_ADDRESS_POSTALCODE, PRIMARY_ADDRESS_COUNTRY, ALT_ADDRESS_STREET, ALT_ADDRESS_CITY, ALT_ADDRESS_STATE, ALT_ADDRESS_POSTALCODE, ALT_ADDRESS_COUNTRY, ASSISTANT, "+
			"ASSISTANT_PHONE, PICTURE, LEAD_SOURCE, REPORTS_TO_ID, BIRTHDATE, PORTAL_NAME, PORTAL_ACTIVE, PORTAL_PASSWORD, PORTAL_APP, CAMPAIGN_ID, ADDRESS_SUPPRESSED, CONTACT_NAME_KANA, "+
			"PHONE_WORK_SUPPRESSED, PHONE_MOBILE_SUPPRESSED, PHONE_CODE, PHONE_FAX_SUPPRESSED, VERSION, PREFERRED_LANGUAGE "+
			"from sctid.CONTACTS con "+
			"left outer join sctid.contacts_cstm cstm on cstm.ID_C = con.ID "+
			"left outer join sctid.email_addr_bean_rel email on email.bean_id = con.ID AND email.deleted = 0 "+
			"where con.ID IN (select ID from sctid.contacts where FIRST_NAME like '%PRIVATE_EU_%' AND sctid.contacts.DELETED = 0)";	

		log.info("Executing SQL select statement");
		statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(sqlSelectStatement);
		ArrayList<String[]> privacyList = new ArrayList<String[]>();

		log.info("Populating data structure with information from sql select");
		while (rs.next()) {
			String[] row = new String[72];
			resultSetIsEmpty = false;
			row[0] = rs.getString("ID").trim();
			row[1] = rs.getString("ADDRESS_ALTERNATE_EXT_REF_C");
			row[2] = rs.getString("ADDRESS_PRIMARY_EXT_REF_C");
			row[3] = rs.getString("EXT_REF_ID1_C");
			row[4] = rs.getString("EXT_REF_ID2_C");
			row[5] = rs.getString("KEY_CONTACT_C");
			row[6] = rs.getString("KEY_CONTACT_RELATION_C");
			row[7] = rs.getString("CONTACT_STATUS_C");
			row[8] = rs.getString("SUPP_PERM_C");
			row[9] = rs.getString("PRIMARY_ADDRESS_COUNTRY");
			row[10] = rs.getString("PHONE_CODE");
			row[11] = rs.getString("EMAIL_ADDRESS_ID");
			row[12] = rs.getString("PREFERRED_NAME_C");
			row[13] = rs.getString("LANGUAGE_C");
			row[14] = rs.getString("LAST_UPDATING_SYSTEM_C");
			row[15] = rs.getString("LAST_UPDATING_SYSTEM_DATE_C");
			row[16] = rs.getString("ALT_LANG_FIRST_C");
			row[17] = rs.getString("ALT_LANG_LAST_C");
			row[18] = rs.getString("ALT_LANG_PREFERRED_NAME_C");
			row[19] = rs.getString("LAST_INTERACTION_DATE_C");
			row[20] = rs.getString("ALT_LANGUAGE_C");
			row[21] = rs.getString("CLIENT_VALUE_SURVEY_C");
			row[22] = rs.getString("CONTACT_COVERAGE_STRATEGY_C");
			row[23] = rs.getString("DATE_ENTERED");
			row[24] = rs.getString("DATE_MODIFIED");
			row[25] = rs.getString("MODIFIED_USER_ID");
			row[26] = rs.getString("CREATED_BY");
			row[27] = rs.getString("DESCRIPTION");
			row[28] = rs.getString("DELETED");
			row[29] = rs.getString("ASSIGNED_USER_ID");
			row[30] = rs.getString("TEAM_ID");
			row[31] = rs.getString("TEAM_SET_ID");
			row[32] = rs.getString("SALUTATION");
			row[33] = rs.getString("FIRST_NAME");
			row[34] = rs.getString("LAST_NAME");
			row[35] = rs.getString("TITLE");
			row[36] = rs.getString("DEPARTMENT");
			row[37] = rs.getString("DO_NOT_CALL");
			row[38] = rs.getString("PHONE_HOME");
			row[39] = rs.getString("PHONE_WORK");
			row[40] = rs.getString("PHONE_MOBILE");
			row[41] = rs.getString("PHONE_OTHER");
			row[42] = rs.getString("PHONE_FAX");
			row[43] = rs.getString("PRIMARY_ADDRESS_STREET");
			row[44] = rs.getString("PRIMARY_ADDRESS_CITY");
			try{
			row[44] = row[44].replace("'", "''");
			}
			catch (Exception e) {
			}
			row[45] = rs.getString("PRIMARY_ADDRESS_STATE");
			row[46] = rs.getString("PRIMARY_ADDRESS_POSTALCODE");
			row[47] = rs.getString("PRIMARY_ADDRESS_COUNTRY");
			row[48] = rs.getString("ALT_ADDRESS_STREET");
			row[49] = rs.getString("ALT_ADDRESS_CITY");
			try{
			row[49] = row[49].replace("'", "''");
			}
			catch (Exception e) {
			}
			row[50] = rs.getString("ALT_ADDRESS_STATE");
			row[51] = rs.getString("ALT_ADDRESS_POSTALCODE");
			row[52] = rs.getString("ALT_ADDRESS_COUNTRY");
			row[53] = rs.getString("ASSISTANT");
			row[54] = rs.getString("ASSISTANT_PHONE");
			row[55] = rs.getString("PICTURE");
			row[56] = rs.getString("LEAD_SOURCE");
			row[57] = rs.getString("REPORTS_TO_ID");
			row[58] = rs.getString("BIRTHDATE");
			row[59] = rs.getString("PORTAL_NAME");
			row[60] = rs.getString("PORTAL_ACTIVE");
			row[61] = rs.getString("PORTAL_PASSWORD");
			row[62] = rs.getString("PORTAL_APP");
			row[63] = rs.getString("CAMPAIGN_ID");
			row[64] = rs.getString("ADDRESS_SUPPRESSED");
			row[65] = rs.getString("CONTACT_NAME_KANA");
			row[66] = rs.getString("PHONE_WORK_SUPPRESSED");
			row[67] = rs.getString("PHONE_MOBILE_SUPPRESSED");
			row[68] = rs.getString("PHONE_CODE");
			row[69] = rs.getString("PHONE_FAX_SUPPRESSED");
			row[70] = rs.getString("VERSION");
			row[71] = rs.getString("PREFERRED_LANGUAGE");
			privacyList.add(row);
		}

		Assert.assertEquals(resultSetIsEmpty, false, "No results returned from the main DB, this means that there no privacy contact were found");
		
		log.info("Total number of contacts to be added " + privacyList.size());
		log.info("Closing connection to main db");
		statement.close();
		conn.close();
		
		log.info("Opening connection to privacy db");
//		DB2Connection eurDB2 = new DB2Connection("jdbc:db2://svt5eudb2.rtp.raleigh.ibm.com:50000/saleeur", "sceid", "pwd4eudb");
//		DB2Connection eurDB2 = new DB2Connection("jdbc:db2://svt4eudb2.rtp.raleigh.ibm.com:50000/saleeur", "sceid", "jul13pwd");
		DB2Connection eurDB2 = new DB2Connection("jdbc:db2://svt1eudb2.rtp.raleigh.ibm.com:50000/saleeur", "sceid", "jan14pwd");
		Connection eurConn;

		eurConn = eurDB2.connectToDb2();

		Statement eurStatement = null;
		
		log.info("Starting to populate privacy database");
		for (int i = 0; i < privacyList.size(); i=i+5) {
			eurStatement = eurConn.createStatement();
			String sqlEmailInsertStatement = "insert into sctid.email_addresses_eu (ID, EMAIL_ADDRESS, EMAIL_ADDRESS_CAPS, INVALID_EMAIL, OPT_OUT, DATE_CREATED, DATE_MODIFIED, DELETED)"+
			" values ";
			String sqlCSTMInsertStatement = "insert into sctid.contacts_cstm_eu (ID_C, ADDRESS_ALTERNATE_EXT_REF_C,ADDRESS_PRIMARY_EXT_REF_C,EXT_REF_ID1_C,EXT_REF_ID2_C,KEY_CONTACT_C," +
			"KEY_CONTACT_RELATION_C,CONTACT_STATUS_C,SUPP_PERM_C,PREFERRED_NAME_C,LANGUAGE_C,LAST_UPDATING_SYSTEM_C,LAST_UPDATING_SYSTEM_DATE_C,ALT_LANG_FIRST_C,"+
			"ALT_LANG_LAST_C,ALT_LANG_PREFERRED_NAME_C,LAST_INTERACTION_DATE_C,ALT_LANGUAGE_C,CLIENT_VALUE_SURVEY_C,CONTACT_COVERAGE_STRATEGY_C)"+
			" values "; 
			String sqlContactInsertStatement = "insert into sctid.contacts_eu (ID, DATE_ENTERED, DATE_MODIFIED, MODIFIED_USER_ID, CREATED_BY, DESCRIPTION, DELETED, ASSIGNED_USER_ID, TEAM_ID, TEAM_SET_ID, SALUTATION, FIRST_NAME, LAST_NAME, "+
			"TITLE, DEPARTMENT, DO_NOT_CALL, PHONE_HOME, PHONE_WORK, PHONE_MOBILE, PHONE_OTHER, PHONE_FAX, PRIMARY_ADDRESS_STREET, PRIMARY_ADDRESS_CITY, PRIMARY_ADDRESS_STATE, "+
			"PRIMARY_ADDRESS_POSTALCODE, PRIMARY_ADDRESS_COUNTRY, ALT_ADDRESS_STREET, ALT_ADDRESS_CITY, ALT_ADDRESS_STATE, ALT_ADDRESS_POSTALCODE, ALT_ADDRESS_COUNTRY, ASSISTANT, "+
			"ASSISTANT_PHONE, PICTURE, LEAD_SOURCE, REPORTS_TO_ID, BIRTHDATE, PORTAL_NAME, PORTAL_ACTIVE, PORTAL_PASSWORD, PORTAL_APP, CAMPAIGN_ID, ADDRESS_SUPPRESSED, CONTACT_NAME_KANA, "+
			"PHONE_WORK_SUPPRESSED, PHONE_MOBILE_SUPPRESSED, PHONE_CODE, PHONE_FAX_SUPPRESSED, VERSION, PREFERRED_LANGUAGE)"+
			" values ";
			
			for (int j = 0; j < 5; j++) {
				String row[] = privacyList.get(i+j);
				if (row[11]!=null) {
					sqlEmailInsertStatement += "('" + row[11] + "', '"+row[0]+"@email.com', '" + row[0].toUpperCase() + "@EMAIL.COM', 0, 0,'2013-10-01 12:00:00.0','2013-10-01 12:00:00.0', 0),";
				}
				
				sqlCSTMInsertStatement += "('" + row[0]+"','"+row[1]+"','"+row[2]+"','"+row[3]+"','"+row[4]+"',"+row[5]+","+row[6]+",'"+row[7]+"',"+row[8]+",'PN"+row[0]+"','"+row[13]+"','"+
							row[14]+"','2013-10-01 12:00:00.0','AFN"+row[0]+"','ALN"+row[0]+"','APN"+row[0]+"','2013-10-01 12:00:00.0','"+row[20]+"','"+row[21]+"','"+row[22]+"'),";
				sqlContactInsertStatement += "('" + row[0]+"','2013-10-01 12:00:00.0','2013-10-01 12:00:00.0','"+row[25]+"','"+row[26]+"','"+row[27]+"','"+row[28]+"','"+row[29]+"','"+row[30]+"','"+row[31]+"','Dr.','FN"+row[0]
							+"','LN"+row[0]+"','CIO','"+row[36]+"','"+row[37]+"','88888888','23232323','13131313','15151515','66666666','PAS"+row[0]+"','"+row[44]+"','"+row[45]+"','PC"+row[0]+"','"
							+row[47]+"','AAS"+row[0]+"','"+row[49]+"','"+row[50]+"','PC"+row[0]+"','"+row[52]+"','"+row[53]+"','"+row[54]+"','"+row[55]+"','"+row[56]+"','"+row[57]+"','2013-10-01','"+row[59]+"','"
							+row[60]+"','"+row[61]+"','"+row[62]+"','"+row[63]+"','"+row[64]+"','"+row[65]+"','"+row[66]+"','"+row[67]+"','"+row[68]+"','"+row[69]+"','"+row[70]+"','"+row[71]+"'),";
				
			}
			
			sqlEmailInsertStatement = sqlEmailInsertStatement.substring(0, sqlEmailInsertStatement.length()-1);
			sqlEmailInsertStatement = sqlEmailInsertStatement.replace("null",",NULL" );
			if(sqlEmailInsertStatement.length()>150){
			eurStatement.addBatch(sqlEmailInsertStatement);
			}
			sqlCSTMInsertStatement = sqlCSTMInsertStatement.substring(0, sqlCSTMInsertStatement.length()-1);
			sqlCSTMInsertStatement = sqlCSTMInsertStatement.replace(",'null'",",NULL" );
			eurStatement.addBatch(sqlCSTMInsertStatement);
			
			sqlContactInsertStatement = sqlContactInsertStatement.substring(0, sqlContactInsertStatement.length()-1);
			sqlContactInsertStatement = sqlContactInsertStatement.replace(",'null'",",NULL" );
			eurStatement.addBatch(sqlContactInsertStatement);
			
			try{
				eurStatement.executeBatch();
			}
			catch (Exception e) {
				log.info(e.getMessage());
				log.info("Insert statement that failed was one of the following: ");
				log.info(sqlEmailInsertStatement);
				log.info(sqlCSTMInsertStatement);
				log.info(sqlContactInsertStatement);
			}
			
		}
		log.info("Closing connection to privacy db");
		eurStatement.close();
		eurConn.close();

	}
	
	@Test(groups = { ""})
	public void Test_PrivacySVT1() throws SQLException{
		log.info("Opening connection to main db");
//		DB2Connection mainDB2 = new DB2Connection("jdbc:db2://svt4oltp01.rtp.raleigh.ibm.com:50000/saleconn", "sctid", "g0away45");
		DB2Connection mainDB2 = new DB2Connection("jdbc:db2://svt1oltp01.rtp.raleigh.ibm.com:50000/saleconn", "sctid", "jun13pwd");
		Connection conn;

		conn = mainDB2.connectToDb2();

		Statement statement = null;
		boolean resultSetIsEmpty = true;
		
		log.info("Generating SQL select statement");
		String sqlSelectStatement = "select con.ID, SALUTATION, FIRST_NAME, LAST_NAME "+
			"from sctid.CONTACTS con "+
			"left outer join sctid.contacts_cstm cstm on cstm.ID_C = con.ID "+
			"left outer join sctid.email_addr_bean_rel email on email.bean_id = con.ID AND email.deleted = 0 "+
			"where con.ID IN (select ID from sctid.contacts where FIRST_NAME like '%PRIVATE_EU_%' AND sctid.contacts.DELETED = 0)";
		// fetch first 1 row only

		log.info("Executing SQL select statement");
		statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(sqlSelectStatement);
		ArrayList<String[]> privacyList = new ArrayList<String[]>();

		log.info("Populating data structure with information from sql select");
		while (rs.next()) {
			String[] row = new String[5];
			resultSetIsEmpty = false;
			row[0] = rs.getString("ID").trim();
			row[1] = rs.getString("SALUTATION");
			row[2] = rs.getString("FIRST_NAME");
			row[3] = rs.getString("LAST_NAME");
			privacyList.add(row);
		}

		Assert.assertEquals(resultSetIsEmpty, false, "No results returned from the main DB, this means that there no privacy contact were found");
		
		log.info("Total number of contacts to be added " + privacyList.size());
		log.info("Closing connection to main db");
		statement.close();
		conn.close();
		
		log.info("Opening connection to privacy db");
	//	DB2Connection eurDB2 = new DB2Connection("jdbc:db2://svt4eudb2.rtp.raleigh.ibm.com:50000/saleeur", "sceid", "jul13pwd");
		DB2Connection eurDB2 = new DB2Connection("jdbc:db2://svt1eudb2.rtp.raleigh.ibm.com:50000/saleeur", "sceid", "jan14pwd");
		Connection eurConn;

		eurConn = eurDB2.connectToDb2();

		Statement eurStatement = null;
		
		log.info("Starting to populate privacy database");
		for (int i = 0; i < privacyList.size(); i=i+5) {
			eurStatement = eurConn.createStatement();
			String sqlContactInsertStatement = "insert into sctid.contacts_eu (ID, SALUTATION, FIRST_NAME, LAST_NAME, PHONE_HOME)"+
			" values ";
			
			for (int j = 0; j < 5; j++) {
				String row[] = privacyList.get(i+j);
				
				sqlContactInsertStatement += "('" + row[0]+"','DR.','FN"+row[0]+"','LN"+row[0]+"','88888888'),";	
			}
			
			sqlContactInsertStatement = sqlContactInsertStatement.substring(0, sqlContactInsertStatement.length()-1);
			sqlContactInsertStatement = sqlContactInsertStatement.replace(",'null'",",NULL" );
			eurStatement.addBatch(sqlContactInsertStatement);
			
			try{
				eurStatement.executeBatch();
			}
			catch (Exception e) {
				log.info(e.getMessage());
				log.info("Insert statement that failed was one of the following: ");
				log.info(sqlContactInsertStatement);
			}
			
		}
		log.info("Closing connection to privacy db");
		eurStatement.close();
		eurConn.close();

	}
	
	public void Test_EmptyDB() throws SQLException{
	log.info("Opening connection to privacy db");
	DB2Connection eurDB2 = new DB2Connection("jdbc:db2://svt1eudb2.rtp.raleigh.ibm.com:50000/saleeur", "sceid", "jan14pwd");
	Connection eurConn;

	eurConn = eurDB2.connectToDb2();

	Statement eurStatement = null;
	
	eurStatement = eurConn.createStatement();
	
	ResultSet rs = eurStatement.executeQuery("select count from sctid.contacts_eu");
	System.out.println(rs.getStatement());
	}
	
	
	String tim = "if(isEnumEqualTo (p1page.getEnum (\"ReasonEnum\") , \"Follow Up Call\") != isEnumEqualTo (p1page.getEnum (\"MethodEnum\") , \"Phone\")) return CreateValidationMessage (\"Contact : \"\" , \"\"If the contact reason is 'Followup' the method must be Phone\") else return CreateOKMessage () endif";
	
}


