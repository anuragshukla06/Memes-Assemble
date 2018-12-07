/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity {

  Button logButton;
  LinearLayout butttonLinearLayout;
  Boolean positionUp = true;
  TextView modeChangeTextView;
  TextView msgTextView;
  TextView confirmPasswordEditText;
  TextView usernameEditText;
  TextView passwordEditText;
  SharedPreferences sharedPreferences;
  boolean logInMode = true;


  void goToProfilePage(){
      Intent intent = new Intent(getApplicationContext(), profileActivity.class);
      startActivity(intent);
  }

  Boolean  doQueryForProfile(String username){
      ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
      query.whereEqualTo("username", username);
      try {
          List<ParseObject> objects = query.find();
          if(objects.size()>0){
              if(objects.get(0).getBoolean("isProfileCompelete")){
                  return true;
              } else{
                  return false;
              }
          } else{
              return false;
          }
      } catch (ParseException e) {
          Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
          return false;
      }
  }

    /* -----------------------------------------------------------------------------
       --------------------TextView tapping code------------------------------------
       ------------------------------Start------------------------------------------*/

  void modeChange(View view) {

      if (positionUp && view.getId() == R.id.modeChangeTextView) {
          butttonLinearLayout.animate().translationYBy(250);
          logButton.setText("Sign Up");
          modeChangeTextView.setText("or LogIn?");
          msgTextView.setText("Join Memers!");
          confirmPasswordEditText.animate().alpha(1).setListener(new AnimatorListenerAdapter() {
              @Override
              public void onAnimationStart(Animator animation) {
                  super.onAnimationStart(animation);
                  confirmPasswordEditText.setVisibility(View.VISIBLE);
              }
          });
          positionUp = false;
          logInMode = false;

      } else if (!positionUp && view.getId() == R.id.modeChangeTextView) {
          butttonLinearLayout.animate().translationYBy(-250);
          logButton.setText("Log In");
          modeChangeTextView.setText("or SignUp?");
          msgTextView.setText("Welcome Back");
          confirmPasswordEditText.animate().alpha(0).setListener(new AnimatorListenerAdapter() {
              @Override
              public void onAnimationEnd(Animator animation) {
                  super.onAnimationEnd(animation);
                  confirmPasswordEditText.setVisibility(View.INVISIBLE);
              }
          });
          logInMode = true;
          positionUp = true;
      }
  }

  /* -----------------------------------------------------------------------------
       --------------------TextView tapping code------------------------------------
       ------------------------------End------------------------------------------*/

    /* -----------------------------------------------------------------------------
       --------------------Button tapping code------------------------------------
       ------------------------------Start------------------------------------------*/

  void logButtonTapped(View view){
      final String username = usernameEditText.getText().toString();
      String password = passwordEditText.getText().toString();

      if(username.length() == 0 || password.length()==0){   //Check if both username and password exists.
          Toast.makeText(getApplicationContext(), "Username and Password are required!", Toast.LENGTH_SHORT).show();
      } else{

          if(logInMode) {  // login mode
              ParseUser.logInInBackground(username, password, new LogInCallback() {   //Login callback
                  @Override
                  public void done(ParseUser user, ParseException e) {
                      if (e == null) {
                          boolean isProfileCompelete;
                          isProfileCompelete = doQueryForProfile(username);

                          if(!isProfileCompelete){ //Profile Page Compelete After Login.
                              goToProfilePage();
                          } else {
                              Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                          }

                      } else {
                          Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                      }
                  }
              });
          } else {

              String confirmPassword = confirmPasswordEditText.getText().toString();

              if (!password.equals(confirmPassword)) {   // Checking if password and confirm password match.
                  Toast.makeText(getApplicationContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
              } else {
                  ParseUser user = new ParseUser();
                  user.setUsername(username);
                  user.setPassword(password);
                  user.signUpInBackground(new SignUpCallback() { // SignUp callback
                      @Override
                      public void done(ParseException e) {
                          if (e == null) {
                              //OK
                              Toast.makeText(getApplicationContext(), "SignUp Successfull", Toast.LENGTH_SHORT).show();
                              goToProfilePage();
                          } else {
                              Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                          }
                      }
                  });
              }
          }
      }
  }

  /* -----------------------------------------------------------------------------
       --------------------Button tapping code------------------------------------
       ------------------------------End------------------------------------------*/

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    logButton = (Button) findViewById(R.id.logButton);
    butttonLinearLayout = (LinearLayout) findViewById(R.id.buttonLinearLayout);
    modeChangeTextView = (TextView) findViewById(R.id.modeChangeTextView);
    msgTextView = (TextView) findViewById(R.id.msgTextView);
    sharedPreferences = getSharedPreferences("com.parse.starter", Context.MODE_PRIVATE);
    confirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditText);
    usernameEditText = (TextView) findViewById(R.id.usernameEditText);
    passwordEditText = (TextView) findViewById(R.id.passwordEditText);

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}