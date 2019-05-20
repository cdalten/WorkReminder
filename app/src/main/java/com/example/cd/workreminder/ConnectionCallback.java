package com.example.cd.workreminder;

//package com.example.cd.shiftreminder;

import android.net.NetworkInfo;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public interface ConnectionCallback<T> {
    public NetworkInfo getActiveNetworkInfo();
    public void finishDownloading();
    public void updateFromDownload(String result);
}
