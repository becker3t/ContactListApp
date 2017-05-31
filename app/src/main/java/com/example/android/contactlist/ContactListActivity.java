package com.example.android.contactlist;

//import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {

    private static final String TAG = "ContactListActivity";

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //check if all fields filled here
            switch (v.getId()){
                case R.id.backToMainBtn:
                    BackToMainActivity();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        TextView contactList = (TextView) findViewById(R.id.displayContactsTv);

        Button backToMainBtn = (Button) findViewById(R.id.backToMainBtn);
        backToMainBtn.setOnClickListener(listener);

        SQLiteHelper dbHelper = new SQLiteHelper(getApplicationContext());
        ArrayList<Contact> contacts = dbHelper.readValuesInDb();

        if(contacts.size() > 0) {
            contactList.setText("");
            for (Contact c : contacts) {
                contactList.append("First Name: " + c.getFirstName() + "\n" +
                                    "Last Name: " + c.getLastName() + "\n" +
                                    "Photo Path: " + c.getPhotoPath() + "\n\n");
            }
        }
        else {
            contactList.setText(getString(R.string.emptyContacts));
        }
    }

    private void BackToMainActivity() {
        Intent listActIntent = new Intent(ContactListActivity.this, MainActivity.class);
        startActivity(listActIntent);
    }
}
