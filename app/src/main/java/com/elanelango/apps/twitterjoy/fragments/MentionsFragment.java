package com.elanelango.apps.twitterjoy.fragments;

import com.elanelango.apps.twitterjoy.TwitterClient;
import com.elanelango.apps.twitterjoy.models.Tweet;

/**
 * Created by eelango on 2/28/16.
 */
public class MentionsFragment extends TweetsListFragment {

    @Override
    protected void getInitialTweets(TwitterClient.TweetsListener listener) {
        // client.getMentions(1, 0, listener);
    }

    @Override
    protected void getOlderTweets(TwitterClient.TweetsListener listener) {
        Tweet tweet = tweetsAdapter.getLastTweet();
        long lastTweetId = (tweet != null) ? tweet.getId() : 1;
        // client.getMentions(0, lastTweetId + 1, listener);
    }

    @Override
    protected void getNewerTweets(TwitterClient.TweetsListener listener) {
        Tweet tweet = tweetsAdapter.getFirstTweet();
        // client.getMentions(tweet.getId(), 0, listener);
    }
}
