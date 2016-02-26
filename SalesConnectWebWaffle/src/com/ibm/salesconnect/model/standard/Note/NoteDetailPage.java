/**
 * 
 */
package com.ibm.salesconnect.model.standard.Note;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardDetailPage;

/**
 * @author timlehane
 * @date May 15, 2013
 */
public class NoteDetailPage extends StandardDetailPage
{
	Logger log = LoggerFactory.getLogger(NoteDetailPage.class);
	
	/**
	 * @param exec
	 */
	public NoteDetailPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Note Detail page has not loaded within 60 seconds");	
	}
	
	//Selectors
	public static String displayedNoteSubject ="//span[@data-fieldname='name']//div";
	public static String pageLoaded ="//a[@name='edit_button']/..";
	public static String detailsFrame = "//iframe[@id='bwc-frame']";
	public static String MyFavouritesIcon = "//span/span/span/span/i[@class='fa fa-favorite']";
	
	//Methods
	/**
	 * Returns the displayed note subject
	 * @return displayed note subject
	 */
	public String getdisplayedNoteSubject(){
		return getObjectText(displayedNoteSubject);
	}

	public void addNoteToMyFavorites() {
		click (MyFavouritesIcon);
		log.info("Note Added to My Favorites");
	}
	
	public CreateNotePage openEditPage(){
		click(EditButton);
		return new CreateNotePage(exec);
	}
	
	public void deleteNote(){
		click(editDropDown);
		click(deleteOption);
		click("//a[@data-action='confirm']");
	}
	
	public String getNoteID(){
		String url = getCurrentURL();
		log.info("Current URL: " + url);
	      String pattern = "(.*Notes/)(.*)[^&](.*)";
	      String endOfString= "";
	      String LeadID = "";
	      // Create a Pattern object
	      Pattern r = Pattern.compile(pattern);

	      // Now create matcher object.
	      Matcher m = r.matcher(url);
	      if (m.find( )) {
	    	  endOfString = m.group(2);
	      }
	      String pattern2 ="[^&]*";
	      
	      Pattern r1 = Pattern.compile(pattern2);
	      Matcher m1 = r1.matcher(endOfString);
	      if (m1.find( )) {
	    	  LeadID = m1.group(0);
	    	  log.info("Lead found: " + LeadID);
	      }
	      return LeadID;
	}
		
	}


