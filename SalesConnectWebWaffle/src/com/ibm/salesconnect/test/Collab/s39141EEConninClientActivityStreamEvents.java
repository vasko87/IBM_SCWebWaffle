package com.ibm.salesconnect.test.Collab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.ConnectionsCommunityTab;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.ClientDetailPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.model.standard.Collab.ClientActivityStreamFrame;
import com.ibm.salesconnect.model.standard.Collab.EmbeddedExperience;
import com.ibm.salesconnect.model.standard.Connections.Activities.CreateCommunityActivityPage;
import com.ibm.salesconnect.model.standard.Connections.Blogs.CreateCommunityBlogPage;
import com.ibm.salesconnect.model.standard.Connections.Bookmarks.CreateCommunityBookmarkPage;
import com.ibm.salesconnect.model.standard.Connections.Forums.CreateCommunityForumPage;
import com.ibm.salesconnect.model.standard.Connections.Wikis.CreateCommunityWikiPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Connections.CommunityActivity;
import com.ibm.salesconnect.objects.Connections.CommunityBlog;
import com.ibm.salesconnect.objects.Connections.CommunityBookmark;
import com.ibm.salesconnect.objects.Connections.CommunityForum;
import com.ibm.salesconnect.objects.Connections.CommunityWiki;

/**
 * @author evafarrell
 * @date Mar 14, 2014
 */


public class s39141EEConninClientActivityStreamEvents extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(s39141EEConninClientActivityStreamEvents.class);
		
	@Test(groups = { "Collab","LC"})
	public void test_s39141EEConninClientActivityStreamEvents() {
		
		log.info("Start of test method s39141EEConninClientActivityStreamEvents");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		PoolClient poolClient = commonClientAllocator.getGroupClient("DC",this);
		Client client = new Client();
		client.sClientID = poolClient.getCCMS_ID();
		client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);		
		client.sCCMS_Level = "DC";

		CommunityActivity Activity = new CommunityActivity();
		CommunityBlog Blog = new CommunityBlog();
		CommunityBookmark Bookmark = new CommunityBookmark();
		CommunityForum Forum = new CommunityForum();
		CommunityWiki Wiki = new CommunityWiki();
		
		Activity.populate();
		Blog.populate();
		Bookmark.populate();
		Forum.populate();
		Wiki.populate();
		
		//Client variables
		client.sSearchIn= GC.showingInClientID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForClients;

			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);

			log.info("Open Client detail page");
			ViewClientPage viewClientPage = dashboard.openViewClient();
			viewClientPage.searchForClient(client);
			ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);
						
			log.info("Open Updates Tab for Client and follow Client");
			clientDetailPage.clientUpdates();
			clientDetailPage.followClient();
			
			log.info("Add Status Update");
			String Status = "My Status "+GC.sUniqueNum;
			clientDetailPage.postStatus(Status);
			
