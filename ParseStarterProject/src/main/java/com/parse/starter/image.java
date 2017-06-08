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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.analytics.internal.zzy.e;

public class image extends AppCompatActivity {

    ImageView image;
    TextView authorText;
    String author;
    String imageID;
    TextView likes;
    ImageButton like;
    ImageButton dislike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        image = (ImageView) findViewById(R.id.imageSelected);
        authorText = (TextView) findViewById(R.id.imageAuthor);
        likes = (TextView) findViewById(R.id.likesNumber);
        like = (ImageButton) findViewById(R.id.likeOn);
        dislike = (ImageButton) findViewById(R.id.likeOff);

        Intent intent = getIntent();
        imageID = intent.getStringExtra("imageID");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Image");
        query.include("image").whereEqualTo("objectId",imageID);

        List<ParseObject> imageObjects = null;

        try{
            imageObjects = query.find();

            if (imageObjects.size() == 1) {

                ParseFile imageFile = (ParseFile) imageObjects.get(0).get("image");
                byte[] imageData = imageFile.getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                image.setImageBitmap(bitmap);

                author = imageObjects.get(0).getString("username");
                authorText.setText("Image by "+author);

            }

        } catch(Exception e){
            Log.i("Error in background", e.toString());
        }

        checkLikes();
        loadComments();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        if (author.equals(ParseUser.getCurrentUser().getUsername())){
            menuInflater.inflate(R.menu.image_menu, menu);
        } else {
            menuInflater.inflate(R.menu.profile_menu, menu);
        }
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
            case R.id.deletePicture:
                deletePic();
                intentGoHome = new Intent(getApplicationContext(), home.class);
                startActivity(intentGoHome);
                Log.i("Menu Item Selected", "Home");
                return true;
            default:
                return false;
        }
    }

    public void deletePic(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Image");
        query.include("image").whereEqualTo("objectId",imageID);

        List<ParseObject> imageObjects = null;

        try{
            imageObjects = query.find();

            if (imageObjects.size() == 1) {
                imageObjects.get(0).delete();
                Toast.makeText(this, "Image deleted", Toast.LENGTH_SHORT).show();
            }

        } catch(Exception e){
            Log.i("Error in background", e.toString());
        }
    }

    public void addLike(View view){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Image");
        query.include("image").whereEqualTo("objectId",imageID);

        List<ParseObject> imageObjects = null;

        try{
            imageObjects = query.find();

            imageObjects.get(0).add("likes",ParseUser.getCurrentUser().getUsername());
            imageObjects.get(0).save();

        } catch(Exception e){
            Log.i("Error in background", e.toString());
        }

        checkLikes();
    }

    public void removeLike(View view){
        List likesList = new ArrayList<String>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Image");
        query.include("image").whereEqualTo("objectId",imageID);

        List<ParseObject> imageObjects = null;

        try{
            imageObjects = query.find();

            likesList = imageObjects.get(0).getList("likes");
            imageObjects.get(0).remove("likes");
            imageObjects.get(0).save();


        } catch(Exception e){
            Log.i("Error in background", e.toString());
        }

        for (int i = 0; i < likesList.size(); i++){
            if (likesList.get(i).equals(ParseUser.getCurrentUser().getUsername())){
                likesList.remove(i);
                i--;
            } else {
                try{
                    imageObjects = query.find();

                    imageObjects.get(0).add("likes",likesList.get(i));
                    imageObjects.get(0).save();

                } catch(Exception e){
                    Log.i("Error in background", e.toString());
                }
            }
        }

        checkLikes();
    }

    public void checkLikes(){
        List likesList = new ArrayList<String>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Image");
        query.include("image").whereEqualTo("objectId",imageID);

        List<ParseObject> imageObjects = null;

        try{
            imageObjects = query.find();
            likesList = imageObjects.get(0).getList("likes");

        } catch(Exception e){
            Log.i("Error in background", e.toString());
        }

        likes.setText(String.valueOf(likesList.size()-1));

        for (int i = 0; i < likesList.size(); i++){
            if (likesList.get(i).equals(ParseUser.getCurrentUser().getUsername())){
                like.setVisibility(View.VISIBLE);
                dislike.setVisibility(View.INVISIBLE);
            } else {
                like.setVisibility(View.INVISIBLE);
                dislike.setVisibility(View.VISIBLE);
            }
        }
    }

    public void writeMSG(View view){
        ListView commentList = (ListView) findViewById(R.id.commentList);
        EditText commentText = (EditText) findViewById(R.id.commentText);
        ImageButton sendComment = (ImageButton) findViewById(R.id.sendComment);

        commentList.setVisibility(View.INVISIBLE);
        commentText.setVisibility(View.VISIBLE);
        sendComment.setVisibility(View.VISIBLE);
    }

    public void sendMSG(View view){
        ListView commentList = (ListView) findViewById(R.id.commentList);
        EditText commentText = (EditText) findViewById(R.id.commentText);
        ImageButton sendComment = (ImageButton) findViewById(R.id.sendComment);

        Intent intent = getIntent();
        String imageID = intent.getStringExtra("imageID");

        String text = commentText.getText().toString();

        if (!text.equals("")) {

            ParseObject object = new ParseObject("Comment");
            object.put("username", ParseUser.getCurrentUser().getUsername());
            object.put("comment", text);
            object.put("imageID", imageID);

            try {
                object.save();
            } catch(Exception e){
                Log.i("Error in background", e.toString());
            }

            commentList.setVisibility(View.VISIBLE);
            commentText.setVisibility(View.INVISIBLE);
            sendComment.setVisibility(View.INVISIBLE);

            loadComments();

        } else {
            Toast.makeText(image.this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();

        }
    }

    public void loadComments(){

        Intent intent = getIntent();
        String imageID = intent.getStringExtra("imageID");
        ListView commentList = (ListView) findViewById(R.id.commentList);

        final ArrayList<String> comments = new ArrayList<String>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment");
        query.include("username");
        query.orderByDescending("createdAt");
        query.whereEqualTo("imageID",imageID);

        List<ParseObject> commentObjects = null;

        try{
            commentObjects = query.find();
            if (commentObjects.size()>0){
                for (ParseObject object : commentObjects){
                    comments.add(object.get("username") + ": " + object.getString("comment"));
                }
            }
        } catch(Exception e){
            Log.i("Error in background", e.toString());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, comments);

        commentList.setAdapter(arrayAdapter);
    }
}