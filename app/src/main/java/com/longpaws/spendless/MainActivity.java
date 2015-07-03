package com.longpaws.spendless;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

public class MainActivity extends Activity implements View.OnClickListener {

    private double tempTotalMonthlyBudget;
    private double totalDollarsLeft;
    private double enterAmountSpent;
    private int dayOfMonth;
    private int currentYear;
    private int currentMonth;
    private int previousYear;
    private int previousMonth;
    private int dbMonthINT;
    private int dbYear;
    private String dbMonthSTRING;
    private String enterNameOfExpense;
    private String tempTotalDollarsLeftString;
    private String tempEnterAmountSpentString;
    private String formatToastMessage;
    private String submittedToDBMessage;
    private boolean isAutoResetChecked;
    private BigDecimal totalDollarsLeftBigDecimal;
    private BigDecimal enterAmountSpentBigDecimal;
    private BigDecimal BigDecimalResult;

    private Calendar cal;
    private Calendar dbCal;
    private Toast incorrectFormatToast;
    private Toast submittedToDBToast;
    private Intent startFirstActivityIntent;
    private Intent startChangeBudgetActivityIntent;
    private Intent startDataBaseActivityIntent;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefEditor;
    EditText enterAmountSpentET;
    EditText enterNameOfExpenseET;
    TextView totalAmountLeftTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up Shared Preference file and create the Shared Preference Editor
        sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        prefEditor = sharedPreferences.edit();

        // Starts FirstScreen if this is first time launching app
        if (!sharedPreferences.getBoolean("activity_started", false)) {
            activityHasNotStarted();
        }

        // If AutoReset is checked in ChangeBudgetScreen and it is a new month
        // call newMonthResetBudget which will reset budget and update previous month and year
        isAutoResetChecked = sharedPreferences.getBoolean("AutoResetChecked", true);
        if (isAutoResetChecked && isNewMonth() ) {
            newMonthResetBudget();
        }

