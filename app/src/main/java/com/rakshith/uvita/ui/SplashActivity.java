package com.rakshith.uvita.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.rakshith.uvita.R;
import com.rakshith.uvita.common.AppConstants;


public class SplashActivity extends Activity {
    private static int SPLASH_TIME_OUT=2000;
    private SharedPreferences prefs;
    private String access_token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getApplicationContext().getSharedPreferences(AppConstants.USER_PREFS, Context.MODE_PRIVATE);
        access_token = prefs.getString("access_token",null);


      new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {

              if(access_token!=null){
                  Intent i = new Intent(SplashActivity.this, SearchDoctorAcitivity.class);
                  startActivity(i);
                  finish();
              }else{
                  Intent i = new Intent(SplashActivity.this, MainActivity.class);
                  startActivity(i);
                  finish();
              }

          }
      },SPLASH_TIME_OUT);


    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }
}