/**
 * 
 */
package com.ibm.salesconnect.objects;

import java.util.Locale;
import java.util.Random;
import java.util.Vector;

import com.ibm.salesconnect.common.GC;

/**
 * @author Administrator
 *
 */
public class Opportunity {
		//public String sOpportunityName = "";
		public String sOpptName = "";
		public String sOpptDesc = "";
		public String sCurrency = "";
		public String sOpptAmt = "";
		public String sNextSteps = "";
		public String sLeadSource = "";
		public String sAcctName = "";
		public String sAccID = "";
		public String sContractor = "";
		public String sDateCreated = "";
		public String sCloseDate = "";
		public String sCloseDateExact = "";
		public String sPrimaryContact = "";
		public String sPrimaryContactFirst = "";
		public String sPrimaryContactLast = "";
		public String sPrimaryContactPreferred = "";
		public String sPrimaryContactWithPreferred = "";
		public String sClient = "";
		public String sSalesStage = "";
		public String sOpptNumber = "";
		public Locale lOpptCurrencyLocale = Locale.getDefault();
		public String sOpptSource = "";
		public String sAssignedToEmail="";
		public String sOpptOwner = "";
		public String sAddTeamMember = "";
		public String sAddTeamMemberRole = "";
		public String sSearchDecisionDate = "";
		/**for use only with single tags. for multiple tags, use vTags*/
		public String sTags = "";
		public Vector<String> vTags = new Vector<String>();
		public String sSolutionCodes = "";
		public String sCampaign = "";
		public String sReasonWonLost = "";
		public String sOptRisk = "";
		public String sTeamName0 = "";
		public boolean sTeamName0Primary = false;
		public String sTeamName1 = "";
		public boolean sTeamName1Primary = false;
		public String sProbablity = "";
		public boolean bRestrict = false;
		public String sRestrict = "";
		public boolean bInternational = false;
		public boolean bItar = false;
		public boolean bMyOpportunities = false;
		public boolean bFavOpportunities = false;
		public boolean bMyFavorites= false;

		public Vector<String> vOpportunityTeam = new Vector<String>();
		public Vector<String> vOpportunityRole = new Vector<String>();
		public boolean bUpdateNoticierIdentifier = true;
		
		public Vector<String> vBusinessParters = new Vector<String>();
		public Vector<String> vBusinessRole = new Vector<String>();
		
		public void populate() {
			sOpptDesc = "BVT_Opportunity " + String.valueOf(new Random().nextInt(999999));
			sSalesStage=GC.gsOppIdentifying;
			sCampaign = "Apple";
			sRestrict = GC.gsNotRestricted;
			sCurrency = "Algerian Dinar";
			sTags = "Tag25";
			sPrimaryContactFirst = "BVTFirst1501";
			sPrimaryContactLast ="BVTLast1501";
			sPrimaryContactPreferred ="PrefBvt1501";
			sPrimaryContact = sPrimaryContactFirst +" "+ sPrimaryContactLast;
			sPrimaryContactWithPreferred = sPrimaryContactFirst +" ("+sPrimaryContactPreferred+") "+ sPrimaryContactLast;
			sSearchDecisionDate = GC.gsDecisionDateNext;
			sCloseDate = "All";
			//sClient = "ANTUNA EXILUS";
			sLeadSource = "Account Plan";
			//sCloseDateExact = GC.sCurrentDate;
		}
		
		public void setOpptyNumber(String newNum){
			sOpptNumber = newNum;
		}
		
		public String getOpptyNumber(){
			return sOpptNumber;
		}
		
		public void setOpptyDesc(String newDesc){
			sOpptDesc=newDesc;
		}
		
		public String getOpptyDesc(){
			return sOpptDesc;
		}
}