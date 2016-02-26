/**
 * 
 */
package com.ibm.salesconnect.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.PoolHandling.Opportunity.PoolOpportunity;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Contact;
import com.ibm.salesconnect.objects.Opportunity;


/**
 * @author timlehane
 * @date May 8, 2013
 */
public class DB2Connection extends ProductBaseTest{
	Logger log = LoggerFactory.getLogger(DB2Connection.class);

	public String dbURL = "";
	public String dbUserName="";
	public String dbPwd="";
	
	/**
	 * Constructor for a DB2Connection
	 * @param db2URL
	 * @param db2UserName
	 * @param db2Pwd
	 */
	public DB2Connection(String db2URL,String db2UserName, String db2Pwd) {
		dbURL=db2URL;
		dbUserName=db2UserName;
		dbPwd=db2Pwd;
	}
	
	/**
	 * Creates a database connection to the specified database
	 * @param dbURL
	 * @param dbUserName
	 * @param dbPassword
	 * @return
	 * @throws SQLException
	 */
	public Connection connectToDb2() throws SQLException {

		Connection conn = null;
		
		try {
			if (dbURL.contains("jdbc:mysql")) 
			{
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				conn = DriverManager.getConnection(dbURL, dbUserName, dbPwd);
			} 
			else if (dbURL.contains("jdbc:db2")) 
			{
				 Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
				 /* --> Use this Driver to suit older DB2 versions --> Class.forName("COM.ibm.db2os390.sqlj.jdbc.DB2SQLJDriver").newInstance();*/
				 conn = DriverManager.getConnection(dbURL, dbUserName, dbPwd);
			}

		} catch (Exception ex) {
			System.out.print(ex);
			System.err.println("Could not load DB2 driver \n");
		}	
		return conn;
	}
	
	/** 	
	* Retrieves client data from database using a reference value 
	* and creates a client object using this data.
	* @param String CCMS_Id
	* @param String user email address
	* @return client object with values set
	*/
	public Client retrieveClient(PoolClient clientIn, String userEmail, String testPhase) throws SQLException
	{	
		//create client object
		Client client = new Client();
		if(testPhase.equalsIgnoreCase(GC.nodbaccess)){
			client=populateClient(clientIn, client);
			return client;
		}
		
		boolean resultSetIsEmpty = true;
		//create database connection
		Connection conn = connectToDb2();
		Statement statement = null;

		String query = "SELECT CCMS_ID,CCMS_LEVEL,NAME, " +
		"PHONE_FAX,PHONE_OFFICE,WEBSITE, " +
		"BILLING_ADDRESS_STREET, BILLING_ADDRESS_CITY, BILLING_ADDRESS_STATE, " +
		"BILLING_ADDRESS_POSTALCODE, BILLING_ADDRESS_COUNTY, BILLING_ADDRESS_COUNTRY, " +
		"SHIPPING_ADDRESS_STREET, SHIPPING_ADDRESS_CITY, SHIPPING_ADDRESS_STATE, " +
		"SHIPPING_ADDRESS_POSTALCODE, SHIPPING_ADDRESS_COUNTY, SHIPPING_ADDRESS_COUNTRY " +
		"FROM SCTID.ACCOUNTS Accounts JOIN SCTID.ACCOUNTS_USERS Accounts_Users " +
		"ON Accounts.ID = Accounts_Users.ACCOUNT_ID " +
		"WHERE CCMS_ID = '"+client.sClientID+"' " +
		"AND Accounts_Users.USER_ID = (Select Id from SCTID.USERS where USER_NAME = '"+userEmail+"') " +
		"AND Accounts_Users.DELETED = 0 AND Accounts.DELETED = 0";
		
		log.debug("Client DB Query: " + query);
		
		statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(query);

		while (rs.next()) {
			resultSetIsEmpty = false;
	     	//set client variables
	    	client.sClientID = rs.getString("CCMS_ID").trim();
	    	client.sCCMS_Level = rs.getString("CCMS_Level");
			client.sClientName = rs.getString("NAME").trim();
			client.sPhoneFax = rs.getString("PHONE_FAX");
			client.sPhoneNumber = rs.getString("PHONE_OFFICE");
			client.sWebsite = rs.getString("WEBSITE");
	     	//set Physical Address variables
			client.sPhysicalStreet = rs.getString("BILLING_ADDRESS_STREET");
			client.sPhysicalCity= rs.getString("BILLING_ADDRESS_CITY");
			client.sPhysicalState = rs.getString("BILLING_ADDRESS_STATE");	
			client.sPhysicalPostal = rs.getString("BILLING_ADDRESS_POSTALCODE");
			client.sPhysicalCounty = rs.getString("BILLING_ADDRESS_COUNTY");
			client.sPhysicalCountry = rs.getString("BILLING_ADDRESS_COUNTRY");
	     	//set Mail Address variables (same as Physical address)
			client.sMailStreet = rs.getString("SHIPPING_ADDRESS_STREET");
			client.sMailCity= rs.getString("SHIPPING_ADDRESS_CITY");
			client.sMailState = rs.getString("SHIPPING_ADDRESS_STATE");	
			client.sMailPostal = rs.getString("SHIPPING_ADDRESS_POSTALCODE");
			client.sMailCounty = rs.getString("SHIPPING_ADDRESS_COUNTY");
			client.sMailCountry = rs.getString("SHIPPING_ADDRESS_COUNTRY");	
			log.debug("Client returned :" + client.sClientName);

		}
		
		Assert.assertEquals(resultSetIsEmpty, false, "No clients returned from database!\n" +
				"Either client has been deleted or does not exist\n" +
				"Or the specified user is not on the client team.");
		
		statement.close();
		conn.close();
		return client;
	}
	
