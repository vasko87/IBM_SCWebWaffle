package com.ibm.salesconnect.test.svt;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.common.DateTimeUtility;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;

public class ScConnectionsAuth extends ProductBaseTest {

	Logger log = LoggerFactory.getLogger(ScConnectionsAuth.class);	

	//set to true to run a quick test run based on iTest iterations, 
	//or false to run the iterations for each task

	//path of output file for runtime stats. initialized globally, but the value is set in main,
	//after the default output path is loaded from the config file	
	String STATISTICS_FILE_PATH = "test-output\\statistics";
	String STATISTICS_FILE = STATISTICS_FILE_PATH + "\\SVT_StressTests_Stats.csv";

	@Test
	public void ScConnectionsAuth() {

		log.info("Start of test method: ScConnectionsAuth");

		try {

			//Create test-output directory
			createDir(STATISTICS_FILE_PATH);

			//create PrintWriter object to log the time statistics
			PrintWriter outStream = new PrintWriter(new FileWriter(STATISTICS_FILE));

			// Login User


			User user1 = commonUserAllocator.getUser(this);

			while (user1 != null ) {


				log.info("Logging in user: " + user1.getEmail());

				//Capture the start time at Login
				//String sRunTime = ("Login start time: " + DateTimeUtility.getDateTimeStamp() + "\n");

				//Start the timer for login
				DateTimeUtility.startTimer("Logging");
				Dashboard dashboard = launchWithLogin(user1);

				if(dashboard.checkForElement("//iframe[@id='bwc-frame']")){
					sleep(5);
					log.info("Switching to frame");
					//dashboard.switchToMainFrame();

					boolean auth = dashboard.clickAuthorizeConnections();

					//log the user 
					if (auth){
						outStream.println("Successful: " + user1.getEmail()+ "\n");
						outStream.flush();
					}
					else {
						outStream.println("Failed: " + user1.getEmail()+ "\n");
						outStream.flush();					
					}
				}

				exec.quit();

				user1 = commonUserAllocator.getUser(this);

			}



		} catch (Exception e) {
			log.error("\n*** Exception in Main: Exiting ***\n");
			e.printStackTrace();
			//System.exit(1);
		}

	}

	//Method to create the test-output folder
	public void createDir(String folder){

		//Directory flag set as true
		boolean dirFlag = false;

		//Create folder object
		File dir = new File(folder);

		//Flag becomes true if folder is created successfully
		try {
			dirFlag = dir.mkdir();
		} 
		catch (SecurityException Se) {
			log.info("Error creating directory:" + Se);
		}

		if (dirFlag)
			log.info("Directory created");
		else
			log.info("Directory was not created");

	}

}