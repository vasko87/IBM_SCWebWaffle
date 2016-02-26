package com.ibm.salesconnect.test.Level1;

import java.sql.SQLException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.APIUtilities;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.NoteRestAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Note.NoteDetailPage;
import com.ibm.salesconnect.model.standard.Note.ViewNotePage;


/**
 * <strong>Description:</strong>
 * <br/><br/>
 * Test to validate the Create, Read, Update-Convert to Favorite and Search for the Favorite Note functionality of the Notes module
 * <br/><br/>
 * 
 * @author 
 * Srinidhi B Shridhar
 * 
 */
public class AT_FavoriteNote extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(AT_Sugar.class);
	int rand = new Random().nextInt(100000);
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Open browser and log in</li>
	 * <li>Create Note</li>
	 * <li>Search for created Note</li>
	 * <li>Open Note detail page</li>
	 * <li>Update Note-Add to Favorites</li>
	 * <li>Search for the Favorite Note</li>
	 * </ol>
	 */
	@Test(groups = { "Level1","AT_Sugar","BVT","BVT1"})
	public void Test_AT_FavoriteNote() throws SQLException{
		log.info("Start of test method Test_AT_Note");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
		String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());	
		
		log.info("Creating Note Via API");
		NoteRestAPI noteAPI = new NoteRestAPI();
		String noteSubject ="Favorite Note Subject";
		noteAPI.createNotereturnBean(testConfig.getBrowserURL(), OAuthToken, noteSubject, "Favorite Note description", assignedUserID);
		
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);

		log.info("Searching for created Note");

		ViewNotePage viewNotePage = dashboard.openViewNote();
		viewNotePage.searchForNote(noteSubject);

		NoteDetailPage noteDetailPage = viewNotePage.selectResult(noteSubject);
		noteDetailPage.addNoteToMyFavorites();
//		favoriteNote.bMyFavorites=true;

		log.info("Searching for created note");
		viewNotePage = dashboard.openViewNote();
		viewNotePage.searchForNote(noteSubject);

		noteDetailPage = viewNotePage.selectResult(noteSubject);

		Assert.assertEquals(noteDetailPage.getdisplayedNoteSubject(),noteSubject,"Incorrect note detail page was opened");

		log.info("End of test method Test_AT_favoriteNote");

	}
}