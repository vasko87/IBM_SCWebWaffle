package com.ibm.salesconnect.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class DateTimeUtility {
	
	public static HashMap<String,Long> TimeSystem = new HashMap<String, Long>();
	
	/**
	 * Add time to map
	 * @param string identifier
	 */
	public static void startTimer(String id){
		TimeSystem.put(id, System.currentTimeMillis());
	}
	
	/**
	 * Stop the timer 
	 * @param string identifier
	 * @return the difference from current time to start time, in miliseconds
	 */
	public static long stopTimer(String id){
		return (System.currentTimeMillis() - TimeSystem.get(id));
	}

	/**
	 * Reset key to zero
	 * @param string identifier
	 */
	public static void clearTimer(String id){
		TimeSystem.put(id, 0L);
	}
	
	/** 
	 * Generates String containing a Date Time Stamp	 <p>
	 * @return a String containing a date-based unique value
	 */
	public static String getDateTimeStamp()
	{
		//generates String containing unique random date-based value	
		Date d = new Date();

		//format
		SimpleDateFormat tmformat = new SimpleDateFormat("yyyyMMdd.HHmmss");

		return tmformat.format(d);

	}
	
	public Date convertStringToDate(String string){
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar cal = Calendar.getInstance();

        Date date = null;
		try {
			date = simpleDateFormat.parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
		}

    return date;
}
	
	/** 
	 * Generates String containing a Date Time Stamp with no punctuation for use as unique date based num <p>
	 * @return a String containing a date-based unique value
	 */
	public static String getDateTimeStampNoPunct()
	{
		//generates String containing unique random date-based value	
		Date d = new Date();

		//format
		SimpleDateFormat tmformat = new SimpleDateFormat("yyMMddHHmm");

		return tmformat.format(d);

	}
	
	/** 
	 * Generates String containing a Date separated by /
	 * @return a String containing a date-based unique value
	 */
	public static String getDateSlashSeparated()
	{
		//generates String containing unique random date-based value	
		Date d = new Date(System.currentTimeMillis());

		//format
		SimpleDateFormat tmformat = new SimpleDateFormat("MM/dd/yyyy");

		return tmformat.format(d);

	}
	
	/** 
	 * Generates String containing a Date separated by /
	 * @return a String containing a date-based unique value
	 */
	public static String getDateSlashSeparated(long offsetInDays)
	{
		//generates String containing unique random date-based value	
		Date d = new Date(System.currentTimeMillis() + (86400000 * offsetInDays));

		//format
		SimpleDateFormat tmformat = new SimpleDateFormat("MM/dd/yyyy");

		return tmformat.format(d);

	}

}
