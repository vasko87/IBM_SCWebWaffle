package com.ibm.appium.model.Recents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.Objects.Call;
import com.ibm.appium.Objects.Contact;
import com.ibm.appium.Objects.Note;
import com.ibm.appium.Objects.Opportunity;
import com.ibm.appium.Objects.Task;
import com.ibm.appium.model.MobilePageFrame;
import com.ibm.appium.model.Call.CallDetailPage;
import com.ibm.appium.model.Contact.ContactDetailPage;
import com.ibm.appium.model.Note.NoteDetailPage;
import com.ibm.appium.model.Opportunity.OpportunityDetailPage;
import com.ibm.appium.model.Task.TaskDetailPage;

public class RecentsListPage extends MobilePageFrame {
	
	Logger log = LoggerFactory.getLogger(RecentsListPage.class);

	// XPath Selectors
	public static String pageLoaded = "//div[@id='listing-Recents']";
	
	public RecentsListPage() {
		Assert.assertTrue(isPageLoaded(), "Recents List Page has not loaded");
	}

	/**
	 * Check if new page is loaded by finding element on that page before
	 * starting any actions.
	 */
	@Override
	public boolean isPageLoaded() {
		if (isPresent("//a[@title='Done']")) {
			click("//a[@title='Done']");
		}
		return waitForPageToLoad(pageLoaded);
	}
	
	/**
	 * Get specific record from Recents list view.
	 * 
	 * @param sName
	 *            - Task name
	 */
	public String getListItem(String sName) {
		return "//div[@id='listing-Recents']//span[contains(text(),'" + sName
				+ "')]";
	}

	public ContactDetailPage selectContact(Contact contact) {
		click(getListItem(contact.getsFullName()));
		return new ContactDetailPage();
	}

	public CallDetailPage selectCall(Call call) {
		click(getListItem(call.getsSubject()));
		return new CallDetailPage();
	}
	
	public NoteDetailPage selectNote(Note note) {
		click(getListItem(note.getsSubject()));
		return new NoteDetailPage();
	}
	
	public OpportunityDetailPage selectOpportunity(Opportunity oppty) {
		click(getListItem(oppty.getsDescription()));
		return new OpportunityDetailPage();
	}
	
	public TaskDetailPage selectTask(Task task) {
		click(getListItem(task.getsName()));
		return new TaskDetailPage();
	}	
}
