/**
 * 
 */
package com.ibm.salesconnect.model.standard.Connections.Activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.objects.Connections.*;

/**
 * @author evafarrell
 * @date May 1, 2013
 */
public class CreateCommunityActivityPage extends StandardPageFrame{
	Logger log = LoggerFactory.getLogger(CreateCommunityActivityPage.class);
	/**
	 * @param exec
	 */
	public CreateCommunityActivityPage(Executor exec) {
		super(exec);
		//Assert.assertTrue(isPageLoaded(), "Create Community Activity page has not loaded within 60 seconds");	
	}


	/* (non-Javadoc)
	 * @see com.ibm.ecm.product.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}

	//Selectors
	public static String pageLoaded = "//input[@id='lconn_act_ActivityForm_0_titleInput']";
	public static String displayedCommunityActivityName = "//*[@id='activityList']/div[4]";
	public static String getTextField_CommunityName = "css=*[id='addCommunityName']";
	public static String getLink_StartAnActivity = "link=Start an Activity";
	public static String getTextField_ActivityTitle = "//input[@id='lconn_act_ActivityForm_0_titleInput']";
	public static String getTextField_ActivityTags = "//input[@id='lconn_act_ActivityForm_0tagz']";
	public static String getTextField_ActivityDescription = "//textarea[@id='lconn_act_ActivityForm_0_descriptionInput']";
	public static String getButton_Save = "//input[@value='Save']";
	public static String getButton_Cancel = "//input[@class='lotusFormButton'][@value='Cancel']";
	
	//Tasks
	
	/**
	 * Returns the displayed community activity name
	 * @return displayed community activity name
	 */
	public String getdisplayedCommunityActivityName(){
		return getObjectText(displayedCommunityActivityName);
	}
	
	public CreateCommunityActivityPage enterCommunityActivityInfo(CommunityActivity activity){
		
			//set Activity Title
			if (activity.sActivityTitle.length()>0){
				type(getTextField_ActivityTitle, activity.sActivityTitle);	
			}
			//set Activity Tags
			if (activity.sActivityTags.length()>0){
				type(getTextField_ActivityTags, activity.sActivityTags);	
			}
			//set Activity Description
			if (activity.sActivityTitle.length()>0){
				type(getTextField_ActivityDescription, activity.sActivityDescription);	
			}		
		return this;
	}

	public void saveCommunityActivity(){
		clickJS(getButton_Save);
		sleep(5);
	}
	
	
}
