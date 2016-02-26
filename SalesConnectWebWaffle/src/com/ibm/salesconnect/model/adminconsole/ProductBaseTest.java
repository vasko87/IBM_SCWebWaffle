package com.ibm.salesconnect.model.adminconsole;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.ibm.atmn.waffle.base.BaseSetup;
import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.atmn.waffle.extensions.user.UserAllocation;
import com.ibm.atmn.waffle.log.LogManager;
import com.ibm.salesconnect.PoolHandling.Client.ClientAllocation;
import com.ibm.salesconnect.PoolHandling.Opportunity.OpportunityAllocation;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Login;
import com.ibm.salesconnect.model.standard.Connections.ConnectionsLogin;
import com.ibm.salesconnect.model.standard.Connections.Community.CommunityMainPage;
import com.ibm.salesconnect.model.standard.Connections.Files.FilesMainPage;

public class ProductBaseTest extends BaseSetup {
    Logger log = LoggerFactory.getLogger(ProductBaseTest.class);
    private static String absolutePath = Class.class.getClass().getResource("/").getPath();
    private static String getAbsolutePath(String relativePath) {
        if(absolutePath.contains("build")) {
            return relativePath;
        } else {
            if(absolutePath.contains("SalesConnectWebWaffleDev")) {
                absolutePath = absolutePath.replace("SalesConnectWebWaffleDev", "SalesConnectWebWaffle");
            }
            if(absolutePath.contains("bin")){
            	return absolutePath.substring(0, absolutePath.lastIndexOf("bin")) + relativePath;
            }
            return absolutePath + relativePath;
        }
    }

    protected static final UserAllocation commonUserAllocator = UserAllocation
            .getUserAllocation(getAbsolutePath("test_config/extensions/user/default_users.properties"));

    protected static final ClientAllocation commonClientAllocator = ClientAllocation
    .getClientAllocation(getAbsolutePath("test_config/extensions/client/default_clients.properties"));

    protected static final OpportunityAllocation commonOpportunityAllocator = OpportunityAllocation
    .getOpportunityAllocation(getAbsolutePath("test_config/extensions/opportunity/default_opportunities.properties"));

    protected static final String businessPartnerfilePath = "test_config/extensions/businessPartner/bp.csv";
    
    public static String baseURL = null;

	/**
	 * Launch to url for StartPage and expect request to be intercepted for
	 * authentication.
	 * 
	 * TODO: Add a configuration option to turn off the login expectation so
	 * that tests can be used to test Kerberos SSO systems
	 * 
	 * @param target
	 * @param user
	 */
	public Dashboard launchWithLogin(User user) {
		exec.load(testConfig.getBrowserURL()+GC.baseURLextension);
		exec.maximiseWindow();
		if (user != null) {
			Login loginPage = new Login(exec);
			loginPage.login(user.getEmail(), user.getPassword());
		}
		return new Dashboard(exec);
	}
	
	/**
	 * Get multiple users while avoiding clashes
	 */
	public String[] getMultipleUsers(int numMembers, Object obj){
		String[] team;
		if(numMembers>=1){
		 team = new String[numMembers];
		
		 for (int i = 0; i < numMembers; i++) {
			 String email =  commonUserAllocator.getUser(obj).getEmail();
			 Boolean add = true;
			 for (int j = 0; j < team.length; j++) {
				if (email.equalsIgnoreCase(team[j])) {
					add = false;
				}
			 }
			 if(add){
				 team[i]=email;
			 }
			 else {
				i--;
			}
		 	
		 }	
		return team; 
		}
		log.info("Team size is less than 1");
		return null;
	}

	public void launchWithoutLogin() {
		launchWithLogin(null);
	}
	
	/**
	 * Launch to Connections Community url 
	 * 
	 * @param user
	 */
	public CommunityMainPage launchCnxnCommunity(User user) {
		System.out.println(getParameter(GC.cxnURL) +"communities");
		exec.load(getParameter(GC.cxnURL) +"communities");

		exec.maximiseWindow();
		if (user != null) {
			ConnectionsLogin cnxnLoginPage = new ConnectionsLogin(exec);
			cnxnLoginPage.login(user.getEmail(), user.getPassword());
		}
		return new CommunityMainPage(exec);

	}
	
	public String getCnxnCommunity() {
		return testConfig.getParameter("cxnurl");
	}
	
	public String getParameter(String parameter){
		return testConfig.getParameter(parameter);
	}
	
	public String getApiManagement(){
		return testConfig.getParameter("apimanagement");
	}

	public String getBrowserUrl() {
		return testConfig.getParameter("browser_url")+"/";
	}
	
