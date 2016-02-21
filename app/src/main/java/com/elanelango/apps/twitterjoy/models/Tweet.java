package com.elanelango.apps.twitterjoy.models;

import android.util.Log;

import java.util.Date;

/**
 * Created by eelango on 2/20/16.
 */
public class Tweet {
    long id;
    Date createdAt;
    String text;
    User user;

    public User getUser() {
        return user;
    }

    public long getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getText() {
        return text;
    }

    public String getRelativeTime() {
        long currTime = System.currentTimeMillis() / 1000L;
        int diffTime = (int) (currTime - (createdAt.getTime() / 1000L));
        if (diffTime < 60)
            return diffTime + "s";
        else if (diffTime < 3600)
            return (diffTime / 60)  + "m";
        else if (diffTime < 86400)
            return (diffTime / 3600) + "h";
        else
            return (diffTime / 86400) + "d";
    }
}
