package com.siva.expensetracker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.siva.expensetracker.db.handler.ContactHandler;
import com.siva.expensetracker.db.handler.ExpenseHandler;
import com.siva.expensetracker.dto.DBContacts;
import com.siva.expensetracker.dto.DBExpense;
import com.siva.expensetracker.dto.SMS;
import com.siva.expensetracker.util.ExpenseConstant;
import com.siva.expensetracker.util.ExpenseUtility;

public class MainActivity extends Activity {

	ExpenseHandler dbExp = null;
	ContactHandler dbCon = null;	
	TelephonyManager tm = null;

	String strContact1 = null, strContact2 = null;
	private void loadAllSMSExpensesDB(){
		
		List<DBContacts> listContacts = null;
		List<String> listAllContacts = new ArrayList<String>();
		if( dbCon.getContactsCount() == 0){
			Toast.makeText(getApplicationContext(), "Configure the Bank details in Preference for reading the bank SMS messages!", Toast.LENGTH_LONG).show();
			return;
		}else{
			listContacts = dbCon.getAllContacts();
		}

		
		  for (DBContacts cn : listContacts) {
			  listAllContacts.add(cn.getFromContactPurchaseNumber());
			  listAllContacts.add(cn.getFromContactATMNumber());
			  
	        }
		  
		Set<String> setAllContacts = new HashSet<String>(listAllContacts);		  
		  
		int count = 0;
		
		String sCountryCode = tm.getSimCountryIso();
		
		for(String strCon : setAllContacts) {
			
			for(SMS s : getAllSms()){
					if( s!= null 
						&& s.getAddress().equalsIgnoreCase(strCon) == true 
						&& s.getMsg().contains("credit") == false){
	
					long milliSeconds = Long.parseLong(s.getTime());
					String finalDateString = ExpenseUtility.convertMillisecoundsDatetoString(milliSeconds);
					
					count = Integer.parseInt(s.getId());
					String type ="", expname="";
					if(s.getMsg().contains(ExpenseConstant.RS_CONSTANT)==true){
						
						if(s.getMsg().contains(ExpenseConstant.EXPENSE_TYPE_KEYWORD_ATM)==true){
							type = ExpenseConstant.EXPENSE_TYPE_ATM;
							expname = ExpenseConstant.EXPENSE_NAME_ATM;
						}
						else if(s.getMsg().contains(ExpenseConstant.EXPENSE_TYPE_KEYWORD_PURCHASE)==true){
							type = ExpenseConstant.EXPENSE_TYPE_PURCHASE;
							expname = ExpenseConstant.EXPENSE_NAME_PURCHASE;
						}else if(s.getMsg().contains(ExpenseConstant.EXPENSE_TYPE_KEYWORD_DD)==true){
							//type = ExpenseConstant.EXPENSE_TYPE_DD;
							type = strCon;//ExpenseConstant.EXPENSE_TYPE_DD;
							expname = ExpenseConstant.EXPENSE_NAME_DD;
						}
						
						try{
							dbExp.addExpense(new DBExpense(count,finalDateString, type,expname,ExpenseUtility.getAmount(s.getMsg(),sCountryCode)));
						}catch(SQLException e){
							Toast.makeText(getApplicationContext(), "Not able to insert SMS Expenses!", Toast.LENGTH_SHORT).show();
						}
						
						}
					
				}
			}
		}
		//Log.d("selvamMessage",str);
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dbExp = new ExpenseHandler(this);
		dbCon = new ContactHandler(this);
		
		tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		
		int exCount =dbExp.getExpensesCount(); 
		if( exCount==0){
			loadAllSMSExpensesDB();
			//Toast.makeText(getApplicationContext(), "Expenses count:"+exCount, Toast.LENGTH_LONG).show();
		}
		//do-to count returns 0 validate
		
		//Selvam Test
		//String str="";
		 //TextView textView = (TextView) findViewById(R.string.hello_world);
		 //textView.setText(str);
		  //Selvam Test
		
	
		
	}

	
	
