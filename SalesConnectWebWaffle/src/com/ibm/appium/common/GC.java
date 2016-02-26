package com.ibm.appium.common;

/**
 * @author zbigniew
 * @date Sep 17, 2013
 */
public class GC {
	private static final GC gC = new GC();

	private GC() {
	}

	public static GC getInstance() {
		return gC;
	}

	// ------------Contacts------------
	// Status
	public static String gsStatusActive = "Active";
	public static String gsStatusDeceased = "Inactive - Deceased";
	public static String gsStatusInactiveGen = "Inactive - General";
	public static String gsStatusLeftCompany = "Inactive - Left the Company";
	public static String gsStatusDeleted = "Inactive - Marked for Deletion";
	public static String gsStatusOnLeave = "Inactive - On Leave";
	public static String gsStatusCustomerResponse = "Pending - Customer Response";
	public static String gsStatusQualityReview = "Pending - Quality Review";
	public static String gsStatusVerify = "Pending Marketing Verification";

	// Country
	public static String gsCountryAG = "Argentina";
	public static String gsCountryAO = "Angola";
	public static String gsCountryIE = "Ireland";
	public static String gsCountryUS = "United States";

	// State
	public static String gsStateBusenos = "Buenos Aires";
	public static String gsStateDub = "Dublin";
	public static String gsStateNY = "New York";
	public static String gsStateCA = "Canillo";

	// ------------Opportunity------------
	// Source
	public static String gsOppAccountPlan = "Account Plan";
	public static String gsOppBrandSalesTeam = "Brand Sales Team";
	public static String gsOppClientRepTeam = "Client Rep/Team";

	// Sales stage
	public static String gsOppNoticing = "01-Noticing";
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

	// Reason won lost
	public static String gsOppWLowestPrice = "IBM had lowest price.";
	public static String gsOppWBetterSkills = "Better experts/skills";
	public static String gsOppLPriorityChange = "Business priority changed";
	public static String gsOppLError = "Entered in Error";

	// ------------Calls------------
	// Duration
	public static final String gsDuration00 = "00";
	public static final String gsDuration15 = "15";
	public static final String gsDuration30 = "30";
	public static final String gsDuration45 = "45";
	public static final String gsDuration60 = "60";
	public static final String gsDurationGtr60 = ">60";

	// Status
	public static String gsCallPlanned = "Planned";
	public static String gsCallHeld = "Held";
	public static String gsCallNotHeld = "Not Held";

	// Type
	public static String gsCallFaceToFace = "Face to Face";
	public static String gsCallInbound = "Inbound Call";
	public static String gsCallOutbound = "Outbound Call";

	// ------------Tasks------------
	// Priority
	public static String gsTaskLow = "Low";
	public static String gsTaskMedium = "Medium";
	public static String gsTaskHigh = "High";

	// Status
	public static String gsTaskNotStarted = "Not Started";
	public static String gsTaskInProgress = "In Progress";
	public static String gsTaskComplete = "Completed";
	public static String gsTaskPendingInput = "Pending Input";
	public static String gsTaskDeferred = "Deferred";

	// Task Type
	public static String gsTaskCloseOut = "Close Out Call";
	public static String gsTaskTechnical = "Techinical Sales Activity";
	public static String gsTaskTypeBlank = null;
	public static String gsDealBoard1 = "Deal Board 1";
	public static String gsDealBoard2 = "Deal Board 2";
	public static String gsDealBoard3 = "Deal Board 3";

	// ------------RLI------------
	// Probability
	public static String[] gsRLIProbabilities = {"0%","10%","25%","50%","75%","100%"};
	
	// Roadmap Status
	public static String[] gsRLIRoadmapStatuses = { "NIR", "Stretch", "At Risk", "Solid", "Won" };
	
	// ------------Settings------------
	// Language
	public static String gsLangENG = "English (US)";
	public static String gsLangFR = "Fran�ais (Canada)";

	// Log Level
	public static String gsLogTrace = "TRACE";
	public static String gsLogDebug = "DEBUG";
	public static String gsLogInfo = "INFO";
	public static String gsLogWarning = "WARN";
	public static String gsLogError = "ERROR";
	public static String gsLogFatal = "FATAL";
	
	
	// ------------Filters------------
	// Clients
	public static final String gsMyClientsInMyCountry = "My Clients in My Country";
	public static final String gsMyRestrictedClients = "My Restricted Clients";
	public static final String gsMyClients = "My Clients";
	public static final String gsFavorites = "Favorites";

}
