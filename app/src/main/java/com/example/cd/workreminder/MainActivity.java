package com.example.cd.workreminder;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;

import android.app.ProgressDialog;
import android.net.http.HttpResponseCache;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements ConnectionCallback{
    private WebView getSchedule;
    public String LANDINGPAGE_URL = "myschedule.safeway.com";
    //public static String LOGIN_URL = "https://myschedule.safeway.com/ESS/AuthN/SwyLogin.aspx?ReturnUrl=%2fESS";
    public static String LOGIN_URL = "http://172.31.99.61/index.html";
    protected static final String UA = "Pak N Slave Mobile App; Written by cda@stanford.edu; Uhh...Hi Mom!";
    public static final int NOTIFICATION_ID = 0; //Added on 10 - 14 - 2019

    private final String name = "9857701"; //modified on 1 - 8 - 201
    public static boolean refreshDisplay = true; //added on 6 - 14 - 2018

    private final String PRODUCTION_TAG = "LG_WORK_WEB: "; //used for hardware only

    //private boolean notConnected = false; //modified on 9 - 5- 2018
    private TextView offline; //added on 9 - 8 - 2018
    private String connectionStatus = ""; //added on 9 - 18 - 2018
    public static String CurrentSchedule = "CurrentSchedule"; //added on 9 - 19 - 2018
    public String fileContents = ""; //default is ""
    private SharedPreferences pref; //added on 9 - 21 - 2018

    private boolean pageEnded = false; //Added on 11 - 12 - 2018
    private boolean onClick = false; //Added on 11 - 13 - 2018

    private String OfflineMessage; //Added on 11 - 21 - 2018
    private Intent intent; //Added on 11 - 21 - 2018

    private WorkNetworkFragment mNetworkFragment; //Added on 1 - 2 - 2019

    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean mDownloading = false;
    private static boolean scheduleGotUpdated = false; //Added on 1 - 22 - 2019

    private Handler handler = new Handler();
    private int progressStatus = 0;


    @SuppressLint("ClickableViewAccessibility")
    @TargetApi(19)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_update_job_schedule);

        Log.e(PRODUCTION_TAG, "ONCREATE() BEFORE SAVEDINSTANCE()");
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        //progressBar.setLayoutParams(new ViewGroup.LayoutParams(150, 10));
        ViewGroup.LayoutParams layoutParams = progressBar.getLayoutParams();
        //Log.e(PRODUCTION_TAG, "THE CURRENT LAYOUTWIDTH IS: " + layoutParams.width);
        layoutParams.width = 450; //450dp ??
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;

        Log.e(PRODUCTION_TAG, "THE SCREEN WIDTH IN PIXELS IS: " + screenWidth);
        progressBar.setLayoutParams(layoutParams);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            //textView.setText(progressStatus+"/"+progressBar.getMax());
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        //offline = (TextView)findViewById(R.id.offline);
        //offlineUpdate = (Button) findViewById(R.id.offlineUpdate);
        //offlineUpdate.setVisibility(View.GONE);

        //WebView getSchedule = new WebView(this);
        //setContentView(getSchedule);

        //readFromDrive(); //need to move to fragment?

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        //    if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {

        WebView.setWebContentsDebuggingEnabled(true); //vs instance of a class??=
        //   }
        //}

        try {
            File httpCacheDir = new File(this.getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            Log.i(PRODUCTION_TAG, "HTTP response cache installation failed:" + e);
        }

        intent = new Intent(MainActivity.this, CurrentWeekSchedule.class);

        if (savedInstanceState == null) {
            Log.e(PRODUCTION_TAG, "ONCREATE() WHEN SAVEDINSTANCE() IS NULL");

            //Attempt to invoke interface method 'java.lang.String android.content.SharedPreferences.getString(java.lang.String, java.lang.String)'
            //on a null object reference


            OfflineMessage = "<html>" +
                    "<font size =\"32\"><b>THIS PAGE CANNOT BE LOADED BECAUSE YOUR CELL PHONE CARRIER" +
            //readFromInternalDirectory(new File(CurrentSchedule + ThisWeek));
            intent.setAction(Intent.ACTION_SEND);

            pref = this.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);

            webViewSettings();
            //Change "SUNDAY" to getString(R.String.SUNDAY)???
            /*saveCurrentSchedule(R.string.SUNDAY,
                    R.string.SUNDAY_START_HOUR,
                    R.string.SUNDAY_START_MINUTE,
                    R.string.SUNDAY_START_AM_OR_PM,
                    R.string.SUNDAY_END_HOUR,
                    R.string.SUNDAY_END_MINUTE,
                    R.string.SUNDAY_END_AM_OR_PM);

            saveCurrentSchedule(R.string.MONDAY,
                    R.string.MONDAY_START_HOUR,
                    R.string.MONDAY_START_MINUTE,
                    R.string.MONDAY_START_AM_OR_PM,
                    R.string.MONDAY_END_HOUR,
                    R.string.MONDAY_END_MINUTE,
                    R.string.MONDAY_END_AM_OR_PM);

            saveCurrentSchedule(R.string.TUESDAY,
                    R.string.TUESDAY_START_HOUR,
                    R.string.TUESDAY_START_MINUTE,
                    R.string.TUESDAY_START_AM_OR_PM,
                    R.string.TUESDAY_END_HOUR,
                    R.string.TUESDAY_END_MINUTE,
                    R.string.TUESDAY_END_AM_OR_PM);

            saveCurrentSchedule(R.string.WEDNESDAY,
                    R.string.WEDNESDAY_START_HOUR,
                    R.string.WEDNESDAY_START_MINUTE,
                    R.string.WEDNESDAY_START_AM_OR_PM,
                    R.string.WEDNESDAY_END_HOUR,
                    R.string.WEDNESDAY_END_MINUTE,
                    R.string.WEDNESDAY_END_AM_OR_PM);

            saveCurrentSchedule(R.string.THURSDAY,
                    R.string.THURSDAY_START_HOUR,
                    R.string.THURSDAY_START_MINUTE,
                    R.string.THURSDAY_START_AM_OR_PM,
                    R.string.THURSDAY_END_HOUR,
                    R.string.THURSDAY_END_MINUTE,
                    R.string.THURSDAY_END_AM_OR_PM);

            //12AM represents midnight on my phone
            saveCurrentSchedule(R.string.FRIDAY,
                    R.string.FRIDAY_START_HOUR,
                    R.string.FRIDAY_START_MINUTE,
                    R.string.FRIDAY_START_AM_OR_PM,
                    R.string.FRIDAY_END_HOUR,
                    R.string.FRIDAY_END_MINUTE,
                    R.string.FRIDAY_END_AM_OR_PM);

            saveCurrentSchedule(R.string.SATURDAY,
                    R.string.SATURDAY_START_HOUR,
                    R.string.SATURDAY_START_MINUTE,
                    R.string.SATURDAY_START_AM_OR_PM,
                    R.string.SATURDAY_END_HOUR,
                    R.string.SATURDAY_END_MINUTE,
                    R.string.SATURDAY_END_AM_OR_PM);
                    */

        } else {
            Log.e(PRODUCTION_TAG, "ONCREATE() WHEN SAVEDINSTANCE() IS NOT NULL");
            savedInstanceState.getString("UPDATED_SCHEDULE"); //???

            pref = getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE); //redudant??
            //readFromInternalDirectory(new File(CurrentSchedule + ThisWeek));

            webViewSettings();

        }


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

    //Added on 10 - 16 - 2019
    private void saveCurrentSchedule(
            int day,
            int startHour,
            int startMinute,
            int startAmOrPm,
            int endHour,
            int endMinute,
            int endAmOrPm
    )
    {
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(getString(R.string.SATURDAY),
                pref.getString(getString(day ), getString(day)));
        editor.putString(getString(startHour),
                pref.getString(getString(startHour), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
        editor.putString(getString(startMinute),
                pref.getString(getString(startMinute), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
        editor.putString(getString(startAmOrPm),
                pref.getString(getString(startAmOrPm), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
        editor.putString(getString(endHour),
                pref.getString(getString(endHour), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
        editor.putString(getString(endMinute),
                pref.getString(getString(endMinute), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
        editor.putString(getString(endAmOrPm),
                pref.getString(getString(endAmOrPm), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));

        editor.apply();
    }

    //Added on 10 - 16 - 2019
    private void webViewSettings() {
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
        //mNetworkFragment = WorkNetworkFragment.getInstance(
        //        getSupportFragmentManager(),
        //        "https://" + LANDINGPAGE_URL);

        mNetworkFragment = WorkNetworkFragment.getInstance(
                getSupportFragmentManager(),
                LOGIN_URL);

        getSchedule.loadUrl(LOGIN_URL);
        getSchedule.setVisibility(View.VISIBLE); //disable for debugging.

        /*getSchedule.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                startDownload();
                Log.e(PRODUCTION_TAG, "WEBVIEW BUTTON GOT CLICKED");
                return false;
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


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void onStart() {
        super.onStart();
    }//end onStart()


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
        }
    }

    @Override
    /*
     * Attempt to suppress "Failed to locate a binder for interface: autofill::mojom::PasswordManagerDriver"
     */
    protected void onResume() {
        super.onResume(); //stupid hack;

        Log.i(PRODUCTION_TAG, "MAIN ACTIVITY ON RESUME ");
    }


    //Kill the http connection and flush the cache.
    @Override
    protected void onDestroy() {
        //super.onDestroy();

        //cancel();
        super.onDestroy(); //why?
    }


    /*
     *Added on 8 - 16 - 2018. Checks for connection when app loads up for the first time
     *
     */
    private String downloadLoginPage(URL url) throws IOException {
        InputStream inputStream = null;
        //HttpsURLConnection connection = null;
        URLConnection connection = null;
        String result = null;
        boolean redirect = false; //added on 9 - 25 - 2018
        int code = 0; //added on 12 - 8 - 2018
        try {
            //connection = (HttpsURLConnection) url.openConnection();
            connection = url.openConnection();
            //HttpsURLConnection.setFollowRedirects(false);
            //connection.setInstanceFollowRedirects(true);
            //connection.setReadTimeout(9000); //3000ms
            //connection.setConnectTimeout(9000); //3000ms
            //connection.setRequestMethod("GET");

            if (CookieManager.getInstance().getCookie(connection.getURL().toString()) != null) {
                //String arr[] = CookieManager.getInstance().getCookie(connection.getURL().toString()).split(";");
                //connection.setRequestProperty("Cookie", arr[1]);
                //Log.i(PRODUCTION_TAG, "The client side cookie is: " + arr[1]);
            }

            connection.setDoInput(true);

            //connection.setChunkedStreamingMode(0); //added on 9 - 12 - 2018
            connection.connect();
            //int responsecode = connection.getResponseCode();
            int responsecode = ((HttpURLConnection) connection).getResponseCode();
            Log.e(PRODUCTION_TAG, "THE RESPONSE CODE IS: " + responsecode);
            //int responsecode = 200;
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

        finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                //connection.disconnect(); //why no else if??
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
        //Possibly reading too large data stream. Hence why the insufficient resource error?
        //Log.d(PRODUCTION_TAG, "STARTED TO READ STREAM"); //added on 10 - 4- 2018
        while (numChars < maxLength && readSize != -1) {
            numChars += readSize;;
            inputStreamReader.read();
            readSize = inputStreamReader.read(buffer, numChars, buffer.length - numChars);
        }

        if (numChars != -1 ) {
            //Log.e(PRODUCTION_TAG, "INITIAL HTTP CONNECTION? " + numChars);
            numChars = Math.min(numChars, maxLength);
            result = new String(buffer, 0, numChars);
        }

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


        //Log path. Used from debugging purposes only.
        @TargetApi(21)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request){
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

            //produces infinite get requests when offline.
            Log.i(PRODUCTION_TAG, "The url is: " + url);
            Log.i(PRODUCTION_TAG, "The viewed page is: " + view.getUrl());
            Log.i(PRODUCTION_TAG, "ON PAGE STARTED (DEBUG MODE)");

        }

        //Only download the schedule once a week. Rest of the time have the client handle the connection.
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
            //if (host.equals("myschedule.safeway.com")) {
            if (host.equals("10.105.193.163")) {
                Log.i(PRODUCTION_TAG, "DID THE LINK GET CLICKED? " + onClick);
                Log.i(PRODUCTION_TAG, "CLICKED VIEW URL IS: " + view.getUrl());
                Log.i(PRODUCTION_TAG, "REQUESTED URL IS: " + request.getUrl());

                //pref.getInt(getString(R.string.com_example_cd_shiftreminder_SAVED_DOWNLOAD_DATE),00);

                Calendar cal = Calendar.getInstance();
                    //if (cal.get(Calendar.DAY_OF_WEEK) != pref.getInt(getString(R.string.com_example_cd_shiftreminder_SAVED_DOWNLOAD_DATE),00)
                    //        && pageEnded == true) {
                Log.e(PRODUCTION_TAG, "SCHEDULE HAS BEEN UPDATED");

                //getSchedule.setVisibility(View.INVISIBLE); //suppress server css/html/javascript

                if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                    getSchedule.setVisibility(View.INVISIBLE);
                    return false;
                    //return true;// need to change back to false
                }
            }

            startActivity(intent);
            return true;
            //return false;
        }

        //I use JavaScript to automate the login sequence.
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            final ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);

            if (getSchedule.getUrl().equals(LOGIN_URL)) {
                //if (view.getUrl().equals(url)) {
                //getSchedule.setVisibility(View.VISIBLE);
                //Update.setVisibility(View.VISIBLE);
                /*getSchedule.loadUrl(
                        "javascript:var bld = document.getElementById('EmpID').style.color = 'red' " + ";"
                                + "javascript:var x = document.getElementById('EmpID').value = " + name + ";"
                                //+ "javascript:var y = document.getElementById('Password').style.display = 'none' " + ";"
                                //+ "javascript:var a = ''" + ";"
                                //+ "javascript:var b = document.getElementById('').value = " + 'a' + ";"

                );
                */

                //Default on initial installation
                if (pref.getBoolean("SAVE_PASSWORD", false) == true) {
                    String defaultName =  pref.getString("NAME", "0");
                    String defaultPassword = pref.getString("PASSWORD", "\'\'");
                    getSchedule.loadUrl(
                            "javascript:var bld = document.getElementById('EmpID').style.color = 'red' " + ";"
                                    + "javascript:var x = document.getElementById('EmpID').value = " + defaultName+ ";"
                                    + "javascript:var y = document.getElementById('Password').value = " + defaultPassword + ";"
                                    + "javascript:var z = document.getElementById('Password').style.display = 'none' " + ";"

                    );
                }
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
                        null); }


            else if (url.equals("https://myschedule.safeway.com/ESS/Schedule.aspx")) {
                Log.e(PRODUCTION_TAG, "DID THE SCHEDULE GET UPDATED? " + scheduleGotUpdated);
                //getSchedule.setVisibility(View.VISIBLE);
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
        @TargetApi(23)
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            Log.e(PRODUCTION_TAG, "Http error code: " + errorResponse.getStatusCode());
            Log.e(PRODUCTION_TAG, "Http request url: " + request.getUrl());

        }

        //Display schedule right away if there is no internet connection
        // @Override
        @TargetApi(24)
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            //put super() before getschedule cache()?
            //super.onReceivedError(view, request, error);
            Intent offlineSchedule = new Intent(MainActivity.this, CurrentWeekSchedule.class);
            startActivity(offlineSchedule);
            Log.e(PRODUCTION_TAG, "Error: " + error.getDescription().toString() +
                        " Is connected? " + refreshDisplay);
            Toast.makeText(getApplicationContext(), "" +
                    "NO WIFI. NOT CONNECTED (DEBUG MODE)", Toast.LENGTH_LONG).show();
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

}//end MainActivity