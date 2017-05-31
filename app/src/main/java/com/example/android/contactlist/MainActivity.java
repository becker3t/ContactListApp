package com.example.android.contactlist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;

    private EditText firstNameEt;
    private EditText lastNameEt;
    private ImageView contactImgVw;

    private boolean pictureTaken;

    private Uri mCurrentPhotoURI;

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.photoBtn:
                    TakePhoto();
                    break;
                case R.id.saveBtn:
                    if(AllFieldsFilled()) {
                        SaveToDb();
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.fillAllFieldsMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    break;
                case R.id.listBtn:
                    StartListActivity();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstNameEt = (EditText) findViewById(R.id.firstNameET);
        lastNameEt = (EditText) findViewById(R.id.lastNameET);

        contactImgVw = (ImageView) findViewById(R.id.contactImgView);

        Button photoBtn = (Button) findViewById(R.id.photoBtn);
        Button saveBtn = (Button) findViewById(R.id.saveBtn);
        Button listBtn = (Button) findViewById(R.id.listBtn);

        photoBtn.setOnClickListener(listener);
        saveBtn.setOnClickListener(listener);
        listBtn.setOnClickListener(listener);

        pictureTaken = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                InputStream image_stream = getContentResolver().openInputStream(mCurrentPhotoURI);
                Bitmap imageBitmap = BitmapFactory.decodeStream(image_stream);
                contactImgVw.setImageBitmap(imageBitmap);
                pictureTaken = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean AllFieldsFilled() {
        return pictureTaken && firstNameEt.length() > 0 && lastNameEt.length() > 0;
    }

    private void StartListActivity() {
        Intent listActIntent = new Intent(MainActivity.this, ContactListActivity.class);
        startActivity(listActIntent);
    }

    private void TakePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.contactlist",
                        photoFile);

//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                mCurrentPhotoURI = photoURI;
                Log.d(TAG, "TakePhoto: Photo created at '" + mCurrentPhotoURI.getPath() + "'");
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    private void SaveToDb() {
        SQLiteHelper dbHelper = new SQLiteHelper(getApplicationContext());
        Contact newContact = new Contact(firstNameEt.getText().toString(), lastNameEt.getText().toString(), mCurrentPhotoURI.getPath());
        dbHelper.placeValuesInDb(newContact);
    }
}
