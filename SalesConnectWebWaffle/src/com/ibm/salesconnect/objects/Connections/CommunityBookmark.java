package com.ibm.salesconnect.objects.Connections;

import com.ibm.salesconnect.common.GC;

public class CommunityBookmark {
	public String sBookmarkURL = "w3.ibm.com"; 
	public String sBookmarkName = "BVT Community Bookmark";
			
	public void populate() {
		sBookmarkURL = "w3.ibm"+GC.sUniqueNum+".com"; 
		sBookmarkName = "BVT Community Bookmark" + GC.sUniqueNum;
	}
}
