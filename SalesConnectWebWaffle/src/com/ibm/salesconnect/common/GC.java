/**
 * 
 */
package com.ibm.salesconnect.common;

import com.ibm.salesconnect.common.DateTimeUtility;
import com.ibm.salesconnect.common.GC;

/**
 * @author timlehane
 * @date Apr 23, 2013
 */
public class GC {
	private static final GC gC = new GC();
	
	private GC(){}
	public static GC getInstance(){
		return gC; 
	}
	
	//Sugar API
	public static final String baseURLextension = "index.php";
	public static final String restURLExtension = "service/v4_ibm/rest.php";
	public static final String soapURLExtension = "service/v4_ibm/soap.php";
	
	public static final String sugarAPIHeaders[]={"IBM-salesconnect-key","passw0rd"};
	public static final String emptyArray[]={};
	
	
	//CollabWeb API
	public static final String authURLExtension = "rest/v10/oauth2/token";
	public static final String listCommunitySubscriptionsExtension = "rest/v10/collab/listCommunitySubscriptions";
	public static final String subscribeToCommunityExtension = "rest/v10/collab/subscribeCommunity";
	public static final String unsubscribeCommunityExtension = "rest/v10/collab/unsubscribeCommunity";
	public static final String checkCommunityStatusExtension = "rest/v10/collab/isCommunitySubscribed";
	public static final String activityStreamEventsFilterExtension = "rest/v10/collab/activityStreamEventsFilter";
	public static final String activityStreamMicroblogFilterExtension = "rest/v10/collab/activityStreamMicroblogFilter";
	public static final String postToMicroBlogExtension = "rest/v10/collab/postMicroblog";
	
	public static final String accountsModule = "Accounts";
	public static final String opptysModule = "Opportunities";
	
	
	public static final String sUniqueNum = DateTimeUtility.getDateTimeStampNoPunct();
	public static final String sCurrentDate = DateTimeUtility.getDateSlashSeparated();
	
	//DB2
	public static String db2UserGroup ="db2_users";
	public static String db2URL = "sugarurldb";
	
	//Users
	public static String noGBL_SearchGroup = "noGBL_Search";
	public static String noMemUserGroup = "noMem_users";
	public static String busAdminGroup = "bus_admin";
	public static String e2eUserGroup = "e2e_users";
	public static String e2eUserGroupAG = "e2e_AGusers";
	public static String e2eUserGroupAP = "e2e_APusers";
	public static String e2eUserGroupEU = "e2e_EUusers";
	public static String cchFnIdGroup = "cchFnId";
	public static String nonCchFnIdGroup = "nonCchFnId";
	public static String gbsMgr = "gbs_Mgr";
	public static String gbsSel = "gbs_Sel";
	
	//XML Parameters
	public static final String testPhase = "testphase";	
	public static final String nodbaccess = "nodbaccess";
	public static final String cxnURL = "cxnurl";
	public static final String explicitTimeoutName = "timeout_explicit_wait";
	public static final String implicitTimeoutName = "timeout_implicit_wait";

	
	//Client
	public static final String DC = "DC";
	public static final String SC = "SC";
	public static final String E2EAG = "E2EAG";
	public static final String E2EAP = "E2EAP";
	public static final String E2EEU = "E2EEU";
	public static final String showingInClientName = "Client name";
	public static final String showingInClientID = "Client ID";
	public static final String showingInSiteID = "Site ID";
	public static final String gsSavedSearches = "Saved searches";
	public static final String GbClient = "GBCLIENT";
	public static final String GuClient = "GUCLIENT";
	public static final String CommunityClient = "COMMUNITYMEMBERSHIP";
	
	public static String searchForAF = "Afghanistan";
	public static String searchForBR = "Brazil";
	public static String searchForCN = "China";
	public static String searchForIE = "Ireland";
	public static String searchForUS = "United States";
	public static String searchForGE = "Germany";
	public static String searchForUK = "United Kingdom";
	public static String searchForAR = "Argentina";
	public static String searchForFR = "France";
	public static String searchForAll = "All countries";
	
	public static String showingForAll = "All Clients";
	public static String showingForClients = "Clients";
	public static String showingForSites = "Sites";
	
	public static String[] gsShowingFor = {"All Clients", "Clients", "Sites"};
	
	public static Boolean myClients = true;
	public static Boolean allClients = false;
	
	public static String ClientID = "ClientID";
	public static String ClientName = "ClientName";
	
	//Contact
	public static String gsSalutationMs = "Ms.";
	public static String gsSalutationMr = "Mr.";
	public static String gsSalutationMrs = "Mrs.";
	public static String gsSalutationDr = "Dr.";
	public static String gsSalutationProf = "Prof.";
	
