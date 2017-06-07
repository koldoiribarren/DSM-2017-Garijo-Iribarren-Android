package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class profile extends AppCompatActivity {

    String user;

    ArrayList<ImageView> imageList;
    String[] imageIDs;
    TextView pageNumber;
    Button prevPage;
    Button nextPage;

    int numPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        String user = intent.getStringExtra("username");
        if (user.equals(ParseUser.getCurrentUser().getUsername())){
            TextView profileName = (TextView) findViewById(R.id.userName);
            profileName.setText("Your images");
        } else {
            TextView profileName = (TextView) findViewById(R.id.userName);
            profileName.setText(user+"'s images");
        }

        imageList = new ArrayList<ImageView>();
        pageNumber = (TextView) findViewById(R.id.pageNumber);
        prevPage = (Button) findViewById(R.id.prevPage);
        nextPage = (Button) findViewById(R.id.nextPage);

        numPage = 0;

        imageList.add((ImageView) findViewById(R.id.pic1));
        imageList.add((ImageView) findViewById(R.id.pic2));
        imageList.add((ImageView) findViewById(R.id.pic3));
        imageList.add((ImageView) findViewById(R.id.pic4));
        imageList.add((ImageView) findViewById(R.id.pic5));
        imageList.add((ImageView) findViewById(R.id.pic6));
        imageList.add((ImageView) findViewById(R.id.pic7));
        imageList.add((ImageView) findViewById(R.id.pic8));
        imageList.add((ImageView) findViewById(R.id.pic9));
        imageList.add((ImageView) findViewById(R.id.pic10));
        imageList.add((ImageView) findViewById(R.id.pic11));
        imageList.add((ImageView) findViewById(R.id.pic12));

        imageIDs = new String[12];

        loadContent();

    }

    protected void setNumPage(View view){
        switch (view.getTag().toString()){
            case "-1":
                numPage -= 1;
                break;
            case "1":
                numPage +=1;
                break;
            default:
                break;
        }

        loadContent();
    }

    protected void loadContent(){

        Intent intent = getIntent();
        String user = intent.getStringExtra("username");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Image");
        query.include("image").whereEqualTo("username",user);

        List<ParseObject> imageObjects = null;

        try{
            imageObjects = query.find();
            if (imageObjects.size() >= numPage*12 && imageObjects.size() > (numPage+1)*12) {

                Log.i("Numero de imágenes", ">12");
                Log.i("Número de imágenes", String.valueOf(imageObjects.size()));

                for (int i = numPage*12; i < (numPage+1)*12; i++) {

                    ParseFile imageFile = (ParseFile) imageObjects.get(i).get("image");
                    byte[] imageData = imageFile.getData();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                    imageList.get(i%12).setImageBitmap(bitmap);
                    imageIDs[i%12] = imageObjects.get(i).getObjectId();
                    imageList.get(i%12).setVisibility(View.VISIBLE);
                    imageList.get(i%12).setClickable(true);
                }

                if (numPage == 0){
                    prevPage.setVisibility(View.INVISIBLE);
                    nextPage.setVisibility(View.VISIBLE);
                } else {
                    prevPage.setVisibility(View.VISIBLE);
                    nextPage.setVisibility(View.VISIBLE);
                }

            } else if (imageObjects.size() >= numPage*12 && imageObjects.size() <= (numPage+1)*12){

                for (int i = numPage*12; i < imageObjects.size(); i++) {

                    ParseFile imageFile = (ParseFile) imageObjects.get(i).get("image");
                    byte[] imageData = imageFile.getData();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                    imageList.get(i%12).setImageBitmap(bitmap);
                    imageIDs[i%12] = imageObjects.get(i).getObjectId();
                    imageList.get(i%12).setClickable(true);
                }
                for (int i = imageObjects.size(); i < (numPage+1)*12; i++){
                    imageList.get(i%12).setVisibility(View.INVISIBLE);
                    imageList.get(i%12).setClickable(false);
                }

                if (numPage == 0){
                    prevPage.setVisibility(View.INVISIBLE);
                    nextPage.setVisibility(View.INVISIBLE);
                } else {
                    prevPage.setVisibility(View.VISIBLE);
                    nextPage.setVisibility(View.INVISIBLE);
                }
            }
        }  catch(Exception e){
            Log.i("Error in background", e.toString());
        }
        pageNumber.setText(String.valueOf(numPage+1));
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
                return true;
            case R.id.uploadPicture:
                Intent intentUpload = new Intent(getApplicationContext(), load.class);
                startActivity(intentUpload);
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

    public void selectImage(View view){
        int position = Integer.parseInt(view.getTag().toString());

        Intent intentImage = new Intent(getApplicationContext(), image.class);
        intentImage.putExtra("imageID",imageIDs[position]);
        startActivity(intentImage);
    }

}
