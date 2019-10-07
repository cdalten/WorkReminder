package com.example.cd.workreminder;

//package com.example.cd.shiftreminder;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/*
 * Classed added on 1 - 2 - 2019 because the Android GUI sucks massive dick.
 */
public class WorkNetworkFragment extends Fragment {
    public static final String TAG = "NetworkFragment";

    private static final String URL_KEY = "UrlKey";

    private ConnectionCallback mCallback;
    private DownloadTask mDownloadTask;
    private String mUrlString;
    private String PRODUCTION_TAG = "LG_WORK_PHONE";
    private WebView getSchedule;

    public static WorkNetworkFragment getInstance(FragmentManager fragmentManager, String url) {
        WorkNetworkFragment networkFragment = (WorkNetworkFragment) fragmentManager
                .findFragmentByTag(WorkNetworkFragment.TAG);

        if (networkFragment == null) {
            networkFragment = new WorkNetworkFragment();
            Bundle args = new Bundle();
            args.putString(URL_KEY, url);
            networkFragment.setArguments(args);
            fragmentManager.beginTransaction().add(networkFragment, TAG).commit();
        }
        return networkFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("LG_WORK_PHONE", "ONCREATEVIEW GOT CALLED WITH ARGS: " + getArguments().getString(URL_KEY));
        //View view = inflater
        //        .inflate(R.layout.activity_update_job_schedule, container, false);

        //getSchedule = new WebView(getActivity());

        //return view;
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); //??? 1 - 4- 2019
        mUrlString = getArguments().getString(URL_KEY);
        Log.e("LG_WORK_PHONE", "ONCREATE FRAGMENT GOT CALLED " + mUrlString);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Host Activity will handle callbacks from task.
        mCallback = (ConnectionCallback) context;
        Log.e("LG_WORK_PHONE", "ONATTACH FRAGMENT GOT CALLED " );
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Clear reference to host Activity to avoid memory leak.
        mCallback = null;
    }

    @Override
    public void onDestroy() {
        // Cancel task when Fragment is destroyed.
        cancelDownload();
        super.onDestroy();
    }


    public void startDownload() {
        cancelDownload();
        mDownloadTask = new DownloadTask(mCallback);
        //mDownloadTask.execute(mUrlString);
        Log.e(PRODUCTION_TAG, "THE URL STRING IN STARTDOWNLOAD() IS: " + mUrlString);
        mDownloadTask.execute(mUrlString);
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing DownloadTask execution.
     */
    public void cancelDownload() {
        if (mDownloadTask != null) {
            mDownloadTask.cancel(true);
            mDownloadTask = null;

        }
    }

    /**
     * Implementation of AsyncTask designed to fetch data from the network.
     */
    private class DownloadTask extends AsyncTask<String, Integer, DownloadTask.Result> {

        private ConnectionCallback<String> mCallback;

        DownloadTask(ConnectionCallback<String> callback) {
            setCallback(callback);
        }

        void setCallback(ConnectionCallback<String> callback) {
            mCallback = callback;
        }


        /**
         * Wrapper class that serves as a union of a result value and an exception. When the download
         * task has completed, either the result value or exception can be a non-null value.
         * This allows you to pass exceptions to the UI thread that were thrown during doInBackground().
         */
        class Result {
            public String mResultValue;
            public Exception mException;
            public Result(String resultValue) {
                mResultValue = resultValue;
            }
            public Result(Exception exception) {
                mException = exception;
            }
        }

        /**
         * Cancel background network operation if we do not have network connectivity.
         */
        @Override
        protected void onPreExecute() {
            if (mCallback != null) {
                NetworkInfo networkInfo = mCallback.getActiveNetworkInfo();
                //NetworkInfo networkInfo = null; //Added on 1 - 4 - 2019 for debugging
                if (networkInfo == null || !networkInfo.isConnected() ||
                        (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                                && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                    // If no connectivity, cancel task and update Callback with null data.
                    mCallback.updateFromDownload(null);
                    cancel(true);
                }
            }
        }

        /**
         * Defines work to perform on the background thread.
         */
        protected DownloadTask.Result doInBackground(String... urls) {
            Result result = null;
            if (!isCancelled() && urls != null && urls.length > 0) {
                String urlString = urls[0];
                try {
                    URL url = new URL(urlString);
                    String resultString = downloadLoginPage(url);
                    //String resultString = null;
                    if (resultString != null) {
                        result = new Result(resultString);
                        Log.e(PRODUCTION_TAG, "BACKGROUND FRAGMENT IS: " + resultString);
                    } else {
                        throw new IOException("No response received.");
                    }
                } catch(Exception e) {
                    Log.e(PRODUCTION_TAG, "PHONE DATA TURNED OFF??" + e);
                    result = new Result(e);
                    //Intent intent = new Intent(getContext(), CurrentWeekSchedule.class); //stupid hack
                    //startActivity(intent); //added on 1 - 12 - 2019
                }
            }
            return result;
        }

        /**
         * Updates the DownloadCallback with the result.
         */
        @Override
        protected void onPostExecute(Result result) {
            if (result != null && mCallback != null) {
                if (result.mException != null) {
                    //mCallback.updateFromDownload(result.mException.getMessage());
                } else if (result.mResultValue != null) {
                    mCallback.updateFromDownload(result.mResultValue);
                    Log.e(PRODUCTION_TAG,"ON POST RESULT FROM FRAGMENT ---> " + result.mResultValue);
                }
                mCallback.finishDownloading();
            }
        }

        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(Result result) {
        }
    }

    private String downloadLoginPage(URL url) throws IOException {
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        String result = null;
        boolean redirect = false; //added on 9 - 25 - 2018
        int code = 0; //added on 12 - 8 - 2018
        try {
            connection = (HttpURLConnection) url.openConnection();

            //Bug. Shows already connected. Why 9 - 26 - 2018
            //String serverCookie = connection.getHeaderField("Set-Cookie");
            //Log.i(PRODUCTION_TAG, "The server side cookie is: " + serverCookie);
            //connection.setRequestProperty("Cookie", serverCookie);

            //Need to set cookie also? 9 - 26 - 2018
            //connection.setRequestProperty("Location", "https://myschedule.safeway.com/ESS/");
            //connection.addRequestProperty("Cache-Control", "private");
            HttpsURLConnection.setFollowRedirects(false);
            //connection.setInstanceFollowRedirects(true);
            connection.setReadTimeout(3000); //3000ms
            connection.setConnectTimeout(3000); //3000ms
            connection.setRequestMethod("GET");

            if (CookieManager.getInstance().getCookie(connection.getURL().toString()) != null) {
                String arr[] = CookieManager.getInstance().getCookie(connection.getURL().toString()).split(";");
                connection.setRequestProperty("Cookie", arr[1]);
                Log.i(PRODUCTION_TAG, "The client side cookie is: " + arr[1]);
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

                throw new IOException("Redirect response code" + responsecode); //added on 10 - 2 - 2018
            }


            inputStream = connection.getInputStream();
            if (inputStream != null) {
                //Log.e(PRODUCTION_TAG, "STARTED DOWNLOAD");
                //log_e(buffer - offset) / (2 ^n - 1)
                result = readStream(inputStream, 600);
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
}