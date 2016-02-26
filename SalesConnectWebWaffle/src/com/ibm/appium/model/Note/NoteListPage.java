package com.ibm.appium.model.Note;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.Objects.Note;
import com.ibm.appium.model.MobilePageFrame;

public class NoteListPage extends MobilePageFrame {
	Logger log = LoggerFactory.getLogger(NoteListPage.class);

	// XPath Selectors
	public static String pageLoaded = "//div[@id='listing-Notes']";
	public static String searchField = "//input[@class='search-query']";
	public static String searchButton = "(//a[@track='click:search'])[1]";
	public static String searchClear = "//i[@class='icon clear-button icon-remove-sign']";
	
	public NoteListPage() {
		Assert.assertTrue(isPageLoaded(), "Note List Page has not loaded");
	}

	/**
	 * Check if new page is loaded by finding element on that page before
	 * starting any actions.
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}
	
	/**
	 * Get specific Note record from Note list view.
	 * 
	 * @param sSubject
	 *            - Note subject
	 */
	public String searchedListItem(String sSubject) {
		return "//div[contains(text(),'" + sSubject
				+ "')]";
	}

	/**
	 * Search for specific note using search input box located on note list
	 * view.
	 */
	public NoteDetailPage searchForNote(Note note) {

		String searchQuery = note.getsSubject();
		
		log.info("Searching for item" + searchQuery);
		click(searchButton);
		click(searchField);
		log.info("Clearing search field if needed");
		clear();
		log.info("Typing search phrase");
		type(searchField, searchQuery);
		log.debug("Typed in. Waiting for element: " + searchedListItem(searchQuery));
		waitForElementPresent(searchedListItem(searchQuery), 30);
		log.debug("Done waiting. Is it present?");
		
		if(isPresent(searchedListItem(searchQuery))){
			log.debug("Element found. Clicking.");
			click(searchedListItem(searchQuery));
		}
		else {
			log.debug("Element not found. Trying search again.");
			click(searchField);
			clear();
			log.info("Typing search phrase again");
			type(searchField, searchQuery);				
			log.debug("Typed in. Waiting for element.");
			waitForElementPresent(searchedListItem(searchQuery), 15);
			
			log.debug("Done waiting. Is it present?");
			if(isPresent(searchedListItem(searchQuery))){
				log.debug("Element found. Clicking.");
				click(searchedListItem(searchQuery));
			}
			else {
				log.debug("Element not found. Trying search last time.");
				click(searchField);
				clear();
				log.info("Typing search phrase again");
				type(searchField, searchQuery);				
				waitForElementPresent(searchedListItem(searchQuery), 15);
				click(searchedListItem(searchQuery));
			}
		}
		return new NoteDetailPage();
		
	}

	/**
	 * Select result after searching for note record and navigate to Note detail
	 * page.
	 * 
	 * @return NoteDetailPage
	 */
	public NoteDetailPage selectResult(Note note) {
		//TODO: Swipe down or remove and type in again
		click(searchedListItem(note.getsSubject()));
		return new NoteDetailPage();
	}
}
