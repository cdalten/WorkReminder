package com.example.cd.workreminder;

import android.Manifest;
//import android.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.http.HttpResponseCache;
import android.os.Parcelable;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.provider.BaseColumns;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
//import android.provider.AlarmClock;

import android.os.Environment;
import android.os.Handler;
//import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
//import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
//import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cd.workreminder.AlarmTimer;
import com.example.cd.workreminder.ConnectionCallback;
import com.example.cd.workreminder.CurrentWeekSchedule;
import com.example.cd.workreminder.CurrentWorkHours;
import com.example.cd.workreminder.CurrentWorkWeek;
import com.example.cd.workreminder.R;
import com.example.cd.workreminder.WorkAlarmReceiver;
import com.example.cd.workreminder.WorkNetworkFragment;
import com.example.cd.workreminder.WorkReaderContract;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

//import org.apache.http.util.EncodingUtils; //stupid hack. Added on 7 - 1 - 2018

public class MainActivity extends AppCompatActivity implements ConnectionCallback{
//public class MainActivity extends MilitaryTime implements ConnectionCallback {
    private WebView getSchedule;
    public String LANDINGPAGE_URL = "myschedule.safeway.com";
    public static String TEST_URL = "https://sfbay.craigslist.org"; //added on 12 - 6 - 2017 for debugging
    public static String LOGIN_URL = "https://myschedule.safeway.com/ESS/AuthN/SwyLogin.aspx?ReturnUrl=%2fESS";
    protected static final String UA = "Pak N Slave Mobile App; Written by cda@stanford.edu; Uhh...Hi Mom!";

    ConnectionCallback connectionCallback; //added on 10 - 7 - 2018
    //private TabLayout.Tab alarm; //added on 9 - 14 - 2017
    //private TabHost alarm; //added on 9 - 14 - 2017
    //protected Context context; // added on 8 - 22 - 2017
    //public File file; //added on 9 - 21 - 2017. Changed to public on 7 - 31 - 2018
    //protected TabLayout th; //added on 9 - 28 - 2017

    private final String name = "9857701"; //modified on 1 - 8 - 201
    public static boolean refreshDisplay = true; //added on 6 - 14 - 2018

    private final String PRODUCTION_TAG = "LG_WORK_WEB: "; //used for hardware only

    private final boolean debug = true; //added on 7 - 22 - 2018

    private String schedule = ""; //added on 7 - 25 - 2018
    private boolean BECAUSE_INTENTS_ON_DEVACTIVED_PHONES_DONT_FUCKING_WORK = false;

    //private boolean notConnected = false; //modified on 9 - 5- 2018
    private TextView offline; //added on 9 - 8 - 2018
    private String connectionStatus = ""; //added on 9 - 18 - 2018
    public static String CurrentSchedule = "CurrentSchedule"; //added on 9 - 19 - 2018
    public String fileContents = ""; //default is ""
    private SharedPreferences pref; //added on 9 - 21 - 2018
    private boolean doIWorkToday = false; //added on 10 - 17 - 2018
    CurrentWorkHours date; //added on 10 - 22 - 2018

    private boolean pageEnded = false; //Added on 11 - 12 - 2018
    private boolean onClick = false; //Added on 11 - 13 - 2018
    private TableLayout update; //Added on 11 - 15 - 2018
    private Button Update; //Added on 11 - 15 - 2018
    private Button finish;


    private String OfflineMessage; //Added on 11 - 21 - 2018
    private Intent intent; //Added on 11 - 21 - 2018

    private final String WORK_TAG = "WorkAlarm";
    private WorkNetworkFragment mNetworkFragment; //Added on 1 - 2 - 2019

    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean mDownloading = false;
    private static boolean scheduleGotUpdated = false; //Added on 1 - 22 - 2019

    private CurrentWorkWeek sundayWorkHours; //Added on 2 - 17 - 2019
    private CurrentWorkWeek mondayWorkHours;
    private CurrentWorkWeek tuesdayWorkHours;
    private CurrentWorkWeek wednesdayWorkHours;
    private CurrentWorkWeek thursdayWorkHours;
    private CurrentWorkWeek fridayWorkHours;
    private CurrentWorkWeek saturdayWorkHours;

    private SharedPreferences.Editor saveMe; //Added on 4 - 25 - 2019
    private Button workPreferences; //Added on 5 - 9 - 2019

    private BroadcastReceiver WorkAlarmReceiver;