        // Initializes variables to default setting; must be set before rest of onCreate
        totalDollarsLeft = Double.parseDouble(sharedPreferences.getString("totalDollarsLeft", "0.0"));
        enterAmountSpent = 0.0;
        enterNameOfExpense = "N/A";
        // Create EditText and TextView boxes
        enterAmountSpentET = (EditText) findViewById(R.id.enterAmountSpent);
        enterNameOfExpenseET = (EditText) findViewById(R.id.enterNameOfExpense);
        totalAmountLeftTV = (TextView) findViewById(R.id.show_total_amount_left);
        //Set addTextChangedListeners
        enterAmountSpentET.addTextChangedListener(enterAmountSpentListener);
        enterNameOfExpenseET.addTextChangedListener(enterNameOfExpenseListener);
        // Set TextView to totalDollarsLeft
        totalAmountLeftTV.setText("$" + (Double.toString(totalDollarsLeft)));
        // Create buttons
        Button submitAmountSpentButton = (Button) findViewById(R.id.submitAmountSpentButton);
        Button changeMonthlyBudgetButton = (Button) findViewById(R.id.changeMonthlyBudgetButton);
        Button goToDataBaseScreenButton = (Button) findViewById(R.id.goToHistory);
        // Set onClickListener for buttons
        submitAmountSpentButton.setOnClickListener(this);
        changeMonthlyBudgetButton.setOnClickListener(this);
        goToDataBaseScreenButton.setOnClickListener(this);
        // Create Intents to change Activity
        startChangeBudgetActivityIntent = new Intent(this, ChangeBudgetScreen.class);
        startDataBaseActivityIntent = new Intent(this, DataBaseScreen.class);
        // Create Toast messages
        formatToastMessage = "Dollar amount must be submitted in correct format. Example: 100.00";
        submittedToDBMessage = "Submitted to DataBase";
        // Create Toasts
        incorrectFormatToast = Toast.makeText(getApplicationContext(), formatToastMessage, Toast.LENGTH_LONG);
        submittedToDBToast = Toast.makeText(getApplicationContext(), submittedToDBMessage, Toast.LENGTH_LONG);
    }

    // Rounds enterAmountSpent to 2 decimals places, subtracts it from totalDollarsLeft,
    // updates shared preference, updates the TextView, submits new transaction to database;
    // Resets enterAmountSpent and nameOfExpense variables, resets corresponding EditText boxes
    public void onClick(View v) {

        if (v.getId()==R.id.submitAmountSpentButton) {
            // BigDecimal used for rounding
            totalDollarsLeftBigDecimal = new BigDecimal(totalDollarsLeft).setScale(2, RoundingMode.HALF_UP);
            enterAmountSpentBigDecimal = new BigDecimal(enterAmountSpent).setScale(2, RoundingMode.HALF_UP);
            BigDecimalResult = totalDollarsLeftBigDecimal.subtract(enterAmountSpentBigDecimal);
            totalDollarsLeft = BigDecimalResult.doubleValue();
            prefEditor.putString("totalDollarsLeft", Double.toString(totalDollarsLeft));
            prefEditor.commit();
            totalAmountLeftTV.setText("$" + Double.toString(totalDollarsLeft));

            if (enterAmountSpent==0.0) {
                incorrectFormatToast.show();
            }

            double tempEnterAmountSpent = enterAmountSpentBigDecimal.doubleValue();
            if (tempEnterAmountSpent != 0.0) {
                newTransaction(enterNameOfExpense, tempEnterAmountSpent);
                submittedToDBToast.show();
            }

            enterAmountSpent = 0.0;
            enterNameOfExpense = "N/A";
            enterAmountSpentET.setText(null);
            enterNameOfExpenseET.setText(null);

        } else if (v.getId()==R.id.changeMonthlyBudgetButton) {
            startActivity(startChangeBudgetActivityIntent);
        }
        else if (v.getId()==R.id.goToHistory) {
            startActivity(startDataBaseActivityIntent);
        }
    }


    // Call when FirstActivity has not started - if this is first time launching app
    // Start FirstScreen Activity
    private void activityHasNotStarted() {
        startFirstActivityIntent = new Intent(this, FirstScreen.class);
        startActivity(startFirstActivityIntent);
        finish();
    }


    // Instantiates Calendar object and gets current Month and Year
    // Also gets previous month and year from Shared Preference file
    // If it is a new month or year, returns true; if not a new month, returns false
    public boolean isNewMonth() {
        cal = Calendar.getInstance();
        dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        currentYear = cal.get(Calendar.YEAR);
        currentMonth = cal.get(Calendar.MONTH);
        previousYear = sharedPreferences.getInt("Year", 0);
        previousMonth = sharedPreferences.getInt("Month", 0);

        return (currentYear > previousYear || currentMonth > previousMonth);
    }


    // Determines if it is a new month or year since last login
    // Sets totalDollarsLeft to equal totalMonthlyBudget (resets budget)
    // Puts new value in Year and Month in Shared Preference to update last time app started
    public void newMonthResetBudget() {
        cal = Calendar.getInstance();
        dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        currentYear = cal.get(Calendar.YEAR);
        currentMonth = cal.get(Calendar.MONTH);
        previousYear = sharedPreferences.getInt("Year", 0);
        previousMonth = sharedPreferences.getInt("Month", 0);
        if (currentYear > previousYear) { // next year. reset both month and year.
            prefEditor.putInt("Year", currentYear);
            prefEditor.putInt("Month", currentMonth);
            tempTotalMonthlyBudget = Double.parseDouble(sharedPreferences.getString("totalMonthlyBudget", "0.0"));
            prefEditor.putString("totalDollarsLeft", Double.toString(tempTotalMonthlyBudget));
            prefEditor.commit();
        } else { // next month, but same year.
            prefEditor.putInt("Month", currentMonth);
            tempTotalMonthlyBudget = Double.parseDouble(sharedPreferences.getString("totalMonthlyBudget", "0.0"));
            prefEditor.putString("totalDollarsLeft", Double.toString(tempTotalMonthlyBudget));
            prefEditor.commit();
        }
    }


    // Add a transaction to the data base
    public void newTransaction(String expenseName, double dollarsSpent) {
        DataBaseHandler dbHandler = new DataBaseHandler(this, null, null, 1);
        dbCal = Calendar.getInstance();
        dbYear = cal.get(Calendar.YEAR);
        dbMonthINT = cal.get(Calendar.MONTH);
        dbMonthSTRING = getMonthString(dbMonthINT);
        DollarsSpentTransaction newTransaction = new DollarsSpentTransaction(dbMonthSTRING, dbYear, expenseName, dollarsSpent);
        dbHandler.addTransaction(newTransaction);
    }


    // Returns string of month for corresponding int from Java Calendar
    // Example: int 0 return "January" Example: int 3 returns "April"
    // Used to put data in database Month column which takes a String as input
    public String getMonthString(int month) {

        String monthString;
        if (month == 0)
            monthString = "January";
        else if (month == 1)
            monthString = "February";
        else if (month == 2)
            monthString = "March";
        else if (month == 3)
            monthString = "April";
        else if (month == 4)
            monthString = "May";
        else if (month == 5)
            monthString = "June";
        else if (month == 6)
            monthString = "July";
        else if (month == 7)
            monthString = "August";
        else if (month == 8)
            monthString = "September";
        else if (month == 9)
            monthString = "October";
        else if (month == 10)
            monthString = "November";
        else if (month == 11)
            monthString = "December";
        else
            monthString = "";

        return monthString;
    }


    // When EditText is changed, if value entered is legal (a double other than 0.0)
    // assign value entered to enterAmountSpent; if illegal, assign 0.0 to enterAmountSpent
    private TextWatcher enterAmountSpentListener = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                enterAmountSpent = Double.parseDouble(s.toString());
            } catch (NumberFormatException e) {
                enterAmountSpent = 0.0;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    // When EditText (expense name) is changed, assigns value entered to
    // enterNameOfExpense; ragardless of what is entered, assign it to variable (everything legal)
    private TextWatcher enterNameOfExpenseListener = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            enterNameOfExpense = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}