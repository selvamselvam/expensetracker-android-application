package com.siva.expensetracker;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.siva.expensetracker.db.handler.ContactHandler;
import com.siva.expensetracker.dto.DBContacts;
import com.siva.expensetracker.util.ExpenseConstant;

public class ExpensePreferenceActivity extends Activity {

	  ListView listView ;
	  ContactHandler db = null;
	  EditText mEditatm; //mEditcard
	  private RadioGroup radioTypeGroup=null;
	 
	  
	  private void loadContent() {
	        listView = (ListView) findViewById(R.id.listView1);
	        radioTypeGroup = (RadioGroup) findViewById(R.id.radioType);
	        
	        List<DBContacts> contacts = db.getAllContacts();       
	        String log="";
	        String strText ="";
	        List<String> list = new ArrayList<String>();
	        //Bank Purchase Contact
	        // Bank Withdraw Contact
	        for (DBContacts cn : contacts) {
	            //log = "Id: "+cn.getID()+" ,Name: " + cn.getFromContactATMNumber() + " ,Phone: " + cn.getFromContactPurchaseNumber();
	        	//log = "Bank"+ cn.getID() +" Purchase: " + cn.getFromContactATMNumber();
	            //list.add(log);
	        	if(cn.getFromContactATMNumber().equalsIgnoreCase(ExpenseConstant.PRE_BANKCONTACT))
	        		strText = "SMS Contact No ";
	        	else if(cn.getFromContactATMNumber().equalsIgnoreCase(ExpenseConstant.PRE_ACCOUNTORCARDNUMBER))
	        		strText = "Account/Card No";
	        	
	            log = cn.getID() + " " + strText + " : " + cn.getFromContactPurchaseNumber();
	            
	            list.add(log);
	        }
	        
	        String[] values = new String[list.size()];
	        values = list.toArray(values);
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        		android.R.layout.simple_list_item_1, android.R.id.text1, values);
	        // Assign adapter to ListView
	        listView.setAdapter(adapter); 
		  
	        RadioButton rb1 = (RadioButton) findViewById(R.id.radioButton2);
	        rb1.setChecked(true);
	        
	  }
	  
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_preference);
		
		db = new ContactHandler(this);
		loadContent();
	}
	
	public void DeleteAllExpensesContacts(View view){
		db.deleteContactAll();
		loadContent();
	}
	
	private String getContactType(){
		RadioButton TheTextIsHere = (RadioButton) findViewById(radioTypeGroup.getCheckedRadioButtonId());
		String type = "";
		if(TheTextIsHere.getText().toString().equalsIgnoreCase(getApplicationContext().getString(R.string.preference_radio_BankSMSContact))==true)
			type = ExpenseConstant.PRE_BANKCONTACT;
		else if(TheTextIsHere.getText().toString().equalsIgnoreCase(getApplicationContext().getString(R.string.preference_radio_BankACOrCarddigits))==true)
			type = ExpenseConstant.PRE_ACCOUNTORCARDNUMBER;
		return type;
	}
	
	public void SaveExpensesContact(View view){
		mEditatm   = (EditText)findViewById(R.id.EditTextATMWithdraw);
		int count = db.getContactsCount() +1;
		String sCustomExpenseTitle="", sCustomExpenseMessage="";
		
		//db.addContact(new DBContacts(count,mEditcard.getText().toString(), mEditatm.getText().toString()));
		db.addContact(new DBContacts(count,getContactType(), mEditatm.getText().toString()));
		loadContent();
		
		 final AlertDialog.Builder alert = new AlertDialog.Builder(
	                this);
		    
		    //if(bsave == true){
		    	alert.setIcon(R.drawable.ic_success);
		    	sCustomExpenseTitle = "Success!";
		    	sCustomExpenseMessage = "Successfully saved the Preference Data!Do you want Enter new contact?";
		    	
		    //}else{
		    //	alert.setIcon(R.drawable.ic_stat_name);
		    //	sCustomExpenseTitle = "Error!";
		    //	sCustomExpenseMessage = "Not able to save the Expense Data!";
		   // }
		   
	        alert.setTitle(sCustomExpenseTitle);
	        alert.setMessage(sCustomExpenseMessage);
	        alert.setPositiveButton("Yes",
	                new DialogInterface.OnClickListener() {

	                    @Override
	                    public void onClick(DialogInterface dialog,
	                            int which) {
	                    	mEditatm.setText("");
	                    }
	                });
	        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
	       	 public void onClick(DialogInterface dialog, int which) {
	             finish();
	       	 }
	       	});
	        alert.show();
	        
	        
        /*AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Packing List");
        alertDialog.setMessage(temp);
        alertDialog.show();
		 */
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expense_preference, menu);
		return true;
	}

}
