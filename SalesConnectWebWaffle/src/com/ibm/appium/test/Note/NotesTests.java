package com.ibm.appium.test.Note;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.appium.MobileBaseTest;
import com.ibm.appium.Objects.Note;
import com.ibm.appium.model.Dashboard;
import com.ibm.appium.model.Menus.MainMenu;
import com.ibm.appium.model.Menus.QuickCreateMenu;
import com.ibm.appium.model.Note.CreateNotePage;
import com.ibm.appium.model.Note.NoteDetailPage;
import com.ibm.appium.model.Note.NoteListPage;
import com.ibm.atmn.waffle.extensions.user.User;

public class NotesTests extends MobileBaseTest {
	
	private static String showMore = "//span[@class='show-more']";
	
	Logger log = LoggerFactory.getLogger(NotesTests.class);
	Note note = new Note();
	
	/**
	 * Login to Sales Connect mobile, create new note using quick menu and
	 * verify note has been created.
	 */
	@Test
	public void s3410NotesCreate() {
		log.info("Starting method s3410NotesCreate");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		log.info("Using user: " + user.getEmail());

		try {
			note.populate();
	
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
			
			log.info("Quick create note");
			QuickCreateMenu qCM = dashBoard.openQuickCreateMenu();
	
			log.info("Creating note");
			CreateNotePage createNotePage = qCM.openCreateNotePage();
			createNotePage.enterNoteInfo(note);
			NoteDetailPage noteDetailPage = createNotePage.saveNote();
	
			log.info("Verifying note creation");
		
			if(noteDetailPage.isPageLoaded()){
				isTutorial();
				click(showMore);
			}
			Assert.assertEquals(noteDetailPage.getNoteSubject(),
					note.getsSubject(), "The Note subject does not match expected");
			Assert.assertEquals(noteDetailPage.getNoteDescription(),
					note.getsDescription(),
					"The Note description does not match expected");			
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method s3410NotesCreate");		
		}	
	}

	@Test(dependsOnMethods = { "s3410NotesCreate" })
	public void sNoteDashboardSearch() {
		log.info("Starting method sNoteDashboardSearch");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		log.info("Using user: " + user.getEmail());

		try {
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
			dashBoard.openGlobalSearchPage();
			NoteDetailPage noteDetailPage = dashBoard.searchForItem(note);
	
			log.info("Verifying note contents");
			
			if(noteDetailPage.isPageLoaded()) { 
			  isTutorial();
			  click(showMore);
			  }
		
			Assert.assertEquals(noteDetailPage.getNoteSubject(),
					note.getsSubject(), "The Note subject does not match expected");
			Assert.assertEquals(noteDetailPage.getNoteDescription(),
					note.getsDescription(),
					"The Note description does not match expected");
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method sNoteDashboardSearch");		
		}	
	}
	
	@Test(dependsOnMethods = { "s3410NotesCreate" })
	public void sNoteListViewSearchMyItems() {
		log.info("Starting method sNoteListViewSearchMyItems");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		log.info("Using user: " + user.getEmail());

		try {
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
	
			log.info("Navigating to note list view");
			MainMenu mM = dashBoard.openMainMenu();
			NoteListPage noteListPage = mM.openNoteListView();
			
			log.info("Search for previously created note");
			NoteDetailPage noteDetailPage = noteListPage.searchForNote(note); 
			
			log.info("Verifying note contents");		
			if(noteDetailPage.isPageLoaded()) { 
				  isTutorial();
				  click(showMore);
				  }		
			Assert.assertEquals(noteDetailPage.getNoteSubject(),
					note.getsSubject(), "The Note subject does not match expected");
			Assert.assertEquals(noteDetailPage.getNoteDescription(),
					note.getsDescription(),
					"The Note description does not match expected");	
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method sNoteListViewSearchMyItems");		
		}			
	}
	
	@Test(dependsOnMethods = { "s3410NotesCreate" })
	public void sNoteEdit() {
		log.info("Starting method sNoteEdit");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		log.info("Using user: " + user.getEmail());

		try {
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
	
			log.info("Navigating to note list view");
			MainMenu mM = dashBoard.openMainMenu();
			NoteListPage noteListPage = mM.openNoteListView();
			
			log.info("Search for previously created note");
			NoteDetailPage noteDetailPage = noteListPage.searchForNote(note); 

	
			log.info("Editing note");
			CreateNotePage createNotePage = noteDetailPage.editNote();
			createNotePage.updateNoteInfo(note);
			createNotePage.saveNote();
	
			if(noteDetailPage.isPageLoaded()) { 
			  isTutorial();
			  click(showMore);
			  }
			
			log.info("Verifying note update");
			Assert.assertEquals(noteDetailPage.getNoteSubject(), note.getsSubject()
					+ note.getsSubjectUPD(),
					"Updated Note name does not match expected");
			note.updateDetails();
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method sNoteEdit");		
		}
	}

}
