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

import java.util.ArrayList;
import java.util.List;

public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = R.string.file_name + "DB";
    private static final String TABLE_SETTINGS = "Settings";
    private static final String TABLE_EVENTS = "Events";
    private static final String TABLE_TOPICS = "Topics";
    private static final String KEY_ID = "id";
    private static final String KEY_TOPIC_ID = "topicId";
    private static final String KEY_KEY = "key";
    private static final String KEY_VALUE = "value";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_SUBSCRIBE = "subscribe";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private String CREATE_TABLE_SETTINGS = "";
    private String CREATE_TABLE_EVENTS = "";
    private String CREATE_TABLE_TOPICS = "";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        CREATE_TABLE_SETTINGS = context.getResources().getString(R.string.DB_Settings);
        CREATE_TABLE_EVENTS = context.getResources().getString(R.string.DB_Events);
        CREATE_TABLE_TOPICS = context.getResources().getString(R.string.DB_Topics);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
            db.execSQL(CREATE_TABLE_SETTINGS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
            db.execSQL(CREATE_TABLE_EVENTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPICS);
            db.execSQL(CREATE_TABLE_TOPICS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPICS);
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
        db.insert(TABLE_TOPICS, // table
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
        db.insert(TABLE_EVENTS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        db.close();
    }

    public String getSettings(String key) {
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        String sc = "";
        // 2. build query
        Cursor cursor =
                db.query(TABLE_SETTINGS, // a. table
                        new String[]{KEY_VALUE}, // b. column names
                        "key = " + "\'" + String.valueOf(key) + "\'", // c. selections
                        null, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            sc = cursor.getString(0);
        }
        return sc;
    }

    public List<TopicContent> getTopics() {
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        List<TopicContent> topics = new ArrayList<>();
        // 2. build query
        Cursor cursor =
                db.query(TABLE_TOPICS, // a. table
                        new String[]{"*"}, // b. column names
                        null, // c. selections
                        null, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                topics.add(new TopicContent(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), false));
            }

        }
        return topics;
    }

    public List<EventContent> getEvents() {
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        List<EventContent> event = new ArrayList<>();
        // 2. build query
        Cursor cursor =
                db.query(TABLE_EVENTS, // a. table
                        new String[]{"*"}, // b. column names
                        null, // c. selections
                        null, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                event.add(new EventContent(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)));

            }

        }
        return event;
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

    public int updateEvents(EventContent event) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ID, event.getId());
        values.put(KEY_DESCRIPTION, event.getDescription());
        values.put(KEY_TITLE, event.getTitle());
        values.put(KEY_DATE, event.getDate());
        values.put(KEY_TIME, event.getTitle());
        values.put(KEY_TOPIC_ID, event.getTopicId());

        // 3. updating row
        int i = db.update(TABLE_EVENTS, //table
                values, // column/value
                KEY_ID + " = ?", // selections
                new String[]{String.valueOf(event.getId())}); //selection args

        // 4. close
        db.close();
        return i;
    }

    public int updateTopics(TopicContent topic) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();

        values.put(KEY_ID, topic.getId());
        values.put(KEY_TITLE, topic.getTitle());
        values.put(KEY_DESCRIPTION, topic.getDescription());
        values.put(KEY_SUBSCRIBE, topic.getSubscribe());

        // 3. updating row
        int i = db.update(TABLE_SETTINGS, //table
                values, // column/value
                KEY_ID + " = ?", // selections
                new String[]{String.valueOf(topic.getId())}); //selection args

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

    public void deleteEvents(EventContent event) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_EVENTS, //table name
                KEY_ID + " = ?",  // selections
                new String[]{String.valueOf(event.getId())}); //selections args
        // 3. close
        db.close();
    }

    public void deleteTopic(TopicContent topic) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_TOPICS, //table name
                KEY_ID + " = ?",  // selections
                new String[]{String.valueOf(topic.getId())}); //selections args
        // 3. close
        db.close();
    }

}