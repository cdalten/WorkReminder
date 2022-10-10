package com.example.cd.workreminder;

import android.arch.lifecycle.ReportFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RetrofitHttpClient extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrofit retrofit = new Retrofit.Builder()
        //        .baseUrl(MainActivity.LOGIN_URL + "/")
        //        .build();
    }

    //@GET("")
    //Call<ResponseBody> getIP;
    interface Api {
        //@GET
        //Call<ResponseBody> getIP;
    }
}//end class
