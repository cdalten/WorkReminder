package com.example.cd.workreminder;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class WorkPreferences extends AppCompatActivity {
    Spinner dayPreference;
    private EditText alarmMinutesPreference; //Added on 4 - 5- 2019
    private EditText currentPassword; //Added on 4 - 4- 2019
    private Button savePreferences;
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
        alarmMinutesPreference = (EditText) findViewById(R.id.alarmMinutesPreference);
        newAlarmTime = alarmMinutesPreference.getText().toString();
        //currentPassword = (EditText) findViewById(R.id.currentPassword);
        save = (Button) findViewById(R.id.save);

        this.pref = getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        i = getIntent();

        editPref = pref.edit();
        //i.putExtra("SAVE_ALARM_MINUTES", alarmMinutesPreference.getText().toString());

        //editPref.putString("NEW_ALARM_MINUTES", "15");
        newAlarmTime = alarmMinutesPreference.getText().toString();
        i.putExtra("NEW_ALARM_TIME", newAlarmTime);
        editPref.putString("UPDATED_ALARM_TIME", alarmMinutesPreference.getText().toString()); //don't check for length


        //editPref = pref.edit();
        //editPref.putString("PASSWORD", currentPassword.getText().toString());
        editPref.apply();

        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        //        R.array.day_Preference, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //dayPreference.setAdapter(adapter);

        //dayPreference.setSelection(i.getIntExtra("NEW_DOWNLOAD_DATE",
        //        pref.getInt(getString(R.string.com_example_cd_shiftreminder_SAVED_DOWNLOAD_DATE),0)));
        //dayPreference.setSelection(pref.getInt(getString(R.string.com_example_cd_shiftreminder_SAVED_DOWNLOAD_DATE),0));
        /*dayPreference.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //editPref = pref.edit();
                //i.putExtra("GET_NEW_DOWNLOAD_DATE", parent.getItemAtPosition(position).toString());
                //editPref.putString(getString(R.string.com_example_cd_shiftreminder_SAVED_DOWNLOAD_DATE), i.getStringExtra("GET_NEW_DOWNLOAD_DATE"));
                //editPref.putInt(getString(R.string.com_example_cd_shiftreminder_SAVED_DOWNLOAD_DATE),
                //        getDownloadDate(position));
                editPref.apply();
                setResult(1, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        */
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updateTime = alarmMinutesPreference.getText().toString();
                Log.e("LG_WORK_PHONE", "NEW ALARM TIME IS: " + newAlarmTime );
                Log.e("LG_WORK_PHONE", "NEW ALARM TIME AGAIN IS: " + updateTime);
                editPref = pref.edit();
                editPref.putInt(getString(R.string.ALARM_MINUTES), Integer.parseInt(updateTime));
                //editPref.putInt("NEW_DOWNLOAD_DATE", 3);
                editPref.apply();
                //alarmMinutesPreference.setText(updateTime);
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

    //Added on 4 - 4 - 2019
    @TargetApi(24)
    private int getDownloadDate(int day) {
        int newDay = 0;
        //Calendar cal = Calendar.getInstance();
        switch(day) {
            case 0:
                newDay = Calendar.SUNDAY;
                break;
            case 1:
                newDay = Calendar.MONDAY;
                break;
            case 2:
                newDay = Calendar.TUESDAY;
                break;
            case 3:
                newDay = Calendar.WEDNESDAY;
                break;
            case 4:
                newDay = Calendar.THURSDAY;
                break;
            case 5:
                newDay = Calendar.FRIDAY;
                break;
            case 6:
                newDay = Calendar.SATURDAY;
                break;
        }
        return newDay;
    }
}//end class}
