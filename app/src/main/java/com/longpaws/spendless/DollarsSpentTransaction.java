package com.longpaws.spendless;

/*
 * Created by Mitch on 6/25/2015.
 *
 * Creates a new transaction object.
 *
 * New transactions are submitted to the DataBase using the DataBaseHandler class.
 * Arguments of a DollarsSpentTransaction (id, month, year, name, dollar_amount) correspond
 * to the columns in the DataBase.
 *
 * When a DollarsSpentTransaction object is submitted to the DataBase, it can be viewed in the
 * DataBaseScreen using a method such as viewDataBase();
 */
public class DollarsSpentTransaction {

    // Data corresponding to columns in the database table
    private int _id;
    private String _month;
    private int _year;
    private String _nameOfExpense;
    private double _dollarsSpent;

    public DollarsSpentTransaction() {
    }


    // Constructor for creating a DollarsSpentTransaction
    // Takes arguments corresponding with table columns from the database
    // Used to put a new transaction (row) into the database from the DataBaseHandler class
    public DollarsSpentTransaction(int id, String month, int year, String expenseName, double dollarsSpent) {
        this._id = id;
        this._month = month;
        this._year = year;
        this._nameOfExpense = expenseName;
        this._dollarsSpent = dollarsSpent;
    }

    public DollarsSpentTransaction(String month, int year, String expenseName, double dollarsSpent) {
        this._month = month;
        this._year = year;
        this._nameOfExpense = expenseName;
        this._dollarsSpent = dollarsSpent;
    }

    // Getters and Setters for all data in the class
    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public void setMonth(String month) {
        this._month = month;
    }

    public String getMonth() {
        return this._month;
    }

    public void setYear(int year) {
        this._year = year;
    }

    public int getYear() {
        return this._year;
    }

    public void setDollarsSpent(double dollarsSpent) {
        this._dollarsSpent = dollarsSpent;
    }

    public double getDollarsSpent() {
        return this._dollarsSpent;
    }

    public void setExpenseName(String expenseName) {
        this._nameOfExpense = expenseName;
    }

    public String getExpenseName() {
        return this._nameOfExpense;
    }

}
