package com.ibm.salesconnect.model.standard.Connections;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

public class ConnectionsLogin extends StandardPageFrame {

	public ConnectionsLogin(Executor exec) {
		super(exec);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isPageLoaded() {
		// TODO Auto-generated method stub
		return false;
	}
	

	//Selectors

	public String linkSSO = "//a[contains(text(),'Single Sign-on (SSO)')]";
	
//Original selector that works with non-SSO page
	
	public String cnxnUserNameField = "//input[@id='username']";
	
	public String cnxnPasswordField = "//input[@id='password']";
	
	public String loginButton = "//input[@value='Log In']";

//SSO page selectors	
	public String cnxnUserNameField_SSO = "//span//input[contains(@id,'Intranet_ID')]";
	
	public String cnxnPasswordField_SSO = "//span//input[contains(@id,'password')]";

	public String loginButton_SSO = "//input[contains(@name,'ibm-submit')]";
	
	public String loginLogoutLink = "//a[@id='logoutLink']";
	

	
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
		if(checkForElement(loginLogoutLink)){
			click(loginLogoutLink);
		}
		
		if(checkForElement(linkSSO)){
			type(cnxnUserNameField_SSO, username);
			type(cnxnPasswordField_SSO, password);
			
			click(loginButton_SSO);
		}else{
			type(cnxnUserNameField, username);
			type(cnxnPasswordField, password);
			
			click(loginButton);
		}
		
		
	}
	

}
