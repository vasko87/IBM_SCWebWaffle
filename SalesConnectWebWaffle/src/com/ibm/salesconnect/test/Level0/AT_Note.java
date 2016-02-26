package com.ibm.salesconnect.test.Level0;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.LeadsRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.NoteRestAPI;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Lead.CreateLeadPage;
import com.ibm.salesconnect.model.standard.Note.CreateNotePage;
import com.ibm.salesconnect.model.standard.Note.NoteDetailPage;
import com.ibm.salesconnect.model.standard.Note.ViewNotePage;
import com.ibm.salesconnect.objects.Note;

/**
 * <strong>Description:</strong>
 * <br/><br/>
 * Test to validate the create, read update and delete functionality of the Notes module
 * <br/><br/>
 * 
 * @author 
 * Tim Lehane
 * 
 */
public class AT_Note extends ProductBaseTest {
	
	
	Logger log = LoggerFactory.getLogger(AT_Note.class);

	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Open browser and log in</li>
	 * <li>Create Note</li>
	 * <li>Search for created Note</li>
	 * <li>Edit the Note</li>
	 * <li>Confirm that the Note has been edited</li>
	 * <li>Delete the Note</li>
	 * <li>Confirm that the Note has been deleted</li>
	 * </ol>
	 */	
	@Test(groups = { "Level1","AT_Sugar","BVT","BVT0"})
	public void Test_AT_Note(){
		log.info("Start of test method Test_AT_Note");

		Note note = new Note();
		note.populate();
		User user1 = commonUserAllocator.getUser(this);

		log.info("Logging in");

		Dashboard dashboard = launchWithLogin(user1);

		log.info("Create a Note");
		CreateNotePage createNotePage = dashboard.openCreateNote();
		createNotePage.enterNoteInfo(note);
		createNotePage.saveNote();

		log.info("Searching for created Note");
		ViewNotePage viewNotePage = dashboard.openViewNote();
		viewNotePage.searchForNote(note);
		NoteDetailPage noteDetailPage = viewNotePage.selectResult(note);

		Assert.assertEquals(noteDetailPage.getdisplayedNoteSubject(),note.sSubject,"Incorrect note detail page was opened");
		String noteID = noteDetailPage.getNoteID(); 
		
		log.info("Editing note");
		CreateNotePage editNotePage = noteDetailPage.openEditPage();

		note.setsSubject(note.getsSubject()+"edit");
		editNotePage.editNoteSubject(note);
		editNotePage.saveEditedNote();
		
		log.info("Searching for edited Note ");	
		sleep(10);
		Assert.assertEquals(noteDetailPage.getdisplayedNoteSubject(), note.getsSubject(), "Subject value on page does not equal the edited value");
		
		noteDetailPage.deleteNote();
		
		log.info("Confirming note has been deleted via API");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword());
		
		NoteRestAPI noteRestAPI = new NoteRestAPI();
		noteRestAPI.getNote(testConfig.getBrowserURL(), token, noteID,  "404");

		log.info("End of test method Test_AT_Note");
	}
}
