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


public class ChangeBudgetScreen extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private String tempTotalMonthlyBudget;
    private String toastChangeBudgetMessage;
    private String formatToastMessage;
    private String resetBudgetMessage;
    private String autoResetOnMessage;
    private String autoResetOffMessage;
    private double tempChangeMonthlyBudget;

    private Toast budgetChangedToast;
    private Toast incorrectFormatToast;
    private Toast budgetResetToast;
    private Toast autoResetTurnedOnToast;
    private Toast autoResetTurnedOffToast;

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
        toastChangeBudgetMessage = "Budget will reset on the first of the next month.";
        formatToastMessage = "Dollar amount must be submitted in correct format. Example: 100.00";
        resetBudgetMessage = "Reset dollars left to budget.";
        autoResetOnMessage = "Budget will reset every month";
        autoResetOffMessage = "Budget will not reset every month";

        // Initialize Toasts
        budgetChangedToast = Toast.makeText(getApplicationContext(), toastChangeBudgetMessage, Toast.LENGTH_LONG);
        incorrectFormatToast = Toast.makeText(getApplicationContext(), formatToastMessage, Toast.LENGTH_LONG);
        budgetResetToast = Toast.makeText(getApplicationContext(), resetBudgetMessage, Toast.LENGTH_SHORT);
        autoResetTurnedOnToast = Toast.makeText(getApplicationContext(), autoResetOnMessage, Toast.LENGTH_SHORT);
        autoResetTurnedOffToast = Toast.makeText(getApplicationContext(), autoResetOffMessage, Toast.LENGTH_SHORT);

        // Set the tempChangeMonthlyBudget variable to 0.0 by default whenever Activity starts
        tempChangeMonthlyBudget = 0.0;

        // Create Intent to launch Main Activity Screen
        startMainActivityIntent = new Intent(this, MainActivity.class);
    }

    @Override
    public void onClick(View v) {

        /*  If Back Button is clicked, starts activity to go to MainActivity.

            If submit button is clicked:
            1. Checks if tempMonthlyBudget == 0.0
            2. If it does, shows incorrect format toast and resets the EditText
            3. If not, saves the value of tempChangeMonthlyBudget in sharedPreferene "totalMonthlyBudget",
               shows toast that budget is changed, starts intent to go to MainActivity

         */

        if (v.getId()==R.id.BackToMainButton) {

            startActivity(startMainActivityIntent);

        } else if (v.getId()==R.id.submitMonthlyChangeButton) {

            if (tempChangeMonthlyBudget==0.0) {
                incorrectFormatToast.show();
                changeMonthlyBudgetET.setText(null);
            } else {
                prefEditor.putString("totalMonthlyBudget", Double.toString(tempChangeMonthlyBudget));
                prefEditor.commit();
                budgetChangedToast.show();

                startActivity(startMainActivityIntent);
            }
        } else if (v.getId() == R.id.resetBudgetNowButton) {
            // reset total dollars left to equal total monthly
            // tempTotalMonthlyBudget = Double.parseDouble(sharedPreferences.getString("totalMonthlyBudget", "0.0"));
            // prefEditor.putString("totalDollarsLeft", Double.toString(tempTotalMonthlyBudget));
            tempTotalMonthlyBudget = sharedPreferences.getString("totalMonthlyBudget", "0.0");
            prefEditor.putString("totalDollarsLeft", tempTotalMonthlyBudget);
            prefEditor.commit();
            budgetResetToast.show();
        }
    }

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
