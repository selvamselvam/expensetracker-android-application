package com.siva.expensetracker.db.handler;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.siva.expensetracker.dto.DBExpense;
import com.siva.expensetracker.util.ExpenseConstant;

public class ExpenseHandler extends SQLiteOpenHelper {
	 // Contacts Table Columns names
    private static final String EXPENSE_KEY_ID = "id"; // String
    private static final String EXPENSE_KEY_DATE = "date"; //int
    private static final String EXPENSE_KEY_TYPE = "type";
    private static final String EXPENSE_KEY_EXPENSENAME = "expensename";
    private static final String EXPENSE_KEY_AMOUNT = "amount";
	
	
    public ExpenseHandler(Context context) {
        super(context, ExpenseConstant.DATABASE_NAME_DATA, null, ExpenseConstant.DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EXPENSE_TABLE = "CREATE TABLE " + ExpenseConstant.TABLE_EXPENSE + "("
                + EXPENSE_KEY_ID + " INTEGER PRIMARY KEY," 
        		+ EXPENSE_KEY_DATE + " TEXT,"
        		+ EXPENSE_KEY_TYPE + " TEXT,"
                + EXPENSE_KEY_EXPENSENAME + " TEXT," 
                + EXPENSE_KEY_AMOUNT + " DOUBLE"
        		+ ")";
        db.execSQL(CREATE_EXPENSE_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ExpenseConstant.TABLE_EXPENSE);
 
        // Create tables again
        onCreate(db);
    }
    
    
 // Adding new contact
 	public void addExpense(DBExpense expense) throws SQLException{
 		SQLiteDatabase db = this.getWritableDatabase();

 		ContentValues values = new ContentValues();
 		values.put(EXPENSE_KEY_ID, expense.getID()); // Contact Name
 		values.put(EXPENSE_KEY_DATE, expense.getDate()); // Contact
 		values.put(EXPENSE_KEY_TYPE, expense.getType()); // Contact
 		values.put(EXPENSE_KEY_EXPENSENAME, expense.getExpenseName()); // Contact
 		values.put(EXPENSE_KEY_AMOUNT, expense.getAmount()); // Contact
 		
 		
 		// Inserting Row
 		 if( db.insertOrThrow(ExpenseConstant.TABLE_EXPENSE, null, values) == -1)
 			throw new SQLException();
 		
 		db.close(); // Closing database connection
 	}

 	
 	public void deleteAllExpense() {
 		SQLiteDatabase db = this.getWritableDatabase();
 		db.delete(ExpenseConstant.TABLE_EXPENSE, null, null);
 		db.close();
 	}
 	
 	
 	// Getting contacts Count
 	public int getExpensesCount() {
 		String countQuery = "SELECT  * FROM "
 					+ ExpenseConstant.TABLE_EXPENSE +" WHERE type='" 
 					+ ExpenseConstant.EXPENSE_TYPE_ATM +"' OR type='"
 					+ ExpenseConstant.EXPENSE_TYPE_PURCHASE + "' OR type='" //Buy //DD
 					+ ExpenseConstant.EXPENSE_TYPE_DD + "'"; //Buy //DD
 		return getCount(countQuery);
 	}
 	
 	// Getting contacts Count
  	public int getCustomExpensesCount() {
  		String countQuery = "SELECT  * FROM "
  					+ ExpenseConstant.TABLE_EXPENSE +" WHERE type='" + ExpenseConstant.EXPENSE_TYPE_CUSTOM +"'";
  		return getCount(countQuery);

  	}
  	
 	
  	private int getCount(String countQuery){
  		Cursor cursor = null;
  		SQLiteDatabase db = null;
  		int reval = 0;
  		try {
  			db = this.getReadableDatabase();
  			cursor = db.rawQuery(countQuery, null);
  			reval = cursor.getCount();
  			
  		} catch (SQLiteException e) {
  			if (e.getMessage().toString().contains("no such table")) {
  				
  			}
  		}finally{
  			cursor.close();
  			db.close();
  		}
  		return reval;
  	}
  	
  	
 	public List<DBExpense> getTodaysExpenses() {
 		String selectQuery = "SELECT  * FROM " 
 				+ ExpenseConstant.TABLE_EXPENSE + " WHERE date LIKE strftime('%d/%m/%Y','now')" ;
 	 		return getExpenses(selectQuery);
 	}
 	
 	
 	// getAllContacts()
 	public List<DBExpense> getMonthExpenses(String strMonthYear) {
 		String selectQuery = "SELECT  * FROM " + 
 					ExpenseConstant.TABLE_EXPENSE + " WHERE date LIKE '%" + strMonthYear + "'" ;
 		return getExpenses(selectQuery);
 	}
 	
 	private List<DBExpense> getExpenses(String strQuery){
 		List<DBExpense> expenseList = new ArrayList<DBExpense>();
 		SQLiteDatabase db = this.getWritableDatabase();
 		Cursor cursor = db.rawQuery(strQuery, null);

 		// looping through all rows and adding to list
 		if (cursor.moveToFirst()) {
 			do {
 				DBExpense expense = new DBExpense();
 				expense.setID(Integer.parseInt(cursor.getString(0)));
 				expense.setDate(cursor.getString(1));
 				expense.setType(cursor.getString(2));
 				expense.setExpenseName(cursor.getString(3));
 				expense.setAmount(Double.parseDouble(cursor.getString(4)));
 				// Adding contact to list
 				expenseList.add(expense);
 			} while (cursor.moveToNext());
 		}
 		cursor.close();
 		db.close();
 		return expenseList;
 	}
 	
 	// Deleting single contact
 	public boolean deleteAllCustomExpenses() {
 		SQLiteDatabase db = this.getWritableDatabase();
 		int res = db.delete(ExpenseConstant.TABLE_EXPENSE, EXPENSE_KEY_TYPE + " = ?",
 				new String[] { String.valueOf(ExpenseConstant.EXPENSE_TYPE_CUSTOM) });
 		db.close();
		
 		if(res == 0)
 			return false;
 		else
 			return true;
 	}
 	
 	
}
