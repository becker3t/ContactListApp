package com.example.android.contactlist;

/**
 * Created by Android on 5/30/2017.
 */

public class Contact {
    private String firstName;
    private String lastName;
    private String photoPath;

    public Contact (String first, String last, String path) {
        firstName = first;
        lastName = last;
        photoPath = path;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhotoPath() {
        return photoPath;
    }
}
