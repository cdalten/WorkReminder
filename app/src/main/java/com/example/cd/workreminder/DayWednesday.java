/*
 Copyright © 2017-2022 Chad Altenburg <cdalten@PumpingDansHotLookingStepMom.com>

 Permission to use, copy, modify, distribute, and sell this software and its
 documentation for any purpose is hereby granted without fee, provided that
 the above copyright notice appear in all copies and that both that
 copyright notice and this permission notice appear in supporting
 documentation.  No representations are made about the suitability of this
 software for any purpose.  It is provided "as is" without express or
 implied warranty.
*/
package com.example.cd.workreminder;

import android.app.DialogFragment;
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

public class DayWednesday extends BackKey {
    Spinner startHour; //Added on 1 - 22 - 2019
    Spinner startMinute; //Added on 1 - 23 - 2019
    Spinner startAmOrPm;
    Spinner endHour;
    Spinner endMinute;
    Spinner endAmOrPm;
    Spinner dayOfTheWeek; //Added on 2 - 1 - 2019

    Intent setWednesdayHours; //Added on 1 - 24 - 2019
    Button finish; //Added on 2 - 1- 2019
    private int dayPosition;
    private final String PRODUCTION_TAG = "DAY_WEDNESDAY:";
    //private SharedPreferences pref; //Added on 5 - 24 - 2019

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

        SharedPreferences pref = getSharedPreferences("BECAUSE_INTENTS_SUCK_MASSIVE_DICK", MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();
        //Pipe data back.
        setWednesdayHours = getIntent();

        //Get day of week from drop down menu. If off, make list view row blank
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.wednesday, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dayOfTheWeek.setAdapter(adapter);
        dayOfTheWeek.setSelection(WorkReaderContract.SELECTION_DEFAULT_VALUE);//get via intent

        dayOfTheWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.d(PRODUCTION_TAG, "WEDNESDAY DAY POSITION IS: " + position);
                switch (position) {
                    case WorkReaderContract.ON_DAY:
                        setWednesdayHours.putExtra("CURRENT_DAY", WorkReaderContract.WEDNESDAY);
                        setResult(WorkReaderContract.RESULT_OK_WORK, setWednesdayHours);
                        //saveDay = position;
                        break;
                    case WorkReaderContract.OFF_DAY: //OFF
                        SharedPreferences.Editor editor = pref.edit();

                        //case WorkReaderContract.WorkEntry.TUESDAY:
                        editor.putString(getString(R.string.WEDNESDAY_HOURS), "OFF");
                        editor.putString(getString(R.string.WEDNESDAY_START_HOUR), "");
                        editor.putString(getString(R.string.WEDNESDAY_START_MINUTE), "");
                        editor.putString(getString(R.string.WEDNESDAY_START_AM_OR_PM), "");
                        editor.putString(getString(R.string.WEDNESDAY_END_HOUR), "");
                        editor.putString(getString(R.string.WEDNESDAY_END_MINUTE), "");
                        editor.putString(getString(R.string.WEDNESDAY_END_AM_OR_PM), "");
                        setWednesdayHours.putExtra("POSITION", WorkReaderContract.OFF); //??
                        //   break;
                        //}
                        setWednesdayHours.putExtra("CURRENT_DAY", WorkReaderContract.OFF);
                        startHour.setVisibility(View.INVISIBLE);
                        startMinute.setVisibility(View.INVISIBLE);
                        startAmOrPm.setVisibility(View.INVISIBLE);
                        endHour.setVisibility(View.INVISIBLE);
                        endMinute.setVisibility(View.INVISIBLE);
                        endAmOrPm.setVisibility(View.INVISIBLE);

                        setResult(WorkReaderContract.RESULT_OKAY_NO_WORK, setWednesdayHours);
                        editor.apply();
                        break;
                }//end switch


                Log.d(PRODUCTION_TAG, "THE DAY OF THE WEEK IS: " + position);

                setWednesdayHours.putExtra("DAY_WEEK",  parent.getItemAtPosition(0).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Get starting hour from drop down menu
        adapter = ArrayAdapter.createFromResource(this,
                R.array.start_hour, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startHour.setAdapter(adapter);
        startHour.setSelection(setWednesdayHours.getIntExtra("START_HOUR",
                WorkReaderContract.SELECTION_DEFAULT_VALUE));
        //Need to check fo "OFF"
        //startHour.setSelection(Integer.parseInt( pref.getString(getString(R.string.SUNDAY_START_HOUR),
        //        WorkReaderContract.WorkEntry.START_HOUR_DEFAULT)));
        startHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putString(getString(R.string.WEDNESDAY_START_HOUR),
                        //pref.getString(getString(R.string.MONDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
                        parent.getItemAtPosition(position).toString());

                setWednesdayHours.putExtra(getString(R.string.START_HOUR),
                        parent.getItemAtPosition(position).toString());
                editor.putString(getString(R.string.WEDNESDAY_HOURS), "WEDNESDAY");
                setResult(WorkReaderContract.RESULT_OK_WORK, setWednesdayHours);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //pass
            }
        });


