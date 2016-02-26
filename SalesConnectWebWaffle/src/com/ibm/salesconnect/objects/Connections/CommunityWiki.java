package com.ibm.salesconnect.objects.Connections;

import com.ibm.salesconnect.common.GC;

public class CommunityWiki {
	public String sWikiTitle="";
			
	public void populate() {
		sWikiTitle = "BVT Community Wiki" + GC.sUniqueNum;
	}
}