	/** 	
	* Retrieves a specified opportunity from the database
	* @param String opportunity ID
	* @param String user email address
	* @return Opportunity object with values set
	*/
	public Opportunity retrieveOpportunity(PoolOpportunity opptyIn, String userEmail, String testPhase) throws SQLException
	{	
		Opportunity opportunityIn = new Opportunity();
		if(testPhase.equalsIgnoreCase(GC.nodbaccess)){
			opportunityIn=populateOpportunity(opptyIn, opportunityIn);
			return opportunityIn;
		}
		
		//create database connection
		boolean resultSetIsEmpty = true;
		
		Connection conn = connectToDb2();
		Statement statement = null;
		String query = "Select opp.NAME, opp.DATE_CLOSED, opp.DESCRIPTION, con.FIRST_NAME, con.LAST_NAME, opp.LEAD_SOURCE, acc.NAME AS CLIENT_NAME, acc.CCMS_ID "+
		"from SCTID.ACCOUNTS_OPPORTUNITIES accop "+
		"inner join SCTID.ACCOUNTS acc on accop.ACCOUNT_ID = acc.id "+
		"join SCTID.OPPORTUNITIES opp on accop.OPPORTUNITY_ID = opp.id "+
		"join SCTID.OPPORTUNITIES_USERS oh ON opp.NAME = oh.OPPORTUNITY_ID " +
		"join SCTID.OPPORTUNITIES_CONTACTS oppCon ON opp.NAME = oppCon.OPPORTUNITY_ID "+
		"join SCTID.CONTACTS con ON oppCon.CONTACT_ID = con.ID "+
		"where opp.NAME LIKE '%"+opptyIn.getOpportunityNumber()+"%'"+
		"			AND acc.NAME NOT LIKE '%''%' "+                
		"			AND oh.USER_ID = (Select Id from SCTID.USERS where USER_NAME = '"+userEmail+"') "	+	
		"			AND accop.DELETED = '0' "+
		"			GROUP BY opp.NAME, opp.DATE_CLOSED, opp.DESCRIPTION, con.FIRST_NAME, con.LAST_NAME, opp.LEAD_SOURCE, acc.NAME, acc.CCMS_ID "+
		"		ORDER BY RAND() FETCH FIRST 1 ROWS ONLY ";

		log.info("Opportunity DB Query: " + query);
		
		statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(query);

		while (rs.next()) {
			resultSetIsEmpty = false;
			opportunityIn.sOpptNumber = rs.getString("NAME");
			opportunityIn.sCloseDate = rs.getString("DATE_CLOSED");
			opportunityIn.sOpptDesc = rs.getString("DESCRIPTION");
			opportunityIn.sPrimaryContact = rs.getString("FIRST_NAME") + " " + rs.getString("LAST_NAME");
			opportunityIn.sLeadSource = rs.getString("LEAD_SOURCE");
			opportunityIn.sAcctName = rs.getString("CLIENT_NAME");
			opportunityIn.sAccID = rs.getString("CCMS_ID");
			log.info("Opportunity returned :" + opportunityIn.sOpptNumber);
		}
			
		Assert.assertEquals(resultSetIsEmpty, false, "No opportunities returned from database!");
		
		statement.close();
		conn.close();
		return opportunityIn;
	}
	

