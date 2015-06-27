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
import android.widget.Button;
import android.widget.TextView;


public class DataBaseScreen extends Activity implements View.OnClickListener {

    Intent goBackToMainIntent;
    TextView textViewDataBase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base_screen);

        Button goToMainButton = (Button) findViewById(R.id.goBackToMain);
        goToMainButton.setOnClickListener(this);

        textViewDataBase = (TextView) findViewById(R.id.textBoxDataBase);

        goBackToMainIntent = new Intent(this, MainActivity.class);
        viewDataBase();
    }

    @Override
    public void onClick(View v) {

        if (v.getId()== R.id.goBackToMain) {
            startActivity(goBackToMainIntent);
        }
    }


    // not sure if it will work. Test. Test. Test.


    public void viewDataBase() {

        DataBaseHandler dbHandler = new DataBaseHandler(this, null, null, 1);
        String[] theArray = dbHandler.displayDataBase();
        String theText = "";

        for (int index = 0; index < theArray.length; index++) {
            theText = theText + theArray[index];
        }

        textViewDataBase.setText(theText);
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