	/*public String getclient_ID(){
		return "client_id=" +testConfig.getParameter("clientid");
	}
	
	public String getclient_secret(){
		return "client_secret=" + testConfig.getParameter("clientsecret");
	}*/
	
	
	/**
	 * Launch to Connections Files url
	 *  
	 * @param user
	 */
	public FilesMainPage launchCnxnFiles(User user) {
		exec.load(getParameter(GC.cxnURL)+"files/app");
		
		exec.maximiseWindow();
		if (user != null) {
			ConnectionsLogin cnxnLoginPage = new ConnectionsLogin(exec);
			cnxnLoginPage.login(user.getEmail(), user.getPassword());
		}
		return new FilesMainPage(exec);

	}

	/**
	 * Create specified file and directory (if directory does not exists)
	 * (i.e. createFile("C:\\Documents\\", "myFile", "myFileContent")
	 * @param sDirName, sFileName, sConent
	 * @return boolean true if file was created
	 */
	public boolean createFile(String sDirName, String sFileName, String sContent) {

		String sPath = sDirName + "/" + sFileName;
		File file = new File(sPath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		try {
			file.createNewFile();
		} catch (IOException e) {
			Assert.assertTrue(false,e.getMessage());
		}

		try {
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sContent);
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			Assert.assertTrue(false,e.getMessage());
		}
		try {
			FileReader fr = new FileReader(file);
			BufferedReader bReader = new BufferedReader(fr);
			String string = bReader.readLine();
			if(string.equalsIgnoreCase(sContent))
				return true;		
		} catch (FileNotFoundException e) {
			Assert.assertTrue(false,e.getMessage());
		} catch (IOException e) {
			Assert.assertTrue(false,e.getMessage());
		}
		return false;
	}
	
	/**
	 * Initiates a Thread.sleep - catches exception so tasks don't have to throw it
	 * @param iWait - time to wait in seconds
	 */
	public static void sleep (int iWait){
		try {
			Thread.sleep(iWait*1000);

		}catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@AfterMethod(alwaysRun=true)
	@Override
	public void afterMethod(ITestResult result, Method method) {
		commonUserAllocator.checkInAllUsersWithToken(this);
		commonUserAllocator.checkInAllGroupUsersWithToken(GC.db2UserGroup, this);
		commonUserAllocator.checkInAllGroupUsersWithToken("dach_users", this);
		commonUserAllocator.checkInAllGroupUsersWithToken("us_users", this);
		commonUserAllocator.checkInAllGroupUsersWithToken(GC.busAdminGroup, this);
		commonUserAllocator.checkInAllGroupUsersWithToken(GC.nonCchFnIdGroup, this);
		commonUserAllocator.checkInAllGroupUsersWithToken(GC.cchFnIdGroup, this);
		commonUserAllocator.checkInAllGroupUsersWithToken("dummy", this);
		commonUserAllocator.checkInAllGroupUsersWithToken(GC.gbsMgr, this);
		commonUserAllocator.checkInAllGroupUsersWithToken(GC.gbsSel, this);
	//	commonUserAllocator.checkInAllGroupUsersWithToken(GC.e2eUserGroupAG, this);
	//	commonUserAllocator.checkInAllGroupUsersWithToken(GC.e2eUserGroupAP, this);
	//	commonUserAllocator.checkInAllGroupUsersWithToken(GC.e2eUserGroupEU, this);
		commonClientAllocator.checkInAllclientsWithToken(this);
		commonClientAllocator.checkInAllGroupClientWithToken(GC.DC, this);
		commonClientAllocator.checkInAllGroupClientWithToken(GC.SC, this);
		commonClientAllocator.checkInAllGroupClientWithToken(GC.E2EAG, this);
		commonClientAllocator.checkInAllGroupClientWithToken(GC.E2EAP, this);
		commonClientAllocator.checkInAllGroupClientWithToken(GC.E2EEU, this);
		commonClientAllocator.checkInAllGroupClientWithToken("RES", this);
		commonClientAllocator.checkInAllGroupClientWithToken(GC.GbClient, this);
		commonClientAllocator.checkInAllGroupClientWithToken(GC.GuClient, this);
		commonOpportunityAllocator.checkInAllopportunitiesWithToken(this);
		commonOpportunityAllocator.checkInAllGroupOpportunityWithToken("1", this);
		commonOpportunityAllocator.checkInAllGroupOpportunityWithToken("2", this);
		commonOpportunityAllocator.checkInAllGroupOpportunityWithToken("3", this);

		try{
		exec.close();
		}
		catch (Exception e) {
			log.info("Closing of webdriver instance failed, this is either due to the preceding test being api only or an issue with the test");
		}
	
		super.afterMethod(result, method);
		LogManager.stopTestLogging();
	}
	
	@BeforeMethod(alwaysRun=true)
	@Override
	public void beforeMethod(ITestContext context, Method method) {
		LogManager.startTestLogging(context.getSuite().getName()+"-"+context.getName()+"-"+method.getName());
		super.beforeMethod(context, method);
	}
	
	@BeforeClass(alwaysRun=true)
	@Override
	public void beforeClass(ITestContext context) {
		super.beforeClass(context);
		baseURL = testConfig.getBrowserURL();
		System.out.println(baseURL);

	}


}
