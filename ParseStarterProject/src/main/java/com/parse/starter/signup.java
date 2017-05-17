package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import static com.google.android.gms.analytics.internal.zzy.e;

public class signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void goToLogIn(View view){
        Intent intent = new Intent(getApplicationContext(), login.class);
        startActivity(intent);
    }

    public void suSend(View view){

        ParseUser user = new ParseUser();
        EditText name = (EditText) findViewById(R.id.suName);
        EditText nick = (EditText) findViewById(R.id.suNick);
        EditText mail = (EditText) findViewById(R.id.suMail);
        EditText pass = (EditText) findViewById(R.id.suPass1);
        EditText pass2 = (EditText) findViewById(R.id.suPass2);

        Toast.makeText(signup.this, "P1: " + pass.getText().toString() + " P2: " + pass2.getText().toString(), Toast.LENGTH_SHORT).show();

        if (pass.getText().toString().equals(pass2.getText().toString())){

            user.setUsername(nick.getText().toString());
            user.setPassword(pass.getText().toString());
            user.setEmail(mail.getText().toString());

            user.signUpInBackground(new SignUpCallback() {

                public void done(ParseException e) {
                    if (e == null){
                        Log.i("Sign Up", "Succesful");
                        Intent intentGoHome = new Intent(getApplicationContext(), home.class);
                        startActivity(intentGoHome);
                    } else {
                        Log.i("Sign Up", "Failed: " + e.toString());
                    }
                }
            });

        } else {
            Toast.makeText(signup.this, "Error, different passwords", Toast.LENGTH_SHORT).show();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }
}
