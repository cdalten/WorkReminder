/*
 Copyright © 2017-2020 Chad Altenburg <cdalten@PumpingDansHotLookingStepMom.com>

 Permission to use, copy, modify, distribute, and sell this software and its
 documentation for any purpose is hereby granted without fee, provided that
 the above copyright notice appear in all copies and that both that
 copyright notice and this permission notice appear in supporting
 documentation.  No representations are made about the suitability of this
 software for any purpose.  It is provided "as is" without express or
 implied warranty.
*/


package com.example.cd.workreminder;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Locale;

/*
  Added on 3 - 3 - 2020. To prevent the alarm from firing at say 8am if not in the 8am
  time zone.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the use
        Log.e("LG_WORK_PHONE", "The TIME PICKER hour is: " + hourOfDay);
        Log.e("LG_WORK_PHONE", "The TIME PICKER minute is: " + minute);

        //Butchered algorithm ripped off from Stackoverflow
        int hour = hourOfDay % 12;
        if (hour == 0) hour = 12;
        String amOrPm = "";

        if (hourOfDay > 12 ) {
            saveStartCivilanTime(hour, minute, "PM");
        } else {
            saveStartCivilanTime(hour, minute, "AM");
        }
        //System.out.println(String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, _AM_PM));
    }

    //Added on 3 - 4 - 202
    @TargetApi(23)
    private void saveStartCivilanTime(int civilianHour, int civilianMinute, String amOrPm) {
        DataToMemory dataToMemory = new DataToMemory(getContext());
        dataToMemory.saveCurrentCivilianHour(getContext(),
                "ALARM_HOUR",
                civilianHour);

        dataToMemory.saveCurrentCivilianMinute(getContext(),
                (getContext().getString(R.string.ALARM_MINUTES)),
                civilianMinute);

        //dataToMemory.saveCurrentCivilianAmOrPm(getContext(),
        //        getString(R.string.WEDNESDAY_START_AM_OR_PM),
        //        amOrPm);
    }
}//End class
