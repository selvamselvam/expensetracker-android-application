package com.siva.expensetracker.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.telephony.TelephonyManager;

public class ExpenseUtility {

	public static long convertStringDatetoMillisecounds(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

		String inputString = "00:01:30.500";

		Date date = null;
		try {
			date = sdf.parse("1970-01-01 " + inputString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("in milliseconds: " + date.getTime());
		return date.getTime();
	}
	
	public static String convertMillisecoundsDatetoString(long milliSeconds) {
		//long milliSeconds = Long.parseLong(s.getTime());
		java.text.DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		String finalDateString = formatter.format(calendar.getTime());
		return finalDateString;
	}
	
	public static NumberFormat getCurrencyFormat(TelephonyManager tm){
		
	     String countryCode = tm.getSimCountryIso();
	     if( countryCode == null)
	    	 countryCode = "US";
	     
	    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en",countryCode.toUpperCase()));
	    
		return formatter;
	}
	
	public static double getAmount(String strMessage,String sCountryCode){
		
		double d  = 0.0;
		String ss="";
		boolean findFirst = true;
		
		Pattern p = Pattern.compile("[" +ExpenseConstant.CURRENCY_SYMBOLS + "][\\d,]+");
        Matcher m = p.matcher(strMessage);

        int i=0;
        while (m.find()) {
        	if(i==0){
        		ss= m.group().toString().substring(1);
        		break;
        	}
            i++;
        }
		
		try{
			d = Double.parseDouble(ss);
		}catch(NumberFormatException e){
			findFirst = false;
		}
		
		
		
		if(findFirst == false){
			int rsindex = strMessage.indexOf(ExpenseConstant.RS_CONSTANT, 0);
			
			boolean r = false;
			
			if(rsindex!= -1){
				for(i=rsindex;i<strMessage.length();i++){
					if(Character.isDigit(strMessage.charAt(i)) == true){
							ss+=strMessage.charAt(i);
							r = true;
					}
					if(r == true && Character.isDigit(strMessage.charAt(i)) == false)
						break;
				}
				
			}else{
				//Log.e(str,"selvamMessage:"+str);
			}
		}
		
	
		try{
			d = Double.parseDouble(ss);
		}catch(NumberFormatException e){
			d = 0.0;
		}
		
		return d;
	}
	
	
}
