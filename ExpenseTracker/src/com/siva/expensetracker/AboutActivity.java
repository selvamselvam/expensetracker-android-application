package com.siva.expensetracker;

import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.widget.TextView;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		String str = "";
		str = "<b>SMS Expense Tracker</b> "+
		"<p align='center'> "+
		"SMS Expense Tracker is simple easy to use Android application which use to track the expenses." + 
		"</p>" +

		"<p> Unique Future: </p>" +
		"<p> Read the multiple Bank ATM/Purchase SMS/Alert messages and track the expense</p>" +
		"<p> Option to track the currency using custom Expense</p>" +
		"<p> Provide Daily and Monthly Reports for analysing the expenses</p>" +
		"<p> Preference to configure multiple bank contact for tracking the expenses</p>" +
		"<p> No manual entry for expenses using Debit/Credit Card purchase/withdraw</p>" +
		"<a href=\"mailto:selvamselvam@hotmail.com?subject=ExpenseTracker\">Send Feedback</a>" ;
				
		//TextView textView = (TextView) findViewById(R.id.textViewAbout);
		//textView.setText(str);
		 
		TextView feedback = (TextView) findViewById(R.id.textViewAbout);
		feedback.setText(Html.fromHtml(str));
		feedback.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}

}
