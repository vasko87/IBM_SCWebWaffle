/**
 * 
 */
package com.ibm.salesconnect.test.SugarSOAPAPI;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;

/**
 * @author timlehane
 * @date Mar 12, 2014
 */
public class OpptySoapAPITests extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(OpptySoapAPITests.class);
	private String opptyID = null;
	private User user1 = null;

	/**
	 * Test to create oppty via the Soap API
	 */
	@Test(groups = {"SugarSoapAPI"})
	public void Test_CreateOppty() {
		
		log.info("Start test method Test_CreateOppty.");
		log.info("Getting user.");	
		user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		String URL = testConfig.getBrowserURL();
		
		log.info("Getting client.");
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		
		int rand = new Random().nextInt(99999);
		String contactID = "22SC-" + rand;
		opptyID = "33SC-" + rand;
		
		SugarAPI sugarAPI = new SugarAPI();
		try{
		log.info("Creating Contact.");
		sugarAPI.createContact(URL, contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst_"+rand, "ContactLast_"+rand);
		
		log.info("Creating Opportunity.");
		sugarAPI.createOpptySOAP(URL, opptyID, "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		
		log.info("End test method Test_CreateOppty.");
		}
		catch(Exception e){
			log.info(e.getMessage());
			commonUserAllocator.checkInAllGroupUsersWithToken(GC.busAdminGroup, this);
			sugarAPI.deleteOpptySOAP(URL, opptyID, user1.getEmail(), user1.getPassword());
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * Test to check for oppty presence via the Soap API
	 * 
	 */
	@Test(groups = {"SugarSoapAPI"},dependsOnMethods = {"Test_CreateOppty"})
	public void Test_CheckOppty() {
		log.info("Start test method Test_CheckOppty.");
		log.info("Getting user.");	

		String URL = testConfig.getBrowserURL();
		
		SugarAPI sugarAPI = new SugarAPI();

		try{
		log.info("Checking that Opportunity was created.");
		Assert.assertEquals(sugarAPI.checkOpptySOAP(URL, opptyID, user1.getEmail(), user1.getPassword()),"100", "Oppty check was not successful"); 
		}
		catch(Exception e){
			log.info(e.getMessage());
			commonUserAllocator.checkInAllGroupUsersWithToken(GC.busAdminGroup, this);
		}
		log.info("End test method Test_CheckOppty.");
	}
	
	/**
	 * Test to delete the created oppty
	 */
	@Test(groups = {"SugarSOAPAPI"},dependsOnMethods = {"Test_CheckOppty"})
	public void Test_DeleteOppty(){
		log.info("Start test method Test_DeleteOppty");
		log.info("Getting user.");	

		String URL = testConfig.getBrowserURL();
		
		SugarAPI sugarAPI = new SugarAPI();
		try{
		log.info("Deleting Oppotunity");
		Assert.assertEquals(sugarAPI.deleteOpptySOAP(URL, opptyID, user1.getEmail(), user1.getPassword()), "100");
		}
		finally{
			commonUserAllocator.checkInAllGroupUsersWithToken(GC.busAdminGroup, this);
		}
		log.info("End test method Test_DeleteOppty");
	}

}
