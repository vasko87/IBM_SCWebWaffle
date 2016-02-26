package com.ibm.salesconnect.common;

import java.io.File;


public class Wrapper{
	
	public static void main(String[] args){

		String jarName = findJarName();
		if(jarName == null) {
			jarName = "SalesConnectAuto.jar";
		}
		String[] arguments = {"test_config/AT_Sugar_Grid.xml"};
		
		if(args.length == 0){	
			org.testng.TestNG.main(arguments);
		}
		else if((args[0].equalsIgnoreCase("-xmlpathinjar")) && (args.length==2)){;
			arguments = new String[] {"-xmlpathinjar", args[1]};
			org.testng.TestNG.main(arguments);
		}
		else if((args[0].equalsIgnoreCase("-testngxml")) && (args.length==2)){
			org.testng.TestNG.main(new String[] {args[1]});
		}
		else if((args[0].equalsIgnoreCase("-level")) && (args.length==2)){
			String[] newParams = {selectTestngXmlFile(Integer.parseInt(args[1]))};
			org.testng.TestNG.main(newParams);
		}
		else{
			System.out.println("\nInvalid parameter(s) specified\n");
			outputHelp();
		}

	}
	
	public static String selectTestngXmlFile(int level){
		String testngXmlFile = "";
		String testngXmlFilePath = "test_config/"; 
		switch (level) {
		case 0:
			testngXmlFile = testngXmlFilePath + "Level0.xml";
			break;
		case 1:
			testngXmlFile = testngXmlFilePath + "Level1.xml";
			break;
		case 2:
			testngXmlFile = testngXmlFilePath + "Level2.xml";
			break;
		case 3:
			testngXmlFile = testngXmlFilePath + "Level3.xml";
			break;
		default:
			System.out.println("\nInvalid level specified\n");
			outputHelp();
			break;
		}
		return testngXmlFile;
	}
	
	public static void outputHelp(){
		System.out.println("Usage:");
		System.out.println("Default (No Parameters) : Run Sugar acceptance tests \n");
		System.out.println("-testngxml filename: Name of locally stored testngxml file to run with\n");
		System.out.println("-level : Level of automation testing to run\n");
		System.out.println("     -level 0 : Run All tests\n");
		System.out.println("     -level 1 : Run Basic BVT\n");
		System.out.println("     -level 2 : Run Extended BVT\n");
		System.out.println("     -level 3 : Run Full Regression\n");
		System.exit(1);
	}
	
	private static String findJarName() throws IllegalStateException {
		String jarPath = new File(Wrapper.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath();
		String vals[] = jarPath.split(File.separator.equals("\\") ? "\\\\" : File.separator);
		return vals[vals.length-1];
	}
}