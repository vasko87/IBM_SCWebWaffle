/**
 * 
 */
package com.ibm.salesconnect.API;

import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.common.HttpUtils;

/**
 * @author timlehane
 * @date Aug 6, 2013
 */
public class CollabWebAPI {
	Logger log = LoggerFactory.getLogger(CollabWebAPI.class);

	public String getOAuth2Token(String URL, String userName, String password){
		return getOAuth2Token(URL, userName, password, "200");
	}

	public String getOAuth2Token(String URL, String userName, String password, String expectedResponse){
		String contentType = "application/x-www-form-urlencoded";
		String body = "{\"grant_type\":\"password\",\"username\":\"" + userName + "\",\"password\":\"" + password + "\",\"client_id\":\"sugar\",\"platform\":\"mobile\",\"client_secret\":\"\"}";
		//,\"client_info\":{\"app\":{\"name\":\"nomad\",\"isNative\":false,\"version\":\"2.2.0.15009\",\"build\":\"{build}\"},\"device\":{},\"browser\":{\"webkit\":true,\"version\":\"600.2.5\",\"userAgent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/600.2.5 (KHTML, like Gecko) Version/8.0.2 Safari/600.2.5\"}}}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(URL + GC.authURLExtension,GC.emptyArray,body,contentType, expectedResponse);
		
		JSONObject postResponse=null;
		try {
			postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e5) {
			e5.printStackTrace();
			return "false";
		}
		
		String token = "";
		try {
			token = (String) postResponse.get("access_token");
		} catch (Exception e) {
			log.info("Token was not returned as expected");
			return postResponseString;
		}
		log.info("Token returned: " + token);
		return token;
	}
	
	public String failToGetOAuth2Token(String URL, String userName, String password, String expectedResponse){
		String contentType = "application/x-www-form-urlencoded";
		String body = "{\"grant_type\":\"password\",\"username\":\"" + userName + "\",\"password\":\"" + password + "\",\"client_id\":\"sugar\",\"platform\":\"mobile\",\"client_secret\":\"\",\"client_info\":{\"app\":{\"name\":\"nomad\",\"isNative\":false,\"version\":\"2.3.1\",\"build\":\"{build}\"},\"device\":{},\"browser\":{\"webkit\":true,\"version\":\"600.2.5\",\"userAgent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/600.2.5 (KHTML, like Gecko) Version/8.0.2 Safari/600.2.5\"}}}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(URL + GC.authURLExtension,GC.emptyArray,body,contentType, expectedResponse);
		
		return postResponseString;
	}
	
	
	public Boolean checkSubscribeCommunityStatus(String URL, String[] headers, String beanId, String module){
		return checkSubscribeCommunityStatus(URL, headers, "", beanId, module);
	}
	
	public Boolean checkSubscribeCommunityStatus(String URL, String[] headers, String community, String beanId, String module){
		URL += GC.checkCommunityStatusExtension+"?";
		if (!community.equalsIgnoreCase("")) {
			URL += "community=" + community + "&";
		}
		URL += "beanId=" + beanId + "&module=" + module;
		
		log.info("Sending GET request: " + URL);
		HttpUtils httpUtils = new HttpUtils();
		String getResponseString = httpUtils.getRequest(URL,headers);
		log.info("Received GET response: "+getResponseString);
		JSONObject getResponse=null;
		try {
			getResponse = (JSONObject)new JSONParser().parse(getResponseString);
		} catch (ParseException e4) {
			e4.printStackTrace();
		}
		
		Boolean subscribedCommunities = false;
		try {
			subscribedCommunities = (Boolean) getResponse.get("subscribed");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Community subscription information was not returned as expected");
		}
		
		return subscribedCommunities;
	}
	
	public void subscribeCommunity(String URL, String[] headers, String beanId, String module){
		subscribeCommunity(URL, headers, "", beanId, module);
	}
	