	//Opportunity
	//Tabs
	public static String gsOpportunityOverviewTab = "Opportunity overview";
	public static String gsUpdatesTab = "Updates";
	public static String gsConnectionsCommunityTab = "Connections community";
	public static String gsRecommendedDocumentsTab = "Recommended documents";
	public static String gsConnectionsExpertsTab = "Recommended experts";
	
	//team member roles
	public static String gsClientRep = "Client Rep";
	public static String gsTeamMember = "Team Member";
	public static String gsIGFRep = "IGF Rep";
	public static String gsCoverageRep = "Coverage Rep";
	public static String gsGBSDevManager = "GBS Business Development Manager";
	public static String gsLineItemOwner = "Line Item Owner";
	
	//Search
	public static String gsBasicSearch = "Basic";
	public static String gsAdvancedSearch = "Advanced search";
	public static String gsDecisionDateLast = "Last Quarter";
	public static String gsDecisionDateThis = "This Quarter";
	public static String gsDecisionDateNext = "Next Quarter";
	public static String gsDecisionDateBefore = "Before";
	public static String gsdecisionDateExact = "Exact";
	public static String gsDecisionDateAll = "All"; //Obsolete
	
	//source
	public static String gsOppAccountPlan ="Account Plan";
	public static String gsOppCampaign = "Campaign";
	public static String gsClientRepTeam ="Client Rep/Team";
	public static String gsOppColdCall = "Cold Call";
	public static String gsOppConference = "Conference";
	public static String gsOppDirectMail = "Direct Mail";
	public static String gsOppEmail = "Email";
	public static String gsOppEmployee = "Employee";
	public static String gsOppExistingClient = "Existing Client";
	public static String gsOppPartner = "Business Partner";
	public static String gsOppPublicRelations = "Public Relations";
	public static String gsOppSelfGen = "Self Generated";
	public static String gsOppTradeShow = "Trade Show";
	public static String gsOppWordOfMouth = "Word of Mouth";
	public static String gsOppBrandSalesTeam = "Big Data Industry Team";
	public static String gsOppIBMService = "IBM Services";
	public static String gsOppIBMTelemarketing = "IBM Telemarketing";
	public static String gsOppISVSolutionSales = "ISV Solution Sales";
	public static String gsOppSolutionProvider = "Solution Provider - Core BP";
	public static String gsOppSuperRestricted = "Super Restricted";
	public static String gsOppSWLeadDevelopment = "SW Lead Development Rep";
	public static String gsOppTelecoverage = "Telecoverage";
	public static String gsOppTelesales = "Telesales";
	public static String gsOppVendorTelemarketing = "Vendor Telemarketing";
	
	//sales stage
	public static String gsOppProspecting = "01-Noticing";
	public static String gsOppIdentifying = "02-Noticed/Identifying";
	public static String gsOppValidating = "03-Identified/Validating";
	public static String gsOppQualifying = "04-Validated/Qualifying";
	public static String gsOppGainingAgreement = "05-Qualified/Gaining Agreement";
	public static String gsOppClosing = "06-Cond Agreed/Closing";
	public static String gsOppWonImplementing = "07-Won/Implementing";
	public static String gsOppWonComplete = "08-Won and Complete";
	public static String gsOppNoBid = "09-No Bid";
	public static String gsOppCustomerNotPursue = "10-Customer Did Not Pursue";
	public static String gsOppLostToCompetition = "11-Lost to Competition";
	
	//IGF Sales Stage
	public static String gsIGF01 = "01-NOTICING";
	public static String gsIGF02 = "02-NOTICED / IDENTIFYING";
	public static String gsIGF03 = "03-IDENTIFIED / VALIDATING";
	public static String gsIGF04 = "04-VALIDATED / QUALIFYING";
	public static String gsIGF05 = "05-QUALIFIED/GAINING COND AGR";
	public static String gsIGF06 = "06-COND AGREED/CLOSING";
	public static String gsIGF07 = "07-WON / IMPLEMENTING";
	public static String gsIGF08 = "08-WON AND COMPLETED"; 
	
	//reason won lost
	public static String gsOppWLowestPrice = "IBM had lowest price.";
	public static String gsOppLSoleOnPrice = "Client bought solely on price";
	public static String gsOppLPriorityChange = "Business priority changed";
	public static String gsOppLLowOdds = "Low odds of winning";
	
	// ------------RLI------------
	// Probability
	public static String[] gsRLIProbabilities = {"0","10","25","50","75","100"};
	
