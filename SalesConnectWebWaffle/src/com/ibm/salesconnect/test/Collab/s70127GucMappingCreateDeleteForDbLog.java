package com.ibm.salesconnect.test.Collab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.API.CommunityMappingRestAPI;
import com.ibm.salesconnect.API.ConnectionsCommunityAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;

/**
 * @author kvnlau@ie.ibm.com
 * @date Jul 07, 2015
 */

public class s70127GucMappingCreateDeleteForDbLog  extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(s70127GucMappingCreateDeleteForDbLog.class);

	@Test(groups = {"SEMI_AUTOMATION"})	
	public void Test_s70127CchMappingCreateDeleteForDbLogs() {

		String searchType = null;
		String token = null;
		String headers[] = {null, null};
		String ccmsId = null;
		String communityId = null;
		boolean isCCH = true;
		String responseString = null;
		CommunityMappingRestAPI mappingApi = new CommunityMappingRestAPI(); // for path extension methods
		ConnectionsCommunityAPI connApi= new ConnectionsCommunityAPI();
		
		log.info("Start of test method Test_s70127GucMappingCreateDeleteForDbLog()");
		try {
			// Populate variable from users.cvs, clients.csv files.
			PoolClient guClient = commonClientAllocator.getGroupClient(GC.GuClient,this);
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			
			ccmsId = guClient.getCCMS_ID(); //"GB001DK0"; // existing Client mapped to a community
			
			log.info("Retrieving OAuth2Token.");		
			token = new LoginRestAPI().getOAuth2Token(this.getBrowserUrl(), cchFnIdUser.getEmail(), cchFnIdUser.getPassword());
			headers[0] = "OAuth-Token";
			headers[1] = token;

			// ensure no mappings exists for cmmsId -------------------------------------
			log.debug("Test setup: checking if community already exists for ccmsId=" + ccmsId);
			communityId = mappingApi.getCommunityIdIfMappingExistsForCcmsId(
												this.getBrowserUrl(), token, cchFnIdUser, 
												ccmsId);
			
			log.info("ensure no mappings exists for cmmsId=" + ccmsId);
			if (communityId!=null) { 
				log.debug("Test setup: community already exists for ccmsId=" + ccmsId + " so deleting it now");
				responseString = mappingApi.deleteCommunityMapping(
												this.getBrowserUrl(), token, communityId, null,
						 						"999"); // don't care about response code
			} else {
				log.debug("Test setup: no community exists for ccmsId=" + ccmsId + " so pre-requisites completed");
			}
			
			if (communityId==null) {
				// create new community
				log.info("Create new community");
				
				communityId = connApi.createConnectionsCommunity(cchFnIdUser.getEmail(),
																cchFnIdUser.getPassword(),getCnxnCommunity());
				log.info("Created communityUuId: " + communityId + " with owner " + cchFnIdUser.getEmail());
				
				log.info("Adding new community members: " + connApi.funcIdEmail + " & " + cchFnIdUser.getEmail());
				responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), 
																	cchFnIdUser.getPassword(), 
																	connApi.funcIdEmail,
																	communityId, getCnxnCommunity());
			}
			
			// -------------- Begin test -------------------------------------
			log.info("======== Check entry count in DB logs ====================");
			log.info("check latest entries in DB table, by asking DB admin to run query below...");
			log.info("Run cmd: db2 \"select * from sctid.ibm_accounts_communities_audit where account_id='" + ccmsId + "' limit 5\"");	
			
			log.info("form body of (post) create single mapping request");		
			String body = "{\"communityId\":\"" + communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId + 
							"\"],\"isCCH\":\"" + isCCH + "\"}";
			
			log.info("Create a single mapping first with ccmsId=" + ccmsId);
			responseString = mappingApi.createCommunityMappingForCcmsId(
												this.getBrowserUrl(), token, cchFnIdUser, 
												connApi.funcIdEmail, communityId, ccmsId, isCCH);
			
			log.info("======== Now verify increased count in the DB logs ====================");
			log.info("check latest entries in DB table, by asking DB admin to run query below...");
			log.info("db2 \"select * from sctid.ibm_accounts_communities_audit where account_id='" + ccmsId + "' limit 5\"");
			
			log.info("Delete single mapping for ccmsId=" + ccmsId);
			responseString = mappingApi.deleteCommunityMapping(
												this.getBrowserUrl(), token, communityId, null,
					 							"999"); // don't care about response code
			
			log.info("======== Now verify decreased count in the DB logs ====================");
			log.info("check latest entries in DB table, by asking DB admin to run query below...");
			log.info("db2 \"select * from sctid.ibm_accounts_communities_audit where account_id='" + ccmsId + "' limit 5\"");
			// "select * from sctid.ibm_accounts_communities_audit limit 5 where account_id=" + ccmsId 
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, "test failed due to Exception");
		} finally {
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(
											this.getBrowserUrl(), token, communityId, null,
											 "999"); // don't care about response code
					
			log.info("End of test method Test_s70127GucMappingCreateDeleteForDbLog()");
		}
	}		

	
}