	/**
	 * Retrieve the contact for an opportunity
	 * @param opportunityID
	 * @return contact
	 * @throws SQLException
	 */
	public Contact retrieveDBContactFromOpportunity(String opportunityID) throws SQLException
	{			
		//create database connection
		boolean resultSetIsEmpty = true;
		Connection conn = connectToDb2();
		Statement statement = null;
		Contact contact = new Contact();
		
		//query to return contacts
		String query = "select * "+
				"from sctid.contacts con "+
				"join sctid.opportunities_contacts oppcon on con.id = oppcon.contact_id "+
				"join sctid.opportunities opp on oppcon.opportunity_id = opp.id "+
				"where opp.id='" + opportunityID + "' "+
				"FETCH FIRST 1 ROWS ONLY";
		
		log.info("Contact DB Query: "+query);

		statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(query);
		
		while (rs.next()) {
			resultSetIsEmpty = false;
	     	//set contact variables
			contact.sFirstName = rs.getString("FIRST_NAME");
			contact.sLastName = rs.getString("LAST_NAME");
			contact.sOfficePhone = rs.getString("PHONE_WORK");
			contact.sMobile = rs.getString("PHONE_MOBILE");
			//contact.sEmail0 = rs.getString("");
			contact.sCountry = rs.getString("PRIMARY_ADDRESS_COUNTRY");
			log.info("Contact returned :" + contact.sFirstName + " " + contact.sLastName);
		}
			
		Assert.assertEquals(resultSetIsEmpty, false, "No contacts returned from database!");
		
		statement.close();
		conn.close();
		
		return contact;
	}
	
	/** 	
	* Retrieves GBClient CCMS_IDs from database limited to count 
	* @author kvnlau
	* @param int count of the number to return
	* @return String array of GBClient's CCMS_IDs
	*/
	public String[] retrieveGbClientIds(int count) throws SQLException
	{	
		String[] gbClientList = new String[count];
		
		//create database connection
		Connection conn = connectToDb2();
		Statement statement = null;

		String query = "select * from sctid.ibm_accountscontainers where ccms_level = 'GB' " + 
						"and CCMS_ID NOT IN (select ACCOUNT_ID from sctid.IBM_ACCOUNTS_COMMUNITIES" + 
						" where ACCOUNT_ID like 'GB%')";
		
		log.debug("GBClient Query: " + query);
		
		statement = conn.createStatement();
		statement.setMaxRows(count); // limit number of results
		ResultSet rs = statement.executeQuery(query);
		
		// process correct number of results 
		int resultsCounter=0;
		while (rs.next()) {
			//add GU client to String array
			gbClientList[resultsCounter]= rs.getString("CCMS_ID").trim();
			log.debug("gbClientList[" + resultsCounter + "]=" + gbClientList[resultsCounter]);
			
			resultsCounter = resultsCounter + 1;
		}
		
		// check if request number of results were found
		if (resultsCounter <= 0) {
			Assert.assertFalse(rs.getFetchSize()<=0, "No GBClients returned from database!");
		}
		if (resultsCounter < count) {
			Assert.assertFalse(rs.getFetchSize()<count, "Insuffient GBClients (i.e. " + 
								rs.getFetchSize() + ") returned from database when expect " + count);
		}
		
		statement.close();
		conn.close();
		return gbClientList;
	}
	