        //Get starting minute from drop down menu
        adapter = ArrayAdapter.createFromResource(this,
                R.array.start_minute, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startMinute.setAdapter(adapter);
        startMinute.setSelection(setWednesdayHours.getIntExtra("START_MINUTE",
                WorkReaderContract.SELECTION_DEFAULT_VALUE));
        startMinute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(PRODUCTION_TAG, "THE POSITION BEFORE IS: " + position);
                editor.putString(getString(R.string.WEDNESDAY_START_MINUTE),
                        parent.getItemAtPosition(position).toString());
                setWednesdayHours.putExtra(getString(R.string.START_MINUTE),
                        parent.getItemAtPosition(position).toString());
                editor.putString(getString(R.string.WEDNESDAY_HOURS), "WEDNESDAY");
                setResult(WorkReaderContract.RESULT_OK_WORK, setWednesdayHours);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        //Get starting am or pm from down down menu
        adapter = ArrayAdapter.createFromResource(this,
                R.array.start_am_or_pm, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startAmOrPm.setAdapter(adapter);
        startAmOrPm.setSelection(setWednesdayHours.getIntExtra("START_AM_OR_PM",
                WorkReaderContract.SELECTION_DEFAULT_VALUE));
        //Because interfaces still suck massive dick
        startAmOrPm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putString(getString(R.string.WEDNESDAY_START_AM_OR_PM),
                        parent.getItemAtPosition(position).toString());
                setWednesdayHours.putExtra(getString(R.string.START_AM_OR_PM), parent.getItemAtPosition(position).toString());
                editor.putString(getString(R.string.WEDNESDAY_HOURS), "WEDNESDAY");
                setResult(WorkReaderContract.RESULT_OK_WORK, setWednesdayHours);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Get end hour from drop down menu.
        adapter = ArrayAdapter.createFromResource(this,
                R.array.end_hour, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endHour.setAdapter(adapter);
        endHour.setSelection(setWednesdayHours.getIntExtra("END_HOUR",
                WorkReaderContract.SELECTION_DEFAULT_VALUE));
        endHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putString(getString(R.string.WEDNESDAY_END_HOUR),
                        parent.getItemAtPosition(position).toString());
                setWednesdayHours.putExtra(getString(R.string.END_HOUR),
                        parent.getItemAtPosition(position).toString());
                editor.putString(getString(R.string.WEDNESDAY_HOURS), "WEDNESDAY");
                setResult(WorkReaderContract.RESULT_OK_WORK, setWednesdayHours);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Get end minute from drop down menu
        adapter = ArrayAdapter.createFromResource(this,
                R.array.end_minute, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endMinute.setAdapter(adapter);
        endMinute.setSelection(setWednesdayHours.getIntExtra("END_MINUTE",
                WorkReaderContract.SELECTION_DEFAULT_VALUE));
        endMinute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putString(getString(R.string.WEDNESDAY_END_MINUTE),
                        parent.getItemAtPosition(position).toString());

                setWednesdayHours.putExtra(getString(R.string.END_MINUTE), parent.getItemAtPosition(position).toString());
                editor.putString(getString(R.string.WEDNESDAY_HOURS), "WEDNESDAY");
                setResult(WorkReaderContract.RESULT_OK_WORK, setWednesdayHours);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //pass
            }
        });

        //Get end am or pm from drop down menu
        adapter = ArrayAdapter.createFromResource(this,
                R.array.end_am_or_pm, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endAmOrPm.setAdapter(adapter);
        endAmOrPm.setSelection(setWednesdayHours.getIntExtra("END_AM_OR_PM",
                WorkReaderContract.SELECTION_DEFAULT_VALUE));
        endAmOrPm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putString(getString(R.string.WEDNESDAY_END_AM_OR_PM),
                        parent.getItemAtPosition(position).toString());
                setWednesdayHours.putExtra(getString(R.string.END_AM_OR_PM), parent.getItemAtPosition(position).toString());
                editor.putString(getString(R.string.WEDNESDAY_HOURS), "WEDNESDAY");
                setResult(WorkReaderContract.RESULT_OK_WORK, setWednesdayHours);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setResult(WorkReaderContract.WorkEntry.RESULT_OK_WORK, intent);
                editor.apply();
                finish();
            }
        });
    }//end oncreate)()

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setIntent(setWednesdayHours);
    }

}//end class

