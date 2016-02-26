package com.ibm.salesconnect.test.Collab;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.AddACommunityPopup;
import com.ibm.salesconnect.model.partials.ConnectionsCommunityTab;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.ClientDetailPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.model.standard.Connections.ConnectionsLogin;
import com.ibm.salesconnect.model.standard.Connections.Activities.CreateCommunityActivityPage;
import com.ibm.salesconnect.model.standard.Connections.Blogs.CreateCommunityBlogPage;
import com.ibm.salesconnect.model.standard.Connections.Bookmarks.CreateCommunityBookmarkPage;
import com.ibm.salesconnect.model.standard.Connections.Forums.CreateCommunityForumPage;
import com.ibm.salesconnect.model.standard.Connections.Wikis.CreateCommunityWikiPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Opportunity;
import com.ibm.salesconnect.objects.Connections.Community;
import com.ibm.salesconnect.objects.Connections.CommunityActivity;
import com.ibm.salesconnect.objects.Connections.CommunityBlog;
import com.ibm.salesconnect.objects.Connections.CommunityBookmark;
import com.ibm.salesconnect.objects.Connections.CommunityForum;
import com.ibm.salesconnect.objects.Connections.CommunityWiki;

/**
 * @author evafarrell
 * @date Sept 16, 2013
 */


public class s4890VerifyUIInConnectionsTabClient extends ProductBaseTest {
	
	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String opptyID = "33SC-" + rand;
	String clientID = null;
	SugarAPI sugarAPI = new SugarAPI();
	Opportunity oppt = new Opportunity();

	@Test(groups = { "Collab","LC"})
	public void Test_s4890VerifyUIInConnectionsTabClient() {
		Logger log = LoggerFactory.getLogger(s4890VerifyUIInConnectionsTabClient.class);
		log.info("Start of test method s4890VerifyUIInConnectionsTabClient");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);

