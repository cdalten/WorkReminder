/*
 Copyright © 2017-2019 Chad Altenburg <cdalten@PumpingDansHotLookingStepMom.com>

 Permission to use, copy, modify, distribute, and sell this software and its
 documentation for any purpose is hereby granted without fee, provided that
 the above copyright notice appear in all copies and that both that
 copyright notice and this permission notice appear in supporting
 documentation.  No representations are made about the suitability of this
 software for any purpose.  It is provided "as is" without express or
 implied warranty.
*/
package com.example.cd.workreminder;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class WorkPreferences extends AppCompatActivity {
    Spinner dayPreference;
    private EditText alarmMinutesPreference; //Added on 4 - 5- 2019
    private EditText currentPassword; //Added on 4 - 4- 2019
    private Button rememberPassword; //Added on 7 - 3- 2019
    private Intent i;
    private SharedPreferences pref;
    private int newAlarmTime; //Added on 10 - 28 - 2019

    private Button save; //Added on 6 - 24 - 2019


    /*
      The Diazepam, which I use for Vertigo, was making me a bit loopy when I wrote this. So yeah,
      I don't know the reasoning behind it since I was high at the time. But I don't touch it
      because the shit don't break.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_preferences);
        //dayPreference = (Spinner) findViewById(R.id.dayPreference);
        this.pref = getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        alarmMinutesPreference = (EditText) findViewById(R.id.alarmMinutesPreference);
        rememberPassword = (Button) findViewById(R.id.RememberPassword);

        //currentPassword = (EditText) findViewById(R.id.currentPassword);
        save = (Button) findViewById(R.id.save);
        i = getIntent();


        pref = getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        newAlarmTime = pref.getInt(getString(R.string.ALARM_MINUTES), WorkReaderContract.WorkEntry.ALARM_DEFAULT);
        alarmMinutesPreference.setText(newAlarmTime + "");

        //editPref = pref.edit();
        //editPref.putString("PASSWORD", currentPassword.getText().toString());
        //editPref.apply();
        rememberPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WorkPreferences.this, RememberMe.class));
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String updateTime = alarmMinutesPreference.getText().toString().trim();

                /*
                  Because every other forced updated seems to undo my XML preference settings.
                 */
                if (isValid(updateTime)) {
                    updateTime =  pref.getString("NEW_ALARM_TIME", "20");
                    Toast.makeText(getApplicationContext(), "Invalid Input.", Toast.LENGTH_LONG).show();
                }

                else if (updateTime.equals("") || updateTime == null ||
                        Integer.parseInt(updateTime) < 0 ||
                        Integer.parseInt(updateTime) > 720
                        )
                {
                    updateTime =  pref.getString("NEW_ALARM_TIME", "20");
                    Toast.makeText(getApplicationContext(), "Invalid Input.", Toast.LENGTH_LONG).show();
                }
                alarmMinutesPreference.setText(updateTime);

                AlarmTimer alarmTimer = AlarmTimer.getInstance();
                alarmTimer.setMinutesBeforeShift(getApplicationContext(), Integer.parseInt(updateTime));

                Log.e("LG_WORK_PHONE", "NEW ALARM TIME AGAIN IS: " + updateTime);
                Log.e("LG_WORK_PHONE", "NEW ALARM HOUR IS: " + alarmTimer.getNewMilitaryHour());
                Log.e("LG_WORK_PHONE", "NEW ALARM MINUTE IS: " + alarmTimer.getNewMilitaryMinute());
                //alarmTimer.setSavedAlarmTime(getApplicationContext(), "",
                //        alarmTimer.getStartMilitaryHour(),alarmTimer.getMilitaryMinute(), true );

                //editPref.putInt("ALARM_HOUR", alarmTimer.getUpdatedHour()); //??
                //editPref.putInt("MINUTES", alarmTimer.getUpdatedMinute()); //??
                //editPref.putInt("NEW_DOWNLOAD_DATE", 3);
                //editPref.apply(); //??

                /*
                  This is done because I don't want the notification calls to be spread across a
                  bunch of different files. Yeah, this might suck. But it makes debugging 10x
                  easier.
                 */
                setResult(WorkReaderContract.WorkEntry.RESULT_OKAY_UPDATE_WORK_ALARM_TIME, i);
                finish();
            }
        });


    }//end onCreate()


    //Copied from stackoverflow
    private boolean isValid(String str) {
        //if (str.length() != 1) return false;
        char c = Character.toLowerCase(str.charAt(0));
        return c >= 'a' && c <= 'z';
    }

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

}//end class}
