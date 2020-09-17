package com.example.addcontacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editTextName;
    EditText editTextNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextNumber = (EditText)findViewById(R.id.editTextNumber);


        /*intializing phone spinner*/
        Spinner spinnerSaveType = (Spinner)findViewById(R.id.spinnerSaveType);
        String saveTypeArr[] = {"SIM1", "SIM2", "Phone"};
        ArrayAdapter<String> saveTypeSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, saveTypeArr);
        spinnerSaveType.setAdapter(saveTypeSpinnerAdapter);


        final Button saveButton = (Button)findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = ContactsContract.Data.CONTENT_URI;

                //add emty contact and get the generated id
                long rowContactID = getRawContactID();

                //Add contact name data
                String displayName = editTextName.getText().toString();
                insertContactDisplayName(uri, rowContactID, displayName);

                //Add contact number data
                String phoneNumber = editTextNumber.getText().toString();
                insertContactDisplayNumber(uri, rowContactID, phoneNumber);
                Toast.makeText(getApplicationContext(), "New Contact added", Toast.LENGTH_SHORT).show();
                //finish();
            }
        });
    }

    /*This method will only insert an empty data to the RawContacts.CONTENT_URI
    * The purpose is to get a system generated raw contact id*/
    private long getRawContactID() {
        ContentValues contentValues = new ContentValues();
        Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
        // get the newly created contact raw id
        long ret = ContentUris.parseId(rawContactUri);
        return ret;
    }

    private void insertContactDisplayName(Uri uri, long rawContactID, String displayName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactID);

        //each contact must be mime type to avoid error
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);

        //put contact display name value
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, displayName);
        getContentResolver().insert(uri, contentValues);


    }

    private void insertContactDisplayNumber(Uri uri, long rawContactID, String displayNumber) {
        ContentValues contentValues = new ContentValues();

        //each content must have an id to avoid illegal Argument Exception
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactID);
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);

        //put phone number values
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, displayNumber);

        getContentResolver().insert(uri, contentValues);

    }
}