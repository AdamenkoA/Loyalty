package com.example.adamenko.loyalty.Content;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class TopicContent {

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public TopicContent() {
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Boolean subscribe) {
        this.subscribe = subscribe;
    }

    private Integer id;
    private String title;
    private String description;
    private Boolean subscribe;
    private String color;

    public TopicContent(Integer id, String title, String description, Boolean subscribe, String color) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.subscribe = subscribe;
        this.color = color;
    }

    @Override
    public String toString() {
        return description;
    }

}
