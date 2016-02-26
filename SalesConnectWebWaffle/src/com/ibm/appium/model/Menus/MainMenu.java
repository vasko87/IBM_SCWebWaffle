package com.ibm.appium.model.Menus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.model.Dashboard;
import com.ibm.appium.model.MobilePageFrame;
import com.ibm.appium.model.Call.CallListPage;
import com.ibm.appium.model.Client.ClientListPage;
import com.ibm.appium.model.Contact.ContactListPage;
import com.ibm.appium.model.Note.NoteListPage;
import com.ibm.appium.model.Opportunity.OpportunityListPage;
import com.ibm.appium.model.RecentEvents.RecentEventsListPage;
import com.ibm.appium.model.Recents.RecentsListPage;
import com.ibm.appium.model.Task.TaskListPage;

public class MainMenu extends MobilePageFrame {
	Logger log = LoggerFactory.getLogger(MainMenu.class);

	// XPath Selectors
	public static String pageLoaded = "//a[@href='#Search']";
	public static String linkHomePage = "//a[@href='#Search']";
	public static String linkRecents = "//a[@href='#Recents']";
	public static String linkClientsView = "//a[@href='#Accounts']";
	public static String linkContactsView = "//a[@href='#Contacts']";
	public static String linkOpportunitiesView = "//a[@href='#Opportunities']";
	public static String linkCallsView = "//a[@href='#Calls']";
	public static String linkTasksView = "//a[@href='#Tasks']";
	public static String linkMeetingsView = "//a[@href='#Meetings']";
	public static String linkNotesView = "//a[@href='#Notes']";
	public static String linkRecentEventsView = "//a[@href='#IBMMobileConnections']";
	public static String linkForecastView = "//a[@href='#ibm_Cadences']";
	public static String linkAboutView = "//a[@href='#About']";
	public static String linkSettingsView = "//a[@href='#Settings']";
	public static String linkTourView = "//a[@href='#tour']";
	public static String logoutButton = "//a[@href='#logout']";
	
	public MainMenu() {
		isTutorial();
		Assert.assertTrue(isPageLoaded(), "Main menu has not loaded");
	}

	/**
	 * Check if new page is loaded by finding element on that page before
	 * starting any actions.
	 */
	@Override
	public boolean isPageLoaded() {
		//isTutorial();
		return waitForPageToLoad(pageLoaded,false);
	}

	/**
	 * Navigate to Dashboard from main menu.
	 * 
	 * @return OpportunityListPage
	 */
	public Dashboard openGlobalSearch() {
		click(linkHomePage);
		return new Dashboard();
	}

	/**
	 * Navigate to Opportunity List view from main menu.
	 * 
	 * @return OpportunityListPage
	 */
	public OpportunityListPage openOpptyListView() {
		click(linkOpportunitiesView);
		return new OpportunityListPage();
	}

	/**
	 * Navigate to Call List view from main menu.
	 * 
	 * @return CallListPage
	 */
	public CallListPage openCallListView() {
		click(linkCallsView);
		return new CallListPage();
	}
	
	/**
	 * Navigate to Client List view from main menu.
	 * 
	 * @return ClientListPage
	 */
	public ClientListPage openClientListView() {
		click(linkClientsView);
		return new ClientListPage();
	}
	
	/**
	 * Navigate to Contact List view from main menu.
	 * 
	 * @return ContactListPage
	 */
	public ContactListPage openContactListView() {
		click(linkContactsView);
		return new ContactListPage();
	}
	
	/**
	 * Navigate to Note List view from main menu.
	 * 
	 * @return NoteListPage
	 */
	public NoteListPage openNoteListView() {
		click(linkNotesView);
		return new NoteListPage();
	}
	
	/**
	 * Navigate to Task List view from main menu.
	 * 
	 * @return TaskListPage
	 */
	public TaskListPage openTaskListView() {
		click(linkTasksView);
		return new TaskListPage();
	}
	
	/**
	 * Navigate to Recents List view from main menu.
	 * 
	 * @return RecentsListPage
	 */
	public RecentsListPage openRecentsListView() {
		click(linkRecents);
		return new RecentsListPage();
	}
	
	/**
	 * Navigate to Recents Events list view from main menu.
	 * 
	 * @return RecentsListPage
	 */
	public RecentEventsListPage openRecentEventsListView() {
		click(linkRecentEventsView);
		return new RecentEventsListPage();
	}
	

}
