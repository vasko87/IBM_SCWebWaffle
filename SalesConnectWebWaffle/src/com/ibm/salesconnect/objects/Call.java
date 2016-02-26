/**
 * 
 */
package com.ibm.salesconnect.objects;

import java.util.Date;

import com.google.common.base.Strings;
import com.ibm.salesconnect.common.GC;

/**
 * @author timlehane
 * @date May 15, 2013
 */
public class Call {

		public String sSubject="";
		public String sCallCreated="";
		public String sCallDate="";
		public String sCallHours="";
		public String sCallMinutes="";
		public String sCallMeridiem="";
		public String sDuration="";
		public String sSummaryOfCall="";
		public String sAssignedToID="";
		public String sAssignedTo="";
		public String sCallStatus="";
		public String sCallDirection="";
		public String sRelatedType="";
		public String sRelatedTo="";
		public String sReminderTime="";
		public String sTeam="";
		public String sCallType="";
		public boolean bMyCalls = false;
		public boolean bMyFavorites = false;// Favorite
		public void populate() {
			sSubject = "call" + System.currentTimeMillis();
			sCallDirection = GC.gsInbound;
			sCallStatus = GC.gsPlanned;
			sDuration = GC.gsDuration00;
			sCallCreated = sDateAPIFormat(new Date());
		}
		
		@SuppressWarnings("deprecation")
		public String sDateAPIFormat(Date date){
			return String.valueOf(date.getYear() + 1900) + "-" + pad(String.valueOf(date.getMonth())) + "-" + pad(String.valueOf(date.getDate()))
					+ "T" + pad(String.valueOf(date.getHours())) + ":" + pad(String.valueOf(date.getMinutes())) + ":" + 
					pad(String.valueOf(date.getSeconds())) + ".000Z";
		}
		
		public String pad(String string){
			return Strings.padStart(string, 2, '0');
		}
		
		public void setCallSubject(String subject){
			sSubject = subject;
		}
		
		public String getCallSubject(){
			return sSubject;
		}
}

