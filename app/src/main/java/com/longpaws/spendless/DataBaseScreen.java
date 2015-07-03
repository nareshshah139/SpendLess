package com.longpaws.spendless;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class DataBaseScreen extends Activity implements View.OnClickListener {

    Intent goBackToMainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base_screen);

        // Creates and/or initializes Button, OnClickListener, and Intent
        Button goToMainButton = (Button) findViewById(R.id.goBackToMain);
        goToMainButton.setOnClickListener(this);
        goBackToMainIntent = new Intent(this, MainActivity.class);

        // Calls viewDataBase() displaying the 'entire' database
        viewDataBase();
    }


    // If goBackToMain button is clicked, launch intent to start MainActivity
    @Override
    public void onClick(View v) {

        if (v.getId()== R.id.goBackToMain) {
            startActivity(goBackToMainIntent);
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
