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


public class ChangeBudgetScreen extends Activity implements View.OnClickListener {


    private double tempChangeMonthlyBudget;

    EditText changeMonthlyBudgetET;

    Intent startMainActivityIntent;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefEditor;

    String toastChangeBudget;
    Toast budgetChangedToast;

    Toast incorrectFormatToast;
    String formatToastMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_budget_screen);

        sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        prefEditor = sharedPreferences.edit();

        changeMonthlyBudgetET = (EditText) findViewById(R.id.changeMonthlyBudgetEditText);
        changeMonthlyBudgetET.addTextChangedListener(changeMonthlyBudgetListener);

        startMainActivityIntent = new Intent(this, MainActivity.class);

        Button backToMainButton = (Button) findViewById(R.id.BackToMainButton);
        backToMainButton.setOnClickListener(this);

        Button submitMonthlyBudgetChangeButton = (Button) findViewById(R.id.submitMonthlyChangeButton);
        submitMonthlyBudgetChangeButton.setOnClickListener(this);

        // Toast Trial
        toastChangeBudget = "Budget will reset on the first of the next month.";
        budgetChangedToast = Toast.makeText(getApplicationContext(), toastChangeBudget, Toast.LENGTH_LONG);

        formatToastMessage = "Dollar amount must be submitted in correct format. Example: 100.00";
        incorrectFormatToast = Toast.makeText(getApplicationContext(), formatToastMessage, Toast.LENGTH_LONG);

        tempChangeMonthlyBudget = 0.0;
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
