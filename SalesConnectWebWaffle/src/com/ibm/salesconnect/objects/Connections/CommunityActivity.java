package com.ibm.salesconnect.objects.Connections;

import com.ibm.salesconnect.common.GC;

public class CommunityActivity {
	public String sActivityTitle = ""; 
	public String sActivityTags = ""; 
	public String sActivityDescription = "";
	
	public void populate() {
		sActivityTitle = "BVT Community Activity"+GC.sUniqueNum;
		sActivityTags = "ActivityTag"; 
		sActivityDescription = "Activity Description";
	}
}
