/**
 * 
 */
package com.ibm.salesconnect.model.standard.Connections.Blogs;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.objects.Connections.*;

/**
 * @author evafarrell
 * @date May 1, 2013
 */
public class CreateCommunityBlogPage extends StandardPageFrame{
	Logger log = LoggerFactory.getLogger(CreateCommunityBlogPage.class);
	/**
	 * @param exec
	 */
	public CreateCommunityBlogPage(Executor exec) {
		super(exec);
		//Assert.assertTrue(isPageLoaded(), "Create Community Blog page has not loaded within 60 seconds");	
	}


	/* (non-Javadoc)
	 * @see com.ibm.ecm.product.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}

	//Selectors
	public static String pageLoaded = "//input[@id='title']";
	public static String displayedCommunityBlogEntryName = "//div[@id='blogEntries']/table/tbody/tr[2]/td[2]/h4/a";
	public static String getTextField_BlogEntryTitle = "//input[@id='title']";
	public static String getLink_BlogEntryTags = "//a[@title='Add Tags']";
	public static String getTextField_BlogEntryTags = "//input[@id='addtagwidgetAddTagsTypeAhead']";
	public static String getButton_AddTagOk = "//form[@dojoattachpoint='_tagSearchFormElem']/input[@type='button']";
	public static String getTextEditor_BlogEntryDesc = "//*[@id='cke_contents_ckeditor'or @id='cke_1_contents']/iframe";
	public static String getButton_Post = "//input[@id='postEntryID']";
	public static String addAComment = "//a[@id='AddWeblogEntryCommentLink']";
	
	
	//Tasks
	/**
	 * Returns the displayed community blog entry name
	 * @return displayed community blog entry name
	 */
	public String getdisplayedCommunityBlogEntryName(){
		return getObjectText(displayedCommunityBlogEntryName);
	}
	
	public CreateCommunityBlogPage enterCommunityBlogInfo(CommunityBlog blog){

			//set Blog Title
			if (blog.sBlogEntryTitle.length()>0){
				type(getTextField_BlogEntryTitle, blog.sBlogEntryTitle);	
			}
			//set Blog Tags
			if (blog.sBlogEntryTag.length()>0){
				click(getLink_BlogEntryTags);
				type(getTextField_BlogEntryTags, blog.sBlogEntryTag);
				click(getButton_AddTagOk);
			}
			//set Blog Description
			if (blog.sBlogEntryDescription.length()>0){
				click(getTextEditor_BlogEntryDesc);
				typeNative(blog.sBlogEntryDescription);	
			}		
		return this;
	}

	public void postCommunityBlogEntry(){
		click(getButton_Post);
		sleep(4);
		//Sometimes Post is not clicked the first time!
		if (checkForElement(getButton_Post)){
			clickJS(getButton_Post);
		}
	}
	
	
}
