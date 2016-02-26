/**
 * 
 */
package com.ibm.salesconnect.objects;

import com.ibm.salesconnect.common.GC;
/**
 * @author Administrator
 * class used to hold contact information for passing into methods
 */
public class Contact {

		//short form fields
		public String sFirstName = ""; //required when creating
		public String sLastName= ""; //required when creating
		public String sPreferredName= "";
		public String sAltFirstName = "";
		public String sAltPreferredName="";
		public String sAltLastName = "";
		public String sAltLangPref = "";
		
		public boolean bKeyContact = false;
		public boolean bKeySetMet = false;

		public String sClientName=""; // this is the "client", required when creating	
		public String sClientID="";
		public String sJobTitle= "";

		public String sOfficePhone= "";
		public boolean bOfficePhoneSuppress=false;
		public String sMobile= "";
		public boolean bMobileSuppress=false;

		public String sEmail0= "";
		public boolean bEmail0Primary=false;
		public boolean bEmail0Invalid=false;
		public boolean bEmail0Suppress=false;
		public String sEmail1="";
		public boolean bEmail1Primary=false;
		public boolean bEmail1Invalid=false;
		public boolean bEmail1Suppress=false;

		public String sLanguage="";

		//Long Form additional fields 		
		public String sAltMiddleName = "";
		public String sSalutation = "";

		public String sContactStatus = "";
		public String sKeyContact = ""; //expecting "", "Y", or "R" to check Yes or Relationship set/met box
		public String sTags = "";

		public String sAddress = "";
		public String sCountry = "";
		public String sCity = "";
		public String sState = "";
		public String sPostalCode = "";

		public String sAltAddress = "";
		public String sAltCountry = "";
		public String sAltCity = "";
		public String sAltState = "";
		public String sAltPostalCode = "";

		public String sPhoneFax= "";
		public boolean bPhoneFaxSuppress=false;

		public String sEmpHistClient = ""; //Employment history client name
		public String sEmpHistJobTitle = "";//Employment history job title

		//obsolete contact fields
		public boolean bDoNotCall=false;
		public boolean bTeam0Primary=false;
		public boolean bTeam1Primary=false;
		public String sContactPrimary="";
		public String sLeadSource="";
		public String sTeam0Name="";
		public String sTeam1Name="";

		// Contact List view 
		public boolean bMyItems = false;
		public boolean bMyFavorites = false;
		
		//Search Options
		public boolean sSearchName=false;
		public boolean sSearchAnyEmail= false;
		public boolean sSearchContactTags= false;
		
		
		public void populate() {
			int sRand = (int) Math.round((Math.random()) * 10000);
			
			sSalutation = GC.gsSalutationDr;
			sFirstName = "BVTFirst"+sRand; 			
			sLastName = "BVTLast"+sRand;
			sPreferredName = "PrefBvt"+sRand;
			sAltFirstName = "AltBVT"+sRand;
			sAltLastName = "AltBVTL"+sRand;
			//sAltLangPref = "AltLangPref"+sRand;
			sContactStatus = "Inactive - General";
			bKeyContact = false;
			bKeySetMet =  false;
			sJobTitle = " long job title" + sRand;
			sTags="bvt_Contact Tag"+sRand;
			sAddress = "15 Watson Way";
			sCity = "Poughkeepsie";
			sPostalCode = "11111";
			sCountry = "United States";
			sState = "Alabama";
			//Mailing Address
			sAltAddress = "14 watson way";
			sAltCity = "Armonk";
			sAltPostalCode = "33333";
			sAltCountry = "Albania";
			sAltState = "Devoll";
			sMobile = "1111111111";
			sOfficePhone = "2222222222";
			sEmail0=sLastName +"@SFAbvt.com";
			bEmail0Primary=true;
			bEmail0Suppress = false;
			bEmail0Invalid =  false;
			//sLanguage = "English";			
			//sEmpHistClient = "IBM";
			//sEmpHistJobTitle = "cheese monger";	

			sClientName = "BVT Client "+sRand;
			sPhoneFax = "0000000000";
			bPhoneFaxSuppress= false;
			
			sSearchName = false;
			sSearchAnyEmail = false;
			sSearchContactTags = false;
		}
		
