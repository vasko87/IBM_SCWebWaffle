package com.ibm.salesconnect.model.standard.Forecast;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardViewPage;

public class RoadmapSummaryPage extends StandardViewPage{	
	Logger log = LoggerFactory.getLogger(RoadmapSummaryPage.class);
	
	int  dealListTotal, adjTotal, amountNum, statusNum;
	boolean flag = true;	
	protected static RoadmapSummaryPage sumPage;
	public WebDriver wd = (WebDriver) exec.getBackingObject();
	
	protected RoadmapSummaryPage(Executor exec) {
		super(exec);		
	}
	
	public static RoadmapSummaryPage getInstance(){
		if(sumPage == null){
			sumPage = new RoadmapSummaryPage(exec);
		}
		return sumPage;			
	}	

	public boolean verifyRoadmapStatusSum(String status) throws Exception{
		int roadmapSum = getRoadmapSum(status);					
		int dealListSum = getDealListSum();			
		int adjustmentSum = getAdjustmentSum(status);			
		
		if(roadmapSum == adjustmentSum+dealListSum){
			log.info("The sum of \""+status+"\" from RoadMap is "+roadmapSum);
			log.info("The sum of \""+status+"\" from Deal List is "+dealListSum);
			log.info("The sum of \""+status+"\" from Adjustment is "+adjustmentSum);
			log.info("The sum of \""+status+"\" from Deal Lists and Adjustment is "+(adjustmentSum+dealListSum));
			
			return true;
		}			
		else{
			log.info("The sum of \""+status+"\" from RoadMap is "+roadmapSum);
			log.info("The sum of \""+status+"\" from Deal List is "+dealListSum);
			log.info("The sum of \""+status+"\" from Adjustment is "+adjustmentSum);
			log.info("The sum of \""+status+"\" from Deal Lists and Adjustment is "+(adjustmentSum+dealListSum));	
			
			return false;
		}			
	}

	public int getRoadmapSum(String status) throws Exception{			
		int head = 0;
		int sumTol = 0;		
		
		WebElement summary = wd.findElement(By.xpath("//table[@id='manager-summary']/thead/tr[1]"));
		List<WebElement> summaries = summary.findElements(By.tagName("th")); 
		for(int a=0; a<summaries.size(); a++){
			if(summaries.get(a).getText().contains(status)){
				head = a+1;
			}
		}		
		WebElement tol = wd.findElement(By.xpath("//tbody/tr[@id='total_row']/td["+head+"]/span/a"));
		String headText = tol.getText();	
		String headTol = headText.substring(0,headText.length()-1);		
		sumTol = Integer.parseInt(headTol.replace("," , ""));
		tol.click();
		waitForAlertExpiration();
		
		return sumTol;
	}

	public boolean verifyNextPage(String pagePath) throws Exception{
		WebElement dealListPage = wd.findElement(By.xpath(pagePath));	
				
		if(dealListPage.getText().contains("+"))
			return true;
		else
			return false;
	}

	public int getDealListSum() throws Exception{		
		int amountNum = 0, dealListSum = 0;			
		String dealListTitles = "//table[contains(@class, 'columns')][contains(@id, 'roadmaps')]//thead/tr[1]";
		String dealListContext = "//table[contains(@class, 'columns')][contains(@id, 'roadmaps')]/tbody";
		String dealListPage = "//ul[contains(@class, 'Roadmaps')]//span[@class='pagination-text']";
		String dealListNextButton = "//ul[contains(@class, 'Roadmaps')]//button[@data-action='show-next']";			 
		
		WebElement titles = wd.findElement(By.xpath(dealListTitles));
		List<WebElement> title = titles.findElements(By.tagName("th")); 
		for(int a=0; a<title.size(); a++){
			if(title.get(a).getText().contains("Amount")){
				amountNum= a+1;
			}
		}		
		WebElement listBody = wd.findElement(By.xpath(dealListContext));
		List<WebElement> listTrs = listBody.findElements(By.tagName("tr")); 
		for(int a=1; a<=listTrs.size(); a++){				
			WebElement td = wd.findElement(By.xpath(dealListContext+"/tr["+a+"]/td["+amountNum+"]"));
			String amount = td.getText().substring(0,td.getText().length()-1);				 	
			dealListSum = Integer.parseInt(amount.replace("," , ""));								
			dealListTotal = dealListTotal + dealListSum;	
		}	 	
		if(verifyNextPage(dealListPage)){
			click(dealListNextButton);							
			waitForAlertExpiration();
			this.getDealListSum();
		}			
		return dealListTotal;				
	}
	
	public int getAdjustmentSum(String status) throws Exception{		
		int adjAmount = 0;		
		String adjTitles = "//table[@id='manager-adjustments']/thead/tr[1]";
		String adjContext = "//table[@id='manager-adjustments']/tbody";
		String dealListPage = "//ul[@class='nav results ibm_Adjustments']//span[@class='pagination-text']";
		String dealListNextButton = "//ul[@class='nav results ibm_Adjustments']//button[@data-action='show-next']";
		
		WebElement titles = wd.findElement(By.xpath(adjTitles));		
		List<WebElement> title = titles.findElements(By.tagName("th"));		
		if(flag){
			for(int i=0; i<title.size(); i++){					
				if(title.get(i).getText().contains("Roadmap Status")){					
					statusNum = i+1;					
				}				
				if(title.get(i).getText().contains("Amount")){
					amountNum = i+1;					
				}		
			}
		}				
		WebElement adjBody = wd.findElement(By.xpath(adjContext));
		List<WebElement> adjTrs = adjBody.findElements(By.tagName("tr"));
		for(int a=1; a<=adjTrs.size(); a++){
			WebElement statu = wd.findElement(By.xpath(adjContext+"/tr["+a+"]"+"/td["+statusNum+"]"));
			if(statu.getText().equalsIgnoreCase(status)){
				WebElement amount = wd.findElement(By.xpath(adjContext+"/tr["+a+"]"+"/td["+amountNum+"]"));
				if(amount.getText().contains("k")){
					String amt = amount.getText().substring(0,amount.getText().length()-1);	
					adjAmount = Integer.parseInt(amt.replace("," , ""));					
					adjTotal = adjTotal + adjAmount;
				}					
			}			
		}		
		if(verifyNextPage(dealListPage)){
			flag = false;
			click(dealListNextButton);				
			waitForAdjustmentAlertExpiration();
			this.getAdjustmentSum(status);
		}
		return adjTotal;
	}		
}
