package com.ibm.salesconnect.test.Level1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Connections.Activities.CreateCommunityActivityPage;
import com.ibm.salesconnect.model.standard.Connections.Blogs.CreateCommunityBlogPage;
import com.ibm.salesconnect.model.standard.Connections.Bookmarks.CreateCommunityBookmarkPage;
import com.ibm.salesconnect.model.standard.Connections.Community.CommunityDetailPage;
import com.ibm.salesconnect.model.standard.Connections.Community.CommunityMainPage;
import com.ibm.salesconnect.model.standard.Connections.Community.CreateCommunityPage;
import com.ibm.salesconnect.model.standard.Connections.Forums.CreateCommunityForumPage;
import com.ibm.salesconnect.model.standard.Connections.Wikis.CreateCommunityWikiPage;

import com.ibm.salesconnect.objects.Connections.*;

public class BVT_Connections extends ProductBaseTest {

	Logger log = LoggerFactory.getLogger(BVT_Connections.class);

	@Test(groups = { "Level1","AT"})
	public void Test_BVT_Connections(){

		log.info("Start of test method BVT_Connections");
		User user1 = commonUserAllocator.getUser(this);

		try {
		Community community = new Community();
		CommunityActivity communityActivity = new CommunityActivity();
		CommunityBlog communityBlog = new CommunityBlog();
		CommunityBookmark communityBookmark = new CommunityBookmark();
		CommunityForum communityForum = new CommunityForum();
		CommunityWiki communityWiki = new CommunityWiki();
		
		community.populate();
		communityActivity.populate();
		communityBlog.populate();
		communityBookmark.populate();
		communityForum.populate();
		communityWiki.populate();
				
		log.info("Logging in to Connections Server");
		CommunityMainPage communityMainPage = launchCnxnCommunity(user1);
		
		log.info("Creating Community");
		CreateCommunityPage createCommunityPage = communityMainPage.startACommunity();
		createCommunityPage.enterCommunityInfo(community);
		createCommunityPage.saveCommunity();
		
		log.info("Verify Community Exists");
		Assert.assertEquals(createCommunityPage.getdisplayedCommunityName(),community.sCommunityName,"Community "+community.sCommunityName+" Not Found");
		
		log.info("Customize the Community - Add Activity, Blog & Wiki Widget");
		CommunityDetailPage communityDetailPage = communityMainPage.customizeCommunity();
		communityDetailPage.AddWidgetsToCommunity();
		
		log.info("Create Activity in the Community");
		CreateCommunityActivityPage createCommunityActivityPage = communityDetailPage.CreateYourFirstActivity();
		createCommunityActivityPage.enterCommunityActivityInfo(communityActivity);
		createCommunityActivityPage.saveCommunityActivity();

		log.info("Verify Activity Exists in the Community");
		communityDetailPage.GoToOverviewPageView();
		Assert.assertTrue(exec.isTextPresent(communityActivity.sActivityTitle), "Incorrect Community Activity Displayed");
		//Assert.assertEquals(createCommunityActivityPage.getdisplayedCommunityActivityName(),communityActivity.sActivityTitle,"Incorrect Community Activity Displayed");
	
		log.info("Create Blog in the Community");
		CreateCommunityBlogPage createCommunityBlogPage = communityDetailPage.CreateYourFirstEntry();
		createCommunityBlogPage.enterCommunityBlogInfo(communityBlog);
		createCommunityBlogPage.postCommunityBlogEntry();

		log.info("Verify Blog Exists in the Community");
		communityDetailPage.GoToOverviewPageView();
		Assert.assertEquals(createCommunityBlogPage.getdisplayedCommunityBlogEntryName(),communityBlog.sBlogEntryTitle,"Incorrect Community Blog Entry Displayed");

		log.info("Create Bookmark in the Community");
		CreateCommunityBookmarkPage createCommunityBookmarkPage = communityDetailPage.AddYourFirstBookmark();
		createCommunityBookmarkPage.enterCommunityBookmarkInfo(communityBookmark);
		createCommunityBookmarkPage.saveCommunityBookmark();

		log.info("Verify Bookmark Exists in the Community");
		communityDetailPage.GoToOverviewPageView();
		Assert.assertEquals(createCommunityBookmarkPage.getdisplayedCommunityBookmarkName(),communityBookmark.sBookmarkName,"Incorrect Community Bookmark Displayed");
		
		log.info("Create Forum in the Community");
		CreateCommunityForumPage createCommunityForumPage = communityDetailPage.StartTheFirstTopic();
		createCommunityForumPage.enterCommunityForumInfo(communityForum);
		createCommunityForumPage.saveCommunityForum();

		log.info("Verify Forum Exists in the Community");
		communityDetailPage.GoToOverviewPageView();
		if(!communityMainPage.checkForumPresent()){
			communityMainPage.searchAndOpenCommunity(community.sCommunityName, testConfig.getParameter(GC.cxnURL)+"/communities");	
		}
		communityDetailPage.GoToOverviewPageView();
		Assert.assertEquals(createCommunityForumPage.getdisplayedCommunityForumName(),communityForum.sForumTopicTitle,"Incorrect Community Forum Displayed");

		log.info("Create Wiki in the Community");
		CreateCommunityWikiPage createCommunityWikiPage = communityDetailPage.CreateAWikiPage();
		createCommunityWikiPage.enterCommunityWikiInfo(communityWiki);
		createCommunityWikiPage.saveCommunityWiki();

		log.info("Verify Wiki Exists in the Community");
		communityDetailPage.GoToOverviewPageView();
		if(!communityMainPage.checkWikiPresent()){
			communityMainPage.searchAndOpenCommunity(community.sCommunityName, testConfig.getParameter(GC.cxnURL)+"/communities");	
		}
		communityDetailPage.GoToOverviewPageView();
		Assert.assertEquals(createCommunityWikiPage.getdisplayedCommunityWikiName(),communityWiki.sWikiTitle,"Incorrect Community Wiki Displayed");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		commonUserAllocator.checkInAllUsersWithToken(this);
		log.info("End of test method BVT_Connections");

	}
}
