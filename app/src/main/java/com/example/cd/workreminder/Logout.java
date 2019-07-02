package com.example.cd.workreminder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class Logout extends AppCompatActivity {
    private WebView loginPage; //Added o n 7 - 2 - 2019
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        loginPage = (WebView) findViewById(R.id.LoginPage);
        //loginPage.setWebViewClient(WWebViewClient());
        //loginPage = new WebView(this);
        loginPage.getSettings().setLoadWithOverviewMode(true);
        loginPage.getSettings().setUseWideViewPort(true);
        loginPage.getSettings().setJavaScriptEnabled(true);
        loginPage.getSettings().setDomStorageEnabled(true);
        loginPage.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //Logout sequence acquired via a packet sniffer
        loginPage.loadUrl("https://myschedule.safeway.com//ESS/AuthN/Swylogin.aspx?ReturnUrl=%2fESS%2fLogin.aspx%3fClose%3dtrue&Close=true");
    }
}//end class
