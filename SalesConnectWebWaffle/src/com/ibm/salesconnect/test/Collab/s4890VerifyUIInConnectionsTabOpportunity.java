package com.ibm.salesconnect.test.Collab;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.AddACommunityPopup;
import com.ibm.salesconnect.model.partials.ConnectionsCommunityTab;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Connections.ConnectionsLogin;
import com.ibm.salesconnect.model.standard.Connections.Activities.CreateCommunityActivityPage;
import com.ibm.salesconnect.model.standard.Connections.Blogs.CreateCommunityBlogPage;
import com.ibm.salesconnect.model.standard.Connections.Bookmarks.CreateCommunityBookmarkPage;
import com.ibm.salesconnect.model.standard.Connections.Forums.CreateCommunityForumPage;
import com.ibm.salesconnect.model.standard.Connections.Wikis.CreateCommunityWikiPage;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Opportunity;
import com.ibm.salesconnect.objects.Connections.Community;
import com.ibm.salesconnect.objects.Connections.CommunityActivity;
import com.ibm.salesconnect.objects.Connections.CommunityBlog;
import com.ibm.salesconnect.objects.Connections.CommunityBookmark;
import com.ibm.salesconnect.objects.Connections.CommunityForum;
import com.ibm.salesconnect.objects.Connections.CommunityWiki;

/**
 * @author evafarrell
 * @date July 16, 2013
 */


public class s4890VerifyUIInConnectionsTabOpportunity extends ProductBaseTest {
	
	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String opptyID = null;
	String clientID = null;
	SugarAPI sugarAPI = new SugarAPI();
	Opportunity oppt = new Opportunity();

