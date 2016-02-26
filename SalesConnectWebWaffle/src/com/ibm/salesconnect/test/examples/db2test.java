/**
 * 
 */
package com.ibm.salesconnect.test.examples;

import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;

/**
 * @author timlehane
 * @date May 8, 2013
 */
public class db2test extends ProductBaseTest {
//	Logger log = LoggerFactory.getLogger(db2test.class);
//
//	@Test(groups = {"db2Test"})
//	public void Test_db2Test() {
//	log.info("Start of test method db2Test");
//	User db2User = commonUserAllocator.getGroupUser(GC.db2UserGroup,this);
//	User user1 = commonUserAllocator.getUser(this);
//	
//	try {
//	DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
//	//Temp hardocding of require client id
//	
//	String ccmsID_In = "S00725DSTT";
//	Client testClient = db2.retrieveClient(ccmsID_In, user1.getEmail(),testConfig.getParameter(GC.testPhase));
//	System.out.println(testClient.sClientName);
//	
//	//Temp hardcoding of required oppty id
//	String oppty = "OK-N936DUW";
//	Opportunity testOppty = db2.retrieveOpportunity(oppty, user1.getEmail(),testConfig.getParameter(GC.testPhase));
//	System.out.println(testOppty.sOpptName);
//	
//	Contact testcon = db2.retrieveDBContactFromOpportunity("OK-N936DUW");
//	System.out.println(testcon.sFirstName);
//	
//	} catch (SQLException e) {
//		e.printStackTrace();
//	}
//	commonUserAllocator.checkInAllUsersWithToken(this);
//	log.info("End of test method db2Test");
//	}
}
