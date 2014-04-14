package com.siva.expensetracker.db.handler;

import java.util.ArrayList;
import java.util.List;

import com.siva.expensetracker.dto.DBContacts;
import com.siva.expensetracker.util.ExpenseConstant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactHandler extends SQLiteOpenHelper {

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_FROMATM = "fromcontactatmnumber";
	private static final String KEY_FROMPURCH = "fromcontactpurchasenumber";

	public ContactHandler(Context context) {
		super(context, ExpenseConstant.DATABASE_NAME, null,
				ExpenseConstant.DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE "
				+ ExpenseConstant.TABLE_CONTACTS + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY," + KEY_FROMATM + " TEXT,"
				+ KEY_FROMPURCH + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + ExpenseConstant.TABLE_CONTACTS);

		// Create tables again
		onCreate(db);
	}

	// Getting single contact
	public DBContacts getContact(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(ExpenseConstant.TABLE_CONTACTS, new String[] {
				KEY_ID, KEY_FROMATM, KEY_FROMPURCH }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		DBContacts contact = new DBContacts(Integer.parseInt(cursor
				.getString(0)), cursor.getString(1), cursor.getString(2));
		
		cursor.close();
		db.close();
		// return contact
		return contact;
	}

	// getAllContacts()
	// Getting All Contacts
	public List<DBContacts> getAllContacts() {
		List<DBContacts> contactList = new ArrayList<DBContacts>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + ExpenseConstant.TABLE_CONTACTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				DBContacts contact = new DBContacts();
				contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.setFromContactATMNumber(cursor.getString(1));
				contact.setFromContactPurchaseNumber(cursor.getString(2));
				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();
		
		// return contact list
		return contactList;
	}

	// Adding new contact
	public void addContact(DBContacts contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, contact.getID()); // Contact Name
		values.put(KEY_FROMATM, contact.getFromContactATMNumber()); // Contact
																	// Phone
		values.put(KEY_FROMPURCH, contact.getFromContactPurchaseNumber()); // Contact
																			// Phone
		// Inserting Row
		db.insert(ExpenseConstant.TABLE_CONTACTS, null, values);
		db.close(); // Closing database connection
	}

	// Updating single contact
	public int updateContact(DBContacts contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_FROMATM, contact.getFromContactATMNumber());
		values.put(KEY_FROMPURCH, contact.getFromContactPurchaseNumber());

		int retVal = db.update(ExpenseConstant.TABLE_CONTACTS, values, KEY_ID
				+ " = ?", new String[] { String.valueOf(contact.getID()) });
		
		db.close();
		
		// updating row
		return retVal;
		
	}

	// Getting contacts Count
	public int getContactsCount() {
		Cursor cursor = null;
		SQLiteDatabase db = null;
		int reval = 0;
		try {
			String countQuery = "SELECT  * FROM "
					+ ExpenseConstant.TABLE_CONTACTS;
			db = this.getReadableDatabase();
			cursor = db.rawQuery(countQuery, null);
			reval = cursor.getCount();
			cursor.close();
		} catch (SQLiteException e) {
			if (e.getMessage().toString().contains("no such table")) {

			}
			
		}finally{
			cursor.close();
			db.close();
		}

		return reval;

	}

	// deleteContact()
	// Deleting single contact
	public void deleteContactAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		// db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
		// new String[] { String.valueOf(contact.getID()) });
		db.delete(ExpenseConstant.TABLE_CONTACTS, null, null);
		db.close();
	}

}
