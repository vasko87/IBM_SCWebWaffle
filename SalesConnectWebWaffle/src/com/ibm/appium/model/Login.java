package com.ibm.appium.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Login extends MobilePageFrame {
	Logger log = LoggerFactory.getLogger(Login.class);
	
	// XPath Selectors
	public static String getTextField_Username = "//input[@placeholder='Username']";
	public static String getTextField_Password = "//input[@placeholder='Password']";
	public static String getTextField_UsernameCSS = "#username";
	public static String getTextField_PasswordCSS = "#password";
	public static String getButton_Login = "//a[@id='login_btn']";
	public static String getWelcomeText = "//a[@title='Welcome']";
	public static String getDoneText = "//a[@title='Done']";

	/**
	 * Check if new page is loaded by finding element on that page before
	 * starting any actions.
	 */
	@Override
	public boolean isPageLoaded() {
		return false;
	}

	/**
	 * Main method of logging to Sales Connect mobile.
	 * 
	 * @param userName
	 *            - valid user
	 * @param password
	 *            - valid password
	 * @return Dashboard
	 */
	public Dashboard login(String userName, String password) {
		
		log.info("Waiting for " + getTextField_Username);
		waitForElementVisible(getTextField_Username, 30);

		clear(getTextField_UsernameCSS);
		typeByCSS(getTextField_UsernameCSS, userName);		
		typeByCSS(getTextField_PasswordCSS, password);		
		click(getButton_Login);
		
		return new Dashboard();
	}
}
