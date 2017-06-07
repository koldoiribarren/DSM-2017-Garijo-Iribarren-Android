package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class userList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        ListView usersListview = (ListView) findViewById(R.id.userList);

        final ArrayList<String> users = new ArrayList<String>();

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.include("username");
        query.orderByAscending("username");

        List<ParseUser> userObjects = null;

        try{
            userObjects = query.find();
            if (userObjects.size()>0){
                for (ParseUser object : userObjects){
                    if(!object.getString("username").equals(ParseUser.getCurrentUser().getUsername())) {
                        users.add(object.getString("username"));
                    }
                }
            }
        } catch(Exception e){
            Log.i("Error in background", e.toString());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users);

        usersListview.setAdapter(arrayAdapter);

        usersListview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intentProfile = new Intent(getApplicationContext(), profile.class);
                intentProfile.putExtra("username", users.get(position));
                startActivity(intentProfile);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.user_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.goHome:
                Intent intentGoHome = new Intent(getApplicationContext(), home.class);
                startActivity(intentGoHome);
                Log.i("Menu Item Selected", "Home");
                return true;
            case R.id.uploadPicture:
                Intent intentUpload = new Intent(getApplicationContext(), load.class);
                startActivity(intentUpload);
                Log.i("Menu Item Selected", "Upload Picture");
                return true;
            case R.id.profile:
                Intent intentProfile = new Intent(getApplicationContext(), profile.class);
                intentProfile.putExtra("username",ParseUser.getCurrentUser().getUsername());
                startActivity(intentProfile);
                Log.i("Menu Item Selected", "User Profile");
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
