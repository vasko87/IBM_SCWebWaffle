package com.ibm.appium.test.Search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.appium.MobileBaseTest;
import com.ibm.appium.Objects.Call;
import com.ibm.appium.Objects.Contact;
import com.ibm.appium.Objects.Note;
import com.ibm.appium.Objects.Opportunity;
import com.ibm.appium.Objects.Task;
import com.ibm.appium.model.Dashboard;
import com.ibm.appium.model.Menus.MainMenu;
import com.ibm.appium.model.Menus.QuickCreateMenu;
import com.ibm.appium.model.Note.CreateNotePage;
import com.ibm.appium.model.Note.NoteDetailPage;
import com.ibm.appium.test.Note.NotesTests;
import com.ibm.atmn.waffle.extensions.user.User;

public class SearchTests extends MobileBaseTest {

	private static String showMore = "//span[@class='show-more']";
//	private static String favoriteIcon = "//span[@class='show-more']";
	
	Logger log = LoggerFactory.getLogger(NotesTests.class);
	Note note = new Note();
	Contact contact = new Contact();
	Call call = new Call();
	Task task = new Task();
	Opportunity oppty = new Opportunity();

	@Test
	public void s25254HomePageSearchMyFavorites() {

		log.info("Starting method s2419TasksCreate");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		log.info("Using user: " + user.getEmail());

		try {
			//Easiest would be to run some API calls here
			
			//Expand if Mobile APIs available to:
			// - Create oppty
			// - Create task
			// - Create call
	
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
		
			if(noteDetailPage.isPageLoaded())
				click(showMore);
			
			//noteDetailPage.favoriteNote(true);
			
			//Favorite (Star) note
			
			//Go to Dashboard
			MainMenu mMenu = new MainMenu();
			dashBoard = mMenu.openGlobalSearch();
			
			noteDetailPage = dashBoard.searchForItem(note);
			
			//Select favorites only
			//Search for note
			//If found - click on star
			//Wait for 5 sec
			//Search for note
			//Wait for 10 sec
			//If not found - PASS
			
			Assert.assertEquals(noteDetailPage.getNoteDescription(),
					note.getsDescription(),
					"Opportunty description does not match expected");
			log.info("Awesome! Search works for opportunity!");
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method s2419TasksCreate");		
		}
	}

}
