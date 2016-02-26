package com.ibm.appium.test.RecentEvents;

import org.testng.annotations.Test;

import com.ibm.appium.MobileBaseTest;
import com.ibm.appium.model.Dashboard;
import com.ibm.appium.model.Menus.MainMenu;
import com.ibm.appium.model.RecentEvents.RecentEventsListPage;
import com.ibm.atmn.waffle.extensions.user.User;

public class RecentEventsTests extends MobileBaseTest {

	@Test(dependsOnMethods = { "s2419TasksCreate" })
	public void s62198RecentEventsCreateEvent() {
		log.info("Starting method s62198RecentEventsCreateEvent");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		log.info("Using user: " + user.getEmail());
		
		try {
			
			//TODO: Create an event using an API

			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
	
			log.info("Navigating to tasks list view");
			MainMenu mM = dashBoard.openMainMenu();
			RecentEventsListPage recentEventsListView = mM.openRecentEventsListView();
			
			//TODO: Check if event is visible
			
			//TODO: If not, figure out a way to refresh
			
			//TODO: If event exists check it's strings
			
			//TODO: Open to see if the proper details view is being open
			
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method s62198RecentEventsCreateEvent");		
		}	
	}
}
