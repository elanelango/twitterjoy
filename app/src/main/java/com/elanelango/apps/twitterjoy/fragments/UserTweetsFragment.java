package com.elanelango.apps.twitterjoy.fragments;

import android.os.Bundle;

import com.elanelango.apps.twitterjoy.TwitterClient;
import com.elanelango.apps.twitterjoy.models.Tweet;

/**
 * Created by eelango on 2/28/16.
 */
public class UserTweetsFragment extends TweetsListFragment {

    public static UserTweetsFragment newInstance(String screenName) {
        UserTweetsFragment utFragment = new UserTweetsFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        utFragment.setArguments(args);
        return utFragment;
    }

    @Override
    protected void getInitialTweets(TwitterClient.TweetsListener listener) {
        String screenName = getArguments().getString("screen_name");
        client.getUserTimeline(screenName, 1, 0, listener);
    }

    @Override
    protected void getOlderTweets(TwitterClient.TweetsListener listener) {
        String screenName = getArguments().getString("screen_name");
        Tweet tweet = tweetsAdapter.getLastTweet();
        long lastTweetId = (tweet != null) ? tweet.getId() : 1;
        client.getUserTimeline(screenName, 0, lastTweetId + 1, listener);
    }

    @Override
    protected void getNewerTweets(TwitterClient.TweetsListener listener) {
        String screenName = getArguments().getString("screen_name");
        Tweet tweet = tweetsAdapter.getFirstTweet();
        client.getUserTimeline(screenName, tweet.getId(), 0, listener);
    }
}
