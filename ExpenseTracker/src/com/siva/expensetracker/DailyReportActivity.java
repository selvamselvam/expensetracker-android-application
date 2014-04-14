package com.siva.expensetracker;

import java.util.ArrayList;
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

public class DailyReportActivity extends Activity {

	GridView gridView;
	 
	
	ExpenseHandler db = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily_report);
		
		gridView = (GridView) findViewById(R.id.gridView1);
		
		db = new ExpenseHandler(this);
		
		//NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		
		
		//List<DBExpense> exp= db.getAllExpenses();
		List<DBExpense> exp= db.getTodaysExpenses();
		
		 String log="";
	     List<String> list = new ArrayList<String>();
	     Double tot = 0.00;
	     
	        for (DBExpense cn : exp) {
	            log = cn.getType();
	            list.add(log);
	            log= cn.getExpenseName();
	            //log= cn.getDate();
	            list.add(log);
	            //log= Double.toString(cn.getAmount());
	            log = ExpenseUtility.getCurrencyFormat(tm).format(cn.getAmount());
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.daily_report, menu);
		return true;
	}

}
