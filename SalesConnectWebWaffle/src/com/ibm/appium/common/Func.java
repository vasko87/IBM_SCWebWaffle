package com.ibm.appium.common;

public class Func {
	
	public static int RandomRange(int min, int max){
		return min + (int)(Math.random()*max);
	}
}
