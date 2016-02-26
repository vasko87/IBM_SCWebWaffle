package com.ibm.salesconnect.model.standard.Forecast.Seller;

import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

public class SellerForecastDetailsPage extends StandardPageFrame{

	protected static SellerForecastDetailsPage forecastSeller;

	public SellerForecastDetailsPage(Executor exec) {
		super(exec);	
		Assert.assertTrue(isPageLoaded(), "Seller Forecast page has not loaded within 60 seconds");
	}

	@Override
	public boolean isPageLoaded() {
		return waitForPageLoad(pageLoaded);		
	}
	
	public static SellerForecastDetailsPage getInstance(){
		if(forecastSeller == null){
			forecastSeller = new SellerForecastDetailsPage(exec);
		}
		return forecastSeller;			
	}
	
	//Selectors
	public static String pageLoaded = ".//*[@id='s2id_autogen1']";
	
}