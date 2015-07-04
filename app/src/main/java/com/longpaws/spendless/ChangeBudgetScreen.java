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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

/*
   Screen/activity that allows users to change settings. User have 3 options:
   1. Change their monthly budget
   2. Reset remaining budget to their monthly budget
   3. Turn on/off auto reset - budget will reset every new month by default

   These three options are controlled by buttons and an EditText to change monthly budget that
   takes a double as its value.

   User returns to the main activity by clicking 'main' button or, currently, submitting
   a change monthly budget which will start a main activity intent
 */


public class ChangeBudgetScreen extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private String tempTotalMonthlyBudget;
    private String toastChangeBudgetMessage;
    private String formatToastMessage;
    private String resetBudgetMessage;
    private String autoResetOnMessage;
    private String autoResetOffMessage;
    private String budgetChangedAutoResetOnMessage;
    private double tempChangeMonthlyBudget;

    private Toast budgetChangedAutoResetOffToast;
    private Toast incorrectFormatToast;
    private Toast budgetResetToast;
    private Toast autoResetTurnedOnToast;
    private Toast autoResetTurnedOffToast;
    private Toast budgetChangedAutoResetOnToast;

    EditText changeMonthlyBudgetET;
    SharedPreferences.Editor prefEditor;
    SharedPreferences sharedPreferences;
    ToggleButton toggleButton;
    Intent startMainActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_budget_screen);

        // Set up Shared Preference file and create the Shared Preference Editor
        sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        prefEditor = sharedPreferences.edit();

        // Initialize the EditText box and set the TextChangedListener
        changeMonthlyBudgetET = (EditText) findViewById(R.id.changeMonthlyBudgetEditText);
        changeMonthlyBudgetET.addTextChangedListener(changeMonthlyBudgetListener);

        // Create buttons
        Button backToMainButton = (Button) findViewById(R.id.BackToMainButton);
        Button submitMonthlyBudgetChangeButton = (Button) findViewById(R.id.submitMonthlyChangeButton);
        Button resetBudgetButton = (Button) findViewById(R.id.resetBudgetNowButton);

        // Create setOnClickListeners for each button
        backToMainButton.setOnClickListener(this);
        submitMonthlyBudgetChangeButton.setOnClickListener(this);
        resetBudgetButton.setOnClickListener(this);

        // Create ToggleButton and setOnCheckedChangeListener for the ToggleButton
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener(this);

        // Create Toast messages
        toastChangeBudgetMessage = "Monthly budget successfully changed";
        formatToastMessage = "Dollar amount must be greater than 0.0 and submitted in correct format: " +
                "ex. 100.00";
        resetBudgetMessage = "Reset dollars left to budget.";
        autoResetOnMessage = "Budget will reset every month";
        autoResetOffMessage = "Budget will not reset every month";
        budgetChangedAutoResetOnMessage = "Monthly budget successfully changed. Budget " +
                "will reset next month.";

        // Initialize Toasts
        budgetChangedAutoResetOffToast = Toast.makeText(getApplicationContext(), toastChangeBudgetMessage, Toast.LENGTH_LONG);
        incorrectFormatToast = Toast.makeText(getApplicationContext(), formatToastMessage, Toast.LENGTH_LONG);
        budgetResetToast = Toast.makeText(getApplicationContext(), resetBudgetMessage, Toast.LENGTH_SHORT);
        autoResetTurnedOnToast = Toast.makeText(getApplicationContext(), autoResetOnMessage, Toast.LENGTH_SHORT);
        autoResetTurnedOffToast = Toast.makeText(getApplicationContext(), autoResetOffMessage, Toast.LENGTH_SHORT);
        budgetChangedAutoResetOnToast = Toast.makeText(getApplicationContext(),
                budgetChangedAutoResetOnMessage, Toast.LENGTH_SHORT);

        // Set the tempChangeMonthlyBudget variable to 0.0 by default whenever Activity starts
        tempChangeMonthlyBudget = 0.0;

        // Create Intent to launch Main Activity Screen
        startMainActivityIntent = new Intent(this, MainActivity.class);
    }


    // Changes monthly budget, resets total dollars left in budget, dispalys appropriate Toast
    // depending on if AutoReset is checked on or off
    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.BackToMainButton) {

            startActivity(startMainActivityIntent);

        } else if (v.getId()==R.id.submitMonthlyChangeButton) {

            // If format is correct puts new monthly budget into the SharedPreference file
            // If tempChangeMonthlyBudget is 0.0 (determined by value in EditText),
            // EditText is set to null and incorrect format toast is started
            if (tempChangeMonthlyBudget==0.0) {
                incorrectFormatToast.show();
                changeMonthlyBudgetET.setText(null);
            } else {
                prefEditor.putString("totalMonthlyBudget", Double.toString(tempChangeMonthlyBudget));
                prefEditor.commit();

                boolean isAutoResetChecked = sharedPreferences.getBoolean("AutoResetChecked", true);
                if (isAutoResetChecked)
                    budgetChangedAutoResetOnToast.show();
                else
                    budgetChangedAutoResetOffToast.show();
            }
        } else if (v.getId() == R.id.resetBudgetNowButton) {
            // Resets the total dollars left to equal the monthly budget
            tempTotalMonthlyBudget = sharedPreferences.getString("totalMonthlyBudget", "0.0");
            prefEditor.putString("totalDollarsLeft", tempTotalMonthlyBudget);
            prefEditor.commit();
            budgetResetToast.show();
        }
    }


    // Updates SharedPreferences stating whether the Auto Reset Toggle Button is checked on or off
    // If on, budget will reset every new month when Main Activity starts
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked)
        {
            prefEditor.putBoolean("AutoResetChecked", true);
            prefEditor.commit();
            autoResetTurnedOnToast.show();
        }
        else
        {
            prefEditor.putBoolean("AutoResetChecked", false);
            prefEditor.commit();
            autoResetTurnedOffToast.show();
        }
    }


    // If value in EditText is a double; assigns it to tempChangeMonthlyBudget
    // If value is not a double, tempChangeMonthlyBudget is set to 0.0 by default
    private TextWatcher changeMonthlyBudgetListener = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                tempChangeMonthlyBudget = Double.parseDouble(s.toString());
            } catch (NumberFormatException e) {
                tempChangeMonthlyBudget = 0.0;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_budget_screen, menu);
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
