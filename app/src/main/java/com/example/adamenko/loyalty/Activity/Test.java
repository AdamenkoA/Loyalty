package com.example.adamenko.loyalty.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.adamenko.loyalty.Content.SettingsContent;
import com.example.adamenko.loyalty.DataBase.MySQLiteHelper;
import com.example.adamenko.loyalty.R;

/**
 * Created by Adamenko on 05.10.2016.
 */

public class Test extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MySQLiteHelper db = new MySQLiteHelper(this);

        String decryptedStr = "1010101";
        db.addSettings(new SettingsContent(1, "BarCode", decryptedStr));
        db.addSettings(new SettingsContent(2, "app", getString(R.string.app)));
     //   db.deleteAllEvents();
     // db.deleteAllTopic();
        Intent myIntent = new Intent(Test.this, Home.class);
        myIntent.putExtra("ITEM_ID", decryptedStr);
        startActivity(myIntent);
        finish();
    }

}

