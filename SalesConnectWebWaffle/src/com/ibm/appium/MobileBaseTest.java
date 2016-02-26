package com.ibm.appium;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;

import com.ibm.appium.common.ClientAllocation;
import com.ibm.appium.model.Dashboard;
import com.ibm.appium.model.Login;
import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.atmn.waffle.extensions.user.UserAllocation;
import com.ibm.appium.TestBase;

public class MobileBaseTest extends TestBase {
	
	public static String preloader = "//div[@id='preloader']";
	public static String tutorialDone = "//a[@title='Done']";

	protected Logger log = LoggerFactory.getLogger(MobileBaseTest.class);

	private static String absolutePath = Class.class.getClass()
			.getResource("/").getPath();

	private static String getAbsolutePath(String relativePath) {
		if (absolutePath.contains("build")) {
			return relativePath;
		} else {
			if (absolutePath.contains("SalesConnectWebWaffleDev")) {
				absolutePath = absolutePath.replace("SalesConnectWebWaffleDev",
						"SalesConnectWebWaffle");
			}
			if (absolutePath.contains("bin")) {
				return absolutePath.substring(0,
						absolutePath.lastIndexOf("bin"))
						+ relativePath;
			}
			return absolutePath + relativePath;
		}
	}

	protected static final UserAllocation commonUserAllocator = UserAllocation
			.getUserAllocation(getAbsolutePath("test_config/extensions/user/default_users.properties"));

	protected static final ClientAllocation commonClientAllocator = ClientAllocation
			.getClientAllocation(getAbsolutePath("test_config/extensions/client/default_clients.properties"));
	
	public Dashboard launchWithLogin(User user) {
		super.setUp();
		switchToContentHybrid();

		if (user != null) {		  
		  if(isPresent(preloader,1)){
				System.out.println("Preloader still visible. Going into preloader check loop.");
				while(isPresent(preloader,1)){
					System.out.println("Checking for preloader");
					if (isPresent(Dashboard.pageLoaded,1)) {
						System.out.println("Already logged in to SalesConnect. Showing dashboard.");
						return new Dashboard();
					}
				}
		  	}
		  else if(isPresent(Login.getTextField_Username,1)){
				System.out.println("Not logged in yet. Logging in now.");
				Login loginPage = new Login();
				Dashboard dashBoard = loginPage.login(user.getEmail(), user.getPassword());
				System.out.println("Returning Dashboard after login.");
				return dashBoard;
		  }
		}
		return new Dashboard();
	}

	@AfterMethod
	public void tearDown() {
		super.tearDown();
	}

	public void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void isTutorial(){
		if (isPresent(tutorialDone,3))
			click(tutorialDone);
	}
}
