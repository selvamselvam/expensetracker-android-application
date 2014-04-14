package com.siva.expensetracker.util;

public class ExpenseConstant {
	 // All Static variables
    // Database Version
    public static final int DATABASE_VERSION = 1;
 
    // Database Name
    public static final String DATABASE_NAME = "ExpenseSystem";
 
    public static final String DATABASE_NAME_DATA = "ExpenseSystemData1";
    
    // Contacts table name
    public static final String TABLE_CONTACTS = "contacts";
    
    // Contacts table name
    public static final String TABLE_EXPENSE = "expense";
    
    
    
    public static final String RS_CONSTANT = "Rs";
    
    //SMS URI
    public static final String SMS_URI = "content://sms/";
    
    //Expense Type
    public static final String EXPENSE_TYPE_KEYWORD_ATM = "ATM";
    public static final String EXPENSE_TYPE_KEYWORD_PURCHASE = "purchase";
    public static final String EXPENSE_TYPE_KEYWORD_DD = "DD";
    
    public static final String EXPENSE_TYPE_ATM = "ATM";
    public static final String EXPENSE_TYPE_PURCHASE = "PayByCard";
    public static final String EXPENSE_TYPE_CUSTOM = "Custom";
    public static final String EXPENSE_TYPE_DD = "DD";
    
    public static final String EXPENSE_NAME_ATM = "Withdraw";
    public static final String EXPENSE_NAME_PURCHASE = "Purchase";
    //public static final String EXPENSE_NAME_DD = "DemandDraft";
    public static final String EXPENSE_NAME_DD = "DD";
    
    public static final String CURRENCY_SYMBOLS= "\\p{Sc}\u0024\u060B";
    
    
    public static final String PRE_BANKCONTACT= "SMSContact";
    public static final String PRE_ACCOUNTORCARDNUMBER= "CardACNumber";
    
    
}