		public boolean equals(Contact contact2){
			boolean result = true;
			if(!this.sFirstName.equals(contact2.sFirstName)) result = false;
			
			if(!this.sLastName.equals(contact2.sLastName)) result = false;

			if(!this.sPreferredName.equals(contact2.sPreferredName)) result = false;
		
			if(!this.sAltFirstName.equals(contact2.sAltFirstName)) result = false;

			if(!this.sAltLastName.equals(contact2.sAltLastName)) result = false;
		
			if(!this.sAltPreferredName.equals(contact2.sAltPreferredName)) result = false;
		
			if(!this.sClientName.equals(contact2.sClientName)) result = false;
		
			if(!this.sJobTitle.equals(contact2.sJobTitle)) result = false;
		
			if(!this.sOfficePhone.equals(contact2.sOfficePhone)) result = false;
		
			if(!this.sMobile.equals(contact2.sMobile)) result = false;
		
			if(!this.sEmail0.equals(contact2.sEmail0)) result = false;
				
			if(this.bEmail0Primary != contact2.bEmail0Primary) result = false;
				
			if(this.bEmail0Suppress != contact2.bEmail0Suppress) result = false;
			
			if(this.bEmail0Invalid != contact2.bEmail0Invalid) result = false;
			
			if(!this.sEmail1.equals(contact2.sEmail1)) result = false;
				
			if(this.bEmail1Primary != contact2.bEmail1Primary) result = false;
				
			if(this.bEmail1Suppress != contact2.bEmail1Suppress) result = false;
				
			if(this.bEmail1Invalid != contact2.bEmail1Invalid) result = false;

			if(!this.sSalutation.equals(contact2.sSalutation)) result = false;
			
			if(!this.sContactStatus.equals(contact2.sContactStatus)) result = false;
			
			if(this.bKeyContact != contact2.bKeyContact) result = false;
				
			if(this.bKeySetMet != contact2.bKeySetMet) result = false;

			if(!this.sCountry.equals(contact2.sCountry)) result = false;
		
			if(!this.sAddress.equals(contact2.sAddress)) result = false;
		
			if(!this.sCity.equals(contact2.sCity)) result = false;	
		
			if(!this.sState.equals(contact2.sState)) result = false;
		
			if(!this.sPostalCode.equals(contact2.sPostalCode)) result = false;
			
			if(!this.sAltCountry.equals(contact2.sAltCountry)) result = false;
				
			if(!this.sAltAddress.equals(contact2.sAltAddress)) result = false;
		
			if(!this.sAltCity.equals(contact2.sAltCity)) result = false;
			
			if(!this.sAltState.equals(contact2.sAltState)) result = false;
				
			if(!this.sAltPostalCode.equals(contact2.sAltPostalCode)) result = false;
			
			if(!this.sTags.equals(contact2.sTags)) result = false;
		
			//click(additionalInformationPanelTwisty);
				
			if(!this.sPhoneFax.equals(contact2.sPhoneFax)) result = false;
			
			if(this.bPhoneFaxSuppress != contact2.bPhoneFaxSuppress) result = false;
			
			if(!this.sLanguage.equals(contact2.sLanguage)) result = false;
			
			return result;
		}
		
		public boolean equalsFromSubpanel(Contact contact2){
			boolean result = true;
			
			if(!this.sFirstName.equals(contact2.sFirstName)) result = false;
			if(!this.sLastName.equals(contact2.sLastName)) result = false;
			if(!this.sPreferredName.equals(contact2.sPreferredName)) result = false;
			if(!this.sAltFirstName.equals(contact2.sAltFirstName)) result = false;
			if(!this.sAltLastName.equals(contact2.sAltLastName)) result = false;
			if(!this.sAltPreferredName.equals(contact2.sAltPreferredName)) result = false;
			
			if(!this.sClientName.equals(contact2.sClientName)) result = false;
			if(!this.sClientID.equals(contact2.sClientID)) result = false;
			if(!this.sJobTitle.equals(contact2.sJobTitle)) result = false;
			if(!this.sOfficePhone.equals(contact2.sOfficePhone)) result = false;
			if(!this.sMobile.equals(contact2.sMobile)) result = false;
			if(!this.sEmail0.equals(contact2.sEmail0)) result = false;
			if(!this.sEmail1.equals(contact2.sEmail1)) result = false;
			if(!this.sLanguage.equals(contact2.sLanguage)) result = false;
			
			return result;
		}
		
		public void setContactEmail(String email){
			sEmail0=email;
		}
		
		public String getContactEmail(){
			return sEmail0;
		}

	
}
