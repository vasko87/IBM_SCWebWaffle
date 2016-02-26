/**
 * 
 */
package com.ibm.salesconnect.objects;

import com.ibm.salesconnect.common.GC;

/**
 * @author timlehane
 * @date May 14, 2013
 */
public class Task {

		//Same short and long Form Fields
		public String sName="";
		public String sDueDate="";
		public String sDueDateHours="";
		public String sDueDateMinutes="";
		public String sDueDateMeridiem="";
		public String sPriority="";
		public String sTaskType="";
		public String sAssignedTo="";
		public String sAdditionalAssignee = "";
		public String sDescription="";
		public String sStatus="";
		public String sRelatedToType="";
		public String sRelatedTo="";
		public String sContactName="";
		public boolean bMyTasks=false;
		public boolean bMyFavorites=false;
		
		public void populate(){
			sName = "mytask" + System.currentTimeMillis();
			sPriority = GC.gsHigh;
			sStatus = GC.gsInProgress;
			sDescription="task description"+ System.currentTimeMillis();
			
		}
		
		public void setTaskName(String taskName){
			sName = taskName;
		}
		
		public String getTaskName(){
			return sName;
		}
}