	// Roadmap Status
	public static String[] gsRLIRoadmapStatuses = { "NIR", "Stretch", "Key stretch","At Risk", "Solid", "Won" };
	public static String[] gsRLIRoadmapStatusesAPI = { "NIR", "STR", "KEY","ATR", "SOL", "WON" };

	
	//Team roles for Business Partners
	public static String gsOppRoleISVInfluencer25 = "ISV Influencer (25%)";
	public static String gsOppRoleISVInfluencer50 = "ISV Influencer (50%)";
	public static String gsOppRoleISVInfluencer75 = "ISV Influencer (75%)";
	public static String gsOppRoleISVInfluencer100 = "ISV Influencer (100%)";
	public static String gsOppRoleISVRegionalSystemInegrator = "ISV Regional System Integrator";
	public static String gsOppRoleBusinessPartner = "Business Partner";
	public static String gsOppRoleDistVAD = "Dist / VAD";
	public static String gsOppRoleMarketingServiceProv = "Marketing Service Provider";
	
	//data restrictions
	public static String gsNotRestricted = "Not restricted";
	public static String gsVisibleOpAndClient = "Visible to opportunity and client team only";
	public static String gsVisibleOpOnly = "Visible to opportunity team only";
	
	//RLI Offerings
	public static String gsTivoli = "Tivoli";
	public static String gsTivoliAutomation = "Tivoli Automation";
	public static String gsBigData = "Big Data";
	public static String gsLenovo = "Lenovo";
	public static String gsLenovoSoftware = "Lenovo Software";
	
	//Updates Tab
	public static String gsNotFollowingText = "You must follow to see events";
	public static String gsFollowOpptyConfirmation = "You are now following recent events for this opportunity!";
	public static String gsFollowClientConfirmation = "You are now following recent events for this client!";
	public static String gsStopFollowing = "Stop Following";
	public static String gsPostConfirmation = "Your message was successfully posted";
	
	//Tasks
	//Status
	public static String gsNotStarted = "Not Started";
	public static String gsInProgress = "In Progress";
	public static String gsComplete = "Completed";
	public static String gsPendingInput = "Pending Input";
	public static String gsDeferred = "Deferred";
	
	//Priority
	public static String gsLow = "Low";
	public static String gsMedium = "Medium";
	public static String gsHigh = "High";
	
	//Calls
	public static String gsInbound = "Inbound";
	public static String gsOutbound = "Outbound";

	public static String gsPlanned = "Planned";
	public static String gsHeld = "Held";
	public static String gsNotHeld = "Not Held";

	public static final String gsDuration00 = "00";
	public static final String gsDuration15 = "15";
	public static final String gsDuration30 = "30";
	public static final String gsDuration45 = "45";
	public static final String gsDuration60 = "60";
	public static final String gsDurationGtr60 = ">60";
	
	//Documents
	//Document Sharing Options
	public static String gsShareWithEveryone 		  = "Public";
	public static String gsShareWithSelectedUsers   = "Selected Users";
	public static String gsShareWithClientTeam 	  = "Client Team";
	public static String gsShareWithIndividuals     = "Individuals";
	public static String gsShareWithIndividualsAndClientTeam = "IndivAndClient";
	public static String gsShareWithNoOne     = "NoOne";
	public static String gsShareWithIndividualUserNameSelect = "gsShareWithIndividualUserNameSelect";
	
	public static String notDisplayed = "display: none";
	
	//ManageEvents
	public static final String Clients = "Clients";
	public static final String Opportunities = "Opportunities";
	public static final String ClientsImFollowing = "All clients that I am following";
	public static final String ClientsImNotFollowing = "My clients that I am not following";
	public static final String OpportunitiesImFollowing = "All opportunities that I am following";
	public static final String OpportunitiesImNotFollowing = "My opportunities that I am not following";
	
	//Search Bars
	public static final String EmailColumn = "email";
	public static final String StrengthColumn = "score";
	
	//Seller Forecast Roadmap Status
	public static final String s_won="Won";
	public static final String s_key_stretch="Key stretch";
	public static final String s_solid="Solid";
	public static final String s_at_risk="At risk"; 
	public static final String s_stretch="Stretch";
	public static final String s_nir="NIR";
	public static final String s_WSA="Won+Solid+At risk";
	public static final String s_target="Target";
	public static final String s_best_case="Best case"; 
	public static final String s_worst_case="Worst case"; 
	public static final String s_delta_to_target="Delta to target";
	
	//Adjustment
	public static String adj_description = "create adjustment description";
	public static String adj_type = "Accounting";
	public static String adj_client = "GALLIAN";
	public static String adj_date = DateTimeUtility.getDateSlashSeparated();
	public static String adj_roadmapstatus = "At Risk";
	public static String adj_amount = "10000";
	public static String adj_select = "Lenovo 3000 C300";
	public static String mgr_adj_geography = "";
	public static String mgr_adj_industry = "Automotive Industry"; 
	public static String mgr_adj_gbsunit = "";
	public static String mgr_adj_contract = "New Logo";
	public static String mgr_adj_channel = "Direct";

	
}
