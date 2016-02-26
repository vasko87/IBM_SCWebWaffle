package com.ibm.salesconnect.test.Level0;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.ClientDetailPage;
import com.ibm.salesconnect.model.standard.Client.CreateClientPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.objects.Client;


	/**
	 * <strong>Description:</strong>
	 * <br/><br/>
	 * Test to validate the create and read functionality of the Clients module
	 * (Update and delete is not available)
	 * <br/><br/>
	 * 
	 * @author 
	 * Tim Lehane
	 * 
	 */
	public class AT_Client extends ProductBaseTest {
		Logger log = LoggerFactory.getLogger(AT_Client.class);

		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Open browser and log in</li>
		 * <li>Create Client</li>
		 * <li>Search for created Client</li>
		 * </ol>
		 */
		@Test(groups = { "Level1","AT_Sugar","BVT", "BVT0"})
		public void Test_AT_Client(){
			log.info("Start of test method Test_AT_Client");
			User user1 = commonUserAllocator.getUser(this);

			Client client = new Client();

			client.populate();

			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);

			log.info("Creating Client");
			CreateClientPage createClientPage = dashboard.openCreateClient();

			createClientPage.enterClientInfo(client);

			createClientPage.saveClient();

			log.info("Searching for created client");
			ViewClientPage viewClientPage = dashboard.openViewClient();
			viewClientPage.searchForClient(client);
			ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);

			Assert.assertEquals(clientDetailPage.getdisplayedClientName(),client.sClientName,"Incorrect Client Detail Page was opened");

			log.info("End of test method Test_AT_Client");
		}

	}