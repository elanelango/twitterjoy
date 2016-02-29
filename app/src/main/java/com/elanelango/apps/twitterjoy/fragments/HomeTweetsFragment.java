package com.elanelango.apps.twitterjoy.fragments;

import com.elanelango.apps.twitterjoy.TwitterClient;
import com.elanelango.apps.twitterjoy.models.Tweet;

/**
 * Created by eelango on 2/27/16.
 */
public class HomeTweetsFragment extends TweetsListFragment {
    @Override
    protected void getInitialTweets(TwitterClient.TweetsListener listener) {
        client.getHomeTweets(1, 0, listener);
    }

    @Override
    public void getOlderTweets(TwitterClient.TweetsListener listener) {
        Tweet tweet = tweetsAdapter.getLastTweet();
        long lastTweetId = (tweet != null) ? tweet.getId() : 1;
        client.getHomeTweets(0, lastTweetId + 1, listener);
    }

    @Override
    public void getNewerTweets(TwitterClient.TweetsListener listener) {
        Tweet tweet = tweetsAdapter.getFirstTweet();
        client.getHomeTweets(tweet.getId(), 0, listener);
    }
}
