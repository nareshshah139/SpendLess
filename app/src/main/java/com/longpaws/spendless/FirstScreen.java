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


public class FirstScreen extends Activity implements View.OnClickListener {

    private double tempMonthlyBudget;
    EditText enterMonthlyBudgetET;

    Intent startMainActivityIntent;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefEditor;

    Calendar Cal;
    private int startYear;
    private int startMonth;

    Toast incorrectFormatToast;
    String formatToastMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        prefEditor = sharedPreferences.edit();

        enterMonthlyBudgetET = (EditText) findViewById(R.id.enterMonthlyBudgetEditText);
        enterMonthlyBudgetET.addTextChangedListener(enterMonthlyBudgetListener);

        Button submitMonthlyBudgetBtn = (Button) findViewById(R.id.submitMonthlyBudgetButton);
        submitMonthlyBudgetBtn.setOnClickListener(this);

        startMainActivityIntent = new Intent(this, MainActivity.class);


        formatToastMessage = "Dollar amount must be submitted in correct format. Example: 100.00";
        incorrectFormatToast = Toast.makeText(getApplicationContext(), formatToastMessage, Toast.LENGTH_LONG);

        Cal = Calendar.getInstance();
        startYear = Cal.get(Calendar.YEAR);
        startMonth = Cal.get(Calendar.MONTH);


        tempMonthlyBudget = 0.0;
    }


    @Override
    public void onClick(View v) {

        /* When the submit button is clicked (first time app is opened) it will save
           whatever is in tempMonthlyBudget variable (from the TextWatcher) into the
           SharedPreference file "MyData" in "totalMonthlyBudget" and "totalDollarsLeft".
           This is to initialize both values to wahtever the user entered.

           It will also add the "activity_started" to the SharedPreference and thus the FirstScreen
           activity will be skipped next time the app is opened.

           Then it will launch the MainActivity intent

           However, if tempMonthlyBudget contains 0.0, either by default, or because the user entered
           the data in an incorrect format, then a Toast will be displayed, and the EditText will be
           reset. Pressing the button does nothing in this case. The user will then have to enter a
           new budget in correct format to be taken to the main activity page.
         */

        if (v.getId()==R.id.submitMonthlyBudgetButton) {

            if (tempMonthlyBudget==0.0) {
                incorrectFormatToast.show();
                enterMonthlyBudgetET.setText(null);
            } else {

                prefEditor.putBoolean("activity_started", true);
                prefEditor.putString("totalMonthlyBudget", Double.toString(tempMonthlyBudget));
                prefEditor.putString("totalDollarsLeft", Double.toString(tempMonthlyBudget));

                // Put the current year and month into shared preferences
                prefEditor.putInt("Year", startYear);
                prefEditor.putInt("Month", startMonth);

                prefEditor.commit();

                startActivity(startMainActivityIntent);

            }
        }

    }



    private TextWatcher enterMonthlyBudgetListener = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        /* Whenever the TextView is changed it will check to see if the value is a double.
           If it is, it will assign tempMonthlyBudget to that double. If the user enters anything
           other than a double, tempMonthlyBudget will be assigned a value of 0.0 by default.

           Needs to be changed to force user to enter a double, preferably using while loop. Display
           toast describing problem if they did not enter a double.
         */

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
