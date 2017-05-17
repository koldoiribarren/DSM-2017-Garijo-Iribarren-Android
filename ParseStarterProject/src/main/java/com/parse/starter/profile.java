package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;

public class profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        String user =  ParseUser.getCurrentUser().getUsername();
        TextView profileName = (TextView) findViewById(R.id.pTitle);
        profileName.setText(user);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.goHome:
                Intent intentGoHome = new Intent(getApplicationContext(), home.class);
                startActivity(intentGoHome);
                Log.i("Menu Item Selected", "Home");
            case R.id.uploadPicture:
                Log.i("Menu Item Selected", "Upload Picture");
                return true;
            case R.id.userList:
                Intent intentUserList = new Intent(getApplicationContext(), userList.class);
                startActivity(intentUserList);
                Log.i("Menu Item Selected", "User List");
                return true;
            case R.id.logOut:
                ParseUser.logOut();
                Log.i("Logout check", "Logout succeeded");
                Intent intentLogIn = new Intent(getApplicationContext(), login.class);
                startActivity(intentLogIn);
                Log.i("Menu Item Selected", "Log Out");
                return true;
            default:
                return false;
        }
    }
}
