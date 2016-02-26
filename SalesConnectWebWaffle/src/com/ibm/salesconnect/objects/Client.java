package com.ibm.salesconnect.objects;

import java.util.Vector;

import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;

	/**
	 * class used to hold account information for passing into methods
	 * note the objects in this class are public to facilitate access
	 * which also means care must be taken to avoid inadvertant value changes
	 * for each instance 
	 */
	public class Client{
		//short form fields
		public String sCCMS_Level = "";
		public String sClientName = "";
		public String sClientID = "";
		public String sSiteID = "";
		public String sGrayClientName = "";
		public String sPhysicalStreet = "";
		public String sPhysicalCity = "";
		public String sPhysicalPostal = "";
		public String sPhysicalState = "";
		public String sPhysicalCounty = "";
		public String sPhysicalCountry = "";
		public boolean bCopyPhysical = false;
		public String sMailStreet = "";
		public String sMailCity = "";
		public String sMailState = "";
		public String sMailCounty = "";
		public String sMailPostal = "";
		public String sMailCountry = "";

		public String sWebsite = "";
		public String sPhoneFax= "";
		public String sPhoneNumber= "";
		public String sFaxNumber= "";
		public String sGraySpace = "";
		public String sAccountIndustry= "";
		public String sEmail0= "";
		public boolean bEmail0Primary=false;
		public boolean bEmail0Invalid=false;
		public boolean bEmail0Suppress=false;
		public String sEmail1= "";
		public boolean bEmail1Primary=false;
		public boolean bEmail1Invalid=false;
		public boolean bEmail1Suppress=false;

		public String sAccountType="";
		public String sTeam0Name="";
		public boolean bTeam0Primary=false;
		public String sTeam1Name="";
		public boolean bTeam1Primary=false;
		public String sAccount="";
		public String sLeadSource="";
		public String sContactPrimary="";
		public String sAssignedTo="";
		public String sAssignedToEmail="";
		public String sFullForm="";

		public String sSearchIn= "";
		public String sSearchFor= "";
		public String sSearchShowing="";
		public boolean myClients=false;
		public boolean bMyFavorites = false;
		
		public String sISU="";
		public String sIndustry="";
		public String sIndustryClass="";
		public String sISIC="";
		public String sGBQuad="";
		
		public String[] clientTeam;

		/**
		 * undeprecated - can be used if only 1 tag is desired - for multiple tags, use vTags below
		 */
		public String sTags = "";

		//long form only fields
		public String sCountry="";
		public String sStreet="";
		public String sCity="";
		public String sState="";
		public String sPostalCode="";
		public String sAccountStatus="";
		public Vector<String> vClientTeam = new Vector<String>();		
		public Vector<String> vTags = new Vector<String>();
		
		public String sParentName;
		public String sParentID;
		public String sGlobalClientName;
		public String sGlobalClientID;
		public String sUltimateClientCCMS_ID;
		public String sGlobalClientCCMS_ID;
		public String sDefaultSiteID;
		public String sBeanID;
		
		public Client(){
			
		}
		
		public Client(PoolClient poolClient){
			sClientID = poolClient.getCCMS_ID();
			//sClientName = poolClient.getClientName();
		}
		
		public void populate() {
			sClientName = "BVT Client " + (int) Math.round((Math.random()) * 10000);
			sTags = "bvt_longaccount";		
			sPhysicalStreet = "15 Watson Way";
			sPhysicalCity = "Poughkeepsie";
			sPhysicalCountry = "United States";
			sPhysicalState = "Florida";
			sPhysicalPostal = "99999";
			bCopyPhysical= true;
			sMailStreet = "14 Watson Way";
			sMailCity = "Littleton";
			sMailState = "";
			sMailPostal = "191919191";
			sMailCountry = "Afghanistan";

			sPhoneNumber = "111-111-1111";
			sPhoneFax = "222-222-2222";
			sWebsite = "testclient@test.com";
			sGraySpace = "Preset Test Client";
			
			sSearchIn= GC.showingInClientName;
			sSearchFor= GC.searchForAll;
			sSearchShowing=GC.showingForAll;
			myClients=false;
			
			sISU="Banking";
			sIndustry="Banking";
			sIndustryClass="Banking - Other";
			sISIC="Other Monetary Intermediation";
			sGBQuad="Growth";
			//sGBQuad="GBE Growth";

		}
		
	
	}