package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void goToSignUp(View view){
        Intent intent = new Intent(getApplicationContext(), signup.class);
        startActivity(intent);
    }

    public void liSend(View view){

        EditText nick = (EditText) findViewById(R.id.liNick);
        EditText pass = (EditText) findViewById(R.id.liPass);

        ParseUser.logInInBackground(nick.getText().toString(), pass.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null){
                    Log.i("Log In", "Successful");
                    Intent intent = new Intent(getApplicationContext(), home.class);
                    startActivity(intent);
                } else {
                    Log.i("Log In", "Failed: " + e.toString());
                    Toast.makeText(login.this, "Error: Wrong user or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
