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

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class Logout extends AppCompatActivity {
    private WebView loginPage; //Added on 7 - 2 - 2019
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
