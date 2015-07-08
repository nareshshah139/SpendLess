package com.longpaws.spendless;

import static com.longpaws.spendless.ListViewConstantsDB.FIRST_COLUMN;
import static com.longpaws.spendless.ListViewConstantsDB.SECOND_COLUMN;
import static com.longpaws.spendless.ListViewConstantsDB.THIRD_COLUMN;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/*
 * Screen/Activity that shows spending history.
 *
 * Currently, the only method created is viewDatabase() which uses a DataBaseHandler object to
 * display the entire contents of the database.
 *
 * Uses the ArrayList returned from the DataBaseHandler class and the ListView to display contents
 * of database on the screen.
 */

public class DataBaseScreen extends Activity implements View.OnClickListener {

    private String enteredMonth;
    private int enteredYear;
    private int currentYear;
    private String currentMonth;

    private Calendar calendar;

    Intent goBackToMainIntent;
    EditText enterYearET;
    EditText enterMonthET;
    TextView totalDollarsTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base_screen);

        // Creates and/or initializes Button, OnClickListener, and Intent
        Button goToMainButton = (Button) findViewById(R.id.goBackToMain);
        goToMainButton.setOnClickListener(this);
        goBackToMainIntent = new Intent(this, MainActivity.class);

        // Create button and setOnClickListener
        Button submitMonthYearQueryButton = (Button) findViewById(R.id.submitMonthYearQuery);
        submitMonthYearQueryButton.setOnClickListener(this);

        // Create EditText boxes and TextView
        enterMonthET = (EditText) findViewById(R.id.enterMonth);
        enterYearET = (EditText) findViewById(R.id.enterYear);
        totalDollarsTV = (TextView) findViewById(R.id.totalTextView);

        // Set addTextChangedListener
        enterMonthET.addTextChangedListener(enterMonthListener);
        enterYearET.addTextChangedListener(enterYearListener);

        // Creates calendar instance and gets current month/year
        calendar = Calendar.getInstance();
        currentMonth = MainActivity.getMonthString(calendar.get(Calendar.MONTH));
        currentYear = calendar.get(Calendar.YEAR);

        // Calls viewMonth() with currentMonth/currentYear by default
        viewMonth(currentMonth, currentYear);
    }


    // If goBackToMain button is clicked, launch intent to start MainActivity
    // If submitMonthYearQuery is clicked, show Database for specific month/year
    @Override
    public void onClick(View v) {

        if (v.getId()== R.id.goBackToMain) {
            startActivity(goBackToMainIntent);
        } else if (v.getId()==R.id.submitMonthYearQuery) {
            viewMonth(enteredMonth, enteredYear);
        }
    }


    // Displays entire database; calls displayDataBase() from DataBaseHandler
    // This will display each row from the database in a row in ListView;
    // And set each column accordingly as determined in ListViewCustomAdapter
    public void viewDataBase() {
        ListView listView = (ListView) findViewById(R.id.list_view);
        DataBaseHandler dbHandler = new DataBaseHandler(this, null, null, 1);

        ArrayList<HashMap<String, String>> theList = dbHandler.displayDataBase();
        ListViewCustomAdapter theAdapter = new ListViewCustomAdapter(this, theList);
        listView.setAdapter(theAdapter);
    }

    // Display data for Month/Year entered as arguments
    // Will display data for each row in corresponding column in the ListView
    public void viewMonth(String Month, int Year) {
        ListView listView = (ListView) findViewById(R.id.list_view);
        DataBaseHandler dbHandler = new DataBaseHandler(this, null, null, 1);

        ArrayList<HashMap<String, String>> theList = dbHandler.displayMonth(Month, Year);
        ListViewCustomAdapter theAdapter = new ListViewCustomAdapter(this, theList);
        listView.setAdapter(theAdapter);

        // Displays the total amount for that month in the totalDollars TextView
        double total = 0.0;
        for (int index=0; index < theList.size(); index++) {
            HashMap tempMap = theList.get(index);
            String tempDollarString = (String) tempMap.get(THIRD_COLUMN);
            tempDollarString = tempDollarString.substring(1);
            double temp = Double.parseDouble(tempDollarString);
            total += temp;
        }
        totalDollarsTV.setText("Total: $" + (Double.toString(total)));
    }


    // TextWatchers will assign value that user enters into appropriate variables
    // when EditText is changed;
    // These values will be used as arguments when viewMonth() is called when
    // the Submit button is clicked; displaying the spending from desired month in the ListView
    private TextWatcher enterMonthListener = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        // Assigns value that user entered in the enterMonth EditText to the String variable
        // enteredMonth
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            enteredMonth = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private TextWatcher enterYearListener = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        // Assigns value that user entered in the enterYear EditText to the int variable enteredYear
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                enteredYear = Integer.parseInt(s.toString());
            } catch (NumberFormatException e) {
                enteredYear = 0;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_base_screen, menu);
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
