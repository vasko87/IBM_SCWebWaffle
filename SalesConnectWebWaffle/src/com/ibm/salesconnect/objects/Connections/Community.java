package com.ibm.salesconnect.objects.Connections;

import com.ibm.salesconnect.common.GC;

public class Community {
	public String sStartACommunity = "";
	public String sCommunityName = "";
	public String sCommunityTag = "";
	public String sCommunityHandle = "";
	public String sCommunityDescription = "";
		
	public void populate() {
		sCommunityName = "BVT Community"+ GC.sUniqueNum;
		sCommunityDescription = "BVT Community Description"+GC.sUniqueNum;
	}
}
