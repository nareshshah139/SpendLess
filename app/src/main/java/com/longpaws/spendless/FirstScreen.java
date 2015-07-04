package com.longpaws.spendless;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

/*
 * FirstScreen Activity is only called the first time the app is launched.
 *
 * A method in MainActivity (first screen to start) checks a Shared Preference file to see if
 * Activity has started before. If it has, it skips the Intent to FirstScreen. If Activity has not
 * started, it starts the FirstScreen Activity launching this screen.
 *
 * All the user does in the Activity is enters their origional monthly budget. This will save their
 * monthly budget in SharedPreferences (to make sure this screen is not displayed again) and then
 * start the MainActivity intent.
 */

public class FirstScreen extends Activity implements View.OnClickListener {

    private double tempMonthlyBudget;
    private int startYear;
    private int startMonth;
    private String formatToastMessage;

    private Calendar Cal;
    private Toast incorrectFormatToast;

    EditText enterMonthlyBudgetET;
    Intent startMainActivityIntent;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        // Set up Shared Preference file and create the Shared Preference Editor
        sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        prefEditor = sharedPreferences.edit();

        // Initialize the EditText box and set the TextChangedListener
        enterMonthlyBudgetET = (EditText) findViewById(R.id.enterMonthlyBudgetEditText);
        enterMonthlyBudgetET.addTextChangedListener(enterMonthlyBudgetListener);

        // Create button and setOnClickListener
        Button submitMonthlyBudgetBtn = (Button) findViewById(R.id.submitMonthlyBudgetButton);
        submitMonthlyBudgetBtn.setOnClickListener(this);

        // Create Toast and Toast message if user enters data in an incorrect format
        formatToastMessage = "Dollar amount must be submitted in correct format. Example: 100.00";
        incorrectFormatToast = Toast.makeText(getApplicationContext(), formatToastMessage, Toast.LENGTH_LONG);

        // Creates the Calendar object to get the current month and year
        Cal = Calendar.getInstance();
        startYear = Cal.get(Calendar.YEAR);
        startMonth = Cal.get(Calendar.MONTH);

        // Create intent to start Main Activity
        startMainActivityIntent = new Intent(this, MainActivity.class);

        // Creates and initializes tempMonthlyBudget to 0.0 by default;
        // tempMonthlyBudget must be changed before being accepted
        // Makes sure user enters a valid budget before starting Main Activity
        tempMonthlyBudget = 0.0;
    }


    /* If format of tempMonthlyBudget is legal, updates Shared Preference file;
    // Puts ActivityStarted, current Month, current Year, Total Monthly Budget, and
    // Total Dollars Left into the Shared Preference file for future reference
    // If format is illegal, incorrect format toast is displayed and EditText is reset
    */
    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.submitMonthlyBudgetButton) {

            // Checks if format is illegal (0.0)
            if (tempMonthlyBudget==0.0) {
                incorrectFormatToast.show();
                enterMonthlyBudgetET.setText(null);
            } else {

                // If format is legal updates Shared Preference file
                prefEditor.putBoolean("activity_started", true);
                prefEditor.putString("totalMonthlyBudget", Double.toString(tempMonthlyBudget));
                prefEditor.putString("totalDollarsLeft", Double.toString(tempMonthlyBudget));
                prefEditor.putInt("Year", startYear);
                prefEditor.putInt("Month", startMonth);
                prefEditor.putBoolean("AutoResetChecked", true);

                // Commits shared preference updates and starts Main Activity
                prefEditor.commit();
                startActivity(startMainActivityIntent);
                finish();
            }
        }
    }


    /* Assigns tempMonthlyBduget to value in EditText if format is legal
    // Or assigns tempMonthlyBudget to 0.0 if format is illegal
    // Legal format is a double other than 0.0
    */
    private TextWatcher enterMonthlyBudgetListener = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            try {
                tempMonthlyBudget = Double.parseDouble(s.toString());
            } catch (NumberFormatException e) {
                tempMonthlyBudget = 0.0;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first_screen, menu);
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
