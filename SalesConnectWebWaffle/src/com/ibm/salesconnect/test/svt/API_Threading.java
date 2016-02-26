package com.ibm.salesconnect.test.svt;


public class API_Threading extends Thread{
	private int count;
	private String id, name, module;
	String[] header;
	
	public API_Threading(String[] header, String id, String name, String module, int count){
		this.id = id;
		this.header = header;
		this.name = name;
		this.module = module;
		this.count = count;
	}
	@Override
	public void run(){
		try{
			String result = API_CreateTasks.api_createTask(header, id, name, module);
		}
		catch(Exception e){
			//Log.info("Thread failed: " + e);
		}
	}
}
