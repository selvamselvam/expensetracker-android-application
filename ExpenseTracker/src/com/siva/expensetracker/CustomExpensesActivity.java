package com.siva.expensetracker;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.siva.expensetracker.db.handler.ExpenseHandler;
import com.siva.expensetracker.dto.DBExpense;
import com.siva.expensetracker.util.ExpenseConstant;

public class CustomExpensesActivity extends Activity {

	private TextView Output;
    private Button changeDate;
    //EditText editTextExpenseName, editTextAmount;
    EditText mExpenseName, mAmount;
    TextView mAmountText;
    
    private int year;
    private int month;
    private int day;
    
    String strMonth = null, strDay = null;
 
    static final int DATE_PICKER_ID = 1111; 
    
    ExpenseHandler db = null;
    
    String date = null;
    Spinner spinner1 = null;
    
    private String getTodaysDate(){
    	  final Calendar c = Calendar.getInstance();
          year  = c.get(Calendar.YEAR);
          month = c.get(Calendar.MONTH);
          day   = c.get(Calendar.DAY_OF_MONTH);
   
          if(month <= 9 ){
          	strMonth = "/0";
          }else{
          	strMonth = "/";
          }
          if(day <= 9 ){
          	strDay = "0";
          }else{
          	strDay = "";
          }
          
          
      	date =  new String(new StringBuilder().append(strDay)
      			.append(day).append(strMonth).append(month + 1).append("/")         // Month is 0 based, just add 1
      			.append(year));
      	
          // Show current date
      	return date;
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_expenses);
		
		db = new ExpenseHandler(this);		
		
		Output = (TextView) findViewById(R.id.Output);
        changeDate = (Button) findViewById(R.id.buttonchangeDate);
         //mAmountText = (TextView) findViewById(R.id.textViewAmount);
         //NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
         //mAmountText.setText(R.string.custom_expense_amount + formatter.format(0));
         
        // Get current date by calender
         
        Output.setText(getTodaysDate());
  
        // Button listener to show date picker dialog
       
      changeDate.setOnClickListener(new OnClickListener() {
 
            @SuppressWarnings("deprecation")
			@Override
            public void onClick(View v) {
                 
                // On button click show datepicker dialog 
                showDialog(DATE_PICKER_ID);
 
            }
 
        });
      
      	
  		//addListenerOnButton();
  		//addListenerOnSpinnerItemSelection();
      
      
   }

	 public void addListenerOnSpinnerItemSelection() {
			spinner1 = (Spinner) findViewById(R.id.spinnerExpenseType);
			spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
		  }
	 
	// get the selected dropdown list value
	  public void addListenerOnButton() {
		 
		spinner1 = (Spinner) findViewById(R.id.spinnerExpenseType);

		Button btnSubmit = (Button) findViewById(R.id.button2);
	 
		btnSubmit.setOnClickListener(new OnClickListener() {
	 
		  @Override
		  public void onClick(View v) {
	 	      /*Toast.makeText(CustomExpensesActivity.this,
			"OnClickListener : " + 
	                "\nSpinner 1 : "+ String.valueOf(spinner1.getSelectedItem()) ,
				Toast.LENGTH_SHORT).show();
				*/
		  }
	 
		});
		
	  }
	  
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_PICKER_ID:
             
            // open datepicker dialog. 
            // set date picker for current date 
            // add pickerListener listner to date picker
            return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
 
        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                int selectedMonth, int selectedDay) {
             
            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;
 
            if(month <= 9 ){
            	strMonth = "/0";
            }else{
            	strMonth = "/";
            }
            
            if(day <= 9 ){
            	strDay = "0";
            }else{
            	strDay = "";
            }
            
        	date =  new String(new StringBuilder().append(strDay)
        		.append(day).append(strMonth).append(month + 1).append("/") //Month is 0 based, just add 1
    			.append(year));
            
            Output.setText(date );
     
           }
        };
	        
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.custom_expenses, menu);
		return true;
	}

	
	public void saveCustomExpenses(View view){
		
		String sCustomExpenseTitle = null, sCustomExpenseMessage = null;
	    mExpenseName   = (EditText)findViewById(R.id.editTextCustomExpense);
	    mAmount   = (EditText)findViewById(R.id.editTextAmount);
	    
	    
	    int count =  db.getCustomExpensesCount() + (year + month + day) + 1 ;
	   // int count =1;
	    boolean bsave = false;
	    
	    //if(date != null)
	    {
	    	try{
	    	db.addExpense(new DBExpense(count,date, ExpenseConstant.EXPENSE_TYPE_CUSTOM,
	    			mExpenseName.getText().toString(), Double.parseDouble(mAmount.getText().toString())));
	    	bsave = true;
	    	}catch(Exception e){ 
	    		Toast.makeText(getApplicationContext(), "Not able to insert Custom Expense!", Toast.LENGTH_SHORT).show();
	    	}

	    }
	 
	    final AlertDialog.Builder alert = new AlertDialog.Builder(
                this);
	    
	    if(bsave == true){
	    	alert.setIcon(R.drawable.ic_success);
	    	sCustomExpenseTitle = "Success!";
	    	sCustomExpenseMessage = "Successfully saved the expense Data!Do you want Enter new expense?";
	    	
	    }else{
	    	alert.setIcon(R.drawable.ic_stat_name);
	    	sCustomExpenseTitle = "Error!";
	    	sCustomExpenseMessage = "Not able to save the Expense Data!";
	    }
	   
        alert.setTitle(sCustomExpenseTitle);
        alert.setMessage(sCustomExpenseMessage);
        alert.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                            int which) {
                    	
                    	mExpenseName.setText("");
                    	mAmount.setText("");
                    	Output.setText(getTodaysDate());
                        
                    }
                });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
       	 public void onClick(DialogInterface dialog, int which) {
             finish();
       	 }
       	});
        alert.show();
        
        
        
       
        	 
        	 
	    
	}
}
