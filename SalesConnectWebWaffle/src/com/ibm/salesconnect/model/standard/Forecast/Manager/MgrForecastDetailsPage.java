package com.ibm.salesconnect.model.standard.Forecast.Manager;

import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

public class MgrForecastDetailsPage extends StandardPageFrame{

	protected static MgrForecastDetailsPage mgrForecastDetail;

	public MgrForecastDetailsPage(Executor exec) {
		super(exec);	
		Assert.assertTrue(isPageLoaded(), "Manager Forecast page has not loaded within 60 seconds");
	}

	@Override
	public boolean isPageLoaded() {
		return waitForPageLoad(pageLoaded);		
	}
	
	public static MgrForecastDetailsPage getInstance(){
		if(mgrForecastDetail == null){
			mgrForecastDetail = new MgrForecastDetailsPage(exec);
		}
		return mgrForecastDetail;			
	}
	
	//Selectors
	public static String pageLoaded = "//*[@id='s2id_autogen1']";
	
}