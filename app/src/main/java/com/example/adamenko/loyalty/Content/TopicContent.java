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

    private Integer id;

    public TopicContent() {
    }

    private String title;
    private String description;

    public TopicContent(Integer id, String title, String description, Boolean subscribe) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.subscribe = subscribe;
    }

    private Boolean subscribe;

    public Boolean getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Boolean subscribe) {
        this.subscribe = subscribe;
    }

    @Override
    public String toString() {
        return description;
    }

}