	/** 	
	* Retrieves GUClient CCMS_IDs from database limited to count 
	* @author kvnlau
	* @param count, number of results to return
	* @return String array of GUClient's CCMS_IDs
	*/
	public String[] retrieveGuClientIds(int count) throws SQLException
	{	
		String[] guClientList = new String[count];
		
		//create database connection
		Connection conn = connectToDb2();
		Statement statement = null;

		//String query = "select * from sctid.ibm_accountscontainers where ccms_level = 'GU' and CCMS_ID NOT IN (select ACCOUNT_ID from sctid.IBM_ACCOUNTS_COMMUNITIES where ACCOUNT_ID like 'GU%')";
		String query = "select * from sctid.ibm_accountscontainers where ccms_level = 'GU' " + 
				"and CCMS_ID NOT IN (select ACCOUNT_ID from sctid.IBM_ACCOUNTS_COMMUNITIES" + 
				" where ACCOUNT_ID like 'GU%')";
		
		
		log.debug("GUClient Query: " + query);
		
		statement = conn.createStatement();
		statement.setMaxRows(count); // limit number of results
		ResultSet rs = statement.executeQuery(query);
		
		// process correct number of results 
		int resultsCounter=0;
		while (rs.next()) {
			//add GU client to String array
			guClientList[resultsCounter]= rs.getString("CCMS_ID").trim();
			log.debug("guClientList[" + resultsCounter + "]=" + guClientList[resultsCounter]);
			
			resultsCounter = resultsCounter + 1;
		}
		
		// check if request number of results were found
		if (resultsCounter <= 0) {
			Assert.assertFalse(rs.getFetchSize()<=0, "No GUClients returned from database!");
		}
		if (resultsCounter < count) {
			Assert.assertFalse(rs.getFetchSize()<count, "Insuffient GUClients (i.e. " + 
								rs.getFetchSize() + ") returned from database when expect " + count);
		}
		
		statement.close();
		conn.close();
		return guClientList;
	}
	
	@Deprecated
	Client populateClient(PoolClient clientIn, Client client){
		if(clientIn.getAttribute("group").equalsIgnoreCase(GC.SC)){
			client.sSiteID=clientIn.getCCMS_ID();
		}
		client.sClientID = clientIn.getCCMS_ID();
		//client.sClientName=clientIn.getClientName();
		client.sCCMS_Level=clientIn.getAttribute("group");
		
		return client;
	}
	
	Opportunity populateOpportunity(PoolOpportunity opptyIn, Opportunity opportunity){
		opportunity.sOpptNumber = opptyIn.getOpportunityNumber();
		/*
		 * Commenting out as the values do not seem to be used in any tests
		 */
		/*if (opptyID.equals("F6-JRJGGMO")) {
			opportunity.sOpptNumber = opptyID;
			opportunity.sCloseDate = "2013-08-25";
			opportunity.sOpptDesc = "adsf";
			opportunity.sPrimaryContact = "Andrew Butler";
			opportunity.sLeadSource = "RLPL";
			opportunity.sAcctName = "COMPUTACENTER";
			opportunity.sAccID = "S003NCZTHA";
		} 
		else if (opptyID.equals("V0-FCJ50ZV")) {
			opportunity.sOpptNumber = opptyID;
			opportunity.sCloseDate = "2013-09-20";
			opportunity.sOpptDesc = "asdf";
			opportunity.sPrimaryContact = "Daniel Bautista";
			opportunity.sLeadSource = "RLPL";
			opportunity.sAcctName = "Minera Cuadra";
			opportunity.sAccID = "S0072AG2XD";
		}
		else if (opptyID.equals("T5-3RVCS2H")){
			opportunity.sOpptNumber = opptyID;
			opportunity.sCloseDate = "2013-09-20";
			opportunity.sOpptDesc = "asdf";
			opportunity.sPrimaryContact = "Daniel Bautista";
			opportunity.sLeadSource = "RLPL";
			opportunity.sAcctName = "Minera Cuadra";
			opportunity.sAccID = "S0072AG2XD";
		}
		else if (opptyID.equals("OB-COV8GPX")){
			opportunity.sOpptNumber = opptyID;
			opportunity.sCloseDate = "2013-08-10";
			opportunity.sOpptDesc = "asdf";
			opportunity.sPrimaryContact = "Daniel Bautista";
			opportunity.sLeadSource = "RLPL";
			opportunity.sAcctName = "Minera Cuadra";
			opportunity.sAccID = "S0072AG2XD";
		}
		else {
			Assert.assertTrue(false, "No opportunity info found");
		}*/
		
		return opportunity;
	}

}
