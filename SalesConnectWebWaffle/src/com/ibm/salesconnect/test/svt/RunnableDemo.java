/**
 * 
 */
package com.ibm.salesconnect.test.svt;

import com.ibm.salesconnect.common.HttpUtils;

/**
 * @author timlehane
 * @date Jun 27, 2014
 */
	public class RunnableDemo implements Runnable {
		   private Thread t;
		   private String threadName;
		   private String[] runHeader = null;
		   
		   RunnableDemo( String[] header){
			   runHeader = header;
		   }
		   
//		   @Override
//		   public void run() {
//		    	  try {
//		    		  //String[] runHeader = API_CreateTasksQuick.apiLogin("ie01@tst.ibm.com");
//		    		  //System.out.println("in here");
//		    		  HttpUtils restCalls = new HttpUtils();
//		    		  	String respones = restCalls.postRequest("https://w3-dev.api.ibm.com/sales/development/salesconnect/tasks?client_id=59ed11d5-9272-468d-af0d-c2cbbd35c3c0&client_secret=E2vH1pD3uM5xQ7vW7bJ7vM3nE6kH2pT8oI8rK8rU6lJ8hG5uL2",
//		    		  			runHeader, 
//		    		  			"{\"name\":\"Base task\",\"date_due\":\"2013-10-28T15:14:00.000Z\",\"priority\":\"High\",\"status\":\"Not Started\",\"call_type\":\"Close_out_call\",\"assigned_user_id\":\"92c65ed0-cbf5-981d-4515-543f421c2f06\"}"
//		    		  	, "application/json");
//		    			//String respones = restCalls.getRequest("https://w3-dev.api.ibm.com/sales/development/salesconnect/opportunities?my_items=1&fields=name&client_id=b8dd9731-c359-409b-ba53-564e6e197a86&client_secret=V3kL7kV4wH5gE0wP7rG4sI7jX5yB8hH7wI7uB5gB8qA0oT4jR8",
//		    			//		runHeader, "200");
//		    			
//		    			System.out.println(respones);
//					//API_CreateTasksQuick.api_createTask(runHeader,"temp");
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//		    
//		   }
		   
//		   @Override
//		   public void run() {
//		    	  try {
//
//		    		  HttpUtils restCalls = new HttpUtils();
//		    		  	String respones = restCalls.postRequest("https://w3-dev.api.ibm.com/sales/development/salesconnect/leads?client_id=59ed11d5-9272-468d-af0d-c2cbbd35c3c0&client_secret=E2vH1pD3uM5xQ7vW7bJ7vM3nE6kH2pT8oI8rK8rU6lJ8hG5uL2",
//		    		  			runHeader, 
//		    		  			 "{\"name\":\"bulk name\",\"description\":\"bulk description\",\"first_name\":\"one\",\"last_name\":\"smith\"," +
//		    		  					"\"phone_mobile\":\"123456789\",\"primary_address_country\":\"US\",\"status\":\"LEADPROG\", \"status_detail_c\":\"PROGINIT\", \"lead_source\":\"LSCAMP\"}"
//		    		  	, "application/json");
//		    			System.out.println(respones);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}	    
//		   }
		   
		   @Override
		   public void run() {
		    	  try {

		    		  HttpUtils restCalls = new HttpUtils();
		    		  	String respones = restCalls.postRequest("https://svt4sugarlb01a.rtp.raleigh.ibm.com/sales/salesconnect/rest/v10/Leads",
		    		  			runHeader, 
		    		  			 "{\"name\":\"bulk name\",\"description\":\"bulk description\",\"first_name\":\"John\",\"last_name\":\"Smith\"," +
		    		  					"\"phone_mobile\":\"123456789\",\"primary_address_country\":\"US\",\"status\":\"LEADPROG\", \"status_detail_c\":\"PROGINIT\", \"lead_source\":\"LSCAMP\"}"
		    		  	, "application/json");
		    			System.out.println(respones);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	    
		   }

		}


