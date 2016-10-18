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
    private static final String KEY_COLOR = "color";
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

    public void updateSettings(SettingsContent settingsContent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VALUE, settingsContent.getValue());
        values.put(KEY_KEY, settingsContent.getKey());
        int i = db.update(TABLE_SETTINGS,
                values,
                KEY_ID + " = ?",
                new String[]{String.valueOf(settingsContent.getId())});
        db.close();
    }

    public String getSettings(String key) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sc = "";
        Cursor cursor =
                db.query(TABLE_SETTINGS,
                        new String[]{KEY_VALUE},
                        "key = " + "\'" + String.valueOf(key) + "\'",
                        null, null, null, null, null);
        if ((cursor != null ? cursor.getCount() : 0) != 0) {
            cursor.moveToFirst();
            sc = cursor.getString(0);
        }
        return sc;
    }

    public void deleteSettings(SettingsContent settingsContent) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SETTINGS,
                KEY_ID + " = ?",
                new String[]{String.valueOf(settingsContent.getId())});
        db.close();
    }

    public void addTopics(TopicContent topic) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, topic.getId());
        values.put(KEY_TITLE, topic.getTitle());
        values.put(KEY_DESCRIPTION, topic.getDescription());
        values.put(KEY_SUBSCRIBE, topic.getSubscribe());
        values.put(KEY_COLOR, topic.getColor());
        if (getId(TABLE_TOPICS, topic.getId() + "", db)) {
            db.insert(TABLE_TOPICS,
                    null,
                    values);
        } else
            db.update(TABLE_TOPICS,
                    values,
                    KEY_ID + " = ?",
                    new String[]{topic.getId() + ""});
        db.close();
    }

    public int updateTopics(TopicContent topic) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, topic.getId());
        values.put(KEY_TITLE, topic.getTitle());
        values.put(KEY_DESCRIPTION, topic.getDescription());
        values.put(KEY_SUBSCRIBE, topic.getSubscribe());
        values.put(KEY_COLOR, topic.getSubscribe());
        int i = db.update(TABLE_TOPICS,
                values,
                KEY_ID + " = ?",
                new String[]{String.valueOf(topic.getId())});
        db.close();
        return i;
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

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndex(KEY_ID));
                String title = cursor.getString(cursor.getColumnIndex(KEY_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION));
                Boolean subscribe = cursor.getInt(cursor.getColumnIndex(KEY_SUBSCRIBE)) > 0;
                String color = cursor.getString(cursor.getColumnIndex(KEY_COLOR));

                topics.add(new TopicContent(Integer.parseInt(id), title, description, subscribe, color));
                cursor.moveToNext();
            }
        }

        return topics;
    }

    public TopicContent getTopic(String topicId) {

        SQLiteDatabase db = this.getReadableDatabase();
        String id = "";
        String title = "";
        String description = "";
        Boolean subscribe = false;
        String color = "";
        Cursor cursor =
                db.query(TABLE_TOPICS, // a. table
                        new String[]{"*"}, // c. selections
                        KEY_ID + "= ?", // b. column names
                        new String[]{topicId + ""}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndex(KEY_ID));
            title = cursor.getString(cursor.getColumnIndex(KEY_TITLE));
            description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION));
            subscribe = cursor.getInt(cursor.getColumnIndex(KEY_SUBSCRIBE)) > 0;
            color = cursor.getString(cursor.getColumnIndex(KEY_COLOR));
        }
        return new TopicContent(Integer.parseInt(id), title, description, subscribe, color);
    }

    public void deleteTopic(TopicContent topic) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TOPICS, //table name
                KEY_ID + " = ?",  // selections
                new String[]{String.valueOf(topic.getId())}); //selections args
        // 3. close
        db.close();
    }

    public void deleteAllTopic() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TOPICS, null, null);
        db.close();
    }

    public void addEvents(EventContent event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, event.getId());
        values.put(KEY_DESCRIPTION, event.getDescription());
        values.put(KEY_TITLE, event.getTitle());
        values.put(KEY_DATE, event.getDate());
        values.put(KEY_TIME, event.getTime());
        values.put(KEY_TOPIC_ID, event.getTopicId());
        if (getId(TABLE_EVENTS, event.getId(), db)) {
            db.insert(TABLE_EVENTS,
                    null,
                    values);
        } else
            db.update(TABLE_EVENTS,
                    values,
                    KEY_ID + " = ?",
                    new String[]{String.valueOf(event.getId())});
        db.close();
    }

    public List<EventContent> getEvents() {
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        List<EventContent> event = new ArrayList<>();
        // 2. build query
        Cursor cursor =
                db.query(TABLE_EVENTS,
                        new String[]{"*"},
                        null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndex(KEY_ID));
                String title = cursor.getString(cursor.getColumnIndex(KEY_TITLE));
                String descrpition = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndex(KEY_DATE));
                String time = cursor.getString(cursor.getColumnIndex(KEY_TIME));
                String topic_id = cursor.getString(cursor.getColumnIndex(KEY_TOPIC_ID));
                String color = cursor.getString(cursor.getColumnIndex(KEY_COLOR));
                if (color==null){
                    color=getTopic(topic_id).getColor();
                }
                event.add(new EventContent(id, topic_id, date, time, title, descrpition, color));
                cursor.moveToNext();
            }
        }

        return event;
    }

    public int updateEvents(EventContent event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, event.getId());
        values.put(KEY_DESCRIPTION, event.getDescription());
        values.put(KEY_TITLE, event.getTitle());
        values.put(KEY_DATE, event.getDate());
        values.put(KEY_TIME, event.getTitle());
        values.put(KEY_TOPIC_ID, event.getTopicId());
        int i = db.update(TABLE_EVENTS,
                values,
                KEY_ID + " = ?",
                new String[]{String.valueOf(event.getId())});
        db.close();
        return i;
    }

    public void deleteEvents(EventContent event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS,
                KEY_ID + " = ?",
                new String[]{String.valueOf(event.getId())});
        db.close();
    }

    public void deleteAllEvents() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, null, null);
        db.close();
    }

    private boolean getId(String table, String id, SQLiteDatabase db) {
        Cursor cursor =
                db.query(table,
                        new String[]{KEY_ID},
                        KEY_ID + "= ?",
                        new String[]{id}, null, null, null, null);
        return cursor.getCount() <= 0;
    }

    public boolean getSubscribe(Integer id, SQLiteDatabase db) {
        Boolean result = false;
        Cursor cursor =
                db.query(TABLE_TOPICS,
                        new String[]{KEY_SUBSCRIBE},
                        KEY_ID + "= ?",
                        new String[]{id + ""}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            result = cursor.getInt(cursor.getColumnIndex(KEY_SUBSCRIBE)) > 0;
        }
        return result;
    }

}