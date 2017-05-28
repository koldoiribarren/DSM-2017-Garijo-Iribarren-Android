/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Random;


public class MainActivity extends AppCompatActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      Handler handler = new Handler();
      Runnable run1 = new Runnable(){
          public void run(){
              Intent intent = new Intent(getApplicationContext(), home.class);
              startActivity(intent);
          }
      };
      Runnable run2 = new Runnable(){
          public void run(){
              Intent intent = new Intent(getApplicationContext(), login.class);
              startActivity(intent);
          }
      };

      Random rand = new Random();
      int value = rand.nextInt(10);

      String[] sentences = {
              "Loading...",
              "A.K.A. Fakestagram",
              "This isn\'t actually loading anything",
              "IVAN EHT NIOJ",
              "I think this app deserves a 10/10",
              "Have fun",
              "I can see you... ;)",
              "And rolls, and rolls...",
              "- Critical Error: All files erased -",
              "You should be doing something more productive"
      };

      ParseAnalytics.trackAppOpenedInBackground(getIntent());

      if (ParseUser.getCurrentUser().getUsername()!=null) {

          Log.i("Current User", "User: " + ParseUser.getCurrentUser());
          Log.i("Current User Name", "User name: " + ParseUser.getCurrentUser().getUsername());

          ImageView loading = (ImageView) findViewById(R.id.loadingIcon);
          loading.animate().rotation(-1080).setDuration(3000);

          TextView welcomeMSG = (TextView) findViewById(R.id.loadingText);
          welcomeMSG.setText("Welcome back");

          handler.postDelayed(run1,2000);

      } else {

          ImageView loading = (ImageView) findViewById(R.id.loadingIcon);
          loading.animate().rotation(-1800).setDuration(6000);

          TextView welcomeMSG = (TextView) findViewById(R.id.loadingText);
          welcomeMSG.setText(sentences[value]);

          handler.postDelayed(run2,5000);

      }
  }
}

