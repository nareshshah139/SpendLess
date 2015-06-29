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


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefEditor;

    Intent startFirstActivityIntent;
    Intent startChangeBudgetActivityIntent;
    Intent startDataBaseActivityIntent;

    private double tempTotalMonthlyBudget;
    private int dayOfMonth;
    private int currentYear;
    private int currentMonth;
    private int previousYear;
    private int previousMonth;
    private int dbMonth;
    private int dbYear;

    Calendar cal;
    Calendar dbCal;

    EditText enterAmountSpentET;
    EditText enterNameOfExpenseET;
    TextView changeAmountLeftTV;

    private double totalDollarsLeft;
    private double enterAmountSpent;
    private String enterNameOfExpense;

    String tempTotalDollarsLeftString;
    String tempEnterAmountSpentString;

    BigDecimal totalDollarsLeftBigDecimal;
    BigDecimal enterAmountSpentBigDecimal;
    BigDecimal BigDecimalResult;

    Toast incorrectFormatToast;
    String formatToastMessage;

    Toast submittedToDBToast;
    String submittedToDB;

    boolean isAutoResetChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        prefEditor = sharedPreferences.edit();

        /*  Checks to see if activity_started is in sharedPreferences.
            If it is, "true" is returned but the statement evaluates to false because of the "!".
            The condition is false and the statement is skipped, continuing with MainActivity.

            If activity_started is not in sharedPreferences, "false" is returned but the
            statement evaluates to "true" because of the "!".
            The condition is true so the statement is executed, resulting in the Intent that
            starts the FirstScreen activity.
        */
        if (!sharedPreferences.getBoolean("activity_started", false)) {
            startFirstActivityIntent = new Intent(this, FirstScreen.class);
            startActivity(startFirstActivityIntent);
            finish();
        }


        /* Checks to see if it is the first of the month.
           If it is, resets totalDollarsLeft to totalMonthlyBudget
           from the sharedPreferences.
         */
        cal = Calendar.getInstance();
        dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        currentYear = cal.get(Calendar.YEAR);
        currentMonth = cal.get(Calendar.MONTH);
        previousYear = sharedPreferences.getInt("Year", 0);
        previousMonth = sharedPreferences.getInt("Month", 0);

        isAutoResetChecked = sharedPreferences.getBoolean("AutoResetChecked", true);

        if (isAutoResetChecked) {

            if ((currentYear > previousYear) || (currentMonth > previousMonth)) {

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
        }

        totalDollarsLeft = Double.parseDouble(sharedPreferences.getString("totalDollarsLeft", "0.0"));
        enterAmountSpent = 0.0;
        enterNameOfExpense = "N/A";

        enterAmountSpentET = (EditText) findViewById(R.id.enterAmountSpent);
        enterAmountSpentET.addTextChangedListener(enterAmountSpentListener);

        enterNameOfExpenseET = (EditText) findViewById(R.id.enterNameOfExpense);
        enterNameOfExpenseET.addTextChangedListener(enterNameOfExpenseListener);

        changeAmountLeftTV = (TextView) findViewById(R.id.show_total_amount_left);
        changeAmountLeftTV.setText("$" + (Double.toString(totalDollarsLeft)));

        Button submitAmountSpentButton = (Button) findViewById(R.id.submitAmountSpentButton);
        submitAmountSpentButton.setOnClickListener(this);

        Button changeMonthlyBudgetButton = (Button) findViewById(R.id.changeMonthlyBudgetButton);
        changeMonthlyBudgetButton.setOnClickListener(this);

        Button goToDataBaseScreenButton = (Button) findViewById(R.id.goToHistory);
        goToDataBaseScreenButton.setOnClickListener(this);

        startChangeBudgetActivityIntent = new Intent(this, ChangeBudgetScreen.class);
        startDataBaseActivityIntent = new Intent(this, DataBaseScreen.class);

        formatToastMessage = "Dollar amount must be submitted in correct format. Example: 100.00";
        incorrectFormatToast = Toast.makeText(getApplicationContext(), formatToastMessage, Toast.LENGTH_LONG);

        submittedToDB = "Submitted to DataBase";
        submittedToDBToast = Toast.makeText(getApplicationContext(), submittedToDB, Toast.LENGTH_LONG);
    }

    public void onClick(View v) {

        /* When the Submit button is clicked:
           1. Creates temporary String reference variables that contain the values of
              enterAmountSpent and totalDollarsLeft for BigDecimal conversion
           2. Create BigDecimals out of the new temp strings
           3. Subtracts the BigDecimal version of enterAmountSPent from the BigDecimal totalDollarsLeft
              and assigns value to new BigDecimal result
           4. Converts new BigDecimal 'result' back into a double and assigns it to totalDollarsLeft
           5. Saves the value of totalDollarsLeft into the sharedPreferences file into the string
              "totalDollarsLeft".
           6. Commit() the change to sharedPreferences.
           7. Set the TextView object of changeAmountLeftTV to the new value of totalDollarsLeft
           8. Show Toast if user entered an incorrect format
           9. Reset enterAmountSpent to the double 0.0;

           When Change Budget button is clicked:
           1. Create intent to go to changeMonthlyBudget screen
           2. Start intent, launching the Change Budget screen
         */


        if (v.getId()==R.id.submitAmountSpentButton) {

            // 1. Creates temp string out of totalDollarsLeft and enterAmountSpent
            tempTotalDollarsLeftString = String.valueOf(totalDollarsLeft);
            tempEnterAmountSpentString = String.valueOf(enterAmountSpent);

            // 2. Creates BigDecimals out of new temp strings
            totalDollarsLeftBigDecimal = new BigDecimal(tempTotalDollarsLeftString).setScale(2, RoundingMode.HALF_UP);
            enterAmountSpentBigDecimal = new BigDecimal(enterAmountSpent).setScale(2, RoundingMode.HALF_UP);

            //3. Subtracts BigDecimal and creates new BigDecimal result
            BigDecimalResult = totalDollarsLeftBigDecimal.subtract(enterAmountSpentBigDecimal);

            //4. Converts BigDecimalResult back into a double and assigns it to totalDollarsLeft
            totalDollarsLeft = BigDecimalResult.doubleValue();


            // 5/6. Saves and commits changes to SharedPreference file
            prefEditor.putString("totalDollarsLeft", Double.toString(totalDollarsLeft));
            prefEditor.commit();

            // 7. Set changeAmountLeftTV TextView to new amount of totalDollarsLeft
            changeAmountLeftTV.setText("$" + Double.toString(totalDollarsLeft));

            // 8. If enterAmountSpent equals 0.0 (numberFormatException was caught),
            //    display toast showing error message
            if (enterAmountSpent==0.0) {
                incorrectFormatToast.show();
            }


            double tempEnterAmountSpent = enterAmountSpentBigDecimal.doubleValue();

            // put into data base if enterAmountSpent does not equal 0.0
            if (tempEnterAmountSpent != 0.0) {

                // put into database
                newTransaction(enterNameOfExpense, tempEnterAmountSpent);
                submittedToDBToast.show();
            }

            // 9. Reset enterAmountSpent to 0.0 and enterNameOfExpense to 'N/A'
            enterAmountSpent = 0.0;
            enterNameOfExpense = "N/A";

            // 10. Clear the EditText box enterAmountSpentET
            enterAmountSpentET.setText(null);
            enterNameOfExpenseET.setText(null);



        } else if (v.getId()==R.id.changeMonthlyBudgetButton) {

            // 1/2. Create intent to go to Change Budget Activity. Start intent.
            startActivity(startChangeBudgetActivityIntent);

        }
        else if (v.getId()==R.id.goToHistory) {

            // start activity to go to data base screen
            startActivity(startDataBaseActivityIntent);
        }
    }


    // add a transaction to the data base
    public void newTransaction(String expenseName, double dollarsSpent) {

        DataBaseHandler dbHandler = new DataBaseHandler(this, null, null, 1);

        dbCal = Calendar.getInstance();
        dbYear = cal.get(Calendar.YEAR);
        dbMonth = cal.get(Calendar.MONTH);

        DollarsSpentTransaction newTransaction = new DollarsSpentTransaction(dbMonth, dbYear, expenseName, dollarsSpent);

        dbHandler.addTransaction(newTransaction);
    }



    private TextWatcher enterAmountSpentListener = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            /*  Whener the Textwatcher is changed it check to see if the data entered is a double.
                If it is, the value of the double is assigned to the variable enterAmountSpent.
                If it's not, enterAmountSpent is assigned the value 0.0.

                If the exception is caught, will start a toast message saying user needs to enter
                in the correct format.
             */
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
