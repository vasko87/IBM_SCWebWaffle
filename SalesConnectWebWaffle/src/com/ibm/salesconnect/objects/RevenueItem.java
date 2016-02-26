package com.ibm.salesconnect.objects;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.ibm.salesconnect.common.GC;

public class RevenueItem {
	public String sOfferingKey="";
	public String sFindOffering="";
	public String sSearchOffering="";
	public String sOfferingType="";
	public String sSubBrand="";
	public String sBrandCode="";
	public String sProductInfo="";
	public String sMachineType="";
	public String sCompetitor="";
	public String sRiskAssessment="";
	public String sConditions="";
	public String sProjectStartDate="";
	public String sProjectEndDate="";
	public String sGrossProfit="";
	public String sGPCurrency="";
	public String sOwner="";
	public String sDecisionDate="";
	public String sRoadmapStatus="";
	//Three radio buttons for Revenue Type.
	public String sRevenueType="";
	public String sPeriodType="";
	public String sPeriodNumber="";
	public String sProbability="";
	public String sRevenueAmount="";
	public String sRevenueCurrency="";
	public String sFlowCode="";
	public String sIGFOdds="";
	public String sFinancedRevenueAmount="";
	public String sFinancedRevenueCurrency="";
	public String sAlliancePartner="";
	public String sIGFStage="";
	public String sContractType="";
	public String sIGFRoadmapStatus="";
	public String sIGFVolumesDate="";
	public String sIGFCloseDate="";
	public String sGreenBlueRevenue = "";
	public String sFulfillmentType = "";
	public String sCurrencyType = "";
	public String sSource = "";
	public int iRevenueAmount = 0;
	public String ssRevenueAmount = "";
	public String ssFindOffering="";
	
	// Opportunity form fields
	public String sRLISearchTerm = "";
	
	public String sL10_OfferingType = "";
	public String sL15_SubBrand = "";
	public String sL17_SegmentLine = "";
	public String sL20_BrandCode = "";
	public String sL30_ProductInformation = "";
	public String sL40_MachineType = "";

	public String sBookingType = "";
	public String sBookingDetail = "";
	

	public String sAmount = "";
	public String sAmountInK = "";
	public String sDuration= "";

	public String sBillDate = "";
	public String sAnnualContractValue = "";
	public String sCloseDate = "";
	
	public void populate(){
		populate(false);
	}
	public void populate(Boolean API) {
		
		sFindOffering = "IBM BladeCenter";
		sOfferingType = "SW";
		sL17_SegmentLine = "17EMT";
		sL20_BrandCode = "BQT00";
		sL30_ProductInformation = "BQT99";
		sL40_MachineType = "";
		
		sL10_OfferingType = "BX500";
		sL15_SubBrand = "FBL";
		
		if (API) {
			sRoadmapStatus = GC.gsRLIRoadmapStatusesAPI[new Random().nextInt(GC.gsRLIRoadmapStatusesAPI.length - 1)];
		}
		else {
			sRoadmapStatus = GC.gsRLIRoadmapStatuses[new Random().nextInt(GC.gsRLIRoadmapStatuses.length - 1)];
		}

		sProbability = GC.gsRLIProbabilities[new Random().nextInt(GC.gsRLIProbabilities.length - 1)];
		sGreenBlueRevenue = "Green";
		
		//sFulfillmentType = "Web";.
		sRevenueAmount = "25000";
		sAmountInK =  Integer.toString(ThreadLocalRandom.current().nextInt(111, 999));
		sAmount = sAmountInK + "000";
		
		sCurrencyType = "USD";
		sCompetitor = "Microsoft";
		//sContractType = "New Contract - ALL";
		
		ssFindOffering = "Lenovo ThinkServer";
		ssRevenueAmount = Double.toString((int)(Math.random()*100)*1000+1000); //1k-100k random integer convert to string
	}
	
	public void setRLIAmount(String amount){
		sRevenueAmount=amount;
	}
	
	public String getRLIAmount(){
		return sRevenueAmount;
	}

	public RevenueItem(){
		this.populate();
	}
	
}