//			log.info("Verify Status Update Event in Client Activity Stream");
//			String event = user1.getDisplayName()+" commented on fnscdi@us.ibm.com's message";
//			String embeddedExpEvent = client.sClientName+"("+client.sClientID+")";
//			ClientActivityStreamFrame clientActivityStreamFrame = dashboard.switchToClientActivityStreamFrame();
//			clientDetailPage.selectConnectionsEvents();			
//			//Assert.assertTrue(clientActivityStreamFrame.verifyEntry(event), "Event "+event+" does not exist in Activity Stream");
//			log.info("Verify Updates Tab - Activity Stream - Expand Entries");
//			log.info("Opening first event in activity stream");
//			EmbeddedExperience embeddedExperience = clientActivityStreamFrame.openFirstEventsEmbeddedExperience();
//			log.info("Verifying embedded experience");
//			Assert.assertEquals(embeddedExperience.verifyStatusEE(),embeddedExpEvent, "EE title does not match event title from Activity Stream");
//			embeddedExperience.switchToMainWindow();
//			embeddedExperience.close();
			
			log.info("Create Bookmark");
			dashboard.switchToMainWindow();
			ConnectionsCommunityTab connectionsCommunityTab = clientDetailPage.openConnectionsCommunityTab();		
			connectionsCommunityTab.openBookmarksArea();
			CreateCommunityBookmarkPage createCommunityBookmarkPage = connectionsCommunityTab.createBookmark();
			exec.switchToFirstMatchingWindowByPageTitle("Bookmarks");
			createCommunityBookmarkPage.enterCommunityBookmarkInfo(Bookmark);
			createCommunityBookmarkPage.saveCommunityBookmark();
			
			log.info("Create Forum");
			createCommunityBookmarkPage.GoToOverviewPageView();
			CreateCommunityForumPage createCommunityForumPage = createCommunityBookmarkPage.createForum();
			createCommunityForumPage.enterCommunityForumInfo(Forum);
			createCommunityForumPage.saveCommunityForum();
			exec.close();
			exec.switchToFirstMatchingWindowByPageTitle(client.sClientName);

			log.info("Verify Forum Event in Client Activity Stream");
			String event = user1.getDisplayName()+" created a topic named "+ Forum.sForumTopicTitle;
			String embeddedExpEvent = Forum.sForumTopicTitle;
			clientDetailPage.clientUpdates();
			ClientActivityStreamFrame clientActivityStreamFrame = dashboard.switchToClientActivityStreamFrame();
			clientDetailPage.selectConnectionsEvents();
			clientActivityStreamFrame.refreshEventsStream();
			Assert.assertTrue(clientActivityStreamFrame.verifyEntry(event), "Event "+event+" does not exist in Activity Stream");
			log.info("Verify Updates Tab - Activity Stream - Expand Entries");
			log.info("Opening first event in activity stream");
			EmbeddedExperience embeddedExperience = clientActivityStreamFrame.openFirstEventsEmbeddedExperience();
			log.info("Verifying embedded experience");
			Assert.assertEquals(embeddedExperience.getEmbeddedExperienceTitle(),embeddedExpEvent, "EE title does not match event title from Activity Stream");
			embeddedExperience.switchToMainWindow();
			embeddedExperience.close();
			
			log.info("Verify Bookmark Event in Client Activity Stream");
			event = user1.getDisplayName()+" added the "+ Bookmark.sBookmarkName+" bookmark";
			embeddedExpEvent = Bookmark.sBookmarkName;
			clientDetailPage.clientUpdates();
			dashboard.switchToClientActivityStreamFrame();
			clientDetailPage.selectConnectionsEvents();
			clientActivityStreamFrame.refreshEventsStream();
			Assert.assertTrue(clientActivityStreamFrame.verifyEntry(event), "Event "+event+" does not exist in Activity Stream");
			log.info("Verify Updates Tab - Activity Stream - Expand Entries");
			log.info("Opening second event in activity stream");
			clientActivityStreamFrame.openSecondEventsEmbeddedExperience();
			log.info("Verifying embedded experience");
			Assert.assertEquals(embeddedExperience.getEmbeddedExperienceTitle(),embeddedExpEvent, "EE title does not match event title from Activity Stream");
			embeddedExperience.switchToMainWindow();
			embeddedExperience.close();
									
			log.info("Create Activity");
			dashboard.switchToMainWindow();
			clientDetailPage.openConnectionsCommunityTab();
			CreateCommunityActivityPage createCommunityActivityPage = connectionsCommunityTab.createActivity();
			exec.switchToFirstMatchingWindowByPageTitle("Activity");
			createCommunityActivityPage.enterCommunityActivityInfo(Activity);
			createCommunityActivityPage.saveCommunityActivity();
			exec.close();
			exec.switchToFirstMatchingWindowByPageTitle(client.sClientName);
			
			log.info("Verify Activity Event in Client Activity Stream");
			event = user1.getDisplayName()+" created an activity named "+ Activity.sActivityTitle;
			embeddedExpEvent = Activity.sActivityTitle;
			clientDetailPage.clientUpdates();
			dashboard.switchToClientActivityStreamFrame();
			clientActivityStreamFrame.refreshEventsStream();
			Assert.assertTrue(clientActivityStreamFrame.verifyEntry(event), "Event "+event+" does not exist in Activity Stream");
			log.info("Verify Updates Tab - Activity Stream - Expand Entries");
			log.info("Opening first event in activity stream");
			clientActivityStreamFrame.openFirstEventsEmbeddedExperience();
			log.info("Verifying embedded experience");
			Assert.assertEquals(embeddedExperience.getEmbeddedExperienceTitle(),embeddedExpEvent, "EE title does not match event title from Activity Stream");
			embeddedExperience.switchToMainWindow();
			embeddedExperience.close();
			
			log.info("Create Wiki Page");
			dashboard.switchToMainWindow();
			log.info("Open Client detail page");
			dashboard.openViewClient();
			viewClientPage.searchForClient(client);
			viewClientPage.selectResult(client);
			clientDetailPage.openConnectionsCommunityTab();
			connectionsCommunityTab.openWikisArea();
			CreateCommunityWikiPage createCommunityWikiPage = connectionsCommunityTab.createWikiPage();
			exec.switchToFirstMatchingWindowByPageTitle("Create a Page");
			createCommunityWikiPage.enterCommunityWikiInfo(Wiki);
			createCommunityWikiPage.saveCommunityWiki();
			exec.close();
			exec.switchToFirstMatchingWindowByPageTitle(client.sClientName);
			
			log.info("Verify Wiki Event in Client Activity Stream");
			event = user1.getDisplayName()+" created a wiki page named "+ Wiki.sWikiTitle;
			embeddedExpEvent = Wiki.sWikiTitle;
			clientDetailPage.clientUpdates();
			dashboard.switchToClientActivityStreamFrame();
			clientDetailPage.selectConnectionsEvents();
			clientActivityStreamFrame.refreshEventsStream();
			Assert.assertTrue(clientActivityStreamFrame.verifyEntry(event), "Event "+event+" does not exist in Activity Stream");
			log.info("Verify Updates Tab - Activity Stream - Expand Entries");
			log.info("Opening first event in activity stream");
			clientActivityStreamFrame.openFirstEventsEmbeddedExperience();
			log.info("Verifying embedded experience");
			Assert.assertEquals(embeddedExperience.getEmbeddedExperienceTitle(),embeddedExpEvent, "EE title does not match event title from Activity Stream");
			embeddedExperience.switchToMainWindow();
			embeddedExperience.close();
			
			log.info("Create Blog");
			dashboard.switchToMainWindow();
			log.info("Open Client detail page");
			dashboard.openViewClient();
			viewClientPage.searchForClient(client);
			viewClientPage.selectResult(client);
			clientDetailPage.openConnectionsCommunityTab();
			connectionsCommunityTab.openBlogsArea();
			CreateCommunityBlogPage createCommunityBlogPage = connectionsCommunityTab.createBlog();
			exec.switchToFirstMatchingWindowByPageTitle("Blogs");
			createCommunityBlogPage.enterCommunityBlogInfo(Blog);
			createCommunityBlogPage.postCommunityBlogEntry();
			exec.close();
			exec.switchToFirstMatchingWindowByPageTitle(client.sClientName);
			
			log.info("Verify Blog Event in Client Activity Stream");
			event = user1.getDisplayName()+" created a blog entry named "+ Blog.sBlogEntryTitle;
			embeddedExpEvent = Blog.sBlogEntryTitle;
			clientDetailPage.clientUpdates();
			dashboard.switchToClientActivityStreamFrame();
			clientDetailPage.selectConnectionsEvents();
			clientActivityStreamFrame.refreshEventsStream();
			Assert.assertTrue(clientActivityStreamFrame.verifyEntry(event), "Event "+event+" does not exist in Activity Stream");
			log.info("Verify Updates Tab - Activity Stream - Expand Entries");
			log.info("Opening first event in activity stream");
			clientActivityStreamFrame.openFirstEventsEmbeddedExperience();
			log.info("Verifying embedded experience");
			Assert.assertEquals(embeddedExperience.getEmbeddedExperienceTitle(),embeddedExpEvent, "EE title does not match event title from Activity Stream");
			embeddedExperience.switchToMainWindow();
			embeddedExperience.close();
						
		log.info("End of test method s39141EEConninClientActivityStreamEvents");
		
		commonUserAllocator.checkInAllUsersWithToken(this);
		commonUserAllocator.checkInAllGroupUsersWithToken(GC.db2UserGroup,this);
	}
}
