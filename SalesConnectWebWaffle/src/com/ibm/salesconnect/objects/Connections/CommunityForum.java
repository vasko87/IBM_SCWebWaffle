package com.ibm.salesconnect.objects.Connections;

import com.ibm.salesconnect.common.GC;

public class CommunityForum {
	public String sForumTopicTitle = "";
	public String sForumPostTitle = "";
			
	public void populate() {
		sForumTopicTitle = "BVT Community Forum" + GC.sUniqueNum;
		sForumPostTitle = "BVT Community Post" + GC.sUniqueNum;
	}
}