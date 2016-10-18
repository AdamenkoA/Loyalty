package com.example.adamenko.loyalty.Activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.example.adamenko.loyalty.DataBase.MySQLiteHelper;
import com.example.adamenko.loyalty.Fragments.EventsFragment;
import com.example.adamenko.loyalty.Fragments.HomeFragment;
import com.example.adamenko.loyalty.Fragments.TopicFragment;
import com.example.adamenko.loyalty.OnMyRequestListener;
import com.example.adamenko.loyalty.R;
import com.example.adamenko.loyalty.Request.RequestToHeroku;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.HashMap;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, EventsFragment.OnListFragmentInteractionListener, TopicFragment.OnListFragmentClickListener {
    private String barCode = "";
    private StringCrypter crypter;
    private MySQLiteHelper db;
    Context mContext;

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
        MySQLiteHelper mlh = new MySQLiteHelper(this);

        if (barcode_data.equals("")) {
            barcode_data = crypter.decrypt(mlh.getSettings("BarCode"));
        }

        barCode = barcode_data;
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        mContext = this;
        try {
            fragment = HomeFragment.class.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        bundle.putString("message", barcode_data);
        assert fragment != null;
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        drawer.closeDrawer(GravityCompat.START);

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
            case R.id.home_menu:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.event_menu:
                fragmentClass = EventsFragment.class;
                break;
            case R.id.topic_menu:
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
        assert fragment != null;
        fragment.setArguments(bundle);

        setTitle(item.getTitle());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        item.setChecked(true);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentClickListener(final TopicContent item) {

    }

    @Override
    public void onListFragmentInteraction(EventContent item) {

    }

    @Override
    public void onListFragmentLongClickListener(final TopicContent item, final Boolean mFlag, final TopicFragment.OnDialogClick mClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String question = "";
        final String colorBlack = "#000000";
        question = mFlag ? getResources().getString(R.string.subscribe_to_topic) : getResources().getString(R.string.unsubscribe_to_topic);

        if (!isOnline()) {
            question = getResources().getString(R.string.no_internet_connection);
            builder.setPositiveButton(R.string.subscribe_yes,new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        }
        else {
            builder.setPositiveButton(R.string.subscribe_yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    FirebaseMessaging.getInstance().subscribeToTopic("topic_" + item.getId());

                    MySQLiteHelper db = new MySQLiteHelper(mContext);
                    item.setSubscribe(mFlag);
                    db.updateTopics(item);

                    HashMap<String, String> param = new HashMap<>();
                    param.put("app", db.getSettings("app"));
                    param.put("contact", barCode);
                    param.put("topic", item.getId() + "");


                    RequestToHeroku rth = new RequestToHeroku();
                    rth.HerokuPost(param, "subscribe", new OnMyRequestListener() {
                        @Override
                        public void onSuccess(JSONObject valueTrue) {
                            if (mFlag) {
                                mClickListener.onDialogClick(true, mContext, item.getColor());
                            } else mClickListener.onDialogClick(true, mContext, colorBlack);
                        }

                        @Override
                        public void onFailure(String value) {
                            mClickListener.onDialogClick(false, mContext, colorBlack);
                        }
                    });
                }
            }).setNegativeButton(R.string.subscribe_no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
        }
        builder.setMessage(question);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
