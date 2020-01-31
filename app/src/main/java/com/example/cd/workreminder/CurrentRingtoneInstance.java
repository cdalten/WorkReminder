/*
Copyright 2016 The Android Open Source Project
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.example.cd.workreminder;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

final class CurrrentRingtoneInstance {

    private static Ringtone ringtone; //Added on 10 - 30 - 2019
    private static final String TAG = "LG_MAIN";
    private Context context;
    private ArrayList<Object> arrayList;
    private Object[] array;
    private int size = 0;
    private static CurrrentRingtoneInstance instance;

    private CurrrentRingtoneInstance() {
        arrayList = new ArrayList<Object>(1);
        //array = new Object[2];
    }

    public static CurrrentRingtoneInstance getInstance() {
        if (instance == null) instance = new CurrrentRingtoneInstance();
        return instance;
    }
    public ArrayList<Object> getArrayList() {
        return arrayList;
    }




    public void getRingtone() {
        Log.e(TAG, "THE REMOVED RINGTONE INSTANCE (IN GLOBAL MAIN ACTIVITY) IS: " + arrayList.get(0));
    }
}//end class
