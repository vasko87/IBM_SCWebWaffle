package com.ibm.appium.Objects;

import com.ibm.appium.common.Func;
import com.ibm.appium.common.GC;

public class LineItem {

	// Opportunity form fields
	public String sRLISearchTerm = "";
	
	public String sL10_OfferingType = "";
	public String sL15_SubBrand = "";
	public String sL17_SegmentLine = "";
	public String sL20_BrandCode = "";
	public String sL30_ProductInformation = "";
	public String sL40_MachineType = "";

	public String sOwner = "";
	public String sRoadmapStatus = "";
	public String sProbability = "";
	public String sFulfillmentType = "";
	public String sContractType = "";
	public String sBookingType = "";
	public String sBookingDetail = "";
	
	public String sGreenBlueRevenue= "";
	public String sAmount = "";
	public String sAmountInK = "";
	public String sDuration= "";

	public String sBillDate = "";
	public String sAnnualContractValue = "";
	public String sCloseDate = "";

	public void populate() {
		
		sL10_OfferingType = "Systems Hardware";
		sL15_SubBrand = "IBM BladeCenter";
		sL17_SegmentLine = "IBM BladeCenter";
		sL20_BrandCode = "IBM BladeCenter - System x";
		sL30_ProductInformation = "BladeCenter E Chassis (14 Bays)";
		sL40_MachineType = "BladeCenter * CTO model";
		
		sAmountInK =  Integer.toString(Func.RandomRange(111, 999));
		sAmount = sAmountInK + "000";
		sRoadmapStatus = GC.gsRLIRoadmapStatuses[Func.RandomRange(0,GC.gsRLIRoadmapStatuses.length - 1)];
		sProbability = GC.gsRLIProbabilities[Func.RandomRange(0,GC.gsRLIProbabilities.length - 1)];
		
		sFulfillmentType = "Web";
		sGreenBlueRevenue = "Green";
		
	}
	
	public void updateDetails() {
		
		sL10_OfferingType = "Lenovo";
		sL15_SubBrand = "Mobile";
		sL17_SegmentLine = "Mobile";
		sL20_BrandCode = "Notebook Products";
		sL30_ProductInformation = "Lenovo ThinkPad Tablet";
		sL40_MachineType = "";
		
		sAmountInK =  Integer.toString(Func.RandomRange(111, 999));
		sAmount = sAmountInK + "000";
		sRoadmapStatus = GC.gsRLIRoadmapStatuses[Func.RandomRange(0,GC.gsRLIRoadmapStatuses.length - 1)];
		sProbability = GC.gsRLIProbabilities[Func.RandomRange(0,GC.gsRLIProbabilities.length - 1)];
		
		sFulfillmentType = "Direct";
		sGreenBlueRevenue = "Blue";
		
	}

	public String getsFulfillmentType() {
		return sFulfillmentType;
	}

	public void setsFulfillmentType(String sFulfillmentType) {
		this.sFulfillmentType = sFulfillmentType;
	}

	public String getsRLISearchTerm() {
		return sRLISearchTerm;
	}

	public void setsRLISearchTerm(String sRLISearchTerm) {
		this.sRLISearchTerm = sRLISearchTerm;
	}

	public String getsL10_OfferingType() {
		return sL10_OfferingType;
	}

	public void setsL10_OfferingType(String sL10_OfferingType) {
		this.sL10_OfferingType = sL10_OfferingType;
	}

	public String getsL15_SubBrand() {
		return sL15_SubBrand;
	}

	public void setsL15_SubBrand(String sL15_SubBrand) {
		this.sL15_SubBrand = sL15_SubBrand;
	}

	public String getsL17_SegmentLine() {
		return sL17_SegmentLine;
	}

	public void setsL17_SegmentLine(String sL17_SegmentLine) {
		this.sL17_SegmentLine = sL17_SegmentLine;
	}

	public String getsL20_BrandCode() {
		return sL20_BrandCode;
	}

	public void setsL20_BrandCode(String sL20_BrandCode) {
		this.sL20_BrandCode = sL20_BrandCode;
	}

	public String getsL30_ProductInformation() {
		return sL30_ProductInformation;
	}

	public void setsL30_ProductInformation(String sL30_ProductInformation) {
		this.sL30_ProductInformation = sL30_ProductInformation;
	}

	public String getsL40_MachineType() {
		return sL40_MachineType;
	}

	public void setsL40_MachineType(String sL40_MachineType) {
		this.sL40_MachineType = sL40_MachineType;
	}

	public String getsOwner() {
		return sOwner;
	}

	public void setsOwner(String sOwner) {
		this.sOwner = sOwner;
	}

	public String getsRoadmapStatus() {
		return sRoadmapStatus;
	}

	public void setsRoadmapStatus(String sRoadmapStatus) {
		this.sRoadmapStatus = sRoadmapStatus;
	}

	public String getsProbability() {
		return sProbability;
	}

	public void setsProbability(String sProbability) {
		this.sProbability = sProbability;
	}

	public String getsContractType() {
		return sContractType;
	}

	public void setsContractType(String sContractType) {
		this.sContractType = sContractType;
	}

	public String getsBookingType() {
		return sBookingType;
	}

	public void setsBookingType(String sBookingType) {
		this.sBookingType = sBookingType;
	}

	public String getsBookingDetail() {
		return sBookingDetail;
	}

	public void setsBookingDetail(String sBookingDetail) {
		this.sBookingDetail = sBookingDetail;
	}

	public String getsGreenBlueRevenue() {
		return sGreenBlueRevenue;
	}

	public void setsGreenBlueRevenue(String sGreenBlueRevenue) {
		this.sGreenBlueRevenue = sGreenBlueRevenue;
	}

	public String getsAmount() {
		return sAmount;
	}

	public void setsAmount(String sAmount) {
		this.sAmount = sAmount;
	}

	public String getsAmountInK() {
		return sAmountInK;
	}

	public void setsAmountInK(String sAmountInK) {
		this.sAmountInK = sAmountInK;
	}

	public String getsDuration() {
		return sDuration;
	}

	public void setsDuration(String sDuration) {
		this.sDuration = sDuration;
	}

	public String getsBillDate() {
		return sBillDate;
	}

	public void setsBillDate(String sBillDate) {
		this.sBillDate = sBillDate;
	}

	public String getsAnnualContractValue() {
		return sAnnualContractValue;
	}

	public void setsAnnualContractValue(String sAnnualContractValue) {
		this.sAnnualContractValue = sAnnualContractValue;
	}

	public String getsCloseDate() {
		return sCloseDate;
	}

	public void setsCloseDate(String sCloseDate) {
		this.sCloseDate = sCloseDate;
	}
	
}
