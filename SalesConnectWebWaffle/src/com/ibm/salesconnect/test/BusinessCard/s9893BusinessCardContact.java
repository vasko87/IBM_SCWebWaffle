/**
 * 
 */
package com.ibm.salesconnect.test.BusinessCard;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.DB2Connection;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Contact;
import com.ibm.salesconnect.objects.Opportunity;
import com.ibm.salesconnect.objects.RevenueItem;
import com.ibm.salesconnect.objects.Task;

/**
 * @author timlehane
 * @date Jun 20, 2013
 */
public class s9893BusinessCardContact extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(s9892BusinessCardOpportunities.class);

	@Test(groups = { ""})
	public void Test_s9893BusinessCardContact() {
	log.info("Start test method Test_s9893BusinessCardContact");
	
	User user4 = commonUserAllocator.getUser(this);
	log.info("Logging in");
	//Dashboard dashboard = launchWithLogin(user4);
	
	log.info("Creating Contact");
	//CreateContactPage createContactPage = dashboard.openCreateContact();
	
	log.info("Getting users");
	User user1 = commonUserAllocator.getUser(this);
	User user2 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
	User db2User = commonUserAllocator.getGroupUser(GC.db2UserGroup,this);
	
	Contact contact = new Contact();
	Client client = null;
	Task task = new Task();
	task.populate();
	Opportunity oppt = new Opportunity();
	oppt.populate();
	RevenueItem rli = new RevenueItem();
	rli.populate();

	PoolClient poolClient = commonClientAllocator.getGroupClient("SC");
	DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());

	try {
		client = db2.retrieveClient(poolClient, user1.getEmail(),testConfig.getParameter(GC.testPhase));
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	client.sSearchIn= GC.showingInSiteID;
	client.sSearchFor= GC.searchForAll;
	client.sSearchShowing=GC.showingForAll;
	
	contact.populate();

//	log.info("Logging in");
//	Dashboard dashboard = launchWithLogin(user1);
//	
//	log.info("Creating Contact");
//	CreateContactPage createContactPage = dashboard.openCreateContact();
//	createContactPage.enterContactInfo(contact, client);
//	ContactDetailPage contactDetailPage = createContactPage.saveContact();
//	
//	log.info("Searching for created contact");
//	ViewContactPage viewContactPage = dashboard.openViewContact();
//	viewContactPage.searchForContact(contact);
//
//	viewContactPage.selectResult(contact);
	
//	ActivitiesSubpanel activitiesSubpanel = contactDetailPage.openCreateActivitiesSubPanel();
//	activitiesSubpanel.openCreateTaskForm();
//	activitiesSubpanel.enterTaskInfo(task);
//	activitiesSubpanel.saveTask();
	//activitiesSubpanel.verifyBusinessCard(user1);
	
	//activitiesSubpanel.completeTask(task.sName);
	//Assert.assertTrue(contactDetailPage.isPageLoaded(), "Contact Detail page has not loaded within 60 seconds");
	
	//ActivitiesHistorySubpanel activitiesHistorySubpanel = contactDetailPage.openActivitiesHistorySubpanel();
	//activitiesHistorySubpanel.verifyBusinessCard(user1);
	
	//SetAssessmentSubpanel setAssessmentSubpanel = contactDetailPage.openSetAssessmentSubpanel();
	//setAssessmentSubpanel.verifyBusinessCard(user1);
	
//	OpportunitySubpanel opportunitySubpanel = contactDetailPage.openOpportunitySubpanel();
//	opportunitySubpanel.openCreateOpportunityForm();
//	opportunitySubpanel.enterOpportunityInfo(oppt, rli);
//	opportunitySubpanel.saveOpportunity();
	}
}
