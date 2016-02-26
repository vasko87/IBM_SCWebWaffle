package com.ibm.salesconnect.test.Level1;

import java.sql.SQLException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Contact.ContactDetailPage;
import com.ibm.salesconnect.model.standard.Contact.ViewContactPage;


/**
 * <strong>Description:</strong>
 * <br/><br/>
 * Test to validate the Create, Read, Update-Convert to Favorite and Search for the Favorite Contact functionality of the Contacts module
 * <br/><br/>
 * 
 * @author 
 * Veena Hurukadli
 * 
 */
public class AT_FavoriteContact extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(AT_Sugar.class);
	int rand = new Random().nextInt(100000);
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Open browser and log in</li>
	 * <li>Create Contact</li>
	 * <li>Search for created Contact</li>
	 * <li>Open Contact detail page</li>
	 * <li>Update Contact-Add to Favorites</li>
	 * <li>Search for the Favorite contact</li>
	 * </ol>
	 */
	@Test(groups = { "Level1","AT_Sugar","BVT","BVT1"})
	public void Test_AT_FavoriteContact() throws SQLException{
		log.info("Start of test method Test_AT_Contact");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		
		log.info("Getting client Via API");
		PoolClient poolClient = commonClientAllocator.getGroupClient("SC");
		String clientID = poolClient.getCCMS_ID();
			
		log.info("Creating contact Via API");
		String contactFirst = "contactFirst"+rand;
		String contactLast = "contactLast"+rand;
		String contactID = "22SC-" + rand;
		SugarAPI sugarAPI = new SugarAPI();
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), contactFirst, contactLast);

		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		
		log.info("Searching for API created contact");
		ViewContactPage viewContactPage = dashboard.openViewContact();
		viewContactPage.searchForContactName(contactFirst);


		ContactDetailPage contactDetailPage = viewContactPage.selectTextResult(contactFirst);
		contactDetailPage.addContactToMyFavorites();
		
		log.info("Searching for API created contact");
		viewContactPage = dashboard.openViewContact();
		viewContactPage.searchForFavoriteContactName(contactFirst);

		contactDetailPage = viewContactPage.selectTextResult(contactFirst);

		Assert.assertEquals(contactDetailPage.getdisplayedContactName(),contactFirst +" "+ contactLast, "Incorrect contact detail page displayed");

		commonClientAllocator.checkInAllGroupClients("SC");
		log.info("End of test method Test_AT_FavoriteContact");
	}

}
