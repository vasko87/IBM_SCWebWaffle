/**
 * 
 */
package com.ibm.salesconnect.objects;

/**
 * @author timlehane
 * @date May 15, 2013
 */
public class Note {
		public String sSubject = "";
		public String sAssignedTo = "";
		public String sRelatedToName = "";
		public String sRelatedToType = "";
		public String sDescription = "";
		public String sAdditional_assignees="";
		public boolean bMyNotes = false;
		public boolean bMyFavorites=false;
		public String sAssignedUID = "";
		
		public void populate() {
			sSubject = "note " + System.currentTimeMillis();
			sDescription = "note description " + System.currentTimeMillis();
		}
		
		public void setsSubject(String newSubject){
			sSubject=newSubject;
		}
		
		public String  getsSubject(){
			return sSubject;
		}
		
}