    @SuppressLint("ClickableViewAccessibility")
    @TargetApi(19)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_job_schedule);
        Log.e(PRODUCTION_TAG, "ONCREATE() BEFORE SAVEDINSTANCE()");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.TIME_TICK");

        WorkAlarmReceiver = new WorkAlarmReceiver();
        registerReceiver( WorkAlarmReceiver, filter);

        //offline = (TextView)findViewById(R.id.offline);
        //offlineUpdate = (Button) findViewById(R.id.offlineUpdate);
        //offlineUpdate.setVisibility(View.GONE);

        //mNetworkFragment = WorkNetworkFragment.getInstance(
        //        getSupportFragmentManager(),
        //        "https://" + LANDINGPAGE_URL);

        //WebView getSchedule = new WebView(this);
        //setContentView(getSchedule);

        //readFromDrive(); //need to move to fragment?

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        //    if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {

        WebView.setWebContentsDebuggingEnabled(true); //vs instance of a class??=
        //   }
        //}

        workPreferences = (Button) findViewById(R.id.workPreferences);
        workPreferences.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent workPreferenceIntent = new Intent(MainActivity.this, WorkPreferences.class);
                        startActivity(workPreferenceIntent);
                    }
                }
        );

        //checkDrivePermissions(); //Temporarily disabled on 1 - 16 - 2019

        //Disabled network broadcast on 8 -27 - 2018.
        //IntentFilter filter = new IntentFilter(AlarmManager);
        //IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        //ConnectionStatus connectionStatus = new ConnectionStatus();
        //this.registerReceiver(connectionStatus, filter);

        try {
            File httpCacheDir = new File(this.getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            Log.i(PRODUCTION_TAG, "HTTP response cache installation failed:" + e);
        }

        if (savedInstanceState == null) {
            Log.e(PRODUCTION_TAG, "ONCREATE() WHEN SAVEDINSTANCE() IS NULL");

            //Attempt to invoke interface method 'java.lang.String android.content.SharedPreferences.getString(java.lang.String, java.lang.String)'
            //on a null object reference
            //Log.e(PRODUCTION_TAG, "THE SAVED DAY IS: "+
            //pref.getString("SAVED_DOWNLOAD_DATE", "SUNDAY")
            //);
            //int defaultValue = getResources().getInteger("UPDATE_SCHEDULE");

            getSchedule = (WebView) this.findViewById(R.id.CurrentSchedule);
            getSchedule.setWebViewClient(new WWebViewClient());
            //getSchedule.addJavascriptInterface(new MainActivity.JavaScriptBridge(this), "OFFLINE");
            getSchedule.getSettings().setLoadWithOverviewMode(true);
            getSchedule.getSettings().setUseWideViewPort(true);
            getSchedule.getSettings().setJavaScriptEnabled(true);
            getSchedule.getSettings().setDomStorageEnabled(true);
            getSchedule.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); //added on 9 - 23 - 2018
            CookieManager.getInstance().setAcceptCookie(true);

            //double render????
            mNetworkFragment = WorkNetworkFragment.getInstance(
                    getSupportFragmentManager(),
                    "https://" + LANDINGPAGE_URL);

            //force load???
            getSchedule.loadUrl(LOGIN_URL);
            getSchedule.setVisibility(View.VISIBLE); //disable for debugging.

            getSchedule.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    startDownload();
                    Log.e(PRODUCTION_TAG, "WEBVIEW BUTTON GOT CLICKED");
                    return false;
                }


            });


            intent = new Intent(MainActivity.this, CurrentWeekSchedule.class);

            OfflineMessage = "<html>" +
                    "<font size =\"32\"><b>THIS PAGE CANNOT BE LOADED BECAUSE YOUR CELL PHONE CARRIER" +
                    "IS TOO BUSY SPYING ON YOU. MAYBE YOU SHOULD GO SLEEP WITH YOUR WIFE AND THEN TRY" +
                    " AGAIN IN A FEW HOURS? </b> </font>" +
                    "<p> </p>"+
                    "</html>";
            //readFromInternalDirectory(new File(CurrentSchedule + ThisWeek));
            //date = new CurrentWorkHours();

            pref = getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);

            //setAlarmTime(9, 10, Integer.parseInt(pref.getString("ALARM_MINUTES", "")));
            intent.setAction(Intent.ACTION_SEND);

            SharedPreferences.Editor editor = pref.edit();



            /*
             * Need to merge second intent with the first one after I debug.
             */
            //sundayWorkHours =  new CurrentWorkWeek( pref,
            //        this,
                    //pref.getString(getString(R.string.SUNDAY),"SUNDAY"), //Default is "OFF"
                    //pref.getString(getString(R.string.SUNDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT),
                    //pref.getString(getString(R.string.SUNDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT),
                    //pref.getString(getString(R.string.SUNDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT),
                    //pref.getString(getString(R.string.SUNDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT),
                    //pref.getString(getString(R.string.SUNDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT),
                    //pref.getString(getString(R.string.SUNDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));

            editor.putString(getString(R.string.SUNDAY), pref.getString(getString(R.string.SUNDAY),"SUNDAY"));
            editor.putString(getString(R.string.SUNDAY_START_HOUR),
                    pref.getString(getString(R.string.SUNDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
            editor.putString(getString(R.string.SUNDAY_START_MINUTE),
                    pref.getString(getString(R.string.SUNDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
            editor.putString(getString(R.string.SUNDAY_START_AM_OR_PM),
                    pref.getString(getString(R.string.SUNDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
            editor.putString(getString(R.string.SUNDAY_END_HOUR),
                    pref.getString(getString(R.string.SUNDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
            editor.putString(getString(R.string.SUNDAY_END_MINUTE),
                    pref.getString(getString(R.string.SUNDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
            editor.putString(getString(R.string.SUNDAY_END_AM_OR_PM),
                    pref.getString(getString(R.string.SUNDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));
            //intent.putExtra("SundayHours", sundayWorkHours);

            //intent.getParcelableExtra("SundayHours");

            //intent.putExtra(getString(R.string.com_example_cd_shiftreminder_SUNDAY),
            //
            //      sundayWorkHours.getWorkHours(this));
            //intent.setType("text/plain");

            //mondayWorkHours = new CurrentWorkWeek("OFF");
            //String workToday = mondayWorkHours.getDayOfWeek();
           // if (!workToday.equals("OFF")) {
                /*mondayWorkHours = new CurrentWorkWeek(pref,
                        this,
                        pref.getString(getString(R.string.MONDAY), "MONDAY"),
                        pref.getString(getString(R.string.MONDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT),
                        pref.getString(getString(R.string.MONDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT),
                        pref.getString(getString(R.string.MONDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT),
                        pref.getString(getString(R.string.MONDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT),
                        pref.getString(getString(R.string.MONDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT),
                        pref.getString(getString(R.string.MONDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT)
                );
                */
            editor.putString(getString(R.string.MONDAY), pref.getString(getString(R.string.MONDAY), "MONDAY"));
            editor.putString(getString(R.string.MONDAY_START_HOUR), pref.getString(getString(R.string.MONDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
            editor.putString(getString(R.string.MONDAY_START_MINUTE), pref.getString(getString(R.string.MONDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
            editor.putString(getString(R.string.MONDAY_START_AM_OR_PM),  pref.getString(getString(R.string.MONDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
            editor.putString(getString(R.string.MONDAY_END_HOUR),  pref.getString(getString(R.string.MONDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
            editor.putString(getString(R.string.MONDAY_END_MINUTE), pref.getString(getString(R.string.MONDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
            editor.putString(getString(R.string.MONDAY_END_AM_OR_PM),pref.getString(getString(R.string.MONDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));
            //intent.putExtra("MondayHours", mondayWorkHours);
            //} else {
            //    mondayWorkHours = new CurrentWorkWeek();
            //    intent.putExtra("MondayHours", mondayWorkHours);
           // }


            //tuesdayWorkHours = new CurrentWorkWeek("OFF");
            //String workToday = tuesdayWorkHours.getDayOfWeek();
            //if (!workToday.equals("OFF")) {
                //tuesdayWorkHours = new CurrentWorkWeek(pref,
                //        this,
                //        ,
                       //,
                       //,
                       // ,
                       // ,
                      // ,

                //);
            //} else {
            //        tuesdayWorkHours = new CurrentWorkWeek();
            //        intent.putExtra("TuesdayHours", tuesdayWorkHours);
            //}
            //intent.putExtra(getString(R.string.com_example_cd_shiftreminder_TUESDAY), tuesdayWorkHours.getCurrentWorkHours());
            editor.putString(getString(R.string.TUESDAY), pref.getString(getString(R.string.TUESDAY), "TUESDAY") );
            editor.putString("TUESDAY_START_HOUR",
                    pref.getString(getString(R.string.TUESDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
            editor.putString("TUESDAY_START_MINUTE",
                    pref.getString(getString(R.string.TUESDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
            editor.putString("TUESDAY_START_AM_OR_PM",
                    pref.getString(getString(R.string.TUESDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
            editor.putString("TUESDAY_END_HOUR",
                    pref.getString(getString(R.string.TUESDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
            editor.putString("TUESDAY_END_MINUTE",
                    pref.getString(getString(R.string.TUESDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
            editor.putString("TUESDAY_END_AM_OR_PM",
                    pref.getString(getString(R.string.TUESDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));
            //intent.putExtra("TuesdayHours", tuesdayWorkHours);


            //wednesdayWorkHours = new CurrentWorkWeek(pref,
            //        this,
            //        ,
            //        ,
                    //,
                   //,
                    //,
                   //,

            //);


            editor.putString(getString(R.string.WEDNESDAY),
                    pref.getString(getString(R.string.WEDNESDAY), "WEDNESDAY") );
            editor.putString(getString(R.string.
                    WEDNESDAY_START_HOUR), pref.getString(getString(R.string.WEDNESDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
            editor.putString(getString(R.string.WEDNESDAY_START_MINUTE),
                    pref.getString(getString(R.string.WEDNESDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
            editor.putString(getString(R.string.WEDNESDAY_START_AM_OR_PM),
                    pref.getString(getString(R.string.WEDNESDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT) );
            editor.putString(getString(R.string.WEDNESDAY_END_HOUR),
                    pref.getString(getString(R.string.WEDNESDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
            editor.putString(getString(R.string.WEDNESDAY_END_MINUTE),
                    pref.getString(getString(R.string.WEDNESDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
            editor.putString(getString(R.string.WEDNESDAY_END_AM_OR_PM),
                    pref.getString(getString(R.string.WEDNESDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));

            //intent.putExtra("WednesdayHours", wednesdayWorkHours);

            //thursdayWorkHours =  new CurrentWorkWeek( pref,
            //        this,
                   //,
                   //,
                   // ,
                   // ,
                   //,
                   // ,
                   // );

            editor.putString(getString(R.string.THURSDAY),
                    pref.getString(getString(R.string.THURSDAY), "THURSDAY"));
            editor.putString(getString(R.string.THURSDAY_START_HOUR),
                    pref.getString(getString(R.string.THURSDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
            editor.putString(getString(R.string.THURSDAY_START_MINUTE),
                    pref.getString(getString(R.string.THURSDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
            editor.putString(getString(R.string.THURSDAY_START_AM_OR_PM),
                    pref.getString(getString(R.string.THURSDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
            editor.putString(getString(R.string.THURSDAY_END_HOUR),
                    pref.getString(getString(R.string.THURSDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT) );
            editor.putString(getString(R.string.THURSDAY_END_MINUTE),
                    pref.getString(getString(R.string.THURSDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
            editor.putString(getString(R.string.THURSDAY_END_AM_OR_PM),
                    pref.getString(getString(R.string.THURSDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));

            //intent.putExtra("ThursdayHours", thursdayWorkHours);

            //12AM represents midnight on my phone

                //fridayWorkHours = new CurrentWorkWeek(pref,
                //        this,
                //        ,
                        //,
                        //,
                        //,
                        //,
                       //,

                //);


            editor.putString(getString(R.string.FRIDAY),
                    pref.getString(getString(R.string.FRIDAY), "FRIDAY"));
            editor.putString(getString(R.string.FRIDAY_START_HOUR),
                    pref.getString(getString(R.string.FRIDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
            editor.putString(getString(R.string.FRIDAY_START_MINUTE),
                    pref.getString(getString(R.string.FRIDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
            editor.putString(getString(R.string.FRIDAY_START_AM_OR_PM),
                    pref.getString(getString(R.string.FRIDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
            editor.putString(getString(R.string.FRIDAY_END_HOUR),
                    pref.getString(getString(R.string.FRIDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
            editor.putString(getString(R.string.FRIDAY_END_MINUTE),
                    pref.getString(getString(R.string.FRIDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
            editor.putString(getString(R.string.FRIDAY_END_AM_OR_PM),
                    pref.getString(getString(R.string.FRIDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));

            //intent.putExtra("FridayHours", fridayWorkHours);

            //saturdayWorkHours = new CurrentWorkWeek( pref,
            //        this,
                   //,
                    //,
                    //,
                    //,
                    //,
                    //,

            //);


            editor.putString(getString(R.string.SATURDAY),
                    pref.getString(getString(R.string.SATURDAY ), "SATURDAY"));
            editor.putString(getString(R.string.SATURDAY_START_HOUR),
                    pref.getString(getString(R.string.SATURDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
            editor.putString(getString(R.string.SATURDAY_START_MINUTE),
                    pref.getString(getString(R.string.SATURDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
            editor.putString(getString(R.string.SATURDAY_START_AM_OR_PM),
                    pref.getString(getString(R.string.SATURDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
            editor.putString(getString(R.string.SATURDAY_END_HOUR),
                    pref.getString(getString(R.string.SATURDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
            editor.putString(getString(R.string.SATURDAY_END_MINUTE),
                    pref.getString(getString(R.string.SATURDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
            editor.putString(getString(R.string.SATURDAY_END_AM_OR_PM),
                    pref.getString(getString(R.string.SATURDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));
            //intent.putExtra("SaturdayHours", saturdayWorkHours);

            doIWorkToday();

            editor.apply();


            //CALL LOADURL????
            //getSchedule.loadUrl("https://" + LANDINGPAGE_URL);


        } else {
            Log.e(PRODUCTION_TAG, "ONCREATE() WHEN SAVEDINSTANCE() IS NOT NULL");
            savedInstanceState.getString("UPDATED_SCHEDULE"); //???

            intent = new Intent(MainActivity.this, CurrentWeekSchedule.class);

            pref = getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE); //redudant??
            //readFromInternalDirectory(new File(CurrentSchedule + ThisWeek));

            //registerReceiver(
            //        new ConnectionStatus(),
            //        new IntentFilter(
            //                ConnectivityManager.CONNECTIVITY_ACTION));

            getSchedule = (WebView) this.findViewById(R.id.CurrentSchedule);
            getSchedule.setWebViewClient(new WWebViewClient());
            //getSchedule.addJavascriptInterface(new JavaScriptBridge(this), "OFFLINE");

            getSchedule.setVerticalScrollBarEnabled(true);
            getSchedule.setHorizontalScrollBarEnabled(true); //for landscape??
            getSchedule.getSettings().setLoadWithOverviewMode(true);
            getSchedule.getSettings().setUseWideViewPort(true);
            getSchedule.getSettings().setJavaScriptEnabled(true);
            getSchedule.getSettings().setDomStorageEnabled(true);
            getSchedule.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); //added on 9 - 23 - 2018
            CookieManager.getInstance().setAcceptCookie(true);

            //double render????
            mNetworkFragment = WorkNetworkFragment.getInstance(
                    getSupportFragmentManager(),
                    "https://" + LANDINGPAGE_URL);

            //force load???
            getSchedule.loadUrl(LOGIN_URL);
            getSchedule.setVisibility(View.VISIBLE);

            getSchedule.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    startDownload();
                    Log.e(PRODUCTION_TAG, "WEBVIEW BUTTON GOT CLICKED");
                    return false;
                }


            });

        }


        //getSchedule.getSettings().setDatabaseEnabled(true); //added on 10 - 5- 2017

        /*getSchedule.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("work", consoleMessage.message() + " --- From line"
                + consoleMessage.lineNumber() + " of" +
                consoleMessage.sourceId());
                return true;
            }
        });
        */

    }


    @Override
    public void updateFromDownload(String result) {
        if (result != null) {
            Log.e(PRODUCTION_TAG, "THE DOWNLOADED FRAGMENT IS: --->: " + result);
            //getSchedule.loadUrl("https://www.google.com");
            /* getSchedule.loadDataWithBaseURL("https://" + LANDINGPAGE_URL + "/?",
                result,
                null,
                "uft-8",
                null);
            */

        } else {
            Log.e(PRODUCTION_TAG, "GOT DATA FROM ASYNCTASK ERROR"); //Force up the stack??
        }
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;

    }

    @Override
    public void finishDownloading() {
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();

        }

    }
    //Added on 1 - 2 - 2019
    private void startDownload() {
        if (!mDownloading && mNetworkFragment != null) {
            // Execute the async download.
            mNetworkFragment.startDownload();
            mDownloading = true;
        }
    }




    //Added on 10 - 15 - 2018
    private void doIWorkToday() {

        MilitaryTime militaryTime = MilitaryTime.getInstance();
        AlarmTimer alarmTimer = new AlarmTimer();
        if (Build.VERSION.SDK_INT >= 24) {
            Calendar cal = Calendar.getInstance();
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                //intent.putExtra(Days.CURRENT_DAY, Days.DAY_SUNDAY);
                militaryTime.convertCivilanTimeToMilitaryTime(pref.getString(getString(R.string.SUNDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT),
                        pref.getString(getString(R.string.SUNDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT),
                        pref.getString(getString(R.string.SUNDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
                doIWorkToday = militaryTime.getStartingHour(militaryTime.getStartMilitaryHour() + "",
                        militaryTime.getStartMilitaryMinute() + "",
                        pref.getString(getString(R.string.SUNDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
                //cal.set(DAY_THURSDAY, Calendar.THURSDAY);
                //AlarmTimer.setAlarmTime(this, thursdayWorkHours.getStartMilitaryHour(),
                //        thursdayWorkHours.getStartMilitaryMinute(),
                //        Integer.parseInt(pref.getString("ALARM_MINUTES", "")));
                if (doIWorkToday == true) {
                    alarmTimer.setAlarmTime(this, militaryTime.getStartMilitaryHour(), militaryTime.getStartMilitaryMinute(),
                            Integer.parseInt(pref.getString("ALARM_MINUTES", WorkReaderContract.WorkEntry.ALARM_DEFAULT)));
                    //intent.putExtra(getString(R.string.com_example_cd_shiftreminder_I_WORK_TODAY), thursdayWorkHours.toString());
                    intent.putExtra(getString(R.string.I_WORK_TODAY),
                            pref.getString(getString(R.string.SUNDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT) + ":" +
                                    pref.getString(getString(R.string.SUNDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT
                                            + pref.getString(getString(R.string.SUNDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT)));
                }
            }

            else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                militaryTime.convertCivilanTimeToMilitaryTime(pref.getString(getString(R.string.MONDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT),
                        pref.getString(getString(R.string.MONDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT),
                        pref.getString(getString(R.string.MONDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
                doIWorkToday = militaryTime.getStartingHour(militaryTime.getStartMilitaryHour() + "",
                        militaryTime.getStartMilitaryMinute() + "",
                        pref.getString(getString(R.string.MONDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
                //cal.set(DAY_THURSDAY, Calendar.THURSDAY);
                //AlarmTimer.setAlarmTime(this, thursdayWorkHours.getStartMilitaryHour(),
                //        thursdayWorkHours.getStartMilitaryMinute(),
                //        Integer.parseInt(pref.getString("ALARM_MINUTES", "")));
                if (doIWorkToday == true) {
                    alarmTimer.setAlarmTime(this, militaryTime.getStartMilitaryHour(), militaryTime.getStartMilitaryMinute(),
                            Integer.parseInt(pref.getString("ALARM_MINUTES", WorkReaderContract.WorkEntry.ALARM_DEFAULT)));
                    //intent.putExtra(getString(R.string.com_example_cd_shiftreminder_I_WORK_TODAY), thursdayWorkHours.toString());
                    intent.putExtra(getString(R.string.I_WORK_TODAY),
                            pref.getString(getString(R.string.MONDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT) + ":" +
                                    pref.getString(getString(R.string.MONDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT
                                    + pref.getString(getString(R.string.MONDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT)));
                }
            }

            else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                militaryTime.convertCivilanTimeToMilitaryTime(pref.getString(getString(R.string.TUESDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT),
                        pref.getString(getString(R.string.TUESDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT),
                        pref.getString(getString(R.string.TUESDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
                if (militaryTime.getStartingHour(militaryTime.getStartMilitaryHour() + "",
                        militaryTime.getStartMilitaryMinute() + "",
                        pref.getString(getString(R.string.TUESDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT))) //doIWorkToday = true;
                if (doIWorkToday == true) {
                    alarmTimer.setAlarmTime(this, militaryTime.getStartMilitaryHour(), militaryTime.getStartMilitaryMinute(),
                            Integer.parseInt(pref.getString("ALARM_MINUTES", WorkReaderContract.WorkEntry.ALARM_DEFAULT)));
                    intent.putExtra(getString(R.string.I_WORK_TODAY),
                            pref.getString(getString(R.string.TUESDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT) + ":" +
                                    pref.getString(getString(R.string.TUESDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT
                                            + pref.getString(getString(R.string.TUESDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT)));
                }
            }

            else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                militaryTime.convertCivilanTimeToMilitaryTime(pref.getString(getString(R.string.WEDNESDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT),
                        pref.getString(getString(R.string.WEDNESDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT),
                        pref.getString(getString(R.string.WEDNESDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
                doIWorkToday = militaryTime.getStartingHour(militaryTime.getStartMilitaryHour() + "",
                        militaryTime.getStartMilitaryMinute() + "",
                        pref.getString(getString(R.string.WEDNESDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
                //cal.set(DAY_THURSDAY, Calendar.THURSDAY);
                //AlarmTimer.setAlarmTime(this, thursdayWorkHours.getStartMilitaryHour(),
                //        thursdayWorkHours.getStartMilitaryMinute(),
                //        Integer.parseInt(pref.getString("ALARM_MINUTES", "")));
                if (doIWorkToday == true) {
                    alarmTimer.setAlarmTime(this, militaryTime.getStartMilitaryHour(), militaryTime.getStartMilitaryMinute(),
                            Integer.parseInt(pref.getString("ALARM_MINUTES", WorkReaderContract.WorkEntry.ALARM_DEFAULT)));
                    intent.putExtra(getString(R.string.I_WORK_TODAY),
                            pref.getString(getString(R.string.WEDNESDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT) + ":" +
                                    pref.getString(getString(R.string.WEDNESDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT
                                            + pref.getString(getString(R.string.WEDNESDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT)));
                }
            }


            else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                militaryTime.convertCivilanTimeToMilitaryTime(pref.getString(getString(R.string.THURSDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT),
                        pref.getString(getString(R.string.THURSDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT),
                        pref.getString(getString(R.string.THURSDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
                doIWorkToday = militaryTime.getStartingHour(militaryTime.getStartMilitaryHour() + "",
                        militaryTime.getStartMilitaryMinute() + "",
                        pref.getString(getString(R.string.THURSDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
                if (doIWorkToday == true) {
                    alarmTimer.setAlarmTime(this, militaryTime.getStartMilitaryHour(), militaryTime.getStartMilitaryMinute(),
                            Integer.parseInt(pref.getString("ALARM_MINUTES", WorkReaderContract.WorkEntry.ALARM_DEFAULT)));
                    intent.putExtra(getString(R.string.I_WORK_TODAY),
                            pref.getString(getString(R.string.THURSDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT) + ":" +
                                    pref.getString(getString(R.string.THURSDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT
                                            + pref.getString(getString(R.string.THURSDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT)));
                }
            }


            else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                militaryTime.convertCivilanTimeToMilitaryTime(pref.getString(getString(R.string.FRIDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT),
                        pref.getString(getString(R.string.FRIDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT),
                        pref.getString(getString(R.string.FRIDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
                doIWorkToday = militaryTime.getStartingHour(militaryTime.getStartMilitaryHour() + "",
                        militaryTime.getStartMilitaryMinute() + "",
                        pref.getString(getString(R.string.FRIDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
                //AlarmTimer.setAlarmTime(this, thursdayWorkHours.getStartMilitaryHour(),
                //        thursdayWorkHours.getStartMilitaryMinute(),
                //        Integer.parseInt(pref.getString("ALARM_MINUTES", "")));
                if (doIWorkToday == true) {
                    alarmTimer.setAlarmTime(this,militaryTime.getStartMilitaryHour(), militaryTime.getStartMilitaryMinute(),
                            Integer.parseInt(pref.getString("ALARM_MINUTES", WorkReaderContract.WorkEntry.ALARM_DEFAULT)));
                    intent.putExtra(getString(R.string.I_WORK_TODAY),
                            pref.getString(getString(R.string.FRIDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT) + ":" +
                                    pref.getString(getString(R.string.FRIDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT
                                            + pref.getString(getString(R.string.FRIDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT)));
                }
            }

            else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                militaryTime.convertCivilanTimeToMilitaryTime(pref.getString(getString(R.string.SATURDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT),
                        pref.getString(getString(R.string.SATURDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT),
                        pref.getString(getString(R.string.SATURDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
                doIWorkToday = militaryTime.getStartingHour(militaryTime.getStartMilitaryHour() + "",
                        militaryTime.getStartMilitaryMinute() + "",
                        pref.getString(getString(R.string.SATURDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
                if (doIWorkToday == true) {
                    alarmTimer.setAlarmTime(this, militaryTime.getStartMilitaryHour(), militaryTime.getStartMilitaryMinute(),
                            Integer.parseInt(pref.getString("ALARM_MINUTES", WorkReaderContract.WorkEntry.ALARM_DEFAULT)));
                    //intent.putExtra(getString(R.string.com_example_cd_shiftreminder_I_WORK_TODAY), thursdayWorkHours.toString());
                    intent.putExtra(getString(R.string.I_WORK_TODAY),
                            pref.getString(getString(R.string.SATURDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT) + ":" +
                                    pref.getString(getString(R.string.SATURDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT
                                            + pref.getString(getString(R.string.SATURDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT)));
                }
            }
        }//end untested if

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(PRODUCTION_TAG, "ONCONFIGURATIONCHAGE() GOT CALLED");

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }

        /*if(newConfig.orientation== Configuration.ORIENTATION_LANDSCAPE){
            Log.e(PRODUCTION_TAG,"LANDSCAPE");
        }
        else{
            Log.e(PRODUCTION_TAG,"PORTRAIT");
        }
        */


    }



    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Log.e(PRODUCTION_TAG, "ONRESTOREINSTANCE() GOT CALLED WITH " + savedInstanceState.getString(Days.SCHEDULE_GOT_UPDATED));
        //savedInstanceState.get("URL");
        //getSchedule.restoreState(savedInstanceState);
    }

    //added on 8 - 9 - 2018.
    //checkStoragePermissions connections to checkPermissions. Uhh...I do it to debug shit.
    public void checkStoragePermissions() {
        //if (mainActivityFragment != null) {
        //mainActivityFragment.checkPermissions();
        //}
    }


    public void onStart() {
        super.onStart();
    }//end onStart()



    //Keep when I attempt to get the current schedule on Friday Afternoon

    /*
     *Converts directory to a FILE.
     */
    public File getPublicScheduleStorageFile(String thisWeeksSchedule) {
        File file = new File(Environment.getExternalStorageDirectory(), thisWeeksSchedule);

        if(!file.mkdirs()) {
            Log.e(PRODUCTION_TAG, "Directory not created");
        }
        return file;
    }



    //Added on 11 - 10 - 2018
    private void saveCurrentSchedule(String currentSchedule, int week) {
        File file = new File(getFilesDir(), currentSchedule + "" + week);
        if (file.exists()) {
            writeToInternalDirectory(file);
        }
    }

    /*
     *Added on 7 - 24 - 2018
     */
    private File writeToInternalDirectory(File file) {
        FileOutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;

        Log.i(PRODUCTION_TAG, "Application context is " + getApplicationContext());
        Log.i(PRODUCTION_TAG, "The base context is " + getBaseContext());

        //Need to change to fileProvider to make world readable??

        try {
            //added getApplicationContext() on 9 - 20 - 2018
            outputStream = openFileOutput(file.getName(), Context.MODE_PRIVATE);
            //outputStream.write(UpdatedSchedule.getBytes());
            outputStream.close();
            //outputStream.write(htmlString.getBytes()); //commented out on 8 - 23 for debugging only
            Log.i(PRODUCTION_TAG, "Writing to internal directory");

            if (!isExternalStorageReadable()) {
                Log.e(PRODUCTION_TAG, "Can't read from drive");
            }

            readFromInternalDirectory(file);
            outputStream.close();
            //bufferedWriter.close();
            //outputStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace(); //vs log or pass??
        }


        //File file = getPublicScheduleStorageDir(ThisWeeksScrewJob);
        //schedule = file.toString(); //don't askf
        //try {
        //    schedule = readCurrentSchedule(file);
        //} catch (Exception e) {
        //    Log.e(PRODUCTION_TAG, "---BEGIN TTY WRITE DUMP ERROR---"); //doesn't show context reference???
        //    e.printStackTrace(); //added on 7 - 31 - 2018
        //}
        return file;
    }

    //Added on 11 - 13 - 2018. Use this file for getschedule.loadurl("file://")
    //Pass file as field?
    private String readFromInternalDirectory(File file) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (Exception e) {
            //pass
        }
        String line;
        String text = "";

        try {
            while ((line = br.readLine()) != null) {
                text = text + line;
                Log.i(PRODUCTION_TAG, text + "\n");
                text = text + "\n";
            }
        } catch (Exception e) {
            //pass
        }

        return text;
    }


    @Override
    protected void onPause() {
        super.onPause();

    }


    //Crashes when I press the back key. 8 - 13 - 2018
    @Override
    protected void onStop() {
        super.onStop();

        HttpResponseCache cache = HttpResponseCache.getInstalled();

        if (cache != null) {
            cache.flush();
        }//end if
        //    Log.i(PRODUCTION_TAG, "ON STOP");
        //    Log.i(PRODUCTION_TAG, "                           ");
        //    Log.i(PRODUCTION_TAG, "                           ");
        //super.onStop(); //keep commented out
    }

    @Override
    /*
     * Attempt to suppress "Failed to locate a binder for interface: autofill::mojom::PasswordManagerDriver"
     */
    protected void onResume() {
        super.onResume(); //stupid hack;

        Log.i(PRODUCTION_TAG, "ON RESUME ");
    }


    /*@Override
    protected void onRestart() {
        super.onRestart();
        Log.i(PRODUCTION_TAG, "ON RESTART");
    }
    */

    //Kill the http connection and flush the cache.
    @Override
    protected void onDestroy() {
        //super.onDestroy();

        //cancel()
        //    if (connectionStatus != null) {
        //        this.unregisterReceiver(connectionStatus);
        //    }
        unregisterReceiver(WorkAlarmReceiver);
        super.onDestroy(); //why?
    }



    /*
     *Added on 8 - 16 - 2018. Checks for connection when app loads up for the first time
     *
     */
    private String downloadLoginPage(URL url) throws IOException {
        InputStream inputStream = null;
        HttpsURLConnection connection = null;
        String result = null;
        boolean redirect = false; //added on 9 - 25 - 2018
        int code = 0; //added on 12 - 8 - 2018
        try {
            connection = (HttpsURLConnection) url.openConnection();
            //HttpsURLConnection.setFollowRedirects(false);
            //connection.setInstanceFollowRedirects(true);
            //connection.setReadTimeout(9000); //3000ms
            //connection.setConnectTimeout(9000); //3000ms
            connection.setRequestMethod("GET");

            if (CookieManager.getInstance().getCookie(connection.getURL().toString()) != null) {
                //String arr[] = CookieManager.getInstance().getCookie(connection.getURL().toString()).split(";");
                //connection.setRequestProperty("Cookie", arr[1]);
                //Log.i(PRODUCTION_TAG, "The client side cookie is: " + arr[1]);
            }

            connection.setDoInput(true);

            connection.setChunkedStreamingMode(0); //added on 9 - 12 - 2018
            connection.connect();
            int responsecode = connection.getResponseCode();
            if (responsecode != HttpsURLConnection.HTTP_OK) {
                Log.e(PRODUCTION_TAG, "POSSIBLE 404? in downloadLogin()");

                //connection.disconnect();
                //redirect = true;
                //return null;
                throw new IOException("Http error code" + responsecode); //disabled in an attempt to debug
            }
            if ( responsecode == HttpsURLConnection.HTTP_MOVED_TEMP  || responsecode == HttpsURLConnection.HTTP_MOVED_PERM) {

                Log.e(PRODUCTION_TAG, "REDIRECT DETECTED");
                //redirect = true;
                //Log.e(PRODUCTION_TAG, "URL REDIRECTION DETECTED");
                //throw new IOException(" Http error code" + responsecode);
                //return null;
                throw new IOException("Redirect response code" + responsecode); //added on 10 - 2 - 2018
            }




            inputStream = connection.getInputStream();
            if (inputStream != null) {
                result = readStream(inputStream, 64000); //Lame way to emulate ping.
            }
        }

        /*catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(PRODUCTION_TAG, "ERROR "+ e);
        } catch (ProtocolException e) {
            e.printStackTrace();
            Log.e(PRODUCTION_TAG, "PROTOCOL ERROR " + e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(PRODUCTION_TAG, "IO ERROR " + e);
        }
        */
        finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.disconnect(); //why no else if??
            }
        }

        return result;
    }

    //added on 8 - 16 - 2018.
    private String readStream(InputStream stream, int offset) throws IOException{
        InputStreamReader inputStreamReader = new InputStreamReader(stream, "UTF-8");

        String result = null; //change from empty to null??! 10 - 4 - 2018
        int maxLength = offset; //don't ask
        //int maxLength = 100; //modified on 10 - 7 - 2018
        char [] buffer = new char[maxLength];
        int numChars = 0;
        int readSize = 0;

        //START REVISED CODE
        /*int c;
        String output = null;
        InputStreamReader unbufferedStream = new InputStreamReader(stream, "UTF-8");

        while ((c = unbufferedStream.read()) != - 1) {
            char character = (char) c;
            output = output + character ;
        }
        Log.i(PRODUCTION_TAG, "UNBUFFERED STREADM IS: " + output); //copy output to new String
        */
        //END REVISED CODE


        //Possibly reading too large data stream. Hence why the insufficient resource error?
        //Log.d(PRODUCTION_TAG, "STARTED TO READ STREAM"); //added on 10 - 4- 2018
        while (numChars < maxLength && readSize != -1) {
            numChars += readSize;;
            inputStreamReader.read();
            readSize = inputStreamReader.read(buffer, numChars, buffer.length - numChars);
        }
        //Log.d(PRODUCTION_TAG, "FINISHED READING STREAM"); //added on 10 - 4- 2018


        if (numChars != -1 ) {
            //Log.e(PRODUCTION_TAG, "INITIAL HTTP CONNECTION? " + numChars);
            numChars = Math.min(numChars, maxLength);
            result = new String(buffer, 0, numChars);
        }

        //result = new String(output);
        return result;
        //return output; //close stream??
    }


    class WWebViewClient extends WebViewClient {


        //Added on 12 - 13 - 2018.
        @Override
        public String toString() {
            Log.e(PRODUCTION_TAG, "TOSTRING IMAGE GOT CLICKED ON");
            return "IMAGE";
        }

        /*@Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Log.i("RESOURCE VIEW URL", view.getUrl());
            //Log.i("RESOURCE VIEW ORIG URL", view.getOriginalUrl());
            Log.i("RESORUCE HOST", Uri.parse(url).getHost());
            Log.i("RESOURCE PATH", Uri.parse(url).getPath());
        }
        */

        //log path
        @TargetApi(21)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request){
            //Log.e(PRODUCTION_TAG, "STARTED DOWNLOAD IN MAINACTIVITY()"); //don't ask

            Log.d(PRODUCTION_TAG, " ");
            Log.d(PRODUCTION_TAG, "URI AUTHORITY " + request.getUrl().getAuthority() + "");
            Log.d(PRODUCTION_TAG, "URI ENCODING " + request.getUrl().getEncodedPath() + "");
            Log.d(PRODUCTION_TAG, "URI ENCODE USER INFO " + request.getUrl().getEncodedUserInfo() + "");
            Log.d(PRODUCTION_TAG, "URI ENCODE AUTH " + request.getUrl().getEncodedAuthority() + "");
            Log.d(PRODUCTION_TAG, "URI USER INFO " + request.getUrl().getUserInfo() + "");
            Log.d(PRODUCTION_TAG, "URI ENCODE QUERY " + request.getUrl().getEncodedQuery() + "");
            Log.d(PRODUCTION_TAG, "URI SCHEME SPECIFIC " + request.getUrl().getSchemeSpecificPart());
            Log.d(PRODUCTION_TAG, "HOST " + request.getUrl().getHost());
            Log.d(PRODUCTION_TAG, "URI PATH " + request.getUrl().getPath());
            Log.d(PRODUCTION_TAG, "REQUEST HEADERS " + request.getRequestHeaders());
            Log.d(PRODUCTION_TAG, "METHOD TYPE " + request.getMethod());
            Log.d(PRODUCTION_TAG, "++++++++++++++++++++++++++++++++++++");
            Log.d(PRODUCTION_TAG, " ");

            try {
                if (request.getRequestHeaders().get("Referer").equals(LOGIN_URL)) {
                    pageEnded = true;
                    //startActivity(intent); //stops multiple flickering
                    Log.e(PRODUCTION_TAG, "STARTED SCHEDULE INTENT");

                }
            } catch (Exception e) {
                //pass
            }


            try {
                String result = downloadLoginPage(new URL(request.getUrl().toString()));
                //Log.i(PRODUCTION_TAG, "THE RESULT IS: " + result);
                if (result == null) {
                    Log.e(PRODUCTION_TAG, "INTENT STARTED ON NULL HTTP RESPONSE");
                    //return new WebResourceResponse("text/html",
                    //        "UTF-8",
                    //        new ByteArrayInputStream(OfflineMessage.getBytes("UTF8")));
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e(PRODUCTION_TAG, "ERROR " + e);
            } catch (ProtocolException e) {
                e.printStackTrace();
                Log.e(PRODUCTION_TAG, "PROTOCOL ERROR " + e);
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                Log.e(PRODUCTION_TAG, "NO DATA ERROR " + e);
                //startActivity(intent); //Added on 1 - 13 - 2019
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(PRODUCTION_TAG, "IO ERROR " + e);
            }

            /*if (request.getUrl().toString().contains("LaborSelect.js")
                        || request.getUrl().toString().contains("GenericTree.js")
                        || request.getUrl().toString().contains("jquery-custom-libs.js")
                        || request.getUrl().toString().contains("LoadTree.js")
                        ) {
               Log.e(PRODUCTION_TAG, "FOUND JS SCRIPTS");
                try {
                    return new WebResourceResponse("text/javascript", "UTF-8", getAssets().open("index.html"));
                } catch (Exception e) {
                        //pass
                }
            } */ //end if

            Log.i(PRODUCTION_TAG, "----------------------------------------------------");


            return super.shouldInterceptRequest(view, request);
        }

        /*
         *Possibly add menu visibility here? This is because this appears to be the the only webview
         * entry point when I click on the login button.
         */
        //@Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            //produces infinite get requests when offline.
            Log.i(PRODUCTION_TAG, "The url is: " + url);
            Log.i(PRODUCTION_TAG, "The viewed page is: " + view.getUrl());
            Log.i(PRODUCTION_TAG, "ON PAGE STARTED (DEBUG MODE)");

        }

        @TargetApi(24)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            //super.shouldOverrideUrlLoading(view, request);
            Log.i(PRODUCTION_TAG, "URL DIDN'T GET CLICKED ON.");
            final Uri uri = request.getUrl();
            final String host = uri.getHost();

            //Need to change request.getUrl() to host
            //if (request.getUrl().equals("https://myschedule.safeway.com/ESS/Schedule.aspx")) {
            //    Log.e(PRODUCTION_TAG, "UPDATED SCHEDULE GOT CALLED");
            //    startActivity(intent);
            // }


            //Need to manually set scheduleGotUpdated flag to false on every other day.
            //Check for bad connection before resetting flag?
            if (host.equals("myschedule.safeway.com")) {
                Log.i(PRODUCTION_TAG, "DID THE LINK GET CLICKED? " + onClick);
                Log.i(PRODUCTION_TAG, "CLICKED VIEW URL IS: " + view.getUrl());
                Log.i(PRODUCTION_TAG, "REQUESTED URL IS: " + request.getUrl());

                //pref.getInt(getString(R.string.com_example_cd_shiftreminder_SAVED_DOWNLOAD_DATE),00);

                Calendar cal = Calendar.getInstance();
                    //if (cal.get(Calendar.DAY_OF_WEEK) != pref.getInt(getString(R.string.com_example_cd_shiftreminder_SAVED_DOWNLOAD_DATE),00)
                    //        && pageEnded == true) {
                Log.e(PRODUCTION_TAG, "SCHEDULE HAS BEEN UPDATED");
                    //getSchedule.setVisibility(View.VISIBLE);

                //startActivity(intent); //caused multiple flickering
                    //} //end inner if.

                //getSchedule.setVisibility(View.INVISIBLE); //suppress server css/html/javascript

                if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    getSchedule.setVisibility(View.INVISIBLE);
                    /*getSchedule.loadDataWithBaseURL("myschedule.safeway.com",
                            "<html><font size =\"14\"><b>" +
                                    "DOWNLOADING NEW SCHEDULE" +
                                    "</b></font></html>",
                            "text/html",
                            "uft-8",
                            null);
                            */

                    return false;
                    //return true;// need to change back to false
                }
            }

            startActivity(intent);
            return true;
        }

        //Need to eventually change the UID to my email address.
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //getSchedule.requestLayout();
            final String viewUrl = view.getUrl();
            final String myUrl = url.toString();

            if (getSchedule.getUrl().equals(LOGIN_URL)) {
                //if (view.getUrl().equals(url)) {
                //getSchedule.setVisibility(View.VISIBLE);
                //Update.setVisibility(View.VISIBLE);
                getSchedule.loadUrl(
                        "javascript:var bld = document.getElementById('EmpID').style.color = 'red' " + ";"
                                + "javascript:var x = document.getElementById('EmpID').value = " + name + ";"
                                + "javascript:var y = document.getElementById('Password').style.display = 'none' " + ";"
                                + "javascript:var a = ''" + ";"
                                + "javascript:var b = document.getElementById('Password').value = " + 'a' + ";"

                );
            }

            //Password expired. Need to handle change password case.
            else if (url.equals("https://myschedule.safeway.com/ESS/AuthN/SwyLogInError.aspx")) {
                getSchedule.setVisibility(View.VISIBLE);
                getSchedule.loadDataWithBaseURL("myschedule.safeway.com",
                        "<html><li><font size =\"14\"><b>" +
                                "THE PASSWORD IS EITHER WRONG OR EXPIRED" +
                                "</b></font></li></html>",
                        "text/html",
                        "uft-8",
                        null);
            }


            else if (url.equals("https://myschedule.safeway.com/ESS/Schedule.aspx")) {
                Log.e(PRODUCTION_TAG, "DID THE SCHEDULE GET UPDATED? " + scheduleGotUpdated);
                //getSchedule.setVisibility(View.INVISIBLE);
                startActivity(intent);
                //Update.setVisibility(View.VISIBLE);

            }


            //Update schedule
            else if (url.equals("about:blank")) {

            } else {
                scheduleGotUpdated = false;
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("UPDATE_SCHEDULE", scheduleGotUpdated);
                editor.commit();
                Log.i(PRODUCTION_TAG, "OFFLINE SCHEDULE LOADED");
            }

            Log.e(PRODUCTION_TAG, " ");
            Log.e(PRODUCTION_TAG, "Finished: The cookies in a string:" + CookieManager.getInstance().getCookie(url));
            Log.e(PRODUCTION_TAG, "VIEW PAGE FINISHED " + view.getUrl());
            Log.e(PRODUCTION_TAG, "VIEW URL PAGE FINISHED " + url.toString());
            Log.e(PRODUCTION_TAG, "----------------------------------");
            Log.e(PRODUCTION_TAG, "GETSCHEDULE PAGE FINISHED " + getSchedule.getUrl());
            Log.e(PRODUCTION_TAG, "GETSCHEDULE URL PAGE FINISHED " + getSchedule.toString());
            Log.e(PRODUCTION_TAG, "THE PAGE ENDED STATE IS: " + pageEnded);


        } //pageEnded()


        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
            Log.i(PRODUCTION_TAG, "UPDATE VISIT HISTORY VIEW: " + view.getUrl());
            Log.i(PRODUCTION_TAG, "UPDATE VISIT HISTORY URL: " + url.toString());
        }

        //Authorization isn't implemented on the remote server.
        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
            //handler.proceed("9857701@safeway.com", pass);
            Log.i(PRODUCTION_TAG, "---RECEIVED HTTP AUTH REQUEST");
        }


        //Spits back on 404
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            if (Build.VERSION.SDK_INT >= 23) {
                Log.e(PRODUCTION_TAG, "Http error code: " + errorResponse.getStatusCode());
                Log.e(PRODUCTION_TAG, "Http request url: " + request.getUrl());
            }
        }

        //Distinguish between client side vs server side JavaScript.
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            //put super() before getschedule cache()?
            //super.onReceivedError(view, request, error);
            if (Build.VERSION.SDK_INT >= 24) {
                //notConnected = true;
                Log.e(PRODUCTION_TAG, "Error: " + error.getDescription().toString() +
                        " Is connected? " + refreshDisplay);
                Toast.makeText(getApplicationContext(), "" +
                        "NO WIFI. NOT CONNECTED (DEBUG MODE)", Toast.LENGTH_LONG).show();
            }

        }


        /*
         * Attempt to handle logout and SSL cert errors correctly. 1 - 23 - 2017
         * The Alternative is to uninstall the app.
         *
         */
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //super.onReceivedSslError(view, handler, error);
            if(handler != null) {
                //handler.proceed();
            } else {
                Log.i(PRODUCTION_TAG, "SSL ERROR");
            }

        }

    }


    //Added on 4 - 11 - 2019
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Log.e(PRODUCTION_TAG, "ALARM DATA: " + data.getExtras());
        finish();
    }

    protected void setTextReminder() {

    }



    //Because using an intent as per docs doesn't fucking work.
    //****************************************************************************************

    //I do the MIT thing and just copy and the paste code from the technical docs.
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /*
     *If the drive ain't mounted, I can't read it. This only becomes obvious when my idiot carrier drops
     * my phone signal while I'm watching porn on the BART. 7 - 31 - 2018
     */
    private void checkDrivePermissions() {
        //change to or? 8 - 25 - 2018
        if (isExternalStorageReadable() && isExternalStorageWritable()) {
            checkFilePermissions();
        } else if (isExternalStorageReadable()) {
            checkFilePermissions();
        }else {
            BECAUSE_INTENTS_ON_DEVACTIVED_PHONES_DONT_FUCKING_WORK = true;
            Log.e(PRODUCTION_TAG, "Can't read drive");
        }
    }

    private void checkFilePermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(PRODUCTION_TAG, "Read storage permission already exists");

        } else {
            BECAUSE_INTENTS_ON_DEVACTIVED_PHONES_DONT_FUCKING_WORK = true;
        }
    }

}//end MainActivity