	@Test(groups = {})
	public void Test_s4890VerifyUIInConnectionsTabOpportunity() {
		Logger log = LoggerFactory.getLogger(s4890VerifyUIInConnectionsTabOpportunity.class);
		log.info("Start of test method s4890VerifyUIInConnectionsTabOpportunity");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();

		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		oppt.sOpptNumber = opptyID;
		oppt.sPrimaryContact = "ContactFirst ContactLast";
		oppt.sAccID = clientID;
		
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

		try {

			log.info("Logging in to SalesConnect");
			Dashboard dashboard = launchWithLogin(user1);
			
			log.info("Open Opportunity detail page");
			ViewOpportunityPage viewOpportunityPage = dashboard.openViewOpportunity();
			viewOpportunityPage.searchForOpportunity(oppt);
			OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
			
			log.info("Verify Connections Community Tab");
			ConnectionsCommunityTab connectionsCommunityTab = opportunityDetailPage.openConnectionsCommunityTab();			
			connectionsCommunityTab.verifyCommunityTab(user1.getEmail(), user1.getPassword());
									
			log.info("Create an entry in each part of the Community");
			log.info("Create Forum");
			connectionsCommunityTab.openForumsArea();
			CreateCommunityForumPage createCommunityForumPage = connectionsCommunityTab.createForum();
			if(connectionsCommunityTab.connectionsLoginRequired()){
				ConnectionsLogin cnxnLoginPage = new ConnectionsLogin(exec);
				cnxnLoginPage.login(user1.getEmail(), user1.getPassword());
	  		}
			createCommunityForumPage.enterCommunityForumInfo(Forum);
			createCommunityForumPage.saveCommunityForum();
			exec.close();
			
			log.info("Verify Forum Exists in Opportunity");
			exec.switchToFirstMatchingWindowByPageTitle(oppt.sOpptNumber);
			Assert.assertTrue(connectionsCommunityTab.VerifyForumsArea(Forum.sForumTopicTitle, user1.getDisplayName()),"Forum Not found in Oppty Connections Community Tab");
			
			log.info("Create Bookmark");
			connectionsCommunityTab.openBookmarksArea();
			CreateCommunityBookmarkPage createCommunityBookmarkPage = connectionsCommunityTab.createBookmark();
			exec.switchToFirstMatchingWindowByPageTitle("Bookmarks");
			createCommunityBookmarkPage.enterCommunityBookmarkInfo(Bookmark);
			createCommunityBookmarkPage.saveCommunityBookmark();
			exec.close();
			
			log.info("Verify Bookmark Exists in Opportunity");
			exec.switchToFirstMatchingWindowByPageTitle(oppt.sOpptNumber);
			Assert.assertTrue(connectionsCommunityTab.verifyBookmarksArea(Bookmark.sBookmarkName,user1.getDisplayName()),"Bookmark Not found in Oppty Connections Community Tab");
			
			log.info("Create Activity");
			connectionsCommunityTab.openActivitiesArea();
			CreateCommunityActivityPage createCommunityActivityPage = connectionsCommunityTab.createActivity();
			exec.switchToFirstMatchingWindowByPageTitle("Activity");
			createCommunityActivityPage.enterCommunityActivityInfo(Activity);
			createCommunityActivityPage.saveCommunityActivity();
			exec.close();
			
			log.info("Verify Activity Exists in Opportunity");
			exec.switchToFirstMatchingWindowByPageTitle(oppt.sOpptNumber);
			Assert.assertTrue(connectionsCommunityTab.verifyActivitiesArea(Activity.sActivityTitle, user1.getDisplayName()),"Activity Not found in Oppty Connections Community Tab");
			
			log.info("Create Wiki Page");
			connectionsCommunityTab.openWikisArea();
			CreateCommunityWikiPage createCommunityWikiPage = connectionsCommunityTab.createWikiPage();
			exec.switchToFirstMatchingWindowByPageTitle("Create a Page");
			createCommunityWikiPage.enterCommunityWikiInfo(Wiki);
			createCommunityWikiPage.saveCommunityWiki();
			exec.close();
			
			log.info("Verify Wiki Page Exists in Opportunity");
			exec.switchToFirstMatchingWindowByPageTitle(oppt.sOpptNumber);
			Assert.assertTrue(connectionsCommunityTab.verifyWikisArea(Wiki.sWikiTitle, user1.getDisplayName()),"Wiki Page Not found in Oppty Connections Community Tab");
			
			log.info("Create Blog");
			connectionsCommunityTab.openBlogsArea();
			CreateCommunityBlogPage createCommunityBlogPage = connectionsCommunityTab.createBlog();
			exec.switchToFirstMatchingWindowByPageTitle("Blogs");
			createCommunityBlogPage.enterCommunityBlogInfo(Blog);
			createCommunityBlogPage.postCommunityBlogEntry();
			exec.close();
			
			log.info("Verify Blog Exists in Opportunity");
			exec.switchToFirstMatchingWindowByPageTitle(oppt.sOpptNumber);
			Assert.assertTrue(connectionsCommunityTab.verifyBlogsArea(Blog.sBlogEntryTitle, user1.getDisplayName()),"Blog Not found in Oppty Connections Community Tab");
			
			log.info("Add Related Community");
			connectionsCommunityTab.openRelatedCommunitiesArea();
			AddACommunityPopup addACommunityPopup = connectionsCommunityTab.addACommunity();
			addACommunityPopup.selectCommunity();
			String sRelatedCommunmity = addACommunityPopup.getdisplayedCommunityName();
			addACommunityPopup.closeAddCommunityPopup();
			
			log.info("Verify Related Community Exists in Opportunity");
			exec.switchToFirstMatchingWindowByPageTitle(oppt.sOpptNumber);
			Assert.assertTrue(connectionsCommunityTab.verifyRelatedCommunitiesArea(sRelatedCommunmity, user1.getDisplayName()),"Community Not found in Oppty Connections Community Tab");
					
		} catch (Exception e) {
			e.printStackTrace();

			log.info("Remove the oppty created for this test");
			sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
			sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
			
			Assert.assertTrue(false);
		}
		log.info("End of test method s4890VerifyUIInConnectionsTabOpportunity");
		

		log.info("Remove the oppty created for this test");
		sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		
		commonUserAllocator.checkInAllUsersWithToken(this);
		commonUserAllocator.checkInAllGroupUsersWithToken(GC.db2UserGroup,this);
	}
}