	public void WriteExpensetoDatabase(View view){
		//display in long period of time
		//Toast.makeText(getApplicationContext(), "msg msg", Toast.LENGTH_LONG).show();
		//loadAllSMSExpensesDB();
		//Monthly or 	Daily
		//Expanse Name
		//Expense Amount
		//Save Button
		
		Intent intent = new Intent(this, CustomExpensesActivity.class);
		startActivity(intent);
		
		//loadAllSMSExpensesDB();
	}
	
	
	public void DailyReport(View view){
	
		//SMS Report:
		//ATM Withdraw Report:
		Intent intent = new Intent(this, DailyReportActivity.class);
		startActivity(intent);
	}
	
	public void MonthlyReport(View view){
		//SMS Report:
		//ATM Withdraw Report:
		Intent intent = new Intent(this, MonthlyReportActivity.class);
		startActivity(intent);
	}
	
	public void DeleteReports(View view){
		/*
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		            //Yes button clicked
		        	dbExp.deleteAllExpense();
		        	Toast.makeText(getApplicationContext(), "Delete all the Expense Reports including SMS data! SMS expense load agin next startup", Toast.LENGTH_SHORT).show();
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked
		        	dbExp.deleteAllCustomExpenses();
		        	Toast.makeText(getApplicationContext(), "Delete Only the Custom Expense Reports!", Toast.LENGTH_SHORT).show();
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want delete all Expenses including SMS expenses data?").setPositiveButton("Yes", dialogClickListener)
		    .setNegativeButton("No", dialogClickListener).show();
		*/
		
		/* */
		// From Date // To Date
		String msg = "";
		
		if(dbExp.deleteAllCustomExpenses()==true){
			msg = "Delete all the Custom Expense Reports!";
		}else{
			msg = "No custom Expense Reports avilabel!";
		}
		final AlertDialog.Builder alert = new AlertDialog.Builder(
                this);
        alert.setTitle("Delete!");
        alert.setMessage(msg);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                            int which) {
                        dialog.cancel();
                    }
                });
        alert.show();
		
		/* */
		//dbExp.deleteAllExpense();
		
        
	}
	public void AboutUs(View view){
		
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	      /*AlertDialog alertDialog;
	        alertDialog = new AlertDialog.Builder(this).create();
	        alertDialog.setTitle("Packing List");
	        alertDialog.setMessage(countryCode);
	        alertDialog.show();*/
			
	      
	}
	
	
	public void PreferenceClick(View view){
		Intent intent = new Intent(this, ExpensePreferenceActivity.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public List<SMS> getAllSms() {
	    List<SMS> lstSms = new ArrayList<SMS>();
	    SMS objSms = new SMS();
	    Uri message = Uri.parse(ExpenseConstant.SMS_URI);
	    ContentResolver cr = getApplicationContext().getContentResolver() ;//mActivity.getContentResolver();

	    Cursor c = cr.query(message, null, null, null, null);
	    //mActivity.startManagingCursor(c);
	    //((Activity) getApplicationContext().getContentResolver()).startManagingCursor(c);
	    int totalSMS = c.getCount();

	    if (c.moveToFirst()) {
	        for (int i = 0; i < totalSMS; i++) {

	            objSms = new SMS();
	            objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
	            objSms.setAddress(c.getString(c
	                    .getColumnIndexOrThrow("address")));
	            objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
	            objSms.setReadState(c.getString(c.getColumnIndex("read")));
	            objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
	            
	            
	            if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
	                objSms.setFolderName("inbox");
	            } else {
	                objSms.setFolderName("sent");
	            }

	            lstSms.add(objSms);
	            c.moveToNext();
	        }
	    }
	    // else {
	    // throw new RuntimeException("You have no SMS");
	    // }
	    c.close();

	    return lstSms;
	}

}
