package com.ibm.salesconnect.model.standard;

import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

public class Login extends StandardPageFrame {

	public Login(Executor exec) {
		super(exec);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isPageLoaded() {
		// TODO Auto-generated method stub
		return false;
	}
	
	//Selectors
    public static String scDevelopmentArea = "scdevapp";
	public static String preferencesNext = "//a[@name='next_button']";	
	public static String preferencesStartSugar = "//a[@name='start_sugar_button']";	
	
	public String intranetIDField = "//input[@id='Intranet_ID' or @name='username']";
	
	public String passwordField = "//input[@id='password' or @type='password']";
	public String submitButton = "//*[@name='ibm-submit' or @name='login_button']";
	
	public String londoUserIDField = "//*[@id='name']";
	public String londoPasswordField = "//*[@id='pswd']";
	public String londoSubmitButton = "//*[@value='Log In']";
	public String SAMLiFrame = "//iframe[@id='external-login-dialog']";
	public String userName = "//input[contains(@name,'username')]";
	public String userName_sg = "//input[contains(@name,'username')]";
	public String passWord = "//input[contains(@name,'password')]";
	public String passWord_sg = "//input[contains(@name,'password')]";
	public String loginButton = "//input[contains(@name,'ubmit')]";
	public String loginButton_sg = "//a[contains(@name,'login_button')]";
	
	//Tasks
	/**
	 * This is an exception to the standard page object model in that it
	 * triggers a page change but does not return the next page, because the
	 * login page has no knowledge of the resource it is protecting.
	 * 
	 * @param username
	 * @param password
	 */
	public void login(String username, String password) {
		
//		if (checkForElement(SAMLiFrame)) {
//			switchToFrame(SAMLiFrame);
//			type(userName, username);
//			type(passWord, password);
//			click(loginButton);
//		}
		//else {
			//Assert.assertTrue(false, " No log in screen found");
			type(userName_sg, username);
			type(passWord_sg, password);
			click(loginButton_sg);
		//}
	
		//Can be removed if new log in works
//		if (checkForElement(intranetIDField)){
//			type(intranetIDField, username);
//			type(passwordField, password);
//			
//			clickJS(submitButton);
//			
//		} else if (checkForElement(SAMLiFrame)){	
//			switchToFrame(SAMLiFrame);
//			ProductBaseTest productbasetest = new ProductBaseTest();
//			System.out.println(productbasetest.baseURL);
//			if(productbasetest.baseURL.contains(scDevelopmentArea)){
//				type(londoUserIDField, username);
//				type(londoPasswordField, password);
//				click(londoSubmitButton);
//			}else{
//				type(intranetIDField, username);
//			    type(passwordField, password);
//			    click(submitButton);
//		}
//		}
//		else {
//			Assert.assertTrue(false, " No log in screen found");
//		}
		switchToMainWindow();
		
		if(checkForElement("//*[@id='bwc-frame']")){
			return;
		}
		else if(checkForElement(preferencesNext)){
			click(preferencesNext);
			sleep(5);
			click(preferencesNext);
			isPresent(preferencesStartSugar);
			click(preferencesStartSugar);
		}
		
	}

}
