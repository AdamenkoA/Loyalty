package com.example.adamenko.loyalty.Content;

/**
 * Created by Adamenko on 29.09.2016.
 */

public class EventContent {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String id;
    private String topicId;
    private String date;
    private String time;
    private String title;
    private String description;

    public EventContent() {
    }

    public EventContent(String id, String topicId, String date, String time, String title, String description) {
        this.id = id;
        this.topicId = topicId;
        this.date = date;
        this.time = time;

        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return date + " " + title;
    }


}
