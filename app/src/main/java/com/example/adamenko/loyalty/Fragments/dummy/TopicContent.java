package com.example.adamenko.loyalty.Fragments.dummy;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class TopicContent {


    public final String id;
    public final String content;
    public final String details;

    public TopicContent(String id, String content, String details) {
        this.id = id;
        this.content = content;
        this.details = details;
    }

    @Override
    public String toString() {
        return content;
    }

}
