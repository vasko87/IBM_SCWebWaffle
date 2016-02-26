package com.ibm.appium.Objects;

import com.ibm.appium.common.GC;

public class Opportunity {

	// Opportunity form fields
	public String sId = "";
	public String sOpptyDescription = "";
	public String sOpptyDescriptionUPD = "";
	public String sOpptyClient = "";
	public String sOpptyContact = "";
	public String sSource = "";
	public String sSalesStage = "";
	public String sDecisionDate = "";
	public String sOpptyOwner = "";
	public String sReasonWon = "";
	public String sReasonLost = "";
	public String sOpportunityTag = "";
	public String sOpportunityTagUPD = "";
	public String sOpportunityTag1 = "";
	public String sOpportunityTag2 = "";
	public String sOpportunityTag3 = "";
	public String sOpportunitySplitTag = "";

	public void populate() {
		sOpptyDescription = "oppty " + System.currentTimeMillis();
		sOpptyDescriptionUPD = "UPD";

		sOpportunityTag = "TestOpportunityTag" + System.currentTimeMillis();
		sOpportunityTagUPD = "UPD";

		sSource = GC.gsOppAccountPlan;
		sSalesStage = GC.gsOppValidating;

		sOpportunityTag1 = "Tag";
		sOpportunityTag2 = "Tagging";
		sOpportunityTag3 = "Opportunity Tag";
		sOpportunitySplitTag = "This, is, a, Split, Tag,"; // this tag should
															// save as 5
															// separate tags.
	}

	public void updateDetails(){
		sOpptyDescription += sOpptyDescriptionUPD;
	}
	
	public String getsOpportunityTag1() {
		return sOpportunityTag1;
	}

	public void setsOpportunityTag1(String sOpportunityTag1) {
		this.sOpportunityTag1 = sOpportunityTag1;
	}

	public String getsOpportunityTag2() {
		return sOpportunityTag2;
	}

	public void setsOpportunityTag2(String sOpportunityTag2) {
		this.sOpportunityTag2 = sOpportunityTag2;
	}

	public String getsOpportunityTag3() {
		return sOpportunityTag3;
	}

	public void setsOpportunityTag3(String sOpportunityTag3) {
		this.sOpportunityTag3 = sOpportunityTag3;
	}

	public String getsId() {
		return sId;
	}

	public void setsId(String sId) {
		this.sId = sId;
	}

	public String getsDescription() {
		return sOpptyDescription;
	}

	public void setsDescription(String sOpptyDescription) {
		this.sOpptyDescription = sOpptyDescription;
	}

	public String getsDescriptionUPD() {
		return sOpptyDescriptionUPD;
	}

	public void setsDescriptionUPD(String sOpptyDescriptionUPD) {
		this.sOpptyDescriptionUPD = sOpptyDescriptionUPD;
	}

	public String getsClient() {
		return sOpptyClient;
	}

	public void setsClient(String sOpptyClient) {
		this.sOpptyClient = sOpptyClient;
	}

	public String getsContact() {
		return sOpptyContact;
	}

	public void setsContact(String sOpptyContact) {
		this.sOpptyContact = sOpptyContact;
	}

	public String getsSource() {
		return sSource;
	}

	public void setsSource(String sSource) {
		this.sSource = sSource;
	}

	public String getsSalesStage() {
		return sSalesStage;
	}

	public void setsSalesStage(String sSalesStage) {
		this.sSalesStage = sSalesStage;
	}

	public String getsDecisionDate() {
		return sDecisionDate;
	}

	public void setsDecisionDate(String sDecisionDate) {
		this.sDecisionDate = sDecisionDate;
	}

	public String getsOpptyOwner() {
		return sOpptyOwner;
	}

	public void setsOpptyOwner(String sOpptyOwner) {
		this.sOpptyOwner = sOpptyOwner;
	}

	public String getsReasonWon() {
		return sReasonWon;
	}

	public void setsReasonWon(String sReasonWon) {
		this.sReasonWon = sReasonWon;
	}

	public String getsReasonLost() {
		return sOpptyOwner;
	}

	public void setsReasonLost(String sReasonLost) {
		this.sReasonLost = sReasonLost;
	}

}
