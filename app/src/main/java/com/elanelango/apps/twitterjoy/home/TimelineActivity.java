package com.elanelango.apps.twitterjoy.home;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.elanelango.apps.twitterjoy.R;
import com.elanelango.apps.twitterjoy.TwitterApplication;
import com.elanelango.apps.twitterjoy.TwitterClient;
import com.elanelango.apps.twitterjoy.models.Tweet;
import com.elanelango.apps.twitterjoy.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity implements ComposeDialog.ComposeListener, TwitterClient.TweetsListener {

    private TwitterClient client;

    @Bind(R.id.rvTweets)
    RecyclerView rvTweets;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    ArrayList<Tweet> tweets;
    TweetsAdapter tweetsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                ComposeDialog composeDialog = ComposeDialog.newInstance();
                composeDialog.show(fm, "compose_tweet");
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTweets.setLayoutManager(layoutManager);

        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(tweets);
        rvTweets.setAdapter(tweetsAdapter);

        client = TwitterApplication.getRestClient();
        populateTimeline();

        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Tweet tweet = tweetsAdapter.getLastTweet();
                long lastTweetId = (tweet != null) ? tweet.getId() : 1;
                client.getHomeTweets(0, lastTweetId + 1, TimelineActivity.this);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Tweet tweet = tweetsAdapter.getFirstTweet();
                client.getHomeTweets(tweet.getId(), 0, new TwitterClient.TweetsListener() {
                    @Override
                    public void onTweets(List<Tweet> tweets) {
                        tweetsAdapter.addAllToFront(tweets);
                        swipeContainer.setRefreshing(false);
                        rvTweets.scrollToPosition(0);
                    }

                    @Override
                    public void onError(String errorText) {

                    }
                });
            }
        });
    }

    @Override
    public void onTweets(List<Tweet> tweets) {
        Log.e("ELANLOG", tweets.toString());
        tweetsAdapter.addAll(tweets);
    }

    @Override
    public void onError(String errorText) {

    }

    private void populateTimeline() {
        client.getHomeTweets(1, 0, this);
    }

    @Override
    public void onFinishCompose(String tweet) {
        client.postTweet(tweet, new TwitterClient.PostTweetListener() {
            @Override
            public void onSuccess(Tweet postedTweet) {
                tweetsAdapter.add(0, postedTweet);
                rvTweets.scrollToPosition(0);
            }
        });
    }
}
