package com.siva.expensetracker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.siva.expensetracker.db.handler.ContactHandler;
import com.siva.expensetracker.db.handler.ExpenseHandler;
import com.siva.expensetracker.dto.DBContacts;
import com.siva.expensetracker.dto.DBExpense;
import com.siva.expensetracker.dto.SMS;
import com.siva.expensetracker.util.ExpenseConstant;
import com.siva.expensetracker.util.ExpenseUtility;

public class IncomingSms extends BroadcastReceiver {

	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private static final String TAG = "SMSBroadcastReceiver";

	ContactHandler dbCon = null;
	ExpenseHandler dbExp = null;
	TelephonyManager tm = null;
	
	@Override
	public void onReceive(Context context, Intent intent) {

		dbCon = new ContactHandler(context);
		dbExp = new ExpenseHandler(context);
		tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		
		List<DBContacts> listContacts = null;
		List<String> listAllContacts = new ArrayList<String>();
		if (dbCon.getContactsCount() == 0)
			return;
		else
			listContacts = dbCon.getAllContacts();

		for (DBContacts cn : listContacts) {
			listAllContacts.add(cn.getFromContactPurchaseNumber());
			listAllContacts.add(cn.getFromContactATMNumber());

		}

		Set<String> setAllContacts = new HashSet<String>(listAllContacts);

		SMS objSms = new SMS();

		Log.i(TAG, "Intent recieved: " + intent.getAction());

		if (intent.getAction().equals(SMS_RECEIVED)) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");
				final SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				}
				if (messages.length > -1) {

					// Toast.makeText(context, "Message recieved: " +
					// messages[0].getMessageBody(), 7000).show();
					objSms = new SMS();
					objSms.setId(String.valueOf(messages[0]
							.getProtocolIdentifier())); // getthe current SMS
														// inbox id and increent
														// 1
					objSms.setAddress(messages[0]
							.getDisplayOriginatingAddress());
					objSms.setMsg(messages[0].getMessageBody());
					objSms.setReadState(String.valueOf(messages[0].getStatus()));
					objSms.setTime(String.valueOf(messages[0]
							.getTimestampMillis()));
					objSms.setFolderName("inbox");

				}
			}
		}

		String sCountryCode = tm.getSimCountryIso();
		
		for (String strCon : setAllContacts) {
			if (objSms != null
					&& objSms.getAddress().equalsIgnoreCase(strCon) == true 
					&& objSms.getMsg().contains("credit") == false) {

				long milliSeconds = Long.parseLong(objSms.getTime());
				String finalDateString = ExpenseUtility
						.convertMillisecoundsDatetoString(milliSeconds);

				int count = Integer.parseInt(objSms.getId());
				String type = "", expname = "";
				if (objSms.getMsg().contains(ExpenseConstant.RS_CONSTANT) == true) {

					if (objSms.getMsg().contains(
							ExpenseConstant.EXPENSE_TYPE_KEYWORD_ATM) == true) {
						type = ExpenseConstant.EXPENSE_TYPE_ATM;
						expname = ExpenseConstant.EXPENSE_NAME_ATM;
					} else if (objSms.getMsg().contains(
							ExpenseConstant.EXPENSE_TYPE_KEYWORD_PURCHASE) == true) {
						type = ExpenseConstant.EXPENSE_TYPE_PURCHASE;
						expname = ExpenseConstant.EXPENSE_NAME_PURCHASE;
					} else if (objSms.getMsg().contains(
							ExpenseConstant.EXPENSE_TYPE_KEYWORD_DD) == true) {
						// type = ExpenseConstant.EXPENSE_TYPE_DD;
						type = strCon;// ExpenseConstant.EXPENSE_TYPE_DD;
						expname = ExpenseConstant.EXPENSE_NAME_DD;
					}

					dbExp.addExpense(new DBExpense(count, finalDateString,
							type, expname, ExpenseUtility.getAmount(objSms
									.getMsg(),sCountryCode)));

				}

			}
		}

	}

}
