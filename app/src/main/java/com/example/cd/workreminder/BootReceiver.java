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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/*
  Used to preserve the alarm across device reboot. If I don't do this, there will be a slight delay
  on the first few alarms because Android is too busy loading up all the crapware/spyware.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("PRODUCTION TAG", "WORK BOOT RECEIVER GOT CALLED WITH: " + intent.getAction());
        Log.e("PRODUCTION TAG", "WORK BOOT RECEIVER GOT CALLED WITH: " + intent.getExtras());

        Log.e("BOOT_RECEIVER:", "ONRECEIVE() GOT CALLED");

         /*
         Otherwise the alarm won't fire when the user changes the alarm time after the device after
         the phone reboots. More info on this can be found at...
         https://developer.android.com/training/scheduling/alarms
         */

        try {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                //Toast.makeText(context, "Boot Detected.", Toast.LENGTH_LONG).show();
                Log.e("LG_WORK_PHONE: ", "DEVICE BOOT DETECTED");

            } else {
                Log.e("LG_WORK_PHONE", "DEVICE BOOT NOT DETECTED");
            }


        } catch (Exception e) {
            Log.e("LG_WORK_PHONE ", "BOOT ERROR IS: " + e);
        }

        final AlarmTimer alarmTimer = AlarmTimer.getInstance();
        SetAlarm setAlarm = new SetAlarm();
        setAlarm.setAlarm(context, alarmTimer);
    }
}//end class