	public void subscribeCommunity(String URL, String[] headers, String community, String beanId, String module){
		String contentType = "application/x-www-form-urlencoded";
		
		String body = "{";
		if (!community.equalsIgnoreCase("")) {
			body += "\"community\": \"" + community + "\", ";
		}
		body += "\"beanId\": \"" + beanId + "\", \"module\": \"" + module + "\"}";
		
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(URL + GC.subscribeToCommunityExtension,headers,body,contentType);
		JSONObject postResponse=null;
		try {
			postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e5) {
			e5.printStackTrace();
			Assert.assertTrue(false, "Response from community subscription attempt is not readable");
		}
		
		Assert.assertTrue((Boolean) postResponse.get("success"), "Community subscription was not successful"); 
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void unsubscribeCommunity(String URL, String[] headers, String beanId, String module){
		unsubscribeCommunity(URL, headers, "", beanId, module);
	}
	
	public void unsubscribeCommunity(String URL, String[] headers, String community, String beanId, String module){
		String contentType = "application/x-www-form-urlencoded";
		
		String body = "{";
		if (!community.equalsIgnoreCase("")) {
			body += "\"community\": \"" + community + "\", ";
		}
		body += "\"beanId\": \"" + beanId + "\", \"module\": \"" + module + "\"}";
		
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(URL + GC.unsubscribeCommunityExtension,headers,body,contentType);
		JSONObject postResponse=null;
		try {
			postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e5) {
			e5.printStackTrace();
			Assert.assertTrue(false, "Response from community subscription attempt is not readable");
		}
		
		Assert.assertTrue((Boolean) postResponse.get("success"), "Community unsubscription was not successful"); 
	}
	
	public JSONArray getListofCommunitySubscriptions(String URL, String[] headers, String communityID){

		URL += GC.listCommunitySubscriptionsExtension + "?community="+communityID;
		
		log.info("Sending GET request: " + URL);
		HttpUtils httpUtils = new HttpUtils();
		String getResponseString = httpUtils.getRequest(URL,headers);
		JSONArray getResponse=null;
		try {
			getResponse = (JSONArray)new JSONParser().parse(getResponseString);
		} catch (ParseException e4) {
			e4.printStackTrace();
		}
		return getResponse;
	}
	
	public String getcommunityIDFromBeanID(String URL, String beanId, String module, String[] headers){
		HttpUtils httpCalls = new HttpUtils();
		JSONObject response = null;
		
		String responseString = httpCalls.postRequest(URL + "custom/service/IBMConnections/rest.php", headers, 
				"method=retrieve_mapped_community_id&input_type=json&response_type=json&rest_data={\"module\":\""+module+"\",\"beanId\":\""+beanId+"\"} ","application/x-www-form-urlencoded");
		try {
			response = (JSONObject)new JSONParser().parse(responseString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		log.info("Post response: " + response);
		return (String) response.get("data");
	}
	
	public Boolean joinCommunity(String URL, String beanId, String module, String[] headers){
		HttpUtils httpCalls = new HttpUtils();
		JSONObject response = null;
		
		String responseString = httpCalls.postRequest(URL + "custom/service/IBMConnections/rest.php", headers, 
				"method=add_community_member&input_type=json&response_type=json&rest_data={\"module\":\""+module+"\",\"beanId\":\""+beanId+"\"} ","application/x-www-form-urlencoded");

		log.info("Join Community Response: " + response);
		
		log.info("Checking if community join was successful");
		Pattern pattern = Pattern.compile("\\{\"data\"(.*?)");
		Matcher matcher = pattern.matcher(responseString);
		if (matcher.find())
		{
	     return true;
		}
		return false;
	}
	
	public void leaveCommunity(String URL, String communityId,String[] headers, String email){
		HttpUtils httpCalls = new HttpUtils();

		log.info("Delete request: " + URL + "communities/service/atom/community/members?communityUuid="+communityId + "&email=" + email);
		httpCalls.deleteRequest(URL + "communities/service/atom/community/members?communityUuid="+communityId + "&email=" + email, headers);
	}
	
	public Boolean checkCommunityMembership(String URL, String communityId, String[] headers, String email){
		HttpUtils httpCalls = new HttpUtils();
		httpCalls.getRequest(URL+ "communities/service/atom/community/members?communityUuid="+communityId + "&email=" + email, headers, "200");
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public Boolean isClientonSubscriptionList(JSONArray listofCommunitySubscriptions, String clientBean){
		Iterator<JSONObject> iterator = listofCommunitySubscriptions.iterator();

		while (iterator.hasNext()) {
			JSONObject json = iterator.next();
			if(json.get("module_type").equals(GC.accountsModule)){
				if (json.get("module_id").equals(clientBean)) {
					return true;
				}
			}
			
		}
		return false;
	}
	
	public JSONObject getActivityStreamEventsFilter(String URL, String[] headers, String beanID, String module){
		URL = URL + GC.activityStreamEventsFilterExtension + "?beanId="+beanID+"&module="+module;
		
		log.info("Sending GET request: " + URL);
		HttpUtils httpUtils = new HttpUtils();
		String getResponseString = httpUtils.getRequest(URL,headers);
		log.info("Received GET response: "+getResponseString);
		JSONObject getResponse=null;
		try {
			getResponse = (JSONObject)new JSONParser().parse(getResponseString);
		} catch (ParseException e4) {
			e4.printStackTrace();
		}
		return getResponse;
	}
	
	public JSONObject getActivityStreamMicroBlogFilter(String URL, String[] headers, String beanID, String module){
		URL = URL + GC.activityStreamMicroblogFilterExtension + "?beanId="+beanID+"&module="+module;
		
		log.info("Sending GET request: " + URL);
		HttpUtils httpUtils = new HttpUtils();
		String getResponseString = httpUtils.getRequest(URL,headers);
		log.info("Received GET response: "+getResponseString);
		JSONObject getResponse=null;
		try {
			getResponse = (JSONObject)new JSONParser().parse(getResponseString);
		} catch (ParseException e4) {
			e4.printStackTrace();
		}
		return getResponse;
	}
	
	/**
	 * Post to microblog of a given object client/oppty
	 * @param URL
	 * @param headers
	 * @param message
	 * @param beanID
	 * @param module
	 * @return JSONObject of response 
	 */
	public JSONObject postToMicroBlog(String URL, String[] headers, String message, String beanID, String module){
		URL = URL + GC.postToMicroBlogExtension;
		
		String contentType = "application/x-www-form-urlencoded";
		
		String body = "{";
		body += "\"message\": \"" + message + "\",\"beanId\": \"" + beanID + "\", \"module\": \"" + module + "\"}";

		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(URL,headers,body,contentType);
		JSONObject postResponse=null;
		try {
			postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e5) {
			e5.printStackTrace();
			Assert.assertTrue(false, "Response from microblog post is unreadable");
		}
		log.info("Request to create microblog post: " + body);
		log.info("Response from microblog post: " + postResponse);
		return postResponse;
	}
	
	/**
	 * Get the microblog comments for a particular post
	 * @param URL
	 * @param headers
	 * @param postID
	 * @return
	 * @throws IOException
	 */
	public String getMicroBlog(String URL,String[] headers, String postID) throws IOException{
		 
		URL+="common/opensocial/basic/rest/ublog/@all/@all/"+ postID + "/comments";
		log.info("Sending GET request: " + URL);
		HttpUtils httpUtils = new HttpUtils();
		String getResponseString = httpUtils.getRequest(URL,headers);
		log.info("Received GET response: "+getResponseString);
		JSONObject getResponse=null;
		try {
			getResponse = (JSONObject)new JSONParser().parse(getResponseString);
		} catch (ParseException e4) {
			e4.printStackTrace();
		}
		return getResponse.toString();
	}

	public Pattern activityStreamEventsFilter(String beanId, String communityId) { return Pattern.compile("\"views\":\\{\"main\":\\{(\"description\":\"\"|\"label\":\"\")(,\"description\":\"\"|,\"label\":\"\")?,\"filters\"(.*)");}
	public Pattern activityStreamEventsFilterOppty(String opptyID) { return Pattern.compile("\"views\":\\{\"main\":\\{(\"description\":\"\"|\"label\":\"\")(,\"description\":\"\"|,\"label\":\"\")?,\"filters\"(.*)");}
	//public Pattern activityStreamMicroBlogsFilter(String communityID) { return Pattern.compile("\"defaultUrlTemplateValues\":\\{\"rollup\":\"true\",\"commId\":\"urn:lsid:lconn.ibm.com:communities.community:"+communityID+"\",(.*)");}
	//public Pattern activityStreamMicroBlogsFilter(String communityID) { return Pattern.compile("\"defaultUrlTemplateValues\":\\{\"rollup\":\"true\",\"commId\":\"urn:lsid:lconn.ibm.com:communities.community:(.*)");}
	public Pattern activityStreamMicroBlogsFilter(String communityID) { return Pattern.compile("\"commId\":\"urn:lsid:lconn.ibm.com:communities.community:(.*)");}
																							//","defaultUrlTemplateValues":{"rollup":"true","commId":"urn:lsid:lconn.ibm.com:communities.community:","postId":"0"},"views":{"main":{"label":"","description":""}}}
//	public Pattern activityStreamMicroBlogsFilter(String communityID) { return Pattern.compile("\\{\"views\":\\{\"main\":\\{\"description\":\"\",\"label\":\"\"\\}\\},\"defaultUrlTemplateValues\":\\{\"rollup\":\"true\",\"commId\":\"urn:lsid:lconn.ibm.com:communities.community:"+communityID+"\",(.+)");}

	public String getbeanIDfromClientID(String URL, String clientID, String[] headers){	
		URL = URL + GC.activityStreamEventsFilterExtension + "?beanId="+clientID+"&module="+GC.accountsModule;
		
		log.info("Sending GET request: " + URL);
		HttpUtils httpUtils = new HttpUtils();
		String getResponseString = httpUtils.getRequest(URL,headers);
		log.info("Received GET response: "+getResponseString);
	
		//convert \u0027 to ' and \u0022 to "
		String parsedResponseString1 = getResponseString.replaceAll("\\\\u0027", "'");
		String parsedResponseString = parsedResponseString1.replaceAll("\\\\u0022", "\"");
		Pattern pattern = Pattern.compile("\'type\':\'tag\', \'values\' :\\[\"(.*?)\"(.*?)\\]");
		Matcher matcher = pattern.matcher(parsedResponseString);
		if (matcher.find())
		{
	     return matcher.group(1);
		}

		return "";
	}
	
	

}
