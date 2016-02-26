/**
 * 
 */
package com.ibm.salesconnect.test.examples;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.PoolHandling.Opportunity.PoolOpportunity;
import com.ibm.salesconnect.common.DB2Connection;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.objects.Opportunity;

/**
 * @author timlehane
 * @date May 10, 2013
 */
public class poolFunctionalityTest extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(poolFunctionalityTest.class);

	@Test(groups = {"poolFunctionalityTest"})
	public void Test_poolFunctionalityTest() {
	log.info("Start of test method poolFunctionalityTest");
	
	//Get the db2_user which is specified in users.csv and default_users.properties
	User db2User = commonUserAllocator.getGroupUser(GC.db2UserGroup,this);
//	System.out.println(db2User.getDisplayName());
//	
//	//Get a standard user from the pool
	User user1 = commonUserAllocator.getUser(this);
//	System.out.println(user1.getDisplayName());
//	
//	//Check in all of the standard users
//	commonUserAllocator.checkInAllUsersWithToken(this);
//	
//	//check in the db2_user user
//	commonUserAllocator.checkInAllGroupUsersWithToken(GC.db2UserGroup,this);
//	
//	//Get standard clients from the pool
//	//Note: this only gets the id and name of the client to populate the client object you need to 
//	//retrieve it from the database
//	PoolClient client = commonClientAllocator.getClient(this);
//	PoolClient client1 = commonClientAllocator.getClient(this);
//	System.out.println(client.getClientName());
//	System.out.println(client1.getClientName());
	
	//Get the testRegression client which is specified in the clients.csv and the default_clients.properties 
	PoolClient testRegression = commonClientAllocator.getGroupClient("DC",this);

	//PoolClient testSpecial = commonClientAllocator.getGroupClient("DC", this);
	System.out.println("testRegression ClientName: " + testRegression.getClientName(testConfig.getBrowserURL(), user1));
	
	//Check in all of the standard clients - to be done at the end of all tests which get one from the pool
	commonClientAllocator.checkInAllclientsWithToken(this);
	
	//Check in the testRegression client
	commonClientAllocator.checkInAllGroupClientWithToken("DC", this);
	commonClientAllocator.checkInAllGroupClientWithToken("DC", this);
	
	//Get standard opportunities from the pool
	//Note: this only gets the id and name of the opportunity to populate the opportunity object you need to 
	//retrieve it from the database
	PoolOpportunity poolOppty = commonOpportunityAllocator.getGroupOpportunity("3",this);
//	PoolOpportunity opportunity1 = commonOpportunityAllocator.getOpportunity(this);
	DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
	Opportunity opportunityIn = null;
	try {
		opportunityIn = db2.retrieveOpportunity(poolOppty, user1.getEmail(),testConfig.getParameter(GC.testPhase));
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	
	//Get the testRegression client which is specified in the clients.csv and the default_clients.properties 
	PoolOpportunity testRegressionO = commonOpportunityAllocator.getGroupOpportunity("2",this);
	PoolOpportunity testSpecialO = commonOpportunityAllocator.getGroupOpportunity("1",this);

	
	//Check in all of the standard clients - to be done at the end of all tests which get one from the pool
	commonOpportunityAllocator.checkInAllopportunitiesWithToken(this);
	
	//Check in the testRegression client
	commonOpportunityAllocator.checkInAllGroupOpportunityWithToken("2", this);
	commonOpportunityAllocator.checkInAllGroupOpportunityWithToken("1",this);
	
	log.info("End of test method poolFunctionalityTest");
	}
}
