package com.elanelango.apps.twitterjoy.models;

/**
 * Created by eelango on 2/20/16.
 */
public class User {
    long id;
    String name;
    String screenName;
    String profileImageUrl;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
