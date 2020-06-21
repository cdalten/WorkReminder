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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Entity;
import android.content.pm.PackageManager;
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
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements ConnectionCallback{
    private WebView getSchedule;
    public String LANDINGPAGE_URL = "myschedule.safeway.com";
    //public static String LOGIN_URL = "https://myschedule.safeway.com/ESS/AuthN/SwyLogin.aspx?ReturnUrl=%2fESS";

    public static String LOGIN_URL = "https://www.dayforcehcm.com/MyDayforce/MyDayforce.aspx?isCstBrand=true";
    //public static String LOGIN_URL = "http://192.168.43.114/index.html"; //My phone
    //public static String LOGIN_URL = "http://172.31.99.60/index.html"; //Starbucks
    //public static String LOGIN_URL = "http://10.105.185.33/index.html"; //Level D 4104 Dwinelle Hall

    //public static String LOGIN_URL = "http:/https://usr56.dayforcehcm.com/mydayforce/mydayforce.aspx/";
    protected static final String UA = "Pak N Slave Mobile App; Written by cda@stanford.edu; Uhh...Hi Mom!";
    public static final int NOTIFICATION_ID = 0; //Added on 10 - 14 - 2019
    public static final String ALARM_RINGTONE = "ALARM_RINGTONE"; //Added on 11  - 26 - 2019

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
    private String serverSideCookie = ""; //Added o n 6 - 14 - 2020
    private  boolean amGzip;

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
        //    if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)

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


        enableBootReceiver();

        //try {
        //    String cookies = getServerCookie(new URL(LOGIN_URL));
        //    Log.e(PRODUCTION_TAG, "THE SERVER SIDE COOKIE IS: " + cookies);
        //} catch (Exception e) {
        //    Log.e(PRODUCTION_TAG, "CAN'T GET COOKIE");
        //}
        if (savedInstanceState == null) {
            Log.e(PRODUCTION_TAG, "ONCREATE() WHEN SAVEDINSTANCE() IS NULL");

            //Attempt to invoke interface method 'java.lang.String android.content.SharedPreferences.getString(java.lang.String, java.lang.String)'
            //on a null object reference


            OfflineMessage = "<html>" +
                    "<font size =\"32\"><b>THIS PAGE CANNOT BE LOADED BECAUSE YOUR CELL PHONE CARRIER";
            //readFromInternalDirectory(new File(CurrentSchedule + ThisWeek));
            //intent.setAction(Intent.ACTION_SEND);

            pref = this.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);

            webViewSettings();

            //12AM represents midnight on my phone

        } else {
            Log.e(PRODUCTION_TAG, "ONCREATE() WHEN SAVEDINSTANCE() IS NOT NULL");
            savedInstanceState.getString("UPDATED_SCHEDULE"); //???

            pref = getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE); //redudant??
            //readFromInternalDirectory(new File(CurrentSchedule + ThisWeek));

            webViewSettings();
        }

        /*getSchedule.setWebChromeClient(new WebChromeClient() {
            public
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

    //Added on 2 - 28 - 2020
    private void enableBootReceiver() {
        ComponentName receiver = new ComponentName(this, BootReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void dispatchCurrentWeekSchedule() {
        Intent currentWeekScheduleIntent = new Intent(MainActivity.this, CurrentWeekSchedule.class);
        if (currentWeekScheduleIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(currentWeekScheduleIntent);
            }

    }
    //Added on 10 - 16 - 2019
    @TargetApi(21)
    private void webViewSettings() {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            Log.e(PRODUCTION_TAG, "INTERNET? " + isConnected);
            dispatchCurrentWeekSchedule();
        }
        else {

            //new JavascriptBridge();
            getSchedule = (WebView) this.findViewById(R.id.CurrentSchedule);
            getSchedule.setWebViewClient(new WWebViewClient());
            getSchedule.addJavascriptInterface(new JavaScriptBridge(), "HTMLOUT");
            getSchedule.clearCache(true);
            getSchedule.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            getSchedule.getSettings().setAppCacheEnabled(false);
            //determineCacheMode();
            getSchedule.getSettings().setLoadWithOverviewMode(true);
            getSchedule.getSettings().setUseWideViewPort(true);
            getSchedule.getSettings().setJavaScriptEnabled(true);
            getSchedule.getSettings().setDomStorageEnabled(true);
            getSchedule.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); //added on 9 - 23 - 2018
            //CookieManager.getInstance().setAcceptCookie(true);

            //double render????
            //mNetworkFragment = WorkNetworkFragment.getInstance(
            //        getSupportFragmentManager(),
            //        "https://" + LANDINGPAGE_URL);

            mNetworkFragment = WorkNetworkFragment.getInstance(
                    getSupportFragmentManager(),
                    LOGIN_URL);

            CookieManager cookieManager = CookieManager.getInstance();
            //cookieManager.acceptCookie();
            cookieManager.setAcceptCookie(true);
            //cookieManager.removeSessionCookie(); // remove
            //cookieManager.removeAllCookie(); //remove
            //Namespace=goodeggs&UserName=chad.altenburg
            //cookieManager.setCookie(LOGIN_URL, "NamespaceCookie=goodeggs");
            //cookieManager.setCookie(LOGIN_URL, "UserName=chad.altenburg");
            //cookieManager.setCookie(LOGIN_URL, "LoginCookie=Namespace=goodeggs&UserName=chad.altenburg");
            cookieManager.setAcceptThirdPartyCookies(getSchedule, true);

            getSchedule.loadUrl(LOGIN_URL);
            getSchedule.setVisibility(View.VISIBLE); //disable for debugging.
        }

    }
    /*
     Added on 3 - 12 - 2020.
     Debugging only. Need to remove
     */
    private void determineCacheMode(){
        int mode = getSchedule.getSettings().getCacheMode();
        switch (mode) {
            case WebSettings.LOAD_CACHE_ONLY:
                Log.e(PRODUCTION_TAG, "LOAD_CACHE_ONLY");
                break;
            case WebSettings.LOAD_DEFAULT:
                Log.e(PRODUCTION_TAG, "LOAD_DEFAULT");
                break;
            case WebSettings.LOAD_NO_CACHE:
                Log.e(PRODUCTION_TAG, "LOAD_NO_CACHE");
                break;
            case WebSettings.LOAD_CACHE_ELSE_NETWORK:
                Log.e(PRODUCTION_TAG, "LOAD_CACHE_ELSE_NETWORK");
                break;
        }
    }

    class JavaScriptBridge {
        @JavascriptInterface
        void processHTML(String s) {
            Log.e(PRODUCTION_TAG, "===> " + s);
        }
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

    /*
      getActiveNetworkInfo(), finishDownloading(), and startDownload() code ripped off from the
      office Google sample Google Android sample code.
     */
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
    }


    //Kill the http connection and flush the cache.
    @Override
    protected void onDestroy() {
        //super.onDestroy();

        //cancel();
        super.onDestroy(); //why?
    }


    //Added on 3 - 5- 2020
    private void readJSON(URL url) {

    }
    /*
     *Added on 8 - 16 - 2018. Checks for connection when app loads up for the first time
     *
     */
    private String getServerCookie(URL url) throws IOException {
        Log.d(PRODUCTION_TAG, "===> " + url);
        readJSON(url);
        //InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        //HttpsURLConnection connection = null;
        HttpURLConnection connection = null;
        String result = null;
        String cookies = "";
        int code = 0; //added on 12 - 8 - 2018
        try {

            //HttpsURLConnection.setFollowRedirects(false);
            //connection.setInstanceFollowRedirects(true);
            //connection.setReadTimeout(9000); //3000ms
            //connection.setConnectTimeout(9000); //3000ms

            //The input form passed from the client to the server is in gzip format.
            //Any other other type will result in an I/0 error
            connection = (HttpURLConnection) url.openConnection();
            //String message = connection.getResponseMessage();
            //String cookies = connection.getHeaderField("Set-Cookie");

//cookies.split(";")
            connection.addRequestProperty("Accept", "*/*");
            connection.addRequestProperty("Accept-Encoding", "gzip, deflate, br");
            connection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");

            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            cookies = connection.getHeaderField("Set-Cookie");
            //connection.setChunkedStreamingMode(0); //added on 9 - 12 - 2018
            //Fconnection.connect();

            Log.e(PRODUCTION_TAG, "----------------------------------------");
            Log.e(PRODUCTION_TAG, "THE RESPONSE MESSAGE IS: " + connection.getResponseMessage());
            Log.e(PRODUCTION_TAG, "THE RESPONSE REQUEST IS: " + connection.getRequestMethod());
            Log.e(PRODUCTION_TAG, "THE RESPONSE CONTENT ENCONDING IS: " + connection.getContentEncoding());
            Log.e(PRODUCTION_TAG, "THE RESPONSE HEADERS ARE: " + connection.getHeaderFields());
            Log.e(PRODUCTION_TAG, "-----------------------------------");
            //Log.e(PRODUCTION_TAG, "THE RESPONSE CONTENT: " + connection.getContent().toString());
            //int responsecode = connection.getResponseCode();

            if ( connection.getContentEncoding().equals("gzip")) {
                amGzip = true;
            } else {
                amGzip = false;
            }
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
                //throw new IOException(" Http error code" + responsecode);
                //return null;
                throw new IOException("Redirect response code" + responsecode); //added on 10 - 2 - 2018
            }

            InputStreamReader inputStreamReader = (new InputStreamReader(connection.getInputStream()));
            InputStream inputStream = connection.getInputStream();

            bufferedReader = new BufferedReader(inputStreamReader);

            if (bufferedReader != null) {
                result = readStream(bufferedReader, amGzip,64000); //Lame way to emulate ping.
                if (amGzip == true) {
                  decodeStream(inputStream);

                }

            }
        }

        finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (connection != null) {
                //connection.disconnect(); //why no else if??
            }
        }
        //OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
        //out.write(result);
        //out.close();
        return cookies;
    }

    //Added on 6 - 21 - 2020
    private String decodeStream(InputStream stream) {
        //ByteArrayInputStream bais = new ByteArrayInputStream(stream);
        //InputStreamReader inputStreamReader = new InputStreamReader(stream, "UTF-8");
        //GZIPInputStream inputStreamReader = new GZIPInputStream(stream);

        String result = "";
        int numberOfChars = 0;
        try {
            new BufferedReader(new InputStreamReader(new GZIPInputStream(stream)));
            while ((numberOfChars = stream.read()) > 0) {

            }

        } catch (Exception e) {

        }

        Log.e(PRODUCTION_TAG, "THE DECODED STREAM IS: " + result);
        return "";
    }

    private String downloadLoginPage(URL url) throws IOException {
        Log.d(PRODUCTION_TAG, "===> " + url);
        readJSON(url);
        //InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        //HttpsURLConnection connection = null;
        HttpURLConnection connection = null;
        String result = null;
        String cookies = "";
        int code = 0; //added on 12 - 8 - 2018
        try {

            //HttpsURLConnection.setFollowRedirects(false);
            //connection.setInstanceFollowRedirects(true);
            //connection.setReadTimeout(9000); //3000ms
            //connection.setConnectTimeout(9000); //3000ms

            //The input form passed from the client to the server is in gzip format.
            //Any other other type will result in an I/0 error
            connection = (HttpURLConnection) url.openConnection();
            //String message = connection.getResponseMessage();
            //String cookies = connection.getHeaderField("Set-Cookie");

//cookies.split(";")
            connection.addRequestProperty("Accept", "*/*");
            connection.addRequestProperty("Accept-Encoding", "gzip, deflate, br");
            connection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");

            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //cookies = connection.getHeaderField("Set-Cookie");
            //connection.setRequestProperty("Cookie", cookies);
            //connection.setChunkedStreamingMode(0); //added on 9 - 12 - 2018
            //Fconnection.connect();

            Log.e(PRODUCTION_TAG, "----------------------------------------");
            Log.e(PRODUCTION_TAG, "THE RESPONSE MESSAGE IS: " + connection.getResponseMessage());
            Log.e(PRODUCTION_TAG, "THE RESPONSE REQUEST IS: " + connection.getRequestMethod());
            Log.e(PRODUCTION_TAG, "THE RESPONSE CONTENT ENCONDING IS: " + connection.getContentEncoding());
            Log.e(PRODUCTION_TAG, "THE RESPONSE HEADERS ARE: " + connection.getHeaderFields());
            Log.e(PRODUCTION_TAG, "-----------------------------------");
            //Log.e(PRODUCTION_TAG, "THE RESPONSE CONTENT: " + connection.getContent().toString());
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
                //throw new IOException(" Http error code" + responsecode);
                //return null;
                throw new IOException("Redirect response code" + responsecode); //added on 10 - 2 - 2018
            }

            bufferedReader = new BufferedReader((new InputStreamReader(connection.getInputStream())));

            if (bufferedReader != null) {
                result = readStream(bufferedReader, amGzip, 64000); //Lame way to emulate ping.
            }
        }

        finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (connection != null) {
                //connection.disconnect(); //why no else if??
            }
        }
        //OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
        //out.write(result);
        //out.close();
        return result;
    }

    //added on 8 - 16 - 2018.
    private String readStream(BufferedReader inputStreamReader, boolean amGzip, int offset) throws IOException{

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        String result = ""; //change from empty to null??! 10 - 4 - 2018
        int maxLength = offset; //don't ask
        //int maxLength = 100; //modified on 10 - 7 - 2018
        //char [] buffer = new char[maxLength];
        byte [] buffer = new byte[maxLength];
        int numChars = 0;
        int readSize = 0;
        //START REVISED CODE
        //Possibly reading too large data stream. Hence why the insufficient resource error?
        //Log.d(PRODUCTION_TAG, "STARTED TO READ STREAM"); //added on 10 - 4- 2018
        /*while (numChars < maxLength && readSize != -1) {
            numChars += readSize;;
            inputStreamReader.read();
            readSize = inputStreamReader.read(buffer, numChars, buffer.length - numChars);
        }

        if (numChars != -1 ) {
            //Log.e(PRODUCTION_TAG, "INITIAL HTTP CONNECTION? " + numChars);
            numChars = Math.min(numChars, maxLength);
            result = new String(buffer, 0, numChars);
        }
        */

        //code ripped of from stackoverflow
        int length;
        String input = "";

        while ((input = inputStreamReader.readLine()) != null) {
        //while(inputStreamReader.ready()) {
            //String next = inputStreamReader.readLine().trim();
        //    if(next.isEmpty()) {
                //inDefinition = false;
        //        Log.e(PRODUCTION_TAG, "FOUND BLANK LINE IS RESPONSE BODY");
        //    }
            //byteArrayOutputStream.write(buffer, 0, length);
            result = result + input + "--";
        }



        //result = new String(byteArrayOutputStream.toByteArray(), "UTF-8" );
        //Log.e(PRODUCTION_TAG, "===> " + result);
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
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
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
            Log.d(PRODUCTION_TAG, "THE QUERY BODY IS:  " + request.getUrl().getQuery());
            Log.d(PRODUCTION_TAG, "THE PARAMETERS NAMES BODY IS:  " + request.getUrl().getQueryParameterNames());
            //Log.d(PRODUCTION_TAG, "THE PARAMETERS NAMES BODY IS:  " + request.getUrl);


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
                URL url = new URL(request.getUrl().toString());
                serverSideCookie = getServerCookie(url);
                Log.e(PRODUCTION_TAG, "THE SERVER SIDE COOKIE IS: " + serverSideCookie);
                String result = downloadLoginPage(url);
                Log.e(PRODUCTION_TAG, "THE RESULT URL IS: " + url);
                Log.e(PRODUCTION_TAG, "THE RESULT IS: " + result);
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

            String result = ""; //Debug only. Need to remove.


            try {
                //    return new WebResourceResponse("text/javascript", "UTF-8", getAssets().open("index.html"));
                //return new WebResourceResponse("text/javascript", "UTF-8",  );

                //if (request.getUrl().toString().contains("GetEmployeeBalanceDisplay")){
                    WebResourceResponse webResourceResponse = webResourceResponse(new URL(request.getUrl().toString()));
                    //return webResourceResponse(new URL(request.getUrl().toString()));
                    //super.shouldInterceptRequest(view, request);
            //https://usr57.dayforcehcm.com/MyDayforce/u/XzNaUiyPFkadhacwISpHjg/HR/Employee/GetEmployeeBalanceDisplay

                URL url = new URL(request.getUrl().toString());
                URLConnection connection = url.openConnection();
                    //result = readStream(webResourceResponse.getData(), 64000); //Force debug line
                    result = readStream(new BufferedReader((new InputStreamReader(connection.getInputStream()))),amGzip, 64000); //Force debug line
                //result = readStream(null, 64000); //Force debug line
                    Log.e(PRODUCTION_TAG, "===> " + request.getUrl().toString());
                    Log.e(PRODUCTION_TAG, "THE RESPONSE DATA ==>: " + result);
                    //Log.e(PRODUCTION_TAG, "ShouldIntercept() CONTENT ENCODING IS: " + connection.getContentEncoding());
                    //Log.e(PRODUCTION_TAG, "ShouldIntercept() CONTENT  IS: " + connection.getContent().toString());
                //}
            } catch (Exception e) {
                Log.e(PRODUCTION_TAG, "ERROR IN SHOULDINTERCEPT() " + e);
            }

            Log.i(PRODUCTION_TAG, "----------------------------------------------------");

            return super.shouldInterceptRequest(view, request);

        }

        private WebResourceResponse webResourceResponse(URL url) {
            URLConnection connection = null;
            try {
                connection = url.openConnection();
                //connection.connect();

                return new WebResourceResponse("application/json", "UTF-8", connection.getInputStream());
            } catch (IOException e) {
                Log.e(PRODUCTION_TAG, "CANNOT READ STREAM");
            }
            return null;
        }


        /*
         This is an alternative to using a JavaScript bridge. See onPageFinished() method
         for more details.
         */
        public void rememberLogin() {


        }
        /*
         *Possibly add menu visibility here? This is because this appears to be the the only webview
         * entry point when I click on the login button.
         */
        //@Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            //produces infinite get requests when offline.
            //Log.i(PRODUCTION_TAG, "===>: ONPAGESTARTED" + url);
            //Log.i(PRODUCTION_TAG, "===>: ONPAGESTARTED" + view.getUrl());
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


            Log.e(PRODUCTION_TAG, "THE URL IS: " + request.getUrl().toString());
            Log.e(PRODUCTION_TAG, "THE URL QUERY IS: " + request.getUrl().getQuery());
            Log.e(PRODUCTION_TAG, "THE URL LAST PATH SEGMENT IS: " + request.getUrl().getLastPathSegment());
            //Need to manually set scheduleGotUpdated flag to false on every other day.
            //Check for bad connection before resetting flag?
            //if (host.equals("myschedule.safeway.com")) {
            if (host.equals("10.105.193.163")) {
                Log.i(PRODUCTION_TAG, "DID THE LINK GET CLICKED? " + onClick);
                Log.i(PRODUCTION_TAG, "CLICKED VIEW URL IS: " + view.getUrl());
                Log.i(PRODUCTION_TAG, "REQUESTED URL IS: " + request.getUrl());

                Log.e(PRODUCTION_TAG, "THE URL IS: " + request.getUrl().toString());
                Log.e(PRODUCTION_TAG, "THE URL QUERY IS: " + request.getUrl().getQuery());
                Log.e(PRODUCTION_TAG, "THE URL LAST PATH SEGMENT IS: " + request.getUrl().getLastPathSegment());
                //pref.getInt(getString(R.string.com_example_cd_shiftreminder_SAVED_DOWNLOAD_DATE),00);

                final Calendar cal = Calendar.getInstance();
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

            //Log.e(PRODUCTION_TAG, "THE PARSED URI IS: " + Uri.parse(request.getUrl().getHost()));
            //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + request.getUrl().getHost()));
            //startActivity(intent);

            Intent currentWeekScheduleIntent = new Intent(MainActivity.this, CurrentWeekSchedule.class);
            if (currentWeekScheduleIntent.resolveActivity(getPackageManager()) != null) {
                //startActivity(currentWeekScheduleIntent);
            }
            //Toast.makeText(getApplicationContext(),request.getUrl()., Toast.LENGTH_LONG).show();
            //return true;
            Log.e(PRODUCTION_TAG, "-------------------------------------------");
            Log.e(PRODUCTION_TAG, "THE URL IS: " + request.getUrl().toString());
            Log.e(PRODUCTION_TAG, "THE URL QUERY IS: " + request.getUrl().getQuery());
            Log.e(PRODUCTION_TAG, "THE URL LAST PATH SEGMENT IS: " + request.getUrl().getLastPathSegment());
            Log.e(PRODUCTION_TAG, "THE SERVER SIDE COOKIE AGAIN IS: " + serverSideCookie);
            return false;
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
            /*else if (url.equals("https://myschedule.safeway.com/ESS/AuthN/SwyLogInError.aspx")) {
                getSchedule.setVisibility(View.VISIBLE);
                getSchedule.loadDataWithBaseURL("myschedule.safeway.com",
                        "<html><li><font size =\"14\"><b>" +
                                "THE PASSWORD IS EITHER WRONG OR EXPIRED" +
                                "</b></font></li></html>",
                        "text/html",
                        "uft-8",
                        null); }




            */


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
        /*@TargetApi(24)
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            //put super() before getschedule cache()?
            //super.onReceivedError(view, request, error);
            //Intent offlineSchedule = new Intent(MainActivity.this, CurrentWeekSchedule.class);
            //startActivity(offlineSchedule);
            Log.e(PRODUCTION_TAG, "Error: " + error.getDescription().toString() +
                        " Is connected? " + refreshDisplay);
            Toast.makeText(getApplicationContext(), "" +
                    "NO WIFI. NOT CONNECTED (DEBUG MODE)", Toast.LENGTH_LONG).show();
        }
        */


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