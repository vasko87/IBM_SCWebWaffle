package com.ibm.appium.test.Call;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.appium.MobileBaseTest;
import com.ibm.appium.Objects.Call;
import com.ibm.appium.model.Dashboard;
import com.ibm.appium.model.Menus.MainMenu;
import com.ibm.appium.model.Menus.QuickCreateMenu;
import com.ibm.appium.model.Call.CallListPage;
import com.ibm.appium.model.Call.CreateCallPage;
import com.ibm.appium.model.Call.CallDetailPage;
import com.ibm.atmn.waffle.extensions.user.User;

public class CallsTests extends MobileBaseTest {

	public static String showMore = "//span[@class='show-more']";

	Logger log = LoggerFactory.getLogger(CallsTests.class);
	Call call = new Call();

	/**
	 * Login to Sales Connect mobile, create new task using quick menu and
	 * verify task has been created.
	 */
	@Test
	public void s2495CallsCreate() {

		log.info("Starting method s2495CallsCreate");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		log.info("Using user: " + user.getEmail());

		try {
			call.populate();
	
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
	
			QuickCreateMenu qCM = dashBoard.openQuickCreateMenu();
	
			log.info("Logging new call");
			CreateCallPage createCallPage = qCM.openCreateCallPage();
			createCallPage.enterCallInfo(call);
			CallDetailPage callDetailPage = createCallPage.saveCall();
	
			log.info("Verifying call creation");
			
			Assert.assertEquals(callDetailPage.getCallSubject(),
					call.getsSubject(), "Call subject does not match expected");
			Assert.assertEquals(callDetailPage.getCallType(), call.getsCallType(),
					"Call Type does not match expected");
			Assert.assertEquals(callDetailPage.getCallDuration(),
					call.getsDurationMinutes(),
					"Call Duration does not match expected");
			Assert.assertEquals(callDetailPage.getCallStatus(),
					call.getsCallStatus(), "Call Status does not match expected");
			Assert.assertEquals(callDetailPage.getCallSummary(),
					call.getsSummaryOfCall(), "Call Summary does not mact expected");
			log.info("Created call successfully");
			}
		finally{
			log.info("Return the user back to the pool after test to prevent starvation.");		
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method s2495CallsCreate");		
		}	
	}

	@Test(dependsOnMethods = { "s2495CallsCreate" })
	public void s2495CallsDashboardSearch() {
		
		log.info("Starting method s2495CallsDashboardSearch");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");
		log.info(user.getEmail());
		
		try {
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
			dashBoard.openGlobalSearchPage();
			
			CallDetailPage callDetailPage = dashBoard.searchForItem(call);
			
			log.info("Verifying call creation");
			click(showMore);
			Assert.assertEquals(callDetailPage.getCallSubject(),
					call.getsSubject(), "Call subject does not match expected");
			log.info("Call found.");
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method s2495CallsDashboardSearch");		
		}	
	}

	@Test(dependsOnMethods = { "s2495CallsCreate" })
	public void s2495CallsListViewSearchMyItems() {
		
		log.info("Starting method s2495CallsListViewSearchMyItems");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		log.info("Using user: " + user.getEmail());

		try {
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
	
			log.info("Navigating to calls list view");
			MainMenu mM = dashBoard.openMainMenu();
			CallListPage callListPage = mM.openCallListView();
			
			CallDetailPage callDetailPage = callListPage.searchForCall(call);
			
			log.info("Verifying call creation");
			click(showMore);
			Assert.assertEquals(callDetailPage.getCallSubject(),
					call.getsSubject(), "Call subject does not match expected");
			log.info("Call found.");
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method s2495CallsListViewSearchMyItems");		
		}	
	}
	
	@Test(dependsOnMethods = { "s2495CallsCreate" })
	public void s2496CallsEdit() {
		
		log.info("Starting method s2496CallsEdit");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		log.info("Using user: " + user.getEmail());

		try {
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
	
			log.info("Navigating to calls list view");
			MainMenu mM = dashBoard.openMainMenu();
			CallListPage callListPage = mM.openCallListView();
			
			CallDetailPage callDetailPage = callListPage.searchForCall(call);
	
			log.info("Editing call");
			CreateCallPage createCallPage = callDetailPage.editCall();
			createCallPage.updateCallInfo(call);
			createCallPage.saveCall();
	
			log.info("Verifying call update");
			click(showMore);
			Assert.assertEquals(callDetailPage.getCallSubject(), call.getsSubject()
					+ call.getsSubjectUPD(),
					"Updated Call subject does not match expected");
			log.info("Edited call successfully");
			call.updateDetails();
		}
	finally{
		commonUserAllocator.checkInAllGroupUsers("mobile");
		log.info("End method s2496CallsEdit");		
	}	
	}
}
