package com.longpaws.spendless;

import static com.longpaws.spendless.ListViewConstantsDB.FIRST_COLUMN;
import static com.longpaws.spendless.ListViewConstantsDB.SECOND_COLUMN;
import static com.longpaws.spendless.ListViewConstantsDB.THIRD_COLUMN;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mitch on 6/28/2015.
 */

public class ListViewCustomAdapter extends BaseAdapter {

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;

    public ListViewCustomAdapter(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = activity.getLayoutInflater();

        //if (convertView == null) {

            convertView = inflater.inflate(R.layout.multi_col_layout, null);
            txtFirst = (TextView) convertView.findViewById(R.id.MONTH_YEAR_TEXTVIEW);
            txtSecond = (TextView) convertView.findViewById(R.id.EXPENSE_NAME_TEXTVIEW);
            txtThird = (TextView) convertView.findViewById(R.id.DOLLARS_SPENT_TEXTVIEW);

            //convertView.setTag(holder);

       // }

            //holder = (ViewHolder) convertView.getTag();


            HashMap<String, String> map = list.get(position);
            txtFirst.setText(map.get(FIRST_COLUMN));
            txtSecond.setText(map.get(SECOND_COLUMN));
            txtThird.setText(map.get(THIRD_COLUMN));

            return convertView;
    }


}