package com.siva.expensetracker.dto;

public class DBContacts {
	public int getID() {
		return _id;
	}
	public void setID(int _id) {
		this._id = _id;
	}
	public String getFromContactATMNumber() {
		return _fromcontactatmnumber;
	}
	public void setFromContactATMNumber(String _FromContactATMNumber) {
		this._fromcontactatmnumber = _FromContactATMNumber;
	}
	public String getFromContactPurchaseNumber() {
		return _fromcontactpurchasenumber;
	}
	public void setFromContactPurchaseNumber(String _FromContactPurchaseNumber) {
		this._fromcontactpurchasenumber = _FromContactPurchaseNumber;
	}
	
	public DBContacts(int id,String fromcontactatmnumber,String fromcontactpurchasenumber)
	{
		_id=id;
		_fromcontactatmnumber = fromcontactatmnumber;
		_fromcontactpurchasenumber = fromcontactpurchasenumber;
	}
	public DBContacts(){}
	int _id;
	String _fromcontactatmnumber;
	String _fromcontactpurchasenumber;

}
