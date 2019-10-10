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

public class WorkPreferences extends AppCompatActivity {
    Spinner dayPreference;
    private EditText alarmMinutesPreference; //Added on 4 - 5- 2019
    private EditText currentPassword; //Added on 4 - 4- 2019
    private Button rememberPassword; //Added on 7 - 3- 2019
    private Intent i;
    private SharedPreferences.Editor editPref; //Added on 4 - 4 - 2019
    private SharedPreferences pref;
    private String newAlarmTime;

    private Button save; //Added on 6 - 24 - 2019
    private EditText getAlarmMinutesPreference; //Added on 6 - 24 - 2019


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_preferences);
        //dayPreference = (Spinner) findViewById(R.id.dayPreference);
        this.pref = getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        alarmMinutesPreference = (EditText) findViewById(R.id.AlarmMinutesPreference);
        rememberPassword = (Button) findViewById(R.id.RememberPassword);
        //alarmMinutesPreference.setText(pref.getString("NEW_ALARM_TIME", ""));
        //newAlarmTime = alarmMinutesPreference.getText().toString();

        String minutes = pref.getString("NEW_ALARM_TIME", "20");
        alarmMinutesPreference.setText(minutes);

        //alarmMinutesPreference.setText(newAlarmTime);
        //currentPassword = (EditText) findViewById(R.id.currentPassword);
        save = (Button) findViewById(R.id.save);
        i = getIntent();

        //editPref = pref.edit();
        //i.putExtra("SAVE_ALARM_MINUTES", alarmMinutesPreference.getText().toString());

        //editPref.putString("NEW_ALARM_MINUTES", "15");
        //alarmMinutesPreference.setText(pref.getString("UPDATED_ALARM_TIME", ""));
        //newAlarmTime = alarmMinutesPreference.getText().toString();


        //editPref.putString("UPDATED_ALARM_TIME", newAlarmTime);
        i.putExtra("NEW_ALARM_TIME", newAlarmTime);

        //editPref.putString("UPDATED_ALARM_TIME", alarmMinutesPreference.getText().toString()); //don't check for length

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
                String updateTime = alarmMinutesPreference.getText().toString();

                //if (updateTime == "" || updateTime == null) {
                if (updateTime.equals("")) {
                    updateTime =  pref.getString("NEW_ALARM_TIME", "20");

                }
                //alarmMinutesPreference.setText(updateTime);
                //Log.e("LG_WORK_PHONE", "NEW ALARM TIME IS: " + newAlarmTime );
                Log.e("LG_WORK_PHONE", "NEW ALARM TIME AGAIN IS: " + updateTime);
                editPref = pref.edit();
                //newAlarmTime = alarmMinutesPreference.getText().toString();
                //alarmMinutesPreference.setText(newAlarmTime);
                //alarmMinutesPreference.app;
                //i.putExtra("NEW_ALARM_TIME", newAlarmTime);
                //editPref.putString("NEW_ALARM_TIME", newAlarmTime);

                //alarmMinutesPreference.setText(updateTime);

                AlarmTimer alarmTimer = AlarmTimer.getInstance();
                alarmTimer.setAlarmTime(getBaseContext(), alarmTimer.getStartMilitaryHour(),alarmTimer.getMilitaryMinute(), Integer.parseInt(updateTime) );

                editPref.putInt("ALARM_HOUR", alarmTimer.getUpdatedHour());
                editPref.putInt("MINUTES", alarmTimer.getUpdatedMinute());
                //editPref.putInt("NEW_DOWNLOAD_DATE", 3);
                editPref.apply();


                setResult(1, i);
                finish();
            }
        });


    }//end onCreate()


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
