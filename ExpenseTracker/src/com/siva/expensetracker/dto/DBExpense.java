package com.siva.expensetracker.dto;

public class DBExpense {
	
	int _id;//ID
	String _date;//Date
	String _type;//Type ( Withdraw, Card, Currency)
	String _expensename;//Expense Name (Withdraw, Book purchare, Fuesl, currency)
	double _amount;//Amount
	
	public DBExpense(){}
	public DBExpense(int _id,String _date,String _type,String _expensename,double _amount){
		this._id = _id;
		this._date = _date;
		this._type = _type;
		this._expensename = _expensename;
		this._amount = _amount;
	}
	public int getID() {
		return _id;
		
	}
	public void setID(int _id) {
		this._id = _id;
	}
	
	public String getDate() {
		return _date;
	}
	public void setDate(String _date) {
		this._date = _date;
	}
	
	
	public String getType() {
		return _type;
	}
	public void setType(String _type) {
		this._type = _type;
	}
	
	public String getExpenseName() {
		return _expensename;
	}
	public void setExpenseName(String _expensename) {
		this._expensename = _expensename;
	}
	
	public double getAmount() {
		return _amount;
	}
	public void setAmount(double _amount) {
		this._amount = _amount;
	}
	
	
}
