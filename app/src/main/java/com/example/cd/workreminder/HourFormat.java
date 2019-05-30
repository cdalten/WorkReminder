package com.example.cd.workreminder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Calendar;

//public class HourFormat extends AppCompatActivity {
public class HourFormat extends FragmentActivity {
    Spinner startHour; //Added on 1 - 22 - 2019
    Spinner startMinute; //Added on 1 - 23 - 2019
    Spinner startAmOrPm;
    Spinner endHour;
    Spinner endMinute;
    Spinner endAmOrPm;
    Spinner dayOfTheWeek; //Added on 2 - 1 - 2019

    Intent intent; //Added on 1 - 24 - 2019
    Button finish; //Added on 2 - 1- 2019
    private int dayPosition;
    private final String PRODUCTION_TAG = "LG_WORK_PHONE";
    private SharedPreferences pref; //Added on 5 - 24 - 2019
    private int saveDay = 0; //Added on 5 - 28 - 2019


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hour_format);

        dayOfTheWeek = (Spinner) findViewById(R.id.dayOfTheWeek);
        startHour = (Spinner) findViewById(R.id.startHour);
        startMinute = (Spinner) findViewById(R.id.startMinute);
        startAmOrPm = (Spinner) findViewById(R.id.startAmOrPm);
        endHour = (Spinner) findViewById(R.id.endHour);
        endMinute = (Spinner) findViewById(R.id.endMinute);
        endAmOrPm = (Spinner) findViewById(R.id.endAmOrPm);
        finish = (Button) findViewById(R.id.finish);

        pref = getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        //Pipe data back.
        intent = getIntent();

        //Day of the week
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.day_of_the_week, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayOfTheWeek.setAdapter(adapter);
        dayOfTheWeek.setSelection(intent.getIntExtra("DAY_WEEK", 0));//get via intent
        dayOfTheWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //intent.putExtra(getString(R.string.com_example_cd_shiftreminder_DAY_OF_THE_WEEK),
                //        parent.getItemAtPosition(position).toString());

                dayPosition = position;
                switch (position) {
                    case WorkReaderContract.WorkEntry.SUNDAY:
                        saveDay = position;
                        Log.e(PRODUCTION_TAG, "THE DAY IS SUNDAY");
                        break;
                    case WorkReaderContract.WorkEntry.MONDAY:
                        saveDay = position;
                        Log.e(PRODUCTION_TAG, "THE DAY IS MONDAY");
                        break;
                    case WorkReaderContract.WorkEntry.TUESDAY:
                        saveDay = position;
                        Log.e(PRODUCTION_TAG, "THE DAY IS TUESDAY");
                        break;
                    case WorkReaderContract.WorkEntry.WEDNESAY:
                        saveDay = position;
                        Log.e(PRODUCTION_TAG, "THE DAY IS WEDNESDAY");
                        break;
                    case WorkReaderContract.WorkEntry.THURSDAY:
                        saveDay = position;
                        Log.e(PRODUCTION_TAG, "THE DAY IS THURSDAY");
                        break;
                    case WorkReaderContract.WorkEntry.FRIDAY:
                        saveDay = position;
                        Log.e(PRODUCTION_TAG, "THE DAY IS FRIDAY");
                        break;
                    case WorkReaderContract.WorkEntry.SATURDAY:
                        saveDay = position;
                        Log.e(PRODUCTION_TAG, "THE DAY IS SATURDAY");
                        break;
                    case WorkReaderContract.WorkEntry.OFF:
                        SharedPreferences.Editor editor = pref.edit();
                        switch(saveDay) {
                            case WorkReaderContract.WorkEntry.SUNDAY:
                                intent.putExtra("POSITION", WorkReaderContract.WorkEntry.SUNDAY);
                                break;
                            case WorkReaderContract.WorkEntry.MONDAY:
                                intent.putExtra("POSITION", WorkReaderContract.WorkEntry.MONDAY);
                                break;
                            case WorkReaderContract.WorkEntry.TUESDAY:
                                intent.putExtra("POSITION", WorkReaderContract.WorkEntry.TUESDAY);
                                break;
                            case WorkReaderContract.WorkEntry.WEDNESAY:
                                intent.putExtra("POSITION", WorkReaderContract.WorkEntry.WEDNESAY);
                                break;
                            case WorkReaderContract.WorkEntry.THURSDAY:
                                intent.putExtra("POSITION", WorkReaderContract.WorkEntry.THURSDAY);
                                break;
                            case WorkReaderContract.WorkEntry.FRIDAY:
                                editor.putString(getString(R.string.FRIDAY_START_HOUR), "");
                                editor.putString(getString(R.string.FRIDAY_START_MINUTE), "");
                                editor.putString(getString(R.string.FRIDAY_START_AM_OR_PM), "");
                                editor.putString(getString(R.string.FRIDAY_END_HOUR), "");
                                editor.putString(getString(R.string.FRIDAY_END_MINUTE), "");
                                editor.putString(getString(R.string.FRIDAY_END_AM_OR_PM), "");
                                intent.putExtra("POSITION", WorkReaderContract.WorkEntry.FRIDAY);
                                Log.e(PRODUCTION_TAG, "GOT FRIDAY DAY OFF AT POSITION: " + intent.getIntExtra("POSITION", 0));
                                break;
                            case WorkReaderContract.WorkEntry.SATURDAY:
                                //intent.putExtra(getString(R.string.START_HOUR), "");
                                intent.putExtra("POSITION", WorkReaderContract.WorkEntry.SATURDAY);

                                break;
                        }
                        editor.apply();

                        startHour.setVisibility(View.INVISIBLE);
                        startMinute.setVisibility(View.INVISIBLE);
                        startAmOrPm.setVisibility(View.INVISIBLE);
                        endHour.setVisibility(View.INVISIBLE);
                        endMinute.setVisibility(View.INVISIBLE);
                        endAmOrPm.setVisibility(View.INVISIBLE);
                        break;
                }//end switch

                Log.e(PRODUCTION_TAG, "THE DAY OF THE WEEK IS: " + position);
                intent.putExtra(getString(R.string.DAY_OF_WEEK), position);
                setResult(0, intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //-----START TIME--------------
        adapter = ArrayAdapter.createFromResource(this,
                R.array.start_hour, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startHour.setAdapter(adapter);
        startHour.setSelection(intent.getIntExtra("START_HOUR", 0));
        startHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //if (position != 0) { //Possible bug??

                //setAlarmTime(9, 10, Integer.parseInt(pref.getString("ALARM_MINUTES", "")));

                SharedPreferences.Editor editor = pref.edit();
                switch (dayPosition) {
                    case WorkReaderContract.WorkEntry.SUNDAY:
                        editor.putString(getString(R.string.SUNDAY_START_HOUR),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.MONDAY:
                        editor.putString(getString(R.string.MONDAY_START_HOUR),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.TUESDAY:
                        editor.putString(getString(R.string.TUESDAY_START_HOUR),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.WEDNESAY:
                        editor.putString(getString(R.string.WEDNESDAY_START_HOUR),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.THURSDAY:
                        editor.putString(getString(R.string.WEDNESDAY_START_HOUR),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.FRIDAY:
                        editor.putString(getString(R.string.FRIDAY_START_HOUR),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.SATURDAY:
                        editor.putString(getString(R.string.SATURDAY_START_HOUR),
                                parent.getItemAtPosition(position).toString());
                        break;
                }
                editor.apply();
                //Log.e(PRODUCTION_TAG, "THE START HOUR IS:" +  startHour.getItemAtPosition(startHour.getSelectedItemPosition()).toString());
                //startHour.setSelection(2);
                //Log.e(PRODUCTION_TAG, "THE NEW START HOUR IS:" +  startHour.getItemAtPosition(startHour.getSelectedItemPosition()).toString());
                intent.putExtra(getString(R.string.START_HOUR),
                        parent.getItemAtPosition(position).toString());
                setResult(0, intent);
                //}//end if
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //pass
            }
        });

        adapter = ArrayAdapter.createFromResource(this,
                R.array.start_minute, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startMinute.setAdapter(adapter);
        startMinute.setSelection(intent.getIntExtra("START_MINUTE", 0));
        startMinute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = pref.edit();
                Log.e(PRODUCTION_TAG, "THE POSITION BEFORE IS: " + position);
                switch (dayPosition) {
                    case WorkReaderContract.WorkEntry.SUNDAY:
                        editor.putString(getString(R.string.SUNDAY_START_MINUTE),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.MONDAY:
                        editor.putString(getString(R.string.MONDAY_START_MINUTE),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.TUESDAY:
                        editor.putString(getString(R.string.TUESDAY_START_MINUTE),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.WEDNESAY:
                        editor.putString(getString(R.string.WEDNESDAY_START_MINUTE),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.THURSDAY:
                        editor.putString(getString(R.string.THURSDAY_START_MINUTE),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.FRIDAY:
                        editor.putString(getString(R.string.FRIDAY_START_MINUTE),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.SATURDAY:
                        editor.putString(getString(R.string.SATURDAY_START_MINUTE),
                                parent.getItemAtPosition(position).toString());
                        break;
                }
                editor.apply();
                intent.putExtra(getString(R.string.START_MINUTE),
                        parent.getItemAtPosition(position).toString());
                setResult(0, intent);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        adapter = ArrayAdapter.createFromResource(this,
                R.array.start_am_or_pm, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startAmOrPm.setAdapter(adapter);
        startAmOrPm.setSelection(intent.getIntExtra("START_AM_OR_PM", 0));
        //Because interfaces still suck massive dick
        startAmOrPm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = pref.edit();
                switch (dayPosition) {
                    case WorkReaderContract.WorkEntry.SUNDAY:
                        editor.putString(getString(R.string.SUNDAY_START_AM_OR_PM),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.MONDAY:
                        editor.putString(getString(R.string.MONDAY_START_AM_OR_PM),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.TUESDAY:
                        editor.putString(getString(R.string.TUESDAY_START_AM_OR_PM),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.WEDNESAY:
                        editor.putString(getString(R.string.WEDNESDAY_START_AM_OR_PM),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.THURSDAY:
                        editor.putString(getString(R.string.THURSDAY_START_AM_OR_PM),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.FRIDAY:
                        editor.putString(getString(R.string.FRIDAY_START_AM_OR_PM),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.SATURDAY:
                        editor.putString(getString(R.string.SATURDAY_START_AM_OR_PM),
                                parent.getItemAtPosition(position).toString());
                        break;
                }
                editor.apply();
                intent.putExtra(getString(R.string.START_AM_OR_PM), parent.getItemAtPosition(position).toString());
                //if  (position != 0) { //possible bug??
                //forceBacktoMain.putExtra(getString(R.string.com_example_cd_shiftreminder_START_AM_OR_PM),
                //        parent.getItemAtPosition(position).toString());
                //}

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //----END TIME--------------
        adapter = ArrayAdapter.createFromResource(this,
                R.array.end_hour, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endHour.setAdapter(adapter);
        endHour.setSelection(intent.getIntExtra("END_HOUR", 0));
        endHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = pref.edit();
                switch (dayPosition) {
                    case WorkReaderContract.WorkEntry.SUNDAY:
                        editor.putString(getString(R.string.SUNDAY_END_HOUR),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.MONDAY:
                        editor.putString(getString(R.string.MONDAY_END_HOUR),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.TUESDAY:
                        editor.putString(getString(R.string.TUESDAY_END_HOUR),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.WEDNESAY:
                        editor.putString(getString(R.string.WEDNESDAY_END_HOUR),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.THURSDAY:
                        editor.putString(getString(R.string.THURSDAY_END_HOUR),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.FRIDAY:
                        editor.putString(getString(R.string.FRIDAY_END_HOUR),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.SATURDAY:
                        editor.putString(getString(R.string.SATURDAY_END_HOUR),
                                parent.getItemAtPosition(position).toString());
                        break;
                }
                editor.apply();
                intent.putExtra(getString(R.string.END_HOUR),
                        parent.getItemAtPosition(position).toString());
                setResult(0, intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter = ArrayAdapter.createFromResource(this,
                R.array.end_minute, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endMinute.setAdapter(adapter);
        endMinute.setSelection(intent.getIntExtra("END_MINUTE", 0));
        endMinute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = pref.edit();
                switch (dayPosition) {
                    case WorkReaderContract.WorkEntry.SUNDAY:
                        editor.putString(getString(R.string.SUNDAY_END_MINUTE),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.MONDAY:
                        editor.putString(getString(R.string.MONDAY_END_MINUTE),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.TUESDAY:
                        editor.putString(getString(R.string.TUESDAY_END_MINUTE),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.WEDNESAY:
                        editor.putString(getString(R.string.WEDNESDAY_END_MINUTE),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.THURSDAY:
                        editor.putString(getString(R.string.THURSDAY_END_MINUTE),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.FRIDAY:
                        editor.putString(getString(R.string.FRIDAY_END_MINUTE),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.SATURDAY:
                        editor.putString(getString(R.string.SATURDAY_END_MINUTE),
                                parent.getItemAtPosition(position).toString());
                        break;
                }
                editor.apply();
                intent.putExtra(getString(R.string.END_MINUTE), parent.getItemAtPosition(position).toString());
                setResult(0, intent);
                //forceBacktoMain.putExtra(Days.END_MINUTE, parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //pass
            }
        });

        adapter = ArrayAdapter.createFromResource(this,
                R.array.end_am_or_pm, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endAmOrPm.setAdapter(adapter);
        endAmOrPm.setSelection(intent.getIntExtra("END_AM_OR_PM", 0));
        endAmOrPm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = pref.edit();
                switch (dayPosition) {
                    case WorkReaderContract.WorkEntry.SUNDAY:
                        editor.putString(getString(R.string.SUNDAY_END_AM_OR_PM),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.MONDAY:
                        editor.putString(getString(R.string.MONDAY_END_AM_OR_PM),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.TUESDAY:
                        editor.putString(getString(R.string.TUESDAY_END_AM_OR_PM),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.WEDNESAY:
                        editor.putString(getString(R.string.WEDNESDAY_END_AM_OR_PM),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.THURSDAY:
                        editor.putString(getString(R.string.THURSDAY_END_AM_OR_PM),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.FRIDAY:
                        editor.putString(getString(R.string.FRIDAY_END_AM_OR_PM),
                                parent.getItemAtPosition(position).toString());
                        break;
                    case WorkReaderContract.WorkEntry.SATURDAY:
                        editor.putString(getString(R.string.SATURDAY_END_AM_OR_PM),
                                parent.getItemAtPosition(position).toString());
                        break;
                }
                editor.apply();
                intent.putExtra(getString(R.string.END_AM_OR_PM), parent.getItemAtPosition(position).toString());
                setResult(0, intent);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(forceBacktoMain);
                finish();
            }
        });
    }//end oncreate)()

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle("Discard Changes");
        builder.setMessage("ARE YOU SURE YOU WANT TO DISCARD ANY POSSIBLE CHANGES?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                finish();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.create().show();
        ;
    }
}//end class
