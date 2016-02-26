package com.ibm.salesconnect.test.BusinessCard;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.ActivitiesSubpanel;
import com.ibm.salesconnect.model.partials.ConnectionsBusinessCard;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Contact.ContactDetailPage;
import com.ibm.salesconnect.model.standard.Contact.ViewContactPage;
import com.ibm.salesconnect.model.standard.Task.TaskDetailPage;
import com.ibm.salesconnect.model.standard.Task.ViewTaskPage;
import com.ibm.salesconnect.objects.Contact;
import com.ibm.salesconnect.objects.Task;

/*
 * Tests functionality of business cards within Connections
 * 
 * Tyler Clayton
 * 6-27-2013
 * 
 * UNSURE OF WHAT SHOULD BE DONE HERE
 */
@Test(groups = { "BusinessCard",""})
public class s9897BusinessCardTasks extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(s9897BusinessCardTasks.class);
	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String clientID = null;
	SugarAPI sugarAPI = new SugarAPI();
	Contact contact = new Contact();
	
	@Test
	public void Test_s9897BusinessCardTasks() {
		log.info("Start of test method s9897BusinessCardTasks");
		User user1 = commonUserAllocator.getUser(this);
		clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		contact.sFirstName = "ContactFirst"+GC.sUniqueNum;
		contact.sLastName = "ContactLast"+GC.sUniqueNum;
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), contact.sFirstName, contact.sLastName);
		
		
			//Set parameters for task
			Task task = new Task();
			task.populate();
			task.sName = "Contact Task " + GC.sUniqueNum;
			task.sStatus = GC.gsComplete;
			
			log.info("Login");
			Dashboard dashboard = launchWithLogin(user1);
					
			log.info("Search for created contact");
			ViewContactPage viewContactPage = dashboard.openViewContact();
			viewContactPage.searchForContact(contact);
			
			log.info("Select contact");
			ContactDetailPage contactDetailPage = viewContactPage.selectResult(contact);
			contactDetailPage.isPageLoaded();
			exec.executeScript("window.scrollBy(0,150)", new String());
			
			log.info("Open activity subpanel and create task");
			ActivitiesSubpanel activitiesSubpanel = contactDetailPage.openCreateActivitiesSubPanel();
			exec.executeScript("window.scrollBy(0,150)", new String());
			activitiesSubpanel.openCreateTaskForm();
			activitiesSubpanel.enterTaskInfo(task);
			activitiesSubpanel.saveTask();
				
			log.info("Go to created task and ensure Business Card is viewable");
			activitiesSubpanel.switchToFrame("//iframe[@id='bwc-frame']");
			activitiesSubpanel.verifyBusinessCard(user1);
			contactDetailPage.isPageLoaded();
			//actHistSub.verifyBusinessCard(user1);
			
			log.info("Search for created task");
			ViewTaskPage viewTaskPage = dashboard.openViewTask();
			viewTaskPage.searchForTask(task);
			
			log.info("Open user assigned to that task");
			viewTaskPage.confirmResult(task);
			task.sAssignedTo = user1.getFirstName() + " (" + user1.getFirstName() + ") " + user1.getLastName();

			ConnectionsBusinessCard connBusCard = viewTaskPage.getBusinessCard(task);
			
			connBusCard.verifyBusinessCardContents(user1);
			log.info("Open task and verify Business Card there");
			TaskDetailPage taskDetailPage = viewTaskPage.selectResult(task);
			connBusCard = taskDetailPage.getBusinessCard();
			connBusCard.verifyBusinessCardContents(user1);
			
			sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());

		log.info("End of test method s9897BusinessCardTasks");
	}
}
