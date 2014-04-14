package com.siva.expensetracker;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.siva.expensetracker.db.handler.ExpenseHandler;
import com.siva.expensetracker.dto.DBExpense;
import com.siva.expensetracker.util.ExpenseUtility;

public class MonthlyReportActivity extends Activity {

	GridView gridView;
	ExpenseHandler db = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monthly_report);
		
		
		 Calendar now = Calendar.getInstance();
		 int year = now.get(Calendar.YEAR);
		 int month = now.get(Calendar.MONTH); // Note: zero based!
		 
		 String str="";
		 str = year + " " + getMonthForInt(month) + " Month Expense Report";
		 
			TextView textView = (TextView) findViewById(R.id.textViewMonth);
			 textView.setText(str);
			  //Selvam Test

			 String strMonthYear="", strMonthPrefix ="";
			 
			  if(month <= 9 ){
				  strMonthPrefix = "/0";
		        }else{
		        	strMonthPrefix = "/";
		        }
			 
			  strMonthYear =  new String(new StringBuilder()
			  		.append(strMonthPrefix).append(month + 1).append("/")         // Month is 0 based, just add 1
					.append(year));
			  
			 gridView = (GridView) findViewById(R.id.gridViewMonthly);
				
				db = new ExpenseHandler(this);
				
				List<DBExpense> exp= db.getMonthExpenses(strMonthYear);
				//List<DBExpense> exp= db.getTodaysExpenses();
				
				//float amount = 100000;
			      
				TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
				
				 String log="";
				 Double tot = 0.00;
			     List<String> list = new ArrayList<String>();
			        
			        for (DBExpense cn : exp) {
			        	
			            log=  cn.getDate().substring(0,5);
			            list.add(log);
			            
		            	log = cn.getType();
			            //log = cn.getType();
			            //list.add(log);
			            
			            log= log + " " +cn.getExpenseName();
			            //log= cn.getExpenseName();
			            list.add(log);
			            
			            
			            //log=  Double.toString(cn.getAmount());
			            log=  ExpenseUtility.getCurrencyFormat(tm).format(cn.getAmount());
			            list.add(log);
			            
			            
			            tot+=cn.getAmount();
			        }
			        
			       
			        
			        if(list.size()==0){
			        	list.add(log);
			        }else{
			        	 list.add("");
					     list.add("Total");
					     list.add(ExpenseUtility.getCurrencyFormat(tm).format(tot));
			        }
			        	
			        
			        String[] values = new String[list.size()];
			        values = list.toArray(values);
				
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, values);
				gridView.setAdapter(adapter);
		 
				gridView.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					   Toast.makeText(getApplicationContext(),
						((TextView) v).getText(), Toast.LENGTH_SHORT).show();
					}
				});
				
				
	}
	 String getMonthForInt(int num) {
	        String month = "wrong";
	        DateFormatSymbols dfs = new DateFormatSymbols();
	        String[] months = dfs.getMonths();
	        if (num >= 0 && num <= 11 ) {
	            month = months[num];
	        }
	        return month;
	    }
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.monthly_report, menu);
		return true;
	}

}
