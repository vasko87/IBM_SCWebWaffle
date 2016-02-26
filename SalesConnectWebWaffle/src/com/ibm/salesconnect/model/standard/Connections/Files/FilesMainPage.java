/**
 * 
 */
package com.ibm.salesconnect.model.standard.Connections.Files;

import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

/**
 * @author timlehane
 * @date Jun 4, 2013
 */
public class FilesMainPage extends StandardPageFrame {
	
	/**
	 * @param exec
	 */
	public FilesMainPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Files page has not loaded within 60 seconds");	
	}

	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}

	
	//Selectors
	public static String fileSearchField = "//input[@id='quickSearch_simpleInput']";
	public static String fileSearchSubmit = "//input[@id='quickSearch_submit']";
	public static String sharedByMe = "//div[@id='lotusSidenav']/div/div/div/ul/li[4]/a/span";
	public static String sharedByMeLoaded = "//a[contains(text(),'Files Shared By Me')]";
	public static String sharedWithMe = "//div[@id='lotusSidenav']/div/div/div/ul/li[3]/a/span";
	public static String sharedWithMeLoaded = "//a[contains(text(),'Files Shared With Me')]";
	public static String publicFiles = "//div[@id='lotusSidenav']/div/div/div/ul/li[6]/a/span";
	public static String publicFilesLoaded = "//a[contains(text(),'Public Files')]";
	public static String appsBanner = "//li[@id='lotusBannerApps']";
	public static String appsButton = "//*[@class='lotusNowrap']//a[contains(@href,'files/app')]";
		
	public static String pageLoaded = "//button[@class='lotusBtn lotusBtnDisabled']";
	//public String getFileName(String fileName){return "link=" + fileName;}; 
	public String getFileName(String fileName){return "//a[@title='" + fileName + "']";}
	public String getSharingLink(String fileName){return "//a[@title='" + fileName + "']/../../..//img[contains(@title,'Shared')]";}
	public String getSharedWith(String userName){return "//td[contains(text(),'Editors:')]/..//a[contains(text(),'" + userName+"')]";}
	public String getSharedWithReader(String client){return "//td[contains(text(),'Readers:')]/..//a[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'" + client.toLowerCase()+"')]";}
	
	//Methods
	public boolean isFilePresent(String fileName){
		return checkForElement(getFileName(fileName));
	}
	
	public void openFileSharing(String fileName){
		if(checkForElement(getFileName(fileName))){
			click(getSharingLink(fileName));
		}
	}
	
	public Boolean isSharedWith(String userName){
		if (checkForElement(getSharedWith(userName))) {
			return true;
		}
		else{
			return false;
		}
	}
	
	public Boolean isSharedWithReader(String team){
		if (checkForElement(getSharedWithReader(team))) {
			return true;
		}
		else{
			return false;
		}
	}
	public void openPublicFiles(){
		mouseHover(appsBanner);
		click(publicFiles);
		waitForSubpanelToLoad(publicFilesLoaded);
	}
	
	public void searchForFile(String fileName){
		type(fileSearchField, fileName);
		click(fileSearchSubmit);
		
	}
	
	public void openSharedByMe(){
		click(sharedByMe);
		waitForElement(sharedByMeLoaded);
	}
	
	public void openSharedWithMe(){
		click(sharedWithMe);
		waitForElement(sharedWithMeLoaded);
	}
}
