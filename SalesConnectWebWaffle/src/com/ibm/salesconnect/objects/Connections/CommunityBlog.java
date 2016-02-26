package com.ibm.salesconnect.objects.Connections;

import com.ibm.salesconnect.common.GC;

public class CommunityBlog {
	public String sBlogEntryTitle = ""; 
	public String sBlogEntryTag = ""; 
	public String sBlogEntryDescription = "";
		
	public void populate() {
		sBlogEntryTitle = "BVT Community Blog Entry" + GC.sUniqueNum;
		sBlogEntryTag = "BlogTag"; 
		sBlogEntryDescription = "Blog Description";
	}
}


