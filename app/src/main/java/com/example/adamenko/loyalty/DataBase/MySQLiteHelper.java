package com.example.adamenko.loyalty.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.adamenko.loyalty.Content.EventContent;
import com.example.adamenko.loyalty.Content.SettingsContent;
import com.example.adamenko.loyalty.Content.TopicContent;
import com.example.adamenko.loyalty.R;

/**
 * Created by Adamenko on 03.10.2016.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = R.string.file_name + "DB";
    private Integer TABLE_NAME;
    private static final String TABLE_SETTINGS = "Settings";
    private static final String TABLE_EVENTS = "Events";
    private static final String TABLE_TOPICS = "Topics";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TOPIC_ID = "topicId";
    private static final String KEY_KEY = "key";
    private static final String KEY_VALUE = "value";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_SUBSCRIBE = "subscribe";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";

    private static final String[] COLUMNS = {KEY_ID, KEY_TITLE};
  public  String CREATE_TABLE="";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        CREATE_TABLE=context.getResources().getString(R.string.DB_Settings);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
            db.execSQL(CREATE_TABLE);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        // create fresh books table
        this.onCreate(db);
    }

    public void addSettings(SettingsContent setting) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_KEY, setting.getKey());
        values.put(KEY_VALUE, setting.getValue());
        db.insert(TABLE_SETTINGS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        db.close();
    }

    public void addTopics(TopicContent topic) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, topic.getId());
        values.put(KEY_TITLE, topic.getTitle());
        values.put(KEY_DESCRIPTION, topic.getDescription());
        values.put(KEY_SUBSCRIBE, topic.getSubscribe());
        db.insert(TABLE_SETTINGS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        db.close();
    }

    public void addEvents(EventContent event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, event.getId());
        values.put(KEY_DESCRIPTION, event.getDescription());
        values.put(KEY_TITLE, event.getTitle());
        values.put(KEY_DATE, event.getDate());
        values.put(KEY_TIME, event.getTitle());
        values.put(KEY_TOPIC_ID, event.getTopicId());
        db.insert(TABLE_SETTINGS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        db.close();
    }

    public SettingsContent getSettings(String key) {
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        // 2. build query
        Cursor cursor =
                db.query(TABLE_SETTINGS, // a. table
                        COLUMNS, // b. column names
                        " key = ?", // c. selections
                        new String[]{String.valueOf(key)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();
        SettingsContent sc = new SettingsContent();
        sc.setId(Integer.parseInt(cursor.getString(0)));
        sc.setKey(cursor.getString(1));
        sc.setValue(cursor.getString(2));

        return sc;
    }

    public int updateSettings(SettingsContent settingsContent) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_VALUE, settingsContent.getValue()); // get title
        values.put(KEY_KEY, settingsContent.getKey()); // get author

        // 3. updating row
        int i = db.update(TABLE_SETTINGS, //table
                values, // column/value
                KEY_ID + " = ?", // selections
                new String[]{String.valueOf(settingsContent.getId())}); //selection args

        // 4. close
        db.close();
        return i;
    }

    public void deleteSettings(SettingsContent settingsContent) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_SETTINGS, //table name
                KEY_ID + " = ?",  // selections
                new String[]{String.valueOf(settingsContent.getId())}); //selections args

        // 3. close
        db.close();

    }


}