package com.ibm.salesconnect.objects;

import com.ibm.salesconnect.common.GC;

public class Adjustment {
	private static Adjustment adj;
	public String adj_description = "";
	public String adj_type = "";
	public String adj_client = "";
	public String adj_date = "";
	public String adj_roadmapstatus = "";
	public String adj_amount = "";
	public String adj_select = "";	
	public String adj_geography = "";	
	public String adj_industry = "";
	public String adj_gbsunit = "";
	public String adj_contract = "";
	public String adj_channel = "";
	
	
	public Adjustment(){
		populate();
	}
	
	public static Adjustment getInstance(){
		if(adj == null)
			adj = new Adjustment();
		return adj;
	}
	public void populate() {
		adj_description = GC.adj_description;		
		adj_type = GC.adj_type;
		adj_client = GC.adj_client;
		adj_date = GC.adj_date;
		adj_roadmapstatus = GC.adj_roadmapstatus;
		adj_amount = GC.adj_amount;
		adj_select = GC.adj_select;
		adj_geography = GC.mgr_adj_geography;		
		adj_industry = GC.mgr_adj_industry;
		adj_gbsunit = GC.mgr_adj_gbsunit;
		adj_contract = GC.mgr_adj_contract;
		adj_channel = GC.mgr_adj_channel;
	}
}
