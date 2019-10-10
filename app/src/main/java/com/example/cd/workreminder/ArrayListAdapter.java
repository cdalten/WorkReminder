
package com.example.cd.workreminder;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ArrayListAdapter extends ArrayAdapter<String> {
    private int visibleMenuOptions = 3; //3 is for debugging only

    storeHoursInGUI currentWeek = new storeHoursInGUI(getContext());
    //ArrayList week = currentWeek.addHours();
    ArrayList<ArrayList<String>> week;

    TextView text_day; //Added on 2 - 10 - 2019
    TextView text_start_hour; //Added on 2 - 10 -2019
    TextView text_separator; //Added on 3 - 19 - 2019
    TextView text_end_hour; //Added on 3 - 19 -2019

    private final String PRODUCTION_TAG = "LG WORK PHONE";

    Context context;

    ArrayListAdapter(Context context, int resource, List<String> Objects) {
        super(context, resource, Objects);
        this.context = context;
        this.visibleMenuOptions = visibleMenuOptions;
    }

    @Override
    public int getCount() {
        //return 6;
        //return len;
        return week.size() - 1;
    }

    @Override
    public long getItemId(int position) {
        return week.get(position).hashCode();
    }

    @Override
    public java.lang.String getItem(int position) {
        return week.get(position).toString();
    }


    //Added on 6 - 24 - 201
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getDropDownView(position, convertView, parent);
        Log.e(PRODUCTION_TAG, "MENU GOT HIDDEN AT POSITION: " + position);
        View v = null;
        if (position == visibleMenuOptions) {
            Log.e(PRODUCTION_TAG, "MENU GOT HIDDEN AT POSITION: " + position);
            TextView tv = new TextView(getContext());
            tv.setVisibility(View.GONE);
            v = tv;
        } else {
            v = super.getDropDownView(position, null, parent);
        }
        return v;
    }

    @TargetApi(24)
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //super.getView(position, convertView, parent);
        if (convertView == null) {
            //Log.e(PRODUCTION_TAG, "CONVERT VIEW IS NULL");

            //LayoutInflater inflater =
            //convertView = context.getLayoutInflater().inflate(R.layout.schedule_list, parent, false);
            //convertView = getLayoutInflater().inflate(android.R.layout.simple_expandable_list_item_2, parent, false);
        }

            /*Display display;
            Point size;
            int width, height;
            float txtsize;

            display = getWindowManager().getDefaultDisplay();
            size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
            */
        if (week.get(position).get(0).equals("SUNDAY")) {
            //savePreviousSaturday();
        }

        //set list view to a blank line
        if (week.get(position).get(0).equals("OFF")) {
            ((TextView) convertView.findViewById(R.id.dayOfWeek)).setText("");
            ((TextView) convertView.findViewById(R.id.startTime)).setText("");
            ((TextView) convertView.findViewById(R.id.hour_separator)).setText("");
            ((TextView) convertView.findViewById(R.id.endTime)).setText("");
        } else {
            //week.remove(position);
            //week.get(0).add(dayPair.get(position));
            ((TextView) convertView.findViewById(R.id.dayOfWeek)).setText(week.get(position).get(0));
            ((TextView) convertView.findViewById(R.id.startTime))
                    .setText(week.get(position).get(WorkReaderContract.WorkEntry.START_HOUR)
                            + ":" + week.get(position).get(WorkReaderContract.WorkEntry.START_MINUTE) + " "
                            + week.get(position).get(WorkReaderContract.WorkEntry.START_AM_OR_PM));
            ((TextView) convertView.findViewById(R.id.hour_separator)).setText("to");
            ((TextView) convertView.findViewById(R.id.endTime))
                    .setText(week.get(position).get(WorkReaderContract.WorkEntry.END_HOUR) + ":"
                            + week.get(position).get(WorkReaderContract.WorkEntry.END_MINUTE) + " "
                            + week.get(position).get(WorkReaderContract.WorkEntry.END_AM_OR_PM));
        }

        text_start_hour = (TextView)convertView.findViewById(R.id.startTime);
        text_end_hour = (TextView)convertView.findViewById(R.id.endTime);
        //text_day = (TextView)convertView.findViewById(android.R.id.text1); //modified on 2 - 22 - 2019
        text_day = (TextView)convertView.findViewById(R.id.dayOfWeek);

        text_separator = (TextView)convertView.findViewById(R.id.hour_separator);

        //stupid hack
        text_start_hour.setMinHeight(0); // Min Height
        text_start_hour.setMinimumHeight(0); // Min Height
        text_start_hour.setHeight(120); // Height in pixels. Not dip?
        text_start_hour.setWidth(0);

        //text_separator.setMinHeight(0); // Min Height
        //text_separator.setMinimumHeight(0); // Min Height
        //text_separator.setHeight(120); // Height in pixels. Not dip?
        //text_separator.setWidth(20);

        text_end_hour.setMinHeight(0); // Min Height
        text_end_hour.setMinimumHeight(0); // Min Height
        text_end_hour.setHeight(120); // Height in pixels. Not dip?
        text_end_hour.setWidth(0);

        //text_day.setMinHeight(0); // Min Height
        //text_day.setMinTimumHeight(0); // Min Height
        //text_day.setHeight(120); // Height in pixels. Not dip?

        //text_day.setText(days[position]);

        Calendar cal = Calendar.getInstance();
        //if (handleThirdShift() == position + 1) {
            text_start_hour.setTypeface(text_start_hour.getTypeface(), Typeface.BOLD);  //vs null??
            text_separator.setTypeface(text_separator.getTypeface(), Typeface.BOLD);  //vs null??
            text_end_hour.setTypeface(text_end_hour.getTypeface(), Typeface.BOLD);  //vs null??
            text_day.setTypeface(text_day.getTypeface(), Typeface.BOLD);

        //}

        //return super.getView(position, convertView, parent);
        return convertView;
        //return  view;
    }//end method

        }//end class
