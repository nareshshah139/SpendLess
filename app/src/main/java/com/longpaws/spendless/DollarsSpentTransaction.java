package com.longpaws.spendless;

/*
 * Created by Mitch on 6/25/2015.
 */
public class DollarsSpentTransaction {

    private int _id;
    private int _month;
    private int _year;
    private String _nameOfExpense;
    private double _dollarsSpent;

    public DollarsSpentTransaction() {

    }

    public DollarsSpentTransaction(int id, int month, int year, String expenseName, double dollarsSpent) {
        this._id = id;
        this._month = month;
        this._year = year;
        this._nameOfExpense = expenseName;
        this._dollarsSpent = dollarsSpent;
    }

    public DollarsSpentTransaction(int month, int year, String expenseName, double dollarsSpent) {
        this._month = month;
        this._year = year;
        this._nameOfExpense = expenseName;
        this._dollarsSpent = dollarsSpent;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public void setMonth(int month) {
        this._month = month;
    }

    public int getMonth() {
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
