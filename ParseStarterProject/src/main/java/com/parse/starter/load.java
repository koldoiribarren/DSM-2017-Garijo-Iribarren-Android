package com.parse.starter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class load extends AppCompatActivity {

    public void getPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);{
                getPhoto();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            getPhoto();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                byte[] byteArray = stream.toByteArray();
                ParseFile file = new ParseFile("image.png", byteArray);
                ParseObject object = new ParseObject("Image");
                object.put("image", file);
                object.put("username", ParseUser.getCurrentUser().getUsername());
                object.add("likes",null);

                object.saveInBackground(new SaveCallback(){
                    @Override
                    public void done(com.parse.ParseException e) {
                        if(e == null){
                            Toast.makeText(load.this, "Image saved!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(load.this, "Image could not be saved. Please try again later :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (IOException e){
                e.printStackTrace();
            }
            Intent goHome = new Intent(getApplicationContext(), home.class);
            startActivity(goHome);
        }
    }
}