		PoolClient poolClient = commonClientAllocator.getGroupClient("DC",this);
		Client client = new Client();
		client.sClientID = poolClient.getCCMS_ID();
		client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);		
		client.sCCMS_Level = "DC";
		
		//Client variables
		client.sSearchIn= GC.showingInClientID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForClients;
		
		Community community = new Community();
		CommunityActivity Activity = new CommunityActivity();
		CommunityBlog Blog = new CommunityBlog();
		CommunityBookmark Bookmark = new CommunityBookmark();
		CommunityForum Forum = new CommunityForum();
		CommunityWiki Wiki = new CommunityWiki();
		
		community.populate();
		Activity.populate();
		Blog.populate();
		Bookmark.populate();
		Forum.populate();
		Wiki.populate();

			log.info("Logging in to SalesConnect");
			Dashboard dashboard = launchWithLogin(user1);
			
			log.info("Open Client detail page");
			ViewClientPage viewClientPage = dashboard.openViewClient();
			viewClientPage.searchForClient(client);
			ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);
						
			log.info("Verify Connections Community Tab");
			ConnectionsCommunityTab connectionsCommunityTab = clientDetailPage.openConnectionsCommunityTab();			
			connectionsCommunityTab.verifyCommunityTab(user1.getEmail(), user1.getPassword());
									
			log.info("Create an entry in each part of the Community");
			
			log.info("Create Bookmark");
			connectionsCommunityTab.openBookmarksArea();
			CreateCommunityBookmarkPage createCommunityBookmarkPage = connectionsCommunityTab.createBookmark();
			if(connectionsCommunityTab.connectionsLoginRequired()){
				ConnectionsLogin cnxnLoginPage = new ConnectionsLogin(exec);
				sleep(2);
				cnxnLoginPage.login(user1.getEmail(), user1.getPassword());
			}
			else exec.switchToFirstMatchingWindowByPageTitle("Bookmarks");
			createCommunityBookmarkPage.enterCommunityBookmarkInfo(Bookmark);
			createCommunityBookmarkPage.saveCommunityBookmark();
			
			log.info("Create Forum");
			createCommunityBookmarkPage.GoToOverviewPageView();
			CreateCommunityForumPage createCommunityForumPage = createCommunityBookmarkPage.createForum();
			createCommunityForumPage.enterCommunityForumInfo(Forum);
			createCommunityForumPage.saveCommunityForum();
			exec.close();
			
			log.info("Verify Bookmark Exists in Client");
			exec.switchToFirstMatchingWindowByPageTitle(client.sClientName);
			Assert.assertTrue(connectionsCommunityTab.verifyBookmarksArea(Bookmark.sBookmarkName,user1.getDisplayName()),"Bookmark Not found in Client Connections Community Tab");
			
			log.info("Verify Forum Exists in Client");
			Assert.assertTrue(connectionsCommunityTab.VerifyForumsArea(Forum.sForumTopicTitle, user1.getDisplayName()),"Forum Not found in Client Connections Community Tab");

			log.info("Create Activity");
			connectionsCommunityTab.openActivitiesArea();
			CreateCommunityActivityPage createCommunityActivityPage = connectionsCommunityTab.createActivity();
			if(connectionsCommunityTab.connectionsLoginRequired()){
				ConnectionsLogin cnxnLoginPage = new ConnectionsLogin(exec);
				sleep(2);
				cnxnLoginPage.login(user1.getEmail(), user1.getPassword());
			}
			else exec.switchToFirstMatchingWindowByPageTitle("Activity");
			createCommunityActivityPage.enterCommunityActivityInfo(Activity);
			createCommunityActivityPage.saveCommunityActivity();
			exec.close();
			
			log.info("Verify Activity Exists in Client");
			exec.switchToFirstMatchingWindowByPageTitle(client.sClientName);
			Assert.assertTrue(connectionsCommunityTab.verifyActivitiesArea(Activity.sActivityTitle, user1.getDisplayName()),"Activity Not found in Client Connections Community Tab");
			
			log.info("Create Wiki Page");
			connectionsCommunityTab.openWikisArea();
			CreateCommunityWikiPage createCommunityWikiPage = connectionsCommunityTab.createWikiPage();
			if(connectionsCommunityTab.connectionsLoginRequired()){
				ConnectionsLogin cnxnLoginPage = new ConnectionsLogin(exec);
				sleep(2);
				cnxnLoginPage.login(user1.getEmail(), user1.getPassword());
			}
			else exec.switchToFirstMatchingWindowByPageTitle("Create a Page");
			createCommunityWikiPage.enterCommunityWikiInfo(Wiki);
			createCommunityWikiPage.saveCommunityWiki();
			exec.close();
			
			log.info("Verify Wiki Page Exists in Client");
			exec.switchToFirstMatchingWindowByPageTitle(client.sClientName);
			Assert.assertTrue(connectionsCommunityTab.verifyWikisArea(Wiki.sWikiTitle, user1.getDisplayName()),"Wiki Page Not found in Client Connections Community Tab");
			
			log.info("Create Blog");
			connectionsCommunityTab.openBlogsArea();
			CreateCommunityBlogPage createCommunityBlogPage = connectionsCommunityTab.createBlog();
			if(connectionsCommunityTab.connectionsLoginRequired()){
				ConnectionsLogin cnxnLoginPage = new ConnectionsLogin(exec);
				sleep(2);
				cnxnLoginPage.login(user1.getEmail(), user1.getPassword());
			}
			else exec.switchToFirstMatchingWindowByPageTitle("Blogs");
			createCommunityBlogPage.enterCommunityBlogInfo(Blog);
			createCommunityBlogPage.postCommunityBlogEntry();
			exec.close();
			
			log.info("Verify Blog Exists in Client");
			exec.switchToFirstMatchingWindowByPageTitle(client.sClientName);
			Assert.assertTrue(connectionsCommunityTab.verifyBlogsArea(Blog.sBlogEntryTitle, user1.getDisplayName()),"Blog Not found in Client Connections Community Tab");
			
			log.info("Add Related Community");
			connectionsCommunityTab.openRelatedCommunitiesArea();
			AddACommunityPopup addACommunityPopup = connectionsCommunityTab.addACommunity();
			addACommunityPopup.selectCommunity();
			String sRelatedCommunmity = addACommunityPopup.getdisplayedCommunityName();
			addACommunityPopup.closeAddCommunityPopup();
			
			log.info("Verify Related Community Exists in Client");
			Assert.assertTrue(connectionsCommunityTab.verifyRelatedCommunitiesArea(sRelatedCommunmity, user1.getDisplayName()),"Community Not found in Client Connections Community Tab");

		log.info("End of test method s4890VerifyUIInConnectionsTabClient");
		
		commonUserAllocator.checkInAllUsersWithToken(this);
		commonUserAllocator.checkInAllGroupUsersWithToken(GC.db2UserGroup,this);
	}
}
