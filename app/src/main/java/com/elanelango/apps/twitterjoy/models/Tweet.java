package com.elanelango.apps.twitterjoy.models;

/**
 * Created by eelango on 2/20/16.
 */
public class Tweet {
    long id;
    String createdAt;
    String text;
    User user;

    public User getUser() {
        return user;
    }

    public long getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getText() {
        return text;
    }
}
