package com.ibm.salesconnect.common;

public class Interaction {
	public String Selector;
	public String Type;
	public String Choice;
	
	public Interaction(String selector, String type, String choice){
		Selector = selector;
		Type = type;
		Choice = choice;
	}
}
