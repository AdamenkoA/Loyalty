package com.example.adamenko.loyalty.Activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.adamenko.loyalty.Content.EventContent;
import com.example.adamenko.loyalty.Content.TopicContent;
import com.example.adamenko.loyalty.Crypter.StringCrypter;
import com.example.adamenko.loyalty.Fragments.EventsFragment;
import com.example.adamenko.loyalty.Fragments.HomeFragment;
import com.example.adamenko.loyalty.Fragments.Subscribe;
import com.example.adamenko.loyalty.Fragments.TopicFragment;
import com.example.adamenko.loyalty.R;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TopicFragment.OnListFragmentInteractionListener, EventsFragment.OnListFragmentInteractionListener {


    private String fileName = R.string.file_name + "";
    private String barCode = "";
    private String strLine = "";
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private StringCrypter crypter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Intent intent = getIntent();
        String barcode_data = intent.getStringExtra("ITEM_ID");


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (barcode_data.equals("")) {
            try {
                File tempFile = new File(getBaseContext().getCacheDir().getPath() + "/" + fileName);
                FileReader fReader = new FileReader(tempFile);
                BufferedReader bReader = new BufferedReader(fReader);
                while ((strLine = bReader.readLine()) != null) {
                    barcode_data = crypter.decrypt(strLine).toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (barcode_data.equals("")) {
            barcode_data = "1010101";
        }
        barCode = barcode_data;
//        Bitmap bitmap = null;
//        ImageView iv = (ImageView) findViewById(R.id.bar_code_view);
//
//        try {
//            EncodeAsBitmap eab=new EncodeAsBitmap();
//            bitmap =  eab.encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 600, 300);
//            iv.setImageBitmap(bitmap);
//
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        Class fragmentClass;
        Bundle bundle = new Bundle();
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.nav_calendar:
                fragmentClass = EventsFragment.class;
                break;
            case R.id.nav_topics:
                fragmentClass = TopicFragment.class;
                break;
            default:
                fragmentClass = HomeFragment.class;

        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        bundle.putString("message", barCode);
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());
        // Close the navigation drawer

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(final TopicContent item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.subscribe_to_topic)
                .setTitle("Topic");

        builder.setPositiveButton(R.string.subscribe_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                FirebaseMessaging.getInstance().subscribeToTopic("topic_" + item.id);
                new Subscribe(barCode, item.id);
            }
        });
        builder.setNegativeButton(R.string.subscribe_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onListFragmentInteraction(EventContent item) {

    }
}
