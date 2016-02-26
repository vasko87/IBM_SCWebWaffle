/**
 * 
 */
package com.ibm.salesconnect.model.standard.Collab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

/**
 * @author evafarrell
 * @date Nov 20th, 2013
 */
public class ClientMicroBloggingFrame extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(ClientMicroBloggingFrame.class);

	/**
	 * @param exec
	 */
	public ClientMicroBloggingFrame(Executor exec) {
		super(exec);		
		Assert.assertTrue(isPageLoaded(), "MicroBlog Frame has not loaded within 60 seconds");
	}


	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}
	
	String eventTitle = "";
	//selectors
	public static String activtyStreamFrame = "//iframe[@title='Updates']";
	public static String activityStreamEnable = "//a[contains(text(),'Click here')]";
	public static String iframeLoaded = "//div[@id='lotusFrame']";
	public static String enableActivityStreamButton = "//input[@id='authBtn']"; 
	public static String pageLoaded = "//a[@class='lotusFeed lotusAction']";
	public static String firstEvent = "//ul[@class='lotusStream']/li[1]/div[1]";
	public static String refresh = "//a[@id='com_ibm_social_as_gadget_refresh_RefreshButton_0']";
	public static String embeddedMicroBlogs = "//div[@id='eeDialoggadget_MicroblogsActivityStream-eeDiv-3_underlay']";
	public String like(String status) { return "//div[contains(text(),'"+ status +"')]/../..//a[@title='Like this']";};
	public String unLike(String status) { return "//div[contains(text(),'"+ status +"')]/../..//a[@title='Unlike']";};
	public String status(String status) { return "//div[contains(text(),'"+ status +"')]";};
	public String getLikeCount(String status) {return getObjectAttribute("//div[contains(text(),'"+ status +"')]/../..//a[@class='lotusLikeCount']","title");};

	
	public void refreshEventsStream(){
		sleep(5);
		click(refresh);
	}
		
	public boolean verifyStatus(String Status){
		sleep(5);
		return isPresent(status(Status));
	}
	
	public void likeStatus(String status){
		click(like(status));
		log.info("Comment Liked");
	}
	
	public void unLikeStatus(String status){
		click(unLike(status));
		log.info("Comment unLiked");
	}
	
	public void verifyLikeCount(String status, Integer expectedLikeCount){
		char likeCounter = getLikeCount(status).charAt(0);
		String likeCount = String.valueOf(likeCounter);
		Integer intLikeCount = Integer.valueOf(likeCount);
		Assert.assertEquals(intLikeCount, expectedLikeCount, "Like Count is " +intLikeCount + ". Expected value is " +expectedLikeCount);
	}
	
}
