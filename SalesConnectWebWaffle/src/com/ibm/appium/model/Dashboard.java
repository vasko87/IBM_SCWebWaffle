package com.ibm.appium.model;

import com.ibm.appium.Objects.Call;
import com.ibm.appium.Objects.Client;
import com.ibm.appium.Objects.Contact;
import com.ibm.appium.Objects.Note;
import com.ibm.appium.Objects.Opportunity;
import com.ibm.appium.Objects.Task;
import com.ibm.appium.model.Call.CallDetailPage;
import com.ibm.appium.model.Client.ClientDetailPage;
import com.ibm.appium.model.Contact.ContactDetailPage;
import com.ibm.appium.model.Menus.MainMenu;
import com.ibm.appium.model.Menus.QuickCreateMenu;
import com.ibm.appium.model.Note.NoteDetailPage;
import com.ibm.appium.model.Opportunity.OpportunityDetailPage;
import com.ibm.appium.model.Task.TaskDetailPage;

/**
 * @author peterpoliwoda
 *
 */
public class Dashboard extends MobilePageFrame {

	// XPath Selectors
	public static String quickCreateMenu = "(//a[@track='click:create'])[1]";
	public static String quickCreateMenuCSS = "a.createBtn";
	public static String pageLoaded = "//div[@class='pull-to-refresh']";
	public static String mainMenu = "//div[@id='nav-main-menu']//a[@title='Dashboard']";
	public static String mainMenuCSS = ".menuBtn";
	public static String searchField = "//input[@class='search-query']";
	public static String searchButton = "(//a[@track='click:search'])[1]";
	public static String searchClear = "//i[@class='icon clear-button icon-remove-sign']";
	public static String searchClearCSS = "i.clear-button";
	
	public static String globalSearchPage = "//a[@href='#Search']"; 
	
	public static String filterButton = "//span[@class='filter__item filter__item-main']//a";
	public static String filterAllRecords = "//a[@data-filter-id='all_records']";
	public static String filterMyRecords = "//a[@data-filter-id='assigned_to_me']";
	public static String filterMyFavorites= "//a[@data-filter-id='favorites']";
	
	public String searchedListItem(String itemName) {
		return "//div[contains(text(), '" + itemName + "')]";
	}

	public Dashboard() {
		isPageLoaded();
		log.debug("Dashboard loaded");
		
		//What is this and why are there 3 of those with the same id?
		executeJS("$('#tempBorder').remove()");

	}
	
	/**
	 * Check if new page is loaded by finding element on that page before
	 * starting any actions.
	 */
	@Override
	public boolean isPageLoaded() {
	  isTutorial();
	  return waitForPageToLoad(pageLoaded);
	}

	/**
	 * Open quick create menu
	 * 
	 * @return QuickCreateMenu
	 */
	public QuickCreateMenu openQuickCreateMenu() {
		openGlobalSearchPage();
		clickByJS(quickCreateMenuCSS);
		return new QuickCreateMenu();
	}
	
	/**
	 * Open main Sales Connect menu
	 * 
	 * @return MainMenu
	 */

	public MainMenu openMainMenu() {
		clickByJS(mainMenuCSS);
		return new MainMenu();
	}
	
	
	/**
	 * Open Global Search Page (old Homepage) 
	 */
	public void openGlobalSearchPage(){
		waitForElementPresent(mainMenu,30);
		isTutorial();
		System.out.println("Opening menu");
		openMainMenu();
		System.out.println("Opened menu");
		isTutorial();
		click(globalSearchPage);
	}
	
	/**
	 * Search for an object and click to open it's details page
	 */
	private void searchAndOpen(String searchQuery) {
		log.info("Selecting My Records");
		click(filterButton);
		click(filterMyRecords);
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
	}

	/**
	 * Search for a call and open it's detail view
	 * 
	 * @param call
	 * @return CallDetailPage
	 */
	public CallDetailPage searchForItem(Call call) {
		searchAndOpen(call.getsSubject());
		return new CallDetailPage();
	}

	/**
	 * Search for a client and open it's detail view
	 * 
	 * @param client
	 * @return ClientDetailPage
	 */
	public ClientDetailPage searchForItem(Client client) {
		searchAndOpen(client.getsName());
		return new ClientDetailPage();
	}
	
	/**
	 * Search for a contact and open it's detail view
	 * 
	 * @param contact
	 * @return ContactDetailPage
	 */
	public ContactDetailPage searchForItem(Contact contact) {
		searchAndOpen("\"" + contact.getsFirstName() + " " + contact.getsLastName() + "\"");
		return new ContactDetailPage();
	}

	/**
	 * Search for a note and open it's detail view
	 * 
	 * @param note
	 * @return NoteDetailPage
	 */
	public NoteDetailPage searchForItem(Note note) {
		searchAndOpen(note.getsSubject());
		return new NoteDetailPage();
	}

	/**
	 * Search for an opportunity and open it's detail view
	 * 
	 * @param oppty
	 * @return OpportunityDetailPage
	 */
	public OpportunityDetailPage searchForItem(Opportunity oppty) {
		searchAndOpen(oppty.getsId());
		return new OpportunityDetailPage();
	}

	/**
	 * Search for a task and open it's detail view
	 * 
	 * @param task
	 * @return TaskDetailPage
	 */
	public TaskDetailPage searchForItem(Task task) {
		searchAndOpen(task.getsName());
		return new TaskDetailPage();
	}
}
