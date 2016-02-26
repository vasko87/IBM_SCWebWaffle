package com.ibm.salesconnect.test.Level1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.ContactAPITemplate;
import com.ibm.salesconnect.API.ContactRestAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.GlobalSearchResultsPage;
import com.ibm.salesconnect.model.standard.Contact.CreateContactPage;
import com.ibm.salesconnect.model.standard.Contact.ViewContactPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Contact;

/**
 * <strong>Description:</strong>
 * <br/><br/>
 * Test to validate the Create, Read and Global Search for Contact functionality of the Global Search module
 * <br/><br/>
 * 
 * @author 
 * Christeena J Prabhu
 * 
 */
public class s1950VerifyContactGlobalSearch extends ProductBaseTest {

	Logger log = LoggerFactory.getLogger(s1950VerifyContactGlobalSearch.class);

	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Open browser and log in</li>
	 * <li>Create Contact</li>
	 * <li>Search for created Contact</li>
	 * <li>Open Contact detail page</li>
	 * <li>Global Search for Contact</li>
	 * <Li>Verify Contact under Global Results</Li>
	 * </ol>
	 */
	
	@Test(groups = {"Search","BVT", "BVT1"})
	public void Test_s1950VerifyContactGlobalSearch() {
		log.info("Start of test method s1950VerifyContactGlobalSearch");
		log.info("Getting user");
		User user1 = commonUserAllocator.getUser(this);
			
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);
			
			log.info("Creating Contact via api");
			String contactName = ContactRestAPI.createContactHelperGetName(user1, log, testConfig.getBrowserURL());

			
			log.info("Perform Global Search and Verify Results");
		try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Assert.assertTrue(false);
			}
		
		GlobalSearchResultsPage globalsearchresultspage = dashboard.performGlobalSearch(contactName);
		
		//if(globalsearchresultspage.isPageLoaded()){log.info("Search Results Page Loaded");}
		Assert.assertTrue(globalsearchresultspage.isPageLoaded(),"Global Results Page not loaded");
		//if(globalsearchresultspage.verifySearchResults(sContactName)){log.info("Search Results Verified and Item Appears in Search Results List");}
		
		// Note: IsTestPresent in call below does not work well with spaces, so use only 1 word & not full name
		Assert.assertTrue(globalsearchresultspage.verifySearchResults(contactName), "Contact Item does not appear in Search Results List");														

		log.info("End of test method s1950VerifyContactGlobalSearch");
	}

}
