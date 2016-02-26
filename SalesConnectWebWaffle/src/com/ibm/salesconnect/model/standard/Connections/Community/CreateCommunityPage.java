/**
 * 
 */
package com.ibm.salesconnect.model.standard.Connections.Community;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.objects.Connections.*;

/**
 * @author evafarrell
 * @date May 1, 2013
 */
public class CreateCommunityPage extends StandardPageFrame{
	Logger log = LoggerFactory.getLogger(CreateCommunityPage.class);
	/**
	 * @param exec
	 */
	public CreateCommunityPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Create Community page has not loaded within 60 seconds");	
	}


	/* (non-Javadoc)
	 * @see com.ibm.ecm.product.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}

	//Selectors
	public static String getTextField_CommunityName =  "//input[@id='addCommunityName']";
	public static String getTextEditor_CommunityDesc = "//td[@id='cke_contents_ckeditor']/iframe";
	public static String getRadioButton_CommunityAccessOption1 = "//*[@id='addPublicAccess']";
	public static String getRadioButton_CommunityAccessOption2 = "//*[@id='addPublicInviteOnlyAccess']";
	public static String getRadioButton_CommunityAccessOption3 = "//*[@id='addPrivateAccess']";
	public static String CKEditor_Toolbar_Bidi = "//*[@id='cke_addCommunityDescription']";
	public static String getTextEditor_CommunityDescription = "//body/p";

	public static String getButton_Save = "//input[@value='Save']";
	public static String pageLoaded = "//*[@id='addCommunityName']";
	public static String displayedCommunityName = "//a[@id='communityName']";

	//Tasks
	
	/**
	 * Returns the displayed community name
	 * @return displayed community name
	 */
	public String getdisplayedCommunityName(){
		return getObjectText(displayedCommunityName);
	}
	
	public CreateCommunityPage enterCommunityInfo(Community community) throws InterruptedException{
			if (community.sCommunityName.length()>0){
				waitForElement(getTextField_CommunityName);
				scrollElementToMiddleOfBrowser(getTextField_CommunityName);
				type(getTextField_CommunityName, community.sCommunityName);	
			}
			
			if (community.sCommunityDescription.length()>0){
				click(CKEditor_Toolbar_Bidi);
				typeNative(community.sCommunityDescription);	
			}
		return this;
	}

	public void saveCommunity(){
		clickJS(getButton_Save);
	}
	
	
}
