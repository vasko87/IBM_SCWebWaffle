package com.ibm.salesconnect.API;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.apache.http.Header;

import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.common.HttpUtils;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;


/**
 * @author kvnlau
 * @date April 08, 2015
 */

public class ConnectionsCommunityAPI extends ProductBaseTest
{
	Logger log = LoggerFactory.getLogger(LoginRestAPI.class);
	private String contentType = "text/xml";
	public String funcIdEmail = "fnscdi@us.ibm.com";
	
	/**
	 * Creates connection community
	 * @param userEmail (user authorized to access the connections API)
	 * @param userPassword
	 * @param connectionsUrl (e.g. "http://<connections hostname>/") -- 
	 * @return communityUuId (aka communityId) of new community (noting userEmail is added as owner)
	 */
	public String createConnectionsCommunity(String userEmail, String userPassword, String connectionsUrl) {
		Header[] responseHeader = null;
		String communityUuId = null;
		
		try {
			String cchUserPwd= userEmail + ":" + userPassword;
			String logon64Base = DatatypeConverter.printBase64Binary(cchUserPwd.getBytes());
			log.info("logon64Base=" + logon64Base);
			String headers[]={"Authorization", "Basic " + logon64Base};
	
			// Retrieve latest date
			DateFormat dateFormat = new SimpleDateFormat("yyy/MM/dd HH:mm:ss");
			Date date = new Date();
			
			log.info("createConnectionsCommunity(): form body of (post) create community request");		
			String body = "<entry xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" xmlns=\"http://www.w3.org/2005/Atom\"><title type=\"text\">FakeCCH - " +
							dateFormat.format(date) +
							"</title><category term=\"community\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category><category term=\"fake-cch\"></category><snx:communityType>private</snx:communityType><snx:preModeration>false</snx:preModeration><snx:postModeration>false</snx:postModeration><content type=\"html\">A fake CCH community created for testing</content></entry>";
			
			HttpUtils restCalls = new HttpUtils();
			
			// call API to create connections community
			responseHeader = restCalls.postRequestReturnHeaders(connectionsUrl + getCreateCommunityExtension(), 
															headers, body, contentType, "201");
			
			// retrieve the communityUuId of the new community (from the response header)
			// 1) cycle through all response header content checking for key: 'Location'
			// 2) when found, retrieve the communityUuId
			for (int count =0; count<responseHeader.length; count++) {
				if (responseHeader[count]!=null) {
					// if found key:'Location', process it's associated value
					if (responseHeader[count].getName().contentEquals("Location")){
						log.info("Response Header["+ String.valueOf(count) + 
								 "] (Location): " + responseHeader[count].getValue());
						
						//Setup indexes to isolate & retrieve value after string "communityUuid="
						int startIndex = responseHeader[count].getValue().lastIndexOf("communityUuid=");
						int startIndex2= responseHeader[count].getValue().indexOf("=", startIndex);
						
						log.info("Response Header["+ String.valueOf(count) + "] (communityId):" + responseHeader[count].getValue().substring(startIndex2+1) );
						communityUuId = responseHeader[count].getValue().substring(startIndex2+1);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return communityUuId;
	}

	/**
	 * Adds member to existing connection community
	 * @param userEmail (authorized user in connections)
	 * @param userPassword
	 * @param newMemberEmail -- new member for community
	 * @param communityId -- communityId of community to be updated with additional member
	 * @param connectionsUrl
	 * @return communityUuId (aka communityId) of updated community
	 */
	public String addUserConnectionsCommunity(String userEmail, String userPassword, String newMemberEmail, String communityId, String connectionsUrl) {
		Header[] responseHeader = null;
		String communityUuId = null;
		
		try {
			String cchUserPwd= userEmail + ":" + userPassword;
			String logon64Base = DatatypeConverter.printBase64Binary(cchUserPwd.getBytes());
			log.info("logon64Base=" + logon64Base);
			String headers[]={"Authorization", "Basic " + logon64Base};
			
			log.info("form body of (post) add user to community request");		
			String body ="<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\"> <contributor> <email>" + 
					newMemberEmail + "</email> </contributor> <snx:role xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" component=\"http://www.ibm.com/xmlns/prod/sn/communities\">owner</snx:role> </entry>";

			HttpUtils restCalls = new HttpUtils();
			responseHeader = restCalls.postRequestReturnHeaders(connectionsUrl + getAddCommunityUserExtension() +
																"?communityUuid=" + communityId, 
																headers, body, contentType, "201");
			
			log.info("Verify response for adding members, where it should return the communityId");
			try {
				String userId = null;
				for (int count =0; count<responseHeader.length; count++) {
					if (responseHeader[count]!=null) {
						if (responseHeader[count].getName().contentEquals("Location")){
							System.out.println("Response Header["+ String.valueOf(count) + "] (Location): " + 
												responseHeader[count].getValue());
							
							int startIndex = responseHeader[count].getValue().lastIndexOf("communityUuid=");
							int startCommunityIdIndex2= responseHeader[count].getValue().indexOf("=", startIndex);
							int endCommunityIdIndex= responseHeader[count].getValue().indexOf("&userid=", startCommunityIdIndex2);
							int startUserIdIndex3= responseHeader[count].getValue().indexOf("=", startCommunityIdIndex2+1);
							
							log.info("Response Header["+ String.valueOf(count) + "] (communityId):" + responseHeader[count].getValue().substring(startCommunityIdIndex2+1, endCommunityIdIndex) );
							communityUuId = responseHeader[count].getValue().substring(startCommunityIdIndex2+1, endCommunityIdIndex);
							log.info("Response(communityUuId)=" + communityUuId);
							userId = responseHeader[count].getValue().substring(startUserIdIndex3+1);
							log.info("Response(userId)=" + userId);
							break;
						}
					}
				}
				Assert.assertTrue(communityUuId.equals((String)communityId), "Returned communityUuId is not equal to input communityId");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return communityUuId;
	}

	/**
	 * Retrieves all members of connection community
	 * @param userEmail (user authorized to access the connections API)
	 * @param userPassword
	 * @param communityId -- communityId of community 
	 * @param connectionsUrl
	 * @return String[] containing list of community's members
	 */
	public String[] getUsersConnectionsCommunity(String userEmail, String userPassword, String communityId, String connectionsUrl) {
		String responseString;
		String[] communityMemeberList = null;

		try {
			String cchUserPwd= userEmail + ":" + userPassword;
			String logon64Base = DatatypeConverter.printBase64Binary(cchUserPwd.getBytes());
			log.debug("logon64Base=" + logon64Base);
			String headers[]={"Authorization", "Basic " + logon64Base};

			HttpUtils restCalls = new HttpUtils();
			log.debug("Retrieving community members");
			responseString = restCalls.getRequest(connectionsUrl + getAddCommunityUserExtension() + 
													"?communityUuid=" + communityId, headers, "200");
			
			log.debug("Extract members from response");
			try {
				// Initialize loop variables
				int index = 0;
				int tagValStartsAt = 0;
				int tagValEndsAt = 0;
				int memberCounter = 0;

				// find total number of community members
				while (index<=responseString.length() && responseString.indexOf("<email>",index)!=-1) {
					// locate next occurrence of tag "<email>"
					tagValStartsAt = responseString.indexOf("<email>", index) + "<email>".length();
					tagValEndsAt = responseString.indexOf("</email>", tagValStartsAt) -1;
					
					if (tagValEndsAt<=responseString.length()) {
						log.debug("email=" + responseString.substring(tagValStartsAt, tagValEndsAt+1));
						memberCounter++;
					}
					index = tagValEndsAt;
					log.debug("current string ptr index="+ index);
				}

				// Re-initialize loop variables
				index = 0; // rest index
				tagValStartsAt = 0;
				tagValEndsAt = 0;
				String[] communityMembers = new String[memberCounter];
				memberCounter = 0; //reset counter
				// extract all community members into string array
				while (index<=responseString.length() && responseString.indexOf("<email>",index)!=-1) {
					// locate next occurrence of tag "<email>"
					tagValStartsAt = responseString.indexOf("<email>", index) + "<email>".length();
					tagValEndsAt = responseString.indexOf("</email>", tagValStartsAt) -1;
					
					if (tagValEndsAt<=responseString.length()) {					
						// Add member to Array
						communityMembers[memberCounter] = responseString.substring(tagValStartsAt, tagValEndsAt+1);
						memberCounter++;
					}
					index = tagValEndsAt;
					//log.info("counter="+ index);
				}
				communityMemeberList =  communityMembers;
				
			} catch (Exception e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}	
		}finally {

		}
		return communityMemeberList;
	}
	
	/**
	 * Get URL extension to create connections community
	 * @return string with URL extension to create community
	 */
	public String getCreateCommunityExtension(){
		return "communities/service/atom/communities/my";
	}
	
	/**
	 * Get URL extension to add user to connections community
	 * @return string with URL extension to add user to community
	 */
	public String getAddCommunityUserExtension(){
		return "communities/service/atom/community/members";
	}
	
}