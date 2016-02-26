/**
 * 
 */
package com.ibm.salesconnect.model.standard.Connections.Forums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.objects.Connections.*;

/**
 * @author evafarrell
 * @date May 1, 2013
 */
public class CreateCommunityForumPage extends StandardPageFrame{
	Logger log = LoggerFactory.getLogger(CreateCommunityForumPage.class);
	/**
	 * @param exec
	 */
	public CreateCommunityForumPage(Executor exec) {
		super(exec);
		//Assert.assertTrue(isPageLoaded(), "Create Community Forum page has not loaded within 60 seconds");	
	}


	/* (non-Javadoc)
	 * @see com.ibm.ecm.product.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}

	//Selectors
	public static String pageLoaded = "//a[contains(text(),'Attach a File')]";
	public static String displayedCommunityForumName = "//div[@id='list']/table/tbody/tr[2]/td[2]/h4/a  ";
	public static String getTextField_TopicTitle = "//input[@id='lconn_forums_PostForm_0_postTitle']";
	public static String getFrame_ForumDesc = "//*[@id='cke_contents_editor1'or @id='cke_1_contents']";
	public static String getButton_Save = "//input[@value='Save']";
	public static String emptyTopic = "//div[contains(@style,'opacity: 1')]//div[contains(text(),'You have not entered any content in the forum post. Do you want to continue?')]";
	public static String dialogSaveButton = "//div[contains(@style,'opacity: 1')]//*[@type='button' and @value='OK']";
	
	//Tasks
	/**
	 * Returns the displayed community Forum name
	 * @return displayed community Forum name
	 */
	public String getdisplayedCommunityForumName(){
		return getObjectText(displayedCommunityForumName);
	}
	
	public CreateCommunityForumPage enterCommunityForumInfo(CommunityForum forum){

			//set Forum Topic Title
			if (forum.sForumTopicTitle.length()>0){
				type(getTextField_TopicTitle, forum.sForumTopicTitle);	
			}
			
			//set Forum Post Title
			if (forum.sForumPostTitle.length()>0){
				click(getFrame_ForumDesc);
				typeNative(forum.sForumPostTitle);
			}
			
		return this;
	}

	public void saveCommunityForum(){
		clickJS(getButton_Save);
		
		if(checkForElement(emptyTopic)){
			click(dialogSaveButton);
		}
		sleep(2);
	}
	
	
}
