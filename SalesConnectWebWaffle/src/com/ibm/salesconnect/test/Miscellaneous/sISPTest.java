/**
 * 
 */
package com.ibm.salesconnect.test.Miscellaneous;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.DB2Connection;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.ClientDetailPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.objects.Client;


/**
 * <strong>Description:</strong>
 * <br/><br/>
 * Test to validate that ISP client tabs load successfully
 * <br/><br/>
 * 
 * @author Adrian Strock
 */
public class sISPTest extends
		ProductBaseTest {
	Logger log = LoggerFactory.getLogger(sISPTest.class);
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Open browser and log in</li>
	 * <li>Navigate to ISP Client (client must exist on ISP server)</li>
	 * <li>Click each client ISP tab (Overview, Predictive Buying Analytics, Intelligence, Complains and PMRs, Install Base, References, Client IBM Spend, Client Touch Points)</li>
	 * <li>Confirm each client ISP tab loads without error</li>
	 * </ol>
	 */
	@Test(groups = {"BVT1"})
	public void Test_sISPTest() throws SQLException, InterruptedException {
		log.info("Start of test setup for sISPTest");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		User db2User = commonUserAllocator.getGroupUser(GC.db2UserGroup,this);
		DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
		
		PoolClient poolClient = commonClientAllocator.getGroupClient("ISP", this);
		Client client = db2.retrieveClient(poolClient, user1.getEmail(), testConfig.getParameter(GC.testPhase));
		
		client.sSearchIn= GC.showingInClientID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForClients;
		client.sClientID=poolClient.getCCMS_ID();
		client.sClientName=poolClient.getClientName(baseURL, user1);
		
		log.info("End of test setup for sISPTest");
		log.info("Start of test for sISPTest");
		
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		
		log.info("Open Client detail page");
		ViewClientPage viewClientPage = dashboard.openViewClient();
		viewClientPage.searchForClient(client);
		ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);
		
		clientDetailPage.clientOverview();
			
		clientDetailPage.predictiveBuyingAnalytics();
		
		clientDetailPage.intelligence();
		
		clientDetailPage.complaintsAndPMRs();
				
		clientDetailPage.installBase();
		
		clientDetailPage.refrences();

		clientDetailPage.clientIBMSpend();

		clientDetailPage.clientTouchPoints();		
		
		commonClientAllocator.checkInAllGroupClientWithToken(GC.DC, this);
		
		log.info("End of test method Test_sISPTest");
	}
}
