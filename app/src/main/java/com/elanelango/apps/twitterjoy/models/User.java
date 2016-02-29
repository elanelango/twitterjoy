package com.elanelango.apps.twitterjoy.models;

import org.parceler.Parcel;

/**
 * Created by eelango on 2/20/16.
 */

@Parcel
public class User {
    long id;
    String name;
    String screenName;
    String profileImageUrl;
    int followersCount;
    int friendsCount;
    String description;

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

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public String getDescription() {
        return description;
    }
}
